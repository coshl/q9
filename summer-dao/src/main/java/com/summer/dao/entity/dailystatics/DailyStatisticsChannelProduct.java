/* https://github.com/12641561 */
package com.summer.dao.entity.dailystatics;

import java.math.BigDecimal;
import java.util.Date;

public class DailyStatisticsChannelProduct {
    /**
     */
    private Integer id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 渠道运营人员id
     */
    private Integer plateformUserId;

    /**
     * 独立访问量  默认：0
     */
    private Integer uv;

    /**
     * 点击量  默认：0
     */
    private Integer pv;

    /**
     * 注册量  默认：0
     */
    private Integer registerNumber;

    /**
     * 扣量后的注册数  默认：0
     */
    private Integer registerNumberChannel;

    /**
     * 当日扣量系数  默认：0.00
     */
    private BigDecimal factor;

    /**
     * 申请人数  默认：0
     */
    private Integer pplicationNumber;

    /**
     * 渠道结算费用 单位为分  默认：0
     */
    private Integer sumChannelCost;

    /**
     * 放款量  默认：0
     */
    private Integer loanNumberChannel;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 统计时间
     */
    private Date reportTime;
    //合作价格
    private Integer price;
    //合作模式：0 cpa, 1 cps ,2 其他,3,uv
    private Integer cooperationMode;


    public Integer getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(Integer cooperationMode) {
        this.cooperationMode = cooperationMode;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * 获取 daily_statistics_channel_product.id
     *
     * @return daily_statistics_channel_product.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 daily_statistics_channel_product.id
     *
     * @param id daily_statistics_channel_product.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 产品名称 daily_statistics_channel_product.product_name
     *
     * @return 产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置 产品名称 daily_statistics_channel_product.product_name
     *
     * @param productName 产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * 获取 渠道id daily_statistics_channel_product.channel_id
     *
     * @return 渠道id
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置 渠道id daily_statistics_channel_product.channel_id
     *
     * @param channelId 渠道id
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * 获取 渠道运营人员id daily_statistics_channel_product.plateform_user_id
     *
     * @return 渠道运营人员id
     */
    public Integer getPlateformUserId() {
        return plateformUserId;
    }

    /**
     * 设置 渠道运营人员id daily_statistics_channel_product.plateform_user_id
     *
     * @param plateformUserId 渠道运营人员id
     */
    public void setPlateformUserId(Integer plateformUserId) {
        this.plateformUserId = plateformUserId;
    }

    /**
     * 获取 独立访问量 daily_statistics_channel_product.uv
     *
     * @return 独立访问量
     */
    public Integer getUv() {
        return uv;
    }

    /**
     * 设置 独立访问量 daily_statistics_channel_product.uv
     *
     * @param uv 独立访问量
     */
    public void setUv(Integer uv) {
        this.uv = uv;
    }

    /**
     * 获取 点击量 daily_statistics_channel_product.pv
     *
     * @return 点击量
     */
    public Integer getPv() {
        return pv;
    }

    /**
     * 设置 点击量 daily_statistics_channel_product.pv
     *
     * @param pv 点击量
     */
    public void setPv(Integer pv) {
        this.pv = pv;
    }

    /**
     * 获取 注册量 daily_statistics_channel_product.register_number
     *
     * @return 注册量
     */
    public Integer getRegisterNumber() {
        return registerNumber;
    }

    /**
     * 设置 注册量 daily_statistics_channel_product.register_number
     *
     * @param registerNumber 注册量
     */
    public void setRegisterNumber(Integer registerNumber) {
        this.registerNumber = registerNumber;
    }

    /**
     * 获取 扣量后的注册数 daily_statistics_channel_product.register_number_channel
     *
     * @return 扣量后的注册数
     */
    public Integer getRegisterNumberChannel() {
        return registerNumberChannel;
    }

    /**
     * 设置 扣量后的注册数 daily_statistics_channel_product.register_number_channel
     *
     * @param registerNumberChannel 扣量后的注册数
     */
    public void setRegisterNumberChannel(Integer registerNumberChannel) {
        this.registerNumberChannel = registerNumberChannel;
    }

    /**
     * 获取 当日扣量系数 daily_statistics_channel_product.factor
     *
     * @return 当日扣量系数
     */
    public BigDecimal getFactor() {
        return factor;
    }

    /**
     * 设置 当日扣量系数 daily_statistics_channel_product.factor
     *
     * @param factor 当日扣量系数
     */
    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    /**
     * 获取 申请人数 daily_statistics_channel_product.pplication_number
     *
     * @return 申请人数
     */
    public Integer getPplicationNumber() {
        return pplicationNumber;
    }

    /**
     * 设置 申请人数 daily_statistics_channel_product.pplication_number
     *
     * @param pplicationNumber 申请人数
     */
    public void setPplicationNumber(Integer pplicationNumber) {
        this.pplicationNumber = pplicationNumber;
    }

    /**
     * 获取 渠道结算费用 单位为分 daily_statistics_channel_product.sum_channel_cost
     *
     * @return 渠道结算费用 单位为分
     */
    public Integer getSumChannelCost() {
        return sumChannelCost;
    }

    /**
     * 设置 渠道结算费用 单位为分 daily_statistics_channel_product.sum_channel_cost
     *
     * @param sumChannelCost 渠道结算费用 单位为分
     */
    public void setSumChannelCost(Integer sumChannelCost) {
        this.sumChannelCost = sumChannelCost;
    }

    /**
     * 获取 放款量 daily_statistics_channel_product.loan_number_channel
     *
     * @return 放款量
     */
    public Integer getLoanNumberChannel() {
        return loanNumberChannel;
    }

    /**
     * 设置 放款量 daily_statistics_channel_product.loan_number_channel
     *
     * @param loanNumberChannel 放款量
     */
    public void setLoanNumberChannel(Integer loanNumberChannel) {
        this.loanNumberChannel = loanNumberChannel;
    }

    /**
     * 获取 创建时间 daily_statistics_channel_product.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 daily_statistics_channel_product.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 daily_statistics_channel_product.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 daily_statistics_channel_product.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}