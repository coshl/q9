package com.summer.enums;

public enum PayChannelEnum {
    BITE(1, "比特支付"),
    AOCHUANG(2, "奥创支付"),
    MAYA(3, "玛雅支付"),
    FUGUI(4, "富贵支付");

    private Integer value;
    private String desc;

    PayChannelEnum(Integer value, String desc) {
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
