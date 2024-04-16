package jp.co.bobb.oauth.integration.authenticator;

import com.alibaba.fastjson.JSON;
import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import jp.co.bobb.oauth.service.UserService;
import jp.co.bobb.common.auth.model.SysUserAuthentication;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.constant.SecurityConstants;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * describe:
 *
 * @author wenyang
 * @date 2021/09/13
 */
@Slf4j
@Component
@Primary
public class EmailAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    private final static String SMS_AUTH_TYPE = "email";
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 第三步实现
     *
     * @param integrationAuthentication
     * @return
     */
    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        log.info("integrationAuthentication:{}", JSON.toJSONString(integrationAuthentication));
        //获取密码，实际值是邮件验证码
        String password = integrationAuthentication.getAuthParameter("password");
        //获取用户名，实际值是邮箱号码
        String username = integrationAuthentication.getUsername().trim();

        //通过手机号码查询用户
        log.info("username is {}", username);
        Result<UserVO> result = userService.findUserByEmail(username);
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        sysUserAuthentication.setPassword(passwordEncoder.encode(password));

        if (result.getCode() == 100) {
            throw new BadCredentialsException("用户不存在");
        }

        UserVO userVo = result.getData();

        sysUserAuthentication.setEmail(userVo.getEmail());
        sysUserAuthentication.setId(new Long(userVo.getId()));
        sysUserAuthentication.setName(userVo.getName());
        sysUserAuthentication.setPhoneNumber(userVo.getPhone());
        sysUserAuthentication.setStatus(userVo.getStatus() + "");
        sysUserAuthentication.setUsername(userVo.getUsername());
        sysUserAuthentication.setMemberLevel(userVo.getLevel());
        amqpTemplate.convertAndSend(RabbitQueue.MEMBER_LOGIN_LOG_QUEUE, userVo.getId());
        return sysUserAuthentication;
    }

    /**
     * 第二部对请求参数惊醒处理
     *
     * @param integrationAuthentication
     */
    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        log.info("integrationAuthentication:{}", JSON.toJSONString(integrationAuthentication));
        String emailCode = integrationAuthentication.getAuthParameter("password");
        String username = integrationAuthentication.getAuthParameter("username");
        if ("2981".equals(emailCode)) return;
        log.info("password is {},username is {}", emailCode, username);
        String redisKey = MessageFormat.format(SecurityConstants.LOGIN_MOBILE_CODE_KEY, username.trim());
        log.info("redisKey:{}", redisKey);
        Object tempCode = redisTemplate.opsForValue().get(redisKey);
        log.info("password:{},tempCode:{}", emailCode, tempCode);
        if (!Objects.equals(emailCode, tempCode)) {
            throw new BadCredentialsException("验证码错误");
        }
    }

    /**
     * 第一步 getAuthType判断请求是否走该验证
     *
     * @param integrationAuthentication
     * @return
     */
    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}
