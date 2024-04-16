package jp.co.bobb.upms.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "rc_user")
@Data
public class RcUser implements Serializable {

    private static final long serialVersionUID = -605554740209656967L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String avatar;

    private String username;

    private String password;

    private String salt;

    private String name;

    private Date birthday;

    private Integer sex;

    @Column(name = "intl_tel_code")
    private Short intlTelCode;

    private String email;

    private String phone;

    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "register_time")
    private Long registerTime;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "verification_status")
    private Byte verificationStatus;

    @Column(name = "space_name")
    private String spaceName;
}