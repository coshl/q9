package com.summer.pojo.dto;

import java.io.Serializable;

/**
 * dto
 */
public class ContactsParamDto implements Serializable {
    private static final long serialVersionUID = 6403593125435264058L;
    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 手机号码
     */
    private String contactPhone;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
