package jp.co.bobb.oauth.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class Msg implements Serializable {
    public static final int SUCCESS = HttpStatus.OK.value();
    public static final int FAILED = HttpStatus.CREATED.value();
    private static final long serialVersionUID = 7514826298158585250L;
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
