package com.summer.dao.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 借款用户登陆信息表
 */
@Alias("SourceUserLoginInfo")
@Data
public class SourceUserLoginInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆用户的手机号
     */
    private String phoneNumber;

    /**
     * 用户访问的h5页面id
     */
    private String sourceH5Code;

    /**
     * 访问次数
     */
    private Integer loginTimes;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 渠道商编号;这里记录source_h5_id
     */
    private Integer sourceChannelCode;

    /**
     * 访问时间，上一次获取验证码
     */
    private Date loginTime;
    /**
     * 客户端类型：0表示未知，1表示安卓，2表示IOS
     */
    private Integer clientType;
    //用户注册时间
    private Date userCreatTime;
    /**
     * 用户登录APP时，手机基本信息
     */
    private String userLoginInfo;

    public String getUserLoginInfo() {
        return userLoginInfo;
    }

    public void setUserLoginInfo(String userLoginInfo) {
        this.userLoginInfo = userLoginInfo;
    }

    public SourceUserLoginInfo() {
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSourceH5Code() {
        return sourceH5Code;
    }

    public void setSourceH5Code(String sourceH5Code) {
        this.sourceH5Code = sourceH5Code;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getSourceChannelCode() {
        return sourceChannelCode;
    }

    public void setSourceChannelCode(Integer sourceChannelCode) {
        this.sourceChannelCode = sourceChannelCode;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }


    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Date getUserCreatTime() {
        return userCreatTime;
    }

    public void setUserCreatTime(Date userCreatTime) {
        this.userCreatTime = userCreatTime;
    }
}
