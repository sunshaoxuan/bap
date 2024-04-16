package jp.co.bobb.oauth.service.impl;

import jp.co.bobb.oauth.service.RoleService;
import jp.co.bobb.common.auth.model.Authorize;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Override
    public Result<List<RoleVO>> getRoleByUserId(Integer userId) {
        log.info("调用{}失败", "getRoleByUserId");
        return Result.failure(100, "调用getRoleByUserId失败");
    }

    @Override
    public Result<Authorize> getAuthorize(Long userId) {
        log.info("调用{}失败", "getAuthorize");
        return Result.failure(100, "调用getAuthorize失败");
    }
}
