package com.summer.dao.entity;

import java.util.Date;

public class OrderRepayment {
    private Integer id;

    private Integer userId;

    private Integer borrowId;

    private Integer repaymentAmount;

    private Integer paidAmount;

    private Integer principalAmount;

    private Integer feeAmount;

    private Integer lateFee;
    private Integer trueLateFee;
    private Integer truePrincipal;
    private Integer reduceAmount;

    private Short lateFeeApr;

    private Date repaymentTime;

    private Date paidTime;

    private Date lateStartTime;

    private Date lateUpdateTime;

    private Integer lateDay;

    private Date createTime;

    private Date updateTime;

    private String remark;

    private Byte status;

    private Byte collected;

    private Byte paidForward;

    private Byte payType;

    private Date firstRepaymentTime;
    //借款时间
    private Date loanTime;
    //还款倒计时
    private Integer repaymentCountDown;

    private String orderNum;

    public Integer getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Integer reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Integer getTruePrincipal() {
        return truePrincipal;
    }

    public void setTruePrincipal(Integer truePrincipal) {
        this.truePrincipal = truePrincipal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrueLateFee() {
        return trueLateFee;
    }

    public void setTrueLateFee(Integer trueLateFee) {
        this.trueLateFee = trueLateFee;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Integer principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Integer getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getLateFee() {
        return lateFee;
    }

    public void setLateFee(Integer lateFee) {
        this.lateFee = lateFee;
    }

    public Short getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(Short lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Date getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }

    public Date getLateStartTime() {
        return lateStartTime;
    }

    public void setLateStartTime(Date lateStartTime) {
        this.lateStartTime = lateStartTime;
    }

    public Date getLateUpdateTime() {
        return lateUpdateTime;
    }

    public void setLateUpdateTime(Date lateUpdateTime) {
        this.lateUpdateTime = lateUpdateTime;
    }

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getCollected() {
        return collected;
    }

    public void setCollected(Byte collected) {
        this.collected = collected;
    }

    public Byte getPaidForward() {
        return paidForward;
    }

    public void setPaidForward(Byte paidForward) {
        this.paidForward = paidForward;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Date getFirstRepaymentTime() {
        return firstRepaymentTime;
    }

    public void setFirstRepaymentTime(Date firstRepaymentTime) {
        this.firstRepaymentTime = firstRepaymentTime;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Integer getRepaymentCountDown() {
        return repaymentCountDown;
    }

    public void setRepaymentCountDown(Integer repaymentCountDown) {
        this.repaymentCountDown = repaymentCountDown;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

}