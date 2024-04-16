package jp.co.bobb.oauth.service.impl;

import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import jp.co.bobb.oauth.integration.IntegrationAuthenticationContext;
import jp.co.bobb.oauth.integration.authenticator.IntegrationAuthenticator;
import jp.co.bobb.oauth.model.User;
import jp.co.bobb.oauth.service.RoleService;
import jp.co.bobb.common.auth.model.Authorize;
import jp.co.bobb.common.auth.model.SysUserAuthentication;
import jp.co.bobb.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private RoleService roleService;

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        //判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }
        integrationAuthentication.setUsername(username);
        SysUserAuthentication sysUserAuthentication = this.authenticate(integrationAuthentication);

        if (sysUserAuthentication == null) {
            log.error("sysUserAuthentication is null,Incorrect username or password");
            //throw new UsernameNotFoundException("Incorrect username or password");
            throw new BadCredentialsException("用户不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(sysUserAuthentication, user);
        this.setAuthorize(user);
        return user;
    }

    /**
     * 设置授权信息
     *
     * @param user
     */
    public void setAuthorize(User user) {
        Result<Authorize> result = roleService.getAuthorize(user.getId());
        if (result.getCode() == 100) {
            log.info("getAuthorize 失败");
        }
        Authorize authorize = result.getData();
        user.setRoles(authorize.getRoles());
        user.setResources(authorize.getResources());
    }

    private SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}
