package jp.co.bobb.oauth.integration.authenticator.mobile;

import jp.co.bobb.oauth.integration.IntegrationAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/05/27
 */
@Component
@Slf4j
public class IntegrationListener {

    @Async
    @EventListener
    public void listenEvent(IntegrationAuthentication event) {
        log.info("----------------------------");
        log.info(event.getUsername());
    }

}
