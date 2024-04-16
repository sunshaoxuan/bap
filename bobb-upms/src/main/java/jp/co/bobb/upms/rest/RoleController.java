package jp.co.bobb.upms.rest;

import jp.co.bobb.common.auth.model.Authorize;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.upms.entity.RcRole;
import jp.co.bobb.upms.service.RoleService;
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
@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("getRoleByUserId/{userId}")
    public Result getRoleByUserId(@PathVariable("userId") Integer userId) {
        List<RcRole> roleList = roleService.getRoleByUserId(userId);
        return Result.ok().setData(roleList);
    }

    @GetMapping("getAuthorize/{userId}")
    public Result getAuthorize(@PathVariable("userId") Integer userId) {
        log.info("userId {}", userId);
        Authorize authorize = roleService.getAuthorize(userId);
        return Result.ok().setData(authorize);
    }

}
