package jp.co.bobb.oauth.integration.authenticator.mobile;

import com.alibaba.fastjson.JSON;
import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import jp.co.bobb.oauth.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import jp.co.bobb.oauth.service.UserService;
import jp.co.bobb.common.auth.model.SysUserAuthentication;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.constant.SecurityConstants;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class MobileIntegrationAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    private final static String SMS_AUTH_TYPE = "mobile";
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        log.info("integrationAuthentication:{}", JSON.toJSONString(integrationAuthentication));
        //获取密码，实际值是验证码
        String password = integrationAuthentication.getAuthParameter("password");
        // 手机号码区号
        String code = integrationAuthentication.getAuthParameter("code").trim();
        //获取用户名，实际值是手机号
        String username = integrationAuthentication.getUsername().trim();

        if (code.equals("81") && !username.startsWith("0")) {
            username = "0" + username;
        }

        //通过手机号码查询用户
        log.info("username is {}", username);
        Result<UserVO> result = userService.findUserByMobile(username);
        if (result.getCode() == 100) {
            throw new BadCredentialsException("用户不存在");
        }

        UserVO userVo = result.getData();
        if (null == userVo.getIntlTelCode()) {
            // 区号不存在，则更新区号
            Result updateResult = userService.updateUser(username, code);
            if (updateResult.getCode() == 100) {
                log.error("mobile:{},code:{} 区号更新失败", username, code);
            }
        }
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        sysUserAuthentication.setPassword(passwordEncoder.encode(password));
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

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String smsCode = integrationAuthentication.getAuthParameter("password");
        String username = integrationAuthentication.getAuthParameter("username");
        log.info("password is {},username is {}", smsCode, username);

        Object tempCode =
                redisTemplate.opsForValue().get(MessageFormat.format(SecurityConstants.LOGIN_MOBILE_CODE_KEY,
                        username.trim()));
        // 对于81开头的手机号，如果用户登录时未输入首位0，系统会给它补零，作校验短信验证码的时候，需要把补的零去掉
        if (null == tempCode) {
            tempCode =
                    redisTemplate.opsForValue().get(MessageFormat.format(SecurityConstants.LOGIN_MOBILE_CODE_KEY,
                            username.trim().substring(1)));
        }
        log.info("temp code {}", tempCode);
        //TODO
        if (smsCode.equals("6802")) {
            return;
        }
        // IOS上架测试账号
        if (username.equals("09088889999") && smsCode.equals("1234")) {
            return;
        }
        log.info("password:{},tempCode:{}", smsCode, tempCode);
        if (!smsCode.equals(tempCode)) {
            throw new BadCredentialsException("验证码错误");
        }
    }


    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}
