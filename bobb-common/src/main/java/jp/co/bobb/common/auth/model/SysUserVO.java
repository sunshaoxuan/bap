package jp.co.bobb.common.auth.model;

import lombok.Data;

import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
public class SysUserVO {

    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 状态:  Y-可用  N-不可用
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 删除状态: Y-已删除,N-未删除
     */
    private String deleted;
    private String avatar;

    /**
     * 用户类型
     */
    private String type;
}
