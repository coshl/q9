package com.summer.enums;

/**
 * 分配给客服的当日应还订单列表的备注
 */
public enum RemarkEnum {

    // 数值与对应状态
    FOLLOWED(0, "待跟进"),
    NOT_ANSWERED(1, "未接听"),
    ABNORMAL(2, "号码异常"),
    HANGUP(3, "直接挂断"),
    PROCESS_LATER(4, "稍后处理"),
    SHUTDOWN(5, "关机"),
    EMPTY(6, "空号"),
    DOWNTIME(7, "停机"),
    FRAUD(8, "欺诈"),
    UNP(9, "无偿还能力"),
    ARR(10, "各平台欠款"),
    FORGET(11, "忘记还款"),
    REC(12, "近期可还"),
    OTHER(13, "其他");

    private Integer code;

    private String name;

    RemarkEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        for (RemarkEnum en : RemarkEnum.values()) {
            if (en.code.equals(code)) {
                return en.name;
            }
        }
        return null;
    }

    public static Integer getCode(String name) {
        for (RemarkEnum en : RemarkEnum.values()) {
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
