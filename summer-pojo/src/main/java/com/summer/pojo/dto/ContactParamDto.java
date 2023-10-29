package com.summer.pojo.dto;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 紧急联系人认证参数dto
 */
public class ContactParamDto implements Serializable {
    /**
     * 第一联系人姓名
     */
    @NotBlank(message = "第一联系人姓名不能为空")
    private String firstContactName;
    /**
     * 第一联系人手机
     */
    @NotBlank(message = "第一联系人手机号不能为空")
    private String firstContactPhone;
    /**
     * 和第一联系人关系（0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹）
     */
    private String firstContactRelation;
    /**
     * 第二联系人姓名
     */
    @NotBlank(message = "第二联系人姓名不能为空")
    private String secondContactName;
    /**
     * 第二联系人手机号码
     */
    @NotBlank(message = "第二联系人手机号不能为空")
    private String secondContactPhone;
    /**
     * 与第二联系人关系（0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹）
     */
    private String secondContactRelation;
    /**
     * 现居住地
     */
    private String presentAddress;
    /**
     * 现居住地详情地址
     */
    private String presentAddressDetail;
    /**
     * 现居地维度
     */
    private String presentLatitude;
    /**
     * 现居地经度
     */
    private String presentLongitude;
    /**
     * 邮箱地址
     */
    private String emailAddress;

    /**
     * 手机基础信息
     */
    //  private PhoneBaseInfo phoneBaseInfo ;

   /* public PhoneBaseInfo getPhoneBaseInfo() {
        return phoneBaseInfo;
    }

    public void setPhoneBaseInfo(PhoneBaseInfo phoneBaseInfo) {
        this.phoneBaseInfo = phoneBaseInfo;
    }*/
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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


    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPresentAddressDetail() {
        return presentAddressDetail;
    }

    public void setPresentAddressDetail(String presentAddressDetail) {
        this.presentAddressDetail = presentAddressDetail;
    }

    public String getFirstContactRelation() {
        return firstContactRelation;
    }

    public void setFirstContactRelation(String firstContactRelation) {
        this.firstContactRelation = firstContactRelation;
    }

    public String getSecondContactRelation() {
        return secondContactRelation;
    }

    public void setSecondContactRelation(String secondContactRelation) {
        this.secondContactRelation = secondContactRelation;
    }

    public String getPresentLatitude() {
        return presentLatitude;
    }

    public void setPresentLatitude(String presentLatitude) {
        this.presentLatitude = presentLatitude;
    }

    public String getPresentLongitude() {
        return presentLongitude;
    }

    public void setPresentLongitude(String presentLongitude) {
        this.presentLongitude = presentLongitude;
    }

}
