package com.summer.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户金额费率实体类
 */
public class UserMoneyRate implements Serializable {

    private static final long serialVersionUID = -1039229144689550563L;
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 最大金额
     */
    private Integer maxAmount;
    /**
     * 信审查询费率
     */
    private Double creditVet;
    /**
     * 账户管理费利率
     */
    private Double accountManage;
    /**
     * 实际利息利率
     */
    private Double accrual;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 正常还款次数
     */
    private Integer normalRepaymentTimes;
    /**
     * 逾期还款的次数
     */
    private Integer overdueRepaymentTimes;
    /**
     * 服务费
     */
    private Double serviceCharge;
    /**
     * 复贷次数
     */
    private Integer repetitionTimes;
    /**
     * 原来的系统允许的最大金额 该属性无需对应数据库字段
     */
    private Integer beforeMaxAmount;

    private Integer loanTerm;

    public UserMoneyRate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getCreditVet() {
        return creditVet;
    }

    public void setCreditVet(Double creditVet) {
        this.creditVet = creditVet;
    }

    public Double getAccountManage() {
        return accountManage;
    }

    public void setAccountManage(Double accountManage) {
        this.accountManage = accountManage;
    }

    public Double getAccrual() {
        return accrual;
    }

    public void setAccrual(Double accrual) {
        this.accrual = accrual;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public Integer getNormalRepaymentTimes() {
        return normalRepaymentTimes;
    }

    public void setNormalRepaymentTimes(Integer normalRepaymentTimes) {
        this.normalRepaymentTimes = normalRepaymentTimes;
    }

    public Integer getOverdueRepaymentTimes() {
        return overdueRepaymentTimes;
    }

    public void setOverdueRepaymentTimes(Integer overdueRepaymentTimes) {
        this.overdueRepaymentTimes = overdueRepaymentTimes;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getRepetitionTimes() {
        return repetitionTimes;
    }

    public void setRepetitionTimes(Integer repetitionTimes) {
        this.repetitionTimes = repetitionTimes;
    }

    public Integer getBeforeMaxAmount() {
        return beforeMaxAmount;
    }

    public void setBeforeMaxAmount(Integer beforeMaxAmount) {
        this.beforeMaxAmount = beforeMaxAmount;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }
}

