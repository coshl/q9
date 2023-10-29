package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.annotation.Log;
import com.summer.dao.entity.PlatformUser;
import com.summer.api.service.OrderCollectionCallerService;
import com.summer.util.BuildXLSX;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.XlsxParam;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.OrderCollectionCallerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * @author ls
 * @version V1.0
 * @Title: 协议
 * @Description:
 * @date 2018/12/24 9:54
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/distribution")
public class DistributionController extends BaseController {

    @Resource
    private OrderCollectionCallerService callerService;

    /**
     * 查询分配给当日催收的当日应还订单列表
     */
    @PostMapping("/selectRepaymentDistribution")
    public String selectRepaymentDistribution(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> params = JSONObject.parseObject(jsonData);
            //获取当前用户信息
            PlatformUser platformUser = redisPlatformUser(request);
            if (null == platformUser) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }
            // 超管、当日催收主管、当日催收可以查询
            Integer roleId = platformUser.getRoleId();
            if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN && roleId != Constant.ROLEID_COLLECTION_TODAY) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
            }
            //当日催收只能看到分配给自己的订单
            if (Constant.ROLEID_COLLECTION_TODAY == platformUser.getRoleId()) {
                params.put("serviceUserId", platformUser.getId());
            }
            return callerService.selectRepaymentDistribution(params);
        } catch (Exception e) {
            log.error("selectRepaymentDistribution error", e);
            return CallBackResult.returnJson(999, "系统异常");
        }
    }

    /**
     * 下载当日催收订单
     */
    @Log(title = "下载当日催收订单")
    @PostMapping("/downDistribution")
    public void downDistribution(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response) {
        try {

            PlatformUser platformUser = redisPlatformUser(request);
            if (null == platformUser) {
                returnLoginBad(response);
                return;
            }
//            Integer roleId = platformUser.getRoleId();
//            if (Constant.ROLEID_SUPER != roleId) {
//                returnErrorAuth(response);
//                return;
//            }

            //判断下载通道开启关闭
//            Integer downloadSwitch = downloadSwitch();
            /*if (null != downloadSwitch){
                downloadBad(response);
                return;
            }*/

            Map<String, Object> params = JSONObject.parseObject(jsonData);
            //获取当前用户信息
            //客服只能看到分配给自己的订单
            if (Constant.ROLEID_COLLECTION_TODAY == platformUser.getRoleId()) {
                params.put("serviceUserId", platformUser.getId());
            }
            List<OrderCollectionCallerVo> list = callerService.findParams(params);
            String title = "分配订单";
            BuildXLSX.setFileDownloadHeader(request, response, title);
            LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
            titleMap.put("realName", "客户姓名");
            titleMap.put("phone", "手机号码");
            titleMap.put("channelName", "渠道名称");
            titleMap.put("repaymentStatusStr", "订单状态");
            titleMap.put("repaymentAmount", "借款金额/分");
            titleMap.put("loanTerm", "借款期限");
            titleMap.put("principalAmount", "应还款本金/分");
            titleMap.put("lateFee", "滞纳金/分");
            titleMap.put("paidAmount", "已还款金额/分");
            titleMap.put("loanTime", "放款时间");
            titleMap.put("repaymentTime", "应还款时间");
            titleMap.put("remark", "备注");
            XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
            OutputStream os = response.getOutputStream();
            BuildXLSX.buildExcel(xlsxParam, os);
        } catch (Exception e) {
            log.error("downDistribution error", e);
        }
    }

    /**
     * 修改备注
     *
     * @param request
     * @return
     */
    @Log(title = "当日cs登记")
    @PostMapping("/updateDistribution")
    public String updateDistribution(@RequestBody String jsonData, HttpServletRequest request) {
        try {
            Map<String, Object> params = JSONObject.parseObject(jsonData);
            //获取当前用户信息
            PlatformUser platformUser = redisPlatformUser(request);
            //客服修改看到分配给自己的订单
            if (null == platformUser) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }
            // 超管、当日催收主管、当日催收
            Integer roleId = platformUser.getRoleId();
            if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN && roleId != Constant.ROLEID_COLLECTION_TODAY) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
            }
            return callerService.updateDistribution(params);
        } catch (Exception e) {
            log.error("updateDistribution error", e);
            return CallBackResult.returnJson(999, "系统异常");
        }
    }

    /**
     * 当日催收订单转派
     *
     * @param request
     * @return
     */
    @PostMapping("/transfer")
    public String transfer(@RequestBody String jsonData, HttpServletRequest request) {
        try {
            //获取当前用户信息
            PlatformUser platformUser = redisPlatformUser(request);
            if (null == platformUser) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }
            // 超管、当日催收主管
            Integer roleId = platformUser.getRoleId();
            if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
            }
            return callerService.transfer(jsonData, platformUser);
        } catch (Exception e) {
            log.error("transfer error", e);
            return CallBackResult.returnJson(999, "系统异常");
        }
    }


}
