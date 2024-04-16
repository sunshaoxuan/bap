package jp.co.bobb.common.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
public class SysUserAuthentication implements Serializable {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String status;

    private String name;

    private String type;

    private Integer memberLevel;
}
