package jp.co.bobb.upms.listener;

import jp.co.bobb.upms.entity.RcLoginLog;
import jp.co.bobb.upms.mapper.RcLoginLogMapper;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.util.DateUtil;
import jp.co.bobb.common.util.SnowflakeIdFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/28
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.MEMBER_LOGIN_LOG_QUEUE)
public class MemberLoginLogListener {

    @Autowired
    RcLoginLogMapper rcLoginLogMapper;

    @Autowired
    SnowflakeIdFactory snowflakeIdFactory;

    @RabbitHandler
    public void process(Integer userId) {
        log.info("current login user's id  is {}", userId);

        RcLoginLog rcLoginLog = new RcLoginLog();
        rcLoginLog.setId(snowflakeIdFactory.nextId());
        rcLoginLog.setLoginTime(DateUtil.getNowDate());
        rcLoginLog.setUserId(userId);
        try {
            rcLoginLogMapper.insert(rcLoginLog);
        } catch (Exception e) {
            log.error("exception", e);
        }
    }
}
