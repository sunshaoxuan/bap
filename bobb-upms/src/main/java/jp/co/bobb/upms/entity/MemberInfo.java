package jp.co.bobb.upms.entity;

import java.io.Serializable;
import java.util.Date;

public class MemberInfo implements Serializable {
    private static final long serialVersionUID = -6692969787282953218L;
    private Integer userId;

    private Short level;

    private Date updateTime;

    private String nameJp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNameJp() {
        return nameJp;
    }

    public void setNameJp(String nameJp) {
        this.nameJp = nameJp == null ? null : nameJp.trim();
    }
}