package com.summer.pojo.vo;


import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.Constant;

/**
 * Desc:
 * Created by tl on 2019/3/15
 */
public class FinanceStatisticVO extends BaseUserDecrypt {

   /* private String realName;
    private String phone;*/
    private String applyTime;
    private String loanTime;
    private Double applyMoney;
    private Double intoMoney;
    private Integer loanTerm;
    private Double interest;
    private Integer reloanTime;
    private String orderNo;

    private String repayTime;
    private Double topayMoney;
    private String repayTypeName;
    private byte payType;
    private Double paidMoney;

    private String renewalTime;
    private Integer renewalTerm;
    private String oldRepayTime;
    private String newRepayTime;
    private String renewalTypeName;
    private Double renewalFee;

    private String statisticTime;
    private String backuserName;
    private String channelName;
    private Double price;
    private Integer registerNum;
    private Double expenseAmount;

    private Integer status;

    private Integer loanCost;

    private Integer cooperationMode;

    public String getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(String statisticTime) {
        this.statisticTime = statisticTime;
    }

    public String getBackuserName() {
        return backuserName;
    }

    public void setBackuserName(String backuserName) {
        this.backuserName = backuserName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

    public Double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(Double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }


    public String getRenewalTime() {
        return renewalTime;
    }

    public void setRenewalTime(String renewalTime) {
        this.renewalTime = renewalTime;
    }

    public Integer getRenewalTerm() {
        return renewalTerm;
    }

    public void setRenewalTerm(Integer renewalTerm) {
        this.renewalTerm = renewalTerm;
    }

    public String getOldRepayTime() {
        return oldRepayTime;
    }

    public void setOldRepayTime(String oldRepayTime) {
        this.oldRepayTime = oldRepayTime;
    }

    public String getNewRepayTime() {
        return newRepayTime;
    }

    public void setNewRepayTime(String newRepayTime) {
        this.newRepayTime = newRepayTime;
    }

    public String getRenewalTypeName() {
        return renewalTypeName;
    }

    public Double getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(Double renewalFee) {
        this.renewalFee = renewalFee;
    }

    public byte getPayType() {
        return payType;
    }

    public void setPayType(byte payType) {
        this.payType = payType;
        this.repayTypeName = Constant.PAY_TYPE_MAP.get(payType);
        this.renewalTypeName = Constant.PAY_TYPE_MAP.get(payType);
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public Double getTopayMoney() {
        return topayMoney;
    }

    public void setTopayMoney(Double topayMoney) {
        this.topayMoney = topayMoney;
    }

    public String getRepayTypeName() {
        return repayTypeName;
    }


    public Double getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(Double paidMoney) {
        this.paidMoney = paidMoney;
    }
/*
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }*/

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public Double getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(Double applyMoney) {
        this.applyMoney = applyMoney;
    }

    public Double getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(Double intoMoney) {
        this.intoMoney = intoMoney;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Integer getReloanTime() {
        return reloanTime;
    }

    public void setReloanTime(Integer reloanTime) {
        if (null != reloanTime && reloanTime > 0) {
            reloanTime--;
        }
        this.reloanTime = reloanTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLoanCost() {
        return loanCost;
    }

    public void setLoanCost(Integer loanCost) {
        this.loanCost = loanCost;
    }

    public Integer getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(Integer cooperationMode) {
        this.cooperationMode = cooperationMode;
    }
}
