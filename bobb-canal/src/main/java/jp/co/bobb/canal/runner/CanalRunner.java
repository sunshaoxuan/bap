package jp.co.bobb.canal.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import jp.co.bobb.canal.config.CanalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/29
 */
@Slf4j
public class CanalRunner {

    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");
    /**
     * 启动次数
     */
    private static int runners = 0;
    private AmqpTemplate rabbitTemplate;
    private String database;

    public static int getRunners() {
        return runners;
    }

    /**
     * 重启
     */
    public static void restart(AmqpTemplate rabbitTemplate, String database) {
        CanalRunner canalRunner = new CanalRunner();
        canalRunner.rabbitTemplate = rabbitTemplate;
        canalRunner.database = database;
        canalRunner.starter();
    }

    /**
     * 下划线转驼峰
     *
     * @return string
     */
    private static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private void starter() {
        // 使用固定线程池操作
        ExecutorService executorService = newFixedThreadPool(3);
        executorService.execute(() -> run());
    }

    /**
     * 启动canal调用
     */
    private void run() {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(CanalConfig.host, CanalConfig.port),
                CanalConfig.instance,
                "",
                ""
        );
        try {
            connector.connect();
            connector.subscribe(database + ".bobb_owner_withdraw,"
                    + database + ".bobb_sale_payment,"
                    + database + ".bobb_sale_order,"
                    + database + ".bobb_finance,"
                    + database + ".bobb_trust_order,"
                    + database + ".bobb_trust_order_whole,"
                    + database + ".bobb_owner,"
                    + database + ".bobb_finance_header");
            connector.rollback();
            ++runners;
            Message message;
            while (true) {
                // 获取批量数据
                message = connector.getWithoutAck(CanalConfig.batchSize);
                if (message.getId() == -1 || CollectionUtils.isEmpty(message.getEntries())) {
                    try {
                        Thread.sleep(CanalConfig.sleep);
                    } catch (InterruptedException e) {
                        log.error("Thread sleep exception", e);
                    }
                } else {
                    log.info(message.toString());
                    parseEntryList(message.getEntries());
                }
                // 确认
                connector.ack(message.getId());
            }
        } finally {
            if (runners > 0) {
                --runners;
            }
            log.error("connect canal error");
            connector.disconnect();
        }
    }

    /**
     * 解析数据
     *
     * @param list
     */
    private void parseEntryList(List<CanalEntry.Entry> list) {
        list.forEach(this::parseEntry);
    }

    /**
     * 解析单个事件
     *
     * @param entry
     */
    private void parseEntry(CanalEntry.Entry entry) {
        if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
            return;
        }

        CanalEntry.RowChange rowChange;
        try {
            rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            throw new RuntimeException("ERROR ## parser has an error , data:" + entry.toString(), e);
        }

        //单条 binlog sql
        CanalEntry.EventType eventType = rowChange.getEventType();
        log.info("*****************开始解析->binlog[{}:{}],name[{},{}],eventType:{}*****************",
                entry.getHeader().getLogfileName(),
                entry.getHeader().getLogfileOffset(),
                entry.getHeader().getSchemaName(),
                entry.getHeader().getTableName(),
                eventType
        );
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            parseRowData(entry.getHeader(), eventType, rowData);
        }
        log.info("*****************解析结束->binlog[{}:{}],name[{},{}],eventType:{}*****************",
                entry.getHeader().getLogfileName(),
                entry.getHeader().getLogfileOffset(),
                entry.getHeader().getSchemaName(),
                entry.getHeader().getTableName(),
                eventType
        );
    }

    /**
     * 解析单行SQL数据
     *
     * @param header    头
     * @param eventType 事件类型
     * @param rowData   行数据
     */
    private void parseRowData(CanalEntry.Header header, CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //String tableName = header.getSchemaName() + "." + header.getTableName();
        String tableName = header.getTableName();
        if (eventType == CanalEntry.EventType.DELETE) {
            saveRowData(tableName + ".DELETE", rowData.getBeforeColumnsList());
        } else if (eventType == CanalEntry.EventType.INSERT) {
            saveRowData(tableName + ".INSERT", rowData.getAfterColumnsList());
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            saveRowData(tableName + ".UPDATE", rowData.getAfterColumnsList());
        }
    }

    /**
     * 保存行数据
     *
     * @param tableEventType 表数据事件类型
     * @param columns        表字段数据
     */
    private void saveRowData(String tableEventType, List<CanalEntry.Column> columns) {
        Map map = new HashMap();
        columns.forEach(column -> map.put(lineToHump(column.getName()), column.getValue()));
        log.info("tableEventType:{},json解析:{}", tableEventType, JSON.toJSON(map));
        //if (tableEventType.startsWith("canal")) {
        log.info("send to mq,queue:{}", tableEventType.toUpperCase());

        rabbitTemplate.convertAndSend(tableEventType.toUpperCase(), JSON.toJSONString(map));
        //}
    }
}
