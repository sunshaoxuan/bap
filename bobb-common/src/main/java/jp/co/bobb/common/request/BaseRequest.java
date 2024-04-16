package jp.co.bobb.common.request;

import lombok.Data;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/06/08
 */
@Data
public class BaseRequest {
    private String framework_admin;
    private Integer framework_user_id;
    private String framework_user_name;
    private String framework_nick_name;
    private Integer cooperatorId;
}
