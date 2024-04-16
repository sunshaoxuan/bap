package jp.co.bobb.canal.task;

import jp.co.bobb.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/11/13
 */
@Component
@Slf4j
public class BankTradeSyncTask {

    @Autowired
    AmqpTemplate amqpTemplate;

    // risona数据同步任务
    String RISONA_BANK_CRAW_SYNC_01_TASK_QUEUE = "RISONA_BANK_CRAW_SYNC_01.QUEUE";
    String RISONA_BANK_CRAW_SYNC_02_TASK_QUEUE = "RISONA_BANK_CRAW_SYNC_02.QUEUE";

    /**
     * risona数据同步时间 每天10点同步上一次同步的时间+1 到昨天的数据
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void risonaSync01() {
        log.info("start");
        amqpTemplate.convertAndSend(RISONA_BANK_CRAW_SYNC_01_TASK_QUEUE, DateUtil.getNowLocalDate());
    }

    @Scheduled(cron = "0 0 11 * * ?")
    public void risonaSync02() {
        log.info("start");
        amqpTemplate.convertAndSend(RISONA_BANK_CRAW_SYNC_02_TASK_QUEUE, DateUtil.getNowLocalDate());
    }
}
