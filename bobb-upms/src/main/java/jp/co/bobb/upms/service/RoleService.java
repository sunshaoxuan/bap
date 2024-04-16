package jp.co.bobb.upms.service;

import jp.co.bobb.upms.dto.RoleInfoDTO;
import jp.co.bobb.upms.entity.RcRole;
import jp.co.bobb.upms.vo.WarrantVO;
import jp.co.bobb.common.auth.model.Authorize;
import jp.co.bobb.common.vo.Result;

import java.util.List;

/**
 * @author Parker Sun
 */
public interface RoleService {

    /**
     * 雇员角色id的key
     */
    String EMPLOYEE_CODE = "employee";

    /**
     * @param userId
     * @return
     */
    List<RcRole> getRoleByUserId(Integer userId);

    /**
     * @param userId
     * @return
     */
    Authorize getAuthorize(Integer userId);

    /**
     * @param owner
     * @return
     */
    RcRole getRoleByValue(String owner);

    /**
     * @return
     */
    Result queryRoleList();

    /**
     * @param id
     * @return
     */
    Result queryRole(Long id);

    /**
     * @param mobile
     * @return
     */
    Result queryRolesByMobile(String mobile);

    /**
     * @param roleInfoDTO
     * @return
     */
    Result newRole(RoleInfoDTO roleInfoDTO);

    /**
     * 認可する
     *
     * @param warrantVO
     * @return
     */
    Result warrent(WarrantVO warrantVO);

    /**
     * 役割が削除する
     *
     * @param warrantVO
     * @return
     */
    Result deleteWarrent(WarrantVO warrantVO);

    Result updateRole(RoleInfoDTO roleInfoDTO);
}
