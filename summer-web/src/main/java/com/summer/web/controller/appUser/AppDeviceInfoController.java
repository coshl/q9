package com.summer.web.controller.appUser;

import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.IAppVersionInfoService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.vo.AppVersionInfoVo;
import com.summer.service.impl.UserInfoService;
import com.summer.service.impl.channel.ChannelCountRepaymentService;
import com.summer.util.*;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PhoneDeviceInfo;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.PhoneDeviceInfoDAO;
import com.summer.api.service.IAppDeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ls
 * @Title: 手机设备信息存储
 * @date 2019/5/27 17:41
 */
@RestController
@Slf4j
@RequestMapping("/v1.0/api/app")
public class AppDeviceInfoController extends BaseController {
    @Resource
    private IAppDeviceInfoService iAppDeviceInfoService;
    @Resource
    private PhoneDeviceInfoDAO phoneDeviceInfoDAO;
    @Resource
    private IAppVersionInfoService appVersionInfoService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    IChannelAsyncCountService channelAsyncCountService;
    @Resource
    RedisUtil redisUtil;

    /**
     * 保存手机设备信息存储
     *
     * @param
     * @return
     */
    @PostMapping("/saveAppDeviceInfo")
    public String saveAppDeviceInfo(HttpServletRequest httpServletRequest, @RequestBody String jsonData) {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        UserInfo userInfo = redisUser(httpServletRequest);
        if (null != userInfo) {
            PhoneDeviceInfo mPhoneDeviceInfo = new PhoneDeviceInfo();
            mPhoneDeviceInfo.setPhone(userInfo.getPhone());
            mPhoneDeviceInfo.setUserId(userInfo.getId());
            mPhoneDeviceInfo.setTrantime(new Date());
            mPhoneDeviceInfo.setTimestamp(new Date());
            mPhoneDeviceInfo.setUpdateTime(new Date());
            Object src = params.get("data");
            String s;
            if (null != src) {
                s = EmojiUtils.emojiChange(src.toString());
                s = s.replaceAll("\u0014", "");
                JSONObject jsonObject = JSONObject.parseObject(s);
                log.info("替换前cellularIp=" + jsonObject.get("cellularIp"));
                jsonObject.put("cellularIp", IpAdrressUtil.getIpAdrress(httpServletRequest));
                log.info("替换后cellularIp=" + jsonObject.get("cellularIp"));
                mPhoneDeviceInfo.setPhoneDeviceDataStr(jsonObject.toJSONString());
            }
            Map<String, Object> param = new HashMap<>();
            param.put("phone", userInfo.getPhone());
            List<PhoneDeviceInfo> phoneDeviceInfos = phoneDeviceInfoDAO.selectSimple(param);
            if (CollectionUtils.isEmpty(phoneDeviceInfos)) {
                iAppDeviceInfoService.insertSelective(mPhoneDeviceInfo);
            } else {
                mPhoneDeviceInfo.setId(phoneDeviceInfos.get(0).getId());
                phoneDeviceInfoDAO.updateByPrimaryKeySelective(mPhoneDeviceInfo);
            }
            return CallBackResult.returnSuccessJson();
        } else {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
    }

    /**
     * 获取APP版本信息
     *
     * @return
     */
    @GetMapping("/getDownUrl")
    public String getByIdversionInfo(String appType, String phone, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("appType", appType);
        List<AppVersionInfoVo> appVersionInfoVos = appVersionInfoService.selectByParam(map);
        try {
            String ipAdrress = IpAdrressUtil.getIpAdrress(request);
            if (redisUtil.setIfAbsent("sys_donw_lock_" + phone + "_" + ipAdrress, "1", 1, TimeUnit.DAYS)) {
                UserInfo userInfo = userInfoService.findByPhone(phone);
                if (null != userInfo)
                    channelAsyncCountService.appDownloadCount(userInfo, new Date());
            }
        } catch (Exception e) {
            log.error("渠道登录率统计异常");
        }

        return CallBackResult.returnJson(appVersionInfoVos);
    }
}
