package jp.co.bobb.common.constant;

/**
 * 20 普通黑卡 21超级 22璀璨 23 至尊 10 普通
 *
 * @author Parker Sun
 */
public enum MemberTitle {
    GENERAL_BLACK(20, "TRADER", "普通黑卡"),
    SUPER_BLACK(21, "BOSS", "超级黑卡"),
    TOP_BLACK(22, "CAPITALIST", "璀璨黑卡"),
    KING_BLACK(23, "PREDATOR", "至尊黑卡"),
    GENERAL_MEMBER(10, "FARMER", "普通会员");
    private Integer level;
    private String code;
    private String desc;

    MemberTitle(Integer level, String code, String desc) {
        this.level = level;
        this.desc = desc;
        this.code = code;
    }

    public static String getDescByValue(Integer value) {
        MemberTitle[] values = MemberTitle.values();
        for (MemberTitle memberTitle : values) {
            if (memberTitle.level.equals(value)) {
                return memberTitle.desc;
            }
        }
        return GENERAL_MEMBER.desc;
    }

    public static MemberTitle getByCode(String code) {
        MemberTitle[] values = MemberTitle.values();
        for (MemberTitle memberTitle : values) {
            if (memberTitle.code.equalsIgnoreCase(code)) {
                return memberTitle;
            }
        }
        return null;
    }

    public Integer getLevel() {
        return level;
    }
}
