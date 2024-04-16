package jp.co.bobb.canal.service;

import java.io.File;
import java.util.Map;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/22
 */
public interface MailService {

    /**
     * 文本邮件
     *
     * @param to
     * @param subject
     * @param model
     */
    void sendSimpleMail(String to,
                        String subject,
                        Map model);

    /**
     * 模板邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    void sendTemplateMail(String to,
                          String subject,
                          String content);

    /**
     * 附件邮件
     *
     * @param to
     * @param subject
     * @param model
     * @param inputStream
     */
    void sendAttachmentsMail(String to,
                             String subject,
                             Map model,
                             File inputStream);

    /**
     * 内嵌图片邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param imgPath
     * @param imgId
     */
    void sendInlineResourceMail(String to,
                                String subject,
                                String content,
                                String imgPath,
                                String imgId);
}
