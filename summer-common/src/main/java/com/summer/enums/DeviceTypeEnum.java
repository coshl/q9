package com.summer.enums;

import java.util.Objects;

/**
 * 手机设备类型
 */
public enum DeviceTypeEnum {

    UN_KNOWN(0, "unknown"),
    ANDROID(1, "android"),
    IOS(2, "ios");

    private Integer value;
    private String desc;

    DeviceTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDescByValue(Integer value) {
        for (DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()) {
            if (Objects.equals(deviceTypeEnum.value, value)) {
                return deviceTypeEnum.getDesc();
            }
        }
        return UN_KNOWN.getDesc();
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
