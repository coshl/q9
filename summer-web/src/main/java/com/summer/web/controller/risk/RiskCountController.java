package com.summer.web.controller.risk;

import com.alibaba.fastjson.JSONObject;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.OrderBorrowMapper;
import com.summer.dao.mapper.RiskTrialCountDAO;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.pojo.vo.RiskTrialCountVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ： RiskCountController
 * @Description ：
 * @Author：
 * @Date ：2019/10/15 11:05
 * @Version ：V1.0
 **/
@RestController
@RequestMapping("v1.0/api/back/riskCount")
public class RiskCountController extends BaseController {


    @Resource
    private RiskTrialCountDAO riskTrialCountDAO;

    /**
     * 风控规则统计
     *
     * @param jsonData
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public Object selectRisk(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        params.put("countType", 0);
        List<RiskTrialCountVo> list = riskTrialCountDAO.selectRuleCount(params);
        Map<String, Object> resultMap = new HashMap<>();
        //命中的集合
        resultMap.put("list", list);
        return CallBackResult.returnJson(resultMap);
    }
}
