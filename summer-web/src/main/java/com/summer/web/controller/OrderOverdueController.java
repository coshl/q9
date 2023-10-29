package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.MmanLoanCollectionPerson;
import com.summer.dao.entity.OrderCollection;
import com.summer.dao.entity.OrderRepayment;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.*;
import com.summer.api.service.IOrderCollectionService;
import com.summer.api.service.ITaskjobService;
import com.summer.pojo.vo.BorrowUserVO;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.CollectionUserVO;
import com.summer.web.util.ExcelExportUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2019/1/4
 */
@Controller
@RequestMapping("/v1.0/api/overdueOrder")
public class OrderOverdueController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(OrderOverdueController.class);
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private IOrderCollectionService orderCollectionService;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private ITaskjobService taskjobService;

    /**
     * 逾期订单列表
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
        // 超管、逾期主管才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_CUISHOUADMIN) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderOverdueController queryWithUser params=" + params.toString());
        PageHelper.startPage(params);
        List<CollectionUserVO> list = orderCollectionDAO.findParams(params);
        params.clear();
        params.put("roleId", Constant.ROLEID_COLLECTOR);
        List<PlatformUser> platformUsers = platformUserMapper.selectSimple(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("platformUsers", platformUsers);
        return CallBackResult.returnJson(res);
    }

    /**
     * 派单
     *
     * @return
     */
    @GetMapping("/autoDispatch")
    @ResponseBody
    public String autoDispatch(HttpServletRequest request) {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登陆失效，请重新登陆！");
        }

        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ADMIN_ROLE_ID && Constant.CUISHOU_ADMIN_ID != roleId) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "您无权操作！");
        }
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo131List =
                getMmanLoanCollectionPeople(Constant.XJX_OVERDUE_LEVEL_S1);
        if (CollectionUtils.isEmpty(mmanLoanCollectionPersonNo131List)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "请添加催收人员");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("collection", 1);
        List<OrderRepayment> repayments = orderRepaymentMapper.selectSimple(map);
        if (CollectionUtils.isEmpty(repayments)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "当前无逾期订单");
        }
        taskjobService.handleDispatchTask();
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "派单成功");
    }

    private List<MmanLoanCollectionPerson> getMmanLoanCollectionPeople(String level) {
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo132List;
        Map<String, Object> personMap = new HashMap<String, Object>();
        personMap.put("dispatchTime", DateUtil.getDateFormat(new Date(), "yyyy-MM-dd"));
        personMap.put("groupLevel", level);
        personMap.put("userStatus", 0);
        personMap.put("roleId", Constant.ROLEID_COLLECTOR);

        mmanLoanCollectionPersonNo132List =
                platformUserMapper.findCollecterByCurrentUnCompleteCount(personMap);
        return mmanLoanCollectionPersonNo132List;
    }

    /**
     * 逾期统计
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/statistic")
    public String statistic(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ADMIN_ROLE_ID && roleId != Constant.CUISHOU_ADMIN_ID) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "您无权操作！");
        }

        List<Map<String, Object>> list = orderCollectionDAO.countOverdue();
        return CallBackResult.returnJson(new PageInfo<>(list));
    }

    /**
     * 转派
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/dispatch")
    public String dispatch(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }

        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ADMIN_ROLE_ID && Constant.CUISHOU_ADMIN_ID != roleId) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "您无权操作！");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Object platformUserIdObj = params.get("platformUserId");
        if (platformUserIdObj == null) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        int platformUserId = Integer.parseInt(platformUserIdObj.toString());
        PlatformUser byPrimaryKey = platformUserMapper.selectByPrimaryKey(platformUserId);
        log.info("OrderOverdueController dispatch params=" + params.toString());
        List<OrderCollection> orderCollections = checkId(params);
        if (CollectionUtils.isEmpty(orderCollections)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        for (OrderCollection orderCollection : orderCollections) {
            orderCollectionService.dispatch(byPrimaryKey, orderCollection);
        }
        return CallBackResult.returnJson("操作成功");
    }

    private List<OrderCollection> checkId(Map<String, Object> params) {
        List<OrderCollection> list = new ArrayList<>();
        Object idObj = params.get("id");
        if (null == idObj) {
            return list;
        }
        String[] split = idObj.toString().split(",");
        return orderCollectionDAO.selectByIds(split);
    }

    /**
     * 导出逾期订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出逾期订单列表")
    @RequestMapping("downloadOrderOverdue")
    public void downloadOrderOverdue(HttpServletRequest request, HttpServletResponse response,
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
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);

        log.info("OrderOverdueController downloadOrderOverdue params=" + params.toString());
        List<CollectionUserVO> list = orderCollectionDAO.findParams(params);
        log.info("OrderOverdueController downloadOrderOverdue size=" + list.size());
        String title = "逾期管理";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("payStatusName", "逾期状态");
        titleMap.put("applyAmountNormal", "借款金额/元");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("principalAmountNormal", "应还金额/元");
        titleMap.put("lateFeeNormal", "滞纳金/元");
        titleMap.put("reductionAmountNormal", "减免金额/元");
        titleMap.put("lateDay", "逾期天数");
        titleMap.put("overdueCount", "逾期次数");
        titleMap.put("paidAmountNormal", "已还金额/元");
        titleMap.put("loanTimeChg", "放款日期");
        titleMap.put("repaymentTimeChg", "预计还款日期");
        titleMap.put("groupLevel", "催收小组");
        titleMap.put("userName", "催收人员");


        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 导出逾期订单表Excel
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/withUserX")
    public String exportWithPsXls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER || isExporExcel(platformUser.getPhoneNumber())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        List<CollectionUserVO> VOlist = orderCollectionDAO.findParams(params);

        List<Map> map = new ArrayList<>();
        for (CollectionUserVO vo : VOlist) {
            Map<String, Object> param = new HashMap<>();
            param.put("realName", vo.getRealName());
            param.put("phone", vo.getPhone());
            param.put("payStatusName", vo.getPayStatusName());
            param.put("applyAmount", vo.getApplyAmountNormal());
            param.put("loanTerm", vo.getLoanTerm());
            param.put("principalAmount", vo.getPrincipalAmountNormal());
            param.put("lateFee", vo.getLateFeeNormal());
            param.put("reductionAmount", vo.getReductionAmount());
            param.put("lateDay", vo.getLateDay());
            param.put("overdueCount", vo.getOverdueCount());
            param.put("paidTimeChg", vo.getPaidTimeChg());
            param.put("paidAmount", vo.getPaidAmount());
            param.put("loanTimeChg", vo.getLoanTimeChg());
            param.put("repaymentTimeChg", vo.getRepaymentTimeChg());
            param.put("groupLevel", vo.getGroupLevel());
            param.put("userName", vo.getUserName());
            map.add(param);
        }
        String[] title = new String[]{"客户姓名", "手机号码", "逾期状态", "借款金额/元", "借款期限/天", "应还本金/元", "滞纳金/元", "减免滞纳金/元", "逾期天数/天", "逾期次数", "实际还款时间", "已还款金额/元", "放款时间", "应还时间", "催收小组", "催收人员"};
        String[] properties = new String[]{"realName", "phone", "payStatusName", "applyAmount", "loanTerm", "principalAmount", "lateFee", "reductionAmount", "lateDay", "overdueCount", "paidTimeChg", "paidAmount", "loanTimeChg", "repaymentTimeChg", "groupLevel", "userName"};
        String sheetName = "逾期订单";
        ExcelExportUtil.exportExcel(response, map, title, properties, sheetName);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "导出成功");
    }
}
