package jp.co.bobb.oauth.config;

import jp.co.bobb.oauth.exception.WebResponseExceptionTranslator;
import jp.co.bobb.oauth.integration.IntegrationAuthenticationFilter;
import jp.co.bobb.oauth.model.User;
import jp.co.bobb.oauth.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * describe:启用授权服务器
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Configuration
@Order(Integer.MIN_VALUE)
@EnableAuthorizationServer
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    WebResponseExceptionTranslator exceptionTranslator;
    @Autowired
    IntegrationAuthenticationFilter integrationAuthenticationFilter;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    RedisTokenStore redisTokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setAuthenticationKeyGenerator(new CustomAuthenticationKeyGenerator());
        return tokenStore;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }


    @Bean
    public ClientDetailsService clientDetails() {
        String selectStatement = "select client_id, client_secret, resource_ids, scope," +
                "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity," +
                "refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";

        String findStatement = "select client_id, client_secret, resource_ids, scope," +
                "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity," +
                "refresh_token_validity, additional_information, autoapprove from oauth_client_details order by client_id";

        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(selectStatement);
        clientDetailsService.setFindClientDetailsSql(findStatement);
        return clientDetailsService;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //token增强配置
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        endpoints
                .tokenStore(redisTokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(exceptionTranslator)
                // refreshtoken是否一直有效直到过期 否则每次refreshtoken refreshtoken同步更新
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        BobbJwtAccessTokenConverter jwtAccessTokenConverter = new BobbJwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("jp-tokyo-bobb");
        return jwtAccessTokenConverter;
    }

    /**
     * jwt 生成token 定制化处理
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(3);
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            if (user != null) {
                additionalInfo.put("usename", user.getUsername());
                additionalInfo.put("mobile", user.getPhoneNumber());
                additionalInfo.put("memberLevel", user.getMemberLevel());
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     *
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
        // token有效期自定义设置，默认12小时
        //tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(2));
        //默认30天，这里修改
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .addTokenEndpointAuthenticationFilter(integrationAuthenticationFilter);
    }
}
