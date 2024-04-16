package jp.co.bobb.canal.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe: 结算提醒
 *
 * @author Parker Sun
 * @date 2019/05/30
 */
//@Component
//@Slf4j
public class OwnerSettlementNoticeTask {


    //@Autowired
    AmqpTemplate amqpTemplate;

    String BANK_ACCOUNT_COUNT_QUEUE = "OWNER.SETTLEMENT.QUEUE";

    //@Scheduled(cron = "0 0 9 * * ?")
    public void task() {
        amqpTemplate.convertAndSend(BANK_ACCOUNT_COUNT_QUEUE, 1);
    }
}
