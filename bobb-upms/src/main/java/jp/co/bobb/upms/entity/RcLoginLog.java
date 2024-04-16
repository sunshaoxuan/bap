package jp.co.bobb.upms.entity;

import lombok.Data;

import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/28
 */
@Data
public class RcLoginLog {
    private long id;
    private Integer userId;
    private Date loginTime;
}
