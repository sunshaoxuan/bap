package jp.co.bobb.oauth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/12/07
 */
public class MyUsernameNotFoundException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public MyUsernameNotFoundException(String msg) {
        super(msg);
    }

    public MyUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
