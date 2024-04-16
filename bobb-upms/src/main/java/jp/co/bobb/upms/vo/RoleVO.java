package jp.co.bobb.upms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/14
 */
@Data
public class RoleVO implements Serializable {
    private static final long serialVersionUID = -4429731901165245867L;
    private Long id;
    @NotNull(message = "役割の名前は必須です")
    private String name;
    @NotNull(message = "役割のvalueは必須です")
    private String value;
    private String tips;
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private Date createTime;
    private Integer status;
}
