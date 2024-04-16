package jp.co.bobb.upms.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "rc_role")
@Data
public class RcRole implements Serializable {
    private static final long serialVersionUID = 8755384345095178029L;
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String name;

    private String value;

    private String tips;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private Integer status;
}