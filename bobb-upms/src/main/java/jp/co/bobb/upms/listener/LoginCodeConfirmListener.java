package jp.co.bobb.upms.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.common.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/15
 */
//@Component
@Slf4j
//@RabbitListener(queues = RabbitQueue.LOGIN_CODE_CONFIRM_QUEUE)
public class LoginCodeConfirmListener {

    @Autowired
    RedisTemplate redisTemplate;

    @RabbitHandler
    public void process(JSONObject message) {
        log.info("message is {}", message);
        if (message.getString("action").equalsIgnoreCase("login")) {
            String key = MessageFormat.format(SecurityConstants.LOGIN_MOBILE_CODE_KEY,
                    message.getString("mobile"));
            redisTemplate.opsForValue()
                    .set(key, message.getString("code"),
                            SecurityConstants.DEFAULT_LOGIN_VAL_CODE_EXPIRE,
                            TimeUnit.MINUTES);
            log.info("{}:{}", key, redisTemplate.opsForValue().get(key));
        }

        if (message.getString("action").equalsIgnoreCase("meeting")) {
            redisTemplate.opsForValue()
                    .set(MessageFormat.format(SecurityConstants.MEETING_FREE_MOBILE_CODE,
                                    message.getString("mobile")),
                            message.getString("code"),
                            SecurityConstants.DEFAULT_MEETING_VAL_CODE_EXPIRE,
                            TimeUnit.MINUTES);
            String key = MessageFormat.format(SecurityConstants.MEETING_FREE_MOBILE_CODE, message.getString("mobile"));
            log.info("{}:{}", key, redisTemplate.opsForValue().get(key));
        }

    }

}
