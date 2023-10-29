package com.summer.dao.entity;

import java.util.Date;

public class OrderRenewal {
    private Long id;

    private Integer userId;

    private Integer repaymentId;

    private Integer borrowId;

    private Integer repaymentPrincipal;

    private Integer repaymentFee;

    private Integer lateFee;

    private Integer renewalFee;

    private Date oldRepaymentTime;

    private Byte renewalDay;

    private String remark;

    private Byte status;

    private Byte payType;

    private Byte renewalNo;

    private Integer moneyAmount;
    private Integer reviewUserId;

    private Date repaymentTime;

    private Date createTime;

    private Date updateTime;

    private Integer lateDay;

    private String outTradeNo;
    private String third;
    private String payUrl;
    private Byte sign;

    //续期类型
    private Integer renewalType;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public Integer getRenewalType() {
        return renewalType;
    }

    public void setRenewalType(Integer renewalType) {
        this.renewalType = renewalType;
    }

    public Integer getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Integer reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Integer getRepaymentPrincipal() {
        return repaymentPrincipal;
    }

    public void setRepaymentPrincipal(Integer repaymentPrincipal) {
        this.repaymentPrincipal = repaymentPrincipal;
    }

    public Integer getRepaymentFee() {
        return repaymentFee;
    }

    public void setRepaymentFee(Integer repaymentFee) {
        this.repaymentFee = repaymentFee;
    }

    public Integer getLateFee() {
        return lateFee;
    }

    public void setLateFee(Integer lateFee) {
        this.lateFee = lateFee;
    }

    public Integer getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(Integer renewalFee) {
        this.renewalFee = renewalFee;
    }

    public Date getOldRepaymentTime() {
        return oldRepaymentTime;
    }

    public void setOldRepaymentTime(Date oldRepaymentTime) {
        this.oldRepaymentTime = oldRepaymentTime;
    }

    public Byte getRenewalDay() {
        return renewalDay;
    }

    public void setRenewalDay(Byte renewalDay) {
        this.renewalDay = renewalDay;
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

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Byte getRenewalNo() {
        return renewalNo;
    }

    public void setRenewalNo(Byte renewalNo) {
        this.renewalNo = renewalNo;
    }

    public Integer getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Integer moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
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

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public Byte getSign() {
        return sign;
    }

    public void setSign(Byte sign) {
        this.sign = sign;
    }
}