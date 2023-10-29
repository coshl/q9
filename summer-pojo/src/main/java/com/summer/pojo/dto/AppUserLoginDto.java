package com.summer.pojo.dto;

import com.summer.util.Constant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户登录注册dto
 */
public class AppUserLoginDto implements Serializable {

    private static final long serialVersionUID = 7124895133455744289L;
    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = Constant.PHONE_REGULAR, message = Constant.PHONE_IS_ILLEGAL)
    private String phoneNumber;
    /**
     * 验证码
     */
//    @NotBlank(message = "验证码不能为空")--可以用密码登录，这时验证码可以为空
    private String smsCode;
    /**
     * GPS定位信息
     */
    private String GPS;
    /**
     * 客户端类型：1表示安卓，2表示IOS
     */
    private String clientType;
    /**
     * App版本号
     */
    private String appVersion;
    /**
     * 设备Id
     */
    private String deviceId;
    /**
     * 设备名称：SM-G925F
     */
    private String deviceName;
    /**
     * os版本
     */
    private String osVersion;
    /**
     * App名字：向钱冲首字母小写
     */
    private String appName;
    /**
     * 应用市场：lyb-self
     */
    private String appMarket;
    /**
     * 渠道编码 ：固定值
     */
    private String channelCode;
    /**
     * 用户APP登录密码
     */
    private String password;
    
    /*
     * 用户设备型号
     */
    private String deviceModel;
    
    /**
     * 用户系统版本
     */
    private String systemVersion;
    
    

    public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppMarket() {
        return appMarket;
    }

    public void setAppMarket(String appMarket) {
        this.appMarket = appMarket;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
