package com.summer.enums;

/**
 * 人审机审区分
 */
public enum CreditLevelEnum {
    UN_KNOW(0, "未知"),
    MANUAL_AUDIT_PASS(1,"人审通过"),
    MACHINE_AUDIT_PASS(2,"机审通过"),
    MANUAL_AUDIT_REJECTION(3,"人审拒绝"),
    MACHINE_AUDIT_REJECTION(4,"机审拒绝");

    private Integer value;
    private String desc;

    CreditLevelEnum(Integer value, String desc) {
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
