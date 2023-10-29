package com.summer.pojo.dto;

import com.summer.group.HandleFeedbackInfo;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ls
 * @Title:
 * @date 2019/2/25 10:14
 */
public class AdviceDTO {

    private String startTime;
    private String endTime;
    private String pageSize;
    private String pageNum;

    /**
     * ID
     */
    @NotNull(message = "id不能为空", groups = HandleFeedbackInfo.class)
    private Integer id;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 建议内容
     */
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

    /**
     * 添加时间  默认：CURRENT_TIMESTAMP
     */
    private Date adviceAddtime;

    /**
     * 处理状态(0:未处理,1:已处理)  默认：0
     */
    private Integer isHandle;

    /**
     * 处理时间  默认：CURRENT_TIMESTAMP
     */
    private Date handleDate;

    /**
     * 显示处理(0:显示,1:不显示)  默认：0
     */
    private Integer hidden;

    /**
     * 处理人
     */
    private String handlePerson;

    /**
     * 处理情况
     */
    @NotBlank(message = "处理情况不能为空", groups = HandleFeedbackInfo.class)
    private String handleStatusInfo;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取 ID platform_advice.id
     *
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 ID platform_advice.id
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取 建议内容 platform_advice.advice_info
     *
     * @return 建议内容
     */
    public String getAdviceInfo() {
        return adviceInfo;
    }

    /**
     * 设置 建议内容 platform_advice.advice_info
     *
     * @param adviceInfo 建议内容
     */
    public void setAdviceInfo(String adviceInfo) {
        this.adviceInfo = adviceInfo == null ? null : adviceInfo.trim();
    }

    /**
     * 获取 系统版本 platform_advice.os_version
     *
     * @return 系统版本
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * 设置 系统版本 platform_advice.os_version
     *
     * @param osVersion 系统版本
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion == null ? null : osVersion.trim();
    }

    /**
     * 获取 app版本 platform_advice.app_version
     *
     * @return app版本
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * 设置 app版本 platform_advice.app_version
     *
     * @param appVersion app版本
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    /**
     * 获取 设备id platform_advice.device_id
     *
     * @return 设备id
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置 设备id platform_advice.device_id
     *
     * @param deviceId 设备id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * 获取 设备名 platform_advice.device_name
     *
     * @return 设备名
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * 设置 设备名 platform_advice.device_name
     *
     * @param deviceName 设备名
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    /**
     * 获取 添加时间 platform_advice.advice_addtime
     *
     * @return 添加时间
     */
    public Date getAdviceAddtime() {
        return adviceAddtime;
    }

    /**
     * 设置 添加时间 platform_advice.advice_addtime
     *
     * @param adviceAddtime 添加时间
     */
    public void setAdviceAddtime(Date adviceAddtime) {
        this.adviceAddtime = adviceAddtime;
    }

    public Integer getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(Integer isHandle) {
        this.isHandle = isHandle;
    }

    /**
     * 获取 处理时间 platform_advice.handle_date
     *
     * @return 处理时间
     */
    public Date getHandleDate() {
        return handleDate;
    }

    /**
     * 设置 处理时间 platform_advice.handle_date
     *
     * @param handleDate 处理时间
     */
    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    /**
     * 获取 显示处理(0:显示,1:不显示) platform_advice.hidden
     *
     * @return 显示处理(0 : 显示, 1 : 不显示)
     */
    public Integer getHidden() {
        return hidden;
    }

    /**
     * 设置 显示处理(0:显示,1:不显示) platform_advice.hidden
     *
     * @param hidden 显示处理(0:显示,1:不显示)
     */
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    /**
     * 获取 处理人 platform_advice.handle_person
     *
     * @return 处理人
     */
    public String getHandlePerson() {
        return handlePerson;
    }

    /**
     * 设置 处理人 platform_advice.handle_person
     *
     * @param handlePerson 处理人
     */
    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson == null ? null : handlePerson.trim();
    }

    /**
     * 获取 处理情况 platform_advice.handle_status_info
     *
     * @return 处理情况
     */
    public String getHandleStatusInfo() {
        return handleStatusInfo;
    }

    /**
     * 设置 处理情况 platform_advice.handle_status_info
     *
     * @param handleStatusInfo 处理情况
     */
    public void setHandleStatusInfo(String handleStatusInfo) {
        this.handleStatusInfo = handleStatusInfo == null ? null : handleStatusInfo.trim();
    }
}
