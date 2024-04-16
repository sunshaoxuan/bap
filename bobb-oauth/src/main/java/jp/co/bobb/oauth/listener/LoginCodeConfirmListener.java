package jp.co.bobb.oauth.listener;

import com.alibaba.fastjson.JSONObject;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/04/15
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitQueue.LOGIN_CODE_CONFIRM_QUEUE)
public class LoginCodeConfirmListener {

    @Autowired
    RedisTemplate redisTemplate;

    @Resource
    RedisTokenStore redisTokenStore;

    private void deletePreToken(JSONObject message) {
        log.info("{}", message);
        String username = Optional.ofNullable(message.getString("username")).orElse(message.getString("mobile"));

        redisTokenStore.findTokensByClientIdAndUserName("webApp", username).forEach(token -> {
            redisTokenStore.removeAccessToken(token);
            redisTokenStore.removeRefreshToken(token.getRefreshToken());
        });
        redisTokenStore.findTokensByClientIdAndUserName("app", username).forEach(token -> {
            redisTokenStore.removeAccessToken(token);
            redisTokenStore.removeRefreshToken(token.getRefreshToken());
        });
    }

    @RabbitHandler
    public void process(JSONObject message) {
        log.info("message is {}", message);
        if ("login".equalsIgnoreCase(message.getString("action"))) {
            String key = MessageFormat.format(SecurityConstants.LOGIN_MOBILE_CODE_KEY,
                    message.getString("mobile"));
            redisTemplate.opsForValue()
                    .set(key, message.getString("code"),
                            SecurityConstants.DEFAULT_LOGIN_VAL_CODE_EXPIRE,
                            TimeUnit.MINUTES);
            log.info("{}:{}", key, redisTemplate.opsForValue().get(key));
            deletePreToken(message);
        }
        if ("email_login".equalsIgnoreCase(message.getString("action"))) {
            String key = MessageFormat.format(SecurityConstants.LOGIN_MOBILE_CODE_KEY,
                    message.getString("email"));
            redisTemplate.opsForValue()
                    .set(key, message.getString("code"),
                            SecurityConstants.DEFAULT_LOGIN_VAL_CODE_EXPIRE,
                            TimeUnit.MINUTES);
            log.info("{}:{}", key, redisTemplate.opsForValue().get(key));
            deletePreToken(message);
        }
        if ("meeting".equalsIgnoreCase(message.getString("action"))) {
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
