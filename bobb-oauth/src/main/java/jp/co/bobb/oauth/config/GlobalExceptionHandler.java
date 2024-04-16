package jp.co.bobb.oauth.config;

import jp.co.bobb.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * describe: 数据校验异常处理框架
 *
 * @author Parker Sun
 * @date 2019/10/14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception e) {
        log.error("exception", e);
        return new R(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
