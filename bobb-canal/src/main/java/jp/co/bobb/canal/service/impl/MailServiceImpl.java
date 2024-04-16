package jp.co.bobb.canal.service.impl;

import jp.co.bobb.canal.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/22
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String mailUserName;

    @Autowired
    TemplateEngine templateEngine;

    @Override
    public void sendSimpleMail(String to, String subject, Map model) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailUserName);
            helper.setTo(to);
            helper.setSubject(subject);
            Context contextText = new Context();
            contextText.setVariables(model);
            String mailContext = this.templateEngine.process("simple", contextText);
            helper.setText(mailContext, true);
            mailSender.send(mimeMessage);
            log.info("simple mail had send");
        } catch (Exception e) {
            log.error("simple mail exception", e);
        }
    }

    @Override
    public void sendTemplateMail(String to, String subject, String content) {

    }

    @Override
    public void sendAttachmentsMail(String to, String subject, Map model, File inputStream) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailUserName);
            helper.setTo(to);
            helper.setSubject(subject);
            Context contextText = new Context();
            contextText.setVariables(model);
            String mailContext = this.templateEngine.process("job", contextText);
            helper.setText(mailContext, true);
            helper.addAttachment(model.get("fileName").toString(), inputStream);
            mailSender.send(mimeMessage);
            log.info("simple mail had send");
        } catch (Exception e) {
            log.error("simple mail exception", e);
        } finally {
            if (inputStream.exists()) {
                inputStream.delete();
            }
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String imgPath, String imgId) {

    }
}
