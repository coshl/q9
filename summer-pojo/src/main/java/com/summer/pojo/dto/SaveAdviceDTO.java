package com.summer.pojo.dto;

import com.summer.group.SaveAdvice;

import javax.validation.constraints.NotNull;

/**
 * @author ls
 * @Title:
 * @date 2019/3/18 10:38
 */
public class SaveAdviceDTO {
    /**
     * 手机号码
     */
    @NotNull(message = "用户手机号不能为空", groups = {SaveAdvice.class})
    private String userPhone;

    /**
     * 建议内容
     */
    @NotNull(message = "建议内容不能为空", groups = {SaveAdvice.class})
    private String adviceInfo;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * app版本
     */
    private String appVersion;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名
     */
    private String deviceName;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAdviceInfo() {
        return adviceInfo;
    }

    public void setAdviceInfo(String adviceInfo) {
        this.adviceInfo = adviceInfo;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
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
}
