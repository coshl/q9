package com.summer.pojo.dto;

import java.io.Serializable;

/**
 * 用户APP应用dto
 */
public class UserAppSoftwareDto implements Serializable {
    /**
     * 应用软件名称
     */
    private String appName;

    /**
     * 应用包名
     */
    private String packageName;

    /**
     * 应用版本名
     */
    private String versionName;

    /**
     * 应用版本号
     */
    private String versionCode;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
