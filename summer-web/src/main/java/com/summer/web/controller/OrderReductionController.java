package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.OrderCollectionReduction;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.OrderCollectionReductionDAO;
import com.summer.dao.mapper.PlatformUserMapper;
import com.summer.api.service.IOrderReductionService;
import com.summer.util.BuildXLSX;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.XlsxParam;
import com.summer.pojo.vo.CollectionUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/1/4
 */
@Controller
@RequestMapping("/v1.0/api/reductionOrder")
public class OrderReductionController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(OrderReductionController.class);
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private IOrderReductionService orderReductionService;
    @Resource
    private PlatformUserMapper platformUserMapper;

    /**
     * 减免订单列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryWithUser")
    public String queryWithUser(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderReductionController queryWithUser params=" + params.toString());
        PageHelper.startPage(params);
        List<CollectionUserVO> list = orderCollectionReductionDAO.findParams(params);
        params.clear();
        params.put("roleId", Constant.ROLEID_COLLECTOR);
        List<PlatformUser> platformUsers = platformUserMapper.selectSimple(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("platformUsers", platformUsers);
        return CallBackResult.returnJson(res);
    }

    /**
     * 减免操作
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/reduction")
    public String reduction(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管和财务可以减免
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderReductionController reduction params=" + params.toString());
        OrderCollectionReduction orderCollectionReduction = checkId(params);
        if (null == orderCollectionReduction) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        orderReductionService.reductConfirm(params, orderCollectionReduction, platformUser.getId());
        return CallBackResult.returnJson("操作成功");
    }


    private OrderCollectionReduction checkId(Map<String, Object> params) {
        Object idObj = params.get("id");
        if (null == idObj) {
            return null;
        }
        return orderCollectionReductionDAO.selectByPrimaryKey(Long.parseLong(idObj.toString()));
    }

    /**
     * 导出减免订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出减免订单列表")
    @RequestMapping("downloadOrderReduction")
    public void downloadOrderReduction(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody String jsonData) throws Exception {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            returnLoginBad(response);
            return;
        }
        Integer roleId = platformUser.getRoleId();
        if (Constant.ROLEID_SUPER != roleId) {
            returnErrorAuth(response);
            return;
        }

        //判断下载通道开启关闭
        Integer downloadSwitch = downloadSwitch();
        if (null != downloadSwitch){
            downloadBad(response);
            return;
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderReductionController downloadOrderReduction params=" + params.toString());
        List<CollectionUserVO> list = orderCollectionReductionDAO.findParams(params);
        log.info("OrderReductionController downloadOrderReduction size=" + list.size());
        String title = "减免订单";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("auditStatusName", "审核状态");
        titleMap.put("applyAmountNormal", "借款金额/元");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("principalAmountNormal", "应还金额/元");
        titleMap.put("lateFeeNormal", "滞纳金/元");
        titleMap.put("reductionAmountNormal", "减免滞纳金/元");
        titleMap.put("lateDay", "逾期天数");
        titleMap.put("groupLevel", "催收小组");
        titleMap.put("userName", "催收人员");
        titleMap.put("overdueCount", "逾期次数");
        titleMap.put("payStatusName", "逾期状态");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }
}
