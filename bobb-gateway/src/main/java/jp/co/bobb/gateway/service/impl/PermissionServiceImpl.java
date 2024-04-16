package jp.co.bobb.gateway.service.impl;

import com.google.common.collect.Lists;
import jp.co.bobb.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;


@Service("permissionService")
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    /**
     * 可以做URLs匹配，规则如下
     * <p>
     * ？匹配一个字符
     * *匹配0个或多个字符
     * **匹配0个或多个目录
     * 用例如下
     * <p>https://www.cnblogs.com/zhangxiaoguang/p/5855113.html</p>
     */

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String requestUrl = request.getRequestURI();
        log.info("requestUrl:{}", requestUrl);

        List<SimpleGrantedAuthority> grantedAuthorityList = Lists.newArrayList();
        List<String> resources = Lists.newArrayList();

        if (authentication instanceof OAuth2Authentication) {
            Authentication userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
            grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
            LinkedHashMap<String, LinkedHashMap> detailsLinkedHashMap = (LinkedHashMap) userAuthentication.getDetails();
            LinkedHashMap<String, LinkedHashMap> userAuthenticationLinkedHashMap = detailsLinkedHashMap.get("userAuthentication");
            LinkedHashMap<String, LinkedHashMap> principalLinkedHashMap = userAuthenticationLinkedHashMap.get("principal");
            resources = (List) principalLinkedHashMap.get("resources");
        }
        boolean hasPermission = false;

        if (principal != null) {
            if (CollectionUtils.isEmpty(grantedAuthorityList)) {
                log.info("hasPermission0:{}", hasPermission);
                return hasPermission;
            }

            for (SimpleGrantedAuthority authority : grantedAuthorityList) {
                if (authority.getAuthority().startsWith("admin")
                        || authority.getAuthority().startsWith("super")) {
                    return true;
                }

                if (antPathMatcher.match(authority.getAuthority(), requestUrl)) {
                    hasPermission = true;
                    break;
                }
            }

            if (resources.stream().anyMatch(resource -> !StringUtils.isEmpty(resource) && resource.equals(requestUrl))) {
                hasPermission = true;
            }
        }
        log.info("hasPermission1:{}", hasPermission);
        return hasPermission;
    }
}
