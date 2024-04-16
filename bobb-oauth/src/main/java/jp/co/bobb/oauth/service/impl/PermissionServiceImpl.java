package jp.co.bobb.oauth.service.impl;

import jp.co.bobb.oauth.service.PermissionService;
import jp.co.bobb.common.vo.MenuVO;
import jp.co.bobb.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {
    @Override
    public Result<List<MenuVO>> getRolePermission(Integer roleId) {
        log.info("调用{}失败", "getRolePermission");
        return Result.failure(100, "调用getRolePermission失败");
    }
}
