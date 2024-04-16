package jp.co.bobb.canal.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.canal.service.MailService;
import jp.co.bobb.common.constant.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/22
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.MAIL_JOB_SEND_QUEUE)
public class MailAttachSendListener {

    @Autowired
    MailService mailService;

    @Value("${file.upload.path}")
    String path;

    @RabbitHandler
    public void process(JSONObject jsonObject) {
        log.info("mail to {}", jsonObject.getString("to"));
        log.info("mail subject {}", jsonObject.getString("subject"));
        log.info("mail attr {}", jsonObject.getInnerMap());

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = new File(path);
        try {
            fos = new FileOutputStream(file.getPath() + File.separator + jsonObject.getString("fileName"));
            bos = new BufferedOutputStream(fos);
            bos.write(jsonObject.getBytes("file"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("mail attr {}", jsonObject.getInnerMap());
        mailService.sendAttachmentsMail(jsonObject.getString("to"),
                jsonObject.getString("subject"),
                jsonObject.getInnerMap(),
                new File(file.getPath() + File.separator + jsonObject.getString("fileName")));
    }
}
