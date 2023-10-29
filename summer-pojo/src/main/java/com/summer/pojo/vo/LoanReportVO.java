package com.summer.pojo.vo;

import java.math.BigDecimal;

/**
 * LoanReportVO
 * Description:
 *
 * @author : GeZhuo
 * Date: 2019/2/27
 */
public class LoanReportVO {

    private Integer loanOrderCount;
    /**
     * 报表日期
     */
    private String reportDate;

    /**
     * 注册人数  默认：0
     */
    private Integer registerCount;

    /**
     * 申请人数  默认：0
     */
    private Integer borrowApplyCount;

    /**
     * 申请率（申请人数/注册人数） 默认：0.0
     */
    private BigDecimal applyRate;

    /**
     * 放款总单数  默认：0
     */
//    private Integer loanOrderCount;

    /**
     * 借款成功人数  默认：0（暂时先注释掉）
     */
    private Integer borrowSucCount;

    /**
     * 放款金额（元）  默认：0
     */
    private BigDecimal moneyAmountCount;

    /**
     * 放款率（借款成功人数/申请人数）默认：0.0
     */
    private BigDecimal borrowRate;

    /**
     * 老用户放款单数  默认：0
     */
    private Integer oldLoanOrderCount;

    /**
     * 老用户放款总额  默认：0
     */
    private Long oldLoanMoneyCount;

    /**
     * 新用户放款单数  默认：0
     */
    private Integer newLoanOrderCount;

    /**
     * 新用户放款金额  默认：0
     */
    private Long newLoanMoneyCount;

    /**
     * 复贷率  （当天续期人数+老客放款人数 / 当日回款总户）
     * */
    private BigDecimal fdl;


    public Integer getLoanOrderCount() {
        return loanOrderCount;
    }

    public void setLoanOrderCount(Integer loanOrderCount) {
        this.loanOrderCount = loanOrderCount;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getBorrowApplyCount() {
        return borrowApplyCount;
    }

    public void setBorrowApplyCount(Integer borrowApplyCount) {
        this.borrowApplyCount = borrowApplyCount;
    }

    public BigDecimal getApplyRate() {
        return applyRate;
    }

    public void setApplyRate(String applyRate) {
        this.applyRate = new BigDecimal(applyRate);
    }

/*    public Integer getLoanOrderCount() {
        return loanOrderCount;
    }

    public void setLoanOrderCount(Integer loanOrderCount) {
        this.loanOrderCount = loanOrderCount;
    }*/

    public Integer getBorrowSucCount() {
        return borrowSucCount;
    }

    public void setBorrowSucCount(Integer borrowSucCount) {
        this.borrowSucCount = borrowSucCount;
    }

    public BigDecimal getMoneyAmountCount() {
        return moneyAmountCount;
    }

    public void setMoneyAmountCount(BigDecimal moneyAmountCount) {
        this.moneyAmountCount = moneyAmountCount;
    }

    public BigDecimal getBorrowRate() {
        return borrowRate;
    }

    public void setBorrowRate(String borrowRate) {
        this.borrowRate = new BigDecimal(borrowRate);
    }

    public Integer getOldLoanOrderCount() {
        return oldLoanOrderCount;
    }

    public void setOldLoanOrderCount(Integer oldLoanOrderCount) {
        this.oldLoanOrderCount = oldLoanOrderCount;
    }

    public Long getOldLoanMoneyCount() {
        return oldLoanMoneyCount;
    }

    public void setOldLoanMoneyCount(Long oldLoanMoneyCount) {
        this.oldLoanMoneyCount = oldLoanMoneyCount;
    }

    public Integer getNewLoanOrderCount() {
        return newLoanOrderCount;
    }

    public void setNewLoanOrderCount(Integer newLoanOrderCount) {
        this.newLoanOrderCount = newLoanOrderCount;
    }

    public Long getNewLoanMoneyCount() {
        return newLoanMoneyCount;
    }

    public void setNewLoanMoneyCount(Long newLoanMoneyCount) {
        this.newLoanMoneyCount = newLoanMoneyCount;
    }


    public BigDecimal getFdl() {
        return fdl;
    }

    public void setFdl(BigDecimal fdl) {
        this.fdl = fdl;
    }
}
