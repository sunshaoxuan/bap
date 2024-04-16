package jp.co.bobb.canal.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/27
 */
@Component
@Slf4j
public class TaxTask {
    @Autowired
    AmqpTemplate amqpTemplate;

    String TAX_CAL_QUEUE = "TAX.CAL.NEW.QUEUE";

    String TAX_CAL_S3_UPDATE = "TAX.CAL.APPLY.EXTRA.S3.PUBLIC.LINK.UPDATE.QUEUE";

    /**
     * 健康检查  canal重启 当机 异常重启时 canal消费者定时任务的方式保证消费继续进行
     */
    @Scheduled(cron = "0 0 23 31 1 ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(TAX_CAL_QUEUE, "1");
    }

    //@Scheduled(cron = "0 0 6 * * ?")
    public void taxS3LinkUpdate() {
        amqpTemplate.convertAndSend(TAX_CAL_S3_UPDATE, "1");
    }
}
