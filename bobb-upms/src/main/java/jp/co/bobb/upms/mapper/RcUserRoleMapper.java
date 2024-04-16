package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.RcMenu;
import jp.co.bobb.upms.entity.RcPermission;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @author Parker Sun
 */
@Mapper
public interface RcUserRoleMapper {

    /**
     * 新增会员角色
     *
     * @param userId
     * @param roleId
     */
    @Insert("insert into rc_user_role (user_id,role_id) values (#{userId},#{roleId})")
    void insertPermission(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 查询会员角色
     *
     * @param userId
     * @return
     */
    @Select("select id,user_id,role_id from rc_user_role where user_id = #{userId} ")
    List<RcPermission> getPermissionsByUserId(@Param("userId") Integer userId);

    @Select("select c.* from rc_privilege as a join rc_role as b on a.role_id=b.id JOIN rc_user_role as d on a.role_id = b.id and a.user_id = #{userId} join rc_menu as c on a.menu_id= c.id and c.is_menu > 1 order by c.sort")
    List<RcMenu> getMenuByUserId(@Param("userId") Integer userId);

    @Select("select * from rc_menu where is_menu = 1 order by sort")
    List<RcMenu> getAdminMenu();

    @Delete("delete from rc_privilege where menu_id in (${ids})")
    void deleteBatchByMenuIds(@Param("ids") String ids);

    @Delete("delete from rc_user_role where role_id = #{roleId} and user_id = #{userId}")
    void delete(@Param("roleId") Integer roleId, @Param("userId") Integer userId);

    @Delete("delete from rc_user_role where user_id = #{userId}")
    void deleteByUserId(@Param("userId") Integer userId);

    @Select({"<script>",
            "select id,user_id,role_id from rc_user_role",
            "where role_id in",
            "<foreach item='bizId' index='key' collection='collection' open='(' separator=',' close=')' >",
            "#{bizId}",
            "</foreach>",
            "</script>"
    })
    List<RcPermission> selectByRoleIds(Set<Integer> collect);

    @Delete({
            "<script>",
            "delete from rc_user_role",
            "where user_id = #{userId} and role_id in",
            "<foreach item='bizId' index='key' collection='collection' open='(' separator=',' close=')' >",
            "#{bizId}",
            "</foreach>",
            "</script>"
    })
    void batchDelete(@Param("collection") Set<Integer> organizationRoles, @Param("userId") Integer userId);
}
