package com.summer.web.controller.feigh;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.UserInfo;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.service.impl.BackConfigParamsService;
import com.summer.service.impl.LoanReportService;
import com.summer.service.impl.UserInfoService;
import com.summer.util.BackConfigParams;
import com.summer.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(value = "/api/provider/user")
public class FeighUserInfoController {


    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private BackConfigParamsService backConfigParamsService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoanReportService loanReportService;


    @GetMapping("/findByIdCard")
    public Boolean findByIdCard(@RequestParam String idCard) {
        return !CollectionUtils.isEmpty(userInfoService.selectByIdCard(idCard));
    }

    @GetMapping("/findUserByPhone")
    public String findUserByPhone(@RequestParam String phone) {
        UserInfo userInfo =  userInfoService.findByPhone(phone);
        if (Objects.isNull(userInfo))
            return null;
        JSONObject res = new JSONObject();
        res.put("name",userInfo.getRealName());
        res.put("idcardNo",userInfo.getIdCard());
        res.put("appId",getAppIdByCache());
        return res.toJSONString();
    }

    public String getAppIdByCache()
    {
        if (redisUtil.hasKey("sys_appId"))
            return redisUtil.get("sys_appId");
        else
        {
            BackConfigParamsVo backConfigParams =  backConfigParamsService.findBySysKey("appId");
            String appId  = backConfigParams.getSysValue().toString();
            redisUtil.set("sys_appId",appId);
            return appId;
        }
    }

    @GetMapping("/getReport")
    public String getReport(@RequestParam String begin, @RequestParam String end) {
        Map<String,Object> map = loanReportService.getReport(begin,end);
        JSONObject json = new JSONObject(map);
        return json.toJSONString();
    }

}
