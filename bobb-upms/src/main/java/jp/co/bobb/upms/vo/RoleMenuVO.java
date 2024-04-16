package jp.co.bobb.upms.vo;

import lombok.Data;

import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/16
 */
@Data
public class RoleMenuVO extends RoleVO {
    private List<MenuVo> menuVoList;
    private List<String> menuIdList;
}
