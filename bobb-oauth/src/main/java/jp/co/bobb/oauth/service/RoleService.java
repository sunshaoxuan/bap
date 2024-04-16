package jp.co.bobb.oauth.service;

import jp.co.bobb.oauth.service.impl.RoleServiceImpl;
import jp.co.bobb.common.auth.model.Authorize;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.RoleVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "bobb-upms", fallback = RoleServiceImpl.class)
public interface RoleService {
    @GetMapping("role/getRoleByUserId/{userId}")
    Result<List<RoleVO>> getRoleByUserId(@PathVariable("userId") Integer userId);


    @GetMapping("role/getAuthorize/{userId}")
    Result<Authorize> getAuthorize(@PathVariable("userId") Long userId);

}
