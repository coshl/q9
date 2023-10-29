package com.summer.pojo.vo;

import com.summer.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * CountCollectionAssessmentVO
 * Description:
 *
 * @author : GeZhuo
 * Date: 2019/3/4
 */
public class CountCollectionAssessmentVO {
    /**
     * 催收员姓名
     */
    private String personName;

    /**
     * 订单总数  默认：0
     */
    private Integer orderTotal;

    /**
     * 本金金额，元
     */
    private BigDecimal loanMoney;

    /**
     * 订单还款率
     */
    private BigDecimal repaymentOrderRate;

    /**
     * 已还金额，元
     */
    private BigDecimal repaymentMoney;

    /**
     * 利息已还款，元
     */
    private BigDecimal repaymentInterest;

    /**
     * 滞纳金已还款，元
     */
    private BigDecimal repaymentPenalty;

    /**
     * 统计时间
     */
    private String countDate;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRepaymentOrderRate() {
        return repaymentOrderRate;
    }

    public void setRepaymentOrderRate(BigDecimal repaymentOrderRate) {
        this.repaymentOrderRate = repaymentOrderRate;
    }

    public BigDecimal getRepaymentMoney() {
        return repaymentMoney;
    }

    public void setRepaymentMoney(BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        ;
    }

    public BigDecimal getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(BigDecimal repaymentInterest) {
        this.repaymentInterest = repaymentInterest.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getRepaymentPenalty() {
        return repaymentPenalty;
    }

    public void setRepaymentPenalty(BigDecimal repaymentPenalty) {
        this.repaymentPenalty = repaymentPenalty.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(Date countDate) {
        this.countDate = DateUtil.formatTimeYmdHms(countDate);
    }

    @Override
    public String toString() {
        return "CountCollectionAssessmentVO{" +
                "personName='" + personName + '\'' +
                ", orderTotal=" + orderTotal +
                ", loanMoney=" + loanMoney +
                ", repaymentOrderRate=" + repaymentOrderRate +
                ", repaymentMoney=" + repaymentMoney +
                ", repaymentInterest=" + repaymentInterest +
                ", repaymentPenalty=" + repaymentPenalty +
                ", countDate='" + countDate + '\'' +
                '}';
    }
}
