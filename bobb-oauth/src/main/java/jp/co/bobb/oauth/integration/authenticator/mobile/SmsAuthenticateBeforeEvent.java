package jp.co.bobb.oauth.integration.authenticator.mobile;

import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import org.springframework.context.ApplicationEvent;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/24
 */
public class SmsAuthenticateBeforeEvent extends ApplicationEvent {

    public SmsAuthenticateBeforeEvent(IntegrationAuthentication source) {
        super(source);
    }
}
