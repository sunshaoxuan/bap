package jp.co.bobb.common.enums;

import lombok.Getter;

@Getter
public enum SMSTemplateEnum {

    CHANGEPHONE_SENGCODE("CHANGEPHONE_SENGCODE", "SMS_195705169", "SMS_195720157", "验证码${code}，您正在变更手机号码，若非本人操作，请忽略。"),
    CHANGEPHONE_REMIND_MANAGE("CHANGEPHONE_REMIND_MANAGE", "SMS_195575129", "SMS_195580140", "客户${name}已经提交变更手机号码的申请，请在5个工作日内登陆小程序“BoBBPlus”在“我的待办”中处理，谢谢！"),
    CHANGEPHONE_REMIND_OWNER_SUCCESS("CHANGEPHONE_REMIND_OWNER_SUCCESS", "SMS_195570156", "SMS_195570155", "您提交的“变更手机号”申请已通过审核，请使用新手机号码登录“BoBBPlus”小程序。"),
    CHANGEPHONE_REMIND_OWNER_CANCEL("CHANGEPHONE_REMIND_OWNER_CANCEL", "SMS_195580147", "SMS_195580146", "您提交的“变更手机号”申请被驳回，请核实信息重新提交申请，如有疑问请联系您的投资顾问，谢谢！"),
    INJAPAN_REMIND_OWNER("INJAPAN_REMIND_OWNER", "SMS_193524191", "SMS_193514154", "尊敬的用户您好，${name}的居住者身份有效期还剩${days}天，请及时在微信小程序上更新，谢谢！");

    private String key;

    private String internationalTemplate;

    private String domesticTemplate;

    private String desc;

    SMSTemplateEnum(String key, String internationalTemplate, String domesticTemplate, String desc) {
        this.key = key;
        this.desc = desc;
        this.internationalTemplate = internationalTemplate;
        this.domesticTemplate = domesticTemplate;
    }
}
