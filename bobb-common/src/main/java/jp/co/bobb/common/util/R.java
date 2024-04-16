package jp.co.bobb.common.util;

import lombok.Data;

import java.io.Serializable;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
public class R<T> implements Serializable {
    public static final int SUCCESS = 200;
    public static final int FAIL = 500;
    private static final long serialVersionUID = 1L;
    private String msg = "success";

    private int code = SUCCESS;

    private T data;

    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public R(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = FAIL;
    }
}
