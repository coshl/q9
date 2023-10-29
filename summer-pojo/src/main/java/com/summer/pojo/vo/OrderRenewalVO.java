package com.summer.pojo.vo;

public class OrderRenewalVO {
    /**
     * 客户类型：0:新用户；1:老用户
     */
    private Integer customerType;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 应还金额
     */
    private Long expireAmount;

    /**
     * 已付续期金额
     */
    private Long renewalAmount;

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getExpireAmount() {
        return expireAmount;
    }

    public void setExpireAmount(Long expireAmount) {
        this.expireAmount = expireAmount;
    }

    public Long getRenewalAmount() {
        return renewalAmount;
    }

    public void setRenewalAmount(Long renewalAmount) {
        this.renewalAmount = renewalAmount;
    }
}
