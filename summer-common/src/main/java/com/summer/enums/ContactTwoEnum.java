package com.summer.enums;

/**
 * 第二联系人关系
 */
public enum ContactTwoEnum {

    //与第二联系人的关系(0未知1.同学2.亲戚3.同事4.朋友5.其他)
    UNKNOWN(0, "未知"),
    FATHER(1, "同学"),
    MOTHER(2, "亲戚"),
    SON(3, "同事"),
    DAUGHTER(4, "朋友"),
    SPOUSE(5, "其他");

    private Integer code;

    private String name;

    ContactTwoEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        for (ContactTwoEnum en : ContactTwoEnum.values()) {
            if (en.code.equals(code)) {
                return en.name;
            }
        }
        return "未知";
    }

    public static Integer getCode(String name) {
        for (ContactTwoEnum en : ContactTwoEnum.values()) {
            if (en.name.equals(name)) {
                return en.code;
            }
        }
        return 0;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
