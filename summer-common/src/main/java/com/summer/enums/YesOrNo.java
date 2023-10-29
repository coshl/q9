package com.summer.enums;

public enum YesOrNo {
    YES(1, "1", true, "true"),
    No(0, "0", false, "false");

    private Integer value;
    private String strValue;
    private Boolean boolValue;
    private String desc;

    YesOrNo(Integer value, String strValue, Boolean boolValue, String desc) {
        this.value = value;
        this.strValue = strValue;
        this.boolValue = boolValue;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public String getDesc() {
        return desc;
    }
}
