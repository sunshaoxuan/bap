package jp.co.bobb.upms.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jp.co.bobb.upms.dto.MenuInfoDTO;
import jp.co.bobb.upms.entity.RcMenu;
import jp.co.bobb.upms.entity.RcRole;
import jp.co.bobb.upms.entity.RcRoleMenu;
import jp.co.bobb.upms.mapper.RcMenuMapper;
import jp.co.bobb.upms.mapper.RcRoleMapper;
import jp.co.bobb.upms.mapper.RcRoleMenuMapper;
import jp.co.bobb.upms.mapper.RcUserRoleMapper;
import jp.co.bobb.upms.service.MenuService;
import jp.co.bobb.upms.vo.MenuVo;
import jp.co.bobb.common.constant.RoleEnum;
import jp.co.bobb.common.util.DateUtil;
import jp.co.bobb.common.util.SnowflakeIdFactory;
import jp.co.bobb.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/03/19
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    RcMenuMapper menuMapper;

    @Autowired
    RcUserRoleMapper rcUserRoleMapper;

    @Autowired
    RcRoleMapper rcRoleMapper;

    @Autowired
    RcRoleMenuMapper rcRoleMenuMapper;

    @Autowired
    SnowflakeIdFactory snowflakeIdFactory;

    @Override
    public Result queryMenus(String pId, Integer userId, String system, String domain) {
        log.info("pid {},userId {}", pId, userId);
        Result result = Result.ok();
        RcMenu rm = menuMapper.selectById(pId);
        List<Long> roleIds = Lists.newArrayList();
        if (null != userId) {
            roleIds = menuMapper.getRoleIdsByUserId(userId);
        }
        log.info("{}", JSON.toJSONString(rm));
        if (rm.getLevel() > 1 || rm.getLevel() == 0) {
            result.setData(selectMenus(pId, roleIds, null, domain));
        } else {
            result.setData(selectMenus(pId, roleIds, system, domain));
        }
        //log.info(JSON.toJSONString(result.getData()));
        return result;
    }

    private List<MenuVo> selectMenus(String pId, List<Long> roleIds, String system, String domain) {
        List<RcMenu> menuList = menuMapper.queryChildMenusByRoles(pId, roleIds, system, domain);
        List<MenuVo> menuVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(menuList)) {
            return menuVoList;
        }
        menuList.forEach(menu -> {
            MenuVo vo = new MenuVo();
            BeanUtils.copyProperties(menu, vo);
            menuVoList.add(vo);
            if (menu.getLevel() == 1) {
                vo.setChildren(selectMenus(vo.getId(), roleIds, system, domain));
            } else {
                vo.setChildren(selectMenus(vo.getId(), roleIds, null, domain));
            }
        });
        return menuVoList;
    }

    private List<MenuVo> selectMenus(String pId, Integer userId, String system) {
        List<RcMenu> menuList = menuMapper.queryChildMenus(pId, userId, system);
        List<MenuVo> menuVoList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(menuList)) {
            return menuVoList;
        }
        menuList.forEach(menu -> {
            MenuVo vo = new MenuVo();
            BeanUtils.copyProperties(menu, vo);
            menuVoList.add(vo);
            if (menu.getLevel() == 1) {
                vo.setChildren(selectMenus(vo.getId(), userId, system));
            } else {
                vo.setChildren(selectMenus(vo.getId(), userId, null));
            }
        });
        return menuVoList;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result newMenu(MenuInfoDTO menuDTO) {
        RcMenu rcMenu = new RcMenu();
        BeanUtils.copyProperties(menuDTO, rcMenu);
        rcMenu.setId(snowflakeIdFactory.nextId() + "");
        rcMenu.setSort(RandomUtils.nextInt());
        RcMenu pMenu = menuMapper.selectById(menuDTO.getPId());
        if (StringUtils.isBlank(rcMenu.getDomain())) {
            rcMenu.setDomain(pMenu.getDomain());
        }
        if (StringUtils.isBlank(menuDTO.getSystem()) && StringUtils.isNotBlank(menuDTO.getPId())) {
            if (pMenu != null) {
                if (StringUtils.isNotBlank(pMenu.getSystem())) rcMenu.setSystem(pMenu.getSystem());
            }
        }
        int row = menuMapper.insertMenu(rcMenu);
        if (row > 0) {
            RcRole rcRole = rcRoleMapper.getRoleByValue(RoleEnum.ADMIN_ROLE.getValue());
            RcRoleMenu rcRoleMenu = new RcRoleMenu();
            rcRoleMenu.setCreateTime(DateUtil.getNowDate());
            rcRoleMenu.setMenuId(rcMenu.getId());
            rcRoleMenu.setRoleId(Long.valueOf(rcRole.getId()));
            rcRoleMenuMapper.insertBean(rcRoleMenu);
            return Result.ok();
        } else {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "save failure");
        }
    }

    @Override
    public Result updateMenu(MenuInfoDTO menuDTO) {
        RcMenu rcMenu = new RcMenu();
        BeanUtils.copyProperties(menuDTO, rcMenu);
        int row = menuMapper.updateByIdSelective(rcMenu);
        if (row > 0) {
            return Result.ok();
        } else {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "save failure");
        }
    }

    @Override
    public Result queryMenu(String id) {
        RcMenu rcMenu = menuMapper.queryRcMenu(id);
        MenuVo vo = new MenuVo();
        BeanUtils.copyProperties(rcMenu, vo);
        Result result = Result.ok();
        result.setData(vo);
        return result;
    }

    @Override
    @Transactional
    public Result deleteMenu(String id) {
        log.info("delete menu's id {}", id);
        Set<String> idSet = Sets.newHashSet();
        idSet.add(id);
        List<RcMenu> childMenuList = menuMapper.queryMenusByParendIds(idSet);

        if (!CollectionUtils.isEmpty(childMenuList)) {
            Set<String> pIdSet = childMenuList.stream().map(RcMenu::getId).collect(Collectors.toSet());
            childMenuList = menuMapper.queryMenusByParendIds(pIdSet);
            if (!CollectionUtils.isEmpty(childMenuList)) {
                childMenuList.stream().map(RcMenu::getId).forEach(pIdSet::add);
            }
            menuMapper.deleteMenuBatch(pIdSet);

            pIdSet.add(id);
            String sb = pIdSet.stream().map(bean -> "," + bean).collect(Collectors.joining());
            rcUserRoleMapper.deleteBatchByMenuIds(sb.substring(1));
        }

        menuMapper.deleteMenu(id);

        return Result.ok();
    }
}
