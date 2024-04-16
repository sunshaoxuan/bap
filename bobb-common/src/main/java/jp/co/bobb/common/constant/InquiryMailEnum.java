package jp.co.bobb.common.constant;

/**
 * @author Parker Sun
 */
public enum InquiryMailEnum {
    MANAGEMENT_DEP("pm@bobb.com", "不动产管理部", "PM_FX"),
    CAMP_DEP("plan-japan@bobb.com", "营缮部", "EIGYO_FX"),
    SALE_DEP("sales@bobb.com", "营业部", "PLAN-JAPAN_FX"),
    HOTEL_DEP("info@travel.bobb.com", "旅馆部", "HOTEL_FX"),
    OTHER_DEP("info@bobb.com", "其它", "INFO_FX");
    private String mail;
    private String desc;
    private String code;

    InquiryMailEnum(String mail, String desc, String code) {
        this.mail = mail;
        this.code = code;
        this.desc = desc;
    }

    public static InquiryMailEnum getMailByDesc(String value) {
        InquiryMailEnum[] values = InquiryMailEnum.values();
        for (InquiryMailEnum inquiryMailEnum : values) {
            if (inquiryMailEnum.code.equals(value)) {
                return inquiryMailEnum;
            }
        }
        return null;
    }

    public String getMail() {
        return mail;
    }

    public String getDesc() {
        return desc;
    }
}
