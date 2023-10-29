package com.summer.dao.entity.paixu;

public class RiskResponseBaseVO {

    /**
     * 结果编码
     */
    String rspCode;

    /**
     * 描述信息
     */
    String rspMsg;

    /**
     * 签名
     */
    String sign;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
