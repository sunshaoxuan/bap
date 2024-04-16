package jp.co.bobb.upms.entity;

import lombok.Data;

import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/23
 */
@Data
public class RcRoleMenu {
    private Integer id;
    private Long roleId;
    private String menuId;
    private Date createTime;
    private String createBy;
}
