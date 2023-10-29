package com.summer.enums;

/**
 * risk_credit_user表risk_status字段
 * 当前风控运算状态
 * 1.待机审；
 * 2.机审通过(接口成功)；
 * 3 机审不通过(接口成功)
 * 4，机审建议复审(接口成功，规则计算出需要复审)
 * 5.辅助用户初次额度计算.
 * 6已人工审核；
 * 7人工强制通过，
 * 8.未知异常；
 * 9.征信异常,
 * 10.复审通过，
 * 11.复审不通过,
 * 12.放款驳回,
 * 13，人为，
 * 14.新模型风控中
 */
public enum RiskStatusEnum {

    /**
     * 风控审核通过的状态
     */
    RISK_STATUS_PASS(10, "复审通过"),
    /**
     * 风控审核不通过的状态
     */
    RISK_STATUS_REJUCT(11, "复审不通过");

    private Integer value;
    private String desc;

    RiskStatusEnum(Integer value, String desc) {
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
