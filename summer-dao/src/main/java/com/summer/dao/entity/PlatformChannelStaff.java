/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 渠道人员统计
 */
public class PlatformChannelStaff {
    /**
     */
    private Integer id;

    /**
     * 统计时间
     */
    private Date reportTime;

    /**
     * 运营人员id
     */
    private Integer plateformUserId;

    /**
     * 运营人员
     */
    private String userName;

    /**
     * 投放连接数
     */
    private Integer deliveryConnection;

    /**
     * 真实注册数
     */
    private Integer reallyRegister;

    /**
     * 渠道注册数
     */
    private Integer channelRegister;

    /**
     * 放款成本，以分为单位
     */
    private Integer loanCost;
    private Integer price;
    private Integer expense;

    /**
     * 注册成本，以分为单位
     */
    private Integer registerCost;
    private String channelName;
    /**
     * 扣量系数
     */
    private BigDecimal decreasePercentage;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getExpense() {
        return expense;
    }

    public void setExpense(Integer expense) {
        this.expense = expense;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取 platform_channel_staff.id
     *
     * @return platform_channel_staff.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 platform_channel_staff.id
     *
     * @param id platform_channel_staff.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 统计时间 platform_channel_staff.report_time
     *
     * @return 统计时间
     */
    public Date getReportTime() {
        return reportTime;
    }

    /**
     * 设置 统计时间 platform_channel_staff.report_time
     *
     * @param reportTime 统计时间
     */
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * 获取 运营人员 platform_channel_staff.plateform_user_id
     *
     * @return 运营人员
     */
    public Integer getPlateformUserId() {
        return plateformUserId;
    }

    /**
     * 设置 运营人员 platform_channel_staff.plateform_user_id
     *
     * @param plateformUserId 运营人员
     */
    public void setPlateformUserId(Integer plateformUserId) {
        this.plateformUserId = plateformUserId;
    }

    /**
     * 获取 投放连接数 platform_channel_staff.delivery_connection
     *
     * @return 投放连接数
     */
    public Integer getDeliveryConnection() {
        return deliveryConnection;
    }

    /**
     * 设置 投放连接数 platform_channel_staff.delivery_connection
     *
     * @param deliveryConnection 投放连接数
     */
    public void setDeliveryConnection(Integer deliveryConnection) {
        this.deliveryConnection = deliveryConnection;
    }

    /**
     * 获取 真实注册数 platform_channel_staff.really_register
     *
     * @return 真实注册数
     */
    public Integer getReallyRegister() {
        return reallyRegister;
    }

    /**
     * 设置 真实注册数 platform_channel_staff.really_register
     *
     * @param reallyRegister 真实注册数
     */
    public void setReallyRegister(Integer reallyRegister) {
        this.reallyRegister = reallyRegister;
    }

    /**
     * 获取 渠道注册数 platform_channel_staff.channel_register
     *
     * @return 渠道注册数
     */
    public Integer getChannelRegister() {
        return channelRegister;
    }

    /**
     * 设置 渠道注册数 platform_channel_staff.channel_register
     *
     * @param channelRegister 渠道注册数
     */
    public void setChannelRegister(Integer channelRegister) {
        this.channelRegister = channelRegister;
    }

    /**
     * 获取 放款成本，以分为单位 platform_channel_staff.loan_cost
     *
     * @return 放款成本，以分为单位
     */
    public Integer getLoanCost() {
        return loanCost;
    }

    /**
     * 设置 放款成本，以分为单位 platform_channel_staff.loan_cost
     *
     * @param loanCost 放款成本，以分为单位
     */
    public void setLoanCost(Integer loanCost) {
        this.loanCost = loanCost;
    }

    /**
     * 获取 注册成本，以分为单位 platform_channel_staff.register_cost
     *
     * @return 注册成本，以分为单位
     */
    public Integer getRegisterCost() {
        return registerCost;
    }

    /**
     * 设置 注册成本，以分为单位 platform_channel_staff.register_cost
     *
     * @param registerCost 注册成本，以分为单位
     */
    public void setRegisterCost(Integer registerCost) {
        this.registerCost = registerCost;
    }

    public BigDecimal getDecreasePercentage() {
        return decreasePercentage;
    }

    public void setDecreasePercentage(BigDecimal decreasePercentage) {
        this.decreasePercentage = decreasePercentage;
    }
}