package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.OaOrgRoleRelation;
import jp.co.bobb.upms.entity.OaRole;
import jp.co.bobb.upms.entity.RcRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Parker Sun
 */
@org.apache.ibatis.annotations.Mapper
public interface RcRoleMapper extends Mapper<RcRole> {
    /**
     * 根据会员ID查询会员角色
     *
     * @param userId
     * @return
     */
    @Select(value = "select role.* from rc_role role,rc_user_role ur where role.id=ur.role_id and ur.user_id=#{userId}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "value", property = "value", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tips", property = "tips", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.INTEGER),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<RcRole> getRoleByUserId(Integer userId);

    /**
     * 根据value查询角色
     *
     * @param owner
     * @return
     */
    @Select(value = "select * from rc_role where value=#{owner}")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "value", property = "value", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tips", property = "tips", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.INTEGER),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    RcRole getRoleByValue(String owner);

    /**
     * @return
     */
    @Select(value = "select id,name,value,tips,parent_id,create_time as createTime,status from rc_role where is_public = 0 order by create_time desc")
    List<RcRole> getRoles();

    /**
     * 新增角色
     *
     * @param rcRole
     * @return
     */
    @Insert(value = "insert into rc_role (name,value,tips,parent_id,create_time,update_time,status) values (#{name},#{value},#{tips},#{parentId},#{createTime},#{updateTime},#{status})")
    int insertRole(RcRole rcRole);

    @Select(value = "select id,name,value,tips,parent_id,create_time as createTime,status from rc_role where id = #{id}")
    RcRole getRoleById(Integer id);

    @Select(value = "select id,name,value,tips,parent_id,create_time as createTime,status from rc_role")
    List<RcRole> selectAllRole();

    @Select(value = "select id,name,value,tips,parent_id,create_time as createTime,status from rc_role where parent_id = #{parentId}")
    List<RcRole> selectByParentId(Integer parentId);

    /**
     * 用户绑定的 组织与角色的绑定关系id(rc_oa_role)
     *
     * @param id
     * @return
     */
    @Select({
            "select",
            "id, name, value, tips, parent_id, status, create_time, update_time",
            "from rc_oa_role",
            "where id = #{id,jdbcType=BIGINT} "
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "value", property = "value", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tips", property = "tips", jdbcType = JdbcType.VARCHAR),
            @Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.BIGINT),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    OaRole getNewRoleById(Long id);

    /**
     * 用户绑定的 组织与角色的绑定关系id(rc_oa_role)
     *
     * @param userId
     * @return
     */
    @Select(value = "select org_role_id from rc_oa_org_role_user where user_id = #{userId}")
    List<Long> getOrgRoleIdsByUserId(Integer userId);

    /**
     * 用户根据组织与角色的绑定关系，查找用户在新的角色表里绑定的角色id rc_oa_role
     *
     * @param userId
     * @return
     */
    @Select(value = "select b.role_id from rc_oa_org_role_user as a join rc_oa_org_role as b on a.org_role_id = b.id where a.user_id = #{userId}")
    List<Long> getRoleIdsByUserId(Integer userId);

    @Select({
            "SELECT a.id,",
            "a.org_id  AS orgId,",
            "a.role_id AS roleId,",
            "b.name    AS orgName,",
            "b.value   AS orgValue,",
            "b.tips    AS orgTips,",
            "c.name    AS roleName,",
            "c.value   AS roleValue,",
            "c.tips    AS roleTips",
            "FROM rc_oa_org_role AS a",
            "JOIN rc_oa_org AS b",
            "ON a.org_id = b.id",
            "JOIN rc_oa_role AS c",
            "ON a.role_id = c.id",
            "WHERE a.id = #{id}"
    })
    OaOrgRoleRelation selectOrgRoleRelation(Long id);
}