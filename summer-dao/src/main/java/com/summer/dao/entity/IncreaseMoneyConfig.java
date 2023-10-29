/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class IncreaseMoneyConfig {
    /**
     */
    private Integer id;

    /**
     * 名称
     */
    private String productName;

    /**
     * 次数达到XX次数
     */
    private Integer achieveTimes;

    /**
     * 还款次数达到XX次提额，提升额度的费率（占总金额的百分比）
     */
    private Double increaseMoney;

    /**
     * 还款次数达到XX次降息，降息的费用的费率（占总金额的百分比）
     */
    private Double reductionMoney;

    /**
     * 复贷次数达到XX次提升金额为（在贷款规则表中最多借款金额上减或者加；提额类型2时使用）
     */
    private Integer repetitionInreaseMoney;

    /**
     * 改变利息为（在贷款规则中的利息上减或加 ；类型为2时使用）
     */
    private Double reduceInterest;

    /**
     * 提额类型，（0表示正常还款，1表示逾期还款,2表示复贷次数）  默认：0
     */
    private Byte increaseType;

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
    private Integer riskScore;
    private Double serviceMoney;

    private Integer loanTerm;

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    /**
     * 获取 increase_money_config.id
     *
     * @return increase_money_config.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 increase_money_config.id
     *
     * @param id increase_money_config.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 名称 increase_money_config.product_name
     *
     * @return 名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置 名称 increase_money_config.product_name
     *
     * @param productName 名称
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * 获取 次数达到XX次数 increase_money_config.achieve_times
     *
     * @return 次数达到XX次数
     */
    public Integer getAchieveTimes() {
        return achieveTimes;
    }

    /**
     * 设置 次数达到XX次数 increase_money_config.achieve_times
     *
     * @param achieveTimes 次数达到XX次数
     */
    public void setAchieveTimes(Integer achieveTimes) {
        this.achieveTimes = achieveTimes;
    }

    /**
     * 获取 还款次数达到XX次提额，提升额度的费率（占总金额的百分比） increase_money_config.increase_money
     *
     * @return 还款次数达到XX次提额，提升额度的费率（占总金额的百分比）
     */
    public Double getIncreaseMoney() {
        return increaseMoney;
    }

    /**
     * 设置 还款次数达到XX次提额，提升额度的费率（占总金额的百分比） increase_money_config.increase_money
     *
     * @param increaseMoney 还款次数达到XX次提额，提升额度的费率（占总金额的百分比）
     */
    public void setIncreaseMoney(Double increaseMoney) {
        this.increaseMoney = increaseMoney;
    }

    /**
     * 获取 还款次数达到XX次降息，降息的费用的费率（占总金额的百分比） increase_money_config.reduction_money
     *
     * @return 还款次数达到XX次降息，降息的费用的费率（占总金额的百分比）
     */
    public Double getReductionMoney() {
        return reductionMoney;
    }

    /**
     * 设置 还款次数达到XX次降息，降息的费用的费率（占总金额的百分比） increase_money_config.reduction_money
     *
     * @param reductionMoney 还款次数达到XX次降息，降息的费用的费率（占总金额的百分比）
     */
    public void setReductionMoney(Double reductionMoney) {
        this.reductionMoney = reductionMoney;
    }

    /**
     * 获取 复贷次数达到XX次提升金额为（在贷款规则表中最多借款金额上减或者加；提额类型2时使用） increase_money_config.repetition_inrease_money
     *
     * @return 复贷次数达到XX次提升金额为（在贷款规则表中最多借款金额上减或者加；提额类型2时使用）
     */
    public Integer getRepetitionInreaseMoney() {
        return repetitionInreaseMoney;
    }

    /**
     * 设置 复贷次数达到XX次提升金额为（在贷款规则表中最多借款金额上减或者加；提额类型2时使用） increase_money_config.repetition_inrease_money
     *
     * @param repetitionInreaseMoney 复贷次数达到XX次提升金额为（在贷款规则表中最多借款金额上减或者加；提额类型2时使用）
     */
    public void setRepetitionInreaseMoney(Integer repetitionInreaseMoney) {
        this.repetitionInreaseMoney = repetitionInreaseMoney;
    }

    /**
     * 获取 改变利息为（在贷款规则中的利息上减或加 ；类型为2时使用） increase_money_config.reduce_interest
     *
     * @return 改变利息为（在贷款规则中的利息上减或加 ；类型为2时使用）
     */
    public Double getReduceInterest() {
        return reduceInterest;
    }

    /**
     * 设置 改变利息为（在贷款规则中的利息上减或加 ；类型为2时使用） increase_money_config.reduce_interest
     *
     * @param reduceInterest 改变利息为（在贷款规则中的利息上减或加 ；类型为2时使用）
     */
    public void setReduceInterest(Double reduceInterest) {
        this.reduceInterest = reduceInterest;
    }

    /**
     * 获取 提额类型，（0表示正常还款，1表示逾期还款,2表示复贷次数） increase_money_config.increase_type
     *
     * @return 提额类型，（0表示正常还款，1表示逾期还款,2表示复贷次数）
     */
    public Byte getIncreaseType() {
        return increaseType;
    }

    /**
     * 设置 提额类型，（0表示正常还款，1表示逾期还款,2表示复贷次数） increase_money_config.increase_type
     *
     * @param increaseType 提额类型，（0表示正常还款，1表示逾期还款,2表示复贷次数）
     */
    public void setIncreaseType(Byte increaseType) {
        this.increaseType = increaseType;
    }

    /**
     * 获取 创建时间 increase_money_config.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 increase_money_config.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 increase_money_config.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 increase_money_config.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 0: 无效,1:有效 increase_money_config.status
     *
     * @return 0: 无效,1:有效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 0: 无效,1:有效 increase_money_config.status
     *
     * @param status 0: 无效,1:有效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(Double serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }
}