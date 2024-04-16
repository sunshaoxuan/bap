package jp.co.bobb.gateway.model;


import org.springframework.http.HttpStatus;

public class ErrorCode {

    public static final int OK = HttpStatus.OK.value();


    public static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
    public static final int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
    public static final int FORBIDDEN = HttpStatus.FORBIDDEN.value();
    public static final int NOT_FOUNT = HttpStatus.NOT_FOUND.value();

    public static final int MICRO_SERVICE_UNAVAILABLE = 40001;//微服务不可用

    public static final int SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final int BAD_GATEWAY = HttpStatus.BAD_GATEWAY.value();
    public static final int SERVICE_UNAVAILABLE = HttpStatus.SERVICE_UNAVAILABLE.value();

}
