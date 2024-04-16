package jp.co.bobb.upms.service;

import jp.co.bobb.upms.entity.RcMenu;
import jp.co.bobb.upms.entity.RcPermission;

import java.util.List;

/**
 * @author Parker Sun
 */
public interface PermissionService {
    /**
     * 查询角色菜单
     *
     * @param roleId
     * @return
     */
    List<RcMenu> getPermissionsByRoleId(Integer roleId);

    /**
     * 新增会员角色
     *
     * @param userId
     * @param roleId
     */
    void insertUserOwnerPermission(Integer userId, Integer roleId);

    /**
     * 查询用户角色
     *
     * @param id
     * @return
     */
    List<RcPermission> getPermissionsByUserId(Integer id);

    /**
     * @param userId
     * @return
     */
    List<RcMenu> getMenuByUserId(Integer userId);
}
