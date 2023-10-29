package com.summer.enums;

/**
 * 运营商通道
 */
public enum MobileSwitch {
    FUYGS("1", "fuygs运营商"),
    XINGPAN("2", "天创运营商,线下不走风控"),
    BOX("3", "大圣运营商,线上有风控分"),
    WUHUA("4", "五花运营商,线下不走风控");

    private String value;
    private String desc;

    MobileSwitch(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
