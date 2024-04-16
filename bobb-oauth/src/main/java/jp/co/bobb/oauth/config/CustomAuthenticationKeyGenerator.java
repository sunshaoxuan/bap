package jp.co.bobb.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

@Slf4j
public class CustomAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {
    public static final String SCOPE = "scope";
    public static final String USERNAME = "username";
    private static final String DEVICE = "device";

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<>();
        OAuth2Request request2 = authentication.getOAuth2Request();
        if (!authentication.isClientOnly()) {
            values.put(USERNAME, authentication.getName());
        }
        if (!CollectionUtils.isEmpty(request2.getScope())) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<>(request2.getScope())));
        }
        String device = request2.getRequestParameters().get(DEVICE);
        if (StringUtils.isNotBlank(device)) {
            log.info("{}", device);
            values.put(DEVICE, device);
        }
        return generateKey(values);
    }
}
