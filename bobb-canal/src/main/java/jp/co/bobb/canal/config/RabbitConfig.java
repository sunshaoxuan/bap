package jp.co.bobb.canal.config;

import jp.co.bobb.common.constant.RabbitQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/22
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue mail() {
        return new Queue(RabbitQueue.MAIL_SEND_QUEUE);
    }

    @Bean
    public Queue job() {
        return new Queue(RabbitQueue.MAIL_JOB_SEND_QUEUE);
    }

    @Bean
    public Queue fx_yx_dd_robot() {
        return new Queue(RabbitQueue.FX_YX_DING_DING_ROBOT);
    }

    @Bean
    public Queue abroad_sms() {
        return new Queue(RabbitQueue.FX_YX_ABROAD_SMS);
    }
}
