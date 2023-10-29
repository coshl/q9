package com.summer.pojo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 贷款规则的dto
 *
 * @author Administrator
 */
public class LoanRuleConfigDto implements Serializable {

    private static final long serialVersionUID = -4754649556546506562L;
    @NotNull(message = "贷款规则id不能为空")
    private Integer id;
    @NotNull(message = "借款期限不能为空")
    private Integer expire;
    @NotNull(message = "最高借款金额不能为空")
    private Integer loanAmount;
    //@NotNull(message = "信审费率不能为空")
    private Double inquire;
    @NotNull(message = "借款利息不能为空")
    private Double borrowInterest;
    //@NotNull(message = "账户管理费不能为空")
    private Double accountManagement;
    @NotNull(message = "逾期一天的利率不能为空")
    private Double overdueRate;
    @NotNull(message = "最高逾期金额利率不能为空")
    private Double highestOverdueRate;
    @NotNull(message = "续期期限不能为空")
    private Integer renewalExpire;
    @NotNull(message = "续期利率不能空")
    private Double renewalFee;

    @NotNull(message = "服务费不能为空")
    private Double serviceCharge;

    /**
     * 命中风控多少天后才能再借（天）0表示永远不能再借
     */
    // @NotNull(message = "命中风控参数不能为空")
    private Integer hitRiskAllowBorrowDay;

    /**
     * 命中黑明单后多少天后才能再借（天）0表示久不能再借
     */
    // @NotNull(message = "命中黑明单参数不能为空")
    private Integer hitBlackAllowBorrowDay;

    /**
     * 正常还款是否可以复贷：0表示"是" 1表示不否  默认：0
     */
    //@NotNull(message = "正常还款是否可以复贷参数不能为空")
    private Integer normalRepaymentRepetitionLoan;


    private String password;

    /**
     * 逾期7天以下还款是否可以复贷 0表示否 1表示是
     */
    //@NotNull(message = "逾期还款是否允许复贷参数不能为空")
    private Integer isAllowLoan;
    /**
     * 逾期XX天以下允许借款
     */
    private Integer overdueDay;
    /**
     * 最多续期
     */
    private Integer hightestRenewal;
    @NotEmpty(message = "提额降息数据不能为空")
    private List<IncreaseMoneyConfigDto> increaseMoneyConfigs = new ArrayList<IncreaseMoneyConfigDto>();

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

    public Double getInquire() {
        return inquire;
    }

    public void setInquire(Double inquire) {
        this.inquire = inquire;
    }

    public Double getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(Double borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public Double getAccountManagement() {
        return accountManagement;
    }

    public void setAccountManagement(Double accountManagement) {
        this.accountManagement = accountManagement;
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


    public List<IncreaseMoneyConfigDto> getIncreaseMoneyConfigs() {
        return increaseMoneyConfigs;
    }

    public void setIncreaseMoneyConfigs(List<IncreaseMoneyConfigDto> increaseMoneyConfigs) {
        this.increaseMoneyConfigs = increaseMoneyConfigs;
    }

    @NotNull
    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(@NotNull Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    @NotNull
    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(@NotNull Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getHightestRenewal() {
        return hightestRenewal;
    }

    public void setHightestRenewal(Integer hightestRenewal) {
        this.hightestRenewal = hightestRenewal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
