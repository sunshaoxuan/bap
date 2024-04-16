package jp.co.bobb.eureka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class InstanceRenewListener implements ApplicationListener<EurekaInstanceRenewedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceRenewListener.class);

    @Override
    public void onApplicationEvent(EurekaInstanceRenewedEvent event) {
        Optional.ofNullable(event.getInstanceInfo()).ifPresent(e ->
                LOGGER.info("heart check service name:{},address:{}",
                        e.getAppName(),
                        e.getIPAddr())
        );

    }
}
