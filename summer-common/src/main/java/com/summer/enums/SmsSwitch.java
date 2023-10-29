package com.summer.enums;

/**
 * 短信商
 */
public enum SmsSwitch {

    MENG_MENG(1, "萌萌短信"),
    SMGW(2, "SMGW短信"),
    HAIYU(3, "海鱼短信"),
    JUGUANG(4, "聚光短信"),
    HTTPSMS(5, "HTTP短信"),
    HUAXINYUN(6, "华信云短信"),
    XINMAO(7, "新猫短信");

    private Integer value;
    private String desc;

    SmsSwitch(Integer value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
