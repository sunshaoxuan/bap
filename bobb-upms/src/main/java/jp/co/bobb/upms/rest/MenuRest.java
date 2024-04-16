package jp.co.bobb.upms.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jp.co.bobb.common.constant.FrameworkConstant;
import jp.co.bobb.common.constant.SecurityConstants;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.upms.dto.MenuInfoDTO;
import jp.co.bobb.upms.service.MenuService;
import jp.co.bobb.upms.vo.MenuVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/03/19
 */
@Slf4j
@Api(value = "メニューのrest", tags = {"メニュー"})
@RestController
@RequestMapping("menu")
public class MenuRest {

    @Autowired
    MenuService menuService;


    /**
     * 　メニューList
     *
     * @param parentId
     * @return
     */
    @ApiOperation(value = "list", notes = "list")
    @ApiImplicitParam(name = "parentId", value = "上部メニューのID", required = true, dataType = "String", paramType = "query")
    @GetMapping("list")
    public Result menuList(@Param("parentId") String parentId, @Param("domain") Boolean domainExist, HttpServletRequest request) {
        if (StringUtils.isEmpty(parentId)) {
            parentId = "000000000000000000";
        }
        String userId = request.getParameter(FrameworkConstant.FRAMEWORK_LOGIN_USER_ID_CONSTANT);
        String adminUser = request.getParameter(FrameworkConstant.FRAMEWORK_LOGIN_ADMIN_CONSTANT);
        String domainStr = null;
        domainExist = domainExist == null || domainExist;
        if (domainExist) {
            domainStr = request.getHeader(SecurityConstants.HTTP_SERVER_DOMAIN_NAME);
            String smartwe = "admin-waiter.smartwe.co.jp";
            String oa = "oa.bobb.com";
            String manage = "admin.bobb.com";
            if (domainStr.endsWith(smartwe)) {
                domainStr = smartwe;
            } else if (domainStr.endsWith(oa)) {
                domainStr = oa;
            } else if (domainStr.endsWith(manage)) {
                domainStr = manage;
            } else {
                return Result.failure(100, "域名非法");
            }
        }
        log.info("system:{}", request.getParameter("system"));
        if (Boolean.parseBoolean(adminUser)) {
            userId = null;
        }
        Result result = menuService.queryMenus(parentId, userId == null ? null : Integer.parseInt(userId), request.getParameter("system"), domainStr);
        log.info(" menu linst out {}", result);
        return result;
    }

    /**
     * メニューを問合せる
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "get", notes = "get")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String", paramType = "query")
    @GetMapping("get")
    public Result menu(@Param("id") String id) {
        return menuService.queryMenu(id);
    }

    /**
     * メニューを保存する
     *
     * @param menuVo
     * @return
     */
    @ApiOperation(value = "new", notes = "new")
    @PostMapping("new")
    public Result newMenu(@RequestBody MenuVo menuVo) {
        Integer level = menuVo.getLevel();
        if (level == 1 && StringUtils.isEmpty(menuVo.getDomain())) {
            return Result.failure(100, "一级菜单必须添加所属域名");
        }
        MenuInfoDTO menuInfoDTO = new MenuInfoDTO();
        BeanUtils.copyProperties(menuVo, menuInfoDTO);
        return menuService.newMenu(menuInfoDTO);
    }

    /**
     * メニューをなおしする
     *
     * @param menuVo
     * @return
     */
    @ApiOperation(value = "update", notes = "update")
    @PutMapping("update")
    public Result updateMenu(@Valid @RequestBody MenuVo menuVo) {
        MenuInfoDTO menuInfoDTO = new MenuInfoDTO();
        BeanUtils.copyProperties(menuVo, menuInfoDTO);
        return menuService.updateMenu(menuInfoDTO);
    }

    /**
     * 　メニューを削除する
     */
    @ApiOperation(value = "delete", notes = "delete")
    @DeleteMapping("delete")
    public Result delete(@RequestBody MenuVo menuVo) {
        if (StringUtils.isEmpty(menuVo.getId())) {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "最初にデータを選択してください");
        }
        return menuService.deleteMenu(menuVo.getId());
    }
}
