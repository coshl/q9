package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.Advice;
import com.summer.dao.entity.AppException;
import com.summer.dao.entity.HelpCenter;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.AdviceDAO;
import com.summer.dao.mapper.AppExceptionDAO;
import com.summer.api.service.IHelpCenterService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ls
 * @version V1.0
 * @Title: 用户中心
 * @Description:
 * @date 2018/12/22 14:10
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/user")
public class UserCenterController extends BaseController {
    @Resource
    private AdviceDAO adviseDAO;
    @Resource
    private IHelpCenterService helpCenterService;
    @Resource
    private AppExceptionDAO appExceptionDAO;

    /**
     * 意见反馈
     *
     * @param request
     * @return
     */
    @PostMapping("/feedback")
    @ResponseBody
    public String feedBack(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        String adviceInfo = (String) params.get("adviceInfo");
        String osVersion = (String) params.get("osVersion");
        String appVersion = (String) params.get("appVersion");
        String deviceId = (String) params.get("deviceId");
        String deviceName = (String) params.get("deviceName");

        UserInfo userInfo = redisUser(request);
        if (adviceInfo == null || "".equals(adviceInfo)) {
            return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY, Constant.PLEASE_INPUT_FEEDBACK_CONTENT);
        }

        if (userInfo != null) {
            Advice advice = new Advice();
            advice.setUserPhone(userInfo.getPhone());
            advice.setAppVersion(appVersion);
            advice.setDeviceId(deviceId);
            advice.setOsVersion(osVersion);
            advice.setDeviceName(deviceName);
            int i = adviseDAO.insertSelective(advice);
            if (1 > 0) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
            }
        }
        return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY, CallBackResult.MSG_OPERATE_FAIL);
    }

    /**
     * 帮助中心
     *
     * @return
     */
    @PostMapping("/helpCenter")
    public String helpCenter() throws Exception {
        int mTotal = helpCenterService.selectColumn();
        List<List<HelpCenter>> mHelpCenter = null;
        if (mTotal > 0) {
            mHelpCenter = new ArrayList<>();
            for (int i = 1; i <= mTotal; i++) {
                ArrayList<HelpCenter> helpCenters = new ArrayList<>();
                HelpCenter helpCenterBean = new HelpCenter();
                HelpCenter helpCenter = helpCenterService.selectByPrimaryKey(i);
                helpCenterBean.setTitle(helpCenter.getTitle());
                helpCenterBean.setContent(helpCenter.getContent());
                helpCenters.add(helpCenter);
                mHelpCenter.add(helpCenters);
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", mHelpCenter);
        return CallBackResult.returnJson(hashMap);
    }

    /**
     * app异常
     *
     * @param jsonData
     * @return
     */
    @PostMapping("/appException")
    @ResponseBody
    public String appException(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        String osVersion = (String) params.get("osVersion");
        String appVersion = (String) params.get("appVersion");
        String deviceId = (String) params.get("deviceId");
        String deviceName = (String) params.get("deviceName");
        String errMsg = (String) params.get("errMsg");
        UserInfo userInfo = redisUser(request);
        if (userInfo != null) {
            AppException appException = new AppException();
            appException.setOsVersion(osVersion);
            appException.setAppVerion(appVersion);
            appException.setDeviceId(deviceId);
            appException.setDeviceName(deviceName);
            appException.setUserId(Integer.parseInt(userInfo.getPhone()));
            appException.setException(errMsg);
            appExceptionDAO.insert(appException);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
        }
        return CallBackResult.returnJson(CallBackResult.ABRUPT_ABNORMALITY, CallBackResult.MSG_OPERATE_FAIL);
    }

}
