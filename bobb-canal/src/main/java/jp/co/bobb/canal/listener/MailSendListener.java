package jp.co.bobb.canal.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.canal.service.MailService;
import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/22
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.MAIL_SEND_QUEUE)
public class MailSendListener {

    @Autowired
    MailService mailService;

    @RabbitHandler
    public void process(JSONObject jsonObject) {
        log.info("mail to {}", jsonObject.getString("to"));
        log.info("mail subject {}", jsonObject.getString("subject"));
        log.info("mail attr {}", jsonObject.getInnerMap());

        mailService.sendSimpleMail(jsonObject.getString("to"),
                jsonObject.getString("subject"),
                jsonObject.getInnerMap());
    }
}
