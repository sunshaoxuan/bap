package jp.co.bobb.oauth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Component
@Slf4j
public class WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {
    public static final String BAD_MSG = "Bad Certificate";

    /**
     * @param e spring security内部异常
     * @return 经过处理的异常信息
     * @throws Exception 通用异常
     */
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("Grant Error : " + e);
        e.printStackTrace();
        OAuth2Exception oAuth2Exception;
        if (e.getMessage() != null && e.getMessage().equals(BAD_MSG)) {
            oAuth2Exception = new InvalidGrantException("Incorrect username or password!", e);
        } else if (e instanceof InternalAuthenticationServiceException) {
            oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
        } else {
            oAuth2Exception = new UnsupportedResponseTypeException("Internal server error", e);
        }
        return super.translate(oAuth2Exception);
    }
}
