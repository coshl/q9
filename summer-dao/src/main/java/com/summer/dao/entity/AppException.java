/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class AppException {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 系统(android,ios)
     */
    private String sysType;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * app版本
     */
    private String appVerion;

    /**
     * 设备名
     */
    private String deviceName;

    /**
     * 设备号
     */
    private String deviceId;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 获取 自增id platform_app_exception.id
     *
     * @return 自增id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增id platform_app_exception.id
     *
     * @param id 自增id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 用户ID platform_app_exception.user_id
     *
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户ID platform_app_exception.user_id
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 系统(android,ios) platform_app_exception.sys_type
     *
     * @return 系统(android, ios)
     */
    public String getSysType() {
        return sysType;
    }

    /**
     * 设置 系统(android,ios) platform_app_exception.sys_type
     *
     * @param sysType 系统(android,ios)
     */
    public void setSysType(String sysType) {
        this.sysType = sysType == null ? null : sysType.trim();
    }

    /**
     * 获取 系统版本 platform_app_exception.os_version
     *
     * @return 系统版本
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * 设置 系统版本 platform_app_exception.os_version
     *
     * @param osVersion 系统版本
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion == null ? null : osVersion.trim();
    }

    /**
     * 获取 app版本 platform_app_exception.app_verion
     *
     * @return app版本
     */
    public String getAppVerion() {
        return appVerion;
    }

    /**
     * 设置 app版本 platform_app_exception.app_verion
     *
     * @param appVerion app版本
     */
    public void setAppVerion(String appVerion) {
        this.appVerion = appVerion == null ? null : appVerion.trim();
    }

    /**
     * 获取 设备名 platform_app_exception.device_name
     *
     * @return 设备名
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * 设置 设备名 platform_app_exception.device_name
     *
     * @param deviceName 设备名
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    /**
     * 获取 设备号 platform_app_exception.device_id
     *
     * @return 设备号
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置 设备号 platform_app_exception.device_id
     *
     * @param deviceId 设备号
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * 获取 创建时间 platform_app_exception.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 platform_app_exception.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 platform_app_exception.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 platform_app_exception.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 异常信息 platform_app_exception.exception
     *
     * @return 异常信息
     */
    public String getException() {
        return exception;
    }

    /**
     * 设置 异常信息 platform_app_exception.exception
     *
     * @param exception 异常信息
     */
    public void setException(String exception) {
        this.exception = exception == null ? null : exception.trim();
    }
}