package jp.co.bobb.upms.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.RoleVO;
import jp.co.bobb.upms.dto.RoleInfoDTO;
import jp.co.bobb.upms.service.RoleService;
import jp.co.bobb.upms.vo.WarrantVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/03/25
 */
@Slf4j
@Api(value = "役割のrest", tags = {"役割"})
@RestController
@RequestMapping("role")
public class RoleRest {

    @Autowired
    RoleService roleService;

    @ApiOperation(value = "list", notes = "list")
    @GetMapping("list")
    public Result roleList() {
        return roleService.queryRoleList();
    }


    @ApiOperation(value = "get", notes = "get")
    @ApiImplicitParam(name = "id", value = "role's id", required = true, dataType = "Long", paramType = "query")
    @GetMapping("get")
    public Result role(@Param("id") Long id) {
        return roleService.queryRole(id);
    }

    @ApiOperation(value = "query", notes = "query")
    @ApiImplicitParam(name = "mobile", value = "role's mobile", required = true, dataType = "String", paramType = "query")
    @GetMapping("query")
    public Result query(@Param("mobile") String mobile) {
        return roleService.queryRolesByMobile(mobile);
    }


    @PostMapping("new")
    public Result newRole(@RequestBody RoleVO roleVO) {
        RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
        BeanUtils.copyProperties(roleVO, roleInfoDTO);
        return roleService.newRole(roleInfoDTO);
    }


    @ApiOperation(value = "update", notes = "updateRole")
    @PutMapping("update")
    public Result updateRole(@RequestBody RoleVO roleVO) {
        RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
        BeanUtils.copyProperties(roleVO, roleInfoDTO);
        return roleService.updateRole(roleInfoDTO);
    }

    @ApiOperation(value = "warrent", notes = "warrent")
    @PostMapping("warrant")
    public Result warrent(@RequestBody WarrantVO warrantVO) {
        log.info("warrant {}", warrantVO.toString());
        return roleService.warrent(warrantVO);
    }

    @ApiOperation(value = "warrent", notes = "warrent")
    @DeleteMapping("warrant")
    public Result deleteWarrent(@RequestBody WarrantVO warrantVO) {
        log.info("warrant {}", warrantVO.toString());
        return roleService.deleteWarrent(warrantVO);
    }
}
