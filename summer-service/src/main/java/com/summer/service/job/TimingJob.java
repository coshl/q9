package com.summer.service.job;

import com.alibaba.fastjson.JSON;
import com.summer.api.service.*;
import com.summer.api.service.channel.IChannelCountRepaymentService;
import com.summer.api.service.risk.IRiskRuleConfigService;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.enums.OcrStatusEnum;
import com.summer.service.impl.MerchantService;
import com.summer.service.impl.UserOcrService;
import com.summer.service.mq.OrderProducer;
import com.summer.service.sms.ISmsService;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import com.summer.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class TimingJob {
    @Value("${system.prefix}")
    private String PREFIX;
    @Resource
    private IReportRepaymentService reportRepaymentService;
    @Resource
    private IChannelCountRepaymentService channelCountRepaymentService;
    @Resource
    private ITaskjobService taskjobService;
    @Resource
    private ILoanReportService loanReportService;
    @Resource
    private UserOcrService userOcrService;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderProducer orderProducer;
    @Resource
    private MerchantService merchantService;

    @Resource
    private UserInfoMapper userInfoMapper;


    @Resource
    private RedisUtil redisUtil;
    @Value(value = "${spring.profiles.active}")
    private String env;


//    /**
//     * 自动放款
//     *
//     * @throws Exception
//     */
//    @Scheduled(cron = "0 */2 * * * ? ")
//    public void autoLoan(){
//        boolean getLock = false;
//        try {
//            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
//            if (getLock = RedissLockUtil.tryLock(PREFIX + "autoLoan", 0, 120)) {
//                log.info("autoLoan start");
//                OrderBorrow order= orderBorrowMapper.selectOneLoan();
//                if(null!= order){
//                    String s = order.getId().toString();
//                    log.info("order ="+order.toString());
//                    String time = redisUtil.get("loanTime" + s);
//                    if(StringUtils.isBlank(time)){
//                        redisUtil.set("loanTime" + s,"2");
//                    }else if("2".equals(time)) {
//                        redisUtil.set("loanTime" + s,"3");
//                    }else if("3".equals(time)) {
//                        //次数控制
//                        order.setHitRiskTimes((byte)1);
//                        orderBorrowMapper.updateByPrimaryKeySelective(order);
//                        redisUtil.del("loanTime" + s);
//                    }else {
//                        return;
//                    }
//                    orderBorrowService.autoLoan(s);
//                }
//            }else {
//                log.info("------------------------other is autoLoan");
//            }
//        } catch (Exception e) {
//            log.error("autoLoan error" + e.getMessage());
//        } finally {
//            if (!getLock) {
//                return;
//            }
//            RedissLockUtil.unlock(PREFIX + "autoLoan");
//        }
//    }

    /**
     * 逾期订单自动派单
     */
    @Scheduled(cron = "0 35 1 * * ? ")
    public void autoDispatch() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "autoDispatch", 0, 600)) {
                log.info("autoDispatch start");
                taskjobService.handleDispatchTask();
                //删表
                String tablelastName = DateUtil.getDateFormat(DateUtil.addDay(new Date(), -3), "yyyyMMdd");
            }
        } catch (Exception e) {
            log.error("autoDispatch error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "autoDispatch");
        }
    }

    /**
     * 首页报表统计
     */
    @Scheduled(cron = "0 0 4 * * ? ")
    public void indexStatistic() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "indexStatistic", 0, 600)) {
                log.info("indexStatistic start");
                taskjobService.statisticIndex(new Date(), true);
            }
        } catch (Exception e) {
            log.error("indexStatistic error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "indexStatistic");
        }
    }

    /**
     * 催收统计
     */
    @Scheduled(cron = "0 0 3 * * ? ")
    public void collectCount() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "collectCount", 0, 600)) {
                log.info("collectCount start");
                taskjobService.statisticCollect(DateUtil.addDay(new Date(), -1));
            }
        } catch (Exception e) {
            log.error("collectCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "collectCount");
        }
    }

    /**
     * 还款统计
     */
    @Scheduled(cron = "0 5 0 * * ? ")
    public void repayCount() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "repayCount", 0, 600)) {
                log.info("repayCount start");
                String date = DateUtil.getDateFormat(DateUtil.addDay(new Date(), -1), "yyyy-MM-dd");
                //生成或更新前一天还款统计
                reportRepaymentService.reportRepaymentToday(date, 1);
                //生成或更新前一天实际还的逾期或提前还款的统计
//                reportRepaymentService.reportRepayment(date);
                //生成或更新本日二次及以上的续期
                reportRepaymentService.renewalToday(date);
            }
        } catch (Exception e) {
            log.error("repayCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "repayCount");
        }
    }


    /**
     * 还款提前一天短信通知
     */
    //@Scheduled(cron = "30 0 12 * * ? ")
    public void repaySmsRemind() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "repaySmsRemind", 0, 600)) {
                taskjobService.repaySmsRemind();
            }
        } catch (Exception e) {
            log.error("repaySmsRemind error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "repaySmsRemind");
        }
    }

    /**
     * 还款当天短信通知  每天9点 12点 17点发送用户借款到期短信
     */
    //@Scheduled(cron = "30 0 9,12,17 * * ? ")
    public void repaySmsRemindNow() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "repaySmsRemindNow", 0, 600)) {
                taskjobService.repaySmsRemindNow();
            }
        } catch (Exception e) {
            log.error("repaySmsRemindNow error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "repaySmsRemindNow");
        }
    }

    /**
     * 逾期短信通知
     */
    //@Scheduled(cron = "30 0 9,12,17 * * ? ")
    public void repaySmsOverdue() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "repaySmsOverdue", 0, 600)) {
                taskjobService.repaySmsOverdue();
            }
        } catch (Exception e) {
            log.error("repaySmsOverdue error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "repaySmsOverdue");
        }
    }


    /**
     * 每天凌晨一点更新逾期信息
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void overdue() {

        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "overdue", 0, 600)) {
                taskjobService.overdue();

            }
        } catch (Exception e) {
            log.error("overdue error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "overdue");
        }
    }

    /**
     * 每天凌晨一点半更新渠道还款统计
     */
    @Scheduled(cron = "0 30 1 * * ?")
    public void channelRepaymentCount() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "channelRepaymentCount", 0, 600)) {
                Date date = new Date();
                log.info("每天凌晨一点半更新渠道还款统计---------->channelRepaymentCount start date={}", DateUtil.formatTimeYmdHms(date));
                channelCountRepaymentService.channelRepaymentCount(date);

                log.info("每天凌晨一点半更新渠道还款统计---------->channelRepaymentCount end date={}", DateUtil.formatTimeYmdHms(new Date()));
            }
        } catch (Exception e) {
            log.error("channelRepaymentCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "channelRepaymentCount");
        }
    }

    /**
     * 每天2点到23更新渠道还款统计:已还款数量 每30分钟统计一次
     */
    @Scheduled(cron = "0 0/10 2-23 * * ?")
    public void updateChannelRepaymentedCount() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateChannelRepaymentedCount", 0, 300)) {
                Date date = new Date();
                log.info("每天2点到23更新渠道还款统计:已还款数量 每10分钟统计一次---------->updateChannelRepaymentedCount start date={}", DateUtil.formatTimeYmdHms(date));
                channelCountRepaymentService.repaymentCount(date);
                log.info("每天2点到23更新渠道还款统计:已还款数量 每10分钟统计一次---------->updateChannelRepaymentedCount end date={}", DateUtil.formatTimeYmdHms(new Date()));
            }
        } catch (Exception e) {
            log.error("updateChannelRepaymentedCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "updateChannelRepaymentedCount");
        }
    }

    /**
     * 每天23：59更新渠道还款统计 ：已还款数量
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void updateChannelRepaymentedCount2() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateChannelRepaymentedCount2", 0, 600)) {
                Date nowTime = new Date();
                log.info("每天23：59更新渠道还款统计 ：已还款数量统计一次---------->updateChannelRepaymentedCount2 start date={}", DateUtil.formatTimeYmdHms(nowTime));
                channelCountRepaymentService.repaymentCount(nowTime);
                // channelAsyncCountService.pvUvCount(nowTime);
                log.info("每天凌晨一点半更新渠道还款统计---------->updateChannelRepaymentedCount2 end date={}", DateUtil.formatTimeYmdHms(new Date()));
            }
        } catch (Exception e) {
            log.error("updateChannelRepaymentedCount2 error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "updateChannelRepaymentedCount2");
        }
    }

    /**
     * 每10分钟更新一次当日放款、还款统计
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void updateLoanAndRepayment() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateLoanAndRepayment", 0, 300)) {
                log.info("每10分钟更新一次每日放款、还款统计");
                //生成或更新本日放款统计
                loanReportService.statisticLoanReport(0);
                String date = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
                //生成或更新本日还款统计
                reportRepaymentService.reportRepaymentToday(date, 1);
                //生成或更新本日二次及以上的续期
                reportRepaymentService.renewalToday(date);
            }
        } catch (Exception e) {
            log.error("updateLoanAndRepayment error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "updateLoanAndRepayment");
        }
    }


    /**
     * 每1小时的59分59秒时（整点统计0点时会有遗漏）生成或更新以前实际还的逾期或提前还款的统计
     */
    @Scheduled(cron = "59 59 */1 * * ?")
    public void updateRepayment() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateRepayment", 0, 30)) {
                log.info("每1小时更新一次还款统计中逾期或提前还款");
                Date now = new Date();
                String date = DateUtil.getDateFormat(now, "yyyy-MM-dd");
                //获取一小时前的时间
                String beginTime = DateUtil.getDate(now, 60, "yyyy-MM-dd HH:mm:ss");
                String endTime = DateUtil.getDateFormat(now, "yyyy-MM-dd HH:mm:ss");
                //生成或更新以前实际还的逾期或提前还款的统计
                reportRepaymentService.reportRepayment(date, beginTime, endTime);
            }
        } catch (Exception e) {
            log.error("updateRepayment error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "updateRepayment");
        }
    }

    /**
     * 每天凌晨2点半更新渠道还款统计(逾期统计)
     */
    @Scheduled(cron = "0 15 2 * * ?")
    public void channelRepaymentOverdueCount() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "channelRepaymentOverdueCount", 0, 600)) {
                Date date = new Date();
                log.info("每天凌晨2点半更新渠道逾期统计---------->channelRepaymentOverdueCount start date={}", DateUtil.formatTimeYmdHms(date));
                //逾期统计
                channelCountRepaymentService.overdueCount(date);
                log.info("每天凌晨2点半更新渠道逾期统计---------->channelRepaymentCount end date={}", DateUtil.formatTimeYmdHms(new Date()));
            }
        } catch (Exception e) {
            log.error("channelRepaymentCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "channelRepaymentOverdueCount");
        }
    }

    /**
     * 每天凌晨2点半更新渠道还款统计(逾期统计)
     */
    @Scheduled(cron = "0 30 2 * * ?")
    public void customerType() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "customerType", 0, 600)) {
                Date date = new Date();
                log.info("每天凌晨2点半更新新老用户---------->channelRepaymentOverdueCount start date={}", DateUtil.formatTimeYmdHms(date));
                date = DateUtil.dateSubtraction(date, -1);
                String s = DateUtil.formatTimeYmd(date);
                String s1 = redisUtil.get(s + "_customerType");
                if (!StringUtils.isBlank(s1)) {
                    List<OrderBorrow> orderBorrowList1 = JSON.parseArray(s1, OrderBorrow.class);
                    for (OrderBorrow orderBorrow : orderBorrowList1) {
                        UserInfo userCopy = new UserInfo();
                        userCopy.setId(orderBorrow.getUserId());
                        userCopy.setCustomerType(Constant.CUSTOMER_TYPE_OLD);
                        userCopy.setUpdateTime(new Date());
                        userInfoMapper.updateByPrimaryKeySelective(userCopy);
                        orderBorrow = orderBorrowMapper.selectByPrimaryKey(orderBorrow.getId());
                        if (orderBorrow != null) {
                            orderBorrow.setCustomerType(Constant.CUSTOMER_TYPE_OLD);
                            orderBorrowMapper.updateByPrimaryKeySelective(orderBorrow);
                        }
                    }

                }
                log.info("每天凌晨2点半更新新老用户---------->channelRepaymentCount end date={}", DateUtil.formatTimeYmdHms(new Date()));
            }
        } catch (Exception e) {
            log.error("channelRepaymentCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "channelRepaymentOverdueCount");
        }
    }


    /**
     * 每40分钟再统计一次总的逾期的
     */
    @Scheduled(cron = "0 */40 * * * ?")
    public void overdueTotalRe() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "overdueTotalRe", 0, 600)) {
                Date date = new Date();
                channelCountRepaymentService.overdueTotal(date, null);
            }
        } catch (Exception e) {
            log.error("channelRepaymentCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "overdueTotalRe");
        }
    }


    /**
     * 将今日应还款订单自动派单给当日催收
     */
    @Scheduled(cron = "0 15 0 * * ? ")
    public void autoDispatchWaiter() {
        boolean getLock = false;
        try {
            if (getLock = RedissLockUtil.tryLock(PREFIX + "autoDispatchWaiter", 0, 600)) {
                log.info("autoDispatchWaiter start");
                taskjobService.autoDispatchWaiter();
            }
        } catch (Exception e) {
            log.error("autoDispatchWaiter error", e);
            RedissLockUtil.unlock(PREFIX + "autoDispatchWaiter");
        }
    }

    /**
     * 将待审核订单自动派单给审核人员（每15分钟一次）
     */
    @Scheduled(cron = "0 */15 * * * ? ")
    public void reviewDispatch() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "reviewDispatch", 0, 600)) {
                log.info("reviewDispatch start");
                taskjobService.reviewDispatch();
            }
        } catch (Exception e) {
            log.error("reviewDispatch error", e);
            RedissLockUtil.unlock(PREFIX + "reviewDispatch");
        }
    }

    // @Scheduled(cron = "0 */1 * * * ? ")
    public void autoReviewMqJob() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "autoReviewMqJob", 0, 50)) {
                log.info("autoReviewMqJob start");
                //查询一条待审核的订单
                OrderBorrow orderBorrow = orderBorrowMapper.selectBystatus(2);
                if (null != orderBorrow && null != orderBorrow.getId()) {
                    orderProducer.sendRisk(orderBorrow.getId().toString());
                }
                log.info("autoReviewMqJob end");
            } else {
                log.info("------------------------other is reviewing");
            }
        } catch (Exception e) {
            log.error("autoReviewMqJob error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "autoReviewMqJob");
        }
    }


    /**
     * 每15分钟更新一次调用风控花费
     */
//    @Scheduled(cron = "10 */15 * * * ?")
//    public void updateBilling() {
//        boolean getLock = false;
//        try {
//            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
//            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateBilling", 0, 300)) {
//                log.info("每15分钟更新一次调用风控、注册花费统计");
//                taskjobService.updateBilling();
//            }
//        } catch (Exception e) {
//            log.error("updateBilling error" + e.getMessage());
//            RedissLockUtil.unlock(PREFIX + "updateBilling");
//        }
//    }

    /**
     * 每10分钟更新一次当日催收统计
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void updateCollectionStatistics() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateCollectionStatistics", 0, 300)) {
                log.info("每10分钟更新一次当日催收统计");
                Date now = new Date();
                //开始时间为10分钟前的时间
                String beginTime = DateUtil.formatTimeYmd(now);
                String endTime = DateUtil.getDateFormat(now, "yyyy-MM-dd HH:mm:ss");
                taskjobService.updateCollectionStatistics(now, beginTime, endTime);
            }
        } catch (Exception e) {
            log.error("updateCollectionStatistics error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "updateCollectionStatistics");
        }
    }

    /**
     * 每1小时更新一次逾期催收统计
     */
    @Scheduled(cron = "0 5 */1 * * ?")
    public void updateCollectionStatisticsOverdue() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateCollectionStatisticsOverdue", 0, 300)) {
                log.info("每1小时更新一次逾期催收统计");
                Date now = new Date();
                //开始时间为60分钟前的时间
                String beginTime = DateUtil.formatTimeYmd(now);
                String endTime = DateUtil.getDateFormat(now, "yyyy-MM-dd HH:mm:ss");
                taskjobService.updateCollectionStatisticsOverdue(now, beginTime, endTime);
            }
        } catch (Exception e) {
            log.error("updateCollectionStatisticsOverdue error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "updateCollectionStatisticsOverdue");
        }
    }

    /**
     * 订单逾期时将状态推送到聚光数据中心 并推送黑名单
     */
    @Scheduled(cron = "0 20 1 * * ? ")
    public void pushOverdueInformation() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "pushOverdueInformation", 0, 600)) {
                log.info("pushOverdueInformation start");
                taskjobService.pushOverdueInformation();
            }
        } catch (Exception e) {
            log.error("pushOverdueInformation error", e);
            RedissLockUtil.unlock(PREFIX + "pushOverdueInformation");
        }
    }

//    @Scheduled(cron="0/1 * * * * ? ")//每1秒执行一次
//    public void testCron1() {
//        boolean getLock = false;
//        try {
//            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
//            if (getLock = RedissLockUtil.tryLock(PREFIX + "1testCron1", 0, 600)) {
//                log.info("testCron1 start");
//                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); log.info(sdf.format(new Date())+"*********每1秒执行一次");
//                RedissLockUtil.unlock(PREFIX + "1testCron1");
//            }else{
//                log.info("testCron1 fail");
//            }
//        } catch (Exception e) {
//            RedissLockUtil.unlock(PREFIX + "1testCron1");
//            log.error("testCron1 error", e);
//        }
//    }
//    @Scheduled(cron="0/2 * * * * ? ")//每1秒执行一次
//    public void testCron2() {  boolean getLock = false;
//        try {
//            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
//            if (getLock = RedissLockUtil.tryLock(PREFIX + "1testCron2", 0, 600)) {
//                log.info("testCron2 start");
//                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); log.info(sdf.format(new Date())+"*********每2秒执行一次");
//                RedissLockUtil.unlock(PREFIX + "1testCron2");
//            }else{
//                log.info("testCron2 fail");
//            }
//        } catch (Exception e) {
//            RedissLockUtil.unlock(PREFIX + "1testCron2");
//            log.error("testCron2 error", e);
//        } }
//    @Scheduled(cron="0/3 * * * * ? ")//每1秒执行一次
//    public void testCron3() {  boolean getLock = false;
//        try {
//            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
//            if (getLock = RedissLockUtil.tryLock(PREFIX + "1testCron3", 0, 600)) {
//                log.info("testCron3 start");
//                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); log.info(sdf.format(new Date())+"*********每3秒执行一次");
//                RedissLockUtil.unlock(PREFIX + "1testCron3");
//            }else{
//                log.info("testCron3 fail");
//            }
//        } catch (Exception e) {
//            RedissLockUtil.unlock(PREFIX + "1testCron3");
//            log.error("testCron3 error", e);
//        }
//    }

    /**
     * 获取用户认证信息
     */
    //@Scheduled(cron = "0 0/3 * * * ?")
    public void updateOcrDetail() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "updateOcrDetail", 0, 600)) {
                log.info("==================== OCR报告详情定时任务启动 ====================");
                List<UserOcrData> userOcrDataList = userOcrService.listByStatus(OcrStatusEnum.WAIT_AUTH.getValue());
                log.info("待认证报告数量:{}", userOcrDataList.size());
                for (UserOcrData userOcrData : userOcrDataList) {
                    userOcrService.ocrAuth(userOcrData);
                }
            }
            log.info("==================== OCR报告详情定时任务结束 ====================");
        } catch (Exception e) {
            log.error("updateOcrDetail error" + e.getMessage());
        } finally {
            RedissLockUtil.unlock(PREFIX + "updateOcrDetail");
        }
    }


    /**
     * 每1小时更新一次消费统计
     */
    //@Scheduled(cron = "0 30 1 * * ?")
    @Scheduled(cron = "0 55 0/1 * * ?")
    public void insertTotalExpenses() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "insertTotalExpenses", 0, 600)) {
                Date nowTime = new Date();
                log.info("==================== 定时新增消费统计定时任务启动 ====================");
                log.info("每1小时更新一次消费统计一次---------->insertTotalExpenses start date={}", DateUtil.formatTimeYmdHms(nowTime));
                merchantService.inserttotalExpenses();
                log.info("每1小时更新一次消费统计一次---------->insertTotalExpenses end date={}", DateUtil.formatTimeYmdHms(new Date()));
                log.info("==================== 定时新增消费统计定时任务结束 ====================");
            }
        } catch (Exception e) {
            log.error("insertTotalExpenses error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "insertTotalExpenses");
        }
    }


    /**
     * 每1小时更新一次消费统计
     */
    //@Scheduled(cron = "0 30 1 * * ?")
    @Scheduled(cron = "0 55 0/1 * * ?")
    public void insertMoneyCount() {
        boolean getLock = false;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            //calendar.set(Calendar.HOUR_OF_DAY,-24);
            String todayDate = dateFormat.format(calendar.getTime());
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = RedissLockUtil.tryLock(PREFIX + "insertMoneyCount", 0, 600)) {
                Date nowTime = new Date();
                log.info("==================== 定时新增消费统计定时任务启动 ====================");
                log.info("每1小时更新一次消费统计一次---------->insertMoneyCount start date={}", DateUtil.formatTimeYmdHms(nowTime));
                merchantService.insertCount(todayDate);
                log.info("每1小时更新一次消费统计一次---------->insertMoneyCount end date={}", DateUtil.formatTimeYmdHms(new Date()));
                log.info("==================== 定时新增消费统计定时任务结束 ====================");
            }
        } catch (Exception e) {
            log.error("insertMoneyCount error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "insertMoneyCount");
        }
    }


    /**
     * 每天凌晨两点定时同步订单数据至中央库
     */
    //@Scheduled(cron = "0 0 2 * * ?")
    public void synchronizeOrderData() {
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            //log.info("==================== 每天凌晨一点定时同步黑名单数据至中央库 ---------->synchronizeOrderData ====================");
            //if (getLock = RedissLockUtil.tryLock(PREFIX + "synchronizeOrderData", 0, 600)) {
            Date nowTime = new Date();
            log.info("==================== 每天凌晨两点定时同步订单数据至中央库定时任务启动 ====================");
            log.info("每天凌晨两点定时同步订单数据至中央库---------->synchronizeOrderData start date={}", DateUtil.formatTimeYmdHms(nowTime));
            if (!StringUtils.equalsIgnoreCase(env, "dev")) {
                //blackUserService.synchronizeOrderData();
            }
            log.info("每天凌晨两点定时同步订单数据至中央库---------->synchronizeOrderData end date={}", DateUtil.formatTimeYmdHms(new Date()));
            log.info("==================== 每天凌晨两点定时同步订单数据至中央库定时任务结束 ====================");
            //}
        } catch (Exception e) {
            log.error("synchronizeOrderData error" + e.getMessage());
            RedissLockUtil.unlock(PREFIX + "synchronizeOrderData");
        }
    }
}
