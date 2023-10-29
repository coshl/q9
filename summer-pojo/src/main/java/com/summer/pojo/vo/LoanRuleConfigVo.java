
package com.summer.pojo.vo;


public class LoanRuleConfigVo {
    /**
     * 规则id
     */
    private Integer id;
    /**
     * 借款期限
     */
    private Integer expire;
    /**
     * 借款利息
     */
    private Double borrowInterest;
    /**
     * 服务费率
     */
    private Double serviceCharge;
    /**
     * 最多借款
     */
    private Integer loanAmount;
    /**
     * 滞纳金额利率（滞纳金额）
     */
    private Double overdueRate;
    /**
     * 高滞纳金额利率（最高滞纳金额）
     */
    private Double highestOverdueRate;
    /**
     * 续期期限
     */
    private Integer renewalExpire;
    /**
     * 续期费率（占总金额的百分比）
     */
    private Double renewalFee;
    /**
     * 0表示无限续期
     */
    private Integer hightestRenewal;
    /**
     * 命中风控（0表示永久不能再借：备注{“ 1以上表示命中风控XX天后能再借 ” }）
     */
    private Integer hitRiskAllowBorrowDay;
    /**
     * 命中黑名单（0表示永久不能再借 ：备注{“ 1以上表示命中黑名单XX天后能再借 ” }）
     */
    private Integer hitBlackAllowBorrowDay;
    /**
     * 正常还款是否可以复贷（0表示是，1表示否）
     */
    private Integer normalRepaymentRepetitionLoan;
    /**
     * 逾期还款是否可以复借 （0表示否 1表示是）
     */
    private Integer isAllowLoan;
    /**
     * 当isAllowLoan=1时，显示该值：小于 7     （7为可调数据）
     */
    private Integer overdueDay;
    /**
     * 续期开关
     */
    private BackConfigParamsVo renewalSwitch;
    /**
     * 支付开关
     */
    private BackConfigParamsVo repaySwitch;
    /**
     * 支付开关
     */
    private BackConfigParamsVo interest;

    public BackConfigParamsVo getInterest() {
        return interest;
    }

    public void setInterest(BackConfigParamsVo interest) {
        this.interest = interest;
    }

    public BackConfigParamsVo getRepaySwitch() {
        return repaySwitch;
    }

    public void setRepaySwitch(BackConfigParamsVo repaySwitch) {
        this.repaySwitch = repaySwitch;
    }

    public BackConfigParamsVo getRenewalSwitch() {
        return renewalSwitch;
    }

    public void setRenewalSwitch(BackConfigParamsVo renewalSwitch) {
        this.renewalSwitch = renewalSwitch;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public Double getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(Double borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(Double overdueRate) {
        this.overdueRate = overdueRate;
    }

    public Double getHighestOverdueRate() {
        return highestOverdueRate;
    }

    public void setHighestOverdueRate(Double highestOverdueRate) {
        this.highestOverdueRate = highestOverdueRate;
    }

    public Integer getRenewalExpire() {
        return renewalExpire;
    }

    public void setRenewalExpire(Integer renewalExpire) {
        this.renewalExpire = renewalExpire;
    }

    public Double getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(Double renewalFee) {
        this.renewalFee = renewalFee;
    }

    public Integer getHightestRenewal() {
        return hightestRenewal;
    }

    public void setHightestRenewal(Integer hightestRenewal) {
        this.hightestRenewal = hightestRenewal;
    }

    public Integer getHitRiskAllowBorrowDay() {
        return hitRiskAllowBorrowDay;
    }

    public void setHitRiskAllowBorrowDay(Integer hitRiskAllowBorrowDay) {
        this.hitRiskAllowBorrowDay = hitRiskAllowBorrowDay;
    }

    public Integer getHitBlackAllowBorrowDay() {
        return hitBlackAllowBorrowDay;
    }

    public void setHitBlackAllowBorrowDay(Integer hitBlackAllowBorrowDay) {
        this.hitBlackAllowBorrowDay = hitBlackAllowBorrowDay;
    }

    public Integer getNormalRepaymentRepetitionLoan() {
        return normalRepaymentRepetitionLoan;
    }

    public void setNormalRepaymentRepetitionLoan(Integer normalRepaymentRepetitionLoan) {
        this.normalRepaymentRepetitionLoan = normalRepaymentRepetitionLoan;
    }

    public Integer getIsAllowLoan() {
        return isAllowLoan;
    }

    public void setIsAllowLoan(Integer isAllowLoan) {
        this.isAllowLoan = isAllowLoan;
    }

    public Integer getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(Integer overdueDay) {
        this.overdueDay = overdueDay;
    }
}

