package com.summer.web.controller;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.api.service.IOrderRepaymentDetailService;
import com.summer.api.service.IOrderRepaymentService;
import com.summer.pojo.vo.RenewalUserVO;
import com.summer.service.impl.OrderCollectionService;
import com.summer.util.*;
import com.summer.util.changjiepay.ChanPayUtil;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.PaymentDetailUserVO;
import com.summer.pojo.vo.PaymentUserVO;
import com.summer.web.util.ExcelExportUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2019/1/2
 */
@Controller
@RequestMapping("/v1.0/api/repaymentOrder")
public class OrderRepaymentController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(OrderRepaymentController.class);
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private IOrderRepaymentService orderRepaymentService;
    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDAO;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private OrderCollectionService orderCollectionService;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private IOrderRepaymentDetailService orderRepaymentDetailService;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Resource
    private UserMoneyRateMapper userMoneyRateMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IncreaseMoneyConfigDAO increaseMoneyConfigDAO;

    /**
     * 待还记录列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryToPay")
    public String queryToPay(@RequestBody String jsonData, HttpServletRequest request) {
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
        log.info("OrderRepaymentController queryToPay params=" + params.toString());
        PageHelper.startPage(params);
        params.put("toPay", "true");
        List<PaymentUserVO> list = orderRepaymentService.findParams(params);
        handleRepaymentType(list);
        params.clear();
        params.put("roleId", 40);
        List<PlatformUser> platformUsers = platformUserMapper.selectSimple(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("platformUsers", platformUsers);
        //渠道
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        res.put("channel", plateformChannels);
        return CallBackResult.returnJson(res);
    }

    /**
     * 导出待还记录Excel
     *
     * @param
     * @return
     */
    @PostMapping(value = "/toPayX")
    public String exportToPayXls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER || isExporExcel(platformUser.getPhoneNumber())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        params.put("toPay", "true");
        List<PaymentUserVO> VOlist = orderRepaymentService.findParams(params);
        handleRepaymentType(VOlist);
        List<Map> map = new ArrayList<>();
        for (PaymentUserVO vo : VOlist) {
            Map<String, Object> param = new HashMap<>();
            param.put("realName", vo.getRealName());
            param.put("channelName", vo.getChannelName());
            param.put("phone", vo.getPhone());
            param.put("repaymentType", vo.getRepaymentType());
            param.put("applyAmount", vo.getApplyAmount());
            if (2 == vo.getAuditStatus()) {
                param.put("repaymentAmount", vo.getRepaymentAmountNormal() + "  应还金额(原):" + vo.getStartRepayAmount() + "备注:" + vo.getRemark());
            } else {
                param.put("repaymentAmount", vo.getRepaymentAmountNormal());
            }
            param.put("repaymentTimeChg", vo.getRepaymentTimeChg());
            param.put("renewalCount", vo.getRenewalCount());
            param.put("customerType", vo.getCustomerTypeName());
            param.put("feeAmount", vo.getFeeAmount());
            param.put("intoMoney", vo.getIntoMoney());

            map.add(param);
        }
        String[] title = new String[]{"客户姓名", "渠道来源", "手机号码", "待还状态", "申请金额/元", "服务费/元","借款本金/元","应还总额/元", "应还款时间", "续期次数", "新/老客"};
        String[] properties = new String[]{"realName", "channelName", "phone", "repaymentType", "applyAmount", "feeAmount","intoMoney","repaymentAmount", "repaymentTimeChg", "renewalCount", "customerType"};
        String sheetName = "待还记录";
        ExcelExportUtil.exportExcel(response, map, title, properties, sheetName);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "导出成功");
    }


    /**
     * 导出已还记录表Excel
     *
     * @param
     * @return
     */
    @PostMapping(value = "/paidX")
    public String exportPaidXls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        // 超管和客服才能查看客户列表
        if (roleId != Constant.ROLEID_SUPER || isExporExcel(platformUser.getPhoneNumber())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        params.put("toPay", "true");
        Object overdue = params.get("overdue");
        if (null != overdue) {
            int staus = Integer.parseInt(overdue.toString());
            if (staus == 3) {
                params.put("status", 3);
            }
            if (staus == 4) {
                params.put("status", 1);
            }
        }
        List<PaymentDetailUserVO> VOlist = orderRepaymentDetailService.findParams(params);

        List<Map> map = new ArrayList<>();
        for (PaymentDetailUserVO vo : VOlist) {
            Map<String, Object> param = new HashMap<>();
            param.put("realName", vo.getRealName());
            param.put("thirdOrderNo", vo.getThirdOrderNo());
            param.put("orderNo", vo.getOrderNo());
            param.put("channelName", vo.getChannelName());
            param.put("phone", vo.getPhone());
            param.put("status", vo.getStatusNormal());
            param.put("applyAmount", vo.getApplyAmount());
            if (2 == vo.getAuditStatus()) {
                //paidAmountNormal
                param.put("paidAmount", vo.getPaidAmountNormal() + "  应还金额(原):" + vo.getStartRepayAmount() + "备注:" + vo.getRemark());
            } else {
                param.put("paidAmount", vo.getPaidAmountNormal());
            }
            param.put("repaymentTime", vo.getRepaymentTime());
            param.put("payTimeChg", vo.getPayTimeChg());
            param.put("customerType", vo.getCustomerTypeName());
            param.put("payType", vo.getPayTypeNormal());

            map.add(param);
        }
        String[] title = new String[]{"客户姓名", "第三方订单号", "订单号", "渠道来源", "手机号码", "还款状态", "申请金额/元", "已还总额/元", "应还时间", "实际还款时间", "新/老客", "还款方式"};
        String[] properties = new String[]{"realName", "thirdOrderNo", "orderNo", "channelName", "phone", "status", "applyAmount", "paidAmount", "repaymentTime", "payTimeChg", "customerType", "payType"};
        String sheetName = "已还记录";
        ExcelExportUtil.exportExcel(response, map, title, properties, sheetName);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "导出成功");
    }

    /**
     * 已还记录列表
     *
     * @param jsonData
     * @return
     */
    @ResponseBody
    @PostMapping("/queryPaid")
    public String queryPaid(@RequestBody String jsonData, HttpServletRequest request) {
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
        log.info("OrderRepaymentController queryPaid params=" + params.toString());
        //通过还款类型字段，合并之前的还款类型和还款状态 0正常 1逾期 2提前 3付款中 4付款失败
        Object overdue = params.get("overdue");
        if (null != overdue) {
            int staus = Integer.parseInt(overdue.toString());
            if (staus == 3) {
                params.put("status", 3);
            }
            if (staus == 4) {
                params.put("status", 1);
            }
        }
        PageHelper.startPage(params);
        List<PaymentDetailUserVO> list = orderRepaymentDetailService.findParams(params);
    /*    for (PaymentDetailUserVO paymentDetailUserVO : list) {
            paymentDetailUserVO.setPhone(PhoneUtil.maskPhoneNum(paymentDetailUserVO.getPhone()));
            paymentDetailUserVO.setIdCard(IdcardUtil.hide(paymentDetailUserVO.getIdCard(), 5, 11));
        }*/
        params.clear();
        params.put("roleId", 40);
        List<PlatformUser> platformUsers = platformUserMapper.selectSimple(params);
        Map<String, Object> res = new HashMap<>();
        res.put("page", new PageInfo<>(list));
        res.put("platformUsers", platformUsers);
        //渠道
        List<PlateformChannel> plateformChannels = plateformChannelMapper.selectSimple(new HashMap<>());
        res.put("channel", plateformChannels);
        return CallBackResult.returnJson(res);
    }

    /**
     * 线下还款
     *
     * @param jsonData
     * @return
     */
    @Log(title = "线下还款")
    @ResponseBody
    @PostMapping("/offlinePay")
    public String offlinePay(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderRepaymentController offlinePay params=" + params.toString());
        OrderRepayment orderRepayment = checkId(params);
        if (null == orderRepayment) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Integer id = (Integer) params.get("id");
        if (redisUtil.hasKey(Constant.UNDERTHELINE + id)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "正在处理，请耐心等待2分钟");
        } else {
            redisUtil.set(Constant.UNDERTHELINE + id, id, 120);
        }
        Object paidAmountObj = params.get("paidAmount");
        Object payTimeObj = params.get("repaymentTimeChg");
        String remark = params.get("remark") == null ? "" : params.get("remark").toString();
        String thirdOrderNo = params.get("thirdOrderNo") == null ? ChanPayUtil.generateOutTradeNo() : params.get("thirdOrderNo").toString();
        Object payTypeObj = params.get("payType");
        if (null == paidAmountObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        double paidAmount = Double.parseDouble(paidAmountObj.toString());
        int amount = (int) (paidAmount * 100);
        if (amount <= 0 || amount > (orderRepayment.getRepaymentAmount() - orderRepayment.getPaidAmount() - orderRepayment.getReduceAmount())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "还款金额必须大于0且小于应还金额");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", thirdOrderNo);
        List<OrderRepaymentDetail> details = orderRepaymentDetailDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(details)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "订单号重复,请输入三方支付流水号");
        }
        // 异步处理线下还款
        asynchronousProcessing(orderRepayment, payTimeObj, remark, thirdOrderNo, payTypeObj, amount, platformUser.getId());
//        orderRepaymentService.payOffline(orderRepayment, payTimeObj, remark, thirdOrderNo, payTypeObj, amount,
////                platformUser.getId());
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "操作成功，数据正在处理，请耐心等待片刻，如果失败，请2分钟后重试");
    }

    @Async
    public void asynchronousProcessing(OrderRepayment orderRepayment, Object payTimeObj, String remark, String thirdOrderNo, Object payTypeObj, int amount, Integer userId) {
        orderRepaymentService.payOffline(orderRepayment, payTimeObj, remark, thirdOrderNo, payTypeObj, amount, userId);
    }

    /**
     * 修改应还金额
     *
     * @return
     */
    @Log(title = "修改应还金额")
    @ResponseBody
    @PostMapping("/reduce")
    public String reduce(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管和财务可以修改应还金额
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        log.info("【修改应还金额】-----phone={},ip={},param={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), params);
        // log.info("OrderCollectionController reduce params=" + params.toString());
        OrderRepayment orderRepayment = checkId(params);
        if (null == orderRepayment) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Map<String, Object> map = new HashMap<String, Object>(4) {{
            put("repaymentId", orderRepayment.getId());
        }};
      /*  List<OrderCollectionReduction> orderCollectionReductions = orderCollectionReductionDAO.selectSimple(map);
        if (CollectionUtils.isNotEmpty(orderCollectionReductions)) {
            OrderCollectionReduction orderCollectionReduction = orderCollectionReductions.get(0);
            if (orderCollectionReduction != null && Constant.COLLECTION_AUDIT_TODO ==
                    orderCollectionReduction.getAuditStatus()) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "已有减免申请在审核中,勿重复申请");
            }
        }*/
        Object reductionAmountObj = params.get("reductionAmount");
        if (null == reductionAmountObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        Double reductionAmount = Double.parseDouble(reductionAmountObj.toString());

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
        /**大于0并且小于最早的应还金额*/
        if (reductionAmount <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "还款金额必须大于0且不大于原应还金额");
        }
        String remark = params.get("remark") == null ? "" : params.get("remark").toString();
        //把原来的应还金额:用下划线_拼接在备注后面

        remark = remark + "_" + beforRepayAmount.divide(change, 2, BigDecimal.ROUND_HALF_UP).doubleValue();

        Integer repayMoney = (orderRepayment.getPaidAmount() + orderRepayment.getReduceAmount());
        BigDecimal repayMoneyDec = new BigDecimal(repayMoney);

        BigDecimal reductionAmountDec = new BigDecimal(reductionAmount + repayMoneyDec.divide(change, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderCollectionService.reduce(remark, orderRepayment.getUserId(), 0, orderRepayment.getId(), reductionAmountDec.multiply(change).intValue(), platformUser.getId());
        //贷超状态推送
        return CallBackResult.returnJson("操作成功");
    }

    private OrderRepayment checkId(Map<String, Object> params) {
        Object idObj = params.get("id");
        if (null == idObj) {
            return null;
        }
        return orderRepaymentMapper.selectByPrimaryKey(Integer.parseInt(idObj.toString()));
    }

    private void handleRepaymentType(List<PaymentUserVO> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (PaymentUserVO paymentUserVO : list) {
                if (paymentUserVO == null) {
                    continue;
                }
                /*paymentUserVO.setPhone(PhoneUtil.maskPhoneNum(paymentUserVO.getPhone()));
                paymentUserVO.setIdCard(IdcardUtil.hide(paymentUserVO.getIdCard(), 5, 15));*/
                Integer overdueCount = paymentUserVO.getOverdueCount();
                Integer renewalCount = paymentUserVO.getRenewalCount();
                Integer status = paymentUserVO.getStatus();
                paymentUserVO.setRepaymentType(Constant.REPAYMENT_TYPE_NORMAL);
                paymentUserVO.setRepayType(1);
                if (renewalCount > 0) {
                    paymentUserVO.setRepaymentType(Constant.REPAYMENT_TYPE_RENEWAL);
                    paymentUserVO.setRepayType(2);
                }
                if (Constant.REPAYMENT_STATUS_OVERDUE == status || Constant.REPAYMENT_STATUS_DIRTY == status) {
                    paymentUserVO.setRepaymentType(Constant.REPAYMENT_TYPE_OVERDUE);
                    paymentUserVO.setRepayType(0);
                }
            }
        }
    }

    /**
     * 导出待还订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出待还订单列表")
    @RequestMapping("downloadOrderRepayment")
    public void downloadOrderRepayment(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody String jsonData) throws Exception {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            returnLoginBad(response);
            return;
        }
        Integer roleId = platformUser.getRoleId();
        if (Constant.ROLEID_SUPER != roleId) {
            returnErrorAuth(response);
            return;
        }*/

        //判断下载通道开启关闭
        Integer downloadSwitch = downloadSwitch();
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderRepaymentController downloadOrderRepayment params=" + params.toString());
        params.put("toPay", "true");
        List<PaymentUserVO> list = orderRepaymentMapper.findParams(params);
        handleRepaymentType(list);
        log.info("OrderRepaymentController downloadOrderRepayment size=" + list.size());
        String title = "待还记录";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("repaymentType", "待还类型");
        titleMap.put("applyAmount", "申请金额");
        titleMap.put("repaymentTimeChg", "应还款时间");
        titleMap.put("repaymentAmountNormal", "应还金额");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("repaymentInterval", "距离还款日");
        titleMap.put("renewalCount", "展期次数");
        titleMap.put("overdueCount", "逾期次数");
        titleMap.put("customerTypeName", "新/老客");
        titleMap.put("reviewer", "审核人");
        titleMap.put("outTradeNo", "订单号");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 导出已还订单列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出已还订单列表")
    @RequestMapping("downloadOrderPaid")
    public void downloadOrderPaid(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody String jsonData) throws Exception {

       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            returnLoginBad(response);
            return;
        }
        Integer roleId = platformUser.getRoleId();
        if (Constant.ROLEID_SUPER != roleId) {
            returnErrorAuth(response);
            return;
        }*/

        //判断下载通道开启关闭
        Integer downloadSwitch = downloadSwitch();
        if (null != downloadSwitch) {
            downloadBad(response);
            return;
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);

        log.info("OrderRepaymentController downloadOrderPaid params=" + params.toString());
        List<PaymentDetailUserVO> list = orderRepaymentDetailDAO.findParams(params);
        log.info("OrderRepaymentController downloadOrderPaid size=" + list.size());
        String title = "已还记录";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("statusNormal", "还款状态");

        titleMap.put("repaymentAmountNormal", "应还金额/元");
        titleMap.put("loanTerm", "借款期限");
        titleMap.put("repaymentTime", "应还时间");
        titleMap.put("payTimeChg", "实际还款时间");
        titleMap.put("paidAmountNormal", "实际还款金额");
        titleMap.put("payTypeNormal", "支付方式");
        titleMap.put("overdueNormal", "还款类型");
        titleMap.put("overdueCount", "逾期次数");
        titleMap.put("forwardCount", "提前还款次数");
        titleMap.put("customerTypeName", "新/老客");
        titleMap.put("reviewer", "审核人");
        titleMap.put("thirdOrderNo", "订单号");
        XlsxParam xlsxParam = new XlsxParam(list, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }

    /**
     * 修改已还未未还款  （修改逻辑 repayment.md）
     *
     * @param id
     * @return
     */
    @Log(title = "取消还款")
    @RequestMapping("/cancelRepay")
    @Transactional
    public @ResponseBody
    Object cancelRepay(HttpServletRequest request, Long id) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登陆失效，请重新登陆！");
        }
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ADMIN_ROLE_ID) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "您无权操作！");
        }

        if (null == id) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数不能为空");
        }
        OrderRepaymentDetail orderRepaymentDetail = orderRepaymentDetailDAO.selectByPrimaryKey(id);
        if (null == orderRepaymentDetail) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "订单不存在");
        }
        log.info("【取消还款】---------orderId={},repayDetailId={}", orderRepaymentDetail.getBorrowId(), id);
        /**1、修改订单表 */
        OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(orderRepaymentDetail.getBorrowId());
        if (null != orderBorrow) {

            OrderBorrow orderBorrowNew = new OrderBorrow();
            byte status = orderBorrow.getStatus();
            orderBorrowNew.setId(orderBorrow.getId());
            if (status == (byte) 10) {
                orderBorrowNew.setStatus((byte) 8);
            } else if (status == 13) {
                orderBorrowNew.setStatus((byte) 11);
            } else if (status == 9) {
                orderBorrowNew.setStatus((byte) 8);
            } else {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "当前订单状态不支持修改");
            }
            orderBorrowMapper.updateByPrimaryKeySelective(orderBorrowNew);
        } else {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数错误");
        }
        log.info("【取消还款】---------orderId={}", orderBorrow.getId());
        /**2、修改还款表*/
        OrderRepayment orderRepayment = orderRepaymentMapper.selectByPrimaryKey(orderRepaymentDetail.getRepaymentId());
        if (null != orderRepayment) {
            /**3、还原额度 如果逾期还款没有提额 或者 复贷次数就还是0的情况 就不用还原金额,*/
            UserMoneyRate userMoneyRate = userMoneyRateMapper.findByUserId(orderRepaymentDetail.getUserId());
            if (null != userMoneyRate) {
                //复贷次数不是0，并且正常已还的才可以 还原金额
                if (userMoneyRate.getRepetitionTimes() != 0) {
                    if (orderRepayment.getStatus() == (byte) 2 || orderRepayment.getStatus() == 6) {
                        IncreaseMoneyConfig increaseMoneyConfig = increaseMoneyConfigDAO.selectByTimes(userMoneyRate.getRepetitionTimes());
                        if (null != increaseMoneyConfig) {
                            UserMoneyRate userMoneyRatenew = new UserMoneyRate();
                            userMoneyRatenew.setUserId(userMoneyRate.getUserId());
                            //次数减一
                            userMoneyRatenew.setRepetitionTimes(userMoneyRate.getRepetitionTimes() - 1);
                            //金额还原
                            Integer MaxAmount = userMoneyRate.getMaxAmount() - increaseMoneyConfig.getRepetitionInreaseMoney();
                            userMoneyRatenew.setMaxAmount(MaxAmount);
                            userMoneyRateMapper.update(userMoneyRatenew);
                        }
                    }
                }
            }
            OrderRepayment orderRepaymentNew = new OrderRepayment();
            orderRepaymentNew.setId(orderRepayment.getId());
            orderRepaymentNew.setPaidAmount(0);
            byte statusRepay = orderRepayment.getStatus();
            if (statusRepay == 4) {
                orderRepaymentNew.setStatus((byte) 3);
            } else if (statusRepay == 2 || statusRepay == 6 || statusRepay == 1) {
                orderRepaymentNew.setStatus((byte) 0);
            } else {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "当前订单状态不支持修改");
            }
            orderRepaymentMapper.updateByPrimaryKeySelective(orderRepaymentNew);
        } else {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "参数错误");
        }
        /**4、还原APP首页状态为有订单： 修改 info_index_info 表中borrow_status =1 */
        InfoIndexInfo infoIndexInfo = infoIndexInfoDao.selectByPrimaryKey(orderRepaymentDetail.getUserId());
        if (null != infoIndexInfo) {
            infoIndexInfo.setBorrowStatus("1");
            infoIndexInfoDao.updateByPrimaryKeySelective(infoIndexInfo);
        }
        /**5、删除该笔订单的还款记录 order_repayment_detail*/
        OrderRepaymentDetail orderDetail = new OrderRepaymentDetail();
        orderDetail.setId(id);
        orderDetail.setStatus((byte) 0);
        orderRepaymentDetailDAO.updateByPrimaryKeySelective(orderDetail);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "状态修改成功");
    }

    @GetMapping("/updateServiceCharge")
    @ResponseBody
    public Object updateServiceCharge(HttpServletRequest request,@RequestParam Integer userId,Double serviceCharge) {
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
        if(serviceCharge >= 1) {
        	return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, "费率不能大于等于1！");
        }
    	UserMoneyRate userMoneyRate = new UserMoneyRate();
    	userMoneyRate.setUserId(userId);
    	userMoneyRate.setServiceCharge(serviceCharge - 0.007);
    	int count = orderRepaymentService.updateServiceCharge(userMoneyRate);
    	return count > 0 ? CallBackResult.ok("修改成功") : CallBackResult.fail();
    }


    //修改还款时间
    @GetMapping("/updateRepaymentDate")
    @ResponseBody
    public Object updateRepaymentDate(HttpServletRequest request,@RequestParam Integer id,String repaymentDate) throws ParseException {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管、渠道主管、渠道专员可以查询
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER && roleId != Constant.CHANNEL_ADMIN_ROLE_ID && roleId != Constant.OPERATOR_ROLE_ID) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        if(null == repaymentDate || repaymentDate.equals("")) {
        	return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, "时间不能为空！");
        }
        /*Date dateNow = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        String now = dtf.format(dateNow);
        if(repaymentDate.compareTo(now) >= 0) {
            return CallBackResult.returnJson(CallBackResult.PARAM_IS_ERROR, "日期修改应小于今天,延期修改请用线下续期");
        }*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(repaymentDate);
        OrderRepayment OrderRepayment = new OrderRepayment();
        OrderRepayment.setId(id);
        OrderRepayment.setRepaymentTime(date);
        int count = orderRepaymentMapper.updateRepaymentDate(OrderRepayment);

        return count > 0 ? CallBackResult.ok("修改成功") : CallBackResult.fail();

    }

}
