/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CollectionStatistics {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 统计时间
     */
    private Date countDate;

    /**
     * 催收员角色id
     */
    private Integer userId;

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

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 获取 主键 collection_statistics.id
     *
     * @return 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 collection_statistics.id
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 统计时间 collection_statistics.count_date
     *
     * @return 统计时间
     */
    public Date getCountDate() {
        return countDate;
    }

    /**
     * 设置 统计时间 collection_statistics.count_date
     *
     * @param countDate 统计时间
     */
    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }

    /**
     * 获取 催收员角色id collection_statistics.user_id
     *
     * @return 催收员角色id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 催收员角色id collection_statistics.user_id
     *
     * @param userId 催收员角色id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 分配订单数 collection_statistics.distribution_number
     *
     * @return 分配订单数
     */
    public Integer getDistributionNumber() {
        return distributionNumber;
    }

    /**
     * 设置 分配订单数 collection_statistics.distribution_number
     *
     * @param distributionNumber 分配订单数
     */
    public void setDistributionNumber(Integer distributionNumber) {
        this.distributionNumber = distributionNumber;
    }

    /**
     * 获取 催回订单数 collection_statistics.repayment_number
     *
     * @return 催回订单数
     */
    public Integer getRepaymentNumber() {
        return repaymentNumber;
    }

    /**
     * 设置 催回订单数 collection_statistics.repayment_number
     *
     * @param repaymentNumber 催回订单数
     */
    public void setRepaymentNumber(Integer repaymentNumber) {
        this.repaymentNumber = repaymentNumber;
    }

    /**
     * 获取 订单还款率 collection_statistics.repayment_number_rate
     *
     * @return 订单还款率
     */
    public BigDecimal getRepaymentNumberRate() {
        return repaymentNumberRate;
    }

    /**
     * 设置 订单还款率 collection_statistics.repayment_number_rate
     *
     * @param repaymentNumberRate 订单还款率
     */
    public void setRepaymentNumberRate(BigDecimal repaymentNumberRate) {
        this.repaymentNumberRate = repaymentNumberRate;
    }

    /**
     * 获取 应还金额 collection_statistics.distribution_amount
     *
     * @return 应还金额
     */
    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    /**
     * 设置 应还金额 collection_statistics.distribution_amount
     *
     * @param distributionAmount 应还金额
     */
    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    /**
     * 获取 催回金额 collection_statistics.repayment_amount
     *
     * @return 催回金额
     */
    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    /**
     * 设置 催回金额 collection_statistics.repayment_amount
     *
     * @param repaymentAmount 催回金额
     */
    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    /**
     * 获取 金额催回率 collection_statistics.repayment_amount_rate
     *
     * @return 金额催回率
     */
    public BigDecimal getRepaymentAmountRate() {
        return repaymentAmountRate;
    }

    /**
     * 设置 金额催回率 collection_statistics.repayment_amount_rate
     *
     * @param repaymentAmountRate 金额催回率
     */
    public void setRepaymentAmountRate(BigDecimal repaymentAmountRate) {
        this.repaymentAmountRate = repaymentAmountRate;
    }

    /**
     * 获取 更新时间 collection_statistics.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 collection_statistics.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}