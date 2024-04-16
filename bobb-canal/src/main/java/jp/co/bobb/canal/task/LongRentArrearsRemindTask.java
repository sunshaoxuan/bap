package jp.co.bobb.canal.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每天凌晨统计最新的欠租信息
 */
@Slf4j
@Component
public class LongRentArrearsRemindTask {
    @Autowired
    AmqpTemplate amqpTemplate;

    String LONG_RENT_ARREARS_REMIND_QUEUE = "LONG_RENT_ARREARS_REMIND.QUEUE";

    @Scheduled(cron = "0 0 7 * * ?")
    public void rentArrearsRmind() {
        amqpTemplate.convertAndSend(LONG_RENT_ARREARS_REMIND_QUEUE, "start");
    }
}
