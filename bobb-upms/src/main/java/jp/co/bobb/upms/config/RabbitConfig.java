package jp.co.bobb.upms.config;

import jp.co.bobb.common.constant.RabbitQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/11/02
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue memberInsert() {
        return new Queue(RabbitQueue.VIP_MEMBER_QUEUE_INSERT);
    }

    @Bean
    public Queue memberUpdate() {
        return new Queue(RabbitQueue.VIP_MEMBER_QUEUE_UPDATE);
    }

    @Bean
    public Queue owner() {
        return new Queue(RabbitQueue.SEND_MEMBER_OWNER);
    }

    @Bean
    public Queue newOwner() {
        return new Queue(RabbitQueue.NEW_OWNER_QUEUE);
    }

    @Bean
    public Queue loginConfirmCode() {
        return new Queue(RabbitQueue.LOGIN_CODE_CONFIRM_QUEUE);
    }

    @Bean
    public Queue loginLogQueue() {
        return new Queue(RabbitQueue.MEMBER_LOGIN_LOG_QUEUE);
    }
}
