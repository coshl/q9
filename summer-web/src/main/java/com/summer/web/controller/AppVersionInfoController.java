package com.summer.web.controller;

import com.summer.dao.entity.AppVersionInfo;
import com.summer.dao.entity.PlatformUser;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.IAppVersionInfoService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.pojo.vo.AppVersionInfoVo;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP版本更新controller
 */
@RestController
@RequestMapping("/v1.0/api/version")
public class AppVersionInfoController extends BaseController {

    @Resource
    private IAppVersionInfoService appVersionInfoService;

    /**
     * 获取APP版本信息
     *
     * @return
     */
    @GetMapping("/info")
    public String getAppversionInfo(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        List<AppVersionInfoVo> appVersionInfoVos = appVersionInfoService.selectByParam(null);
        return CallBackResult.returnJson(appVersionInfoVos);
    }

    /**
     * 修改版本信息
     *
     * @return
     */
    @PostMapping("/update")
    public String updateAppversionInfo(@RequestBody AppVersionInfo appVersionInfo, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        appVersionInfo.setUpdateTime(new Date());
        String currentVersion = appVersionInfo.getCurrentVersion();
        if (null != currentVersion) {
            currentVersion = currentVersion.replace(".", "");
            appVersionInfo.setCurrentVersion(currentVersion);
        }
        int isSuc = appVersionInfoService.updateByPrimaryKeySelective(appVersionInfo);
        if (isSuc > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }

    /**
     * APP版本强制更新的功能
     * @param jsonString
     * @return
     */
    @PostMapping("/getAppVersionInfo")
    public String getAppVersionInfo(@RequestBody String jsonString) {
    	Map<String, Object> params = JSONObject.parseObject(jsonString);
    	if(null == params.get("type") || null == params.get("version")) {
			return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数不能为空");
		}
    	Integer type = Integer.parseInt(params.get("type").toString());
    	String version = params.get("version").toString();
    	List<Map<String, Object>> list = appVersionInfoService.getAppVersionInfo(type);
    	if(Integer.parseInt(list.get(0).get("update").toString()) == 1) {
    		if(Integer.parseInt(list.get(0).get("version").toString()) > Integer.parseInt(version)) {
    			list.get(0).put("update", 1);
    		}else {
    			list.get(0).put("update", 0);
    		}
    	}
    	return CallBackResult.returnJson(list);
    }

}
