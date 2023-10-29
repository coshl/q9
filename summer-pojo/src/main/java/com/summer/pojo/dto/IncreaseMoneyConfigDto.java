package com.summer.pojo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提额降息的dto
 *
 * @author
 */
public class IncreaseMoneyConfigDto implements Serializable {

    private static final long serialVersionUID = -8234679733600883080L;
    @NotNull(message = "提额降息配置不能为空")
    private Integer id;
    @NotBlank(message = "正常/逾期还款/复贷次数不能为空")
    private Integer achieveTimes;
    //@NotNull(message = "提升的额度额度利率不能为空")
    private Double increaseMoney;
    // @NotNull(message ="降息的利率不能为空" )
    private Double reductionMoney;
    @NotNull(message = "提额的类型不能为空")
    private Byte increaseType;

    @NotNull(message = "提升的金额不能为空")
    private Integer repetitionInreaseMoney;

    @NotNull(message = "降低利息不能为空")
    private Double reduceInterest;
    @NotNull(message = "风控分数不为空")
    private Integer riskScore;

    private Integer status;
    @NotNull(message = "服务费率不能为空")
    private Double serviceMoney;

    @NotNull
    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(@NotNull Integer riskScore) {
        this.riskScore = riskScore;
    }

    public Integer getRepetitionInreaseMoney() {
        return repetitionInreaseMoney;
    }

    public void setRepetitionInreaseMoney(Integer repetitionInreaseMoney) {
        this.repetitionInreaseMoney = repetitionInreaseMoney;
    }


    public Double getReduceInterest() {
        return reduceInterest;
    }

    public void setReduceInterest(Double reduceInterest) {
        this.reduceInterest = reduceInterest;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAchieveTimes() {
        return achieveTimes;
    }

    public void setAchieveTimes(Integer achieveTimes) {
        this.achieveTimes = achieveTimes;
    }


    public Double getIncreaseMoney() {
        return increaseMoney;
    }

    public void setIncreaseMoney(Double increaseMoney) {
        this.increaseMoney = increaseMoney;
    }


    public Double getReductionMoney() {
        return reductionMoney;
    }

    public void setReductionMoney(Double reductionMoney) {
        this.reductionMoney = reductionMoney;
    }


    public Byte getIncreaseType() {
        return increaseType;
    }

    public void setIncreaseType(Byte increaseType) {
        this.increaseType = increaseType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(Double serviceMoney) {
        this.serviceMoney = serviceMoney;
    }
}
