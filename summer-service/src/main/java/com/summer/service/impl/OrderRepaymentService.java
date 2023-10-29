package com.summer.service.impl;

import com.alibaba.fastjson.JSON;
import com.summer.api.service.*;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.impl.black.JuGuangEnum;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.PaymentUserVO;
import com.summer.pojo.vo.RepaymentRecoredVo;
import com.summer.util.log.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2019/1/3
 */
@Service
public class OrderRepaymentService implements IOrderRepaymentService {
    private static Logger log = LoggerFactory.getLogger(OrderRepaymentService.class);
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserMoneyRateMapper userMoneyRateMapper;
    @Resource
    private IncreaseMoneyConfigDAO increaseMoneyConfigDAO;
    @Autowired
    private ISmsService smsService;
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderRepaymentDetailDAO orderRepaymentDetailDao;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Resource
    private OrderCollectionReductionDAO orderCollectionReductionDAO;
    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Resource
    private RedisUtil redisUtil;
    @Value("${system.prefix}")
    private String PREFIX;
    private static String WITHHOLD_KEYS = "REPAYMENT_REPAY_WITHHOLD";
    @Value("${app.pid}")
    private String pid;

    @Resource
    private AppBaseDAO appBaseDAO;
    @Autowired
    private JuGuangApi juGuangApi;

    @Override
    @Transactional
    public void payOffline(OrderRepayment orderRepayment, Object payTimeObj, String remark, String thirdOrderNo,
                           Object payTypeObj, int paidAmount, Integer backId) {
        Integer id = orderRepayment.getId();
        Date now = new Date();
        OrderRepaymentDetail newOrderDetail = new OrderRepaymentDetail(orderRepayment.getBorrowId(), id,
                orderRepayment.getUserId(), paidAmount, remark, thirdOrderNo, thirdOrderNo, now, "线下还款", now,
                now,
                backId,
                Constant.REPAYMENTDETAIL_STATUS_PAID, Constant.REPAYMENTDETAIL_TYPE_NORMAL,
                payTypeObj == null ? Constant.PAY_TYPE_UNKNOWN :
                        Byte.parseByte(payTypeObj.toString()));
        Date payTime = now;
        if (null != payTimeObj) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                payTime = sdf.parse(payTimeObj.toString());
                newOrderDetail.setPayTime(payTime);
                if (DateUtil.daysBetween(orderRepayment.getRepaymentTime(), payTime) > 0) {
                    newOrderDetail.setOverdue(Constant.REPAYMENTDETAIL_TYPE_OVERDUE);
                } else if (DateUtil.daysBetween(orderRepayment.getRepaymentTime(), payTime) < 0) {
                    newOrderDetail.setOverdue(Constant.REPAYMENTDETAIL_TYPE_FORWARD);
                }
            } catch (ParseException e) {
                log.error("OrderRepaymentService payOffline dateParseError id=" + id);
                payTime = now;
            }
        }
        orderRepaymentDetailDao.insertSelective(newOrderDetail);
        repay(orderRepayment, newOrderDetail);
        if (paidAmount >= orderRepayment.getRepaymentAmount() - orderRepayment.getReduceAmount() - orderRepayment.getPaidAmount()) {
            // 提额
            upLimit(orderRepayment);
        }
        Integer boId = orderRepayment.getBorrowId();
    }

    @Override
    public void upLimit(final OrderRepayment re) {
        Integer id = re.getId();
        boolean up = IPUtils.limitTimesIP(redisUtil, id.toString(), "up", 1);
        if (up) {
            log.info("upLimit次数超限 id={}", id);
            return;
        }
        Integer limit = upUserLimit(re.getUserId());
        log.info("simplePaymentCallback() userId = " + re.getUserId() + " upUserLimitFlag = " + limit);
        if (limit != 0) {
            ThreadPool.getInstance().run(new Runnable() {
                @Override
                public void run() {
                    UserInfo user = userInfoMapper.selectByPrimaryKey(re.getUserId());
                    if (user != null) {
                        UserMoneyRate userMoneyRate = userMoneyRateMapper.findByUserId(re.getUserId());
                        if (userMoneyRate != null) {
                            String phone = user.getPhone();
                            // String increaseSucc = yunFengMsgUtil.getIncreaseSucc();
                            //提额成功
                            Map<String, String> redisMap = smsService.getRedisMap();
                            String increaseSucc = redisMap.get("sms.service.increaseSucc");

                            double maxAmount = limit / 100.0;
                            BigDecimal bigMaxAmount = new BigDecimal(maxAmount);
                            increaseSucc = increaseSucc.replace("#MaxAmount#", "" + bigMaxAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                            smsService.batchSend(phone, increaseSucc);
                            //yunFengMsgUtil.batchSend(phone, "恭喜您已经正常还款，提额到" + userMoneyRate.getMaxAmount()/Constant.CENT_CHANGE_DOLLAR +"元，请继续保持良好的还款习惯！");
                        }
                    }
                }
            });
        }
    }

    private boolean paymentPreCheck(UserInfo user, OrderRepayment re, int money, UserCardInfo userCardInfo) {

        String redis_repay_id = redisUtil.get(PREFIX + WITHHOLD_KEYS + "_" + re.getId());

        //  当前业务处理中， 跳转到错误页面
        if ("true".equals(redis_repay_id)) {
            return false;
        }

        if (money <= 0) {
            //  本条还款不支持还款， 跳转到错误页面
            return false;
        }

        if ("cj_tag".equals(userCardInfo.getAgreeno())) {
            return false;
        }
        redisUtil.set(PREFIX + WITHHOLD_KEYS + "_" + re.getId(), "true", 5 * 60);
        log.info("进入富有代扣还款 锁定订单状态：" + re.getId());

        return true;
    }


    @Override
    public List<OrderRepayment> selectRepaymentByPaidTime(Map<String, Object> params) {
        return orderRepaymentMapper.selectRepaymentByPaidTime(params);
    }

    private OrderRepaymentDetail buildRepaymentDetail(OrderRepayment re, String orderNo, int money) {
        OrderRepaymentDetail detail = new OrderRepaymentDetail();
        detail.setUserId(re.getUserId());
        detail.setRepaymentId(re.getId());
        detail.setPaidAmount(money);
        detail.setOrderNo(orderNo);
        detail.setThirdOrderNo(orderNo);
        detail.setPayType(Constant.PAY_TYPE_AUTO);
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

    public Integer upUserLimit(Integer userId) {
        boolean hasOverdueFlag = true;
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("userId", userId);
        List<OrderRepayment> list = orderRepaymentMapper.selectSimple(queryParam);

        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            log.info("upUserLimit() list is empty");
            return 0;
        }
        for (OrderRepayment orderRepayment : list) {
            Integer lateDay = orderRepayment.getLateDay();
            hasOverdueFlag = lateDay > 0;
            if (hasOverdueFlag) {
                break;
            }
        }

        log.info("upUserLimit() hasOverdueFlag = " + hasOverdueFlag);

        UserMoneyRate userMoneyRate = userMoneyRateMapper.findByUserId(userId);
        if (userMoneyRate == null) {
            userMoneyRate = new UserMoneyRate();
            userMoneyRate.setUserId(userId);
            userMoneyRateMapper.insert(userMoneyRate);
            userMoneyRate = userMoneyRateMapper.findByUserId(userId);
        }

        Integer normalRepay = orderBorrowMapper.findNormalRepay(userId);
        OrderBorrow order = orderBorrowMapper.recentlyOrder(userId);

        Integer curRepet = 1;
        if (userMoneyRate.getMaxAmount().equals(order.getApplyAmount())) {
            curRepet = userMoneyRate.getRepetitionTimes() + 1;
        } else {
            curRepet = userMoneyRate.getRepetitionTimes() == 0 ? 1 : userMoneyRate.getRepetitionTimes();
        }
        //正常还款，逾期还款的
        userMoneyRate.setRepetitionTimes(curRepet);
        boolean isUpLimit = false;
        /*BackConfigParamsVo backConfigParamsVo = backConfigParamsService.findBySysKey("appId");
        if (AppId.XHZ.getValue().equals(backConfigParamsVo.getSysValue()))
            hasOverdueFlag =false;*/
        if (!hasOverdueFlag) {
            /**根据复贷类型type=2查询提额配置规则（increase_money_config ）*/
            List<IncreaseMoneyConfig> allIncreaseConfig = increaseMoneyConfigDAO.findAllIncreaseConfig(Constant.REPETITION_TYPE);
            if (null != allIncreaseConfig && allIncreaseConfig.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                OrderBorrow firstOrder = orderBorrowMapper.findByFirstOrder(userId);
                //计算当前应该提额的总金额
                int increaseTotal = 0;
                for (IncreaseMoneyConfig increaseMoneyConfig : allIncreaseConfig) {
                    int achieveTimes = increaseMoneyConfig.getAchieveTimes();
                    //计算出小于等于当前用户复贷次数的总提额金额
                    if (achieveTimes <= userMoneyRate.getRepetitionTimes()) {
                        increaseTotal += increaseMoneyConfig.getRepetitionInreaseMoney();
                    }
                    //对比达到提额配置中复贷的次数
                    if (achieveTimes == userMoneyRate.getRepetitionTimes()) {
                        /**根据开关判断是否自动提额*/
                        int status = increaseMoneyConfig.getStatus();
                        //状态为1，表示开启自动提额
                        if (status == Constant.INCREASE_OPEN_STATUS) {
                            //设置复贷达到次数后提升的额度 2019-09-05以前都是直接修改用户的复贷金额为多少
                            //  userMoneyRate.setMaxAmount( increaseMoneyConfig.getRepetitionInreaseMoney());

                            //2019-09-05修改为，复贷一次提升多少金额 （最终的金额=用原来的金额+提升的金额）
                            // userMoneyRate.setMaxAmount(userMoneyRate.getMaxAmount() + increaseMoneyConfig.getRepetitionInreaseMoney());
                            /**提升的金额 = 第一笔订单的申请金额+ 小于等于当前用户复贷次数的总提额金额*/
                            Integer newMaxAmount = firstOrder.getApplyAmount() + increaseTotal;
                            if (newMaxAmount > userMoneyRate.getMaxAmount()) {
                                userMoneyRate.setMaxAmount(newMaxAmount);
                            }
                            if (increaseMoneyConfig.getLoanTerm() != null && increaseMoneyConfig.getLoanTerm() > 0) {
                                userMoneyRate.setLoanTerm(increaseMoneyConfig.getLoanTerm());
                            }
                            //复贷达到次数后降低的利息
                            userMoneyRate.setAccrual(increaseMoneyConfig.getReduceInterest());
                        }
                        isUpLimit = true;
                        break;
                    }
                }
            }
        }
        //复借可以用来修改费率
        //userMoneyRate.setCreditVet(0.16);
        // userMoneyRate.setAccountManage(0.126);
        //userMoneyRate.setAccrual(0.014);
        userMoneyRate.setUpdateTime(new Date());
        if (userMoneyRateMapper.update(userMoneyRate) > 0 && isUpLimit)
            return userMoneyRate.getMaxAmount();
        else
            return 0;
    }

    @Override
    public void insertByBorrorOrder(OrderBorrow borrowOrder) {
        Date fkDate = borrowOrder.getLoanTime();
        OrderRepayment repayment = new OrderRepayment();
        repayment.setUserId(borrowOrder.getUserId());
        repayment.setBorrowId(borrowOrder.getId());
        repayment.setRepaymentAmount(borrowOrder.getIntoMoney() + borrowOrder.getLoanFee());
        repayment.setLateFeeApr(borrowOrder.getLateFeeApr());
        repayment.setPaidAmount(0);
        repayment.setPrincipalAmount(borrowOrder.getIntoMoney());
        repayment.setFeeAmount(borrowOrder.getLoanFee());

        repayment.setFirstRepaymentTime(DateUtil.addDay(fkDate, borrowOrder.getLoanTerm() - 1));// 放款时间加上借款期限
        repayment.setRepaymentTime(DateUtil.addDay(fkDate, borrowOrder.getLoanTerm() - 1));// 放款时间加上借款期限

        repayment.setCreateTime(fkDate);
        repayment.setStatus(borrowOrder.getStatus());
        orderRepaymentMapper.insertSelective(repayment);
    }

    @Override
    public boolean repay(OrderRepayment re, OrderRepaymentDetail detail) {
        boolean isTotal = false;
        OrderBorrow bo = new OrderBorrow();
        bo.setId(re.getBorrowId());
        bo.setUserId(re.getUserId());
        OrderRepayment copy = new OrderRepayment();
        copy.setId(re.getId());
        Integer paidAmount = detail.getPaidAmount();
        copy.setPaidAmount(re.getPaidAmount() + paidAmount);//用户已还款金额
        copy.setPayType(detail.getPayType());
        //防止已还金额 > 应还金额
        if (copy.getPaidAmount() > re.getRepaymentAmount()) {
            copy.setPaidAmount(re.getRepaymentAmount());
        }
        UserInfo user = userInfoMapper.selectByPrimaryKey(re.getUserId());
        UserInfo userCopy = new UserInfo();
        userCopy.setId(user.getId());

        Integer isAllRepay = 1;
        // 判断是否全部还清
        if (copy.getPaidAmount() >= re.getRepaymentAmount() - re.getReduceAmount()) {//已还金额 > 还款总金额
            if (re.getLateDay() > 0) {//逾期天数
                // 逾期已还款 告知催收
                //collection(user, re, detail, Repayment.REPAY_COLLECTION);
                copy.setStatus(Constant.REPAYMENT_STATUS_PAID_OVERDUE);//逾期还款状态
                copy.setCollected((byte) 1);
                bo.setStatus(Constant.BORROW_STATUS_PAID_OVERDUE);//逾期还款状态
                Map<String, Object> map = new HashMap<>();
                map.put("repaymentId", re.getId());
                List<OrderCollection> orderCollections = orderCollectionDAO.selectSimple(map);
                if (CollectionUtils.isNotEmpty(orderCollections)) {
                    OrderCollection orderCollection = orderCollections.get(0);
                    OrderCollection newCollect = new OrderCollection();
                    newCollect.setId(orderCollection.getId());
                    newCollect.setStatus(Constant.XJX_COLLECTION_ORDER_STATE_SUCCESS);
                    newCollect.setRealMoney(orderCollection.getRealMoney() + paidAmount);
                    orderCollectionDAO.updateByPrimaryKeySelective(newCollect);
                }
            } else {
                copy.setStatus(Constant.REPAYMENT_STATUS_PAID);
                if (Constant.REPAYMENTDETAIL_TYPE_FORWARD == detail.getOverdue()) {
                    copy.setPaidForward(Constant.REPAYMENT_TYPE_FORWARD);
                    copy.setStatus(Constant.REPAYMENT_STATUS_PAID_FARWORD);
                }
                bo.setStatus(Constant.BORROW_STATUS_PAID);
            }
            //退款不改成老客户
            if (!detail.getOrderNo().startsWith("JPT")) {
                if (Constant.CUSTOMER_TYPE_NEW == user.getCustomerType()) {
                    userCopy.setCustomerType(Constant.CUSTOMER_TYPE_OLD);
                }
            }


            // 全部还款-更新info_user_info borrow_status 状态为不可见
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("USER_ID", user.getId());
            map.put("BORROW_STATUS", "0");
            infoIndexInfoDao.updateInfoUserInfoBorrowStatus(map);

            copy.setPaidTime(null != detail.getPayTime() ? detail.getPayTime() : new Date());
            isTotal = true;
        } else {
            if (re.getLateDay() > 0) {
                // 逾期部分还款 告知催收
                copy.setCollected((byte) 1);
            }
            copy.setStatus(Constant.REPAYMENT_STATUS_PART_PAID);//部分还款状态
            bo.setStatus(Constant.BORROW_STATUS_PART_PAID);//部分还款状态
            isAllRepay = 2;
            log.info("部分还款");
        }
        // 本次还款金额
        Integer money = paidAmount;
        // 已付的逾期费 = 所有还款金额 - 借款金额 - 手续费
        Integer payedOver = re.getPaidAmount() - (re.getFeeAmount() + re.getPrincipalAmount());
        money = payedOver > 0 ? money - payedOver : money;
        if (money > 0) {
            // 把用户可借额度加上
            if ((user.getAmountAvailable() + money) > user.getAmountMax()) {
                userCopy.setAmountAvailable(user.getAmountMax());
            } else {
                userCopy.setAmountAvailable(user.getAmountAvailable() + money);
            }
            log.info("用户额度变回成功userId=" + userCopy.getId() + ",amountAvailable=" + userCopy.getAmountAvailable());
        } else {
            log.info("用户额度变回失败userId=" + userCopy.getId() + ",money=" + money + ",payedOver=" + payedOver);
        }
        if (null != userCopy.getCustomerType() || null != userCopy.getAmountAvailable()) {
            userInfoMapper.updateByPrimaryKeySelective(userCopy);
            log.info("用户额度保存成功userId=" + userCopy.getId() + ",amountAvailable=" + userCopy.getAmountAvailable());
        } else {
            log.info("用户额度保存失败userId=" + userCopy.getId() + ",customerType=" + userCopy.getCustomerType() + ",amountAvailable=" + userCopy.getAmountAvailable());
        }
        bo.setUpdateTime(new Date());
        orderBorrowMapper.updateByPrimaryKeySelective(bo);
        orderRepaymentMapper.updateByPrimaryKeySelective(copy);
//        OrderRepayment orderRepayment = orderRepaymentMapper.selectByPrimaryKey(copy.getId());
//        OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(bo.getId());
        /**还款成功推送白名单到数据中心*/
//        pushWhite(user, orderBorrow, orderRepayment);
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//            @Override
//            public void afterCommit() {
//                // 还款成功时将订单信息同步到数据中心
//                pushRepaymentInformation(orderBorrow);
//            }
//        });

        // 还款上传
        try {
            pushRepaymentInformation(user);
        } catch (Exception e) {
            log.error("聚光共债订单状态上传:{}", e);
        }
        return isTotal;
    }

    @Async
    public void pushRepaymentInformation(UserInfo user) {
        log.info("还款成功时将订单信息同步到数据中心返回参数------res={}", "res");
        List<Map<String, String>> userList = new ArrayList<>();
        Map map = new HashMap();
        map.put("name", user.getRealName());
        map.put("phone", user.getPhone());
        map.put("idNo", user.getIdCard());
        userList.add(map);
        juGuangApi.uploadJointDebt(userList, JuGuangEnum.REPAY.getValue(), 0);
    }

    private int getBorrowStatus(Byte boStatus) {
        int res = 0;
        int status = boStatus;
        switch (status) {
            case Constant.WAIT_SIHN:
                res = 4;
                break;
            case -1:
                res = 1;
                break;
            case 1:
            case 4:
            case 5:
                res = 2;
                break;
            case 6:
                res = 6;
                break;
            case 8:
                res = 7;
                break;
            case 10:
                res = 8;
                break;
            case 13:
                res = 10;
                break;
            case 7:
                res = 15;
                break;
            case 22:
                res = 22;
                break;
            default:
                break;
        }
        return res;
    }

    private String getMoneyFormat(Integer applyAmount) {
        return new BigDecimal(applyAmount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
    }


    /**
     * 续期
     *
     * @param repayment
     * @param record
     * @return
     */
    @Override
    public void renewal(OrderRepayment repayment, OrderRenewal record) {
        if (null == repayment) {
            repayment = orderRepaymentMapper.selectByPrimaryKey(record.getRepaymentId());
        }
        // 更新续期为成功
        record.setStatus(Constant.RENEWAL_STATUS_PAID);//代扣成功状态
        orderRenewalMapper.updateByPrimaryKeySelective(record);
        OrderBorrow borrowOrder = new OrderBorrow();
        borrowOrder.setId(repayment.getBorrowId());
        borrowOrder.setStatus(Constant.BORROW_STATUS_HKZ);
        borrowOrder.setLoanEndTime(DateUtil.addDay(repayment.getRepaymentTime(), record.getRenewalDay()));
        //续期更改为老客
        UserInfo user = userInfoMapper.selectByPrimaryKey(repayment.getUserId());
//        UserInfo userCopy = new UserInfo();
//        userCopy.setId(user.getId());
//        if (Constant.CUSTOMER_TYPE_NEW == user.getCustomerType()) {
//            borrowOrder.setCustomerType(Constant.CUSTOMER_TYPE_OLD);
//            userCopy.setCustomerType(Constant.CUSTOMER_TYPE_OLD);
//            userCopy.setUpdateTime(new Date());
//            userInfoMapper.updateByPrimaryKeySelective(userCopy);
//        }

        if (Constant.CUSTOMER_TYPE_NEW == user.getCustomerType()) {
            String s = DateUtil.formatTimeYmd(new Date());
            String s1 = redisUtil.get(s + "_customerType");
            List<OrderBorrow> orderBorrowList = new ArrayList<>();
            OrderBorrow borrowOrder1 = new OrderBorrow();
            borrowOrder1.setId(repayment.getBorrowId());
            borrowOrder1.setUserId(user.getId());
            borrowOrder1.setCustomerType(Constant.CUSTOMER_TYPE_OLD);
            if (StringUtils.isEmpty(s1)) {
                orderBorrowList.add(borrowOrder1);
                redisUtil.set(s + "_customerType", JSON.toJSONString(orderBorrowList));
            } else {
                orderBorrowList = JSON.parseArray(s, OrderBorrow.class);
                orderBorrowList.add(borrowOrder1);
                redisUtil.set(s + "_customerType", JSON.toJSONString(orderBorrowList));
            }
        }
        orderBorrowMapper.updateByPrimaryKeySelective(borrowOrder);

        OrderRepayment re = new OrderRepayment();
        // 如果申请续期成功
        re.setId(repayment.getId());
        re.setRepaymentAmount(repayment.getPrincipalAmount() + repayment.getFeeAmount());
//		re.setPlanLateFee(repayment.getRepaymentAmount().intValue() - repayment.getPlanLateFee());
        re.setLateFee(0);
        // 还款日期 延后 （上期还款时间 + 逾期天数+续期天数）
        re.setRepaymentTime(DateUtil.addDay(DateUtil.addDay(repayment.getRepaymentTime(), repayment.getLateDay()), record.getRenewalDay()));

        /*if(StringUtils.isNotEmpty(amount)){
            //续期应还
            Integer renewAmount = Integer.valueOf(record.getLateFee() + record.getRenewalFee() + record.getRepaymentFee());

            Integer realAmount = Integer.valueOf(amount)*100;
            //支付金额不取整, 续期部分金额，不续期天数
            if(renewAmount -  realAmount > 1000){
                re.setRepaymentTime(repayment.getRepaymentTime());
            }
        }*/
        re.setLateStartTime(null);
        re.setLateUpdateTime(null);
        re.setLateDay(0);
        re.setStatus(Constant.REPAYMENT_STATUS_INIT);
        // 如果是已逾期的续期（调用催收同步）
        if (Constant.REPAYMENT_STATUS_OVERDUE == repayment.getStatus()) {
            re.setCollected((byte) 1);
        }
        orderRepaymentMapper.updateByPrimaryKeySelective(re);
        try {
            // 续期成功时将订单信息同步到数据中心
            pushRenewalInformation(user);
        } catch (Exception e) {
            log.error("续期共债通知异常:{}", e);
        }
    }

    @Async
    public void pushRenewalInformation(UserInfo user) {
        // 将续期成功的用户订单信息推到数据中心
        log.info("续期成功时将订单信息同步到数据中心返回参数------res={}", "res");
        List<Map<String, String>> userList = new ArrayList<>();
        Map map = new HashMap();
        map.put("name", user.getRealName());
        map.put("phone", user.getPhone());
        map.put("idNo", user.getIdCard());
        userList.add(map);
        juGuangApi.uploadJointDebt(userList, JuGuangEnum.CONTINUE.getValue(), 0);
    }

    @Override
    public boolean insertByBorrorOrder2(OrderBorrow borrowOrder) {
        Integer id = borrowOrder.getId();
        try {
            log.info("添加借款记录===");
            Map<String, Object> param = new HashMap();

            param.put("borrowId", id);
            List<OrderRepayment> havRepayments = orderRepaymentMapper.selectSimple(param);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(havRepayments)) {
                log.info("已经存在还款单，不在生成。借款单id为：" + id);
                return true;
            }
            // add by yu
            Date fkDate = borrowOrder.getLoanTime();
            OrderRepayment repayment = new OrderRepayment();
            repayment.setUserId(borrowOrder.getUserId());
            repayment.setBorrowId(id);
            Integer repaymentAmount = borrowOrder.getApplyAmount();
            Integer intoMoneyL = borrowOrder.getIntoMoney();
            log.info("borrowOrder====" + borrowOrder);
            log.info("borrowOrder.getIntoMoney()==" + borrowOrder.getIntoMoney() + "     borrowOrder.getMoneyAmount()==" + repaymentAmount);
            repayment.setPrincipalAmount(intoMoneyL);
            // 利息前置/后置开关
            BackConfigParamsVo interest = backConfigParamsService.findBySysKey(Constant.INTEREST);
            if (interest == null || interest.getSysValue() == 0) {
                // 前置
                log.info("OrderRepaymentService 利息前置");
                repayment.setRepaymentAmount(repaymentAmount);
            } else {
                // 后置
                log.info("OrderRepaymentService 利息后置");
                repayment.setRepaymentAmount(intoMoneyL + borrowOrder.getLoanFee());
            }
            repayment.setLateFeeApr(borrowOrder.getLateFeeApr());
            repayment.setPaidAmount(0);
            repayment.setFeeAmount(borrowOrder.getLoanFee());

            repayment.setFirstRepaymentTime(DateUtil.addDay(fkDate, borrowOrder.getLoanTerm() - 1));// 放款时间加上借款期限
            repayment.setRepaymentTime(DateUtil.addDay(fkDate, borrowOrder.getLoanTerm() - 1));// 放款时间加上借款期限
            repayment.setStatus(borrowOrder.getStatus());
            orderRepaymentMapper.insertSelective(repayment);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("create repayment insertByBorrorOrder borrowId={},error={}", id, e.getMessage());
            return false;
        }
        return true;
    }

    private void pushRepayToCs(OrderRepayment re) {
        try {
//            jedisCluster.set("REPAY_" + re.getId(), ""+re.getId());
//            jedisCluster.del("OVERDUE_" + re.getId());
//            log.info("collection repay success YQYHK REPAY_" + re.getId() + " value=" + jedisCluster.get("REPAY_" + re.getId()) + " OVERDUE=" + jedisCluster.get("OVERDUE_" + re.getId()));
        } catch (Exception e) {
            log.error("collection repay error YQYHK repaymentId=" + re.getId(), e);
        }
    }

    private boolean getIsOverdue(Integer repayId) {
        boolean isOverdue = false;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("repaymentId", repayId);
        param.put("status", Constant.RENEWAL_STATUS_PAID);
        List<OrderRenewal> records = orderRenewalMapper.selectSimple(param);
        Integer planLateFeeAgo = null;
        if (CollectionUtils.isNotEmpty(records)) {
            for (OrderRenewal renewalRecord : records) {
                planLateFeeAgo = renewalRecord.getLateFee();
                if (planLateFeeAgo != null && planLateFeeAgo > 0) {
                    isOverdue = true;
                    break;
                }
            }
        }
        return isOverdue;
    }

    @Override
    public List<OrderRepayment> findOrderPrepaymentByParam(Map<String, Object> params) {
        return orderRepaymentMapper.findOrderPrepaymentByParam(params);
    }

    @Override
    public List<RepaymentRecoredVo> findOrderRecordByParam(Integer userId, int type) {
        //按照参数查询出来的还款订单
        List<OrderRepayment> orderPrepaymentByParam = null;
        //要返回去的订单集合
        ArrayList<RepaymentRecoredVo> resultOorderRepayments = new ArrayList<>();
        ///**APP查询待还款订单的类型*/
        if (Constant.FIND_WAIT_ORDER_TYPE == type) {
            //根据用户id查询待还款记录
            orderPrepaymentByParam = findStayRepayment(userId);
            if (null != orderPrepaymentByParam && orderPrepaymentByParam.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                for (OrderRepayment orderRepayment : orderPrepaymentByParam) {
                    RepaymentRecoredVo repaymentRecoredVo = new RepaymentRecoredVo();
                    //还款表的订单id
                    repaymentRecoredVo.setId(orderRepayment.getId());
                    //查询借款订单
                    OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(orderRepayment.getBorrowId());
                    if (null != orderBorrow) {
                        //借款金额
                        BigDecimal repaymentAmount = new BigDecimal(orderBorrow.getApplyAmount() / Constant.CHANGE_CENT);
                        repaymentRecoredVo.setRepaymentAmount(repaymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        //借款时间
                        repaymentRecoredVo.setLoanTime(DateUtil.formatTimeYmd(orderBorrow.getCreateTime()));
                    } else {
                        //借款金额(实际到账+服务费)
                        BigDecimal repaymentAmount = new BigDecimal(((orderRepayment.getPrincipalAmount() + orderRepayment.getFeeAmount()) / Constant.CHANGE_CENT));
                        repaymentRecoredVo.setRepaymentAmount(repaymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        repaymentRecoredVo.setLoanTime(DateUtil.formatTimeYmd(orderRepayment.getCreateTime()));
                    }

                    //距离还款日(预计还款日 - 今天)
                    repaymentRecoredVo.setRepaymentCountDown(DateUtil.daysBetween(new Date(), orderRepayment.getRepaymentTime()));
                    //逾期天数（当状态为：3、5）
                    repaymentRecoredVo.setLateDay(orderRepayment.getLateDay());
                    //还款状态（0,1,3,5）0:待还款;1:部分还款;2:已还款;3:已逾期;4:逾期已还款，5:已坏账
                    repaymentRecoredVo.setStatus(orderRepayment.getStatus().intValue());
                    resultOorderRepayments.add(repaymentRecoredVo);
                }
            }
        } else if (Constant.FIND_REPAYMENT_ORDER_TYPE == type) {
            //查询已还款记录 //还款状态（2，4,6）0:待还款;1:部分还款;2:已还款;3:已逾期;4:逾期已还款，5:已坏账,6提前还款
            orderPrepaymentByParam = findRepayment(userId);
            if (null != orderPrepaymentByParam && orderPrepaymentByParam.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                for (OrderRepayment orderRepayment : orderPrepaymentByParam) {
                    RepaymentRecoredVo repaymentRecoredVo = new RepaymentRecoredVo();
                    //还款表id
                    repaymentRecoredVo.setId(orderRepayment.getId());
                    //实际还款时间
                    repaymentRecoredVo.setPaidTime(DateUtil.formatTimeYmd(orderRepayment.getPaidTime()));
                    //查询该借款订单
                    OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(orderRepayment.getBorrowId());
                    if (null != orderBorrow) {
                        //借款金额
                        BigDecimal repaymentAmount = new BigDecimal((orderBorrow.getApplyAmount() / Constant.CHANGE_CENT));
                        repaymentRecoredVo.setRepaymentAmount(repaymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else {
                        //借款金额(实际到账+服务费)
                        BigDecimal repaymentAmount = new BigDecimal(((orderRepayment.getPrincipalAmount() + orderRepayment.getFeeAmount()) / Constant.CHANGE_CENT));
                        repaymentRecoredVo.setRepaymentAmount(repaymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                    //状态还款状态（2，4,6）
                    repaymentRecoredVo.setStatus(orderRepayment.getStatus().intValue());
                    resultOorderRepayments.add(repaymentRecoredVo);
                }
            }
        }
        return resultOorderRepayments;
    }

    @Override
    public OrderRepayment findOrderRecordDetailById(Integer orderId) {
        OrderRepayment orderRepayment = orderRepaymentMapper.selectByPrimaryKey(orderId);
        return orderRepayment;
    }

    @Override
    public OrderRepayment selectByPrimaryKey(Integer id) {
        return orderRepaymentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderRepayment> findStayRepayment(Integer userId) {
        return orderRepaymentMapper.findStayRepayment(userId);
    }

    @Override
    public List<OrderRepayment> findRepayment(Integer userId) {
        return orderRepaymentMapper.findRepayment(userId);
    }

    /**
     * 更新逾期信息
     *
     * @param repayment
     */
    @Override
    public void overdue(OrderRepayment repayment, LoanRuleConfig loanConfig) {
        Date date = new Date();
        try {
            //逾期天数
            int days = DateUtil.daysBetween(repayment.getRepaymentTime(), date);
            UserInfo userInfo = new UserInfo();
            userInfo.setId(repayment.getUserId());
            if (days > 0) {
                //滞纳金=(借款到账金额+服务费)*滞纳金服务费/10000*滞纳天数
                log.info(repayment.getPrincipalAmount().toString());
                log.info(repayment.getFeeAmount().toString());
                log.info(repayment.getLateFeeApr().toString());
                Long lateFee1 = (Long.valueOf(repayment.getPrincipalAmount()) + repayment.getFeeAmount()) * repayment.getLateFeeApr() / 10000 * days;
                Integer lateFee = lateFee1.intValue();
                // LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(Constant.CHANNELRULE_ID);
                int highestOverdue = 0;
                if (null != loanConfig && null != loanConfig.getHighestOverdueRate()) {
                    int intValue = loanConfig.getHighestOverdueRate().intValue();
                    if (intValue > 0) {
                        highestOverdue = intValue;
                    } else {
                        log.error("highestOverdue<0");
                    }
                }
                //如果滞纳金>=本金，那应还滞纳金数为应还金额数额(滞纳金额不能超过本金金额)
                if (lateFee >= highestOverdue) {
                    lateFee = highestOverdue;
                }
                //更新repayment
                repayment.setLateDay(days);
                repayment.setLateUpdateTime(date);
                Integer oldLate = repayment.getLateFee();
                repayment.setLateFee(lateFee);
                if (lateFee < 0) {
                    log.info("lateFee:{}", lateFee);
                    log.info("userId:{}", repayment.getUserId());
                }
                int diffLate = lateFee - oldLate;
                if (diffLate < 0) {
                    diffLate = 0;
                    log.error("diffLate<0 id={}, lateFee={},oldLate={}", repayment.getId(), lateFee, oldLate);
                }
                repayment.setRepaymentAmount(repayment.getRepaymentAmount() + diffLate);

                int receivablePrinciple = repayment.getRepaymentAmount() - lateFee;// 应还本金

                // 实收的利息 = 已还金额 - 应还本金
                Integer paidAmount = repayment.getPaidAmount();
                int realPenlty = paidAmount - receivablePrinciple;

                if (realPenlty <= 0) {
                    repayment.setTruePrincipal(paidAmount);//实收本金
                    repayment.setTrueLateFee(0);//实收罚息
                } else {
                    repayment.setTruePrincipal(paidAmount - realPenlty);//实收本金
                    repayment.setTrueLateFee(realPenlty);//实收罚息
                }

                //更新用户逾期总天数、历史逾期记录数
                UserInfo user = userInfoMapper.selectByUserId(repayment.getUserId());
                if (repayment.getLateDay() == 1) {
                    userInfo.setHistoryOverNum(user.getHistoryOverNum() + 1);
                    repayment.setLateStartTime(date);
                }
                repayment.setStatus(Constant.REPAYMENT_STATUS_OVERDUE);
                Integer borrowId = repayment.getBorrowId();
                OrderBorrow borrow = orderBorrowMapper.selectByPrimaryKey(borrowId);
                if (borrow != null && borrow.getStatus() < 10) {
                    borrow.setStatus(Constant.BORROW_STATUS_OVERDUE);
                    orderBorrowMapper.updateByPrimaryKeySelective(borrow);
                }

                //如果未催收 进入催收
                if (Constant.COLLECTION_AUDIT_TODO == repayment.getCollected()) {
                    log.info("collection overdue success OVERDUE_" + repayment.getId());
                    repayment.setCollected((byte) 1);
                }
                orderRepaymentMapper.updateByPrimaryKeySelective(repayment);
            }
            userInfo.setLastOverDays(days);
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        } catch (Exception e) {
            log.error("overdue error repaymentId = " + repayment.getId(), e);
        }
    }

    @Override
    public List<PaymentUserVO> findParams(Map<String, Object> params) {
        List<PaymentUserVO> paymentUserVOs = orderRepaymentMapper.findParams(params);
        for (PaymentUserVO paymentUserVO : paymentUserVOs) {
            paymentUserVO.setAuditStatus(Byte.parseByte("3"));
            OrderCollectionReduction byRepaymentId = orderCollectionReductionDAO.findByRepaymentId(paymentUserVO.getId());
            if (null != byRepaymentId) {
                paymentUserVO.setAuditStatus(byRepaymentId.getAuditStatus());
                String remark = byRepaymentId.getRemark();
                if (null != remark) {
                    if (remark.contains("_")) {
                        String[] strings = remark.split("_");
                        paymentUserVO.setRemark(strings[0]);
                        String beforeRepayAmount = strings[1];
                        //上一次的应还金额
                        paymentUserVO.setBeforeRepayAmount(beforeRepayAmount);
                    }
                }
            }

            OrderCollectionReduction rderCollectionReduction = orderCollectionReductionDAO.findByRepayIdAsc(paymentUserVO.getId());
            if (null != rderCollectionReduction) {
                // paymentUserVO.setAuditStatus(rderCollectionReduction.getAuditStatus());
                String remarkStart = rderCollectionReduction.getRemark();
                if (null != remarkStart) {
                    if (remarkStart.contains("_")) {
                        String[] string = remarkStart.split("_");
                        //paymentUserVO.setRemark(strings[0]);
                        String beforeRepayAmount = string[1];
                        //最早的应还金额
                        paymentUserVO.setStartRepayAmount(beforeRepayAmount);
                    }
                }
            }
        }
        return paymentUserVOs;
    }

    /**
     * 推送白名单
     *
     * @param userInfo
     * @param orderBorrow
     * @param orderRepayment
     */
    @Async
    public void pushWhite(UserInfo userInfo, OrderBorrow orderBorrow, OrderRepayment orderRepayment) {
        Map<String, Object> reqParam = new HashMap<>();
        //手机号
        reqParam.put("phone", userInfo.getPhone());
        //真是姓名
        reqParam.put("realName", userInfo.getRealName());
        //申请金额
        reqParam.put("applyAmount", orderBorrow.getApplyAmount());
        //申请时间
        reqParam.put("applyTime", DateUtil.formatTimeYmdHms(orderBorrow.getCreateTime()));
        //还款金额
        reqParam.put("repayAmount", orderRepayment.getPaidAmount());
        //还款时间
        reqParam.put("repayTime", DateUtil.formatTimeYmdHms(orderRepayment.getPaidTime()));
        AppBase appBase = appBaseDAO.selectByPrimaryKey(1);
        if (null != appBase) {
            reqParam.put("appName", appBase.getName());
        }
        reqParam.put("pid", pid);


        try {
            //String result = HttpUtil.doPost(centerUrl + "user/pushWhite", reqParam);
            log.info("还款成功推送白名单返回参数---phone={},result={}", userInfo.getPhone(), "result");
        } catch (Exception e) {
            log.error("还款成功推送白名单--异常----phone={},error={}", userInfo.getPhone(), e);
        }

    }

    @Override
    public OrderRepayment selectByBorrowId(Integer borrowId) {
        return orderRepaymentMapper.selectByBorrowId(borrowId);
    }

    @Override
    public int updateServiceCharge(UserMoneyRate userMoneyRate) {
        return orderRepaymentMapper.updateServiceCharge(userMoneyRate);
    }
}
