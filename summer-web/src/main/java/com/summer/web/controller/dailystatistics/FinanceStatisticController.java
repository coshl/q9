package com.summer.web.controller.dailystatistics;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.util.*;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.BorrowPhoneListVO;
import com.summer.pojo.vo.FinanceStatisticVO;
import lombok.extern.slf4j.Slf4j;
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
 * 财务对账
 */
@Slf4j
@Controller
@RequestMapping("/v1.0/api/financeStatistic")
public class FinanceStatisticController extends BaseController {

    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDAO;
    @Resource
    private PlatformChannelStaffDAO platformChannelStaffDAO;
    @Resource
    private PlatformUserMapper platformUserMapper;

    /**
     * 借款对账列表
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/borrow")
    public String borrow(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        PageHelper.startPage(params);
        List<FinanceStatisticVO> list = orderBorrowMapper.findStatistic(params);
        /*for (FinanceStatisticVO financeStatisticVO : list) {
            financeStatisticVO.setPhone(PhoneUtil.maskPhoneNum(financeStatisticVO.getPhone()));
            financeStatisticVO.setIdCard(IdcardUtil.hide(financeStatisticVO.getIdCard(), 5, 15));
        }*/
        List<Map<String, Object>> countStatistic = orderBorrowMapper.countStatistic();
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("count", countStatistic);
        return CallBackResult.returnJson(res);
    }

    /**
     * 导出借款对账订单列表
     *
     * @param request
     * @param response
     * @param params
     * @throws Exception
     */
    @Log(title = "借款对账订单列表")
    @RequestMapping("borrowDown")
    public void borrowDown(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody Map<String, Object> params) throws Exception {
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
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }

        List<FinanceStatisticVO> list = orderBorrowMapper.findStatistic(params);
        String title = "借款对账订单列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("applyTime", "申请时间");
        titleMap.put("loanTime", "放款时间");
        titleMap.put("applyMoney", "申请金额");
        titleMap.put("intoMoney", "实际到账金额");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("interest", "利息");
        titleMap.put("reloanTime", "复贷次数");
        titleMap.put("orderNo", "交易号");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 还款对账列表
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/repay")
    public String repay(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        PageHelper.startPage(params);
        List<FinanceStatisticVO> list = orderRepaymentDetailDAO.findStatistic(params);
        List<Map<String, Object>> countStatistic = orderRepaymentDetailDAO.countStatistic();
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("count", countStatistic);
        res.put("payType", JSON.toJSONString(Constant.PAY_TYPE_MAP));
        return CallBackResult.returnJson(res);
    }

    /**
     * 导出还款对账订单列表
     *
     * @param request
     * @param response
     * @param params
     * @throws Exception
     */
    @Log(title = "还款对账订单列表")
    @RequestMapping("repayDown")
    public void repayDown(HttpServletRequest request, HttpServletResponse response,
                          @RequestBody Map<String, Object> params) throws Exception {
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
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }
        List<FinanceStatisticVO> list = orderRepaymentDetailDAO.findStatistic(params);
        String title = "还款对账订单列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("repayTime", "还款时间");
        titleMap.put("topayMoney", "待还金额");
        titleMap.put("paidMoney", "已还金额");

        titleMap.put("repayTypeName", "还款方式");
        titleMap.put("orderNo", "交易号");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 续期对账列表
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/renewal")
    public String renewal(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        PageHelper.startPage(params);
        List<FinanceStatisticVO> list = orderRenewalMapper.findStatistic(params);
        List<Map<String, Object>> countStatistic = orderRenewalMapper.countStatistic();
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("count", countStatistic);
        res.put("payType", JSON.toJSONString(Constant.PAY_TYPE_MAP));
        return CallBackResult.returnJson(res);
    }

    /**
     * 导出续期对账订单列表
     *
     * @param request
     * @param response
     * @param params
     * @throws Exception
     */
    @Log(title = "续期对账订单列表")
    @RequestMapping("renewalDown")
    public void renewalDown(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody Map<String, Object> params) throws Exception {
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
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }

        List<FinanceStatisticVO> list = orderRenewalMapper.findStatistic(params);
        String title = "续期对账订单列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("renewalTime", "续期时间");
        titleMap.put("renewalTerm", "续期期限");
        titleMap.put("renewalFee", "续期费");

        titleMap.put("oldRepayTime", "续期前应还时间");
        titleMap.put("newRepayTime", "续期后应还时间");
        titleMap.put("renewalTypeName", "续期方式");
        titleMap.put("orderNo", "交易号");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 渠道对账列表
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/channel")
    public String channel(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        PageHelper.startPage(params);
        List<FinanceStatisticVO> list = platformChannelStaffDAO.findStatistic(params);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCooperationMode() != null && list.get(i).getCooperationMode() == 1) {
                list.get(i).setRegisterNum(list.get(i).getLoanCost());
                list.get(i).setExpenseAmount(list.get(i).getLoanCost() * list.get(i).getPrice());
            }
        }
        List<Map<String, Object>> countStatistic = platformChannelStaffDAO.countStatistic();
        Map<String, Object> map = new HashMap<>();
//        map.put("channel","true");
        map.put("status", 0);
        List<PlatformUser> person = platformUserMapper.selectSimple(map);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("count", countStatistic);
        res.put("person", person);
        return CallBackResult.returnJson(res);
    }

    /**
     * 导出渠道对账订单列表
     *
     * @param request
     * @param response
     * @param params
     * @throws Exception
     */
    @Log(title = "渠道对账订单列表")
    @RequestMapping("channelDown")
    public void channelDown(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody Map<String, Object> params) throws Exception {
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
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }

        List<FinanceStatisticVO> list = platformChannelStaffDAO.findStatistic(params);
        String title = "渠道对账订单列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("statisticTime", "日期");
        titleMap.put("backuserName", "运营人员");
        titleMap.put("channelName", "渠道名称");
        titleMap.put("price", "合作价格");
        titleMap.put("registerNum", "渠道注册数");

        titleMap.put("expenseAmount", "结算金额");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 获取手机号码
     *
     * @return
     */
    @GetMapping("/getPhone")
    public @ResponseBody
    Object getPhone(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        List<BorrowPhoneListVO> borrowPhoneListVOs = orderBorrowMapper.findPhone();
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, borrowPhoneListVOs);
    }
}