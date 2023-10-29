package com.summer.web.controller.thirdpart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.summer.api.service.*;
import com.summer.enums.YesOrNo;
import com.summer.service.pay.aochPay.AochPayAPI;
import com.summer.service.pay.bitePay.BitePayAPI;
import com.summer.service.pay.bitePay.dto.InAccountParamDTO;
import com.summer.service.pay.bitePay.dto.InAccountResultDTO;
import com.summer.service.pay.mayaPay.MaYaPayApi;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.impl.BackConfigParamsService;
import com.summer.service.mq.OrderProducer;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import com.summer.util.changjiepay.ChanPayUtil;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Created by tl on 2019/1/19
 */
@Slf4j
@Controller
@RequestMapping(value = "v1.0/api/changJiePay")
public class ChangJiePayController extends BaseController {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IOrderRepaymentService orderRepaymentService;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private UserMoneyRateMapper userMoneyRateMapper;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDAO;
    @Autowired
    private ISmsService smsService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    private static String WITHHOLD_KEYS = "REPAYMENT_REPAY_WITHHOLD";
    private static String RENEWAL_KEYS = "RENEWAL_WITHHOLD";
    private static String WITHDRAW_KEYS = "LOAN_WITHDRAW";
    @Value("${system.prefix}")
    private String PREFIX;
    @Value("${jzf.account_id}")
    private String account_id;
    @Value("${self.url}")
    private String selfUrl;
    @Value("${jzf.jzfPayUrl}")
    private String jzfPayUrl;
    @Value("${jzf.key}")
    private String key;
    @Value("${jzf.success_url}")
    private String success_url;
    @Value("${jzf.error_url}")
    private String error_url;
    @Resource
    private BackConfigParamsService backConfigParamsService;
    @Resource
    private OrderProducer orderProducer;
    @Autowired
    private BitePayAPI bitePayAPI;
    @Autowired
    private AochPayAPI aochPayAPI;
    @Value("${btzf.backUrl}")
    private String backUrl;
    @Value("${btzf.appId}")
    private Integer btzfAppId;
    @Value("${btzf.appKey}")
    private String btzfAppKey;
    @Value("${aczf.userId}")
    private String aczfUserId;
    @Value("${aczf.apiKey}")
    private String aczfApiKey;
    @Value("${aczf.backUrl}")
    private String aczfBackUrl;
    @Autowired
    private MaYaPayApi maYaPayApi;
    @Value("${myzf.mchNo}")
    private String myzfMchNo;
    @Value("${myzf.appId}")
    private String myzfAppId;
    @Value("${myzf.apiKey}")
    private String myzfApiKey;


    @GetMapping("/loanSuccessOrFail/{code}")
    @ResponseBody
    public String loanSuccessOrFail(@PathVariable Integer code) {
        return Objects.equals(YesOrNo.YES.getValue(), code) ? "成功" : "失败";
    }

    private void sendMsg(UserInfo user, BigDecimal moneyAmountBig) {
        try {
            // String smsContent = yunFengMsgUtil.getRemitIsSucc();
            Map<String, String> redisMap = smsService.getRedisMap();
            String smsContent = redisMap.get("sms.service.remitIsSucc");
            smsContent = smsContent.replace("#RealName#", "用户").replace("#applyAmount#", moneyAmountBig.setScale(2, BigDecimal.ROUND_HALF_UP) + "");
            smsService.batchSend(user.getPhone(), smsContent);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sendMsg phone={}, error={}", user.getPhone(), e);
        }
    }


    /**
     * 还款选择
     *
     * @param req
     * @param model
     * @param resp
     * @param id
     * @param payType
     */
    @RequestMapping("/repaymentUsdt")
    @Transactional
    public void repayChoose(HttpServletRequest req, Model model, HttpServletResponse resp, Integer id, Integer payType, Integer returnType) {
        JsonResult rest = null;

        UserInfo userInfo = redisUser(req);
        if (null == userInfo) {
            rest = new JsonResult("-2", "登陆失效，请重新登陆！");
            SpringUtils.renderJson(resp, rest);
            return;
        }

        //查看哪个通道 还款通道选择 0随机,1otc,2bf 为空 默认otc
        BackConfigParamsVo usdt_df = backConfigParamsService.findBySysKey("repay_bf");
        if (null == usdt_df) {
            //repaymentUsdt(req,model,resp,id,payType);
            rest = new JsonResult("609", "还款失败，请联系客服进行线下还款！");
            SpringUtils.renderJson(resp, rest);
            return;
        } else {
            int sysValue = usdt_df.getSysValue();
            if (0 == sysValue) {
                Random random = new Random();
                sysValue = random.nextInt(4) + 1;
            }
            if (1 == sysValue) {
                repaymentByBTZF(req, model, resp, id, payType, returnType);
            } else if (2 == sysValue) {
                repaymentByAOCH(req, model, resp, id, payType, returnType);
            } else if (3 == sysValue) {
                repaymentByMY(req, model, resp, id, payType, returnType);
            } else if (4 == sysValue) {
                repaymentByJZF(req, model, resp, id, payType, returnType);
            } else {
                rest = new JsonResult("609", "还款失败，请联系客服进行线下还款！");
                SpringUtils.renderJson(resp, rest);
            }
        }

    }

    /**
     * 续期选择
     *
     * @param
     * @param model
     * @param resp
     * @param id
     * @param payType
     */
    @RequestMapping("renewalUsdt")
    @Transactional
    public void renewalChoose(HttpServletResponse resp, HttpServletRequest request, Model model, Integer id,
                              BigDecimal money, Integer payType) {
        JsonResult rest = null;
        UserInfo userInfo = redisUser(request);
        if (null == userInfo) {
            rest = new JsonResult("-2", "登陆失效，请重新登陆！");
            SpringUtils.renderJson(resp, rest);
            return;
        }

        //查看哪个通道 还款通道选择 0随机,1otc,2bf 为空 默认otc
        BackConfigParamsVo usdt_df = backConfigParamsService.findBySysKey("repay_bf");
        if (null == usdt_df) {
            rest = new JsonResult("609", "续期失败，请联系客服进行线下续期！");
            SpringUtils.renderJson(resp, rest);
            return;
        } else {
            int sysValue = usdt_df.getSysValue();
            if (0 == sysValue) {
                Random random = new Random();
                sysValue = random.nextInt(4) + 1;
            }
            if (1 == sysValue) {
                renewalByBTZF(resp, request, model, id, money, payType);
            } else if (2 == sysValue) {
                renewalByAOCH(resp, request, model, id, money, payType);
            } else if (3 == sysValue) {
                renewalByMY(resp, request, model, id, money, payType);
            } else if (4 == sysValue) {
                renewalByJZF(resp, request, model, id, money, payType);
            } else {
                rest = new JsonResult("609", "续期失败，请联系客服进行线下续期！");
                SpringUtils.renderJson(resp, rest);
            }
        }

    }

    public void repaymentByJZF(HttpServletRequest req, Model model, HttpServletResponse resp, Integer id, Integer payType, Integer returnType) {
        long s = System.currentTimeMillis();
        log.info("进入【极致付支付】还款--信息校验");
        try {
            String uid = req.getParameter("uid");
            JsonResult rest = null;
            long sre = System.currentTimeMillis();
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);

            long sre1 = System.currentTimeMillis();
            if (re == null || StringUtils.isBlank(uid)) {
                log.error("还款参数错误,uid={},id={}", uid, id);
                return;
            }
//           String redis= redisUtil.get(PREFIX+"repayment"+id);
//            if("true".equals(redis)){
//                rest = new JsonResult("609", "请15分钟后再试");
//            SpringUtils.renderJson(resp, rest);
//            return;
//            }
//            redisUtil.set(PREFIX+"repayment"+id,"true",PAY_WAIT_TIME);
            int money = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            boolean notFitRepaymentRes =
                    Constant.REPAYMENT_STATUS_INIT != re.getStatus() && Constant.REPAYMENT_STATUS_PART_PAID != re.getStatus()
                            && Constant.REPAYMENT_STATUS_OVERDUE != re.getStatus() && Constant.REPAYMENT_STATUS_DIRTY != re.getStatus();
            if (money <= 0) {
                if (notFitRepaymentRes) {
                    rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
                } else {
                    rest = new JsonResult("609", "本条还款不支持还款");
                }
                SpringUtils.renderJson(resp, rest);
                return;
            }

            if (notFitRepaymentRes) {
                rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
            } else {
                HashMap<String, String> result = new HashMap<>();
                long sde = System.currentTimeMillis();
                OrderRepaymentDetail detail = null;
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("repaymentId", id);
                if (returnType != null && 2 == returnType) {
                    orderMap.put("orderType", "JPT");
                    detail = orderRepaymentDetailDAO.selectLatestByRefund(orderMap);
                } else {
                    orderMap.put("orderType", "JP");
                    detail = orderRepaymentDetailDAO.selectLatestByRepay(orderMap);
                }

                long sde1 = System.currentTimeMillis();
                log.info("repay rid={},re={},de={}", id, sre1 - sre, sde1 - sde);
                //查询历史订单是否超时，未超时则继续使用该订单
                if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 20 && StringUtils.isNotBlank(detail.getPayUrl())) {
                    if (queryJZF(detail.getPayUrl())) {
                        //去详情
                        log.info("去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                        result.put("url", detail.getPayUrl());
                        result.put("orderNum", detail.getOrderNo() + "_");
                        rest = new JsonResult("200", "发送成功");
                        rest.setData(result);
                        SpringUtils.renderJson(resp, rest);
                        return;
                    }
                }


                //订单号
                String orderNo = "XMJD" + DateUtil.getCurrentDate("yyyyMMddHHmmssSSS");
                String OutTradeNo = "JP" + ChanPayUtil.generateOutTradeNo();
                try {
                    if (null != returnType && 2 == returnType) {
                        UserMoneyRate userRate = userMoneyRateMapper.findByUserId(re.getUserId());
                        if (null != userRate && 0 == userRate.getRepetitionTimes()) {
                            //如果是退款就修改订单号
                            OutTradeNo = OutTradeNo.replace("JP", "JPT");
                            log.info("退款判定成功，当前订单号为：{}", OutTradeNo);
                            if (DateUtil.isNow(re.getCreateTime())) {
                                log.info("退款判定成功，当前订单借款为{}", DateUtil.isNow(re.getCreateTime()));
                                BackConfigParamsVo bcpvo = backConfigParamsService.findBySysKey(Constant.RETREAT_MONEY);
                                if (null != bcpvo) {
                                    money = bcpvo.getSysValue() * 100;
                                }
                            } else {
                                log.info("rid={},退款失败,只限当天借款才能发起退款", id);
                                rest = new JsonResult("609", "您不是当天借款用户,不能发起退款");
                                SpringUtils.renderJson(resp, rest);
                                return;
                            }
                        } else {
                            log.info("rid={},退款失败,只限新用户才能发起退款", id);
                            rest = new JsonResult("609", "您不是新用户,不能发起退款");
                            SpringUtils.renderJson(resp, rest);
                            return;
                        }
                    }
                } catch (Exception e) {
                    log.info("极支付退款报错{}", e);
                }

                BigDecimal amount = new BigDecimal(money / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                Map<String, Object> map = new HashMap<>();
                map.put("account_id", account_id);
                String type = "bank_bank";
                //payType 支付通道选择？
                List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                if (payTypeList.contains(payType)) {
                    rest = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                map.put("thoroughfare", type);
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());
                map.put("real_name", userInfo.getRealName());
                map.put("phone", userInfo.getPhone());
                map.put("out_trade_no", OutTradeNo);
                map.put("amount", amount);

                map.put("callback_url", selfUrl + "jzfback");
                map.put("success_url", success_url);
                map.put("error_url", error_url);
                String sign = DigestUtils.md5Hex(key.toLowerCase() + DigestUtils.md5Hex(amount.toString() + OutTradeNo));
                map.put("sign", sign);
                map.put("content_type", "json");
                map.put("robin", 1);

                log.info("jzf osdt repay rid={}, param={}", id, map.toString());
                long spo = System.currentTimeMillis();
                String post = HttpUtil.doPost(jzfPayUrl + "create", map);
                long spo1 = System.currentTimeMillis();

                log.info("jzf osdt repay rid={}, res={}", id, post);
                if (StringUtils.isBlank(post)) {
                    rest = new JsonResult("609", "还款失败，请稍后再试！");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                JSONObject jsonObject = JSON.parseObject(post);

                String msg = jsonObject.getString("msg");
                String code = jsonObject.getString("code");
                if (!"200".equals(code)) {
                    log.info("repay rid={},还款失败", id);
                    rest = new JsonResult("609", "还款失败," + msg);
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                String payPage = jsonObject.getString("data");
                doAfterPaymentWork(1, re, money, orderNo, payPage, "", "", OutTradeNo, Constant.PAY_TYPE_ONLINE_OSDT, "");

                result.put("url", payPage);
                result.put("orderNum", OutTradeNo + "_" + orderNo);
                rest = new JsonResult("200", "发送成功");
                rest.setData(result);
                long e = System.currentTimeMillis();
                log.info("repay rid={},tt={},po={}", id, e - s, spo1 - spo);
            }

            SpringUtils.renderJson(resp, rest);

        } catch (Exception e) {
//            redisUtil.del(PREFIX+"repayment"+id);
            e.printStackTrace();
            JsonResult rest = new JsonResult("609", "系统异常");
            SpringUtils.renderJson(resp, rest);
        }
    }


    private boolean queryJZF(String url) throws IOException {
        if (true)
            return true;
        String urlReq = url.replace("wechat", "orderCheck");
        log.info("jzf osdt 代收查询 urlReq {}", urlReq);
        String res = HttpUtil.doGet(urlReq);

        log.info("jzf查询请求结果：urlReq={},res={}", urlReq, res);

        if (StringUtils.isNotBlank(res)) {
            String resultCode = JSON.parseObject(res).getString("code");
            if (!"200".equals(resultCode)) {
                //非成功状态
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


    @RequestMapping("repayStatus")
    @ResponseBody
    public String repayStatus(HttpServletRequest req, HttpServletResponse resp,
                              String orderNum) {

        String returnRes = "";
        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_ERROR, "请求参数不符合要求");
        if (org.apache.commons.lang3.StringUtils.isBlank(orderNum)) {
            return JSONObject.toJSONString(callBackResult);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", orderNum.split("_")[0]);
        List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(map);
        if (CollectionUtils.isEmpty(orderRepaymentDetails) || null == orderRepaymentDetails.get(0)) {
            return JSONObject.toJSONString(callBackResult);
        }
        OrderRepaymentDetail orderRepaymentDetail = orderRepaymentDetails.get(0);

        if (orderRepaymentDetail.getStatus() == Constant.REPAYMENTDETAIL_STATUS_DEFEAT) {
            callBackResult.setMessage("支付失败," + orderRepaymentDetail.getRemark());
            callBackResult.setCode(CallBackResult.BUSINESS_ERROR);
        } else if (orderRepaymentDetail.getStatus() == Constant.REPAYMENTDETAIL_STATUS_PAID) {
            callBackResult.setMessage("支付成功");
            callBackResult.setCode(CallBackResult.SUCCESS);
        } else if (orderRepaymentDetail.getStatus() == Constant.REPAYMENTDETAIL_STATUS_PAYING) {
            callBackResult.setMessage("支付处理中");
            callBackResult.setCode(CallBackResult.BUSINESS_WAIT);
        } else {
            callBackResult.setMessage("待支付");
            callBackResult.setCode(CallBackResult.BUSINESS_INIT);
        }
        return JSONObject.toJSONString(callBackResult);
    }


    public void renewalByJZF(HttpServletResponse resp, HttpServletRequest request, Model model, Integer id,
                             BigDecimal money, Integer payType) {
        log.info("进入【极致付支付】续期费...");
        JsonResult result = new JsonResult("609", "系统异常");
        try {
            String uid = request.getParameter("uid");
            if (StringUtils.isBlank(uid)) {
                log.error("续期参数错误uid=null,uid={},id={}", uid, id);
                return;
            }
            Integer intUid = Integer.parseInt(uid);
            UserInfo user = userInfoMapper.selectByPrimaryKey(intUid);
            if (user == null) {
                JsonResult rest = new JsonResult("609", "未登录");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (payType == null) {
                JsonResult rest = new JsonResult("609", "请选择支付方式");
                SpringUtils.renderJson(resp, rest);
                return;
            }

            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            if (re == null) {
                log.error("续期参数错误re==null,uid={},id={}", uid, id);
                return;
            }
//            String redis= redisUtil.get(PREFIX+"renewal"+id);
//            if("true".equals(redis)){
//                result = new JsonResult("609", "请15分钟后再试");
//                SpringUtils.renderJson(resp, result);
//                return;
//            }
//            redisUtil.set(PREFIX+"renewal"+id,"true",PAY_WAIT_TIME);
            OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(re.getBorrowId());
            if (bo == null) {
                log.error("续期参数错误bo==null,uid={},id={}", uid, id);
                return;
            }

            LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(user.getChannelId());
            int renewalFee = (int) (loanConfig.getRenewalFee() * 100);
            // 待还总金额
            int waitRepay = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            // 待还滞纳金
            int waitLate = re.getLateFee();
            // 待还本金
            int waitAmount = waitRepay - waitLate;
            // 服务费
            Integer loanApr = re.getFeeAmount(); // 服务费
            Integer allCount = waitLate + loanApr + renewalFee;
            log.info("需要支付的费用(allCount)=============" + allCount);
            Integer intValue = money.multiply(new BigDecimal(100)).intValue();
            Byte status = re.getStatus();
            if (Constant.REPAYMENT_STATUS_INIT != status) {
                result = new JsonResult("609", "当前状态不支持续期");
            } else {

                if (!allCount.equals(intValue)) {
                    result = new JsonResult("609", "您的费用" + (allCount / 100.0) + "已更新，请刷新当前页面");
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    OrderRenewal detail = orderRenewalMapper.selectLatestByRepaymentId(id);
                    if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 20 && StringUtils.isNotBlank(detail.getPayUrl())) {
                        if (queryJZF(detail.getPayUrl())) {
                            //去详情
                            log.info("续期去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                            data.put("url", detail.getPayUrl());
                            data.put("orderNum", detail.getOutTradeNo() + "_");
                            result = new JsonResult("200", "发送成功");
                            result.setData(data);

                            SpringUtils.renderJson(resp, result);
                            return;
                        }
                    }
                    String OutTradeNo = "JR" + ChanPayUtil.generateOutTradeNo();
                    BigDecimal amount = new BigDecimal(allCount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    Map<String, Object> map = new HashMap<>();
                    map.put("account_id", account_id);
                    String type = "bank_bank";
                    List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                    if (payTypeList.contains(payType)) {
                        result = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    map.put("thoroughfare", type);
                    map.put("out_trade_no", OutTradeNo);
                    map.put("amount", amount);
                    map.put("real_name", user.getRealName());
                    map.put("phone", user.getPhone());
                    map.put("callback_url", selfUrl + "jzfback");
                    map.put("success_url", success_url);
                    map.put("error_url", error_url);
                    String sign = DigestUtils.md5Hex(key.toLowerCase() + DigestUtils.md5Hex(amount.toString() + OutTradeNo));
                    map.put("sign", sign);
                    map.put("content_type", "json");
                    map.put("robin", 1);

                    log.info("renewalByJZF osdt renewal param {}", map.toString());
                    String post = HttpUtil.doPost(jzfPayUrl + "create", map);

                    log.info("renewalByJZF osdt renewal res {}", post);
                    if (StringUtils.isBlank(post)) {
                        result = new JsonResult("609", "续期失败，请稍后再试！");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    JSONObject jsonObject = JSON.parseObject(post);
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");
                    if (!"200".equals(code)) {
                        result = new JsonResult("609", "续期失败," + msg);
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    String payPage = jsonObject.getString("data");
                    //待还本金，续支付金额，服务费，滞纳金，额外的续期费，续期天数
                    OrderRenewal rr = buildRenewalRecord(re, waitAmount, allCount, loanApr, waitLate, renewalFee + "", bo,
                            OutTradeNo, loanConfig.getRenewalExpire(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("out_trade_no", OutTradeNo);
                    List<OrderRenewal> renewalList = orderRenewalMapper.selectSimple(maps);
                    if (CollectionUtils.isEmpty(renewalList)) {
                        //设置线上续期
                        rr.setRenewalType(1);
                        if (StringUtils.isNotBlank(payPage)) {
                            rr.setPayUrl(payPage);
                        }
                        orderRenewalMapper.insertSelective(rr);
                    }
                    data.put("url", payPage);
                    data.put("orderNum", OutTradeNo + "_");
                    result = new JsonResult("200", "发送成功");
                    result.setData(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            redisUtil.del(PREFIX+"renewal"+id);
        }
        SpringUtils.renderJson(resp, result);
    }


    @RequestMapping("renewalStatus")
    @ResponseBody
    public String renewalStatus(HttpServletRequest req, HttpServletResponse resp,
                                String orderNum) {

        String returnRes = "";
        CallBackResult callBackResult = new CallBackResult(CallBackResult.BUSINESS_ERROR, "请求参数不符合要求");
        if (org.apache.commons.lang3.StringUtils.isBlank(orderNum)) {
            return JSONObject.toJSONString(callBackResult);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", orderNum.split("_")[0]);
        List<OrderRenewal> renewalList = orderRenewalMapper.selectSimple(map);
        if (CollectionUtils.isEmpty(renewalList) || null == renewalList.get(0)) {
            return JSONObject.toJSONString(callBackResult);
        }
        OrderRenewal orderRenewal = renewalList.get(0);

        if (orderRenewal.getStatus() == Constant.RENEWAL_STATUS_DEFEAT) {
            callBackResult.setMessage("支付失败," + orderRenewal.getRemark());
            callBackResult.setCode(CallBackResult.BUSINESS_ERROR);
        } else if (orderRenewal.getStatus() == Constant.RENEWAL_STATUS_PAID) {
            callBackResult.setMessage("支付成功");
            callBackResult.setCode(CallBackResult.SUCCESS);
        } else if (orderRenewal.getStatus() == Constant.RENEWAL_STATUS_PAING) {
            callBackResult.setMessage("支付处理中");
            callBackResult.setCode(CallBackResult.BUSINESS_WAIT);
        } else {
            callBackResult.setMessage("待支付");
            callBackResult.setCode(CallBackResult.BUSINESS_INIT);
        }
        return JSONObject.toJSONString(callBackResult);
    }

    private OrderRenewal buildRenewalRecord(OrderRepayment re, int waitAmount, int allCount, Integer loanApr,
                                            int waitLate, String renewalFee, OrderBorrow bo, String orderNo, Integer renewalDay, byte payType, String third) {
        OrderRenewal rr = new OrderRenewal();
        rr.setUserId(re.getUserId());
        rr.setRepaymentId(re.getId());
        rr.setBorrowId(bo.getId());
        rr.setRepaymentPrincipal(waitAmount);
        rr.setRepaymentFee(loanApr);
        rr.setLateFee(waitLate);
        rr.setRenewalFee(Integer.valueOf(allCount));
        rr.setOldRepaymentTime(re.getRepaymentTime());
        rr.setRenewalDay(renewalDay == null ? bo.getLoanTerm().byteValue() : renewalDay.byteValue());
        rr.setStatus(Constant.UNKNOWN);
        rr.setPayType(payType);
        rr.setMoneyAmount(re.getPrincipalAmount() + re.getFeeAmount());
        rr.setRepaymentTime(DateUtil.addDay(re.getRepaymentTime(), rr.getRenewalDay()));
        rr.setOutTradeNo(orderNo);
        if (StringUtils.isNotBlank(third)) {
            rr.setThird(third);
        }
        return rr;
    }


    /**
     * 支付完成后的工作
     *
     * @param type    1 还款
     *                2 续期
     * @param re
     * @param money
     * @param orderNo
     */
    public void doAfterPaymentWork(int type, OrderRepayment re, int money, String orderNo, String url,
                                   String result, String notifyParams, String checkNo, byte payType, String third) {

        if (type == 1) {
            /*生成发送订单记录*/

            //生成待还
            OrderRepaymentDetail detail = buildRepaymentDetail(re, checkNo, money, payType, third, url);
            Map<String, Object> map = new HashMap<>();
            map.put("out_trade_no", checkNo);
            List<OrderRepaymentDetail> renewalList = orderRepaymentDetailDAO.selectSimple(map);
            if (CollectionUtils.isEmpty(renewalList)) {
                orderRepaymentDetailDAO.insertSelective(detail);
            }
        }
    }

    /**
     * 续期金额不足，创建部分还款订单
     *
     * @param type    1 还款
     * @param re
     * @param money
     * @param orderNo
     */
    public void doAfterPaymentWork1(int type, OrderRepayment re, int money, String orderNo, String url,
                                    String result, String notifyParams, String checkNo, byte payType, String third) {
        if (type == 1) {
            /*生成发送订单记录*/

            //生成待还
            OrderRepaymentDetail detail = buildRepaymentDetail(re, checkNo, money, payType, third, url);
            detail.setRemark("用户续期金额不足，按部分还款处理");
            Map<String, Object> map = new HashMap<>();
            map.put("out_trade_no", checkNo);
            List<OrderRepaymentDetail> renewalList = orderRepaymentDetailDAO.selectSimple(map);
            if (CollectionUtils.isEmpty(renewalList)) {
                orderRepaymentDetailDAO.insertSelective(detail);
            }
        }
    }

    private OrderRepaymentDetail buildRepaymentDetail(OrderRepayment re, String orderNo, int money, byte payType, String third, String url) {
        OrderRepaymentDetail detail = new OrderRepaymentDetail();
        detail.setUserId(re.getUserId());
        detail.setRepaymentId(re.getId());
        detail.setPaidAmount(money);
        detail.setOrderNo(orderNo);
        if (StringUtils.isNotBlank(third)) {
            detail.setThirdOrderNo(third);
        }
        if (StringUtils.isNotBlank(url)) {
            detail.setPayUrl(url);
        }
        detail.setPayType(payType);
        detail.setRemark("认证支付还款");
        detail.setOperatorUserId(0);
        detail.setStatus(Constant.REPAYMENTDETAIL_STATUS_INIT);
        detail.setBorrowId(re.getBorrowId());
        Date now = new Date();
        detail.setCreateTime(now);
        detail.setOverdue(Constant.REPAYMENTDETAIL_TYPE_NORMAL);
        if (DateUtil.daysBetween(re.getRepaymentTime(), now) > 0) {
            detail.setOverdue(Constant.REPAYMENTDETAIL_TYPE_OVERDUE);
        } else if (DateUtil.daysBetween(re.getRepaymentTime(), now) < 0) {
            detail.setOverdue(Constant.REPAYMENTDETAIL_TYPE_FORWARD);
        }
        return detail;
    }

    public void repaymentByBTZF(HttpServletRequest req, Model model, HttpServletResponse resp, Integer id, Integer payType, Integer returnType) {
        long s = System.currentTimeMillis();
        log.info("进入【币特支付】还款--信息校验");
        try {
            String uid = req.getParameter("uid");
            JsonResult rest = null;
            long sre = System.currentTimeMillis();
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            long sre1 = System.currentTimeMillis();
            if (re == null || StringUtils.isBlank(uid)) {
                log.error("还款参数错误,uid={},id={}", uid, id);
                return;
            }
            int money = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            boolean notFitRepaymentRes =
                    Constant.REPAYMENT_STATUS_INIT != re.getStatus() && Constant.REPAYMENT_STATUS_PART_PAID != re.getStatus()
                            && Constant.REPAYMENT_STATUS_OVERDUE != re.getStatus() && Constant.REPAYMENT_STATUS_DIRTY != re.getStatus();
            if (money <= 0) {
                if (notFitRepaymentRes) {
                    rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
                } else {
                    rest = new JsonResult("609", "本条还款不支持还款");
                }
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (notFitRepaymentRes) {
                rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
            } else {
                HashMap<String, String> result = new HashMap<>();
                long sde = System.currentTimeMillis();
                OrderRepaymentDetail detail = null;
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("repaymentId", id);
                if (returnType != null && 2 == returnType) {
                    orderMap.put("orderType", "BPT");
                    detail = orderRepaymentDetailDAO.selectLatestByRefund(orderMap);
                } else {
                    orderMap.put("orderType", "BP");
                    detail = orderRepaymentDetailDAO.selectLatestByRepay(orderMap);
                }

                long sde1 = System.currentTimeMillis();
                log.info("repay rid={},re={},de={}", id, sre1 - sre, sde1 - sde);
                //查询历史订单是否超时，未超时则继续使用该订单
                if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 15 && StringUtils.isNotBlank(detail.getPayUrl())) {
                    if (queryJZF(detail.getPayUrl())) {
                        //去详情
                        log.info("去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                        result.put("url", detail.getPayUrl());
                        result.put("orderNum", detail.getOrderNo() + "_");
                        rest = new JsonResult("200", "发送成功");
                        rest.setData(result);
                        SpringUtils.renderJson(resp, rest);
                        return;
                    }
                }

                //订单号
                String orderNo = "XMJD" + DateUtil.getCurrentDate("yyyyMMddHHmmssSSS");
                String OutTradeNo = "BP" + ChanPayUtil.generateOutTradeNo();
                try {
                    if (null != returnType && 2 == returnType) {
                        UserMoneyRate userRate = userMoneyRateMapper.findByUserId(re.getUserId());
                        if (null != userRate && 0 == userRate.getRepetitionTimes()) {
                            //如果是退款就修改订单号
                            OutTradeNo = OutTradeNo.replace("BP", "BPT");
                            log.info("退款判定成功，当前订单号为：{}", OutTradeNo);
                            if (DateUtil.isNow(re.getCreateTime())) {
                                log.info("退款判定成功，当前订单借款为{}", DateUtil.isNow(re.getCreateTime()));
                                BackConfigParamsVo bcpvo = backConfigParamsService.findBySysKey(Constant.RETREAT_MONEY);
                                if (null != bcpvo) {
                                    money = bcpvo.getSysValue() * 100;
                                }
                            } else {
                                log.info("rid={},退款失败,只限当天借款才能发起退款", id);
                                rest = new JsonResult("609", "您不是当天借款用户,不能发起退款");
                                SpringUtils.renderJson(resp, rest);
                                return;
                            }
                        } else {
                            log.info("rid={},退款失败,只限新用户才能发起退款", id);
                            rest = new JsonResult("609", "您不是新用户,不能发起退款");
                            SpringUtils.renderJson(resp, rest);
                            return;
                        }
                    }
                } catch (Exception e) {
                    log.info("bite支付退款报错{}", e);
                }

                BigDecimal amount = new BigDecimal(money / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);

                //payType 支付通道选择？
                List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                if (payTypeList.contains(payType)) {
                    rest = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());
                InAccountParamDTO inAccountParamDTO = new InAccountParamDTO();
                inAccountParamDTO.setAppid(btzfAppId);
                inAccountParamDTO.setApikey(btzfAppKey);
                inAccountParamDTO.setNotifyUrl(backUrl + "btzfInDsback");
                inAccountParamDTO.setOutTradeNo(OutTradeNo);
                inAccountParamDTO.setName(userInfo.getRealName());
                inAccountParamDTO.setPidnum("120112197808053325");
                inAccountParamDTO.setMoney(amount);
                long spo = System.currentTimeMillis();
                InAccountResultDTO inAccountResultDTO = bitePayAPI.InAccount(inAccountParamDTO);
                log.info("btzf osdt repay rid={}, param={}", id, inAccountResultDTO.toString());
                long spo1 = System.currentTimeMillis();
                log.info("btzf osdt repay rid={}, res={}", id, inAccountResultDTO.toString());
                if (Objects.isNull(inAccountResultDTO)) {
                    rest = new JsonResult("609", "还款失败，请稍后再试！");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                String msg = inAccountResultDTO.getMsg();
                String status = inAccountResultDTO.getStatus();
                if (!"1".equals(status)) {
                    log.info("repay rid={},还款失败", id);
                    rest = new JsonResult("609", "还款失败," + msg);
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                String payPage = inAccountResultDTO.getPayurl();
                doAfterPaymentWork(1, re, money, orderNo, payPage, "", "", OutTradeNo, Constant.PAY_TYPE_ONLINE_OSDT, "");
                result.put("url", payPage);
                result.put("orderNum", OutTradeNo + "_" + orderNo);
                rest = new JsonResult("200", "发送成功");
                rest.setData(result);
                long e = System.currentTimeMillis();
                log.info("repay rid={},tt={},po={}", id, e - s, spo1 - spo);
            }
            SpringUtils.renderJson(resp, rest);
        } catch (Exception e) {
//            redisUtil.del(PREFIX+"repayment"+id);
            e.printStackTrace();
            JsonResult rest = new JsonResult("609", "系统异常");
            SpringUtils.renderJson(resp, rest);
        }
    }

    public void renewalByBTZF(HttpServletResponse resp, HttpServletRequest request, Model model, Integer id,
                              BigDecimal money, Integer payType) {
        log.info("进入【币特支付】续期费...");
        JsonResult result = new JsonResult("609", "系统异常");
        try {
            String uid = request.getParameter("uid");
            if (StringUtils.isBlank(uid)) {
                log.error("续期参数错误uid=null,uid={},id={}", uid, id);
                return;
            }
            Integer intUid = Integer.parseInt(uid);
            UserInfo user = userInfoMapper.selectByPrimaryKey(intUid);
            if (user == null) {
                JsonResult rest = new JsonResult("609", "未登录");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (payType == null) {
                JsonResult rest = new JsonResult("609", "请选择支付方式");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            if (re == null) {
                log.error("续期参数错误re==null,uid={},id={}", uid, id);
                return;
            }

            OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(re.getBorrowId());
            if (bo == null) {
                log.error("续期参数错误bo==null,uid={},id={}", uid, id);
                return;
            }

            LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(user.getChannelId());
            int renewalFee = (int) (loanConfig.getRenewalFee() * 100);
            // 待还总金额
            int waitRepay = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            // 待还滞纳金
            int waitLate = re.getLateFee();
            // 待还本金
            int waitAmount = waitRepay - waitLate;
            // 服务费
            Integer loanApr = re.getFeeAmount(); // 服务费
            Integer allCount = waitLate + loanApr + renewalFee;
            log.info("需要支付的费用(allCount)=============" + allCount);
            Integer intValue = money.multiply(new BigDecimal(100)).intValue();
            Byte status = re.getStatus();
            if (Constant.REPAYMENT_STATUS_INIT != status) {
                result = new JsonResult("609", "当前状态不支持续期");
            } else {

                if (!allCount.equals(intValue)) {
                    result = new JsonResult("609", "您的费用" + (allCount / 100.0) + "已更新，请刷新当前页面");
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    OrderRenewal detail = orderRenewalMapper.selectLatestByRepaymentId(id);
                    if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 15 && StringUtils.isNotBlank(detail.getPayUrl())) {
                        if (queryJZF(detail.getPayUrl())) {
                            //去详情
                            log.info("续期去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                            data.put("url", detail.getPayUrl());
                            data.put("orderNum", detail.getOutTradeNo() + "_");
                            result = new JsonResult("200", "发送成功");
                            result.setData(data);

                            SpringUtils.renderJson(resp, result);
                            return;
                        }
                    }
                    String OutTradeNo = "BR" + ChanPayUtil.generateOutTradeNo();
                    BigDecimal amount = new BigDecimal(allCount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    Map<String, Object> map = new HashMap<>();
                    map.put("account_id", account_id);
                    String type = "bank_bank";
                    List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                    if (payTypeList.contains(payType)) {
                        result = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());
                    InAccountParamDTO inAccountParamDTO = new InAccountParamDTO();
                    inAccountParamDTO.setAppid(btzfAppId);
                    inAccountParamDTO.setApikey(btzfAppKey);
                    inAccountParamDTO.setNotifyUrl(backUrl + "btzfInDsback");
                    inAccountParamDTO.setOutTradeNo(OutTradeNo);
                    inAccountParamDTO.setName(userInfo.getRealName());
                    inAccountParamDTO.setPidnum("120112197808053325");
                    inAccountParamDTO.setMoney(amount);
                    long spo = System.currentTimeMillis();
                    InAccountResultDTO inAccountResultDTO = bitePayAPI.InAccount(inAccountParamDTO);
                    log.info("btzf osdt repay rid={}, param={}", id, inAccountResultDTO.toString());
                    long spo1 = System.currentTimeMillis();
                    log.info("btzf osdt repay rid={}, res={}", id, inAccountResultDTO.toString());

                    if (Objects.isNull(inAccountResultDTO)) {
                        result = new JsonResult("609", "续期失败，请稍后再试！");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    String msg = inAccountResultDTO.getMsg();
                    String code = inAccountResultDTO.getStatus();
                    if (!"1".equals(code)) {
                        result = new JsonResult("609", "续期失败," + msg);
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    String payPage = inAccountResultDTO.getPayurl();

                    //待还本金，续支付金额，服务费，滞纳金，额外的续期费，续期天数
                    OrderRenewal rr = buildRenewalRecord(re, waitAmount, allCount, loanApr, waitLate, renewalFee + "", bo,
                            OutTradeNo, loanConfig.getRenewalExpire(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("out_trade_no", OutTradeNo);
                    List<OrderRenewal> renewalList = orderRenewalMapper.selectSimple(maps);
                    if (CollectionUtils.isEmpty(renewalList)) {
                        //设置线上续期
                        rr.setRenewalType(1);
                        if (StringUtils.isNotBlank(payPage)) {
                            rr.setPayUrl(payPage);
                        }
                        orderRenewalMapper.insertSelective(rr);
                    }
                    data.put("url", payPage);
                    data.put("orderNum", OutTradeNo + "_");
                    result = new JsonResult("200", "发送成功");
                    result.setData(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            redisUtil.del(PREFIX+"renewal"+id);
        }
        SpringUtils.renderJson(resp, result);
    }


    public void repaymentByAOCH(HttpServletRequest req, Model model, HttpServletResponse resp, Integer id, Integer payType, Integer returnType) {
        long s = System.currentTimeMillis();
        log.info("进入【奥创支付】还款--信息校验");
        try {
            String uid = req.getParameter("uid");
            JsonResult rest = null;
            long sre = System.currentTimeMillis();
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            long sre1 = System.currentTimeMillis();
            if (re == null || StringUtils.isBlank(uid)) {
                log.error("还款参数错误,uid={},id={}", uid, id);
                return;
            }
            int money = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            boolean notFitRepaymentRes =
                    Constant.REPAYMENT_STATUS_INIT != re.getStatus() && Constant.REPAYMENT_STATUS_PART_PAID != re.getStatus()
                            && Constant.REPAYMENT_STATUS_OVERDUE != re.getStatus() && Constant.REPAYMENT_STATUS_DIRTY != re.getStatus();
            if (money <= 0) {
                if (notFitRepaymentRes) {
                    rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
                } else {
                    rest = new JsonResult("609", "本条还款不支持还款");
                }
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (notFitRepaymentRes) {
                rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
            } else {
                HashMap<String, String> result = new HashMap<>();
                long sde = System.currentTimeMillis();
                OrderRepaymentDetail detail = null;
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("repaymentId", id);
                if (returnType != null && 2 == returnType) {
                    orderMap.put("orderType", "APT");
                    detail = orderRepaymentDetailDAO.selectLatestByRefund(orderMap);
                } else {
                    orderMap.put("orderType", "AP");
                    detail = orderRepaymentDetailDAO.selectLatestByRepay(orderMap);
                }

                long sde1 = System.currentTimeMillis();
                log.info("repay rid={},re={},de={}", id, sre1 - sre, sde1 - sde);
                //查询历史订单是否超时，未超时则继续使用该订单
                if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 5 && StringUtils.isNotBlank(detail.getPayUrl())) {
                    if (queryJZF(detail.getPayUrl())) {
                        //去详情
                        log.info("去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                        result.put("url", detail.getPayUrl());
                        result.put("orderNum", detail.getOrderNo() + "_");
                        rest = new JsonResult("200", "发送成功");
                        rest.setData(result);
                        SpringUtils.renderJson(resp, rest);
                        return;
                    }
                }

                //订单号
                String orderNo = "XMJD" + DateUtil.getCurrentDate("yyyyMMddHHmmssSSS");
                String OutTradeNo = "AP" + ChanPayUtil.generateOutTradeNo();
                try {
                    if (null != returnType && 2 == returnType) {
                        UserMoneyRate userRate = userMoneyRateMapper.findByUserId(re.getUserId());
                        if (null != userRate && 0 == userRate.getRepetitionTimes()) {
                            //如果是退款就修改订单号
                            OutTradeNo = OutTradeNo.replace("AP", "APT");
                            log.info("退款判定成功，当前订单号为：{}", OutTradeNo);
                            if (DateUtil.isNow(re.getCreateTime())) {
                                log.info("退款判定成功，当前订单借款为{}", DateUtil.isNow(re.getCreateTime()));
                                BackConfigParamsVo bcpvo = backConfigParamsService.findBySysKey(Constant.RETREAT_MONEY);
                                if (null != bcpvo) {
                                    money = bcpvo.getSysValue() * 100;
                                }
                            } else {
                                log.info("rid={},退款失败,只限当天借款才能发起退款", id);
                                rest = new JsonResult("609", "您不是当天借款用户,不能发起退款");
                                SpringUtils.renderJson(resp, rest);
                                return;
                            }
                        } else {
                            log.info("rid={},退款失败,只限新用户才能发起退款", id);
                            rest = new JsonResult("609", "您不是新用户,不能发起退款");
                            SpringUtils.renderJson(resp, rest);
                            return;
                        }
                    }
                } catch (Exception e) {
                    log.info("AOCH支付退款报错{}", e);
                }

                BigDecimal amount = new BigDecimal(money / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);

                //payType 支付通道选择？
                List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                if (payTypeList.contains(payType)) {
                    rest = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());
                Map<String, String> aoch = new HashMap();
                aoch.put("userId", aczfUserId);
                aoch.put("apiKey", aczfApiKey);
                aoch.put("notifyUrl", aczfBackUrl + "aczfRepayBack");
                aoch.put("orderNo", OutTradeNo);
                aoch.put("amount", String.valueOf(amount));
                aoch.put("name", userInfo.getRealName());
                long spo = System.currentTimeMillis();
                String ACResult = aochPayAPI.aochRepay(aoch);
                log.info("btzf osdt repay rid={}, param={}", id, aoch);
                long spo1 = System.currentTimeMillis();
                log.info("btzf osdt repay rid={}, res={}", id, ACResult);
                if (Objects.isNull(ACResult)) {
                    rest = new JsonResult("609", "还款失败，请稍后再试！");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                JSONObject jsonObject = JSON.parseObject(ACResult);
                String status = jsonObject.getString("status");
                if (!"1".equals(status)) {
                    String error = jsonObject.getString("error");
                    log.info("repay rid={},还款失败", id + error);
                    rest = new JsonResult("609", "还款失败," + error);
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                String payPage = jsonObject.getString("payurl");
                doAfterPaymentWork(1, re, money, orderNo, payPage, "", "", OutTradeNo, Constant.PAY_TYPE_ONLINE_OSDT, "");
                result.put("url", payPage);
                result.put("orderNum", OutTradeNo + "_" + orderNo);
                rest = new JsonResult("200", "发送成功");
                rest.setData(result);
                long e = System.currentTimeMillis();
                log.info("repay rid={},tt={},po={}", id, e - s, spo1 - spo);
            }
            SpringUtils.renderJson(resp, rest);
        } catch (Exception e) {
//            redisUtil.del(PREFIX+"repayment"+id);
            e.printStackTrace();
            JsonResult rest = new JsonResult("609", "系统异常");
            SpringUtils.renderJson(resp, rest);
        }
    }

    public void renewalByAOCH(HttpServletResponse resp, HttpServletRequest request, Model model, Integer id,
                              BigDecimal money, Integer payType) {
        log.info("进入【奥创支付】续期费...");
        JsonResult result = new JsonResult("609", "系统异常");
        try {
            String uid = request.getParameter("uid");
            if (StringUtils.isBlank(uid)) {
                log.error("续期参数错误uid=null,uid={},id={}", uid, id);
                return;
            }
            Integer intUid = Integer.parseInt(uid);
            UserInfo user = userInfoMapper.selectByPrimaryKey(intUid);
            if (user == null) {
                JsonResult rest = new JsonResult("609", "未登录");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (payType == null) {
                JsonResult rest = new JsonResult("609", "请选择支付方式");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            if (re == null) {
                log.error("续期参数错误re==null,uid={},id={}", uid, id);
                return;
            }

            OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(re.getBorrowId());
            if (bo == null) {
                log.error("续期参数错误bo==null,uid={},id={}", uid, id);
                return;
            }

            LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(user.getChannelId());
            int renewalFee = (int) (loanConfig.getRenewalFee() * 100);
            // 待还总金额
            int waitRepay = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            // 待还滞纳金
            int waitLate = re.getLateFee();
            // 待还本金
            int waitAmount = waitRepay - waitLate;
            // 服务费
            Integer loanApr = re.getFeeAmount(); // 服务费
            Integer allCount = waitLate + loanApr + renewalFee;
            log.info("需要支付的费用(allCount)=============" + allCount);
            Integer intValue = money.multiply(new BigDecimal(100)).intValue();
            Byte status = re.getStatus();
            if (Constant.REPAYMENT_STATUS_INIT != status) {
                result = new JsonResult("609", "当前状态不支持续期");
            } else {

                if (!allCount.equals(intValue)) {
                    result = new JsonResult("609", "您的费用" + (allCount / 100.0) + "已更新，请刷新当前页面");
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    OrderRenewal detail = orderRenewalMapper.selectLatestByRepaymentId(id);
                    if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 5 && StringUtils.isNotBlank(detail.getPayUrl())) {
                        if (queryJZF(detail.getPayUrl())) {
                            //去详情
                            log.info("续期去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                            data.put("url", detail.getPayUrl());
                            data.put("orderNum", detail.getOutTradeNo() + "_");
                            result = new JsonResult("200", "发送成功");
                            result.setData(data);

                            SpringUtils.renderJson(resp, result);
                            return;
                        }
                    }
                    String OutTradeNo = "AR" + ChanPayUtil.generateOutTradeNo();
                    BigDecimal amount = new BigDecimal(allCount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                    if (payTypeList.contains(payType)) {
                        result = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());

                    Map<String, String> aoch = new HashMap();
                    //aoch.put("url",aczfPayUrl);
                    aoch.put("userId", aczfUserId);
                    aoch.put("apiKey", aczfApiKey);
                    aoch.put("notifyUrl", aczfBackUrl + "aczfRepayBack");

                    aoch.put("orderNo", OutTradeNo);
                    aoch.put("amount", String.valueOf(amount));
                    aoch.put("name", userInfo.getRealName());
                    long spo = System.currentTimeMillis();
                    String ACResult = aochPayAPI.aochRepay(aoch);
                    log.info("btzf osdt repay rid={}, param={}", id, aoch);
                    long spo1 = System.currentTimeMillis();
                    log.info("btzf osdt repay rid={}, res={}", id, ACResult);
                    if (Objects.isNull(ACResult)) {
                        result = new JsonResult("609", "还款失败，请稍后再试！");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    JSONObject jsonObject = JSON.parseObject(ACResult);
                    String ACStatus = jsonObject.getString("status");

                    if (!"1".equals(ACStatus)) {
                        String error = jsonObject.getString("error");
                        result = new JsonResult("609", "续期失败," + error);
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    String payPage = jsonObject.getString("payurl");
                    //待还本金，续支付金额，服务费，滞纳金，额外的续期费，续期天数
                    OrderRenewal rr = buildRenewalRecord(re, waitAmount, allCount, loanApr, waitLate, renewalFee + "", bo,
                            OutTradeNo, loanConfig.getRenewalExpire(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("out_trade_no", OutTradeNo);
                    List<OrderRenewal> renewalList = orderRenewalMapper.selectSimple(maps);
                    if (CollectionUtils.isEmpty(renewalList)) {
                        //设置线上续期
                        rr.setRenewalType(1);
                        if (StringUtils.isNotBlank(payPage)) {
                            rr.setPayUrl(payPage);
                        }
                        orderRenewalMapper.insertSelective(rr);
                    }
                    data.put("url", payPage);
                    data.put("orderNum", OutTradeNo + "_");
                    result = new JsonResult("200", "发送成功");
                    result.setData(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            redisUtil.del(PREFIX+"renewal"+id);
        }
        SpringUtils.renderJson(resp, result);
    }

    //todo td
    public void repaymentByMY(HttpServletRequest req, Model model, HttpServletResponse resp, Integer id, Integer payType, Integer returnType) {
        long s = System.currentTimeMillis();
        log.info("进入【玛雅支付】还款--信息校验");
        try {
            String uid = req.getParameter("uid");
            JsonResult rest = null;
            long sre = System.currentTimeMillis();
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            long sre1 = System.currentTimeMillis();
            if (re == null || StringUtils.isBlank(uid)) {
                log.error("还款参数错误,uid={},id={}", uid, id);
                return;
            }
            int money = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            boolean notFitRepaymentRes =
                    Constant.REPAYMENT_STATUS_INIT != re.getStatus() && Constant.REPAYMENT_STATUS_PART_PAID != re.getStatus()
                            && Constant.REPAYMENT_STATUS_OVERDUE != re.getStatus() && Constant.REPAYMENT_STATUS_DIRTY != re.getStatus();
            if (money <= 0) {
                if (notFitRepaymentRes) {
                    rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
                } else {
                    rest = new JsonResult("609", "本条还款不支持还款");
                }
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (notFitRepaymentRes) {
                rest = new JsonResult("609", "恭喜您！卡内余额充足，还款已成功。");
            } else {
                HashMap<String, String> result = new HashMap<>();
                long sde = System.currentTimeMillis();
                OrderRepaymentDetail detail = null;
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("repaymentId", id);
                if (returnType != null && 2 == returnType) {
                    orderMap.put("orderType", "MYPT");
                    detail = orderRepaymentDetailDAO.selectLatestByRefund(orderMap);
                } else {
                    orderMap.put("orderType", "MYP");
                    detail = orderRepaymentDetailDAO.selectLatestByRepay(orderMap);
                }

                long sde1 = System.currentTimeMillis();
                log.info("repay rid={},re={},de={}", id, sre1 - sre, sde1 - sde);
                //查询历史订单是否超时，未超时则继续使用该订单
                if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 8 && StringUtils.isNotBlank(detail.getPayUrl())) {
                    if (queryJZF(detail.getPayUrl())) {
                        //去详情
                        log.info("去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                        result.put("url", detail.getPayUrl());
                        result.put("orderNum", detail.getOrderNo() + "_");
                        rest = new JsonResult("200", "发送成功");
                        rest.setData(result);
                        SpringUtils.renderJson(resp, rest);
                        return;
                    }
                }

                //订单号
                String orderNo = "XMJD" + DateUtil.getCurrentDate("yyyyMMddHHmmssSSS");
                String OutTradeNo = "MYP" + ChanPayUtil.generateOutTradeNo();
                try {
                    if (null != returnType && 2 == returnType) {
                        UserMoneyRate userRate = userMoneyRateMapper.findByUserId(re.getUserId());
                        if (null != userRate && 0 == userRate.getRepetitionTimes()) {
                            //如果是退款就修改订单号
                            OutTradeNo = OutTradeNo.replace("MYP", "MYPT");
                            log.info("退款判定成功，当前订单号为：{}", OutTradeNo);
                            if (DateUtil.isNow(re.getCreateTime())) {
                                log.info("退款判定成功，当前订单借款为{}", DateUtil.isNow(re.getCreateTime()));
                                BackConfigParamsVo bcpvo = backConfigParamsService.findBySysKey(Constant.RETREAT_MONEY);
                                if (null != bcpvo) {
                                    money = bcpvo.getSysValue() * 100;
                                }
                            } else {
                                log.info("rid={},退款失败,只限当天借款才能发起退款", id);
                                rest = new JsonResult("609", "您不是当天借款用户,不能发起退款");
                                SpringUtils.renderJson(resp, rest);
                                return;
                            }
                        } else {
                            log.info("rid={},退款失败,只限新用户才能发起退款", id);
                            rest = new JsonResult("609", "您不是新用户,不能发起退款");
                            SpringUtils.renderJson(resp, rest);
                            return;
                        }
                    }
                } catch (Exception e) {
                    log.info("玛雅支付退款报错{}", e);
                }

                //BigDecimal amount = new BigDecimal(money / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);

                //payType 支付通道选择？
                List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                if (payTypeList.contains(payType)) {
                    rest = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());

                long spo = System.currentTimeMillis();
                String MYResult = maYaPayApi.maYaRepayment(myzfMchNo, myzfAppId, myzfApiKey, String.valueOf(money), userInfo.getRealName(), aczfBackUrl + "myzfRepayBack", OutTradeNo);
                //{"code":0,"data":{"mchOrderNo":"23213123213447","orderState":1,"payData":"http://cashier.maya-pay.com/#/bank?a=P202212291736263900468","payOrderId":"P202212291736263900468"},"msg":"SUCCESS","sign":"A0D7AB2AB4E3B2AEA7BC0B0640CEB55F"}
                log.info("MYzf osdt repay rid={}, param={}", id, userInfo.getRealName() + "," + String.valueOf(money) + "," + myzfMchNo + "," + myzfAppId);
                long spo1 = System.currentTimeMillis();
                log.info("MYzf osdt repay rid={}, res={}", id, MYResult);
                if (Objects.isNull(MYResult)) {
                    rest = new JsonResult("609", "还款失败，请稍后再试！");
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                JSONObject jsonObject = JSON.parseObject(MYResult);
                int MYCode = jsonObject.getIntValue("code");
                if (MYCode != 0) {
                    String error = jsonObject.getString("msg");
                    log.info("repay rid={},还款失败", id + error);
                    rest = new JsonResult("609", "还款失败," + error);
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                int MYStatus = jsonObject.getJSONObject("data").getIntValue("orderState");
                if (MYStatus != 1) {
                    String error = jsonObject.getString("msg");
                    log.info("repay rid={},还款失败", id + error);
                    rest = new JsonResult("609", "还款失败," + error);
                    SpringUtils.renderJson(resp, rest);
                    return;
                }
                String payPage = jsonObject.getJSONObject("data").getString("payData");
                doAfterPaymentWork(1, re, money, orderNo, payPage, "", "", OutTradeNo, Constant.PAY_TYPE_ONLINE_OSDT, "");
                result.put("url", payPage);
                result.put("orderNum", OutTradeNo + "_" + orderNo);
                rest = new JsonResult("200", "发送成功");
                rest.setData(result);
                long e = System.currentTimeMillis();
                log.info("repay rid={},tt={},po={}", id, e - s, spo1 - spo);
            }
            SpringUtils.renderJson(resp, rest);
        } catch (Exception e) {
//            redisUtil.del(PREFIX+"repayment"+id);
            e.printStackTrace();
            JsonResult rest = new JsonResult("609", "系统异常");
            SpringUtils.renderJson(resp, rest);
        }
    }

    //todo td
    public void renewalByMY(HttpServletResponse resp, HttpServletRequest request, Model model, Integer id,
                            BigDecimal money, Integer payType) {
        log.info("进入【玛雅支付】续期费...");
        JsonResult result = new JsonResult("609", "系统异常");
        try {
            String uid = request.getParameter("uid");
            if (StringUtils.isBlank(uid)) {
                log.error("续期参数错误uid=null,uid={},id={}", uid, id);
                return;
            }
            Integer intUid = Integer.parseInt(uid);
            UserInfo user = userInfoMapper.selectByPrimaryKey(intUid);
            if (user == null) {
                JsonResult rest = new JsonResult("609", "未登录");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            if (payType == null) {
                JsonResult rest = new JsonResult("609", "请选择支付方式");
                SpringUtils.renderJson(resp, rest);
                return;
            }
            OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(id);
            if (re == null) {
                log.error("续期参数错误re==null,uid={},id={}", uid, id);
                return;
            }

            OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(re.getBorrowId());
            if (bo == null) {
                log.error("续期参数错误bo==null,uid={},id={}", uid, id);
                return;
            }

            LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(user.getChannelId());
            int renewalFee = (int) (loanConfig.getRenewalFee() * 100);
            // 待还总金额
            int waitRepay = re.getRepaymentAmount() - re.getPaidAmount() - re.getReduceAmount();
            // 待还滞纳金
            int waitLate = re.getLateFee();
            // 待还本金
            int waitAmount = waitRepay - waitLate;
            // 服务费
            Integer loanApr = re.getFeeAmount(); // 服务费
            Integer allCount = waitLate + loanApr + renewalFee;
            log.info("需要支付的费用(allCount)=============" + allCount);
            Integer intValue = money.multiply(new BigDecimal(100)).intValue();
            Byte status = re.getStatus();
            if (Constant.REPAYMENT_STATUS_INIT != status) {
                result = new JsonResult("609", "当前状态不支持续期");
            } else {

                if (!allCount.equals(intValue)) {
                    result = new JsonResult("609", "您的费用" + (allCount / 100.0) + "已更新，请刷新当前页面");
                } else {
                    HashMap<String, String> data = new HashMap<>();
                    OrderRenewal detail = orderRenewalMapper.selectLatestByRepaymentId(id);
                    if (detail != null && detail.getStatus() < 2 && DateUtil.minutesBetween(detail.getCreateTime()) < 8 && StringUtils.isNotBlank(detail.getPayUrl())) {
                        if (queryJZF(detail.getPayUrl())) {
                            //去详情
                            log.info("续期去详情 rid={},status={},creTime={}", id, detail.getStatus(), detail.getCreateTime());
                            data.put("url", detail.getPayUrl());
                            data.put("orderNum", detail.getOutTradeNo() + "_");
                            result = new JsonResult("200", "发送成功");
                            result.setData(data);

                            SpringUtils.renderJson(resp, result);
                            return;
                        }
                    }
                    String OutTradeNo = "MYR" + ChanPayUtil.generateOutTradeNo();
                    BigDecimal amount = new BigDecimal(allCount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    List<Integer> payTypeList = Lists.newArrayList(2, 3, 4, 5, 6);
                    if (payTypeList.contains(payType)) {
                        result = new JsonResult("609", "该支付通道已关闭，请使用'银行卡转银行卡'支付");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    UserInfo userInfo = userInfoMapper.selectByPrimaryKey(re.getUserId());
                    long spo = System.currentTimeMillis();
                    String MYResult = maYaPayApi.maYaRepayment(myzfMchNo, myzfAppId, myzfApiKey, String.valueOf(intValue), userInfo.getRealName(), aczfBackUrl + "myzfRepayBack", OutTradeNo);
                    log.info("MYzf osdt repay rid={}, param={}", id, userInfo.getRealName() + "," + String.valueOf(money) + "," + myzfMchNo + "," + myzfAppId);
                    long spo1 = System.currentTimeMillis();
                    log.info("MYzf osdt repay rid={}, res={}", id, MYResult);
                    if (Objects.isNull(MYResult)) {
                        result = new JsonResult("609", "还款失败，请稍后再试！");
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    JSONObject jsonObject = JSON.parseObject(MYResult);
                    int MYCode = jsonObject.getIntValue("code");
                    if (MYCode != 0) {
                        String error = jsonObject.getString("msg");
                        result = new JsonResult("609", "续期失败," + error);
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    int MYStatus = jsonObject.getJSONObject("data").getIntValue("orderState");
                    if (MYStatus != 1) {
                        String error = jsonObject.getString("msg");
                        result = new JsonResult("609", "续期失败," + error);
                        SpringUtils.renderJson(resp, result);
                        return;
                    }
                    String payPage = jsonObject.getJSONObject("data").getString("payData");
                    //待还本金，续支付金额，服务费，滞纳金，额外的续期费，续期天数
                    OrderRenewal rr = buildRenewalRecord(re, waitAmount, allCount, loanApr, waitLate, renewalFee + "", bo,
                            OutTradeNo, loanConfig.getRenewalExpire(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("out_trade_no", OutTradeNo);
                    List<OrderRenewal> renewalList = orderRenewalMapper.selectSimple(maps);
                    if (CollectionUtils.isEmpty(renewalList)) {
                        //设置线上续期
                        rr.setRenewalType(1);
                        if (StringUtils.isNotBlank(payPage)) {
                            rr.setPayUrl(payPage);
                        }
                        orderRenewalMapper.insertSelective(rr);
                    }
                    data.put("url", payPage);
                    data.put("orderNum", OutTradeNo + "_");
                    result = new JsonResult("200", "发送成功");
                    result.setData(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            redisUtil.del(PREFIX+"renewal"+id);
        }
        SpringUtils.renderJson(resp, result);
    }


    @RequestMapping(value = "jzfback")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void jzfback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("*******************************************************************");
        Map<String, String> req = getParameters(request);
        log.info("jzfback 回调--------json={}", req);
        String status = req.get("status");
        String outer_trade_no = req.get("out_trade_no");
        String amount = req.get("amount");
        String orderId = req.get("trade_no");
        String sign = req.get("sign");
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(outer_trade_no)) {
            log.error("-------------------------------jzfback error verify false,sign=null || outer_trade_no=null");
            response.getWriter().write("ERROR");
            return;
        }
        String md55 = DigestUtils.md5Hex(key.toLowerCase() + DigestUtils.md5Hex(amount + outer_trade_no));
        if (!md55.equals(sign)) {       //判断MD5加密串是否正确（利用商户密匙）
            log.info("-------------------------------jzfback error verify false,sign={},md55={}", sign, md55);
            response.getWriter().write("ERROR");
            return;
        }
        String lockKey = "jzfback_" + outer_trade_no;
        try {
            Boolean lockResult = redisUtil.setIfAbsent(lockKey, "0", 2, TimeUnit.SECONDS);
            if (!lockResult) {
                log.info("未处理完的回调:{}", outer_trade_no);
                response.getWriter().write("ERROR");
                return;
            }
            //禁止自动还款回调开关
            BackConfigParamsVo zdht = backConfigParamsService.findBySysKey("zdht");
            if (null == zdht) {
                if (outer_trade_no.startsWith("JP")) {
                    //还款
                    // get borroworder
                    Map<String, Object> queryParam = new HashMap<>();
                    queryParam.put("out_trade_no", outer_trade_no);
                    queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                    List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                    //订单处于放款中或放款失败状态且非放款成功状态
                    if (CollectionUtils.isEmpty(orderRepaymentDetails)) {
                        log.info("------------------------repayCallbackCJ error CollectionUtils.isEmpty(orderRepaymentDetails)");
                        response.getWriter().write("success");
                        return;
                    }
                    OrderRepaymentDetail orderRepaymentDetail = orderRepaymentDetails.get(0);
                    if (orderRepaymentDetail != null) {
                        log.info("------------------------repayCallbackCJ orderRepaymentDetail != null");
                        OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(orderRepaymentDetail.getRepaymentId());
                        Integer repayId = re.getId();
                        OrderRepaymentDetail rep = new OrderRepaymentDetail();
                        rep.setId(orderRepaymentDetail.getId());
                        rep.setBorrowId(orderRepaymentDetail.getBorrowId());
                        int paidAmount = new BigDecimal(Double.parseDouble(amount) * 100).intValue();
                        orderRepaymentDetail.setPaidAmount(paidAmount);
                        rep.setPaidAmount(paidAmount);
                        rep.setThirdOrderNo(orderId);
                        if ("success".equals(status)) {
                            // 商户更新订单为成功，处理自己的业务逻辑
                            // 流水号
                            log.info("---------------------repayCallbackCJ WITHDRAWAL_SUCCESS");
                            rep.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                            rep.setPayTime(new Date());
                            if (outer_trade_no.startsWith("JPT")) {
                                log.info("------------refundMoney");
                                BackConfigParamsVo bcpvo = backConfigParamsService.findBySysKey(Constant.RETREAT_MONEY);
                                Integer repaymentAmount = bcpvo.getSysValue() * 100;
                                OrderRepayment orderRepayment = new OrderRepayment();
                                orderRepayment.setRepaymentAmount(repaymentAmount);
                                orderRepayment.setId(repayId);
                                orderRepaymentMapper.updateByPrimaryKeySelective(orderRepayment);
                                re.setRepaymentAmount(repaymentAmount);
                            }
                            boolean isTotal = orderRepaymentService.repay(re, orderRepaymentDetail);
                            if (!outer_trade_no.startsWith("JPT")) {
                                // 提额
                                if (isTotal) {
                                    orderRepaymentService.upLimit(re);
                                }
                            }
                            //用户退款


                        } else {
                            log.info("------------------------repayCallbackCJ PAY_FAIL ");
                            rep.setStatus(Constant.REPAYMENTDETAIL_STATUS_DEFEAT);
                        }
                        //用户退款
                        if (outer_trade_no.startsWith("JPT")) {
                            rep.setRemark("用户APP退款");
                        }
                        orderRepaymentDetailDAO.updateByPrimaryKeySelective(rep);

                        //老客第一次还款强制下单
                        try {
                            orderProducer.oldAuto(re.getUserId());
                        } catch (Exception e) {
                            log.info("老客第一次还款强制下单异常:{},", e.getMessage());
                        }


                    }
                } else if (outer_trade_no.startsWith("JR")) {
                    //续期
                    // get borroworder
                    Map<String, Object> queryParam = new HashMap<>();
                    queryParam.put("out_trade_no", outer_trade_no);
                    queryParam.put("payStatus", Constant.RENEWAL_STATUS_PAID);
                    List<OrderRenewal> orderRenewals = orderRenewalMapper.selectSimple(queryParam);
                    //订单处于放款中或放款失败状态且非放款成功状态
                    if (CollectionUtils.isEmpty(orderRenewals)) {
                        log.info("------------------------renewalCallbackCJ error CollectionUtils.isEmpty(orderRenewals)");
                        response.getWriter().write("success");
                        return;
                    }
                    OrderRenewal orderRenewal = orderRenewals.get(0);
                    if (orderRenewal != null) {

                        OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(orderRenewal.getRepaymentId());
                        OrderRenewal rep = new OrderRenewal();
                        rep.setId(orderRenewal.getId());
                        rep.setBorrowId(orderRenewal.getBorrowId());
                        rep.setThird(orderId);
                        if ("success".equals(status)) {
                            // 商户更新订单为成功，处理自己的业务逻辑
                            // 流水号
                            log.info("------------------------------renewalCallbackCJ WITHDRAWAL_SUCCESS");

                            if (new BigDecimal(amount).multiply(new BigDecimal(100)).subtract(new BigDecimal(orderRenewal.getRenewalFee())).compareTo(new BigDecimal(0)) == -1)//实际支付金额小于续期费用
                            {

                                log.info("用户续期支付金额不足续期费用，把实际支付金额按部分还款处理");
                                //创建部分还款订单
                                doAfterPaymentWork1(1, re, 0, "", "", "", "", orderRenewal.getOutTradeNo(), Constant.PAY_TYPE_ONLINE_OSDT, "");

                                queryParam.put("out_trade_no", orderRenewal.getOutTradeNo());
                                queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                                List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                                OrderRepaymentDetail ord = orderRepaymentDetails.get(0);
                                int paidAmount = new BigDecimal(Double.parseDouble(amount) * 100).intValue();
                                ord.setPaidAmount(paidAmount);
                                orderRepaymentService.repay(re, ord);
                                ord.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                                ord.setPayTime(new Date());
                                orderRepaymentDetailDAO.updateByPrimaryKeySelective(ord);
                                response.getWriter().write("success");
                                return;
                            }

                            orderRepaymentService.renewal(re, orderRenewal);

                        } else {
                            log.info("------------------------renewalCallbackCJ PAY_FAIL");
                            rep.setStatus(Constant.RENEWAL_STATUS_DEFEAT);
                        }
                        orderRenewalMapper.updateByPrimaryKeySelective(rep);
                    }

                } else {
                    log.error("-------------------------------jzfback error type mismatch outer_trade_no=" + outer_trade_no);
                    response.getWriter().write("ERROR");
                    return;
                }
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("repayCallbackCJ no={}, error={}", outer_trade_no, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    @RequestMapping(value = "jzfDfBack")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, timeout = 180)
    public void jzfDfBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("*******************************************************************");
        Map<String, String> req = getParameters(request);
        log.info("jzfDfBack 回调--------json={}", req);
        String status = req.get("status");
        String outer_trade_no = req.get("out_trade_no");
        String lockKey = "JZF_DF_BACK_" + outer_trade_no;
        String u_remark = req.get("u_remark");
        String orderId = req.get("trade_no");
        String amount = req.get("amount");
        String sign = req.get("sign");
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(outer_trade_no)) {
            log.error("-------------------------------jzfDfBack error verify false,sign=null || outer_trade_no=null");
            response.getWriter().write("ERROR");
            return;
        }

        try {

            Boolean repeat = redisUtil.setIfAbsent(lockKey, "1", 2, TimeUnit.SECONDS);
            if (!repeat) {
                log.info("极支付并发重复的回调,拦截");
                response.getWriter().write("REPEAT");
            }
            if (outer_trade_no.startsWith("J")) {
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outer_trade_no);
                queryParam.put("payStatus", Constant.BORROW_PAY_STATUS_SUCCESS);
                List<OrderBorrow> bos = orderBorrowMapper.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(bos)) {
                    log.info("--------------------------jzfDfBack error CollectionUtils.isEmpty(bos),no={}", outer_trade_no);
                    response.getWriter().write("success");
                    return;
                }
                OrderBorrow bo = bos.get(0);
                // 他们有随机金额，只能拿我们自己系统订单号和他们订单号做校验
//                BigDecimal amount = new BigDecimal(bo.getIntoMoney() / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                String md55 = DigestUtils.md5Hex(key.toLowerCase() + DigestUtils.md5Hex(amount + bo.getOutTradeNo()));
                if (!md55.equals(sign)) {       //判断MD5加密串是否正确（利用商户密匙）
                    log.info("-------------------------------jzfDfBack error verify false,sign={},md55={}", sign, md55);
                    response.getWriter().write("ERROR");
                    return;
                }
                if (bo != null && (bo.getStatus() == Constant.BORROW_STATUS_FKZ || bo.getStatus() == Constant.BORROW_STATUS_FKSB || bo.getStatus() == Constant.BORROW_STATUS_QX)) {
                    log.info("--------------------------jzfDfBack error bo != null && bo.getStatus() == Constant.BORROW_STATUS_FKZ,no=" + outer_trade_no);
                    OrderBorrow borrowOrderNew = new OrderBorrow();
                    borrowOrderNew.setId(bo.getId());
                    borrowOrderNew.setUpdateTime(new Date());
                    borrowOrderNew.setFlowNo(orderId);
                    UserInfo user = userInfoMapper.selectByPrimaryKey(bo.getUserId());
                    double moneyAmount = bo.getApplyAmount() / 100.0;
                    BigDecimal moneyAmountBig = new BigDecimal(moneyAmount);
                    if ("4".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------jzfDfBack WITHDRAWAL_SUCCESS,no=" + outer_trade_no);
                        borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_SUCCESS);
                        Date d = new Date();
                        borrowOrderNew.setLoanTime(d);
                        borrowOrderNew.setStatus(Constant.BORROW_STATUS_HKZ);
                        borrowOrderNew.setLoanEndTime(DateUtil.addDay(d, bo.getLoanTerm() - 1));// 放款时间加上借款期限
                        // 放款成功插入还款记录
                        bo.setPayStatus(Constant.BORROW_PAY_STATUS_SUCCESS);
                        bo.setStatus(Constant.REPAYMENT_STATUS_INIT);
                        bo.setLoanTime(d);
                        bo.setLoanEndTime(borrowOrderNew.getLoanEndTime());// 放款时间加上借款期限

                        boolean success = orderRepaymentService.insertByBorrorOrder2(bo);
                        if (!success) {
                            log.info("-------------------------------jzfDfBack error insert outer_trade_no=" + outer_trade_no);
                            response.getWriter().write("ERROR");
                            return;
                        }
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);

                        Byte customerType = bo.getCustomerType();
                        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                            @Override
                            public void afterCommit() {
                                /**异步统计放款成功*/
                                channelAsyncCountService.loanIsSuccCount(user, d, customerType);
                                // 放款成功时将订单信息同步到数据中心
                                //channelAsyncCountService.pushInformation(user, bo);
                            }
                        });
                        // 发送通知短信
                        // String smsContent = "尊敬的" + user.getRealName() + "，您申请的" + moneyAmount+ "元,扣除相关费用到帐"+amt+"元，请注意查收。";
                        //sendMsg(user, moneyAmountBig);
                        //非贷超签约
                        //userAgreeService.getSign(bo, 0);
                        //userAgreeService.getSign(bo, 1);
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    } else if ("3".equals(status)) {
                        log.info("---------------------payWithdrawCallback 1,no=" + outer_trade_no);
                        borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_PART_SUCCESS);
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    } else if ("0".equals(status)) {
                        // 商户更新订单为失败，处理自己的业务逻辑
                        borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
                        log.info("---------------------jzfDfBack WITHDRAWAL_FAIL,no=" + outer_trade_no);
                        borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
                        borrowOrderNew.setPayRemark(u_remark);
                        redisUtil.del(PREFIX + WITHDRAW_KEYS + "_" + bo.getId());
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    }


                }
            } else {
                log.error("-------------------------------jzfDfBack error type mismatch outer_trade_no=" + outer_trade_no);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("jzfDfBack no={}, error={}", outer_trade_no, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }
}
