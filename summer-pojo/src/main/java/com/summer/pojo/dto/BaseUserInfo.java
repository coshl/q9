package com.summer.pojo.dto;

import com.summer.util.encrypt.AESDecrypt;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户关键数据加密解密
 */
public class BaseUserInfo {

    public String phone;
    private String account;
    public String phoneNumber;
    public String idCard;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        //如果长度等于18位就加密处理，如果密码大于18位就解密处理
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
