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
 * @date 2019/08/01
 */
@Component
@Slf4j
public class AccountHistoryMailTask {
    @Autowired
    AmqpTemplate amqpTemplate;

    String ACCOUNT_HISTORY_TASK_QUEUE = "ACCOUNT.HISTORY.TASK.QUEUE";

    /**
     * 账户数据通知财务部（邮件）
     */
    @Scheduled(cron = "0 0 10 * * ?")
    //@Scheduled(cron = "0 */5 * * * ?")
    public void healthCheck() {
        amqpTemplate.convertAndSend(ACCOUNT_HISTORY_TASK_QUEUE, "1");
    }
}
