package jp.co.bobb.oauth.integration.authenticator;

import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import jp.co.bobb.common.auth.model.SysUserAuthentication;

public interface IntegrationAuthenticator {
    /**
     * 处理集成认证
     *
     * @param integrationAuthentication
     * @return
     */
    SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication);

    /**
     * 进行预处理
     *
     * @param integrationAuthentication
     */
    void prepare(IntegrationAuthentication integrationAuthentication);

    /**
     * 判断是否支持集成认证类型
     *
     * @param integrationAuthentication
     * @return
     */
    boolean support(IntegrationAuthentication integrationAuthentication);

    /**
     * 认证结束后执行
     *
     * @param integrationAuthentication
     */
    void complete(IntegrationAuthentication integrationAuthentication);

}
