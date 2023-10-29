package com.summer.dao.entity.paixu;


public class BlackBaseVO {

    /**
     * 机构id
     */
    private String merchantId;

    /**
     * 风控系统分配给产品的编号
     */
    private String userName;
    private String idCard;
    private String phone;


    /**
     * 签名结果
     */
    String sign;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
