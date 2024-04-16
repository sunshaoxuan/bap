package jp.co.bobb.oauth.integration;

import lombok.Data;

import java.util.Map;

/**
 * describe:定义集成认证实体
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
public class IntegrationAuthentication {
    /**
     * 认证类型
     */
    private String authType;
    /**
     * 登录时为邮箱或手机号
     */
    private String username;
    /**
     * 请求登录认证参数集合
     */
    private Map<String, String[]> authParameters;

    public String getAuthParameter(String paramter) {
        String[] values = this.authParameters.get(paramter);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }
}
