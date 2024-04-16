package jp.co.bobb.oauth.service;

import jp.co.bobb.oauth.service.impl.PermissionServiceImpl;
import jp.co.bobb.common.vo.MenuVO;
import jp.co.bobb.common.vo.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "bobb-upms", fallback = PermissionServiceImpl.class)
public interface PermissionService {
    @GetMapping("permission/getRolePermission/{roleId}")
    Result<List<MenuVO>> getRolePermission(@PathVariable("roleId") Integer roleId);
}
