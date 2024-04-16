package jp.co.bobb.canal.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * describe: 口座番号監視
 *
 * @author Parker Sun
 * @date 2019/03/18
 */
@Component
@Slf4j
public class KouZaBanGouTask {
    @Autowired
    AmqpTemplate amqpTemplate;

    String BANK_ACCOUNT_COUNT_QUEUE = "BANK.ACCOUNT.COUNT.QUEUE";

    /**
     * 说明会报名钉钉机器人提示
     */
    @Scheduled(cron = "0 0 16 * * ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(BANK_ACCOUNT_COUNT_QUEUE, "1");
    }
}
