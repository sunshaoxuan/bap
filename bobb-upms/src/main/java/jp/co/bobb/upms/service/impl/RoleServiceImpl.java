package jp.co.bobb.upms.service.impl;

import com.google.common.collect.Lists;
import jp.co.bobb.upms.dto.RoleInfoDTO;
import jp.co.bobb.upms.entity.*;
import jp.co.bobb.upms.mapper.RcMenuMapper;
import jp.co.bobb.upms.mapper.RcRoleMapper;
import jp.co.bobb.upms.mapper.RcRoleMenuMapper;
import jp.co.bobb.upms.mapper.RcUserRoleMapper;
import jp.co.bobb.upms.service.OaFeignService;
import jp.co.bobb.upms.service.RoleService;
import jp.co.bobb.upms.vo.RoleMenuVO;
import jp.co.bobb.upms.vo.RoleVO;
import jp.co.bobb.upms.vo.WarrantVO;
import jp.co.bobb.common.auth.model.Authorize;
import jp.co.bobb.common.constant.SecurityConstants;
import jp.co.bobb.common.util.DateUtil;
import jp.co.bobb.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Parker Sun
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    /**
     * 角色树的根节点的 parent_id
     */
    private final static Integer ROOT_ROLE = 0;
    @Autowired
    RcMenuMapper rcMenuMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RcRoleMenuMapper rcRoleMenuMapper;

    @Autowired
    RcUserRoleMapper rcUserRoleMapper;
    @Autowired
    private RcRoleMapper roleMapper;
    @Autowired
    private OaFeignService oaFeignService;

    @Override
    public List<RcRole> getRoleByUserId(Integer userId) {
        return roleMapper.getRoleByUserId(userId);
    }

    @Override
    public Authorize getAuthorize(Integer userId) {
        List<RcRole> roleList = roleMapper.getRoleByUserId(userId);
        List<Long> orgRoleIds = roleMapper.getOrgRoleIdsByUserId(userId);
        List<Long> newRoleIds = roleMapper.getRoleIdsByUserId(userId);
        if (CollectionUtils.isEmpty(roleList) && CollectionUtils.isEmpty(orgRoleIds) && CollectionUtils.isEmpty(newRoleIds)) {
            return new Authorize();
        }
        List<Long> roleIdList = roleList.stream().map(item ->
                Long.valueOf(item.getId())
        ).collect(Collectors.toList());
        roleIdList.addAll(orgRoleIds);
        roleIdList.addAll(newRoleIds);
        List<RcMenu> menuList = rcMenuMapper.getResoureByRoleIdList(roleIdList);
        Authorize authorize = new Authorize();
        List<String> roleCodeList = roleList.stream().map(RcRole::getValue).collect(Collectors.toList());
        authorize.setRoles(roleCodeList);
        List<String> resourceList = menuList.stream().distinct().map(RcMenu::getUrl).filter(url -> !StringUtils.isEmpty(url)).collect(Collectors.toList());
        authorize.setResources(resourceList);
        return authorize;
    }

    @Override
    public RcRole getRoleByValue(String owner) {
        return roleMapper.getRoleByValue(owner);
    }

    @Override
    public Result queryRoleList() {
        List<RcRole> roleList = roleMapper.getRoles();
        List<RoleVO> dataList = Lists.newArrayList();
        Result result = Result.ok();
        for (RcRole bean : roleList) {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(bean, vo);
            vo.setId(Long.valueOf(bean.getId()));
            dataList.add(vo);
        }
        result.setData(dataList);
        return result;
    }

    @Override
    public Result queryRole(Long id) {
        RoleMenuVO vo = new RoleMenuVO();
        if (id < Integer.MAX_VALUE) {
            // 老角色 rc_role
            RcRole rcRole = Optional.ofNullable(roleMapper.getRoleById(id.intValue()))
                    .orElseThrow(() -> new RuntimeException("入力した情報を検査して下さい"));
            vo.setId(Long.valueOf(rcRole.getId()));
            vo.setName(rcRole.getName());
            vo.setStatus(rcRole.getStatus());
            vo.setTips(rcRole.getTips());
            vo.setValue(rcRole.getValue());
            vo.setCreateTime(rcRole.getCreateTime());
        } else {
            OaRole oaRole = roleMapper.getNewRoleById(id);
            if (null == oaRole) {
                // 新组织与新角色绑定关系 rc_oa_org_role
                OaOrgRoleRelation relation = Optional.ofNullable(roleMapper.selectOrgRoleRelation(id))
                        .orElseThrow(() -> new RuntimeException("入力した情報を検査して下さい"));
                vo.setId(relation.getId());
                vo.setName(MessageFormat.format("{0}-{1}", relation.getOrgName(), relation.getRoleName()));
                vo.setTips(MessageFormat.format("{0}-{1}", relation.getOrgTips(), relation.getRoleTips()));
                vo.setValue(null);
                vo.setStatus(1);
            } else {
                // 新角色 rc_oa_role
                vo.setId(oaRole.getId());
                vo.setName(oaRole.getName());
                vo.setStatus(Integer.valueOf(oaRole.getStatus()));
                vo.setTips(oaRole.getTips());
                vo.setValue(oaRole.getValue());
                vo.setCreateTime(oaRole.getCreateTime());
            }
        }
        List<RcMenu> rcMenuList = rcRoleMenuMapper.getMenuByRoleId(id);
        List<String> menuIdList = rcMenuList.stream().map(RcMenu::getId).collect(Collectors.toList());
        vo.setMenuIdList(menuIdList);
        Result result = Result.ok();
        result.setData(vo);
        return result;
    }

    @Override
    public Result queryRolesByMobile(String mobile) {
        return null;
    }

    @Override
    public Result newRole(RoleInfoDTO roleInfoDTO) {

        RcRole role = roleMapper.getRoleByValue(roleInfoDTO.getValue());
        if (null != role) {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "役割のvalueが有りました");
        }
        RcRole rcRole = new RcRole();
        rcRole.setName(roleInfoDTO.getName());
        rcRole.setStatus(SecurityConstants.ROLE_STATUS_ENABLED);
        rcRole.setValue(roleInfoDTO.getValue());
        rcRole.setTips(roleInfoDTO.getTips());
        rcRole.setCreateTime(DateUtil.getNowDate());
        rcRole.setUpdateTime(DateUtil.getNowDate());
        int row = roleMapper.insertRole(rcRole);
        if (row > 0) {
            return Result.ok();
        } else {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "システメのエラー");
        }
    }

    @Override
    @Transactional
    public Result warrent(WarrantVO warrantVO) {
        if (null == warrantVO.getRoleId() && StringUtils.isEmpty(warrantVO.getRoleCode())) {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "認可しない");
        }
        RcRole rc_role = new RcRole();
        if (null != warrantVO.getRoleId()) {
            if (warrantVO.getRoleId() < Integer.MAX_VALUE) {
                rc_role = roleMapper.selectByPrimaryKey(warrantVO.getRoleId().intValue());
            }
        } else {
            rc_role = roleMapper.getRoleByValue(warrantVO.getRoleCode());
        }
        if (null == rc_role || null == rc_role.getId()) {
            throw new RuntimeException("入力した情報を検査して下さい");
        }
        if (null == warrantVO.getRoleId()) {
            // 会社員が認可する
            if (null != rc_role) {
                rcUserRoleMapper.delete(rc_role.getId(), warrantVO.getUserId());
                rcUserRoleMapper.insertPermission(warrantVO.getUserId(), rc_role.getId());
                oaFeignService.deleteRoleTreeCache();
                return Result.ok();
            }
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "認可しない");
        } else {
            //　会員が認可する
            if (null != warrantVO.getUserId()) {
                if (null != rc_role && null != rc_role.getParentId()) {
                    Integer level = 1;
                    RcRole role = new RcRole();
                    role.setParentId(rc_role.getParentId());
                    while (!ROOT_ROLE.equals(role.getParentId())) {
                        level++;
                        role = roleMapper.selectByPrimaryKey(role.getParentId());
                    }
                    log.info("level------:{}", level);
                    // 树的深度大于2，同一个父节点的子节点角色，不能绑定同一个用户
                    if (level > 2) {
                        List<RcRole> childRoles = roleMapper.selectByParentId(rc_role.getParentId());
                        List<RcPermission> permissions = rcUserRoleMapper.selectByRoleIds(
                                childRoles.stream().map(RcRole::getId).collect(Collectors.toSet()));
                        for (RcPermission perssion : permissions) {
                            if (warrantVO.getUserId().equals(perssion.getUserId())
                                    && !warrantVO.getRoleId().equals(perssion.getRoleId())) {
                                throw new RuntimeException("该角色同级的其他角色已经绑定了该用户");
                            }
                        }
                    }
                }
                rcUserRoleMapper.delete(warrantVO.getRoleId().intValue(), warrantVO.getUserId());
                rcUserRoleMapper.insertPermission(warrantVO.getUserId(), warrantVO.getRoleId().intValue());
                oaFeignService.deleteRoleTreeCache();
                return Result.ok();
            }
            //　役割が認可する
            if (!CollectionUtils.isEmpty(warrantVO.getMenuId())) {
                rcRoleMenuMapper.delete(warrantVO.getRoleId());
                List<String> menuId = warrantVO.getMenuId();
                Set<String> menuIdSet = new HashSet<>(menuId);
                menuIdSet.forEach((s) -> {
                    RcRoleMenu rcRoleMenu = new RcRoleMenu();
                    rcRoleMenu.setCreateTime(DateUtil.getNowDate());
                    rcRoleMenu.setMenuId(s);
                    rcRoleMenu.setRoleId(warrantVO.getRoleId());
                    rcRoleMenuMapper.insertBean(rcRoleMenu);
                });
                oaFeignService.deleteRoleTreeCache();
                return Result.ok();
            }
        }
        return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "認可しない");
    }

    @Override
    @Transactional
    public Result deleteWarrent(WarrantVO warrantVO) {
        if (null == warrantVO.getUserId() || null == warrantVO.getRoleId()) {
            return Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR, "入力エラー");
        }
        RcRole rcRole = Optional.ofNullable(roleMapper.selectByPrimaryKey(warrantVO.getRoleId()))
                .orElseThrow(() -> new RuntimeException("入力した情報を検査して下さい"));
        // 如果是删除雇员id，则把该用户的组织架构id都删掉
        if (EMPLOYEE_CODE.equals(rcRole.getValue())) {
            List<RcRole> roles = roleMapper.getRoleByUserId(warrantVO.getUserId());
            Set<Integer> organizationRoles = roles.stream()
                    .filter(item -> null != item.getParentId()).map(RcRole::getId).collect(Collectors.toSet());
            organizationRoles.add(warrantVO.getRoleId().intValue());
            rcUserRoleMapper.batchDelete(organizationRoles, warrantVO.getUserId());
        } else {
            rcUserRoleMapper.delete(warrantVO.getRoleId().intValue(), warrantVO.getUserId());
        }
        oaFeignService.deleteRoleTreeCache();
        return Result.ok();
    }

    @Override
    public Result updateRole(RoleInfoDTO roleInfoDTO) {
        RcRole rcRole = new RcRole();
        rcRole.setName(roleInfoDTO.getName());
        rcRole.setTips(roleInfoDTO.getTips());
        rcRole.setId(roleInfoDTO.getId());
        roleMapper.updateByPrimaryKeySelective(rcRole);
        oaFeignService.deleteRoleTreeCache();
        return Result.ok();
    }
}
