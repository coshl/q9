package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.MoneyRecord;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.MoneyRecordMapper;
import com.summer.service.impl.MerchantService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1.0/api/moneyRecord")
@Slf4j
public class MoneyRecordController extends BaseController{
    @Autowired
    MoneyRecordMapper moneyRecordMapper;

    @PostMapping("/pageByCondition")
    public String pageByCondition(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管和审核人员才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("MoneyRecordController list params=" + params.toString());
        params.put("review", "true");
        PageHelper.startPage(params);

        List<MoneyRecord> list = moneyRecordMapper.pageByCondition(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        return CallBackResult.returnJson(res);
    }
}
