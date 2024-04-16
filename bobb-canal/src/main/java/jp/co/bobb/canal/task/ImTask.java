package jp.co.bobb.canal.task;

import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * bobb-im里面相关的定时任务
 */
@Slf4j
@Component
public class ImTask {

    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 客服人员定时从会话中退出的任务：
     * 如果当前会话最后一条消息是客服人员发送，并且该消息超过1小时都未得到客户的回复，则该会话为无效会话，客服自动从里面退出。
     * 执行时间：每6小时执行一次
     */
    @Scheduled(cron = "30 45 0/6 * * ?")
    public void quitConversation() {
        amqpTemplate.convertAndSend(RabbitQueue.CUSTOM_SERVICE_QUIT_CONVERSATION, "start");
    }

}
