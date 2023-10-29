package com.summer.dao.entity.paixu;

public class RiskResultResponseVO extends RiskResponseBaseVO {
    /**
     * 审核结果：“OK”: 通过;“MANUAL”: 人工审核;“DENY”: 拒绝
     */
    private String result;
    /**
     * 放款金额
     */
    private String loanAmount;
    /**
     * 模型评分
     */
    private String score;
    /**
     * 模型描述信息
     */
    private String desc;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RiskResultResponseVO{" +
                "result='" + result + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", score='" + score + '\'' +
                ", desc='" + desc + '\'' +
                ", rspCode='" + rspCode + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                '}';
    }
}