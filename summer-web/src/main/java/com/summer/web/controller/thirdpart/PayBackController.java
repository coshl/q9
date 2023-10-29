package com.summer.web.controller.thirdpart;


import com.summer.api.service.IOrderRepaymentService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.service.impl.BackConfigParamsService;
import com.summer.service.mq.OrderProducer;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import com.summer.util.RedisUtil;
import com.summer.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping(value = "v1.0/api/payBack")
public class PayBackController extends BaseController {
    private static String WITHHOLD_KEYS = "REPAYMENT_REPAY_WITHHOLD";
    private static String RENEWAL_KEYS = "RENEWAL_WITHHOLD";
    private static String WITHDRAW_KEYS = "LOAN_WITHDRAW";
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IOrderRepaymentService orderRepaymentService;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDAO;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private BackConfigParamsService backConfigParamsService;
    @Resource
    private OrderProducer orderProducer;
    @Value("${system.prefix}")
    private String PREFIX;

    /**
     * 续期金额不足，创建部分还款订单
     *
     * @param type    1 还款
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

    @GetMapping(value = "btzfDfOutBack")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,timeout = 180)
    public void btzfDfOutBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("*******************************************************************");
        Map<String, String> req = getParameters(request);
        log.info("btzfDfOutBack 回调--------json={}", req);
        String status = req.get("status"); //0 为失败 1 为成功
        log.info("btzfDfOutBack 回调--------status={}", status);
        String outTradeNo = req.get("out_trade_no");
        String lockKey = "BTZF_DF_BACK_" + outTradeNo;
        String data = req.get("data");
        String transactionId = req.get("transaction_id");
        //String amount = req.get("amount");
        String sign = req.get("sign");
        try {
            Boolean repeat = redisUtil.setIfAbsent(lockKey, "1", 2, TimeUnit.SECONDS);
            if (!repeat) {
                log.info("bite支付并发重复的回调,拦截");
                response.getWriter().write("REPEAT");
            }
            if (outTradeNo.startsWith("B")) {
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.BORROW_PAY_STATUS_SUCCESS);
                List<OrderBorrow> bos = orderBorrowMapper.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(bos)) {
                    log.info("--------------------------btzfDfOutBack error CollectionUtils.isEmpty(bos),outTradeNo={}", outTradeNo);
                    response.getWriter().write("success");
                    return;
                }
                OrderBorrow bo = bos.get(0);
                if (bo != null && (bo.getStatus() == Constant.BORROW_STATUS_FKZ || bo.getStatus() == Constant.BORROW_STATUS_FKSB || bo.getStatus() == Constant.BORROW_STATUS_QX)) {
                    log.info("--------------------------btzfDfOutBack error bo != null && bo.getStatus() == Constant.BORROW_STATUS_FKZ,outTradeNo=" + outTradeNo);
                    OrderBorrow borrowOrderNew = new OrderBorrow();
                    borrowOrderNew.setId(bo.getId());
                    borrowOrderNew.setUpdateTime(new Date());
                    borrowOrderNew.setFlowNo(transactionId);
                    UserInfo user = userInfoMapper.selectByPrimaryKey(bo.getUserId());
                    double moneyAmount = bo.getApplyAmount() / 100.0;
                    BigDecimal moneyAmountBig = new BigDecimal(moneyAmount);
                    if ("1".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------btzfDfOutBack WITHDRAWAL_SUCCESS,outTradeNo=" + outTradeNo);
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
                            log.info("-------------------------------btzfDfOutBack error insert outer_trade_no=" + outTradeNo);
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
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    } else if ("0".equals(status)) {
                        // 商户更新订单为失败，处理自己的业务逻辑
                        borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
                        log.info("---------------------btzfDfOutBack WITHDRAWAL_FAIL,outTradeNo=" + outTradeNo);
                        borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
                        borrowOrderNew.setPayRemark(data);
                        redisUtil.del(PREFIX + WITHDRAW_KEYS + "_" + bo.getId());
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    }
                }
            } else {
                log.error("-------------------------------btzfDfOutBack error type mismatch outer_trade_no=" + outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("jzfDfBack no={}, error={}", outTradeNo, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    @GetMapping(value = "btzfInDsback")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void btzfInDsback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("*******************************************************************");
        Map<String, String> req = getParameters(request);
        log.info("btzfInDsback 回调--------json={}", req);
        String status = req.get("status"); //0 为失败 1 为成功
        String outTradeNo = req.get("out_trade_no");
        String orderId = req.get("transaction_id");
        String oldmoney = req.get("oldmoney");
        String amount = req.get("money");
        String lockKey = "btzfInDsback" + outTradeNo;
        try {
            Boolean lockResult = redisUtil.setIfAbsent(lockKey, "0", 2, TimeUnit.SECONDS);
            if (!lockResult) {
                log.info("未处理完的回调:{}", outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            if (outTradeNo.startsWith("BP")) {
                //还款
                // get borroworder
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
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
                    if ("1".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------repayCallbackCJ WITHDRAWAL_SUCCESS");
                        rep.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                        rep.setPayTime(new Date());
                        if(outTradeNo.startsWith("BPT")){
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
                        if(!outTradeNo.startsWith("BPT")){
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
                    if(outTradeNo.startsWith("BPT")){
                        rep.setRemark("用户APP退款");
                    }
                    orderRepaymentDetailDAO.updateByPrimaryKeySelective(rep);
                    //老客第一次还款强制下单
                    try {
                        orderProducer.oldAuto(re.getUserId());
                    }catch (Exception e)
                    {
                        log.info("老客第一次还款强制下单异常:{},",e.getMessage());
                    }
                }
            } else if (outTradeNo.startsWith("BR")) {
                //续期
                // get borroworder
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
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
                    if ("1".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("------------------------------renewalCallbackCJ WITHDRAWAL_SUCCESS");
                        if(new BigDecimal(amount).multiply(new BigDecimal(100)).subtract(new BigDecimal(orderRenewal.getRenewalFee())).compareTo(new BigDecimal(0)) == -1 )//实际支付金额小于续期费用
                        {
                            log.info("用户续期支付金额不足续期费用，把实际支付金额按部分还款处理");
                            //创建部分还款订单
                            doAfterPaymentWork(1, re, 0, "", "", "", "", orderRenewal.getOutTradeNo(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                            queryParam.put("out_trade_no", orderRenewal.getOutTradeNo());
                            queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                            List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                            OrderRepaymentDetail  ord = orderRepaymentDetails.get(0);
                            int paidAmount = new BigDecimal(Double.parseDouble(amount) * 100).intValue();
                            ord.setPaidAmount(paidAmount);
                            orderRepaymentService.repay(re,ord);
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
                log.error("-------------------------------btzfInDsback error type mismatch outer_trade_no=" + outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("repayCallbackCJ no={}, error={}", outTradeNo, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    @PostMapping(value = "aczfPaymentBack")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,timeout = 180)
    public void aczfPaymentBack(HttpServletRequest request, HttpServletResponse response,String fee,String orderno,String status,String outorder) throws IOException {
        log.info("*******************************************************************");
        //Map<String, String> req = getParameters(request);
        log.info("aczfPaymentBack 回调--------orderno={}", orderno);
        log.info("aczfPaymentBack 回调--------status={}", status);
        String outTradeNo = orderno;
        String lockKey = "ACZF_DF_BACK_" + outTradeNo;
        String transactionId = outorder;
        try {
            Boolean repeat = redisUtil.setIfAbsent(lockKey, "1", 2, TimeUnit.SECONDS);
            if (!repeat) {
                log.info("aochuang支付并发重复的回调,拦截");
                response.getWriter().write("REPEAT");
            }
            if (outTradeNo.startsWith("A")) {
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.BORROW_PAY_STATUS_SUCCESS);
                List<OrderBorrow> bos = orderBorrowMapper.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(bos)) {
                    log.info("--------------------------aczfPaymentBack error CollectionUtils.isEmpty(bos),outTradeNo={}", outTradeNo);
                    response.getWriter().write("success");
                    return;
                }
                OrderBorrow bo = bos.get(0);
                if (bo != null && (bo.getStatus() == Constant.BORROW_STATUS_FKZ || bo.getStatus() == Constant.BORROW_STATUS_FKSB || bo.getStatus() == Constant.BORROW_STATUS_QX)) {
                    log.info("--------------------------aczfPaymentBack error bo != null && bo.getStatus() == Constant.BORROW_STATUS_FKZ,outTradeNo=" + outTradeNo);
                    OrderBorrow borrowOrderNew = new OrderBorrow();
                    borrowOrderNew.setId(bo.getId());
                    borrowOrderNew.setUpdateTime(new Date());
                    borrowOrderNew.setFlowNo(transactionId);
                    UserInfo user = userInfoMapper.selectByPrimaryKey(bo.getUserId());
                    double moneyAmount = bo.getApplyAmount() / 100.0;
                    BigDecimal moneyAmountBig = new BigDecimal(moneyAmount);
                    if ("1".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------aczfPaymentBack WITHDRAWAL_SUCCESS,outTradeNo=" + outTradeNo);
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
                            log.info("-------------------------------aczfPaymentBack error insert outer_trade_no=" + outTradeNo);
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
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    } else if ("0".equals(status)) {
                        // 商户更新订单为失败，处理自己的业务逻辑
                        borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
                        log.info("---------------------aczfPaymentBack WITHDRAWAL_FAIL,outTradeNo=" + outTradeNo);
                        borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
                        borrowOrderNew.setPayRemark("代付手续费："+fee);
                        redisUtil.del(PREFIX + WITHDRAW_KEYS + "_" + bo.getId());
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    }
                }
            } else {
                log.error("-------------------------------aczfPaymentBack error type mismatch outer_trade_no=" + outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("aczfPaymentBack no={}, error={}", outTradeNo, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    @PostMapping(value = "aczfRepayBack")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void aczfRepayBack(HttpServletRequest request, HttpServletResponse response,String realamount,String orderno,String outorder,String status) throws IOException {
        log.info("*******************************************************************");
        //Map<String, String> req = getParameters(request);
        log.info("aczfRepayBack 回调--------orderno={}", orderno);
        String outTradeNo = orderno;
        String orderId = outorder;
        String amount = realamount;
        String lockKey = "aczfInDsback" + outTradeNo;
        try {
            Boolean lockResult = redisUtil.setIfAbsent(lockKey, "0", 2, TimeUnit.SECONDS);
            if (!lockResult) {
                log.info("未处理完的回调:{}", outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            if (outTradeNo.startsWith("AP")) {
                //还款
                // get borroworder
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(orderRepaymentDetails)) {
                    log.info("------------------------aczfRepayBack error CollectionUtils.isEmpty(orderRepaymentDetails)");
                    response.getWriter().write("success");
                    return;
                }
                OrderRepaymentDetail orderRepaymentDetail = orderRepaymentDetails.get(0);
                if (orderRepaymentDetail != null) {
                    log.info("------------------------aczfRepayBack orderRepaymentDetail != null");
                    OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(orderRepaymentDetail.getRepaymentId());
                    Integer repayId = re.getId();
                    OrderRepaymentDetail rep = new OrderRepaymentDetail();
                    rep.setId(orderRepaymentDetail.getId());
                    rep.setBorrowId(orderRepaymentDetail.getBorrowId());
                    int paidAmount = new BigDecimal(Double.parseDouble(amount) * 100).intValue();
                    orderRepaymentDetail.setPaidAmount(paidAmount);
                    rep.setPaidAmount(paidAmount);
                    rep.setThirdOrderNo(orderId);
                    if ("1".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------aczfRepayBack WITHDRAWAL_SUCCESS");
                        rep.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                        rep.setPayTime(new Date());
                        if(outTradeNo.startsWith("APT")){
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
                        if(!outTradeNo.startsWith("APT")){
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
                    if(outTradeNo.startsWith("APT")){
                        rep.setRemark("用户APP退款");
                    }
                    orderRepaymentDetailDAO.updateByPrimaryKeySelective(rep);
                    //老客第一次还款强制下单
                    try {
                        orderProducer.oldAuto(re.getUserId());
                    }catch (Exception e)
                    {
                        log.info("老客第一次还款强制下单异常:{},",e.getMessage());
                    }
                }
            } else if (outTradeNo.startsWith("AR")) {
                //续期
                // get borroworder
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.RENEWAL_STATUS_PAID);
                List<OrderRenewal> orderRenewals = orderRenewalMapper.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(orderRenewals)) {
                    log.info("------------------------aczfRepayBack error CollectionUtils.isEmpty(orderRenewals)");
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
                    if ("1".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("------------------------------aczfRepayBack WITHDRAWAL_SUCCESS");
                        if(new BigDecimal(amount).multiply(new BigDecimal(100)).subtract(new BigDecimal(orderRenewal.getRenewalFee())).compareTo(new BigDecimal(0)) == -1 )//实际支付金额小于续期费用
                        {
                            log.info("用户续期支付金额不足续期费用，把实际支付金额按部分还款处理");
                            //创建部分还款订单
                            doAfterPaymentWork(1, re, 0, "", "", "", "", orderRenewal.getOutTradeNo(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                            queryParam.put("out_trade_no", orderRenewal.getOutTradeNo());
                            queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                            List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                            OrderRepaymentDetail  ord = orderRepaymentDetails.get(0);
                            int paidAmount = new BigDecimal(Double.parseDouble(amount) * 100).intValue();
                            ord.setPaidAmount(paidAmount);
                            orderRepaymentService.repay(re,ord);
                            ord.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                            ord.setPayTime(new Date());
                            orderRepaymentDetailDAO.updateByPrimaryKeySelective(ord);
                            response.getWriter().write("success");
                            return;
                        }
                        orderRepaymentService.renewal(re, orderRenewal);
                    } else {
                        log.info("------------------------aczfRepayBack PAY_FAIL");
                        rep.setStatus(Constant.RENEWAL_STATUS_DEFEAT);
                    }
                    orderRenewalMapper.updateByPrimaryKeySelective(rep);
                }
            } else {
                log.error("-------------------------------aczfRepayBack error type mismatch outer_trade_no=" + outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("aczfRepayBack no={}, error={}", outTradeNo, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    // {
    //    "appId": "60cc09bce4b0f1c0b83761c9",
    //    "channelOrderNo": "2021061822001423031419593035",
    //    "createdAt": 1623985552769,
    //    "currency": "cny",
    //    "extParam": "",
    //    "mchNo": "M1623984572",
    //    "mchRefundNo": "mho1623985552430",
    //    "refundAmount": 4,
    //    "refundOrderId": "P202106181105527690009",
    //    "state": 2,
    //    "successTime": 1623985554000
    //}
    @GetMapping(value = "myzfPaymentBack")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,timeout = 180)
    public void tdzfPaymentBack(HttpServletRequest request, HttpServletResponse response,int refundAmount,String refundOrderId,int state,String mchRefundNo) throws IOException {
        log.info("*******************************************************************");
        //Map<String, String> req = getParameters(request);
        Map<String, String> req = getParameters(request);
        log.info("玛雅PaymentBack 回调--------json={}", req);
        String status = req.get("state"); //代付状态 0-订单生成 1-代付中 2-代付成功 3-代付失败 4-代付关闭
        log.info("玛雅PaymentBack 回调--------code={}", state);
        String outTradeNo = req.get("mchRefundNo");
        log.info("玛雅PaymentBack 回调--------outTradeNo={}", mchRefundNo);
        String orderNo = req.get("refundOrderId");
        log.info("玛雅PaymentBack 回调--------refundOrderId={}", refundOrderId);
        String lockKey = "MYZF_DF_BACK_" + outTradeNo;
        //String transactionId = outorder;
        try {
            Boolean repeat = redisUtil.setIfAbsent(lockKey, "1", 2, TimeUnit.SECONDS);
            if (!repeat) {
                log.info("玛雅支付并发重复的回调,拦截");
                response.getWriter().write("REPEAT");
            }
            if (outTradeNo.startsWith("MY")) {
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.BORROW_PAY_STATUS_SUCCESS);
                List<OrderBorrow> bos = orderBorrowMapper.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(bos)) {
                    log.info("--------------------------myzfPaymentBack error CollectionUtils.isEmpty(bos),outTradeNo={}", outTradeNo);
                    response.getWriter().write("success");
                    return;
                }
                OrderBorrow bo = bos.get(0);
                if (bo != null && (bo.getStatus() == Constant.BORROW_STATUS_FKZ || bo.getStatus() == Constant.BORROW_STATUS_FKSB || bo.getStatus() == Constant.BORROW_STATUS_QX)) {
                    log.info("--------------------------myzfPaymentBack error bo != null && bo.getStatus() == Constant.BORROW_STATUS_FKZ,outTradeNo=" + outTradeNo);
                    OrderBorrow borrowOrderNew = new OrderBorrow();
                    borrowOrderNew.setId(bo.getId());
                    borrowOrderNew.setUpdateTime(new Date());
                    borrowOrderNew.setFlowNo(orderNo);
                    UserInfo user = userInfoMapper.selectByPrimaryKey(bo.getUserId());
                    if ("2".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------myzfPaymentBack WITHDRAWAL_SUCCESS,outTradeNo=" + outTradeNo);
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
                            log.info("-------------------------------myzfPaymentBack error insert outer_trade_no=" + outTradeNo);
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
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    } else if (Integer.valueOf(status) > 2) {
                        // 商户更新订单为失败，处理自己的业务逻辑
                        borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
                        log.info("---------------------myzfPaymentBack WITHDRAWAL_FAIL,outTradeNo=" + outTradeNo);
                        borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
                        borrowOrderNew.setPayRemark("代付失败");
                        redisUtil.del(PREFIX + WITHDRAW_KEYS + "_" + bo.getId());
                        //无论返回的订单状态是否成功，都要修改借款表
                        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
                    }
                }
            } else {
                log.error("-------------------------------myzfPaymentBack error type mismatch outer_trade_no=" + outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("myzfPaymentBack no={}, error={}", outTradeNo, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }

    // {
    //     "amount": 5,
    //     "body": "商品描述",
    //     "clientIp": "192.166.1.132",
    //     "createdAt": "1622016572190",
    //     "currency": "cny",
    //     "extParam": "",
    //     "ifCode": "wxpay",
    //     "mchNo": "M1621873433953",
    //     "appId": "60cc09bce4b0f1c0b83761c9",
    //     "mchOrderNo": "mho1621934803068",
    //     "payOrderId": "20210525172643357010",
    //     "state": 3,
    //     "subject": "商品标题",
    //     "wayCode": "WX\_BAR",
    //     "sign": "C380BEC2BFD727A4B6845133519F3AD6"
    //  }
    @GetMapping(value = "myzfRepayBack")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void tdzfRepayBack(HttpServletRequest request, HttpServletResponse response,int amount,String payOrderId,String mchOrderNo,String state) throws IOException {
        log.info("*******************************************************************");
        Map<String, String> req = getParameters(request);
        log.info("玛雅RepayBack 回调--------json={}", req);
        String status = req.get("state"); //支付订单状态 0-订单生成 1-支付中 2-支付成功 3-支付失败 4-已撤销 5-已退款 6-订单关闭
        log.info("玛雅RepayBack 回调--------code={}", state);
        String outTradeNo = req.get("mchOrderNo");
        log.info("玛雅RepayBack 回调--------out_order_no={}", mchOrderNo);
        String orderNo = req.get("payOrderId");
        log.info("玛雅RepayBack 回调--------order_no={}", payOrderId);
        String amountReq = req.get("amount");
        log.info("玛雅RepayBack 回调--------amount={}", amount);

        String lockKey = "myzfInDsback" + outTradeNo;
        try {
            Boolean lockResult = redisUtil.setIfAbsent(lockKey, "0", 2, TimeUnit.SECONDS);
            if (!lockResult) {
                log.info("未处理完的回调:{}", outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            if (outTradeNo.startsWith("MYP")) {
                //还款
                // get borroworder
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(orderRepaymentDetails)) {
                    log.info("------------------------myzfRepayBack error CollectionUtils.isEmpty(orderRepaymentDetails)");
                    response.getWriter().write("success");
                    return;
                }
                OrderRepaymentDetail orderRepaymentDetail = orderRepaymentDetails.get(0);
                if (orderRepaymentDetail != null) {
                    log.info("------------------------myzfRepayBack orderRepaymentDetail != null");
                    OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(orderRepaymentDetail.getRepaymentId());
                    Integer repayId = re.getId();
                    OrderRepaymentDetail rep = new OrderRepaymentDetail();
                    rep.setId(orderRepaymentDetail.getId());
                    rep.setBorrowId(orderRepaymentDetail.getBorrowId());
                    //int paidAmount = new BigDecimal(Double.parseDouble(amount) * 100).intValue();
                    int paidAmount = new BigDecimal(amountReq).intValue();
                    orderRepaymentDetail.setPaidAmount(paidAmount);
                    rep.setPaidAmount(paidAmount);
                    rep.setThirdOrderNo(orderNo);
                    if ("2".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("---------------------myzfRepayBack WITHDRAWAL_SUCCESS");
                        rep.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                        rep.setPayTime(new Date());
                        if(outTradeNo.startsWith("MYPT")){
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
                        if(!outTradeNo.startsWith("MYPT")){
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
                    if(outTradeNo.startsWith("MYPT")){
                        rep.setRemark("用户APP还款支付失败");
                    }
                    orderRepaymentDetailDAO.updateByPrimaryKeySelective(rep);
                    //老客第一次还款强制下单
                    try {
                        orderProducer.oldAuto(re.getUserId());
                    }catch (Exception e)
                    {
                        log.info("老客第一次还款强制下单异常:{},",e.getMessage());
                    }
                }
            } else if (outTradeNo.startsWith("MYR")) {
                //续期
                // get borroworder
                Map<String, Object> queryParam = new HashMap<>();
                queryParam.put("out_trade_no", outTradeNo);
                queryParam.put("payStatus", Constant.RENEWAL_STATUS_PAID);
                List<OrderRenewal> orderRenewals = orderRenewalMapper.selectSimple(queryParam);
                //订单处于放款中或放款失败状态且非放款成功状态
                if (CollectionUtils.isEmpty(orderRenewals)) {
                    log.info("------------------------myzfRepayBack error CollectionUtils.isEmpty(orderRenewals)");
                    response.getWriter().write("success");
                    return;
                }
                OrderRenewal orderRenewal = orderRenewals.get(0);
                if (orderRenewal != null) {
                    OrderRepayment re = orderRepaymentMapper.selectByPrimaryKey(orderRenewal.getRepaymentId());
                    OrderRenewal rep = new OrderRenewal();
                    rep.setId(orderRenewal.getId());
                    rep.setBorrowId(orderRenewal.getBorrowId());
                    rep.setThird(orderNo);
                    if ("2".equals(status)) {
                        // 商户更新订单为成功，处理自己的业务逻辑
                        // 流水号
                        log.info("------------------------------myzfRepayBack WITHDRAWAL_SUCCESS");
                        if(new BigDecimal(amountReq).subtract(new BigDecimal(orderRenewal.getRenewalFee())).compareTo(new BigDecimal(0)) == -1 )//实际支付金额小于续期费用
                        {
                            log.info("用户续期支付金额不足续期费用，把实际支付金额按部分还款处理");
                            //创建部分还款订单
                            doAfterPaymentWork(1, re, 0, "", "", "", "", orderRenewal.getOutTradeNo(), Constant.PAY_TYPE_ONLINE_OSDT, "");
                            queryParam.put("out_trade_no", orderRenewal.getOutTradeNo());
                            queryParam.put("payStatus", Constant.REPAYMENTDETAIL_STATUS_PAID);
                            List<OrderRepaymentDetail> orderRepaymentDetails = orderRepaymentDetailDAO.selectSimple(queryParam);
                            OrderRepaymentDetail  ord = orderRepaymentDetails.get(0);
                            int paidAmount = new BigDecimal(amountReq).intValue();
                            ord.setPaidAmount(paidAmount);
                            orderRepaymentService.repay(re,ord);
                            ord.setStatus(Constant.REPAYMENTDETAIL_STATUS_PAID);
                            ord.setPayTime(new Date());
                            orderRepaymentDetailDAO.updateByPrimaryKeySelective(ord);
                            response.getWriter().write("success");
                            return;
                        }
                        orderRepaymentService.renewal(re, orderRenewal);
                    } else {
                        log.info("------------------------myzfRepayBack PAY_FAIL");
                        rep.setStatus(Constant.RENEWAL_STATUS_DEFEAT);
                    }
                    orderRenewalMapper.updateByPrimaryKeySelective(rep);
                }
            } else {
                log.error("-------------------------------myzfRepayBack error type mismatch outer_trade_no=" + outTradeNo);
                response.getWriter().write("ERROR");
                return;
            }
            response.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("myzfRepayBack no={}, error={}", outTradeNo, e);
            response.getWriter().write("ERROR");
        } finally {
            redisUtil.del(lockKey);
        }
    }
}
