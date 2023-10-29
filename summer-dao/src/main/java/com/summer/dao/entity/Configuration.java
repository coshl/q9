package com.summer.dao.entity;

import java.util.Date;

public class Configuration {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 申请金额，单位为分
     */
    private Integer borrowAmount;
    /**
     * 借款期限
     */
    private Integer borrowPeriod;
    /**
     * 信审查询费
     */
    private Integer inquire;
    /**
     * 借款利息
     */
    private Integer borrowInterest;
    /**
     * 账户管理费
     */
    private Integer accountManagement;
    /**
     * 实际利息利率
     */
    private Integer actual;
    /**
     * 到期应还
     */
    private Integer actualPay;
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

    public Integer getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Integer borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Integer getBorrowPeriod() {
        return borrowPeriod;
    }

    public void setBorrowPeriod(Integer borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }

    public Integer getInquire() {
        return inquire;
    }

    public void setInquire(Integer inquire) {
        this.inquire = inquire;
    }

    public Integer getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(Integer borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public Integer getAccountManagement() {
        return accountManagement;
    }

    public void setAccountManagement(Integer accountManagement) {
        this.accountManagement = accountManagement;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        this.actual = actual;
    }

    public Integer getActualPay() {
        return actualPay;
    }

    public void setActualPay(Integer actualPay) {
        this.actualPay = actualPay;
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
}