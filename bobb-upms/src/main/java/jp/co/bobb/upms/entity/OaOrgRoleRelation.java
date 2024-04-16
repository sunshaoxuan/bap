package jp.co.bobb.upms.entity;

import lombok.Data;

/**
 * @author wenyang.diao
 * 2022/04/07
 */
@Data
public class OaOrgRoleRelation {

    private Long id;

    private Long orgId;

    private Long roleId;

    private String orgName;

    private String orgValue;

    private String orgTips;

    private String roleName;

    private String roleValue;

    private String roleTips;

}
