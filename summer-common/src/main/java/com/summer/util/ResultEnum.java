package com.summer.util;

/**
 * 返回前端错误信息
 */
public enum ResultEnum {
    // 返回前端错误信息
    VERIFICATION_CODE_NULL(9999, "请获取验证码"),

    VERIFICATION_CODE_EXPIRED(998, "验证码已失效，请重新获取"),

    VERIFICATION_CODE_ERROR(997, "验证码错误"),

    NULL_VERIFICATION_CODE(996, "输入验证码为空"),

    PASSWORD_ERROR(995, "用户名或密码错误"),

    PHONE_NULL(994, "手机号码错误或为空"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
