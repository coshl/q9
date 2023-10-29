package com.summer.web.exception;

import com.summer.util.ResultEnum;

/**
 * 声明异常
 */
public class SummerException extends RuntimeException {

    private Integer code;

    public SummerException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public SummerException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
