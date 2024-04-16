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
public class RoleInfoDTO implements Serializable {
    private static final long serialVersionUID = 7350142319848406536L;

    private Integer id;

    private String name;

    private String value;

    private String tips;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Long cacheTime;
}
