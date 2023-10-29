package com.summer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.*;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.impl.black.JuGuangEnum;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Desc:
 * Created by tl on 2019/4/12
 */
@Slf4j
@Service
public class TaskjobService implements ITaskjobService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IRiskCreditUserDao riskCreditUserDao;
    @Resource
    private IRiskCreditUserService riskCreditUserService;
    @Resource
    private IndexReportDAO indexReportDAO;
    @Resource
    private OrderRenewalMapper orderRenewalMapper;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderCollectionDAO orderCollectionDAO;
    @Resource
    private CountCollectionAssessmentDAO countCollectionAssessmentDAO;
    @Resource
    private IMmanLoanCollectionStatusChangeLogDao mmanLoanCollectionStatusChangeLogDao;
    @Resource
    private OrderBorrowService orderBorrowService;
    @Resource
    private IOrderCollectionService orderCollectionService;
    @Value("${app.pid}")
    private String Pid;
    // 客服微信
    @Value("${sms.service.WeChat}")
    private String weChat;
    // 在提醒还款时，提示提升的假额度
    @Value("${sms.service.increaseMoney}")
    private String increaseMoney;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Autowired
    private ISmsService smsService;
    @Resource
    private AppBaseDAO appBaseDAO;
    @Resource
    private ReportRepaymentDAO repaymentDAO;
    @Resource
    private IOrderRepaymentService repaymentService;
    @Resource
    private OrderCollectionCallerDAO orderCollectionCallerDAO;
    @Resource
    private CollectionStatisticsMapper collectionStatisticsMapper;
    @Value("${system.prefix}")
    private String PREFIX;
    private static String FKYE = "FKYE_KEYS";
    @Resource
    private RedisUtil redisUtil;
    /**
     * 数据中心回调地址
     */
    @Value("${billing.centerUrl}")
    private String centerUrl;
    @Value("${app.pid}")
    private String pid;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Autowired
    private JuGuangApi juGuangApi;

    @Override
    public IndexReport statisticIndex(Date date, boolean insert) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("loan", "true");
        Map<String, Object> res = orderRepaymentMapper.statisticByDay(map);
        Map<String, Object> renewal = orderRenewalMapper.statisticByDay(map);
        map.remove("loan");
        map.put("repay", "true");
        Map<String, Object> pay = orderRepaymentMapper.statisticByDay(map);
        map.remove("repay");
        map.put("over", "true");
        Map<String, Object> over = orderRepaymentMapper.statisticByDay(map);
        log.info("------------------------over=" + over.toString());
        map.remove("over");
        map.put("normal", "true");
        Map<String, Object> normal = orderRepaymentMapper.statisticByDay(map);
        Date now = new Date();
        BigDecimal loanAmount = new BigDecimal(res.get("loanAmount").toString());

        BigDecimal amount = new BigDecimal(renewal.get("amount").toString());

        BigDecimal toPay = new BigDecimal(normal.get("toPay").toString());
        BigDecimal overdueMoney = new BigDecimal(over.get("toPay").toString());

        BigDecimal paidAmount = new BigDecimal(pay.get("paidAmount").toString());
        //统计每日注册,放款,逾期
        map.remove("normal");
        Long reg = userInfoMapper.registerByDay(map);
        Long loan = userInfoMapper.loanByDay(map);
        Long overNum = userInfoMapper.overdueByDay(map);
        String dateFormat = DateUtil.getDateFormat(DateUtil.addDay(date, -1), "yyyy-MM-dd");
        byte customerType = (byte) 3;
        IndexReport newRpt = new IndexReport(dateFormat, loanAmount,
                paidAmount, amount, toPay, overdueMoney,
                reg.intValue(), loan.intValue(), overNum.intValue(), now, now, (byte) 1, customerType);

        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", dateFormat);
        params.put("customerType", customerType);
        List<IndexReport> list = indexReportDAO.selectSimple(params);
        if (insert && CollectionUtils.isEmpty(list)) {
            indexReportDAO.insertSelective(newRpt);
        }
        return newRpt;
    }

    @Override
    public void handleDispatchTask() {
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();
        Date repaymentTimeStart = DateUtil.dateSubtraction(date, -30);
        map.put("repaymentTimeStart", repaymentTimeStart);
        map.put("collection", 1);
        List<OrderRepayment> repayments = orderRepaymentMapper.selectSimple(map);
        dispatchforLoanId(repayments);

    }

    @Override
    public void repaySmsRemind() {
        log.info("短信提醒任务开始--------------->repaySmsRemind start");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String repaymentTimeStart = dateFormat.format(DateUtil.addDay(date, 1));
        String rePaymentTimeEnd = dateFormat.format(DateUtil.addDay(date, 2));
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentTimeStart", repaymentTimeStart);
        params.put("repaymentTimeEnd", rePaymentTimeEnd);
        //查找还款中用户
        Byte[] bytes = new Byte[]{Constant.REPAYMENT_STATUS_INIT};
        params.put("statuses", bytes);
        params.put("type", "advance");
        repaySms(params);
    }

    @Override
    public void repaySmsRemindNow() {
        log.info("短信提醒任务开始--------------->repaySmsRemindNow start");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String repaymentTimeStart = dateFormat.format(date);
        String rePaymentTimeEnd = dateFormat.format(DateUtil.addDay(date, 1));
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentTimeStart", repaymentTimeStart);
        params.put("repaymentTimeEnd", rePaymentTimeEnd);
        //查找还款中用户
        Byte[] bytes = new Byte[]{Constant.REPAYMENT_STATUS_INIT};
        params.put("statuses", bytes);
        params.put("type", "now");
        repaySms(params);
    }

    @Override
    public void repaySmsOverdue() {
        log.info("逾期短信通知任务开始--------------->repaySmsOverdue start");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String repaymentTimeStart = dateFormat.format(DateUtil.addDay(date, -2));
        String rePaymentTimeEnd = dateFormat.format(date);
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentTimeStart", repaymentTimeStart);
        params.put("repaymentTimeEnd", rePaymentTimeEnd);
        //查找还款中用户
        Byte[] bytes = new Byte[]{Constant.REPAYMENT_STATUS_OVERDUE};
        params.put("statuses", bytes);
        params.put("type", "overdue");
        repaySms(params);
    }

    /**
     * 还款短息提醒
     *
     * @param params
     */
    private void repaySms(Map<String, Object> params) {
        //查询数据库客服微信
        //AppBase appBase = new AppBase();
        //appBase.setName(yunFengMsgUtil.getAppName());
        AppBase newappBase = appBaseDAO.selectByPrimaryKey(1);
        //如果为空就读取配置文件里面的微信
        if (null == newappBase) {
            newappBase = new AppBase();
            newappBase.setServicePhone(this.weChat);
        }
        List<OrderRepayment> repayments = repaymentDAO.findByRepaymentSmsRemind(params);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String content;
        String type = (String) params.get("type");

        try {
            for (OrderRepayment orderRepayment : repayments) {
                Integer lateDay = orderRepayment.getLateDay();
                if (null != lateDay) {
                    //过7天就不要短信提醒了
                    if (lateDay > 7) {
                        continue;
                    }
                }

                UserInfo user = userInfoMapper.selectByUserId(orderRepayment.getUserId());
                Map<String, String> redisMap = smsService.getRedisMap();

                if ("now".equals(type)) {
                    //content = yunFengMsgUtil.getRepayRemindNow();
                    //到期当天
                    content = redisMap.get("sms.service.repayRemindNow");
                } else if ("advance".equals(type)) {
                    // content = yunFengMsgUtil.getRepayRemind();
                    //到期前一天
                    content = redisMap.get("sms.service.repayRemind");
                } else {
                    //content = yunFengMsgUtil.getRepayOverdue();
                    //逾期短信
                    content = redisMap.get("sms.service.repayOverdue");
                }
                if (content != null) {
                    if (!"overdue".equals(type)) {
                        BigDecimal repaymentAmount = new BigDecimal(orderRepayment.getRepaymentAmount() / 100.0);
                        content = content.replace("#userName#", "客户")
                                .replace("#money#", repaymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "")
                                .replace("#WeChat#", newappBase.getServicePhone()).replace("#increaseMoney#", increaseMoney);
                    } else {
                        Date repaymentDate = orderRepayment.getRepaymentTime();
                        String strD1 = dateFormat.format(repaymentDate);
                        String strD2 = dateFormat.format(new Date());
                        Date d1 = dateFormat.parse(strD1);
                        Date d2 = dateFormat.parse(strD2);
                        //计算逾期的时间,微妙级别
                        long diff = d2.getTime() - d1.getTime();
                        long days = diff / (1000 * 60 * 60 * 24);
                        BigDecimal repaymentAmount = new BigDecimal(orderRepayment.getRepaymentAmount() / 100.0);
                        content = content.replace("#userName#", "客户")
                                .replace("#money#", repaymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "")
                                .replace("#day#", String.valueOf(days)).replace("#WeChat#", newappBase.getServicePhone());
                    }
                } else {
                    log.error("content is null");
                    return;
                }
                if (!smsService.batchSendRemind(user.getPhone(), content)) {
                    log.info("还款短信发送失败--------------repaySms phone ={}", user.getPhone());
                }
                UserShortMessage shortMessage = new UserShortMessage();
                shortMessage.setPhone(user.getPhone());
                shortMessage.setCreatTime(new Date());
                shortMessage.setMessageContent(content);
                shortMessage.setUserId(user.getId());
            }
        } catch (Exception e) {
            log.error("send sms error", e);
        }
        log.info("短信提醒任务结束---------->repaySms end 共" + repayments.size());
    }

    @Override
    public void statisticCollect(Date date) {
        Map<String, Object> map = new HashMap<>();
        String dateFormat = DateUtil.getDateFormat(date, "yyyy-MM-dd");
        map.put("beginTime", dateFormat);
        map.put("endTime", dateFormat);
        List<CountCollectionAssessment> res = orderCollectionDAO.statisticByDay(map);
        if (CollectionUtils.isNotEmpty(res)) {
            for (CountCollectionAssessment re : res) {
                re.setCountDate(date);
            }
            countCollectionAssessmentDAO.insertList(res);
        }
    }

    /**
     * 征信审核
     */
    @Override
    public void autoReview() {
        try {
            HashMap<String, Object> params = new HashMap<>();
            Integer size = 5;
            params.put("pageSize", size);

            int totalPageNum = riskCreditUserDao.findWaitToGetAnalyseRiskCreditUserCount();
            int total = 0;
            if (totalPageNum > 0) {
                if (totalPageNum % size > 0) {
                    total = totalPageNum / size + 1;
                } else {
                    total = totalPageNum / size;
                }
            }
            for (int i = 0; i < total; i++) {
                params.put("offSet", i * size);
                List<HashMap<String, String>> riskCreditUserMapList = riskCreditUserDao.findWaitToGetAnalyseRiskCreditUser(params);
                if (CollectionUtils.isEmpty(riskCreditUserMapList)) {
                    break;
                }
                for (HashMap<String, String> riskCreditUser : riskCreditUserMapList) {
                    try {
                        //进行排序风控模型查询
                        riskCreditUserService.review(Integer.parseInt(riskCreditUser.get("id")), Integer.parseInt(riskCreditUser.get("userId")), Integer.parseInt(riskCreditUser.get("assetId")));
                    } catch (Exception e) {
                        log.error("updateAnalysis,e={},params={}" + e.getMessage() + params);
                    }
                }
            }
        } catch (Exception e) {
            log.error("updateAnalysis error ", e);
        }
    }

    @Override
    public void overdue() {
        log.info("逾期任务开始------------------>overdue start");
        Map<String, Object> params = new HashMap<>();
        Byte[] bytes = new Byte[]{Constant.REPAYMENT_STATUS_INIT, Constant.REPAYMENT_STATUS_PART_PAID, Constant.REPAYMENT_STATUS_OVERDUE, Constant.REPAYMENT_STATUS_DIRTY};
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String repaymentEndTime = dateFormat.format(date);
        Date repaymentTimeStart = DateUtil.dateSubtraction(date, -30);
        params.put("statuses", bytes);
        params.put("repaymentEndTime", repaymentEndTime);
        params.put("repaymentTimeStart", repaymentTimeStart);
        List<OrderRepayment> repayments = repaymentDAO.findByRepaymentSmsRemind(params);
        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(Constant.CHANNELRULE_ID);
        for (OrderRepayment repayment : repayments) {
            try {
                //  OrderRepayment orderRepayment = repaymentService.selectByPrimaryKey(repayment.getId());
                if (!repayment.getStatus().equals(Constant.REPAYMENT_STATUS_PAID) && !repayment.getStatus().equals(Constant.REPAYMENT_STATUS_PAID_OVERDUE)) {
                    repaymentService.overdue(repayment, loanConfig);
                }
            } catch (Exception e) {
                log.error("taskJob overdue error repaymentId = " + repayment.getId(), e);
                continue;
            }
        }
        log.info("逾期任务结束---------->overdue end 共 " + repayments.size());
    }

    @Override
    public void autoDispatchWaiter() {
        Map<String, Object> map = new HashMap<>();
        map.put("due", 1);
        // 已在当日催收表里的订单不再分配，防止重复
        List<Integer> repaymentIds = orderCollectionCallerDAO.selectToday();
        if (CollectionUtils.isNotEmpty(repaymentIds)) {
            map.put("list", repaymentIds);
        }
        // 查询当日应还订单
        List<OrderRepayment> repayments = orderRepaymentMapper.selectSimple(map);
        dispatchforWaiter(repayments);
    }

    private void dispatchforWaiter(List<OrderRepayment> repayments) {
        log.info("dispatchforWaiter start");
        // 初始化参数
        Date now = new Date();
        String sysName = "系统";
        String remark = "待跟进";
        // 和处理步骤对应的订单以及客服列表
        List<OrderCollectionCaller> orderCollectionCallers = new ArrayList<>();
        // 续借客户订单直接让以前的分到的当日催收人员跟进，不用再重新分配
        List<OrderCollectionCaller> orders = new ArrayList<>();
        // 查询当日催收人员
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", Constant.ROLEID_COLLECTION_TODAY);
        map.put("status", 0);
        List<PlatformUser> callers = platformUserMapper.selectSimple(map);
        if (CollectionUtils.isEmpty(callers)) {
            PlatformUser platformUser = new PlatformUser();
            platformUser.setId(0);
            callers.add(platformUser);
        }
        if (CollectionUtils.isNotEmpty(repayments)) {
            for (OrderRepayment repayment : repayments) {
                if (null == repayment.getBorrowId()) {
                    continue;
                }
                // 根据还款订单查询当日催收人员催款列表是否已有
                OrderCollectionCaller orderCollectionCaller = orderCollectionCallerDAO.selectByRepaymentId(repayment.getId());
                if (orderCollectionCaller == null) {
                    orderCollectionCaller = new OrderCollectionCaller();
                    orderCollectionCaller.setRepaymentId(repayment.getId());
                    orderCollectionCaller.setUserId(repayment.getUserId());
                    orderCollectionCaller.setDispatchTime(now);
                    orderCollectionCaller.setOperatorName(sysName);
                    orderCollectionCaller.setRemark(remark);
                    orderCollectionCallers.add(orderCollectionCaller);
                } else {
                    orderCollectionCaller.setId(null);
                    orderCollectionCaller.setDispatchTime(now);
                    orderCollectionCaller.setCurrentCollectionUserId(orderCollectionCaller.getCurrentCollectionUserId());
                    orderCollectionCaller.setStatus((byte) 0);
                    orderCollectionCaller.setPromiseRepaymentTime(null);
                    orderCollectionCaller.setLastCollectionTime(null);
                    orderCollectionCaller.setRemark(remark);
                    orderCollectionCaller.setAllocationNumber(orderCollectionCaller.getAllocationNumber() + 1);
                    orders.add(orderCollectionCaller);
                }
            }
        }
        if (orders.size() > 0) {
            // 续借客户订单批量分配
            orderCollectionCallerDAO.insertBatchSelective(orders);
            log.info("1.客户订单批量分配" + orders.size() + "条，", DateUtil.formatTimeYmdHms(new Date()));
        }
        // 将订单派到对应分组当日催收人员
        orderCollectionService.assignforWaiter(orderCollectionCallers, callers, now);
        log.info("TaskJobMiddleService dispatchforWaiter end");
    }

    @Override
    public void reviewDispatch() {
        log.info("待审核订单分配给审核人员定时任务开始");
        //查询未分配给审核人员的待人工审核订单
        List<OrderBorrow> borrows = orderBorrowMapper.findPendingReview();
        //查询审核人员
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", Constant.ROLEID_REVIEWER);
        map.put("status", 0);
        List<PlatformUser> callers = platformUserMapper.selectSimple(map);
        //将订单派到对应分组审核人员
        orderBorrowService.reviewDispatch(borrows, callers);
        log.info("待审核订单分配给审核人员定时任务结束");
    }

    @Override
    public void updateCollectionStatistics(Date now, String beginTime, String endTime) {
        //查询当日催收催回  应是正常还款，逾期还款属于逾期催收人员催回
        Map<String, Object> params = new HashMap<>();
        String date = DateUtil.formatTimeYmd(now);
        params.put("nowTime", date);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        // 根据时间查询当日催收催回情况（直接查询一天总的，因为订单可能会被转派）
        List<CollectionStatistics> list = orderCollectionCallerDAO.selectRepaymentByTime(params);
        // 根据时间查询当日催收催回的续期
        List<CollectionStatistics> list1 = orderCollectionCallerDAO.selectRenewalByTime(params);
        // 将续期笔数金额加入还款
        for (CollectionStatistics sta : list1) {
            for (CollectionStatistics statistics : list) {
                if (statistics.getUserId().equals(sta.getUserId())) {
                    statistics.setRepaymentNumber(statistics.getRepaymentNumber() + sta.getRepaymentNumber());
                    statistics.setRepaymentAmount(statistics.getRepaymentAmount().add(sta.getRepaymentAmount()));
                }
            }
        }

        for (CollectionStatistics statistics : list) {
            params.clear();
            params.put("countDate", date);
            params.put("userId", statistics.getUserId());
            CollectionStatistics coll = collectionStatisticsMapper.selectByDateAndUserId(params);
            if (coll == null) {
                statistics.setCountDate(now);
                statistics.setRepaymentNumberRate(BigDecimal.valueOf(statistics.getRepaymentNumber()).divide(BigDecimal.valueOf(statistics.getDistributionNumber()), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                statistics.setRepaymentAmountRate(statistics.getRepaymentAmount().divide(statistics.getDistributionAmount(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                collectionStatisticsMapper.insertSelective(statistics);
            } else {
                if (statistics.getRepaymentNumber() != 0) {
                    coll.setDistributionNumber(statistics.getDistributionNumber());
                    coll.setDistributionAmount(statistics.getDistributionAmount());
                    coll.setRepaymentNumber(statistics.getRepaymentNumber());
                    coll.setRepaymentAmount(statistics.getRepaymentAmount());
                    coll.setRepaymentNumberRate(BigDecimal.valueOf(statistics.getRepaymentNumber() * 100).divide(BigDecimal.valueOf(coll.getDistributionNumber()), 2, RoundingMode.HALF_UP));
                    coll.setRepaymentAmountRate(statistics.getRepaymentAmount().multiply(BigDecimal.valueOf(100)).divide(coll.getDistributionAmount(), 2, RoundingMode.HALF_UP));
                    collectionStatisticsMapper.updateByPrimaryKeySelective(coll);
                }
            }
        }
        log.info("当日催收统计更新结束");
    }

    @Override
    public void updateCollectionStatisticsOverdue(Date now, String beginTime, String endTime) {
        //查询逾期催收催回

//        List<OrderRepayment> repayments = repaymentService.selectRepaymentByPaidTime(params);
//        if(CollectionUtils.isEmpty(repayments)) {
//            log.info("该时间段无催收还款");
//            return;
//        }
        // 根据时间查询逾期催收催回情况
        Map<String, Object> map = new HashMap<>();
        String date = DateUtil.formatTimeYmd(now);
        map.put("beginTime", date);
        map.put("endTime", date);
        // 根据时间查询逾期催收分配数
        List<CollectionStatistics> res = orderCollectionDAO.selectByTime(map);
        if (CollectionUtils.isEmpty(res)) {
            log.info("该时间段无催收任务");
            return;
        }
        // 将分配给催收人员的任务数存储到数据库
        for (CollectionStatistics statistics : res) {
            Map<String, Object> params = new HashMap<>();
            params.put("countDate", date);
            params.put("userId", statistics.getUserId());
            CollectionStatistics coll = collectionStatisticsMapper.selectByDateAndUserId(params);
            if (coll == null) {
                statistics.setCountDate(now);
                collectionStatisticsMapper.insertSelective(statistics);
            }
        }

        //根据时间查询逾期催收催回数
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        List<CollectionStatistics> list = orderCollectionDAO.selectRepaymentByTime(params);
        for (CollectionStatistics statistics : res) {
            params.clear();
            params.put("countDate", date);
            params.put("userId", statistics.getUserId());
            CollectionStatistics coll = collectionStatisticsMapper.selectByDateAndUserId(params);
            for (CollectionStatistics stat : list) {
                if (statistics.getUserId().equals(stat.getUserId())) {
                    if (stat.getRepaymentNumber() != 0) {
                        coll.setDistributionNumber(statistics.getDistributionNumber());
                        coll.setDistributionAmount(statistics.getDistributionAmount());

                        Integer repaymentNumber = stat.getRepaymentNumber();
                        BigDecimal repaymentAmount = stat.getRepaymentAmount();
                        coll.setRepaymentNumber(repaymentNumber);
                        coll.setRepaymentAmount(repaymentAmount);
                        coll.setRepaymentNumberRate(BigDecimal.valueOf(repaymentNumber).divide(BigDecimal.valueOf(coll.getDistributionNumber()), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                        coll.setRepaymentAmountRate(repaymentAmount.divide(coll.getDistributionAmount(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                        collectionStatisticsMapper.updateByPrimaryKeySelective(coll);
                    }
                }
            }
        }
        log.info("逾期催收统计更新结束");
    }

    private void dispatchforLoanId(List<OrderRepayment> repayments) {
        log.info("dispatchforLoanId start");
        //初始化参数
        Date now = new Date();
        String sysName = "系统";
        String sysRemark = "系统派单";
        String sysPromoteRemark = "逾期升级，系统重新派单";

        //和处理步骤对应的订单以及催收员列表
        List<OrderCollection> mmanLoanCollectionOrderNo131List = new ArrayList<OrderCollection>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo131List =
                getMmanLoanCollectionPeople(Constant.XJX_OVERDUE_LEVEL_S1);
        List<OrderCollection> mmanLoanCollectionOrderNo132List = new ArrayList<OrderCollection>();
        List<MmanLoanCollectionPerson> mmanLoanCollectionPersonNo132List = getMmanLoanCollectionPeople(Constant.XJX_OVERDUE_LEVEL_S2);
        if (CollectionUtils.isNotEmpty(repayments)) {
            for (OrderRepayment repayment : repayments) {
                if (null == repayment.getBorrowId()) {
                    continue;
                }

                try {
                    //更新借款
                    OrderBorrow orderBorrow = orderBorrowMapper.selectByPrimaryKey(repayment.getBorrowId());
                    if (null == orderBorrow) {
                        log.error("orderBorrow is null ,repaymentId:" + repayment.getId());
                        continue;
                    }
                    BigDecimal loanMoney = new BigDecimal(orderBorrow.getApplyAmount());//总应还额（借款额+罚息金额）
                    if (1 == repayment.getCollected()) {
                        OrderRepayment orderRepayment = orderRepaymentMapper.selectByBorrowId(orderBorrow.getId());
                        int pday = 0;
                        if (orderRepayment != null && orderRepayment.getRepaymentTime() != null) {
                            pday = DateUtil.daysBetween(orderRepayment.getRepaymentTime(), new Date());
                        }
                        BigDecimal pRate = new BigDecimal((orderBorrow.getLateFeeApr() / 10000.0));//罚息率
//                        BigDecimal pmoney = (loanMoney.multiply(pRate).multiply(new BigDecimal(pday))).setScale(2, BigDecimal.ROUND_HALF_UP);//逾期金额（部分还款算全罚息)
//
//                        //对于逾期了多次还款的状况，还够本金则不算新罚息，计算到上次罚息值即可（如借1000，罚息120，上次还了1100，本次补足剩余20即可，罚息依然是120）
//                        repayment.setLateFee(pmoney.intValue());//逾期滞纳金 DecimalFormatUtil.df2Points.format(pmoney.doubleValue())

                        //外层循环处理时间随着数据量增长会越来越长，防止更新滞纳金（罚息）时覆盖更新已还款的数据
//                        OrderRepayment mmanUserLoanForUpdate = new OrderRepayment();
//                        mmanUserLoanForUpdate.setId(repayment.getId());
//                        mmanUserLoanForUpdate.setLateFee(repayment.getLateFee());
//                        orderRepaymentMapper.updateByPrimaryKeySelective(mmanUserLoanForUpdate);
                        //更新还款表中滞纳金
                        //逾期天数大于等于3派给s2
                        if (pday > Constant.COLLECT_LEVEL_DAY) {

//                            log.info("mmanLoancollectionOrder redispatch enter s2 mmanUserLoanOri ,repaymentId:" + repayment.getId());

                            Map<String, Object> map = new HashMap<>();
                            map.put("borrowId", orderBorrow.getId());
                            List<OrderCollection> mmanLoanCollectionOrderList =
                                    orderCollectionDAO.selectSimple(map);


                            if (CollectionUtils.isEmpty(mmanLoanCollectionOrderList)) {


                                OrderCollection mmanLoanCollectionOrderNo132 = new OrderCollection();
                                mmanLoanCollectionOrderNo132.setBorrowId(orderBorrow.getId());
                                mmanLoanCollectionOrderNo132.setOrderNo(orderBorrow.getOutTradeNo());
                                mmanLoanCollectionOrderNo132.setUserId(orderBorrow.getUserId());
                                mmanLoanCollectionOrderNo132.setOverdueDay(pday);
                                mmanLoanCollectionOrderNo132.setRepaymentId(repayment.getId());
                                mmanLoanCollectionOrderNo132.setOperatorName(sysName);
                                mmanLoanCollectionOrderNo132.setDispatchTime(now);
                                mmanLoanCollectionOrderNo132.setRemark(sysRemark);
                                mmanLoanCollectionOrderNo132List.add(mmanLoanCollectionOrderNo132);


                            } else {

                                //原订单不在条件内的只需更新逾期天数
                                OrderCollection mmanLoanCollectionOrderOri = mmanLoanCollectionOrderList.get(0);
                                if (1 == mmanLoanCollectionOrderOri.getDeleted() || Constant.XJX_COLLECTION_ORDER_STATE_SUCCESS == mmanLoanCollectionOrderOri.getStatus()) {
//                                    log.info("mmanLoancollectionOrder renewStatus is deleted,do not dispatch,id:" + mmanLoanCollectionOrderOri.getId());
                                    continue;
                                }
                                mmanLoanCollectionOrderOri.setOverdueDay(pday);
                                HashMap<String, Object> params = new HashMap<>();
                                params.put("currentCollectionUserId", mmanLoanCollectionOrderOri.getCurrentCollectionUserId());
                                params.put("orderId", mmanLoanCollectionOrderOri.getOrderNo());
                                int countSinge = mmanLoanCollectionStatusChangeLogDao.findOrderSingle(params);//判断催收员是否转派过改单
                                //1.3.2 S1中逾期天数 大于2的已存在未完成订单，分组升级为S2
                                if ((Constant.XJX_OVERDUE_LEVEL_S1.equals(mmanLoanCollectionOrderOri.getCurrentOverdueLevel() + ""))
                                        && pday > Constant.COLLECT_LEVEL_DAY && !checkLog(mmanLoanCollectionOrderOri.getOrderNo())
                                        && Constant.XJX_COLLECTION_ORDER_STATE_SUCCESS != mmanLoanCollectionOrderOri.getStatus()
                                        && Constant.XJX_COLLECTION_ORDER_STATE_PAYING != mmanLoanCollectionOrderOri.getStatus() && countSinge <= 0) {

                                    mmanLoanCollectionOrderOri.setDispatchTime(now);
                                    mmanLoanCollectionOrderOri.setOperatorName(sysName);
                                    mmanLoanCollectionOrderOri.setRemark(sysPromoteRemark);
                                    mmanLoanCollectionOrderOri.setLastCollectionUserId(mmanLoanCollectionOrderOri.getCurrentCollectionUserId());//上一催收员

                                    mmanLoanCollectionOrderNo132List.add(mmanLoanCollectionOrderOri);


                                } else {
                                    //原订单不在条件内的只需更新逾期天数
                                    orderCollectionDAO.updateByPrimaryKeySelective(mmanLoanCollectionOrderOri);
                                }
                            }
                        } else {

                            //1.3 若当前为每月12号-月底，订单和所有分组要分多种情况

                            //1.3.1 所有新订单，分组为S1
                            Map<String, Object> map = new HashMap<>();
                            map.put("borrowId", orderBorrow.getId());
                            List<OrderCollection> mmanLoanCollectionOrderList =
                                    orderCollectionDAO.selectSimple(map);
                            if (CollectionUtils.isEmpty(mmanLoanCollectionOrderList)) {

                                OrderCollection mmanLoanCollectionOrderNo131 = new OrderCollection();
                                mmanLoanCollectionOrderNo131.setBorrowId(orderBorrow.getId());
                                mmanLoanCollectionOrderNo131.setOrderNo(orderBorrow.getOutTradeNo());
                                mmanLoanCollectionOrderNo131.setUserId(orderBorrow.getUserId());
                                mmanLoanCollectionOrderNo131.setOverdueDay(pday);
                                mmanLoanCollectionOrderNo131.setRepaymentId(repayment.getId());
                                mmanLoanCollectionOrderNo131.setDispatchTime(now);
                                mmanLoanCollectionOrderNo131.setOperatorName(sysName);
                                mmanLoanCollectionOrderNo131.setRemark(sysRemark);
                                mmanLoanCollectionOrderNo131List.add(mmanLoanCollectionOrderNo131);

                            } else {
                                OrderCollection mmanLoanCollectionOrderOri = mmanLoanCollectionOrderList.get(0);

                                if (1 == mmanLoanCollectionOrderOri.getDeleted() || Constant.XJX_COLLECTION_ORDER_STATE_SUCCESS == mmanLoanCollectionOrderOri.getStatus()) {
                                    continue;
                                }
                                mmanLoanCollectionOrderOri.setOverdueDay(pday);
                                //原订单不在条件内的只需更新逾期天数
                                orderCollectionDAO.updateByPrimaryKeySelective(mmanLoanCollectionOrderOri);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("分配当前催收任务出错，借款ID：" + repayment.getBorrowId(), e);
                }
            }
        }


        //2 将订单派到对应分组催收员
        orderCollectionService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo131List, mmanLoanCollectionPersonNo131List, now);
        if (!"10001".equals(Pid)) {

            orderCollectionService.assignCollectionOrderToRelatedGroup(mmanLoanCollectionOrderNo132List, mmanLoanCollectionPersonNo132List, now);
        }

        log.info("TaskJobMiddleService dispatchForLoanId end");
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
     * 根据派单订单id,查询是否有系统逾期升级至S2的转派日志
     *
     * @param loanCollectionOrderId
     * @return
     */
    public boolean checkLog(String loanCollectionOrderId) {
        HashMap<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("collectionOrderId", loanCollectionOrderId);
        int count = null == mmanLoanCollectionStatusChangeLogDao.findSystemUpToS2Log(paraMap) ? 0 : mmanLoanCollectionStatusChangeLogDao.findSystemUpToS2Log(paraMap).intValue();

        if (count > 0) {
            return true;
        }

        return false;

    }


    @Override
    public Map<String, Object> findbalance() {
        //查询余额接口
        String resultInfo = HttpUtil.doGet(centerUrl + "/getBalance?pid=" + pid);
        //余额默认大于0
        Map<String, Object> dataMap = null;
        log.info("定时推送风控查询余额----返回结果-------resultInfo={}", resultInfo);
        if (StringUtils.isNotBlank(resultInfo)) {
            //把当前的余额返存在redis中，在风控那里进行判断余额，如果余额少于0，就不允许再进行风控调用
            JSONObject resultMap = JSONObject.parseObject(resultInfo);
            Object code = resultMap.get("code");
            if (null != code) {
                if ("200".equals(code.toString())) {
                    Object data = resultMap.get("data");
                    if (null != data) {
                        dataMap = JSONObject.parseObject(data.toString());
                        redisUtil.set(Constant.RISK_BALANCE + pid, JSONObject.toJSONString(dataMap));
                        leftRemind(dataMap);
                    }
                }
            }
        }
        return dataMap;
    }

    @Async
    public void leftRemind(Map<String, Object> balanceMap) {
        try {
            //商户状态
            Object status = balanceMap.get("status");
            //余额
            Object balance = balanceMap.get("balance");
            //风控价格
            Object riskPrice = balanceMap.get("riskPrice");
            if (null != status) {
                //商户状态正常，继续判断余额
                int state = Integer.parseInt(status.toString());
                if (0 == state) {
                    if (null != balance && null != riskPrice) {
                        BigDecimal balanceDec = new BigDecimal(balance.toString());
                        if (balanceDec.intValue() <= 1000) {
                            log.info("风控余额小于1000");
                            String redis_repay_id = redisUtil.get(PREFIX + FKYE);
                            if (StringUtils.isBlank(redis_repay_id)) {
//风控余额短信通知
                                redisUtil.set(PREFIX + FKYE, "true");
                                Map<String, Object> mm = new HashMap<>();
                                mm.put("roleId", Constant.ROLEID_SUPER);
                                List<PlatformUser> byRoleName = platformUserMapper.selectSimple(mm);
                                if (CollectionUtils.isNotEmpty(byRoleName)) {
                                    log.info("风控余额小于1000 ,发短信通知人数=" + byRoleName.size());
                                    Map<String, String> redisMap = smsService.getRedisMap();
                                    String smsContent = redisMap.get("sms.service.risk");
                                    for (PlatformUser platformUser : byRoleName) {
                                        smsService.batchSend(platformUser.getPhoneNumber(), smsContent);
                                        Thread.sleep(1000);
                                    }
                                }
                            }
                        } else {
                            String redis_repay_id = redisUtil.get(PREFIX + FKYE);
                            if (StringUtils.isNotBlank(redis_repay_id)) {
                                log.info("风控余额已充值,删除redis");
                                redisUtil.del(PREFIX + FKYE);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("leftRemind error=", e.getMessage());
        }
    }

    @Override
    public void pushOverdueInformation() {
        List<Map<String, String>> userList = orderRepaymentMapper.selectByLateDay(1);
        if (CollectionUtils.isEmpty(userList)) {
            log.info("无逾期订单可推送到数据中心");
            return;
        }
        // 将逾期的用户订单信息推到数据中心
        juGuangApi.uploadJointDebt(userList, JuGuangEnum.TODAY.getValue(),1);

        // 将逾期的用户订单信息推到黑名单
        List<Map<String, String>> BlackList = orderRepaymentMapper.selectByLateDay(5);
        if (CollectionUtils.isEmpty(BlackList)) {
            log.info("无黑名单可推送到数据中心");
            return;
        }
        juGuangApi.insertBlack(BlackList);

        // 将今日到期的用户订单信息推到聚光
        List<Map<String, String>> TodayList = orderRepaymentMapper.selectTodayOrder();
        if (CollectionUtils.isEmpty(TodayList)) {
            log.info("无今日到期可推送到数据中心");
            return;
        }
        juGuangApi.uploadJointDebt(TodayList, JuGuangEnum.TODAY.getValue(),0);
        //String res = HttpUtil.doPost(dataUrl + "borrowInfo/overdueBorrowInfo", param);
        log.info("逾期时将订单状态同步到数据中心返回参数------res={}", "res");
    }

    @Override
    public Integer deleteByTime(Date now) {
        return collectionStatisticsMapper.deleteByTime(DateUtil.formatTimeYmd(now));
    }

}
