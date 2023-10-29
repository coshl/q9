package com.summer.service.impl.black;

/**
 *风控收费类型
 */
public enum JuGuangEnum {
    /**
     * 今日到期
     */
    TODAY("今日到期",9 ),
    /**
     * 申请
     */
    APPLY("申请", 8),
    /**
     * 借贷
     */
    LOAN("借贷", 1),
    /**
     * 消贷
     */
    REPAY("消贷", 0),
    /**
     * 续期
     */
    CONTINUE("续期", 2);
    /**
     * 名称
     */
    private final String name;

    /**
     * 值
     */
    private final Integer value;

    JuGuangEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取名称
     *
     */
    public String getName() {
        return name;
    }

    /**
     * 获取值
     *
     */
    public Integer getValue() {
        return value;
    }

}
