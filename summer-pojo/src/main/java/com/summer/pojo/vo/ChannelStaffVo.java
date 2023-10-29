package com.summer.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 却道运营人员列表Vo
 */
public class ChannelStaffVo {
    /**
     */
    private Integer id;

    /**
     * 统计时间
     */
    private String reportTime;

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
     * 放款成本，
     */
    private float loanCost;
    /**
     * 单价
     */
    private float price;
    /**
     * 结算金额
     */
    private float expense;

    /**
     * 注册成本
     */
    private float registerCost;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 扣量系数
     */
    private BigDecimal decreasePercentage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getPlateformUserId() {
        return plateformUserId;
    }

    public void setPlateformUserId(Integer plateformUserId) {
        this.plateformUserId = plateformUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getDeliveryConnection() {
        return deliveryConnection;
    }

    public void setDeliveryConnection(Integer deliveryConnection) {
        this.deliveryConnection = deliveryConnection;
    }

    public Integer getReallyRegister() {
        return reallyRegister;
    }

    public void setReallyRegister(Integer reallyRegister) {
        this.reallyRegister = reallyRegister;
    }

    public Integer getChannelRegister() {
        return channelRegister;
    }

    public void setChannelRegister(Integer channelRegister) {
        this.channelRegister = channelRegister;
    }

    public float getLoanCost() {
        return loanCost;
    }

    public void setLoanCost(float loanCost) {
        this.loanCost = loanCost;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getExpense() {
        return expense;
    }

    public void setExpense(float expense) {
        this.expense = expense;
    }

    public float getRegisterCost() {
        return registerCost;
    }

    public void setRegisterCost(float registerCost) {
        this.registerCost = registerCost;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getDecreasePercentage() {
        return decreasePercentage;
    }

    public void setDecreasePercentage(BigDecimal decreasePercentage) {
        this.decreasePercentage = decreasePercentage;
    }
}
