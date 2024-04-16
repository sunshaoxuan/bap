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
 * @date 2019/06/12
 */
@Component
@Slf4j
public class LongRentRenewTask {

    @Autowired
    AmqpTemplate amqpTemplate;

    String BANK_ACCOUNT_COUNT_QUEUE = "LONG.RENT.RENEW.QUEUE";

    /**
     * 长租订单续签提醒
     */
    //@Scheduled(cron = "0 0 10 * * ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(BANK_ACCOUNT_COUNT_QUEUE, "1");
    }
}
