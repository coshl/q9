package com.summer.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.IndexReportDAO;
import com.summer.service.impl.MerchantService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
/**
 * 费用统计
 * @author Administrator
 *
 */
@Controller
@RequestMapping("v1.0/api/statisticalAnalysis")
public class TotalExpensesController extends BaseController{

	@Resource
    private MerchantService merchantService;

	@Resource
    private IndexReportDAO indexReportDAO;
	/**
     * 查询费用统计
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/selectTotalExpenses")
    @ResponseBody
    public String selectTotalExpenses(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //redisPlatformUser
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //如果角色为运营人人员，就得根据对应运营人员去查询
        if (roleId == Constant.OPERATOR_ROLE_ID) {
            params.put("plateformUserId", platformUser.getId());
        }
        PageHelper.startPage(params);
        Map<String, Object> merchantServiceMap = merchantService.selectTotalExpenses(params);
        return CallBackResult.returnJson(merchantServiceMap);
    }


    /**
     * 总充值金额，当前余额
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/selectTotalAmout")
    @ResponseBody
    public String selectTotalAmout(HttpServletRequest request) throws Exception {
        //redisPlatformUser
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> merchantServiceMap = merchantService.selectTotalAmout();
        return CallBackResult.returnJson(merchantServiceMap);
    }


    /**
     * 查询充值记录
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/selectRechargeRecord")
    @ResponseBody
    public String selectRechargeRecord(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
    	Map<String, Object> params = JSONObject.parseObject(jsonData);
        //redisPlatformUser
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //如果角色为运营人人员，就得根据对应运营人员去查询
        if (roleId == Constant.OPERATOR_ROLE_ID) {
            params.put("plateformUserId", platformUser.getId());
        }
        PageHelper.startPage(params);
        Map<String, Object> merchantServiceMap = merchantService.selectRechargeRecord(params);
        return CallBackResult.returnJson(merchantServiceMap);
    }


    /**
     * 查询费用统计
     *
     * @param jsonData
     * @return
     * @throws Exception
     */
    @PostMapping("/findMoneyRecord")
    @ResponseBody
    public String findMoneyRecord(HttpServletRequest request, @RequestBody String jsonData) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //redisPlatformUser
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        //如果角色为运营人人员，就得根据对应运营人员去查询
        if (roleId == Constant.OPERATOR_ROLE_ID) {
            params.put("plateformUserId", platformUser.getId());
        }
        PageHelper.startPage(params);
        Map<String, Object> merchantServiceMap = merchantService.findMoneyRecord(params);
        return CallBackResult.returnJson(merchantServiceMap);
    }


    /**
     * 总充值金额，当前余额
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/findTotalMoney")
    @ResponseBody
    public String findTotalMoney(HttpServletRequest request) throws Exception {
        //redisPlatformUser
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> merchantServiceMap = merchantService.findTotalMoney();
        return CallBackResult.returnJson(merchantServiceMap);
    }

    /**
     * 获取费用统计模式
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/findFlag")
    @ResponseBody
    public String findFlag(HttpServletRequest request) throws Exception {
        //redisPlatformUser
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        int flag = indexReportDAO.selectFlag();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flag",flag);
        return CallBackResult.returnJson(map);
    }


}
