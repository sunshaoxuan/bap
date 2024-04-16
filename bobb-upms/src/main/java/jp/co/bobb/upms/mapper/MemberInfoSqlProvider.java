package jp.co.bobb.upms.mapper;

import jp.co.bobb.upms.entity.MemberInfo;
import org.apache.ibatis.jdbc.SQL;

public class MemberInfoSqlProvider {

    public String insertSelective(MemberInfo record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("member_info");

        if (record.getUserId() != null) {
            sql.VALUES("user_id", "#{userId,jdbcType=INTEGER}");
        }

        if (record.getLevel() != null) {
            sql.VALUES("level", "#{level,jdbcType=SMALLINT}");
        }

        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getNameJp() != null) {
            sql.VALUES("name_jp", "#{nameJp,jdbcType=VARCHAR}");
        }

        return sql.toString();
    }

    public String updateByPrimaryKeySelective(MemberInfo record) {
        SQL sql = new SQL();
        sql.UPDATE("member_info");

        if (record.getLevel() != null) {
            sql.SET("level = #{level,jdbcType=SMALLINT}");
        }

        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }

        if (record.getNameJp() != null) {
            sql.SET("name_jp = #{nameJp,jdbcType=VARCHAR}");
        }

        sql.WHERE("user_id = #{userId,jdbcType=INTEGER}");

        return sql.toString();
    }
}