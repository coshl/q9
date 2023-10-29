package com.summer.pojo.vo;

import java.math.BigDecimal;

/**
 * Desc:
 * Created by tl on 2019/3/26
 */
public class IndexVO {
    /**
     * 当日放款成功总额  默认：0.00
     */
    private BigDecimal loanedMoney;
    private BigDecimal loanedRate;

    /**
     * 当日放款成功笔数  默认：0
     */
    private Integer loanedNum;
    private Integer applyNum;

    /**
     * 当日还款成功总额  默认：0.00
     */
    private BigDecimal paidMoney;
    private BigDecimal paidRate;

    /**
     * 当日还款成功笔数  默认：0
     */
    private Integer paidNum;

    /**
     * 当日续期成功总额  默认：0.00
     */
    private BigDecimal renewalMoney;
    private BigDecimal renewalRate;

    /**
     * 当日续期成功笔数  默认：0
     */
    private Integer renewalNum;

    /**
     * 当日待还总额  默认：0.00
     */
    private BigDecimal payMoney;
    private BigDecimal payRate;

    /**
     * 当日待还笔数  默认：0
     */
    private Integer payNum;

    /**
     * 当日逾期总额  默认：0.00
     */
    private BigDecimal overdueMoney;
    private BigDecimal overdueRate;

    /**
     * 当日逾期笔数  默认：0
     */
    private Integer overdueNum;


    /**
     * 0未知,1.新用户；2；老用户,3全部  默认：0
     */
    private Byte customerType;

    public IndexVO() {
    }

    public IndexVO(BigDecimal loanedMoney, BigDecimal loanedRate, Integer loanedNum, BigDecimal paidMoney,
                   BigDecimal paidRate, Integer paidNum, BigDecimal renewalMoney, BigDecimal renewalRate,
                   Integer renewalNum, BigDecimal payMoney, BigDecimal payRate, Integer payNum,
                   BigDecimal overdueMoney, BigDecimal overdueRate, Integer overdueNum, Byte customerType, Integer applyNum) {
        this.loanedMoney = loanedMoney;
        this.loanedRate = loanedRate;
        this.loanedNum = loanedNum;
        this.paidMoney = paidMoney;
        this.paidRate = paidRate;
        this.paidNum = paidNum;
        this.renewalMoney = renewalMoney;
        this.renewalRate = renewalRate;
        this.renewalNum = renewalNum;
        this.payMoney = payMoney;
        this.payRate = payRate;
        this.payNum = payNum;
        this.overdueMoney = overdueMoney;
        this.overdueRate = overdueRate;
        this.overdueNum = overdueNum;
        this.customerType = customerType;
        this.applyNum = applyNum;
    }

    public BigDecimal getLoanedMoney() {
        return loanedMoney;
    }

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public void setLoanedMoney(BigDecimal loanedMoney) {
        this.loanedMoney = loanedMoney;
    }

    public BigDecimal getLoanedRate() {
        return loanedRate;
    }

    public void setLoanedRate(BigDecimal loanedRate) {
        this.loanedRate = loanedRate;
    }

    public Integer getLoanedNum() {
        return loanedNum;
    }

    public void setLoanedNum(Integer loanedNum) {
        this.loanedNum = loanedNum;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public BigDecimal getPaidRate() {
        return paidRate;
    }

    public void setPaidRate(BigDecimal paidRate) {
        this.paidRate = paidRate;
    }

    public Integer getPaidNum() {
        return paidNum;
    }

    public void setPaidNum(Integer paidNum) {
        this.paidNum = paidNum;
    }

    public BigDecimal getRenewalMoney() {
        return renewalMoney;
    }

    public void setRenewalMoney(BigDecimal renewalMoney) {
        this.renewalMoney = renewalMoney;
    }

    public BigDecimal getRenewalRate() {
        return renewalRate;
    }

    public void setRenewalRate(BigDecimal renewalRate) {
        this.renewalRate = renewalRate;
    }

    public Integer getRenewalNum() {
        return renewalNum;
    }

    public void setRenewalNum(Integer renewalNum) {
        this.renewalNum = renewalNum;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public Integer getPayNum() {
        return payNum;
    }

    public void setPayNum(Integer payNum) {
        this.payNum = payNum;
    }

    public BigDecimal getOverdueMoney() {
        return overdueMoney;
    }

    public void setOverdueMoney(BigDecimal overdueMoney) {
        this.overdueMoney = overdueMoney;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public Integer getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(Integer overdueNum) {
        this.overdueNum = overdueNum;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }
}
