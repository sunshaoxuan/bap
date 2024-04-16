package jp.co.bobb.upms.service.impl;

import jp.co.bobb.upms.entity.RcMenu;
import jp.co.bobb.upms.entity.RcPermission;
import jp.co.bobb.upms.mapper.RcMenuMapper;
import jp.co.bobb.upms.mapper.RcUserRoleMapper;
import jp.co.bobb.upms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Parker Sun
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    RcUserRoleMapper rcUserRoleMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private RcMenuMapper menuMapper;

    @Override
    public List<RcMenu> getPermissionsByRoleId(Integer roleId) {
        return menuMapper.getPermissionsByRoleId(roleId);
    }

    @Override
    public void insertUserOwnerPermission(Integer userId, Integer roleId) {
        rcUserRoleMapper.insertPermission(userId, roleId);
    }

    @Override
    public List<RcPermission> getPermissionsByUserId(Integer userId) {
        return rcUserRoleMapper.getPermissionsByUserId(userId);
    }

    @Override
    public List<RcMenu> getMenuByUserId(Integer userId) {
        if (userId.intValue() < 0) {
            return rcUserRoleMapper.getAdminMenu();
        }
        return rcUserRoleMapper.getMenuByUserId(userId);
    }
}
