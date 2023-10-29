package com.summer.enums;

/**
 * 新/老客
 */
public enum CustomTypeEnum {
    OLD_CUSTOM(1, (byte) 1, "老用户"),
    NEW_CUSTOM(0, (byte) 0, "新用户");

    private Integer value;
    private Byte valueByte;
    private String desc;

    CustomTypeEnum(Integer value, Byte valueByte, String desc) {
        this.value = value;
        this.valueByte = valueByte;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public Byte getValueByte() {
        return valueByte;
    }

    public String getDesc() {
        return desc;
    }
}
