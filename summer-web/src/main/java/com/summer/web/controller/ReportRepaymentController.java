package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.ReportRepayment;
import com.summer.dao.mapper.ReportRepaymentDAO;
import com.summer.api.service.IReportRepaymentService;
import com.summer.util.*;
import com.summer.pojo.vo.ReportRepaymentTitleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * @program: summer
 * @description: ${description}
 * @author: jql
 * @create: 2019-02-26 16:41
 */
@RestController
@RequestMapping("/v1.0/api/reportRepayment")
public class ReportRepaymentController extends BaseController {
    @Autowired
    private IReportRepaymentService reportRepaymentService;
    private static Logger log = LoggerFactory.getLogger(LoanReportController.class);
    @Resource
    private ReportRepaymentDAO reportRepaymentDAO;

    /**
     * 查询每日还款统计
     *
     * @param jsonData
     * @return
     */
    @PostMapping("queryReportInfoAll")
    public String queryReportInfoAll(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        PageHelper.startPage(params);
        PageInfo<ReportRepayment> reportRepaymentPageInfo = reportRepaymentService.queryReport(params);
        ReportRepaymentTitleVO repaymentTitleVO = reportRepaymentService.queryReportTitle(params);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageInfo", reportRepaymentPageInfo);
        hashMap.put("count", repaymentTitleVO);
        return CallBackResult.returnJson(hashMap);
    }

    /**
     * 导出每日还款统计（笔数统计）
     *
     * @param request
     * @param response
     * @param params
     * @throws Exception
     */
    @Log(title = "导出每日还款统计（笔数统计）")
    @RequestMapping("downloadDayStroke")
    public void downloadDayStroke(HttpServletRequest request, HttpServletResponse response,
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
        if (null != downloadSwitch){
            downloadBad(response);
            return;
        }

        log.info("ReportRepaymentController downloadDayStroke params=" + params.toString());
        List<ReportRepayment> list = reportRepaymentDAO.queryReport(params);
        log.info("ReportRepaymentController downloadDayStroke size=" + list.size());
        String title = "每日还款统计（笔数统计）列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("reportDate", "日期");
        titleMap.put("allRepaymentCount", "应还笔数");
        titleMap.put("expireCountNew", "新/应还笔数");
        titleMap.put("expireCountOld", "老/应还笔数");

        titleMap.put("normalRepaymentCount", "还款笔数");
        titleMap.put("normalCount", "正常还款笔数");
        titleMap.put("renewalNumber", "续期笔数");
        titleMap.put("normalCountNew", "新/还款笔数");
        titleMap.put("normalCountOld", "老/还款笔数");
        titleMap.put("repayRateDecimal", "还款率（%）");

        titleMap.put("repayRateNewDecimal", "新/正常还款率（%）");
        titleMap.put("repayRateOldDecimal", "老/正常还款率（%）");
        titleMap.put("repaymentRateS1CountAllDecimal", "逾期3天内还款率（%）");
        titleMap.put("repaymentRateS1CountNewDecimal", "新/逾期3天内还款率（%）");
        titleMap.put("repaymentRateS1CountOldDecimal", "老/逾期3天内还款率（%）");
        titleMap.put("repaymentRateS2CountAllDecimal", "逾期7天内还款率（%）");
        titleMap.put("repaymentRateS2CountNewDecimal", "新/逾期7天内还款率（%）");
        titleMap.put("repaymentRateS2CountOldDecimal", "老/逾期7天内还款率（%）");
        titleMap.put("repaymentRateS3CountAllDecimal", "逾期30天内还款率（%）");
        titleMap.put("repaymentRateS3CountNewDecimal", "新/逾期30天内还款率（%）");
        titleMap.put("repaymentRateS3CountOldDecimal", "老/逾期30天内还款率（%）");

        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 导出每日还款统计（金额统计）列表
     *
     * @param request
     * @param response
     * @param params
     * @throws Exception
     */
    @Log(title = "每日还款统计（金额统计）列表")
    @RequestMapping("downloadDayMoneyCount")
    public void downloadDayMoneyCount(HttpServletRequest request, HttpServletResponse response,
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
        if (null != downloadSwitch){
            downloadBad(response);
            return;
        }

        log.info("ReportRepaymentController downloadDayMoneyCount params=" + params.toString());
        List<ReportRepayment> list = reportRepaymentDAO.queryReport(params);
        log.info("ReportRepaymentController downloadDayMoneyCount size=" + list.size());
        String title = "每日还款统计（金额统计）列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("reportDate", "日期");
        titleMap.put("allRepaymentAmountDecimal", "应还金额（元）");
        titleMap.put("expireAmountNewDecimal", "新/应还金额（元）");
        titleMap.put("expireAmountOldDecimal", "老/应还金额（元）");
        titleMap.put("normalRepaymentAmountDecimal", "还款金额（元）");
        titleMap.put("normalAmount", "正常还款金额");
        titleMap.put("renewalAmount", "续期金额");
        titleMap.put("normalAmountNewDecimal", "新/还款金额（元）");
        titleMap.put("normalAmountOldDecimal", "老/还款金额（元）");
        titleMap.put("repayAmountRateDecimal", "还款率（%）");
        titleMap.put("repayAmountRateNewDecimal", "新/正常还款率（%）");
        titleMap.put("repayAmountRateOldDecimal", "老/正常还款率（%）");
        titleMap.put("repaymentRateS1AmountAllDecimal", "逾期3天内还款率（%）");
        titleMap.put("repaymentRateS1AmountNewDecimal", "新/逾期3天内还款率（%）");
        titleMap.put("repaymentRateS1AmountOldDecimal", "老/逾期3天内还款率（%）");
        titleMap.put("repaymentRateS2AmountAllDecimal", "逾期7天内还款率（%）");
        titleMap.put("repaymentRateS2AmountNewDecimal", "新/逾期7天内还款率（%）");
        titleMap.put("repaymentRateS2AmountOldDecimal", "老/逾期7天内还款率（%）");
        titleMap.put("repaymentRateS3AmountAllDecimal", "逾期30天内还款率（%）");
        titleMap.put("repaymentRateS3AmountNewDecimal", "新/逾期30天内还款率（%）");
        titleMap.put("repaymentRateS3AmountOldDecimal", "老/逾期30天内还款率（%）");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }
}
