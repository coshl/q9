package com.summer.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 渠道方列表
 */
public class PlateformChannelSideVo {
    /**
     * 统计时间
     */
    private Date reportTime;
    /**
     */
    private Integer id;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道方注册量  默认：0
     */
    private Integer deductionRegisterCount;

    /**
     * 渠道方申请借款人数  默认：0
     */
    private Integer deductionBorrowApplyCount;

    /**
     * 渠道方申请成功人数
     */
    private Integer deductionBorrowSucCount;

    /**
     * 系数
     */
    private BigDecimal passRate = new BigDecimal(0);
    /**
     * 扣量系数
     */
    private BigDecimal dedutionCoefficient;

    //命中系统黑名单
    private Integer hitSystemBlack;
    //命中外部三方黑名单
    private Integer hitOutBlack;

    //命中系统黑名单
    private float hitSystemBlackRate;
    //命中外部三方黑名单
    private float hitOutBlackRate;
    //命中黑名单总数
    private Integer blackTotal;

    //命中黑名单率（总的）
    private Double blackTotalRate;

    public BigDecimal getPassRate() {
        return passRate;
    }

    public void setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
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

    public Integer getDeductionRegisterCount() {
        return deductionRegisterCount;
    }

    public void setDeductionRegisterCount(Integer deductionRegisterCount) {
        this.deductionRegisterCount = deductionRegisterCount;
    }

    public Integer getDeductionBorrowApplyCount() {
        return deductionBorrowApplyCount;
    }

    public void setDeductionBorrowApplyCount(Integer deductionBorrowApplyCount) {
        this.deductionBorrowApplyCount = deductionBorrowApplyCount;
    }

    public Integer getDeductionBorrowSucCount() {
        return deductionBorrowSucCount;
    }

    public void setDeductionBorrowSucCount(Integer deductionBorrowSucCount) {
        this.deductionBorrowSucCount = deductionBorrowSucCount;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public BigDecimal getDedutionCoefficient() {
        return dedutionCoefficient;
    }

    public void setDedutionCoefficient(BigDecimal dedutionCoefficient) {
        this.dedutionCoefficient = dedutionCoefficient;
    }

    public Integer getHitSystemBlack() {
        return hitSystemBlack;
    }

    public void setHitSystemBlack(Integer hitSystemBlack) {
        this.hitSystemBlack = hitSystemBlack;
    }

    public Integer getHitOutBlack() {
        return hitOutBlack;
    }

    public void setHitOutBlack(Integer hitOutBlack) {
        this.hitOutBlack = hitOutBlack;
    }

    public float getHitSystemBlackRate() {
        return hitSystemBlackRate;
    }

    public void setHitSystemBlackRate(float hitSystemBlackRate) {
        this.hitSystemBlackRate = hitSystemBlackRate;
    }

    public float getHitOutBlackRate() {
        return hitOutBlackRate;
    }

    public void setHitOutBlackRate(float hitOutBlackRate) {
        this.hitOutBlackRate = hitOutBlackRate;
    }

    public Integer getBlackTotal() {
        return blackTotal;
    }

    public void setBlackTotal(Integer blackTotal) {
        this.blackTotal = blackTotal;

    }

    public Double getBlackTotalRate() {
        return blackTotalRate;
    }

    public void setBlackTotalRate(Double blackTotalRate) {
        this.blackTotalRate = blackTotalRate;
    }
}
