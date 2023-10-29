package com.summer.enums;

/**
 * 订单状态枚举值
 * -1:,
 * 0:待初审(待机审);
 * 1:初审驳回;
 * 2初审通过;
 * 3待人工审核
 * 4复审机审驳回,
 * 5复审人审拒绝;;
 * 6:放款中;
 * 7:放款失败;
 * 8已放款，还款中;
 * 9:部分还款;
 * 10:已还款;
 * 11:已逾期;
 * 12:已坏账，
 * 13逾期已还款；
 */
public enum OrderBorrowStatusEnum {
    CANCEL_SEND_LOAN(-1, "取消放款"),
    FIRST_AUDIT_WAIT(0, "待初审"),
    FIRST_AUDIT_REJECT(1, "初审驳回"),
    FIRST_AUDIT_PASS(2, "初审通过"),
    RE_MANUAL_AUDIT_WAIT(3, "待复审人工审核"),
    RE_MACHINE_AUDIT_REJECT(4, "复审机审驳回"),
    RE_MANUAL_AUDIT_REJECT(5, "复审人审拒绝"),
    SEND_LOAN_RUN(6, "放款中"),
    SEND_LOAN_FAIL(7, "放款失败"),
    SEND_LOAN_SUCCESS(8, "已放款，还款中"),
    PAYMENT_A_LITTLE(9, "部分还款"),
    PAYMENT_THE_END(10, "已还款"),
    OVERDUE(11, "已逾期"),
    BAD_LOAN(12, "已坏账"),
    OVERDUE_PAYMENT_THE_END(13, "逾期已还款");

    private Integer value;
    private String desc;

    OrderBorrowStatusEnum(Integer value, String desc) {
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
