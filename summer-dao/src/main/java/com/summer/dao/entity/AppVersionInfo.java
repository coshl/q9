/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class AppVersionInfo {
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
    private Integer appType;

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
     * md5值
     */
    private String apkMd5;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 更新文案
     */
    private String updateDescription;
    
    /**
     * IOS标题
     */
    private String titleName;
    
	/**
     * IOS包名
     */
    private String bundleId;

    /**
     * 获取 自增ID app_version_info.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID app_version_info.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 当前版本信息 app_version_info.current_version
     *
     * @return 当前版本信息
     */
    public String getCurrentVersion() {
        return currentVersion;
    }

    /**
     * 设置 当前版本信息 app_version_info.current_version
     *
     * @param currentVersion 当前版本信息
     */
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion == null ? null : currentVersion.trim();
    }

    /**
     * 获取 APP类型（1表示安卓，2表示IOS） app_version_info.app_type
     *
     * @return APP类型（1表示安卓，2表示IOS）
     */
    public Integer getAppType() {
        return appType;
    }

    /**
     * 设置 APP类型（1表示安卓，2表示IOS） app_version_info.app_type
     *
     * @param appType APP类型（1表示安卓，2表示IOS）
     */
    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    /**
     * 获取 是否提示更新(0,不更新，1更新) app_version_info.is_update
     *
     * @return 是否提示更新(0, 不更新 ， 1更新)
     */
    public Integer getIsUpdate() {
        return isUpdate;
    }

    /**
     * 设置 是否提示更新(0,不更新，1更新) app_version_info.is_update
     *
     * @param isUpdate 是否提示更新(0,不更新，1更新)
     */
    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * 获取 apk下载地址 app_version_info.apk_download_url
     *
     * @return apk下载地址
     */
    public String getApkDownloadUrl() {
        return apkDownloadUrl;
    }

    /**
     * 设置 apk下载地址 app_version_info.apk_download_url
     *
     * @param apkDownloadUrl apk下载地址
     */
    public void setApkDownloadUrl(String apkDownloadUrl) {
        this.apkDownloadUrl = apkDownloadUrl == null ? null : apkDownloadUrl.trim();
    }

    /**
     * 获取 是否强制更新(0非强制更新，1强制更新) app_version_info.force_update
     *
     * @return 是否强制更新(0非强制更新 ， 1强制更新)
     */
    public Integer getForceUpdate() {
        return forceUpdate;
    }

    /**
     * 设置 是否强制更新(0非强制更新，1强制更新) app_version_info.force_update
     *
     * @param forceUpdate 是否强制更新(0非强制更新，1强制更新)
     */
    public void setForceUpdate(Integer forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    /**
     * 获取 md5值 app_version_info.apk_md5
     *
     * @return md5值
     */
    public String getApkMd5() {
        return apkMd5;
    }

    /**
     * 设置 md5值 app_version_info.apk_md5
     *
     * @param apkMd5 md5值
     */
    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5 == null ? null : apkMd5.trim();
    }

    /**
     * 获取 创建时间 app_version_info.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 app_version_info.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 app_version_info.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 app_version_info.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 更新文案 app_version_info.update_description
     *
     * @return 更新文案
     */
    public String getUpdateDescription() {
        return updateDescription;
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

    /**
     * 设置 更新文案 app_version_info.update_description
     *
     * @param updateDescription 更新文案
     */
    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription == null ? null : updateDescription.trim();
    }
}