package com.summer.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.CountCollectionAssessment;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.OrderCollectionDAO;
import com.summer.api.service.ICountCollectionAssessmentService;
import com.summer.service.impl.PlateformUserService;
import com.summer.util.BuildXLSX;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.XlsxParam;
import com.summer.pojo.vo.OperatorVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LoanReportController
 * 贷款报表
 *
 * @author : GeZhuo
 * Date: 2019/3/4
 */
@RestController
@RequestMapping("/v1.0/api/collectionReport")
public class CountCollectionAssessmentController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(CountCollectionAssessmentController.class);
    @Resource
    private ICountCollectionAssessmentService countCollectionAssessmentService;

    @Resource
    private PlateformUserService plateformUserService;
    @Resource
    private OrderCollectionDAO orderCollectionDAO;

    /**
     * 查询催收业务报表
     *
     * @param params
     * @return
     */
    @PostMapping("/queryCollectionReport")
    public String queryCollectionReport(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管，逾期主管，逾期专员，当日催收主管，当日催收专员
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_CUISHOUADMIN && platformUser.getRoleId() != Constant.ROLEID_COLLECTOR && platformUser.getRoleId() != Constant.ROLEID_COLLECTION_TODAY && platformUser.getRoleId() != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }


        HashMap<String, Object> hashMap = new HashMap<>();
        //处理分页信息
        PageHelper.startPage(params);
        List<CountCollectionAssessment> list = orderCollectionDAO.statisticByDay(params);
        //将CountCollectionAssessment转为CountCollectionAssessmentVO
//       List<CountCollectionAssessmentVO> voList = new CountCollectionAssessmentConvert().convert(list,CountCollectionAssessmentVO.class);
        hashMap.put("pageInfo", new PageInfo<>(list));
        //保留条件
        hashMap.put("params", params);
        //催收人员列表
        List<OperatorVo> users = plateformUserService.findByCuishouRoleId(8);
        hashMap.put("collection", users);
        return CallBackResult.returnJson(hashMap);
    }

    @Log(title = "每日催收统计列表")
    @RequestMapping("downloadCollectionReport")
    public void downloadCollectionReport(HttpServletRequest request, HttpServletResponse response,
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

        log.info("CountCollectionAssessmentController downloadLoanReport params=" + params.toString());
        List<CountCollectionAssessment> list = countCollectionAssessmentService.findParams(params);
//        List<CountCollectionAssessmentVO> voList = new CountCollectionAssessmentConvert().convert(list,CountCollectionAssessmentVO.class);
        log.info("CountCollectionAssessmentController downloadCollectionReport size=" + list.size());
        String title = "每日催收统计列表";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("personName", "催收人");
        titleMap.put("orderTotal", "催收任务数");
        titleMap.put("loanMoney", "逾期本金(元)");
        titleMap.put("repaymentOrderRate", "催回率(%)");
        titleMap.put("repaymentMoney", "催回本金(元)");
        titleMap.put("repaymentInterest", "催回息费(元)");
        titleMap.put("repaymentPenalty", "催回滞纳金(元)");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

}
