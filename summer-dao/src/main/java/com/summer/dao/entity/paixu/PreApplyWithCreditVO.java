package com.summer.dao.entity.paixu;


import java.io.Serializable;

public class PreApplyWithCreditVO extends ControllerBaseVO implements Serializable {
    /**
     * 资产方订单编号
     */
    private String orderId;

    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 模型版本号
     */
    private String version;


    /**
     * 身份证号
     */
    private String cardNum;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 进件数据
     */
    private RiskDataDTO riskData;
//    private RiskData riskData;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public RiskDataDTO getRiskData() {
        return riskData;
    }

    public void setRiskData(RiskDataDTO riskData) {
        this.riskData = riskData;
    }

    @Override
    public String toString() {
        return "PreApplyWithCreditVO{" +
                "orderId='" + orderId + '\'' +
                ", userName='" + userName + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", mobile='" + mobile + '\'' +
                ", riskData=" + riskData +
                '}';
    }
//    public RiskData getRiskData() {
//        return riskData;
//    }
//
//    public void setRiskData(RiskData riskData) {
//        this.riskData = riskData;
//    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}