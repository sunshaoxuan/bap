package jp.co.bobb.common.upms.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/12
 */
@Data
public class UserDTO implements Serializable {
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
}
