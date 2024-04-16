package jp.co.bobb.message.listener;

import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2021/03/05
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.FX_YX_ABROAD_SMS)
public class FxyxAbroadSMSListener {

    @RabbitHandler
    public void process(String message) {
        log.info(message);
    }
}
