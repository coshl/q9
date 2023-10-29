package com.summer.enums;

/**
 * 运营商认证状态
 */
public enum MobileAuthStatus {

    AUTH_WAIT(-1, "待认证"),
    AUTH_RUN(0, "认证中"),
    AUTH_SUCCESS(1, "认证成功"),
    AUTH_ERROR(2, "认证失败");

    private Integer value;
    private String desc;

    MobileAuthStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
