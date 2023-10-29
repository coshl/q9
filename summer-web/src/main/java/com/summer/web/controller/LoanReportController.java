package com.summer.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.PlatformUser;
import com.summer.api.service.ILoanReportService;
import com.summer.dao.entity.ReportRepayment;
import com.summer.service.impl.ReportRepaymentService;
import com.summer.util.BuildXLSX;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.XlsxParam;
import com.summer.pojo.vo.LoanReportVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LoanReportController
 * 贷款报表
 *
 * @author : GeZhuo
 * Date: 2019/2/25
 */
@RestController
@RequestMapping("/v1.0/api/loanReport")
public class LoanReportController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(LoanReportController.class);
    @Resource
    private ILoanReportService loanReportService;
    @Resource
    ReportRepaymentService reportRepaymentService;

    /**
     * 当日放款统计（查询每日贷款报表以及总额和总单数）
     *
     * @param params
     * @return
     */
    @PostMapping("/queryLoan")
    public String queryLoan(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        //处理分页信息
        PageHelper.startPage(params);
        List<LoanReportVO> list = loanReportService.findParams(params);
        //将LoanReport转为LoanReportVO
        Map<String, Object> count = loanReportService.findLoanMoneySum(params);

        for (LoanReportVO entity : list)
        {
            Map p  = new HashMap();
            p.put("reportDateEnd",entity.getReportDate());
            p.put("reportDateStart",entity.getReportDate());
            PageInfo<ReportRepayment> reportRepaymentPageInfo = reportRepaymentService.queryReport(p);
            ReportRepayment hktj = reportRepaymentPageInfo.getList().get(0);//当日还款款统计
            Integer oldC = entity.getOldLoanOrderCount() + hktj.getRenewalNumber();//老客放款 + 续期
            if (hktj.getNormalRepaymentCount().byteValue() == 0)
            {
                entity.setFdl(new BigDecimal(0));
            }else
            {
                BigDecimal fdl = new BigDecimal(oldC).divide(new BigDecimal(hktj.getNormalRepaymentCount()),4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                entity.setFdl(fdl);
            }
        }

        hashMap.put("pageInfo", new PageInfo<>(list));
        if (count == null) {
            count = new HashMap<>();
            count.put("moneyCount", new BigDecimal("0.00"));
            count.put("borrowApplyCount", 0);
            count.put("borrowRate", new BigDecimal("0.00"));
        }

        for (LoanReportVO entity : list)
        {
            Map p  = new HashMap();
            p.put("reportDateEnd",entity.getReportDate());
            p.put("reportDateStart",entity.getReportDate());
            PageInfo<ReportRepayment> reportRepaymentPageInfo = reportRepaymentService.queryReport(p);
            ReportRepayment hktj = reportRepaymentPageInfo.getList().get(0);//当日还款款统计
            Integer oldC = entity.getOldLoanOrderCount() + hktj.getRenewalNumber();//老客放款 + 续期
            if (hktj.getNormalRepaymentCount().byteValue() == 0)
            {
                entity.setFdl(new BigDecimal(0));
            }else
            {
                BigDecimal fdl = new BigDecimal(oldC).divide(new BigDecimal(hktj.getNormalRepaymentCount()),4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                entity.setFdl(fdl);
            }
        }


        hashMap.put("count", count);
        //保留条件
        hashMap.put("params", params);
        return CallBackResult.returnJson(hashMap);
    }

    @Log(title = "每日放款统计列表")
    @RequestMapping("downloadOrderOverdue")
    public void downloadLoanReport(HttpServletRequest request, HttpServletResponse response,
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

        log.info("LoanReportController downloadLoanReport params=" + params.toString());
        List<LoanReportVO> list = loanReportService.findParams(params);
        log.info("LoanReportController downloadLoanReport size=" + list.size());
        String title = "每日放款统计列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("reportDate", "日期");
        titleMap.put("registerCount", "注册人数");
        titleMap.put("borrowApplyCount", "申请人数");
        titleMap.put("applyRate", "申请率(%)");
        titleMap.put("borrowSucCount", "放款人数");
        titleMap.put("moneyAmountCount", "放款金额（元）");
        titleMap.put("borrowRate", "放款率（%）");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

}
