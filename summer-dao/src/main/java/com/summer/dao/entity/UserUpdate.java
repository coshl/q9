package com.summer.dao.entity;


public class UserUpdate {

    private Integer id;

    //手机号
    private String phone ;
          
    //姓名
    private String realName ;

    //身份证号
    private String idCard ;

    //第一联系人姓名
    private String firstContactName ;
    //第一联系人手机号
    private String firstContactPhone ;

    //第二联系人姓名
    private String secondContactName ;
    //第er联系人手机号
    private String  secondContactPhone ;

    private String headPortrait ;

    private String idcardImgZ ;

    private String idcardImgF ;

    private String idCardAddress ;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getFirstContactName() {
        return firstContactName;
    }

    public void setFirstContactName(String firstContactName) {
        this.firstContactName = firstContactName;
    }

    public String getFirstContactPhone() {
        return firstContactPhone;
    }

    public void setFirstContactPhone(String firstContactPhone) {
        this.firstContactPhone = firstContactPhone;
    }

    public String getSecondContactName() {
        return secondContactName;
    }

    public void setSecondContactName(String secondContactName) {
        this.secondContactName = secondContactName;
    }

    public String getSecondContactPhone() {
        return secondContactPhone;
    }

    public void setSecondContactPhone(String secondContactPhone) {
        this.secondContactPhone = secondContactPhone;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getIdcardImgZ() {
        return idcardImgZ;
    }

    public void setIdcardImgZ(String idcardImgZ) {
        this.idcardImgZ = idcardImgZ;
    }

    public String getIdcardImgF() {
        return idcardImgF;
    }

    public void setIdcardImgF(String idcardImgF) {
        this.idcardImgF = idcardImgF;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
