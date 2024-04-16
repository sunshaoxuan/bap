package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.RcUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface RcUserMapper extends Mapper<RcUser> {

    /**
     * rc_user update
     *
     * @param user
     * @return
     */
    @Update("update rc_user set " +
            "avatar = #{avatar}" +
            ",username = #{username}" +
            ",name= #{name}" +
            ",birthday=#{birthday}" +
            ",sex=#{sex}" +
            ",email=#{email}" +
            ",phone=#{phone}" +
            ",status=#{status}" +
            ",member_id=#{memberId} where member_id = #{memberId}")
    int updateUserInfo(RcUser user);

    /**
     * 根据memberId查询会员
     *
     * @param memberId
     * @return
     */
    @Select("select * from rc_user where member_id = #{memberId}")
    RcUser selectUserByMemberId(Integer memberId);

    /**
     * 分页查询内部用户 role_id = 21 的为内部用户
     *
     * @param pageNum 页码
     * @param offset  偏移量
     * @return
     */
    @Select(value = "SELECT USER .* FROM rc_user USER WHERE ID IN( SELECT user_id FROM rc_user_role WHERE role_id = 21) ORDER BY ID LIMIT #{pageNum} OFFSET #{offset}")
    List<RcUser> findInsideUser(@Param("pageNum") Integer pageNum, @Param("offset") Integer offset);

    /**
     * 分页查询外部用户 role_id = 19 的为外部用户
     *
     * @param pageNum 页码
     * @param offset  偏移量
     * @return
     */
    @Select(value = "SELECT USER .* FROM rc_user USER WHERE ID IN( SELECT user_id FROM rc_user_role WHERE role_id = 19) ORDER BY ID LIMIT #{pageNum} OFFSET #{offset}")
    List<RcUser> findOutsideUser(@Param("pageNum") Integer pageNum, @Param("offset") Integer offset);

    /**
     * 查询内部用户总数 role_id = 21 的为内部用户
     *
     * @return
     */
    @Select(value = "SELECT count(1) FROM rc_user USER WHERE ID IN( SELECT user_id FROM rc_user_role WHERE role_id = 21)")
    Integer countInsideUser();

    /**
     * 查询外部用户总数 role_id = 19 的为外部用户
     *
     * @return
     */
    @Select(value = "SELECT count(1) FROM rc_user USER WHERE ID IN( SELECT user_id FROM rc_user_role WHERE role_id = 19)")
    Integer countOutsideUser();

    @Select("select id from rc_user where username = #{mobile} limit 1")
    RcUser queryMemberByMobile(@Param("mobile") String mobile);

    @Insert("insert into member_info(user_id,level,update_time) values (#{user_id},#{level},#{nowTime})")
    void insertUserInfo(@Param("user_id") Integer id, @Param("level") Integer level, @Param("nowTime") Date now);

    @Select({
            "select",
            "id, avatar, username, password, salt, name, birthday, sex, email, intl_tel_code, ",
            "phone, status, create_time, register_time, update_time, member_id",
            "from rc_user",
            "where phone = #{mobile,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "avatar", property = "avatar", jdbcType = JdbcType.VARCHAR),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "salt", property = "salt", jdbcType = JdbcType.VARCHAR),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "birthday", property = "birthday", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "sex", property = "sex", jdbcType = JdbcType.INTEGER),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "intl_tel_code", property = "intlTelCode", jdbcType = JdbcType.SMALLINT),
            @Result(column = "phone", property = "phone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "register_time", property = "registerTime", jdbcType = JdbcType.INTEGER),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "member_id", property = "memberId", jdbcType = JdbcType.INTEGER)
    })
    RcUser selectByMobile(@Param("mobile") String mobile);

    @Update({"<script>",
            "update rc_user",
            "<set>",
            "<if test='item.avatar != null' >",
            "avatar = #{item.avatar,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.username != null' >",
            "username = #{item.username,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.password != null' >",
            "password = #{item.password,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.salt != null' >",
            "salt = #{item.salt,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.name != null' >",
            "name = #{item.name,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.birthday != null' >",
            "birthday = #{item.birthday,jdbcType=DATE},",
            "</if>",
            "<if test='item.sex != null' >",
            "sex = #{item.sex,jdbcType=INTEGER},",
            "</if>",
            "<if test='item.intlTelCode != null' >",
            "intl_tel_code = #{item.intlTelCode,jdbcType=SMALLINT},",
            "</if>",
            "<if test='item.email != null' >",
            "email = #{item.email,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.phone != null' >",
            "phone = #{item.phone,jdbcType=VARCHAR},",
            "</if>",
            "<if test='item.status != null' >",
            "status = #{item.status,jdbcType=INTEGER},",
            "</if>",
            "<if test='item.createTime != null' >",
            "create_time = #{item.createTime,jdbcType=DATE},",
            "</if>",
            "<if test='item.updateTime != null' >",
            "update_time = #{item.updateTime,jdbcType=DATE},",
            "</if>",
            "<if test='item.registerTime != null' >",
            "register_time = #{item.registerTime,jdbcType=DATE},",
            "</if>",
            "<if test='item.memberId != null' >",
            "member_id = #{item.memberId,jdbcType=INTEGER},",
            "</if>",
            "</set>",
            "<where>",
            "id = #{item.id,jdbcType=INTEGER}",
            "</where>",
            "</script>"
    })
    int updateUserByPrimaryKey(@Param("item") RcUser item);

    @Select({
            "select",
            "id, avatar, username, space_name",
            "from rc_user",
            "where email = #{email,jdbcType=VARCHAR} limit 1"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "avatar", property = "avatar", jdbcType = JdbcType.VARCHAR),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "space_name", property = "spaceName", jdbcType = JdbcType.VARCHAR)
    })
    RcUser selectByEmail(@Param("email") String email);

}