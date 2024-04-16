package jp.co.bobb.canal.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每天13点15分扫描所有持主的居住者身份，如果居住者身份即将过期，则进行提醒
 */
@Slf4j
@Component
public class UpdateInJapanInfoTask {
    @Autowired
    AmqpTemplate amqpTemplate;

    String INJAPAN_INFO_UPDATE_QUEUE = "INJAPAN_INFO_UPDATE.QUEUE";

    @Scheduled(cron = "0 15 13 * * ?")
    public void updateInJapanInfo() {
        amqpTemplate.convertAndSend(INJAPAN_INFO_UPDATE_QUEUE, "start");
    }
}
