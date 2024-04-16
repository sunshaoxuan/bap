package jp.co.bobb.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO implements Serializable {
    private static final long serialVersionUID = 3881610071550902762L;

    private Integer id;

    private String avatar;

    private String username;

    private String password;

    private String salt;

    private String name;

    private Date birthday;

    private Short intlTelCode;

    private Integer sex;

    private String email;

    private String phone;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer memberId;

    private Integer level;
}
