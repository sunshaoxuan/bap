package jp.co.bobb.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVO {
    private String id;
    private String code;
    private String pCode;
    private String pId;
    private String name;
    private String url;
    private Integer isMenu;
    private Integer level;
    private Integer sort;
    private Integer status;
    private String icon;
    private Date createTime;
    private Date updateTime;
}
