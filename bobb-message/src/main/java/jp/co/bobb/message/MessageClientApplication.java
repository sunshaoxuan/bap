package jp.co.bobb.message;

import jp.co.bobb.common.constant.RabbitQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/15
 */
@SpringBootApplication
@EnableScheduling
@EnableRabbit
public class MessageClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageClientApplication.class, args);
    }

    @Bean
    public Queue loginCode() {
        return new Queue(RabbitQueue.LOGIN_CODE_QUEUE);
    }
}
