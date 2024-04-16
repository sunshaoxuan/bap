package jp.co.bobb.oauth.integration.authenticator;

import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import jp.co.bobb.common.auth.model.SysUserAuthentication;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
public abstract class AbstractPreparableIntegrationAuthenticator implements IntegrationAuthenticator {
    @Override
    public abstract SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract void prepare(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract boolean support(IntegrationAuthentication integrationAuthentication);

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
