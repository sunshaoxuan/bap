package jp.co.bobb.oauth.integration;

/**
 * describe:定义集成认证上下文
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
public class IntegrationAuthenticationContext {
    private static ThreadLocal<IntegrationAuthentication> holder = new ThreadLocal<>();

    public static void set(IntegrationAuthentication integrationAuthentication) {
        holder.set(integrationAuthentication);
    }

    public static IntegrationAuthentication get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}
