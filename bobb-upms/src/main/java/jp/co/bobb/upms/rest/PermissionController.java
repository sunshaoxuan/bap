package jp.co.bobb.upms.rest;

import jp.co.bobb.common.vo.Result;
import jp.co.bobb.upms.entity.RcMenu;
import jp.co.bobb.upms.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Parker Sun
 */
@Slf4j
@RestController
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("getRolePermission/{roleId}")
    public Result getRolePermission(@PathVariable("roleId") Integer roleId) {
        List<RcMenu> menuList = permissionService.getPermissionsByRoleId(roleId);
        return Result.ok().setData(menuList);
    }

    @GetMapping("getMenuByUserId/{userId}")
    public Result getMenuByUserId(@PathVariable("userId") Integer userId) {
        List<RcMenu> menuList = permissionService.getMenuByUserId(userId);
        return Result.ok().setData(menuList);
    }
}
