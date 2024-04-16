package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.RcMenu;
import jp.co.bobb.upms.entity.RcRoleMenu;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/15
 */
@Mapper
public interface RcRoleMenuMapper {

    @Insert("insert into rc_privilege (role_id,menu_id,create_time) values (#{roleId},#{menuId},#{nowDate})")
    void insert(@Param("roleId") Long roleId, @Param("menuId") String menuId, @Param("nowDate") Date nowDate);

    @Select("select b.id,b.code,b.p_code,b.p_id,b.name,b.page_url,b.url,b.is_menu,b.level,b.status " +
            "from rc_privilege as a " +
            "join rc_menu as b on a.menu_id = b.id " +
            "where a.role_id = #{id} and level = 3")
    List<RcMenu> getMenuByRoleId(Long id);

    @Insert("insert into rc_privilege (role_id,menu_id,create_time) values (#{roleId},#{menuId},#{createTime})")
    void insertBean(RcRoleMenu rcRoleMenu);

    @Delete("delete from rc_privilege where role_id = #{roleId}")
    void delete(@Param("roleId") Long roleId);
}
