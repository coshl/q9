package com.summer.pojo.vo;

public class RiskResultVo {

    //风险明细，合作机构提供的借款人的风险类别
    private String riskDetail;
    //命中项码，如证件号码（当前命中项仅包括证件号码）
    private String riskItemType;
    //命中内容，身份证号的具体值
    private String riskItemValue;
    //风险最近时间，指风险记录最近一次发现的时间 YYYYMMDD
    private String riskTime;

    public String getRiskDetail() {
        return riskDetail;
    }

    public void setRiskDetail(String riskDetail) {
        this.riskDetail = riskDetail;
    }

    public String getRiskItemType() {
        return riskItemType;
    }

    public void setRiskItemType(String riskItemType) {
        this.riskItemType = riskItemType;
    }

    public String getRiskItemValue() {
        return riskItemValue;
    }

    public void setRiskItemValue(String riskItemValue) {
        this.riskItemValue = riskItemValue;
    }

    public String getRiskTime() {
        return riskTime;
    }

    public void setRiskTime(String riskTime) {
        this.riskTime = riskTime;
    }
}
