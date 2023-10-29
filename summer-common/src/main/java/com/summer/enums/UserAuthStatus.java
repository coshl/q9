package com.summer.enums;

public enum UserAuthStatus {

    WAIT_AUTH(0, "待认证"),
    SUCCESS_AUTH(1, "认证成功"),
    FAIL_AUTH(2, "认证失败"),
    RUN_AUTH(3, "认证中");

    private Integer value;
    private String desc;

    UserAuthStatus(Integer value, String desc) {
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
