package com.summer.web.script;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.summer.api.service.RiskFkContentService;
import com.summer.dao.entity.RiskFkContent;
import com.summer.service.IRiskFkContentService;
import com.summer.service.impl.RiskFkContentServiceImpl;
import com.summer.service.impl.UserInfoService;
import com.summer.util.CallBackResult;
import lombok.extern.slf4j.Slf4j;
import org.redisson.misc.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/script")
public class FkScriptController {

    @Autowired
    private RiskFkContentServiceImpl riskFkContentService;

    @GetMapping("/refreshFk/{password}")
    public Object refreshFk(@PathVariable String password) {
        if (!password.equalsIgnoreCase("123456")) {
            return "密码错误";
        }
        log.info("重新获取风控报告开始");
        // 查出所有的用户
        QueryWrapper<RiskFkContent> queryWrapper = new QueryWrapper<RiskFkContent>().select("user_id", "task_id").eq("status", 1);
        List<RiskFkContent> list = riskFkContentService.list(queryWrapper);
        log.info("总任务量:{}", list.size());
        int i = 1;
        for (RiskFkContent content : list) {
            log.info("[{}]-生成报告:{}", i++, JSONObject.toJSONString(content));
            CallBackResult callBackResult = riskFkContentService.initReportByUserId(content.getUserId().intValue());
            log.info("报告生成结果:{}", JSONObject.toJSONString(callBackResult));
        }
        log.info("重新获取风控报告结束");
        return "end";
    }
}
