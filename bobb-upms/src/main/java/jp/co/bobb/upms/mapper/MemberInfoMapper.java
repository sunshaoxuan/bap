package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.MemberInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

/**
 * @author Parker Sun
 */
@org.apache.ibatis.annotations.Mapper
public interface MemberInfoMapper {
    @Delete({
            "delete from member_info",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer userId);

    @Insert({
            "insert into member_info (user_id, level, ",
            "update_time, name_jp)",
            "values (#{userId,jdbcType=INTEGER}, #{level,jdbcType=SMALLINT}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{nameJp,jdbcType=VARCHAR})"
    })
    int insert(MemberInfo record);

    @InsertProvider(type = MemberInfoSqlProvider.class, method = "insertSelective")
    int insertSelective(MemberInfo record);

    @Select({
            "select",
            "user_id, level, update_time, name_jp",
            "from member_info",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "level", property = "level", jdbcType = JdbcType.SMALLINT),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "name_jp", property = "nameJp", jdbcType = JdbcType.VARCHAR)
    })
    MemberInfo selectByPrimaryKey(Integer userId);

    @UpdateProvider(type = MemberInfoSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(MemberInfo record);

    @Update({
            "update member_info",
            "set level = #{level,jdbcType=SMALLINT},",
            "update_time = #{updateTime,jdbcType=TIMESTAMP},",
            "name_jp = #{nameJp,jdbcType=VARCHAR}",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(MemberInfo record);

    @Select({
            "select",
            "a.user_id, a.level, a.update_time, a.name_jp",
            "from member_info as a join rc_user as b on a.user_id = b.id",
            "where b.username = #{mobile,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "level", property = "level", jdbcType = JdbcType.SMALLINT),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "name_jp", property = "nameJp", jdbcType = JdbcType.VARCHAR)
    })
    MemberInfo selectByMobile(@Param("mobile") String mobile);

    @Select({
            "select",
            "a.user_id, a.level, a.update_time, a.name_jp",
            "from member_info as a",
            "where a.user_id = #{userId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "level", property = "level", jdbcType = JdbcType.SMALLINT),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "name_jp", property = "nameJp", jdbcType = JdbcType.VARCHAR)
    })
    MemberInfo selectByMemberId(@Param("userId") Integer userId);

    @Update({
            "update member_info set open_id = #{openId,jdbcType=VARCHAR},union_id = #{unionId,jdbcType=VARCHAR} ",
            "where user_id = #{userId,jdbcType=INTEGER}"
    })
    void updateWxInfo(@Param("userId") Integer userId,
                      @Param("openId") String openId,
                      @Param("unionId") String unionId);
}