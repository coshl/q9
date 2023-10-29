package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlatformUser;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 商户查询自己的费用统计
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1.0/api/billing")
public class BillingController extends BaseController {

    /**
     * 数据中心回调地址
     */
    @Value("${billing.centerUrl}")
    private String centerUrl;
    @Value("${app.pid}")
    private String pid;

    /**
     * 查询商户充值记录或退费记录
     *
     * @param jsonData
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/queryPlateformRecharge")
    public String queryPlateformRecharge(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //向费用中心发送post请求
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        jsonObject.put("pid", pid);
        JSONObject jsonObject1 = HttpUtil.doHttpPost(centerUrl + "/queryPlateformRecharge", jsonObject);
        return jsonObject1.toJSONString();
    }

    /**
     * 查询商户消费记录
     *
     * @param jsonData
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/queryPlateformConsumption")
    public String queryPlateformConsumption(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //向费用中心发送post请求
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        jsonObject.put("pid", pid);
        JSONObject jsonObject1 = HttpUtil.doHttpPost(centerUrl + "/queryPlateformConsumption", jsonObject);
        return jsonObject1.toJSONString();
    }

}
