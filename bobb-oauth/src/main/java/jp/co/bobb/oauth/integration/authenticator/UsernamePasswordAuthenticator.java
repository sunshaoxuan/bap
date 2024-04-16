package jp.co.bobb.oauth.integration.authenticator;

import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import jp.co.bobb.oauth.service.UserService;
import jp.co.bobb.common.auth.model.SysUserAuthentication;
import jp.co.bobb.common.constant.RabbitQueue;
import jp.co.bobb.common.vo.Result;
import jp.co.bobb.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Slf4j
@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    private UserService userService;

    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        Result<UserVO> result = userService.findUserByMobile(integrationAuthentication.getUsername().trim());
        if (result.getCode() == 100) {
            throw new BadCredentialsException("用户不存在");
        }
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        UserVO userVo = result.getData();
        sysUserAuthentication.setEmail(userVo.getEmail());
        sysUserAuthentication.setId(new Long(userVo.getId()));
        sysUserAuthentication.setName(userVo.getName());
        sysUserAuthentication.setPassword(userVo.getPassword());
        sysUserAuthentication.setPhoneNumber(userVo.getPhone());
        sysUserAuthentication.setStatus(userVo.getStatus() + "");
        sysUserAuthentication.setUsername(userVo.getUsername());
        sysUserAuthentication.setMemberLevel(userVo.getLevel());
        amqpTemplate.convertAndSend(RabbitQueue.MEMBER_LOGIN_LOG_QUEUE, userVo.getId());
        return sysUserAuthentication;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}
