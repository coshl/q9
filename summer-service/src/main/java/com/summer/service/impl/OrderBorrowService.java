package com.summer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.summer.api.service.IOrderBorrowService;
import com.summer.api.service.IOrderRepaymentService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.pojo.dto.OrderPushRiskDTO;
import com.summer.pojo.vo.BorrowUserVO;
import com.summer.pojo.vo.OrderBorrowVo;
import com.summer.queue.QueueConstans;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.impl.black.JuGuangEnum;
import com.summer.service.pay.aochPay.AochPayAPI;
import com.summer.service.pay.bitePay.BitePayAPI;
import com.summer.service.pay.bitePay.dto.OutAccountParamDTO;
import com.summer.service.pay.bitePay.dto.OutAccountResultDTO;
import com.summer.service.pay.mayaPay.MaYaPayApi;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import com.summer.util.changjiepay.ChanPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
@Slf4j
@Service
public class OrderBorrowService implements IOrderBorrowService {
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    @Lazy
    private IOrderRepaymentService orderRepaymentService;
    @Autowired
    private ISmsService smsService;
    @Resource
    private IUserCardInfoDao userCardInfoDao;
    @Resource
    private IRiskCreditUserDao riskCreditUserDao;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private RedisUtil redisUtil;
    private static String WITHDRAW_KEYS = "LOAN_WITHDRAW";
    private static String RELOAN = "RELOAN";
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Value("${jzf.account_id}")
    private String account_id;
    @Value("${self.url}")
    private String selfUrl;
    @Value("${jzf.jzfPayUrl}")
    private String jzfPayUrl;
    @Value("${jzf.key}")
    private String key;
    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Autowired
    private JuGuangApi juGuangApi;
    @Value(value = "${spring.profiles.active}")
    private String env;
    @Autowired
    private BitePayAPI bitePayAPI;
    @Autowired
    private AochPayAPI aochPayAPI;
    @Autowired
    private MaYaPayApi maYaPayApi;
    @Value("${myzf.mchNo}")
    private String myzfMchNo;
    @Value("${myzf.appId}")
    private String myzfAppId;
    @Value("${myzf.apiKey}")
    private String myzfApiKey;

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


    @Override
    public int updateSelective(OrderBorrow orderBorrow) {
        return orderBorrowMapper.updateByPrimaryKeySelective(orderBorrow);
    }

    @Override
    @Transactional
    public int cancelLoan(String[] ids) {
        int i = 0;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("ids", ids);
            i = orderBorrowMapper.cancelLoan(map);
            infoIndexInfoDao.updateBorrow(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }


    @Override
    public PageInfo<BorrowUserVO> queryWithUser(Map<String, Object> params) {
        List<BorrowUserVO> list = orderBorrowMapper.findParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public int insertSelective(OrderBorrow record) {
        // log.error("订单JSON数据======order={}",JSONObject.toJSONString(record));
        orderBorrowMapper.insertSelective(record);
        return record.getId();
    }


    @Override
    public OrderBorrow selectByPrimaryKey(Integer id) {
        return orderBorrowMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderBorrow> selectByParams(Map<String, Object> param) {
        return orderBorrowMapper.selectByParams(param);
    }

    /**
     * 审核通过后放款队列
     */
    @Override
    @JmsListener(destination = QueueConstans.loanQueues)
    public void autoLoan(String borrowIdStr) {
        log.info("开始执行放款,bid=" + borrowIdStr);
        OrderBorrow bo = orderBorrowMapper.selectByPrimaryKey(Integer.parseInt(borrowIdStr));
        Boolean result = sendLoanFilter(bo);
        try {
            if (result) {
                // 放款通知上传
                UserInfo userInfo = userInfoMapper.selectByUserId(bo.getUserId());
                pushBorrowOrder(userInfo);
            }
        } catch (Exception e) {
            log.error("共债或风控放款通知异常:{}", e);
        }
    }

    @Async
    public void pushBorrowOrder(UserInfo userInfo) {
        log.info("放款成功时将订单信息同步到数据中心返回参数------res={}", "res");
        // 放款通知上传
        List<Map<String, String>> userList = new ArrayList<>();
        Map map = new HashMap();
        map.put("name",userInfo.getRealName());
        map.put("phone",userInfo.getPhone());
        map.put("idNo",userInfo.getIdCard());
        userList.add(map);
        juGuangApi.uploadJointDebt(userList, JuGuangEnum.LOAN.getValue(),0);
    }

    private Boolean sendLoanFilter(OrderBorrow bo) {
        if (bo == null || Constant.BORROW_STATUS_FKZ != bo.getStatus() || bo.getHitRiskTimes() == 1 || bo.getPayStatus() == Constant.BORROW_PAY_STATUS_PART_SUCCESS) {
            return Boolean.FALSE;
        }
        //放款金额超限 如果配置为空，就默认1000000 （单位分）
        /*Integer loanAmountLimit = 1000000;
        BackConfigParamsVo loanLimitEnd = backConfigParamsDao.findBySysKey("loan_amount_limit");
        if (null != loanLimitEnd) {
            loanAmountLimit = loanLimitEnd.getSysValue();
        }
        if (bo.getApplyAmount() > loanAmountLimit) {
            OrderBorrow orderBorrow = new OrderBorrow();
            orderBorrow.setId(bo.getId());
            orderBorrow.setStatus(Byte.parseByte("7"));
            orderBorrow.setUpdateTime(new Date());
            BigDecimal money = new BigDecimal(loanAmountLimit / 100.0);
            orderBorrow.setLoanReviewRemark("放款失败，放款金额超限,当前最高可放金额为：" + money.setScale(2, BigDecimal.ROUND_HALF_UP) + "元");
            orderBorrowMapper.updateByPrimaryKeySelective(orderBorrow);
            return Boolean.FALSE;
        }*/
        Integer boId = bo.getId();
        try {
            Integer intoMoney = bo.getIntoMoney();
            Integer userId = bo.getUserId();
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("id", bo.getId());
            List<OrderBorrow> orderBorrows = orderBorrowMapper.checkLoan(map);

            if (CollectionUtils.isNotEmpty(orderBorrows)) {
                //取消放款的订单有在借订单
                log.info("OrderBorrowService  该用户有新在借订单,userId=" + userId);
                loanFail(boId, "该用户有新在借订单");
                return Boolean.FALSE;
            }
            // 用户银行卡信息
            UserCardInfo info = userCardInfoDao.selectUserBankCard(userId);
            if (null == info) {
                log.info("OrderBorrowService  UserCardInfo=null,userId=" + userId);
                loanFail(boId, "银行卡解绑");
                return Boolean.FALSE;
            }

            Map<String, Object> param = new HashMap();
            param.put("borrowId", boId);
            List<OrderRepayment> havRepayments = orderRepaymentMapper.selectSimple(param);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(havRepayments)) {
                log.error("已经存在还款单，不再放款。借款单id为：" + boId);
                bo.setPayRemark("已经存在还款单，不再放款");
                orderBorrowMapper.updateByPrimaryKeySelective(bo);
                return Boolean.FALSE;
            }
//            String redis_repay_id = redisUtil.get(PREFIX+WITHDRAW_KEYS + "_" + boId);
//
//            log.info("进入放款 查看锁定订单状态：no={},borrowId={},redis_repay_id={}",bo.getOutTradeNo(),boId,redis_repay_id);
//            if ("true".equals(redis_repay_id)) {
//                return;
//            }
//            redisUtil.set(PREFIX+WITHDRAW_KEYS+"_"+boId, "true", 1 * 60);
            String dateFormatMin = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm");
            String minkey = WITHDRAW_KEYS + dateFormatMin + boId;
            long minIn = redisUtil.incr(minkey, 1);
            int timeIn = 60;
            boolean expireIn = redisUtil.expire(minkey, timeIn);
            if (!expireIn) {
                boolean expire1 = redisUtil.expire(minkey, timeIn);
                if (!expire1) {
                    log.error("进入放款 expireIn error id=" + boId);
                }
            }
            if (minIn > 1) {
                log.error("进入放款 FAIL,id={},minIn={}", boId, minIn);
                return Boolean.FALSE;
            }
            log.info("进入放款 SUC,id={},minIn={}", boId, minIn);
            String usdt_df = backConfigParamsDao.findStrValue("usdt_df");
            if (StringUtils.isBlank(usdt_df)) {
                return Boolean.FALSE;
            }
            if ("0".equals(usdt_df)) {
                Random random = new Random();
                Integer res = random.nextInt(4) + 1;
                usdt_df = res.toString();
            }
            Boolean loanResult = Boolean.FALSE;
            if ("1".equals(usdt_df)) {
                loanByBTZF(bo, intoMoney, info);
            } else if ("2".equals(usdt_df)) {
                loanByACZF(bo, intoMoney, info);
            } else if ("3".equals(usdt_df)) {
                loanByMYZF(bo, intoMoney, info);
            } else if ("4".equals(usdt_df)) {
                loanResult = loanByJZF(bo, intoMoney, info);
            }
            return loanResult;
        } catch (Exception e) {
            log.error("放款程序出错......", e);
        }
        return Boolean.FALSE;
    }

    private void loanFail(Integer boId, String remark) {
        HashMap<String, Object> params2 = new HashMap<String, Object>();
        params2.put("remark", remark);

        params2.put("id", boId);
        params2.put("status", 7);
        params2.put("reviewId", 0);
        riskCreditUserDao.updateLoanFail(params2);//borrow表
        RiskCreditUser byBorrowId = riskCreditUserDao.findByBorrowId(boId);
        riskCreditUserDao.updateRiskStatus(new RiskCreditUser(byBorrowId.getId(), 12, remark));
    }



    private Boolean loanByJZF(OrderBorrow bo, Integer intoMoney, UserCardInfo info) throws IOException {
        log.info("OrderBorrowService loanByJZF start,id={},no={}", bo.getId(), bo.getOutTradeNo());
        String outTradeNo = bo.getOutTradeNo();
        long sq = System.currentTimeMillis();
        Integer userId = bo.getUserId();
        log.info("OrderBorrowService loanByJZF UserCardInfo=" + info.toString());
        String cardNo = info.getCardNo();
        String cardName = info.getOpenName();
        String bankName = info.getBankName();
        long sb = System.currentTimeMillis();
        if (bo.getOutTradeNo().startsWith("J")) {
            outTradeNo = bo.getOutTradeNo();
        } else {
            outTradeNo = "J" + ChanPayUtil.generateOutTradeNo();
        }
        OrderBorrow borrowOrderNew = new OrderBorrow();
        Integer id = bo.getId();
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        int i = orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        if (i < 1) {
            log.error("loanByJZF update bo  error i==0,ono={},=no={}", bo.getOutTradeNo(), outTradeNo);
            return false;
        }
        long sfk = System.currentTimeMillis();
        log.info("loanByJZF update bo  suc ,no={},ono={}", borrowOrderNew.getOutTradeNo(), bo.getOutTradeNo());
        BigDecimal amount = new BigDecimal(intoMoney / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        Map<String, Object> map = new HashMap<>();
        map.put("account_id", account_id);

        map.put("thoroughfare", "bank");
        map.put("out_trade_no", outTradeNo);
        map.put("amount", amount);

        String sign = DigestUtils.md5Hex(key.toLowerCase() + DigestUtils.md5Hex(amount.toString() + outTradeNo));
        map.put("sign", sign);
        map.put("callback_url", selfUrl + "jzfDfBack");
        map.put("real_name", cardName);
        map.put("bank_card", cardNo);
        map.put("bank_name", bankName);
        map.put("zbank_name", info.getBankAddress());
        map.put("phone", info.getPhone());
        log.info("loanByJZF osdt 代付 param {}", map.toString());
        String post = HttpUtil.doPost(jzfPayUrl + "agent-pay-create", map);

        log.info("loanByJZF osdt 代付 res {}", post);

        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        long sfk1 = System.currentTimeMillis();
        log.info("loanByJZF id={}fk={}", id, sfk1 - sfk);
        Boolean flag = Boolean.TRUE;
        if (StringUtils.isNotBlank(post) && "200".equals(JSON.parseObject(post).getString("code"))) {
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_WAIT);
            JSONObject data1 = JSON.parseObject(post).getJSONObject("data");
            String orderId = data1.getString("trade_no");
            if (StringUtils.isNotBlank(orderId)) {
                borrowOrderNew.setFlowNo(orderId);
            }
        } else {
            flag = Boolean.FALSE;
            if (StringUtils.isNotBlank(post)) {
                String resultMsg = JSON.parseObject(post).getString("msg");
                borrowOrderNew.setPayRemark(resultMsg);
            } else {
                borrowOrderNew.setPayRemark("请求失败,返回结果为空");
            }
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
            borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
        }
        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        return flag;
    }

    //币特支付
    private Boolean loanByBTZF(OrderBorrow bo, Integer intoMoney, UserCardInfo info) throws IOException {
        log.info("OrderBorrowService loanByBTZF start,id={},no={}", bo.getId(), bo.getOutTradeNo());
        String outTradeNo = bo.getOutTradeNo();
        long sq = System.currentTimeMillis();
        Integer userId = bo.getUserId();
        log.info("OrderBorrowService loanByBTZF UserCardInfo=" + info.toString());
        String cardNo = info.getCardNo();
        String cardName = info.getOpenName();
        String bankName = info.getBankName();
        long sb = System.currentTimeMillis();
        if (bo.getOutTradeNo().startsWith("B")) {
            outTradeNo = bo.getOutTradeNo();
        } else {
            outTradeNo = "B" + ChanPayUtil.generateOutTradeNo();
        }
        OrderBorrow borrowOrderNew = new OrderBorrow();
        Integer id = bo.getId();
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        int i = orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        if (i < 1) {
            log.error("loanByBTZF update bo  error i==0,ono={},=no={}", bo.getOutTradeNo(), outTradeNo);
            return false;
        }
        long sfk = System.currentTimeMillis();
        log.info("loanByBTZF update bo  suc ,no={},ono={}", borrowOrderNew.getOutTradeNo(), bo.getOutTradeNo());
        BigDecimal amount = new BigDecimal(intoMoney / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        OutAccountParamDTO outAccountParamDTO = new OutAccountParamDTO();
        outAccountParamDTO.setAppid(btzfAppId);
        outAccountParamDTO.setApikey(btzfAppKey);
        outAccountParamDTO.setNotifyUrl(backUrl + "btzfDfOutBack");
        outAccountParamDTO.setOutTradeNo(outTradeNo);
        outAccountParamDTO.setName(cardName);
        outAccountParamDTO.setPidnum("120112197808053325");
        outAccountParamDTO.setMoney(amount);
        outAccountParamDTO.setBankname(bankName);
        outAccountParamDTO.setBankid(cardNo);
        log.info("loanByBTZF 代付 param {}", outAccountParamDTO.toString());
        OutAccountResultDTO outAccountResultDTO = bitePayAPI.OutAccount(outAccountParamDTO);
        log.info("loanByBTZF 代付 res {}", outAccountResultDTO.toString());
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        long sfk1 = System.currentTimeMillis();
        log.info("loanByBTZF id={}fk={}", id, sfk1 - sfk);
        Boolean flag = Boolean.TRUE;
        if ("1".equals(outAccountResultDTO.getStatus())) {
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_WAIT);
            String orderId = outAccountResultDTO.getTransactionId();
            if (StringUtils.isNotBlank(orderId)) {
                borrowOrderNew.setFlowNo(orderId);
            }
        } else {
            flag = Boolean.FALSE;
            if (!Objects.isNull(outAccountResultDTO)) {
                String resultMsg = outAccountResultDTO.getMsg();
                borrowOrderNew.setPayRemark(resultMsg);
            } else {
                borrowOrderNew.setPayRemark("请求失败,返回结果为空");
            }
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
            borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
        }
        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        return flag;
    }


    //奥创支付
    private Boolean loanByACZF(OrderBorrow bo, Integer intoMoney, UserCardInfo info) throws IOException {
        log.info("OrderBorrowService loanByACZF start,id={},no={}", bo.getId(), bo.getOutTradeNo());
        String outTradeNo;
        log.info("OrderBorrowService loanByACZF UserCardInfo=" + info.toString());
        String cardNo = info.getCardNo();
        String cardName = info.getOpenName();
        String bankName = info.getBankName();
        if (bo.getOutTradeNo().startsWith("A")) {
            outTradeNo = bo.getOutTradeNo();
        } else {
            outTradeNo = "A" + ChanPayUtil.generateOutTradeNo();
        }
        OrderBorrow borrowOrderNew = new OrderBorrow();
        Integer id = bo.getId();
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        int i = orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        if (i < 1) {
            log.error("loanByACZF update bo  error i==0,ono={},=no={}", bo.getOutTradeNo(), outTradeNo);
            return false;
        }
        long sfk = System.currentTimeMillis();
        log.info("loanByACZF update bo  suc ,no={},ono={}", borrowOrderNew.getOutTradeNo(), bo.getOutTradeNo());
        BigDecimal amount = new BigDecimal(intoMoney / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        Map<String,String> aoch = new HashMap();
        aoch.put("userId",aczfUserId);
        aoch.put("apiKey",aczfApiKey);
        aoch.put("notifyUrl",aczfBackUrl+"aczfPaymentBack");
        aoch.put("orderNo",outTradeNo);
        aoch.put("amount", String.valueOf(amount));
        aoch.put("bankName",bankName);
        aoch.put("bankCode",cardNo);
        aoch.put("name",cardName);

        log.info("loanByACZF 代付 param {}", aoch);

        String paymentResult = aochPayAPI.aochPayment(aoch);

        log.info("loanByACZF 代付 res {}", paymentResult);
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        long sfk1 = System.currentTimeMillis();
        log.info("loanByACZF id={}fk={}", id, sfk1 - sfk);
        Boolean flag = Boolean.TRUE;
        JSONObject paymentResultJson = JSON.parseObject(paymentResult);
        if (paymentResultJson.getInteger("status") == 1) {
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_WAIT);
            String orderId = paymentResultJson.getString("orderno");
            if (StringUtils.isNotBlank(orderId)) {
                borrowOrderNew.setFlowNo(orderId);
            }
        } else {
            flag = Boolean.FALSE;
            if (StringUtils.isNotBlank(paymentResult)) {
                String resultMsg = paymentResultJson.getString("msg");
                borrowOrderNew.setPayRemark(resultMsg);
            } else {
                borrowOrderNew.setPayRemark("请求失败,返回结果为空");
            }
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
            borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
        }
        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        return flag;
    }


    //玛雅支付
    private Boolean loanByMYZF(OrderBorrow bo, Integer intoMoney, UserCardInfo info) throws IOException {
        log.info("OrderBorrowService loanByTDZF start,id={},no={}", bo.getId(), bo.getOutTradeNo());
        String outTradeNo;
        log.info("OrderBorrowService loanByTDZF UserCardInfo=" + info.toString());
        String cardNo = info.getCardNo();
        String cardName = info.getOpenName();
        String bankName = info.getBankName();
        if (bo.getOutTradeNo().startsWith("MY")) {
            outTradeNo = bo.getOutTradeNo();
        } else {
            outTradeNo = "MY" + ChanPayUtil.generateOutTradeNo();
        }
        OrderBorrow borrowOrderNew = new OrderBorrow();
        Integer id = bo.getId();
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        int i = orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        if (i < 1) {
            log.error("loanByMYZF update bo  error i==0,ono={},=no={}", bo.getOutTradeNo(), outTradeNo);
            return false;
        }
        long sfk = System.currentTimeMillis();
        log.info("loanByMYZF update bo  suc ,no={},ono={}", borrowOrderNew.getOutTradeNo(), bo.getOutTradeNo());
        log.info("loanByMYZF 代付 param {}", cardName+cardNo+bankName+outTradeNo);

        String paymentResult = maYaPayApi.maYaPayment(cardName,cardNo, String.valueOf(intoMoney),myzfMchNo,myzfAppId,aczfBackUrl+"myzfPaymentBack",outTradeNo,myzfApiKey);
        //{"code":0,"data":{"state":1},"msg":"SUCCESS","sign":"89AA85F5E761F2FC918D143E2A686EF8"}
        //state 代付状态 0-订单生成 1-代付中 2-代付成功 3-代付失败 4-代付关闭
        log.info("loanByMYZF 代付 res {}", paymentResult);
        borrowOrderNew.setId(id);
        borrowOrderNew.setOutTradeNo(outTradeNo);
        long sfk1 = System.currentTimeMillis();
        log.info("loanByMYZF id={}fk={}", id, sfk1 - sfk);
        Boolean flag = Boolean.TRUE;
        JSONObject paymentResultJson = JSON.parseObject(paymentResult);
        if (paymentResultJson.getInteger("code") == 0) {
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_WAIT);
            /*String orderId = paymentResultJson.getJSONObject("data").getString("order_no");
            if (StringUtils.isNotBlank(orderId)) {
                borrowOrderNew.setFlowNo(orderId);
            }*/
        } else {
            flag = Boolean.FALSE;
            if (StringUtils.isNotBlank(paymentResult)) {
                String resultMsg = paymentResultJson.getString("msg");
                borrowOrderNew.setPayRemark(resultMsg);
            } else {
                borrowOrderNew.setPayRemark("请求失败,返回结果为空");
            }
            borrowOrderNew.setPayStatus(Constant.BORROW_PAY_STATUS_INIT);
            borrowOrderNew.setStatus(Constant.BORROW_STATUS_FKSB);
        }
        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
        return flag;
    }


    @Override
    public OrderBorrow recentlyOrder(Integer userId) {
        return orderBorrowMapper.recentlyOrder(userId);
    }

    @Override
    public List<OrderBorrow> findByUserIdCustermType(OrderBorrow orderBorrow) {

        return orderBorrowMapper.findByUserIdCustermType(orderBorrow);
    }

    @Override
    public OrderBorrow findByUserIdOrderId(OrderPushRiskDTO orderPushRiskDTO) {
        return orderBorrowMapper.findByUserIdOrderId(orderPushRiskDTO);
    }

    @Override
    public List<OrderBorrow> findByUserOrderParam(OrderBorrow orderBorrow) {
        return orderBorrowMapper.findByUserOrderParam(orderBorrow);
    }

    @Override
    public List<OrderBorrowVo> findByIdCard(String idCard) {
        return orderBorrowMapper.findByIdCard(idCard);
    }

    @Override
    public void reviewDispatch(List<OrderBorrow> borrows, List<PlatformUser> callers) {
        if (CollectionUtils.isEmpty(borrows)) {
            log.info("无待审核订单可分配");
            return;
        }
        if (CollectionUtils.isEmpty(callers)) {
            log.info("无审核人员可分配");
            return;
        }
        //审核人员分配数量无上限
        Integer limitCount = 0;
        //2.2 采用多次均匀涂抹法（将待分配订单数按排好序的客服人员，依次分配，最后一次内层循环会优先分配给手里待处理单子少的）派单(最多循环次数：ceilAvgCount * effectivePersonCount)
        if (CollectionUtils.isEmpty(callers)) {
            log.error("所有审核人员审核规则上限不足，请抓紧调整...");
        } else {
            int orderCount = borrows.size();//待分配订单数
            int effectivePersonCount = callers.size();//当前可用审核人员数
            int ceilAvgCount = new BigDecimal(orderCount).divide(new BigDecimal(effectivePersonCount), 0, BigDecimal.ROUND_CEILING).intValue();//平均订单数向上取整数

            int i = 0;//外层循环次数（ceilAvgCount）
            int j = 0;//已分配的订单数（最大为orderCount）
            while (i < ceilAvgCount) {
                for (int t = 0; t < effectivePersonCount; t++) {
                    PlatformUser effectivePerson = callers.get(t);//当前客服人员
                    // 当催收数量无催收规则上限时 即limitCount = 0
                    if (limitCount == 0) {
                        if (j < orderCount) {
                            OrderBorrow order = borrows.get(j);
                            try {
                                order.setReviewUserId(effectivePerson.getId());
                            } catch (Exception e) {
                                log.error("分配当审核人员审核任务出错，贷款ID：" + order.getId(), e);
                            }
                            j++;
                        } else {
                            //客户待审核订单批量分配
                            orderBorrowMapper.updateBatch(borrows);
                            return;
                        }
                    }
                }
                i++;
            }
            //客户待审核订单批量分配
            orderBorrowMapper.updateBatch(borrows);
            //最终订单数未分配完成，给一个通知
            if (j < orderCount) {
                log.error("本次派单后出现审核人员规则上限不足，剩余" + (orderCount - j) + "单未派送，请及时调整...");
            }
        }
    }

    @Override
    public List<OrderBorrow> findByOverdueStatus(Integer userId) {
        return orderBorrowMapper.findByOverdueStatus(userId);
    }

    @Override
    public OrderBorrow selectByUserId(Integer user_id) {
        return orderBorrowMapper.selectByUserId(user_id);
    }

    /**
     * 手动放款消息队列
     *
     * @param jsonData
     */
    @JmsListener(destination = "sendManualLoan")
    public void loanManual(String jsonData) {
        log.info("【loanManual】  ----开始 jsonData={}", jsonData);
        //  try {
        /**1、判断传入的订单JSON数据是否为空*/
        if (org.apache.commons.lang3.StringUtils.isBlank(jsonData)) {
            log.error("【loanManual】 ------->传入订单JSON字符串数据为空 --json={}", jsonData);
            return;
        }
        List<OrderBorrow> orderBorrowss = JSONObject.parseArray(jsonData, OrderBorrow.class);
        if (CollectionUtils.isNotEmpty(orderBorrowss)) {
            for (OrderBorrow bo : orderBorrowss) {
                if (bo.getStatus() == Constant.BORROW_STATUS_QX) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", bo.getUserId());
                    map.put("id", bo.getId());
                    List<OrderBorrow> orderBorrows = orderBorrowMapper.checkLoan(map);
                    if (CollectionUtils.isNotEmpty(orderBorrows)) {
                        //取消放款的订单有在借订单
                        return;
                    }
                }

                OrderBorrow borrowOrderNew = new OrderBorrow();
                borrowOrderNew.setId(bo.getId());
                borrowOrderNew.setUpdateTime(new Date());
                UserInfo user = userInfoMapper.selectByPrimaryKey(bo.getUserId());
                double moneyAmount = bo.getApplyAmount() / 100.0;
                BigDecimal moneyAmountBig = new BigDecimal(moneyAmount);
                // 商户更新订单为成功，处理自己的业务逻辑
                // 流水号
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

                orderRepaymentService.insertByBorrorOrder2(bo);
//签约

                //非贷超签约
                //userAgreeService.getSign(bo, 0);
                //userAgreeService.getSign(bo, 1);
                Byte customerType = bo.getCustomerType();
                //TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                //  @Override
                //  public void afterCommit() {
                /**异步统计放款成功*/
                channelAsyncCountService.loanIsSuccCount(user, d, customerType);
                // 放款成功时将订单信息同步到数据中心
                //channelAsyncCountService.pushInformation(user, bo);
                // }
                //});

                //表borrowOrderChecking 添加一条记录

                double amt = bo.getIntoMoney() / 100.0;
                // 发送通知短信

                // String smsContent = "尊敬的" + user.getRealName() + "，您申请的" + moneyAmount+ "元,扣除相关费用到帐"+amt+"元，请注意查收。";
                // String smsContent = yunFengMsgUtil.getRemitIsSucc();
                Map<String, String> redisMap = smsService.getRedisMap();
                String smsContent = redisMap.get("sms.service.remitIsSucc");
                String increaseMoney = redisMap.get("sms.service.increaseMoney");
                smsContent = smsContent.replace("#RealName#", "用户").replace("#applyAmount#", moneyAmountBig.setScale(2, BigDecimal.ROUND_HALF_UP) + "")
                        .replace("#increaseMoney#", increaseMoney);
                smsService.batchSend(user.getPhone(), smsContent);
                orderBorrowMapper.updateByPrimaryKeySelective(borrowOrderNew);
            }
        }
    }

}
