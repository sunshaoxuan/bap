package jp.co.bobb.upms.dto;

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
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = -6578085392649955364L;

    private Integer id;

    private String avatar;

    private String username;

    private String password;

    private String salt;

    private String name;

    private Date birthday;

    private Integer sex;

    private String email;

    private String phone;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer memberId;

    private Long cacheTime;
}
