package jp.co.bobb.common.constant;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/11/02
 */
public interface RabbitQueue {
    /**
     * 会员表canal监听MQ队列定义
     */
    String VIP_MEMBER_QUEUE_UPDATE = "VIP_MEMBER.UPDATE";
    /**
     * 会员表canal监听MQ队列定义
     */
    String VIP_MEMBER_QUEUE_INSERT = "VIP_MEMBER.INSERT";

    String QUERY_MEMBER_OWNER = "QUERY.MEMBER.OWNER";

    String SEND_MEMBER_OWNER = "SEND.MEMBER.OWNER";

    String NEW_OWNER_QUEUE = "bobb_OWNER.INSERT";

    /**
     * 登录验证码
     */
    String LOGIN_CODE_QUEUE = "SEND.LOGIN.CODE";

    /**
     * 邮件登录验证码
     */
    String EMAIL_LOGIN_CODE_QUEUE = "EMAIL.SEND.LOGIN.CODE";

    /**
     * google chat登录验证码
     */
    String GOOGLE_CHAT_LOGIN_CODE_QUEUE = "GOOGLE.CHAT.SEND.LOGIN.CODE";

    /**
     * 消息通知
     */
    String NOTICE_QUEUE = "SEND.NOTICE.CODE";

    /**
     * 登录验证码确认发送
     */
    String LOGIN_CODE_CONFIRM_QUEUE = "SEND.LOGIN.CONFIRM.CODE";

    String ABROAD_LOGIN_CODE_QUEUE = "ABROAD.LOGIN.CODE";

    /**
     * 发送邮件
     */
    String MAIL_SEND_QUEUE = "MAIL.SEND.QUEUE";

    /**
     * 发送邮件
     */
    String MAIL_JOB_SEND_QUEUE = "MAIL.JOB.SEND.QUEUE";

    /**
     * 客服人员定时从会话中退出的任务
     */
    String CUSTOM_SERVICE_QUIT_CONVERSATION = "CUSTOM.SERVICE.QUIT.CONVERSATION";

    /**
     * 会员登录日志记录
     */
    String MEMBER_LOGIN_LOG_QUEUE = "MEMBER.LOGIN.LOG.QUEUE";

    String FX_YX_DING_DING_ROBOT = "FX.YX.DING.DING.ROBOT";

    String FX_YX_ABROAD_SMS = "FX.YX.ABROAD.SMS";
}
