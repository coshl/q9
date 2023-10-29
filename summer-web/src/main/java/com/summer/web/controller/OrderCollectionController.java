package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.OrderCollection;
import com.summer.dao.entity.OrderCollectionReduction;
import com.summer.dao.entity.OrderRepayment;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.*;
import com.summer.api.service.IOrderCollectionService;
import com.summer.util.BuildXLSX;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.XlsxParam;
import com.summer.pojo.vo.CollectionUserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2019/1/4
 */
@Controller
@RequestMapping("/v1.0/api/collectOrder")
public class OrderCollectionController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(OrderCollectionController.class);
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private IOrderCollectionService orderCollectionService;
    @Value("${app.pid}")
    private String pid;

    /**
     * 我的催收列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryWithUser")
    public String queryWithUser(@RequestBody String jsonData, HttpServletRequest request) {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderCollectionController queryWithUser params=" + params.toString());
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管、逾期主管和逾期专员才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_CUISHOUADMIN && platformUser.getRoleId() != Constant.ROLEID_COLLECTOR) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        if (Constant.ROLEID_COLLECTOR == platformUser.getRoleId()) {
            params.put("platformUserId", platformUser.getId());
        }
        PageHelper.startPage(params);
        List<CollectionUserVO> list = orderCollectionDAO.findParams(params);
        return CallBackResult.returnJson(new PageInfo<>(list));
    }

    /*
     * 催收详情
     *
     * @param jsonData
     * @return
     */
    /*@ResponseBody
    @PostMapping("/detail")
    public String detail(@RequestBody String jsonData) {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderCollectionController detail params=" + params.toString());
        OrderCollection orderCollection = checkId(params);
        if (null == orderCollection)
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        Map<String, Object> map = new HashMap<String, Object>(4) {{
            put("collectionId", orderCollection.getId());
            put("auditStatus", Constant.COLLECTION_AUDIT_PASS);
            put("repaymentId", orderCollection.getRepaymentId());
            put("status", Constant.REPAYMENTDETAIL_STATUS_PAID);
        }};
        List<OrderCollectionReduction> orderCollectionReductions = orderCollectionReductionDAO.selectSimple(map);
        List<CollectionUserVO> list = new ArrayList<>();
        StringBuilder sb = null;
        if (CollectionUtils.isNotEmpty(orderCollectionReductions)) {
            for (OrderCollectionReduction orderCollectionReduction : orderCollectionReductions) {
                sb = new StringBuilder();
                CollectionUserVO collectionUserVO =
                        new CollectionUserVO(orderCollection.getCurrentCollectionUserId() + "",
                                orderCollectionReduction.getCreateTimeChg(), "减免滞纳金",
                                sb.append("减免").append(orderCollectionReduction.getReductionAmount()).toString());
                list.add(collectionUserVO);
                sb=null;
            }
        }
        List<OrderCollectionDetail> orderCollectionDetails = orderCollectionDetailDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(orderCollectionDetails)) {
            for (OrderCollectionDetail orderCollectionDetail : orderCollectionDetails) {
                sb = new StringBuilder();
                String collectionType = "";
                if (Constant.COLLECTION_TYPE_PHONE == orderCollectionDetail.getCollectionType()) {
                    collectionType = "电话催收";
                } else if (Constant.COLLECTION_TYPE_MSG == orderCollectionDetail.getCollectionType()) {
                    collectionType = "短信催收";
                }
                CollectionUserVO collectionUserVO =
                        new CollectionUserVO(orderCollection.getCurrentCollectionUserId() + "",
                                orderCollectionDetail.getCreateTimeChg(), "催收记录",
                                sb.append(collectionType).append(orderCollectionDetail.getCollectionTag()).append("-").append(orderCollection.getPromiseRepaymentTimeChg()).append("-").append(orderCollectionDetail.getRemark()).toString());
                list.add(collectionUserVO);
                sb=null;
            }
        }
        List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(orderRepaymentDetails)) {
            for (OrderRepaymentDetail orderRepaymentDetail : orderRepaymentDetails) {
                sb = new StringBuilder();
                CollectionUserVO collectionUserVO =
                        new CollectionUserVO(orderCollection.getCurrentCollectionUserId() + "",
                                orderRepaymentDetail.getCreateTimeChg(),
                                Constant.PAY_TYPE_MAP.get(orderRepaymentDetail.getPayType())+"还款",
                                sb.append("已还").append(orderRepaymentDetail.getPaidAmount()).toString());
                list.add(collectionUserVO);
                sb=null;
            }
        }
        return CallBackResult.returnJson(new PageInfo<>(list));
    }*/

    /**
     * 减免操作
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/reduce")
    public String reduce(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }

        Integer roleId = platformUser.getRoleId();
        if (Constant.ADMIN_ROLE_ID != roleId && roleId != Constant.finance_ROLE_ID){
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderCollectionController reduce params=" + params.toString());
        OrderCollection orderCollection = checkId(params);
        if (null == orderCollection) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Map<String, Object> map = new HashMap<String, Object>(4) {{
            put("collectionId", orderCollection.getId());
        }};
        /*List<OrderCollectionReduction> orderCollectionReductions = orderCollectionReductionDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(orderCollectionReductions)) {
            OrderCollectionReduction orderCollectionReduction = orderCollectionReductions.get(0);
            if (orderCollectionReduction != null && Constant.COLLECTION_AUDIT_TODO ==
                    orderCollectionReduction.getAuditStatus()) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "已有减免申请在审核中,勿重复申请");
            }
        }*/
        OrderRepayment orderRepayment = orderRepaymentMapper.selectByPrimaryKey(orderCollection.getRepaymentId());
        Object reductionAmountObj = params.get("reductionAmount");
        if (null == reductionAmountObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Double reductionAmount = Double.parseDouble(reductionAmountObj.toString());
        //if (reductionAmount <= 0 || reductionAmount > (orderRepayment.getLateFee() - orderCollection.getReductionMoney()) / 100)
        BigDecimal change = new BigDecimal(100);
        //原还款 表的应还金额，如果被修改后，就需要查询最早的减免订单表的第一条记录的应还金额
        BigDecimal beforRepayAmount = new BigDecimal((orderRepayment.getRepaymentAmount() - orderRepayment.getPaidAmount() - orderRepayment.getReduceAmount()));
        //最早的应还金额
        Double startRepaymentAount = beforRepayAmount.divide(change, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        /**查询最早的减免记录*/
        OrderCollectionReduction rderCollectionReduction = orderCollectionReductionDAO.findByRepayIdAsc(orderRepayment.getId());
        if (null != rderCollectionReduction) {
            String remarkStart = rderCollectionReduction.getRemark();
            if (null != remarkStart) {
                if (remarkStart.contains("_")) {
                    String[] string = remarkStart.split("_");
                    //paymentUserVO.setRemark(strings[0]);
                    String beforeRepayAmount = string[1];
                    //最早的应还金额
                    if (StringUtils.isNotBlank(beforeRepayAmount)) {
                        //  startRepaymentAount =  Double.parseDouble(beforeRepayAmount);
                    }
                }
            }
        }
        /**大于0且小于最早原来的应还金额*/
        if (reductionAmount <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "还款金额必须大于0且不大于原应还金额");
        }
        String remark = params.get("remark") == null ? "" : params.get("remark").toString();

        //把原来的应还金额:用下划线_拼接在备注后面
        remark = remark + "_" + beforRepayAmount.divide(change, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Integer repayMoney = (orderRepayment.getPaidAmount() + orderRepayment.getReduceAmount());
        BigDecimal repayMoneyDec = new BigDecimal(repayMoney);

        BigDecimal reductionAmountDec = new BigDecimal(reductionAmount + repayMoneyDec.divide(change, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderCollectionService.reduce(remark, orderCollection.getUserId(), orderCollection.getId(), orderRepayment.getId(), reductionAmountDec.multiply(change).intValue(), platformUser.getId());
        return CallBackResult.returnJson("操作成功");
    }


    /**
     * 续期金额减免
     * */
    @ResponseBody
    @PostMapping("/renewal/reduce")
    public String renewalReduce(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }

        Integer roleId = platformUser.getRoleId();
        if (Constant.ADMIN_ROLE_ID != roleId && roleId != Constant.finance_ROLE_ID){
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        JSONObject param =  JSONObject.parseObject(jsonData);
        Integer rid  = param.getInteger("rid");
        String amount  = param.getString("amount");
        OrderRepayment repayment =  orderRepaymentMapper.selectByPrimaryKey(rid);
        if (Objects.isNull(repayment))
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "订单不存在");
        repayment.setRemark("续期费用由["+repayment.getFeeAmount()/100+"]减免为["+amount+"]");
        repayment.setFeeAmount(new BigDecimal(amount).multiply(new BigDecimal(100)).intValue());

        orderRepaymentMapper.updateByPrimaryKey(repayment);

        return CallBackResult.returnJson("操作成功");
    }

    /**
     * 催收登记
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/addCollection")
    public String addCollection(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }

        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ADMIN_ROLE_ID &&  Constant.CUISHOU_ADMIN_ID != roleId && Constant.CUISHOU_USER_ROLE_ID != roleId){
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,"您无权操作！");
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderCollectionController addCollection params=" + params.toString());
        OrderCollection orderCollection = checkId(params);
        if (null == orderCollection) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        orderCollectionService.addDetail(params, orderCollection, platformUser.getId());
        return CallBackResult.returnJson("操作成功");
    }

    private OrderCollection checkId(Map<String, Object> params) {
        Object idObj = params.get("id");
        if (null == idObj) {
            return null;
        }
        return orderCollectionDAO.selectByPrimaryKey(Integer.parseInt(idObj.toString()));
    }

    /**
     * 导出催收订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出催收订单列表")
    @RequestMapping("downloadOrderCollect")
    public void downloadOrderCollect(HttpServletRequest request, HttpServletResponse response,
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
        log.info("OrderCollectionController downloadOrderCollect params=" + params.toString());
        List<CollectionUserVO> list = orderCollectionDAO.findParams(params);
        log.info("OrderCollectionController downloadOrderCollect size=" + list.size());
        String title = "我的催收";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("payStatusName", "逾期状态");
        titleMap.put("auditStatusName", "审核状态");
        titleMap.put("applyAmountNormal", "借款金额/元");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("principalAmountNormal", "应还金额/元");
        titleMap.put("lateFeeNormal", "滞纳金/元");
        titleMap.put("reductionAmountNormal", "减免滞纳金/元");
        titleMap.put("paidAmountNormal", "已还金额/元");
        titleMap.put("loanTimeChg", "放款日期");
        titleMap.put("repaymentTimeChg", "应还款日期");
        titleMap.put("lateDay", "逾期天数/天数");
        titleMap.put("groupLevel", "催收小组");
        titleMap.put("userName", "催收人员");
        titleMap.put("overdueCount", "逾期次数");
        titleMap.put("payTypeName", "还款方式");

        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 加入/取消黑名单
     *
     * @param jsonData
     * @return
     */
    @Log(title = "加入/取消黑名单")
    @ResponseBody
    @PostMapping("/updateCollectedUser")
    public String updateCollectedUser(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管、逾期专员、当日催收、当日催收主管
        Integer roleId = platformUser.getRoleId();
        //小康的只能超管操作黑名单
        if (StringUtils.isNotBlank(pid) && pid.equals("10049") ){
            if (roleId != Constant.ROLEID_SUPER) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
            }
        }else {
            if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_COLLECTOR && roleId != Constant.ROLEID_FINANCE_REVIEWER && roleId != Constant.ROLEID_BOSS_ADMIN_ADMIN) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
            }
        }

        return orderCollectionService.updateCollectedUser(jsonData);
    }
}
