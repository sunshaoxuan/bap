package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.RcMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

@org.apache.ibatis.annotations.Mapper
public interface RcMenuMapper extends Mapper<RcMenu> {
    /**
     * 役割のメニューを問い合わせする
     *
     * @param roleId
     * @return
     */
    @Select(value = "select menu.* from rc_menu menu,rc_privilege p where menu.id=p.menu_id and p.role_id=#{roleId}")
    List<RcMenu> getPermissionsByRoleId(Integer roleId);

    /**
     * 　役割の正しいを問い合わせする
     *
     * @param roleIdList
     * @return
     */
    List<RcMenu> getResoureByRoleIdList(@Param("list") List<Long> roleIdList);

    /**
     * @param pId
     * @return
     */
    List<RcMenu> queryChildMenus(@Param("pId") String pId,
                                 @Param("userId") Integer userId,
                                 @Param("system") String system);

    @Select({
            "SELECT role_id FROM rc_user_role WHERE user_id = #{userId}",
            "union select org_role_id from rc_oa_org_role_user where user_id = #{userId}",
            "union select a.role_id from rc_oa_org_role as a join rc_oa_org_role_user as b on a.id = b.org_role_id where b.user_id = #{userId}"
    })
    List<Long> getRoleIdsByUserId(@Param("userId") Integer userId);

    List<RcMenu> queryChildMenusByRoles(@Param("pId") String pId,
                                        @Param("list") List<Long> roleIdList,
                                        @Param("system") String system,
                                        @Param("domain") String domain
    );

    /**
     * @param id
     * @return
     */
    @Select(value = "select menu.* from rc_menu menu where menu.id=#{id}")
    RcMenu queryRcMenu(String id);

    /**
     * 　メニューを削除する
     *
     * @param id
     * @return
     */
    @Delete(value = "delete from rc_menu where id = #{id}")
    int deleteMenu(String id);

    void deleteMenuBatch(@Param("set") Set<String> pIdSet);

    List<RcMenu> queryMenusByParendIds(@Param("set") Set<String> id);

    @Select(value = "select menu.* from rc_menu menu where menu.id=#{pId}")
    RcMenu selectById(@Param("pId") String pId);

    @Insert(value = "INSERT INTO rc_menu (id,code,p_code,p_id,name,url,page_url,is_menu,level,`system`,sort,status,icon,create_time,update_time) values (#{id},#{code},#{pCode},#{pId},#{name},#{url},#{pageUrl},#{isMenu},#{level},#{system},#{sort},#{status},#{icon},#{createTime},#{updateTime})")
    int insertMenu(RcMenu rcMenu);

    int updateByIdSelective(RcMenu rcMenu);
}