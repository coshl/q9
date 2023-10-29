package com.summer.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 渠道进量统计列表
 */
public class DailyStatisticsChannelProductVo {
    private Integer id;

    private String reportTime;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 运营人员
     */
    private String plateformUserName;

    /**
     * 合作价格
     */
    private Double price;

    /**
     * 独立访问量  默认：0
     */
    private Integer uv;

    /**
     * 点击量  默认：0
     */
    private Integer pv;

    /**
     * uv转化率
     */
    private Float uvConversion;

    /**
     * 注册量  默认：0
     */
    private Integer registerNumber;

    /**
     * 扣量后的注册数  默认：0
     */
    private Integer registerNumberChannel;

    /**
     * 申请人数  默认：0
     */
    private Integer pplicationNumber;

    /**
     * 申请转化率
     */
    private Float pplicationConversion;

    /**
     * 放款量  默认：0
     */
    private Integer loanNumberChannel;

    /**
     * 放款转化率
     */
    private Float loanConversion;

    /**
     * 总注册量
     */
    private Integer registerSum;

    /**
     * 总申请量
     */
    private Integer aplicationSum;

    /**
     * 总放款笔数
     */
    private Integer loanNumberSum;

    /**
     * 扣量渠道注册数
     */
    private Integer channelRegister;

    //合作模式：0 cpa, 1 cps ,2 其他，3 uv
    private Integer cooperationMode;

    //获客成本
    private Double customerCost = 0.0;
    //状态
    private Integer status;

    public Integer getRegisterSum() {
        return registerSum;
    }

    public void setRegisterSum(Integer registerSum) {
        this.registerSum = registerSum;
    }

    public Integer getAplicationSum() {
        return aplicationSum;
    }

    public void setAplicationSum(Integer aplicationSum) {
        this.aplicationSum = aplicationSum;
    }

    public Integer getLoanNumberSum() {
        return loanNumberSum;
    }

    public void setLoanNumberSum(Integer loanNumberSum) {
        this.loanNumberSum = loanNumberSum;
    }

    public Integer getChannelRegister() {
        return channelRegister;
    }

    public void setChannelRegister(Integer channelRegister) {
        this.channelRegister = channelRegister;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPlateformUserName() {
        return plateformUserName;
    }

    public void setPlateformUserName(String plateformUserName) {
        this.plateformUserName = plateformUserName;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(Integer registerNumber) {
        this.registerNumber = registerNumber;
    }

    public Integer getRegisterNumberChannel() {
        return registerNumberChannel;
    }

    public void setRegisterNumberChannel(Integer registerNumberChannel) {
        this.registerNumberChannel = registerNumberChannel;
    }

    public Integer getPplicationNumber() {
        return pplicationNumber;
    }

    public void setPplicationNumber(Integer pplicationNumber) {
        this.pplicationNumber = pplicationNumber;
    }

    public Integer getLoanNumberChannel() {
        return loanNumberChannel;
    }

    public void setLoanNumberChannel(Integer loanNumberChannel) {
        this.loanNumberChannel = loanNumberChannel;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public Float getUvConversion() {
        return uvConversion;
    }

    public void setUvConversion(Float uvConversion) {
        this.uvConversion = uvConversion;
    }

    public Float getPplicationConversion() {
        return pplicationConversion;
    }

    public void setPplicationConversion(Float pplicationConversion) {
        this.pplicationConversion = pplicationConversion;
    }

    public Float getLoanConversion() {
        return loanConversion;
    }

    public void setLoanConversion(Float loanConversion) {
        this.loanConversion = loanConversion;
    }

    public Double getCustomerCost() {
        return customerCost;
    }

//    public void setCustomerCost(BigDecimal customerCost) {
//        this.customerCost = customerCost;
//    }

    public Integer getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(Integer cooperationMode) {
        this.cooperationMode = cooperationMode;
        customerCost = 0.0;

        if (null != price && null != cooperationMode) {
            if (null != loanNumberChannel && loanNumberChannel != 0) {
                //合作模式：0 cpa, 1 cps ,2 其他，3 uv
                if (cooperationMode == 0) {
                    customerCost = registerNumber * price / loanNumberChannel;
                } else if (cooperationMode == 1) {
                    customerCost = price;
                } else if (cooperationMode == 3) {
                    if (null != uv) {
                        customerCost = uv * price / loanNumberChannel;
                    }
                }
                customerCost = (double) Math.round(customerCost * 100) / 100;
            }
        } else {
            //默认 uv 价格1元
            if (null != uv) {
                BigDecimal uvb = new BigDecimal(this.uv);
                if (null != loanNumberChannel && 0 != loanNumberChannel) {
                    BigDecimal loanNumberChannelb = new BigDecimal(loanNumberChannel);
                    customerCost = uvb.divide(loanNumberChannelb, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }

        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
