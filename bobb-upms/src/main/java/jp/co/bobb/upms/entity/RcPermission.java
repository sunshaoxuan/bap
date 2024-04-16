package jp.co.bobb.upms.entity;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/14
 */
@Table(name = "rc_menu")
@Data
public class RcPermission implements Serializable {
    private static final long serialVersionUID = 1605531553065664386L;
    private Long id;
    private Long userId;
    private Long roleId;
}
