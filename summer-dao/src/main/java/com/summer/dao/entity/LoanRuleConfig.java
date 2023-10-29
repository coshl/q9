/* https://github.com/12641561 */
package com.summer.dao.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class LoanRuleConfig {
    /**
     */
    private Integer id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 借款金额  默认：150000
     */
    private Integer loanAmount;

    /**
     * 借款期限（天）  默认：7
     */
    private Integer expire;

    /**
     * 信审查费率（占总金额的百分比 :暂未使用）  默认：0.2
     */
    private Double inquire;

    /**
     * 账户管理费率（占总金额的百分比：暂未使用）  默认：0.143
     */
    private Double accountManagement;

    /**
     * 服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费）  默认：0.343
     */
    private Double serviceCharge;

    /**
     * 借款利息利率（占总金额的百分比）  默认：0.007
     */
    private Double borrowInterest;

    /**
     * 逾期一天的利率（占总金额的百分比）用在滞纳金额计算：滞纳金额=借款金额*逾期一天的利率*逾期天数
     */
    private Double overdueRate;

    /**
     * 最高滞纳金额的（滞纳金额 < 最高滞纳金额）
     */
    private Double highestOverdueRate;

    /**
     * 续期期限（天）  默认：6
     */
    private Integer renewalExpire;

    /**
     * 续期金额（续期的手续费）
     */
    private Double renewalFee;
    /**
     * 最多续期
     */
    private Integer hightestRenewal;


    /**
     * 命中风控多少天后才能再借（天）0表示永远不能再借
     */
    private Integer hitRiskAllowBorrowDay;

    /**
     * 命中黑明单后多少天后才能再借（天）0表示久不能再借
     */
    private Integer hitBlackAllowBorrowDay;

    /**
     * 正常还款是否可以复贷：0表示"是" 1表示不能否  默认：0
     */
    private Integer normalRepaymentRepetitionLoan;

    /**
     * 逾期7天以下还款是否可以复贷 0表示否 1表示是
     */
    private String overdueRepaymentRepetitionLoan;

    /**
     * 创建时间  默认：1970-01-01 08:00:01
     */
    private Date createTime;

    /**
     * 更新时间  默认：1970-01-01 08:00:01
     */
    private Date updateTime;

    /**
     * 0: 无效,1:有效  默认：1
     */
    private Integer status;

    /**
     * 逾期7天以下还款是否可以复贷 0表示否 1表示是
     */
    private Integer isAllowLoan;
    /**
     * 逾期XX天以下允许借款
     */
    private Integer overdueDay;
    /**
     * 渠道id
     */
    private Integer channelId;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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

    /**
     * 获取 loan_rule_config.id
     *
     * @return loan_rule_config.id
     */

    public Integer getId() {
        return id;
    }

    /**
     * 设置 loan_rule_config.id
     *
     * @param id loan_rule_config.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 产品名称 loan_rule_config.product_name
     *
     * @return 产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置 产品名称 loan_rule_config.product_name
     *
     * @param productName 产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * 获取 借款金额 loan_rule_config.loan_amount
     *
     * @return 借款金额
     */
    public Integer getLoanAmount() {
        return loanAmount;
    }

    /**
     * 设置 借款金额 loan_rule_config.loan_amount
     *
     * @param loanAmount 借款金额
     */
    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
     * 获取 借款期限（天） loan_rule_config.expire
     *
     * @return 借款期限（天）
     */
    public Integer getExpire() {
        return expire;
    }

    /**
     * 设置 借款期限（天） loan_rule_config.expire
     *
     * @param expire 借款期限（天）
     */
    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    /**
     * 获取 信审查费率（占总金额的百分比 :暂未使用） loan_rule_config.inquire
     *
     * @return 信审查费率（占总金额的百分比 :暂未使用）
     */
    public Double getInquire() {
        return inquire;
    }

    /**
     * 设置 信审查费率（占总金额的百分比 :暂未使用） loan_rule_config.inquire
     *
     * @param inquire 信审查费率（占总金额的百分比 :暂未使用）
     */
    public void setInquire(Double inquire) {
        this.inquire = inquire;
    }

    /**
     * 获取 账户管理费率（占总金额的百分比：暂未使用） loan_rule_config.account_management
     *
     * @return 账户管理费率（占总金额的百分比：暂未使用）
     */
    public Double getAccountManagement() {
        return accountManagement;
    }

    /**
     * 设置 账户管理费率（占总金额的百分比：暂未使用） loan_rule_config.account_management
     *
     * @param accountManagement 账户管理费率（占总金额的百分比：暂未使用）
     */
    public void setAccountManagement(Double accountManagement) {
        this.accountManagement = accountManagement;
    }

    /**
     * 获取 服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费） loan_rule_config.service_charge
     *
     * @return 服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费）
     */
    public Double getServiceCharge() {
        return serviceCharge;
    }

    /**
     * 设置 服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费） loan_rule_config.service_charge
     *
     * @param serviceCharge 服务费（占总金额的百分比：服务费取代了之前的信审查费率+账户管理费）
     */
    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * 获取 借款利息利率（占总金额的百分比） loan_rule_config.borrow_interest
     *
     * @return 借款利息利率（占总金额的百分比）
     */
    public Double getBorrowInterest() {
        return borrowInterest;
    }

    /**
     * 设置 借款利息利率（占总金额的百分比） loan_rule_config.borrow_interest
     *
     * @param borrowInterest 借款利息利率（占总金额的百分比）
     */
    public void setBorrowInterest(Double borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    /**
     * 获取 逾期一天的利率（占总金额的百分比）用在滞纳金额计算：滞纳金额=借款金额*逾期一天的利率*逾期天数 loan_rule_config.overdue_rate
     *
     * @return 逾期一天的利率（占总金额的百分比）用在滞纳金额计算：滞纳金额=借款金额*逾期一天的利率*逾期天数
     */
    public Double getOverdueRate() {
        return overdueRate;
    }

    /**
     * 设置 逾期一天的利率（占总金额的百分比）用在滞纳金额计算：滞纳金额=借款金额*逾期一天的利率*逾期天数 loan_rule_config.overdue_rate
     *
     * @param overdueRate 逾期一天的利率（占总金额的百分比）用在滞纳金额计算：滞纳金额=借款金额*逾期一天的利率*逾期天数
     */
    public void setOverdueRate(Double overdueRate) {
        this.overdueRate = overdueRate;
    }

    /**
     * 获取 最高滞纳金额的（滞纳金额 < 最高滞纳金额） loan_rule_config.highest_overdue_rate
     *
     * @return 最高滞纳金额的（滞纳金额 < 最高滞纳金额）
     */
    public Double getHighestOverdueRate() {
        return highestOverdueRate;
    }

    /**
     * 设置 最高滞纳金额的（滞纳金额 < 最高滞纳金额） loan_rule_config.highest_overdue_rate
     *
     * @param highestOverdueRate 最高滞纳金额的（滞纳金额 < 最高滞纳金额）
     */
    public void setHighestOverdueRate(Double highestOverdueRate) {
        this.highestOverdueRate = highestOverdueRate;
    }

    /**
     * 获取 续期期限（天） loan_rule_config.renewal_expire
     *
     * @return 续期期限（天）
     */
    public Integer getRenewalExpire() {
        return renewalExpire;
    }

    /**
     * 设置 续期期限（天） loan_rule_config.renewal_expire
     *
     * @param renewalExpire 续期期限（天）
     */
    public void setRenewalExpire(Integer renewalExpire) {
        this.renewalExpire = renewalExpire;
    }

    /**
     * 获取 续期金额（续期的手续费） loan_rule_config.renewal_fee
     *
     * @return 续期金额（续期的手续费）
     */
    public Double getRenewalFee() {
        return renewalFee;
    }

    /**
     * 设置 续期金额（续期的手续费） loan_rule_config.renewal_fee
     *
     * @param renewalFee 续期金额（续期的手续费）
     */
    public void setRenewalFee(Double renewalFee) {
        this.renewalFee = renewalFee;
    }

    /**
     * 获取 命中风控多少天后才能再借（天）0表示永远不能再借 loan_rule_config.hit_risk_allow_borrow_day
     *
     * @return 命中风控多少天后才能再借（天）0表示永远不能再借
     */
    public Integer getHitRiskAllowBorrowDay() {
        return hitRiskAllowBorrowDay;
    }

    /**
     * 设置 命中风控多少天后才能再借（天）0表示永远不能再借 loan_rule_config.hit_risk_allow_borrow_day
     *
     * @param hitRiskAllowBorrowDay 命中风控多少天后才能再借（天）0表示永远不能再借
     */
    public void setHitRiskAllowBorrowDay(Integer hitRiskAllowBorrowDay) {
        this.hitRiskAllowBorrowDay = hitRiskAllowBorrowDay;
    }

    /**
     * 获取 命中黑明单后多少天后才能再借（天）0表示久不能再借 loan_rule_config.hit_black_allow_borrow_day
     *
     * @return 命中黑明单后多少天后才能再借（天）0表示久不能再借
     */
    public Integer getHitBlackAllowBorrowDay() {
        return hitBlackAllowBorrowDay;
    }

    /**
     * 设置 命中黑明单后多少天后才能再借（天）0表示久不能再借 loan_rule_config.hit_black_allow_borrow_day
     *
     * @param hitBlackAllowBorrowDay 命中黑明单后多少天后才能再借（天）0表示久不能再借
     */
    public void setHitBlackAllowBorrowDay(Integer hitBlackAllowBorrowDay) {
        this.hitBlackAllowBorrowDay = hitBlackAllowBorrowDay;
    }

    /**
     * 获取 正常还款是否可以复贷：0表示"是" 1表示不能否 loan_rule_config.normal_repayment_repetition_loan
     *
     * @return 正常还款是否可以复贷：0表示"是" 1表示不能否
     */
    public Integer getNormalRepaymentRepetitionLoan() {
        return normalRepaymentRepetitionLoan;
    }

    /**
     * 设置 正常还款是否可以复贷：0表示"是" 1表示不能否 loan_rule_config.normal_repayment_repetition_loan
     *
     * @param normalRepaymentRepetitionLoan 正常还款是否可以复贷：0表示"是" 1表示不能否
     */
    public void setNormalRepaymentRepetitionLoan(Integer normalRepaymentRepetitionLoan) {
        this.normalRepaymentRepetitionLoan = normalRepaymentRepetitionLoan;
    }

    /**
     * 获取 逾期7天以下还款是否可以复贷 0表示否 1表示是 loan_rule_config.overdue_repayment_repetition_loan
     *
     * @return 逾期7天以下还款是否可以复贷 0表示否 1表示是
     */
    public String getOverdueRepaymentRepetitionLoan() {
        return overdueRepaymentRepetitionLoan;
    }

    /**
     * 设置 逾期7天以下还款是否可以复贷 0表示否 1表示是 loan_rule_config.overdue_repayment_repetition_loan
     *
     * @param overdueRepaymentRepetitionLoan 逾期7天以下还款是否可以复贷 0表示否 1表示是
     */
    public void setOverdueRepaymentRepetitionLoan(String overdueRepaymentRepetitionLoan) {
        this.overdueRepaymentRepetitionLoan = overdueRepaymentRepetitionLoan == null ? null : overdueRepaymentRepetitionLoan.trim();
    }

    /**
     * 获取 创建时间 loan_rule_config.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 loan_rule_config.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 loan_rule_config.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 loan_rule_config.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 0: 无效,1:有效 loan_rule_config.status
     *
     * @return 0: 无效,1:有效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 0: 无效,1:有效 loan_rule_config.status
     *
     * @param status 0: 无效,1:有效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHightestRenewal() {
        return hightestRenewal;
    }

    public void setHightestRenewal(Integer hightestRenewal) {
        this.hightestRenewal = hightestRenewal;
    }
}