package jp.co.bobb.gateway.filter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import jp.co.bobb.common.constant.FrameworkConstant;
import jp.co.bobb.common.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Parker Sun
 */
@Component
@Slf4j
public class PreRequestFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        //int值来定义过滤器的执行顺序，数值越小优先级越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 把url请求的域名带给下游服务
        ctx.getZuulRequestHeaders().put(SecurityConstants.HTTP_SERVER_DOMAIN_NAME, request.getServerName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication's username is {}", authentication.getPrincipal());
        log.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            Object authenticationDetail = auth2Authentication.getUserAuthentication();

            Collection<GrantedAuthority> grantedAuthorityCollection = auth2Authentication.getAuthorities();
            if (authenticationDetail instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationDetailMap =
                        (UsernamePasswordAuthenticationToken) authenticationDetail;
                LinkedHashMap detailLinkedHashMap = (LinkedHashMap) authenticationDetailMap.getDetails();
                Object principal = detailLinkedHashMap.get("principal");
                Map<String, String[]> parameterMap = request.getParameterMap();
                Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
                if (null == requestQueryParams) {
                    requestQueryParams = Maps.newHashMap();
                }
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    requestQueryParams.put(entry.getKey(), Arrays.asList(entry.getValue()));
                }
                List<String> adminList = Lists.newArrayList();
                adminList.add("false");
                grantedAuthorityCollection.forEach(grantedAuthority -> {
                    if (grantedAuthority.getAuthority().equalsIgnoreCase("admin")) {
                        adminList.clear();
                        adminList.add("true");
                    }
                });
                requestQueryParams.put(FrameworkConstant.FRAMEWORK_LOGIN_ADMIN_CONSTANT, adminList);
                if (principal instanceof LinkedHashMap) {
                    LinkedHashMap principalMap = (LinkedHashMap) principal;
                    List<String> userIdList = Lists.newArrayList();
                    userIdList.add(principalMap.get("id").toString());
                    List<String> userNameList = Lists.newArrayList();
                    userNameList.add(principalMap.get("username").toString());
                    List<String> userNickNameList = Lists.newArrayList();
                    if (principalMap.containsKey("name")
                            && null != principalMap.get("name")) {
                        userNickNameList.add(principalMap.get("name").toString());
                    }
                    requestQueryParams.put(FrameworkConstant.FRAMEWORK_LOGIN_USER_ID_CONSTANT, userIdList);
                    requestQueryParams.put(FrameworkConstant.FRAMEWORK_LOGIN_USER_NAME_CONSTANT, userNameList);
                    requestQueryParams.put(FrameworkConstant.FRAMEWORK_LOGIN_NAME_CONSTANT, userNickNameList);
                    requestQueryParams.put("system", Lists.newArrayList("tanimachikun"));
                    requestQueryParams.put("cooperatorId", Lists.newArrayList(""));
                }
                ctx.setRequestQueryParams(requestQueryParams);
            }
            log.info("set pre request");
        }


        return null;
    }
}
