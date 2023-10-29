package com.summer.pojo.vo;

/**
 * 贷款规则，提额列表展示VO
 */
public class IncreaseMoneyConfigVo {
    /**
     * 规则id
     */
    private Integer id;
    /**
     * 次数
     */
    private Integer achieveTimes;
    /**
     * 提升额度
     */
    private Double repetitionInreaseMoney;
    /**
     * 降低利息
     */
    private Double reduceInterest;
    /**
     * 提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）
     */
    private Integer increaseType;

    /**
     * 风控分数
     */
    private Integer riskScore;

    /**
     * 借款期限
     */
    private Integer loanTerm;
    /**
     * 0: 手动提额,1：自动提额
     */
    private Byte status;
    private Double serviceMoney;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
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

    public Double getRepetitionInreaseMoney() {
        return repetitionInreaseMoney;
    }

    public void setRepetitionInreaseMoney(Double repetitionInreaseMoney) {
        this.repetitionInreaseMoney = repetitionInreaseMoney;
    }

    public Double getReduceInterest() {
        return reduceInterest;
    }

    public void setReduceInterest(Double reduceInterest) {
        this.reduceInterest = reduceInterest;
    }

    public Integer getIncreaseType() {
        return increaseType;
    }

    public void setIncreaseType(Integer increaseType) {
        this.increaseType = increaseType;
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
