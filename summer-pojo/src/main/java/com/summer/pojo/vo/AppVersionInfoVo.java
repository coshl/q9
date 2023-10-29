package com.summer.pojo.vo;

import com.summer.util.VersionFormatUtil;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * APP版本信息展示VO
 */
public class AppVersionInfoVo {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 当前版本信息
     */
    private String currentVersion;
    /**
     * APP类型（1表示安卓，2表示IOS）
     */
    private Integer appName;
    /**
     * APP类型（处理过的）
     */
    private String appType;

    /**
     * 是否提示更新(0,不更新，1更新)  默认：0
     */
    private Integer isUpdate;
    /**
     * apk下载地址
     */
    private String apkDownloadUrl;
    /**
     * 是否强制更新(0非强制更新，1强制更新)  默认：0
     */
    private Integer forceUpdate;
    /**
     * 更新时间  默认
     */
    private String updateTime;

    /**
     * 更新文案
     */
    private String updateDescription;

    /**
     * md5值
     */
    private String apkMd5;

    /**
     * IOS标题
     */
    private String titleName;

    /**
     * IOS包名
     */
    private String bundleId;



    public void setAppType(String appType) {
        this.appType = appType;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        if (null != currentVersion) {
            this.currentVersion = VersionFormatUtil.versionNumChange(currentVersion);
        }
    }

    public Integer getAppName() {
        return appName;
    }

    public void setAppName(Integer appName) {
        this.appName = appName;
        if (null != appName) {
            int appNameInt = appName;
            if (appNameInt == 1) {
                this.appType = "安卓";
            } else if (appNameInt == 2) {
                this.appType = "IOS";
            } else if (appNameInt == 3) {
                this.appType = "合并链接";
            } else {
                this.appType = "未知";
            }
        }

    }

    public String getAppType() {
        return appType;
    }


    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getApkDownloadUrl() {
        return apkDownloadUrl;
    }

    public void setApkDownloadUrl(String apkDownloadUrl) {
        this.apkDownloadUrl = apkDownloadUrl;
    }

    public Integer getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Integer forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

}
