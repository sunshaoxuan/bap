package jp.co.bobb.common.constant;

/**
 * @author Parker Sun
 */

public enum RoleEnum {
    GENERAL_MEMEBR_ROLE("member", "普通会员"), OWNER_MEMBER_ROLE("owner", "房主"), ADMIN_ROLE("admin", "管理员");
    private String value;
    private String desc;

    RoleEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
