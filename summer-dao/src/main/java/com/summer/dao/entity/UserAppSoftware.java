/* https://github.com/12641561 */
package com.summer.dao.entity;

public class UserAppSoftware {
    /**
     * 序列号
     */
    private Integer id;

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

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 获取 序列号 user_app_software.id
     *
     * @return 序列号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 序列号 user_app_software.id
     *
     * @param id 序列号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 应用软件名称 user_app_software.app_name
     *
     * @return 应用软件名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置 应用软件名称 user_app_software.app_name
     *
     * @param appName 应用软件名称
     */
    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    /**
     * 获取 应用包名 user_app_software.package_name
     *
     * @return 应用包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置 应用包名 user_app_software.package_name
     *
     * @param packageName 应用包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    /**
     * 获取 应用版本名 user_app_software.version_name
     *
     * @return 应用版本名
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * 设置 应用版本名 user_app_software.version_name
     *
     * @param versionName 应用版本名
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName == null ? null : versionName.trim();
    }

    /**
     * 获取 应用版本号 user_app_software.version_code
     *
     * @return 应用版本号
     */
    public String getVersionCode() {
        return versionCode;
    }

    /**
     * 设置 应用版本号 user_app_software.version_code
     *
     * @param versionCode 应用版本号
     */
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode == null ? null : versionCode.trim();
    }

    /**
     * 获取 用户Id user_app_software.user_id
     *
     * @return 用户Id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户Id user_app_software.user_id
     *
     * @param userId 用户Id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}