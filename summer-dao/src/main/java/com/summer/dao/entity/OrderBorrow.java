package com.summer.dao.entity;

import java.util.Date;

public class OrderBorrow {
    private Integer id;

    private Integer userId;

    private Integer applyAmount;

    private Integer feeApr;

    private Integer loanFee;

    private Integer intoMoney;

    private Byte loanTermType;

    private Integer loanTerm;

    private Date createTime;
    private String applyTimeChg;
    private String cardNo;

    private Date updateTime;

    private Date loanTime;

    private Date loanEndTime;

    private Short lateFeeApr;

    private Integer bankCardId;

    private Date trialTime;

    private String trialRemark;

    private Integer trialUserId;

    private Date reviewTime;

    private String reviewRemark;

    private Integer reviewUserId;

    private Date loanReviewTime;

    private String loanReviewRemark;

    private Integer loanReviewUserId;

    private Integer renewalCount;

    private Byte creditLevel;

    private Byte hitRiskTimes;

    private Byte customerType;

    private Byte status;

    private String payRemark;

    private Byte payStatus;

    private String outTradeNo;
    private String flowNo;

    private String reviewer;

    private UserInfo userInfo;

    //借款服务费率
    private Integer serviceCharge;
    //借款利息利率
    private Integer accrual;

    private Date createDate;

    @Override
    public String toString() {
        return "OrderBorrow{" +
                "id=" + id +
                ", hitRiskTimes=" + hitRiskTimes +
                ", status=" + status +
                ", payStatus=" + payStatus +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", flowNo='" + flowNo + '\'' +
                '}';
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getApplyTimeChg() {
        return applyTimeChg;
    }

    public void setApplyTimeChg(String applyTimeChg) {
        this.applyTimeChg = applyTimeChg;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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

    public Integer getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Integer applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Integer getFeeApr() {
        return feeApr;
    }

    public void setFeeApr(Integer feeApr) {
        this.feeApr = feeApr;
    }

    public Integer getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(Integer loanFee) {
        this.loanFee = loanFee;
    }

    public Integer getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(Integer intoMoney) {
        this.intoMoney = intoMoney;
    }

    public Byte getLoanTermType() {
        return loanTermType;
    }

    public void setLoanTermType(Byte loanTermType) {
        this.loanTermType = loanTermType;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
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

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Date getLoanEndTime() {
        return loanEndTime;
    }

    public void setLoanEndTime(Date loanEndTime) {
        this.loanEndTime = loanEndTime;
    }

    public Short getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(Short lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public Integer getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Integer bankCardId) {
        this.bankCardId = bankCardId;
    }

    public Date getTrialTime() {
        return trialTime;
    }

    public void setTrialTime(Date trialTime) {
        this.trialTime = trialTime;
    }

    public String getTrialRemark() {
        return trialRemark;
    }

    public void setTrialRemark(String trialRemark) {
        this.trialRemark = trialRemark == null ? null : trialRemark.trim();
    }

    public Integer getTrialUserId() {
        return trialUserId;
    }

    public void setTrialUserId(Integer trialUserId) {
        this.trialUserId = trialUserId;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark == null ? null : reviewRemark.trim();
    }

    public Integer getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Integer reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public Date getLoanReviewTime() {
        return loanReviewTime;
    }

    public void setLoanReviewTime(Date loanReviewTime) {
        this.loanReviewTime = loanReviewTime;
    }

    public String getLoanReviewRemark() {
        return loanReviewRemark;
    }

    public void setLoanReviewRemark(String loanReviewRemark) {
        this.loanReviewRemark = loanReviewRemark == null ? null : loanReviewRemark.trim();
    }

    public Integer getLoanReviewUserId() {
        return loanReviewUserId;
    }

    public void setLoanReviewUserId(Integer loanReviewUserId) {
        this.loanReviewUserId = loanReviewUserId;
    }

    public Byte getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(Byte creditLevel) {
        this.creditLevel = creditLevel;
    }

    public Byte getHitRiskTimes() {
        return hitRiskTimes;
    }

    public void setHitRiskTimes(Byte hitRiskTimes) {
        this.hitRiskTimes = hitRiskTimes;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark == null ? null : payRemark.trim();
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public Integer getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Integer serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getAccrual() {
        return accrual;
    }

    public void setAccrual(Integer accrual) {
        this.accrual = accrual;
    }
}