package com.summer.util;

/**
 * 版本格式工具类
 */
public class VersionFormatUtil {
    /**
     * 处理APP版本号
     *
     * @return
     */
    public static String versionNumChange(String appVersion) {
        StringBuffer versionNum = new StringBuffer();
        for (int i = 0; i < appVersion.length(); i++) {
            versionNum.append(appVersion.charAt(i) + ".");
        }
        return versionNum.deleteCharAt(versionNum.length() - 1).toString();
    }
}
