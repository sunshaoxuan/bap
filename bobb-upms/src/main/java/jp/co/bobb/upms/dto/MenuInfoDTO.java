package jp.co.bobb.upms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/01/30
 */
@Data
public class MenuInfoDTO implements Serializable {
    private static final long serialVersionUID = 1779667113067461992L;

    private String id;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单父编码
     */
    private String pCode;

    /**
     * 父菜单ID
     */
    private String pId;

    /**
     * 名称
     */
    private String name;

    /**
     * 请求地址
     */
    private String url;

    private String pageUrl;

    /**
     * 是否是菜单
     */
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
    @ApiModelProperty(value = "菜单图标", name = "icon", example = "", required = true)
    private String icon;

    private String domain;

    private Date createTime;

    private Date updateTime;

    private Long cacheTime;
}
