package com.summer.pojo.vo;

import com.summer.enums.ContactEnum;
import com.summer.enums.ContactTwoEnum;

/**
 * 紧急联系人及现居住地址
 */
public class ContactVo {

    /**
     * 第一联系人姓名
     */
    private String firstContactName;
    /**
     * 第一联系人手机
     */
    private String firstContactPhone;
    /**
     * 和第一联系人关系（0未知1父亲、2母亲、3儿子、4女儿、5配偶、6兄弟、7姐妹）
     */
    private String firstContactRelation;
    /**
     * 第二联系人姓名
     */
    private String secondContactName;
    /**
     * 第二联系人手机号码
     */
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

    public ContactVo() {
    }

    public ContactVo(String firstContactName, String firstContactPhone, Byte firstContactRelation, String secondContactName, String secondContactPhone, Byte secondContactRelation, String presentAddress, String presentAddressDetail) {
        this.firstContactName = firstContactName;
        this.firstContactPhone = firstContactPhone;
        this.firstContactRelation = ContactEnum.getName(Integer.valueOf(firstContactRelation));
        this.secondContactName = secondContactName;
        this.secondContactPhone = secondContactPhone;
        this.secondContactRelation = ContactTwoEnum.getName(Integer.valueOf(secondContactRelation));
        this.presentAddress = presentAddress;
        this.presentAddressDetail = presentAddressDetail;
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

    public String getFirstContactRelation() {
        return firstContactRelation;
    }

    public void setFirstContactRelation(String firstContactRelation) {
        this.firstContactRelation = firstContactRelation;
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

    public String getSecondContactRelation() {
        return secondContactRelation;
    }

    public void setSecondContactRelation(String secondContactRelation) {
        this.secondContactRelation = secondContactRelation;
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
}
