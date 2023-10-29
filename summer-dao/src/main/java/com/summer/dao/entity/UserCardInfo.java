package com.summer.dao.entity;

import java.io.Serializable;
import java.util.Date;

public class UserCardInfo implements Serializable {
    private Integer id;
    private Integer bankId;//银行卡编号
    private Integer userId;//用户编号
    private String bankName;//银行名称
    private String cardNo;//银行卡号
    private String phone;//预留手机号码
    private String creditAmount;//临时改为快钱银行代码
    private String validPeriod;//有效期
    private Integer status;//状态 (0:未生效   1:已生效)
    private Integer mainCard;//是否为主卡  0是 1 不是',
    private String openName;// 开户名称
    private String bankAddress;//开户地址
    private Integer type;//开类型 银行卡类型(1:信用卡   2:借记卡,3:对公账号)
    private Date createTime;//创建时间
    private Date updateTime;//修改时间

    private String mchntSsn;   // 绑卡协议流水号

    private Integer bindingStatus;//是否绑卡成功

    public final static Integer STATUS_DEFAULT = 0;//初始值
    public final static Integer STATUS_SUCCESS = 1;//生效
    public final static Integer TYPE_DEBIT = 2;//借记卡
    public final static Integer TYPE_CREDIT = 1;//1:信用卡
    public final static Integer MAINCARD_Z = 0;//主卡
    public final static Integer MAINCARD_F = 1;//副卡
    public final static String BANK_RIDS = "BANK_";

    public Integer getId() {
        return id;
    }

    private String agreeno;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBank_id() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {

        this.cardNo = cardNo;
        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(card_no)){
            if (card_no.contains("=")){
                //解密手机号
                this.card_no =   AESDecrypt.decrypt(card_no);
            }else {
                this.card_no =   AESDecrypt.encrypt(card_no);
            }

        }*/
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        //如果包含=就解密否则就加密
       /* if (StringUtils.isNotBlank(phone) ){
            if (phone.contains("=")){
                //解密手机号
                this.phone =   AESDecrypt.decrypt(phone);
            }else {
                this.phone =   AESDecrypt.encrypt(phone);
            }

        }*/
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMainCard() {
        return mainCard;
    }

    public void setMainCard(Integer mainCard) {
        this.mainCard = mainCard;
    }

    public String getOpenName() {
        return openName;
    }

    public void setOpenName(String openName) {
        this.openName = openName;
        //如果包含=就解密否则就加密
      /*  if (StringUtils.isNotBlank(openName)){
            if (openName.contains("=")){
                //解密手机号
                this.openName =   AESDecrypt.decrypt(openName);
            }else {
                this.openName =   AESDecrypt.encrypt(openName);
            }

        }*/
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserCardInfo{" +
                "id=" + id +
                ", bankId=" + bankId +
                ", userId=" + userId +
                ", bankName='" + bankName + '\'' +
                ", card_no='" + cardNo + '\'' +
                '}';
    }

    public String getAgreeno() {
        return agreeno;
    }

    public void setAgreeno(String agreeno) {
        this.agreeno = agreeno;
    }

    public String getMchntSsn() {
        return mchntSsn;
    }

    public void setMchntSsn(String mchntSsn) {
        this.mchntSsn = mchntSsn;
    }

    public Integer getBindingStatus() {
        return bindingStatus;
    }

    public void setBindingStatus(Integer bindingStatus) {
        this.bindingStatus = bindingStatus;
    }
}
