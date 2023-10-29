package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.annotation.Log;
import com.summer.dao.entity.PlatformUser;
import com.summer.api.service.CollectionStatisticsService;
import com.summer.util.BuildXLSX;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.XlsxParam;
import com.summer.pojo.vo.CollectionStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 催收业务统计
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1.0/api/collectionStatistics")
public class CollectionStatisticsController extends BaseController {

    @Resource
    private CollectionStatisticsService statisticsService;

    /**
     * 查询催收业务统计
     *
     * @param jsonData
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/queryCollectionStatistics")
    public String queryCollectionStatistics(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管，逾期主管，逾期专员，当日催收主管，当日催收专员
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_CUISHOUADMIN && platformUser.getRoleId() != Constant.ROLEID_COLLECTOR && platformUser.getRoleId() != Constant.ROLEID_COLLECTION_TODAY && platformUser.getRoleId() != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        return statisticsService.queryCollectionStatistics(jsonData, platformUser);
    }

    /**
     * 催收业务统计导出
     *
     * @param jsonData
     * @param request
     * @return
     * @throws Exception
     */
    @Log(title = "催收业务统计导出")
    @PostMapping("/collectionStatisticsDown")
    public void collectionStatisticsDown(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response) throws Exception {
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

        //从jsonData获取数据
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        //催收专员、当日催收员只能看到自己的
        if (platformUser.getRoleId() == 8 || platformUser.getRoleId() == 10) {
            params.put("userName", platformUser.getUserName());
        }
        List<CollectionStatisticsVo> list = statisticsService.findByParams(params);
        String title = "催收统计";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("countDate", "日期");
        titleMap.put("roleName", "角色");
        titleMap.put("userName", "催收人员");
        titleMap.put("distributionNumber", "分配单数");
        titleMap.put("repaymentNumber", "催回单数");
        titleMap.put("repaymentNumberRate", "笔数催回率");
        titleMap.put("distributionAmount", "应还金额");
        titleMap.put("repaymentAmount", "催回金额");
        titleMap.put("repaymentAmountRate", "金额催回率");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

}
