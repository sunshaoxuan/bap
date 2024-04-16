package jp.co.bobb.upms.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Parker Sun
 */
@Table(name = "rc_menu")
@Data
public class RcMenu implements Serializable {
    private static final long serialVersionUID = 1765868518155207061L;
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单父编码
     */
    @Column(name = "p_code")
    private String pCode;

    /**
     * 父菜单ID
     */
    @Column(name = "p_id")
    private String pId;

    /**
     * 名称
     */
    private String name;

    /**
     * 请求地址
     */
    private String url;

    @Column(name = "page_url")
    private String pageUrl;

    /**
     * 是否是菜单
     */
    @Column(name = "is_menu")
    private Integer isMenu;

    /**
     * 菜单层级
     */
    private Integer level;

    private String system;

    /**
     * 菜单排序
     */
    private Integer sort;

    private Integer status;

    private String icon;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 域名
     */
    @Column(name = "domain")
    private String domain;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RcMenu rcMenu = (RcMenu) o;
        return Objects.equals(id, rcMenu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}