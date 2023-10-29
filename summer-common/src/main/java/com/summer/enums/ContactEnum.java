package com.summer.enums;

/**
 * 第一联系人关系
 */
public enum ContactEnum {

    //0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹
    UNKNOWN(0, "未知"),
    FATHER(1, "父亲"),
    MOTHER(2, "母亲"),
    SON(3, "儿子"),
    DAUGHTER(4, "女儿"),
    SPOUSE(5, "配偶"),
    BROTHER(6, "兄弟"),
    SISTER(7, "姐妹");

    private Integer code;

    private String name;

    ContactEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        for (ContactEnum en : ContactEnum.values()) {
            if (en.code.equals(code)) {
                return en.name;
            }
        }
        return "未知";
    }

    public static Integer getCode(String name) {
        for (ContactEnum en : ContactEnum.values()) {
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
