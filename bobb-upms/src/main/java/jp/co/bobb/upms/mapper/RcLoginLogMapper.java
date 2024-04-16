package jp.co.bobb.upms.mapper;


import jp.co.bobb.upms.entity.RcLoginLog;
import org.apache.ibatis.annotations.Insert;

@org.apache.ibatis.annotations.Mapper
public interface RcLoginLogMapper {

    @Insert("insert into rc_user_login_history (id,user_id,login_time) values(#{id},#{userId},#{loginTime})")
    int insert(RcLoginLog rcLoginLog);
}
