package com.summer.web.controller.appUser;

import com.summer.dao.entity.AppVersionInfo;
import com.summer.api.service.IAppVersionInfoService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.RedisUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 检查APP版本更新的Controller
 */
@RestController
@RequestMapping("/v1.0/api/app")
public class AppVersionController {

    @Resource
    private IAppVersionInfoService appVersionInfoService;

    /**
     * 检查安卓版本更新
     *
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("returnAndroidAppHao")
    public String checkAndoroidAppVersion(String appVersion) throws Exception {
        AppVersionInfo appVersionInfo = appVersionInfoService.selectAppVersionByType(Constant.ANDROID_APP_TYPE);
        HashMap<String, Object> resultMap = new HashMap<>();
        if (null != appVersionInfo) {
            String[] str = appVersion.split("\\.");
            String v = "";
            if (str[0].toLowerCase().indexOf("V") != -1) {
                v = str[0].substring(1, str[0].length() - 1);
            } else {
                v = str[0];
            }
            int v2 = Integer.parseInt(v + str[1] + str[2]);
            Integer periodTypes = Integer.valueOf(appVersionInfo.getCurrentVersion());

            if (v2 != periodTypes) {
                StringBuffer stringBuffer = new StringBuffer();
                String s = new String(appVersionInfo.getCurrentVersion());
                for (int i = 0; i < appVersionInfo.getCurrentVersion().length(); i++) {
                    char c = s.charAt(i);
                    if (i == appVersionInfo.getCurrentVersion().length() - 1) {
                        stringBuffer.append(c);
                    } else {
                        stringBuffer.append(c + ".");
                    }
                }
                //数据库当前版本
                resultMap.put("currentVersion", stringBuffer);
                //是否更新
                resultMap.put("isUpdate", appVersionInfo.getIsUpdate());
                //下载地址
                resultMap.put("apkDownloadUrl", appVersionInfo.getApkDownloadUrl());
                //更新描述
                resultMap.put("updateDescription", appVersionInfo.getUpdateDescription());
                //是否强制更新
                resultMap.put("isForceUpdate", appVersionInfo.getForceUpdate());
                return CallBackResult.returnJson(CallBackResult.SUCCESS, "需要更新", resultMap);
            }
        }
        //不需要更新
        resultMap.put("isUpdate", Constant.VERSION_UPDATE_NOTNEED_FLAG);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "不需要更新", resultMap);
    }

    /**
     * ios版本更新
     *
     * @param
     * @return
     */
    @GetMapping("returnIosAppHao/{appVersion}")
    public String checkIOSAppVersion(@PathVariable("appVersion") String appVersion) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        AppVersionInfo appVersionInfo = appVersionInfoService.selectAppVersionByType(Constant.IOS_APP_TYPE);
        if (null != appVersionInfo) {
            int nowIosVersionNumber = Integer.parseInt(appVersion);
            int oldIosVersionNumber = Integer.parseInt(appVersionInfo.getCurrentVersion());

            if (nowIosVersionNumber != oldIosVersionNumber) {
                resultMap.put("apkDownloadUrl", appVersionInfo.getApkDownloadUrl());
                resultMap.put("isForceUpdate", appVersionInfo.getForceUpdate());
                resultMap.put("isUpdate", appVersionInfo.getIsUpdate());
                resultMap.put("description", appVersionInfo.getUpdateDescription());
                return CallBackResult.returnJson(CallBackResult.SUCCESS, "需要更新", resultMap);
            }
        }
        resultMap.put("isUpdate", Constant.VERSION_UPDATE_NOTNEED_FLAG);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "不需要更新", resultMap);
    }

}
