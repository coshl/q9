package com.summer.pojo.vo;

import java.math.BigDecimal;

public class CollectionStatisticsVo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 统计时间
     */
    private String countDate;

    /**
     * 催收员角色id
     */
    private Integer roleId;

    private String roleName;

    /**
     * 催收员姓名
     */
    private String userName;

    /**
     * 分配订单数  默认：0
     */
    private Integer distributionNumber;

    /**
     * 催回订单数  默认：0
     */
    private Integer repaymentNumber;

    /**
     * 订单还款率  默认：0.00
     */
    private BigDecimal repaymentNumberRate;

    /**
     * 应还金额  默认：0.00
     */
    private BigDecimal distributionAmount;

    /**
     * 催回金额  默认：0.00
     */
    private BigDecimal repaymentAmount;

    /**
     * 金额催回率  默认：0.00
     */
    private BigDecimal repaymentAmountRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(String countDate) {
        this.countDate = countDate;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDistributionNumber() {
        return distributionNumber;
    }

    public void setDistributionNumber(Integer distributionNumber) {
        this.distributionNumber = distributionNumber;
    }

    public Integer getRepaymentNumber() {
        return repaymentNumber;
    }

    public void setRepaymentNumber(Integer repaymentNumber) {
        this.repaymentNumber = repaymentNumber;
    }

    public BigDecimal getRepaymentNumberRate() {
        return repaymentNumberRate;
    }

    public void setRepaymentNumberRate(BigDecimal repaymentNumberRate) {
        this.repaymentNumberRate = repaymentNumberRate;
    }

    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getRepaymentAmountRate() {
        return repaymentAmountRate;
    }

    public void setRepaymentAmountRate(BigDecimal repaymentAmountRate) {
        this.repaymentAmountRate = repaymentAmountRate;
    }
}