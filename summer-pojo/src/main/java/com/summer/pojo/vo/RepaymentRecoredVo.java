package com.summer.pojo.vo;

import java.util.Date;

/**
 * APP 还款记录VO
 */
public class RepaymentRecoredVo {
    //还款id
    private Integer id;
    //还款金额
    private String repaymentAmount;
    //借款时间
    private String loanTime;
    //还款倒计时
    private Integer repaymentCountDown;
    //逾期天数
    private Integer LateDay;
    //实际还款时间
    private String paidTime;
    //状态
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(String repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Integer getRepaymentCountDown() {
        return repaymentCountDown;
    }

    public void setRepaymentCountDown(Integer repaymentCountDown) {
        this.repaymentCountDown = repaymentCountDown;
    }

    public Integer getLateDay() {
        return LateDay;
    }

    public void setLateDay(Integer lateDay) {
        LateDay = lateDay;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(String paidTime) {
        this.paidTime = paidTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
