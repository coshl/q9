package com.summer.web.controller;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.annotation.Log;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.pojo.vo.BorrowUserVO;
import com.summer.service.impl.LoanRuleConfigService;
import com.summer.service.impl.OrderRepaymentService;
import com.summer.util.*;
import com.summer.util.changjiepay.ChanPayUtil;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.RenewalUserVO;
import com.summer.web.util.ExcelExportUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Desc:
 * Created by tl on 2018/12/21
 */
@Controller
@RequestMapping("/v1.0/api/renewalOrder")
public class OrderRenewalController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(OrderRenewalController.class);
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderRepaymentService orderRepaymentService;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private LoanRuleConfigService loanRuleConfigService;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Value("${system.prefix}")
    private String PREFIX;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 续期记录列表
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
        // 超管和财务才能查询
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        log.info("OrderRenewalController queryWithUser params=" + params.toString());
        Object status = params.get("status");
//        if(null==status){
//            params.put("status",2);
//        }
        PageHelper.startPage(params);
        List<RenewalUserVO> list = orderRenewalMapper.findParams(params);
     /*   for (RenewalUserVO renewalUserVO : list) {
            renewalUserVO.setPhone(PhoneUtil.maskPhoneNum(renewalUserVO.getPhone()));
            renewalUserVO.setIdCard(IdcardUtil.hide(renewalUserVO.getIdCard(), 5, 15));
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
     * 线下续期
     *
     * @param jsonData
     * @return
     */
    @Log(title = "线下续期")
    @ResponseBody
    @PostMapping("/offlineRenewal")
    public String offlineRenewal(@RequestBody String jsonData, HttpServletRequest request) throws ParseException {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_FINANCE) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Object idObj = params.get("id");
        if (null == idObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        OrderRepayment orderRepayment = orderRepaymentMapper.selectByPrimaryKey(Integer.parseInt(idObj.toString()));

        if (null == orderRepayment) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        //String thirdOrderNo = params.get("thirdOrderNo") == null ? ChanPayUtil.generateOutTradeNo() : params.get("thirdOrderNo").toString();
        String thirdOrderNo = ChanPayUtil.generateOutTradeNo();
        String payType = params.get("payType") == null ? "0" : params.get("payType").toString();
        Object renewDay = params.get("renewDay");
        if (null == renewDay) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "续期时间参数非法");
        }
        log.info("续期时间orderRepayment为"+orderRepayment.getRepaymentTime().toString());
        log.info("续期时间renewDay为"+renewDay.toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = new SimpleDateFormat("yyyy-MM-dd").format(orderRepayment.getRepaymentTime());
        log.info("续期时间format为"+format);
        Date passDate = dateFormat.parse(renewDay.toString());
        Date repayDate = dateFormat.parse(format);
        int day = DateUtil.daysBetween(repayDate, passDate);
        log.info("续期时间天数为"+day);

        if (day <= 0) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "续期时间不能低于当前应还时间");
        }
        //续期金额
        Object paidAmountObj = params.get("paidAmount");
        if (null == paidAmountObj) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "续期金额参数非法");
        }
        Integer paidAmount = Integer.parseInt(paidAmountObj.toString()) * 100;

        // 续期费
     /*   Map<String, Object> loanRuleParams = new HashMap<>();
        loanRuleParams.put("status", Constant.LOAN_RULE_STATUS);
        //查询贷款规则(loan_rule_config表)
        LoanRuleConfig loanConfig = loanRuleConfigService.findLoanConfigByParams(loanRuleParams);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(orderRepayment.getUserId());
        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(userInfo.getChannelId());
        int renewalFee = (int) (loanConfig.getRenewalFee() * 100);
        // 待还滞纳金
        int waitLate = orderRepayment.getLateFee();
        // 服务费
        Integer loanApr = orderRepayment.getFeeAmount(); // 服务费
        Integer allCount = waitLate + loanApr + renewalFee;
        log.info("需要支付的费用(allCount)=============" + allCount);*/

        // 待还滞纳金
        int waitLate = orderRepayment.getLateFee();
        // 服务费
        Integer loanApr = orderRepayment.getFeeAmount(); // 服务费

        /*if (Constant.REPAYMENT_STATUS_INIT != orderRepayment.getPaidAmount()) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "部分还款状态不支持续期");
        }*/
        /*Byte status = orderRepayment.getStatus();
        if (Constant.REPAYMENT_STATUS_INIT != status) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "当前状态不支持续期");
        } else
        if (!allCount.equals(paidAmount.intValue())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "续期费用应为" + (allCount / 100.0) + "元");
        }*/
        // 待还总金额
        Integer id = orderRepayment.getId();
        String redis = redisUtil.get(PREFIX + "renewal" + id);
        if ("true".equals(redis)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "请30s后再试");
        }
        redisUtil.set(PREFIX + "renewal" + id, "true", 30);
        int waitRepay = orderRepayment.getRepaymentAmount() - orderRepayment.getPaidAmount() - orderRepayment.getReduceAmount();
        // 待还本金
        //int waitAmount = waitRepay - waitLate; //涉及逾期复杂 先去掉了
        int waitAmount = waitRepay;
        // 前期工作
        OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(orderRepayment.getBorrowId());
        OrderRenewal rr = buildRenewalRecord(orderRepayment, waitAmount, Byte.parseByte(payType), loanApr, waitLate, paidAmount + "", bo,
                thirdOrderNo, day);
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", thirdOrderNo);
        List<OrderRenewal> renewalList = orderRenewalMapper.selectSimple(map);
        if (CollectionUtils.isEmpty(renewalList)) {
            //设置线下续期
            rr.setRenewalType(2);
            int beforeRes = orderRenewalMapper.insertSelective(rr);
            if (beforeRes <= 0) {
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "未知错误");

            }
        } else {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "订单号重复,请输入三方支付流水号");
        }
        orderRepaymentService.renewal(orderRepayment, rr);
        //贷超状态推送
        redisUtil.del(PREFIX + "renewal" + id);
        return CallBackResult.returnJson("操作成功");
    }

    private OrderRenewal buildRenewalRecord(OrderRepayment re, int waitAmount, byte payType, Integer loanApr,
                                            int waitLate, String renewalFee, OrderBorrow bo, String orderNo, Integer renewalDay) {
        OrderRenewal rr = new OrderRenewal();
        rr.setUserId(re.getUserId());
        rr.setRepaymentId(re.getId());
        rr.setBorrowId(bo.getId());
        rr.setRepaymentPrincipal(waitAmount);
        rr.setRepaymentFee(loanApr);
        rr.setLateFee(waitLate);
        rr.setRenewalFee(Integer.valueOf(renewalFee));
        rr.setOldRepaymentTime(re.getRepaymentTime());
        rr.setRenewalDay(renewalDay == null ? bo.getLoanTerm().byteValue() : renewalDay.byteValue());
        rr.setStatus(Constant.UNKNOWN);
        rr.setPayType(payType);
        rr.setMoneyAmount(re.getPrincipalAmount() + re.getFeeAmount());
        rr.setRepaymentTime(DateUtil.addDay(re.getRepaymentTime(), rr.getRenewalDay()));
        rr.setOutTradeNo(orderNo);
        rr.setThird(orderNo);
        return rr;
    }

    /**
     * 导出展期记录列表
     *
     * @param request
     * @param response
     * @param jsonData
     * @throws Exception
     */
    @Log(title = "导出展期记录列表")
    @RequestMapping("downloadOrderRenewal")
    public void downloadOrderRenewal(HttpServletRequest request, HttpServletResponse response,

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
        List<RenewalUserVO> userVOList = orderRenewalMapper.findParams(params);
        log.info("OrderRenewalController downloadOrderRenewal size=" + userVOList.size());
        for (RenewalUserVO renewalUserVO : userVOList) {
            renewalUserVO.setAmountToPay(renewalUserVO.getRepaymentAmount() - renewalUserVO.getPaidAmount());
        }
        String title = "续期记录";
        BuildXLSX.setFileDownloadHeader(request, response, title);
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("realName", "客户姓名");
        titleMap.put("phone", "手机号码");
        titleMap.put("statusNormal", "续期状态");
        titleMap.put("applyAmount", "申请金额");
        titleMap.put("createTimeChg", "续期时间");
        titleMap.put("repaymentAmountNormal", "应还金额");
        titleMap.put("renewalFeeNormal", "续期费");
        titleMap.put("renewalDay", "续期期限");
        titleMap.put("repaymentTimeChg", "应还款时间");
        titleMap.put("payTypeNormal", "支付方式");
        titleMap.put("renewalNo", "续期次数");
        titleMap.put("reviewer", "审核人");
        titleMap.put("renewalTypeName", "续期类型");
        titleMap.put("outTradeNo", "交易号");
        titleMap.put("third", "三方交易号");

        XlsxParam xlsxParam = new XlsxParam(userVOList, title, titleMap);
        OutputStream os = response.getOutputStream();
        BuildXLSX.buildExcel(xlsxParam, os);
    }


    /**
     * 导出续期记录列表Excel
     *
     * @param params
     * @return
     */
    @PostMapping("/renewalX")
    public String exportRenewalXls(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) throws Exception {
       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Integer roleId = platformUser.getRoleId();
        if (roleId != Constant.ROLEID_SUPER || isExporExcel(platformUser.getPhoneNumber())) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "没有权限");
        }*/
        List<RenewalUserVO> VOlist = orderRenewalMapper.findParams(params);
        List<Map> map = new ArrayList<>();
        for (RenewalUserVO vo : VOlist) {
            Map<String, Object> param = new HashMap<>();
            param.put("realName", vo.getRealName());
            param.put("outTradeNo", vo.getOutTradeNo());
            param.put("third", vo.getThird());
            param.put("channelName", vo.getChannelName());
            param.put("phone", vo.getPhone());
            param.put("status", vo.getStatusNormal());
            param.put("applyAmount", vo.getApplyAmount());
            param.put("createTimeChg", vo.getCreateTimeChg());
            param.put("renewalDay", vo.getRenewalDay());
            param.put("renewalFee", vo.getRenewalFeeNormal());
            param.put("repaymentTimeChg", vo.getRepaymentTimeChg());
            param.put("renewalType", vo.getRenewalTypeName());

            map.add(param);
        }
        String[] title = new String[]{"客户姓名", "订单号", "第三方订单号", "渠道来源", "手机号码", "续期状态", "申请金额", "续期时间", "续期期限/天", "续期费/元", "应还款时间", "续期类型"};
        String[] properties = new String[]{"realName", "outTradeNo", "third", "channelName", "phone", "status", "applyAmount", "createTimeChg", "renewalDay", "renewalFee", "repaymentTimeChg", "renewalType"};
        String sheetName = "续期记录";
        ExcelExportUtil.exportExcel(response, map, title, properties, sheetName);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, "导出成功");
    }
}
