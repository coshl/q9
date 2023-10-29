package com.summer.service.impl;

import com.github.pagehelper.PageInfo;
import com.summer.dao.convert.BaseConverter;
import com.summer.dao.entity.RepaymentInfo;
import com.summer.dao.entity.ReportRepayment;
import com.summer.dao.mapper.OrderRenewalMapper;
import com.summer.dao.mapper.ReportRepaymentDAO;
import com.summer.api.service.IReportRepaymentService;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import com.summer.pojo.vo.OrderRenewalVO;
import com.summer.pojo.vo.ReportRepaymentTitleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @program: summer
 * @description: 还款报表数据
 * @author: jql
 * @create: 2019-02-26 16:34
 */
@Slf4j
@Service
public class ReportRepaymentService implements IReportRepaymentService {

    @Resource
    private ReportRepaymentDAO reportRepaymentDAO;

    @Resource
    private OrderRenewalMapper orderRenewalMapper;

    /**
     * 查询还款报表信息
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<ReportRepayment> queryReport(Map<String, Object> params) {
        return new PageInfo<>(reportRepaymentDAO.queryReport(params));
    }

    /**
     * 查询每日还款title信息
     *
     * @param params
     */
    @Override
    public ReportRepaymentTitleVO queryReportTitle(Map<String, Object> params) {
//        List<RepaymentInfo> repayments = reportRepaymentDAO.findByRepaymentReportTitle(params);
//        ReportRepaymentTitleVO repaymentTitleVO = solvedReportRepayment(null, repayments, repaymentStartTime, repaymentStartTime, 0);
//        updateReportRepayment(repaymentTitleVO);
        ReportRepaymentTitleVO repaymentTitleVO = reportRepaymentDAO.findTitle(params);
        return repaymentTitleVO;
    }

    @Override
    @Transactional
    public void reportRepayment(String firstRepaymentTime, String beginTime, String endTime) {
        log.info("每日还款统计任务开始---------->reportRepayment start " + firstRepaymentTime);
        Map<String, Object> params = new HashMap<String, Object>();
        //查询1小时内部分还款、逾期还款
        params.put("paidTime", firstRepaymentTime);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        //查询今日实际还款的还款记录(提前还款，逾期还款)续期
        List<RepaymentInfo> repayments = reportRepaymentDAO.findByRepaymentReport(params);
//        Long repayments1 = reportRepaymentDAO.findByRepaymentReport1(params);//提前还款
        Long repayments3 = reportRepaymentDAO.findByRepaymentReport3(params);//提前还款
        Long repayments2 = reportRepaymentDAO.findByRepaymentReport2(params);//逾期还款
        Long repayments4 = reportRepaymentDAO.findByRepaymentReport4(params);//提前还款
        List<RepaymentInfo> repaymentList = new ArrayList<>();
        if (repayments.size() == 0) {
                params.clear();
                params.put("reportDateStart", firstRepaymentTime);
                //查询本日实际还的以前应还的统计是否已存在
                List<ReportRepayment> reports = reportRepaymentDAO.queryReportInfo(params);
                //已存在，更新数据
                if (reports.size() == 1) {
                    for (ReportRepayment reportRepayment : reports) {
                        reportRepayment.setRepaymentRateS1AmountAll( repayments3);
                        reportRepayment.setRepaymentRateS2AmountAll(repayments2);
                        reportRepayment.setRepaymentRateS3AmountAll(repayments4);
                        reportRepaymentDAO.updateByPrimaryKeySelective(reportRepayment);
                    }
                } else {
                    log.error("每日还款统计异常---------->reportRepayment end " + firstRepaymentTime);
                    return;
                }

        }
        //将还款与渠道匹配
        for (RepaymentInfo repaymentInfo : repayments) {
            repaymentList.add(repaymentInfo);
            params.clear();
            params.put("reportDateStart", DateUtil.getDateFormat(repaymentInfo.getRepaymentTime(), "yyyy-MM-dd"));
            //查询本日实际还的以前应还的统计是否已存在
            List<ReportRepayment> reports = reportRepaymentDAO.queryReportInfo(params);

            //处理报告数据
            ReportRepaymentTitleVO repaymentTitleVO = new ReportRepaymentTitleVO();
            String repaymentTime = DateUtil.getDateFormat(repaymentInfo.getRepaymentTime(), "yyyy-MM-dd");
            if (reports.size() > 0) {
                repaymentTitleVO = solvedReportRepayment(reports.get(0), repaymentList, repaymentTime, firstRepaymentTime, 1, 1);
            } else {
                repaymentTitleVO = solvedReportRepayment(null, repaymentList, repaymentTime, firstRepaymentTime, 1, 1);
            }
            ReportRepayment report = new BaseConverter<ReportRepaymentTitleVO, ReportRepayment>().convert(repaymentTitleVO, ReportRepayment.class);
            report.setRepaymentRateS1AmountAll(repayments3);
            report.setRepaymentRateS2AmountAll(repayments2);
            report.setRepaymentRateS3AmountAll(repayments4);
            //已存在，更新数据
            if (reports.size() == 1) {
                report.setId(reports.get(0).getId());
                reportRepaymentDAO.updateByPrimaryKeySelective(report);
            } else if (reports.size() == 0) {
                //不存在，创建
                reportRepaymentDAO.insertSelective(report);
            } else {
                log.error("每日还款统计异常---------->reportRepayment end " + firstRepaymentTime);
                return;
            }
            repaymentList.clear();
        }
    }

    @Transactional
    @Override
    public void reportRepaymentToday(String firstRepaymentTime, Integer ty) {
        Map<String, Object> params = new HashMap<String, Object>();
        //查询今日应还（首次应还，延期应还）
        params.put("repaymentTime", firstRepaymentTime);
        params.put("paidTime", firstRepaymentTime);
        List<RepaymentInfo> repayments = reportRepaymentDAO.findTodayRepaymentReport(params);
//        Long repayments1 = reportRepaymentDAO.findByRepaymentReport1(params);//提前还款
        Long repayments3 = reportRepaymentDAO.findByRepaymentReport3(params);//提前还款
        Long repayments2 = reportRepaymentDAO.findByRepaymentReport2(params);//逾期还款
        Long repayments4 = reportRepaymentDAO.findByRepaymentReport4(params);//总还款
        params.clear();
        params.put("reportDateStart", firstRepaymentTime);
        //查询本日还款统计是否已存在
        List<ReportRepayment> reports = reportRepaymentDAO.queryReportInfo(params);

        //处理报告数据
        ReportRepaymentTitleVO repaymentTitleVO = new ReportRepaymentTitleVO();
        if (reports.size() > 0) {
            repaymentTitleVO = solvedReportRepayment(reports.get(0), repayments, firstRepaymentTime, firstRepaymentTime, 0, ty);
        } else {
            repaymentTitleVO = solvedReportRepayment(null, repayments, firstRepaymentTime, firstRepaymentTime, 0, ty);
        }
        ReportRepayment report = new BaseConverter<ReportRepaymentTitleVO, ReportRepayment>().convert(repaymentTitleVO, ReportRepayment.class);
        report.setRepaymentRateS1AmountAll(repayments3);
        report.setRepaymentRateS2AmountAll(repayments2);
        report.setRepaymentRateS3AmountAll(repayments4);
        //已存在，更新数据
        if (reports.size() == 1) {
            report.setId(reports.get(0).getId());
            reportRepaymentDAO.updateByPrimaryKeySelective(report);
        } else if (reports.size() == 0) {
            //不存在，创建
            reportRepaymentDAO.insertSelective(report);
        } else {
            log.error("本日还款统计异常---------->reportRepaymentToday end " + firstRepaymentTime);
            return;
        }
    }

    /**
     * 处理每日还款统计报告
     *
     * @param
     * @param
     * @param reportRepayment
     * @param time
     * @param type
     * @param ty
     */
    public ReportRepaymentTitleVO solvedReportRepayment(ReportRepayment reportRepayment, List<RepaymentInfo> repayments, String firstRepaymentTime, String time, int type, Integer ty) {
        //应还款总数量
        Long allRepaymentCount = 0L;
        //前老用户应还款笔数
        Long expireCountOld = 0L;
        //当前新用户应还笔数
        Long expireCountNew = 0L;

        //应还款金额
        Long allRepaymentAmount = 0L;
        //当前老用户应还款金额
        Long expireAmountOld = 0L;
        //当前新用户应还金额
        Long expireAmountNew = 0L;

        //今日正常还款笔数
        Long normalRepaymentCount = 0L;
        //今日正常还款金额
        Long normalRepaymentAmount = 0L;
        //今日老用户正常还款笔数
        Long normalCountOld = 0L;
        //今日老用户正常还款金额
        Long normalAmountOld = 0L;
        //今日新用户正常还款笔数
        Long normalCountNew = 0L;
        //今日新用户正常还款金额
        Long normalAmountNew = 0L;

        //逾期3天内还款总笔数
        Long repaymentS1Count = 0L;
        //逾期3天内还款笔数新
        Long repaymentS1CountNew = 0L;
        //逾期3天内还款笔数老
        Long repaymentS1CountOld = 0L;
        //逾期3天内还款金额
        Long repaymentS1Amount = 0L;
        //逾期3天内还款金额新
        Long repaymentS1AmountNew = 0L;
        //逾期3天内还款金额老
        Long repaymentS1AmountOld = 0L;

        //逾期7天内还款笔数
        Long repaymentS2Count = 0L;
        //逾期7天内还款笔数新
        Long repaymentS2CountNew = 0L;
        //逾期7天内还款笔数老
        Long repaymentS2CountOld = 0L;
        //逾期7天内还款金额
        Long repaymentS2Amount = 0L;
        //逾期7天内还款金额老
        Long repaymentS2AmountOld = 0L;
        //逾期7天内还款金额新
        Long repaymentS2AmountNew = 0L;

        //逾期30天内还款笔数
        Long repaymentS3Count = 0L;
        //逾期30天内还款笔数新
        Long repaymentS3CountNew = 0L;
        //逾期30天内还款笔数老
        Long repaymentS3CountOld = 0L;
        //逾期30天内还款金额
        Long repaymentS3Amount = 0L;
        //逾期30天内还款金额新
        Long repaymentS3AmountNew = 0L;
        //逾期30天内还款金额老
        Long repaymentS3AmountOld = 0L;

        //已经还款数量
        Long allRepayCount = 0L;
        //已经还款总额
        Long allRepayAmount = 0L;

        //非正常还款（逾期、提前）
        if (reportRepayment != null && type == 1) {
            //应还款总数量
            allRepaymentCount = reportRepayment.getAllRepaymentCount();
            //前老用户应还款笔数
            expireCountOld = reportRepayment.getExpireCountOld();
            //当前新用户应还笔数
            expireCountNew = reportRepayment.getExpireCountNew();

            //应还款金额
            allRepaymentAmount = reportRepayment.getAllRepaymentAmount();
            //当前老用户应还款金额
            expireAmountOld = reportRepayment.getExpireAmountOld();
            //当前新用户应还金额
            expireAmountNew = reportRepayment.getExpireAmountNew();

            //今日正常还款笔数
            normalRepaymentCount = reportRepayment.getNormalRepaymentCount();
            //今日正常还款金额
            normalRepaymentAmount = reportRepayment.getNormalRepaymentAmount();
            //今日老用户正常还款笔数
            normalCountOld = reportRepayment.getNormalCountOld();
            //今日老用户正常还款金额
            normalAmountOld = reportRepayment.getNormalAmountOld();
            //今日新用户正常还款笔数
            normalCountNew = reportRepayment.getNormalCountNew();
            //今日新用户正常还款金额
            normalAmountNew = reportRepayment.getNormalAmountNew();

            //逾期3天内还款总笔数
            BigDecimal divide = BigDecimal.valueOf(reportRepayment.getRepaymentRateS1CountAll() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS1Count = divide.longValue();
            //逾期3天内还款笔数新
            BigDecimal divide1 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS1CountNew() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS1CountNew = divide1.longValue();
            //逾期3天内还款笔数老
            BigDecimal divide2 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS1CountOld() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS1CountOld = divide2.longValue();
            //逾期3天内还款金额
            BigDecimal divide3 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS1AmountAll() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS1Amount = divide3.longValue();
            //逾期3天内还款金额新
            BigDecimal divide4 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS1AmountNew() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS1AmountNew = divide4.longValue();
            //逾期3天内还款金额老
            BigDecimal divide5 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS1AmountOld() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS1AmountOld = divide5.longValue();

            //逾期7天内还款笔数
            BigDecimal divide6 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS2CountAll() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS2Count = divide6.longValue();
            //逾期7天内还款笔数新
            BigDecimal divide7 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS2CountNew() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS2CountNew = divide7.longValue();
            //逾期7天内还款笔数老
            BigDecimal divide8 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS2CountOld() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS2CountOld = divide8.longValue();
            //逾期7天内还款金额
            BigDecimal divide9 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS2AmountAll() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS2Amount = divide9.longValue();
            //逾期7天内还款金额老
            BigDecimal divide10 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS2AmountNew() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS2AmountOld = divide10.longValue();
            //逾期7天内还款金额新
            BigDecimal divide11 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS2AmountOld() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS2AmountNew = divide11.longValue();

            //逾期30天内还款笔数
            BigDecimal divide12 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS3CountAll() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS3Count = divide12.longValue();
            //逾期30天内还款笔数新
            BigDecimal divide13 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS3CountNew() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS3CountNew = divide13.longValue();
            //逾期30天内还款笔数老
            BigDecimal divide14 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS3CountOld() * allRepaymentCount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS3CountOld = divide14.longValue();
            //逾期30天内还款金额
            BigDecimal divide15 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS3AmountAll() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS3Amount = divide15.longValue();
            //逾期30天内还款金额新
            BigDecimal divide16 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS3AmountNew() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS3AmountNew = divide16.longValue();
            //逾期30天内还款金额老
            BigDecimal divide17 = BigDecimal.valueOf(reportRepayment.getRepaymentRateS3AmountOld() * allRepaymentAmount).divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP);
            repaymentS3AmountOld = divide17.longValue();

            //已经还款数量
            allRepayCount = reportRepayment.getAllRepayCount();
            //已经还款总额
            allRepayAmount = reportRepayment.getAllRepayAmount();

            //续期还款
            if (DateUtil.parseTimeYmd(time).before(DateUtil.parseTimeYmd(firstRepaymentTime))) {
                //应还款总数量
                allRepaymentCount = reportRepayment.getAllRepaymentCount();
                //前老用户应还款笔数
                expireCountOld = reportRepayment.getExpireCountOld();
                //当前新用户应还笔数
                expireCountNew = reportRepayment.getExpireCountNew();

                //应还款金额
                allRepaymentAmount = reportRepayment.getAllRepaymentAmount();
                //当前老用户应还款金额
                expireAmountOld = reportRepayment.getExpireAmountOld();
                //当前新用户应还金额
                expireAmountNew = reportRepayment.getExpireAmountNew();
            }
        }

        for (RepaymentInfo re : repayments) {
            /*//生成数据时，当日的数据不能有逾期还款的
            if (ty == 100){
                if (re.getStatus() == 4){
                    re.setStatus((byte) 0);
                }
            }*/
            if (re.getCustomerType() == null) {
                log.error("数据库数据错误---------->缺少对应贷款订单");
            }
            //应还款数量、金额当天计算，之后逾期与提前的只计算正常、逾期还款与还款率
            if (reportRepayment == null || type == 0) {
                //新老用户应还 是否是老用户：0:新用户；1:老用户
                if (re.getCustomerType() != null && re.getCustomerType() == 1) {
                    expireCountOld++;
                    expireAmountOld += re.getRepaymentAmount();
                } else if (re.getCustomerType() != null && re.getCustomerType() == 0) {
                    expireCountNew++;
                    expireAmountNew += re.getRepaymentAmount();
                }
                //应还金额
                allRepaymentAmount += re.getRepaymentAmount();
                //应还总数
                allRepaymentCount++;
            }
            int status = re.getStatus();
            boolean b = true;
            //统计今日已还款笔数
            if (Constant.REPAYMENT_STATUS_PAID == status || Constant.REPAYMENT_STATUS_PAID_FARWORD == status || Constant.REPAYMENT_STATUS_PART_PAID == status || Constant.REPAYMENT_STATUS_PAID_OVERDUE == status) {
                b = false;
                if (re.getLateDay() == 0) {
                    //今日正常还款金额
                    normalRepaymentAmount += re.getPaidAmount();
                    //今日正常还款笔数
                    normalRepaymentCount++;
                    // 新旧用户已还数量 0:新用户；1:老用户
                    if (1 == re.getCustomerType()) {
                        normalCountOld++;
                        normalAmountOld += re.getPaidAmount();
                    } else {
                        normalCountNew++;
                        normalAmountNew += re.getPaidAmount();
                    }
                }
                //逾期3天内还款
                if (re.getLateDay() <= 3 && re.getLateDay() > 0) {
                    repaymentS1Count++;
                    repaymentS1Amount += re.getPaidAmount();
                    if (1 == re.getCustomerType()) {
                        repaymentS1CountOld++;
                        repaymentS1AmountOld += re.getPaidAmount();
                    } else {
                        repaymentS1CountNew++;
                        repaymentS1AmountNew += re.getPaidAmount();
                    }
                    //逾期7天内
                } else if (re.getLateDay() <= 7 && re.getLateDay() > 3) {
                    repaymentS2Count++;
                    repaymentS2Amount += re.getPaidAmount();
                    if (1 == re.getCustomerType()) {
                        repaymentS2CountOld++;
                        repaymentS2AmountOld += re.getPaidAmount();
                    } else {
                        repaymentS2CountNew++;
                        repaymentS2AmountNew += re.getPaidAmount();
                    }
                    //逾期30天内
                } else if (re.getLateDay() <= 30 && re.getLateDay() > 7) {
                    repaymentS3Count++;
                    repaymentS3Amount += re.getPaidAmount();
                    if (1 == re.getCustomerType()) {
                        repaymentS3CountOld++;
                        repaymentS3AmountNew += re.getPaidAmount();
                    } else {
                        repaymentS3CountNew++;
                        repaymentS3AmountNew += re.getPaidAmount();
                    }
                }
                //已还数目、金额
                allRepayCount++;
                allRepayAmount += re.getPaidAmount();
            }
        }
        ReportRepaymentTitleVO report = new ReportRepaymentTitleVO();
        report.setReportDate(firstRepaymentTime);
        if (allRepaymentCount > 0 && allRepaymentAmount > 0) {
            //用户正常还款
            //应还款总笔数
            report.setAllRepaymentCount(allRepaymentCount);
            //应还款总金额
            report.setAllRepaymentAmount(allRepaymentAmount);
            //新老用户应还
            report.setExpireCountOld(expireCountOld);
            report.setExpireCountNew(expireCountNew);
            report.setExpireAmountOld(expireAmountOld);
            report.setExpireAmountNew(expireAmountNew);

            //今日正常还款总笔数
            report.setNormalRepaymentCount(normalRepaymentCount);
            //今日正常还款总金额
            report.setNormalRepaymentAmount(normalRepaymentAmount);
            //当前正常还款率（笔数）
            BigDecimal divide = BigDecimal.valueOf(normalRepaymentCount * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepayRate(divide.longValue());
            //当前正常还款率（金额）
            BigDecimal divide1 = BigDecimal.valueOf(normalRepaymentAmount * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepayAmountRate(divide1.longValue());
            //当前老用户正常还款笔数
            report.setNormalCountOld(normalCountOld);
            //当前老用户正常还款金额
            report.setNormalAmountOld(normalAmountOld);
            //老用户正常还款率 按笔数
            BigDecimal divide2 = BigDecimal.valueOf(normalCountOld * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepayRateOld(divide2.longValue());
            //老用户正常还款率 按金额
            BigDecimal divide3 = BigDecimal.valueOf(normalAmountOld * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepayAmountRateOld(divide3.longValue());
            //当前新用户正常还款笔数
            report.setNormalCountNew(normalCountNew);
            //当前新用户正常还款金额
            report.setNormalAmountNew(normalAmountNew);
            //新用户正常还款率 按笔数
            BigDecimal divide4 = BigDecimal.valueOf(normalCountNew * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepayRateNew(divide4.longValue());
            //新用户正常还款率 按金额
            BigDecimal divide5 = BigDecimal.valueOf(normalAmountNew * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepayAmountRateNew(divide5.longValue());
            //逾期3天内 还款率
            //逾期3天内总还款率 按笔数
            BigDecimal divide6 = BigDecimal.valueOf(repaymentS1Count * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS1CountAll(divide6.longValue());
            //逾期3天内总还款率 按金额
            BigDecimal divide7 = BigDecimal.valueOf(repaymentS1Amount * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS1AmountAll(divide7.longValue());
            //老用户逾期3天内还款率 按笔数
            BigDecimal divide8 = BigDecimal.valueOf(repaymentS1CountOld * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS1CountOld(divide8.longValue());
            //老用户逾期3天内还款率 按金额
            BigDecimal divide9 = BigDecimal.valueOf(repaymentS1AmountOld * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS1AmountOld(divide9.longValue());
            //新用户逾期3天内还款率 按笔数
            BigDecimal divide10 = BigDecimal.valueOf(repaymentS1CountNew * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS1CountNew(divide10.longValue());
            //新用户逾期3天内还款率 按金额
            BigDecimal divide11 = BigDecimal.valueOf(repaymentS1AmountNew * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS1AmountNew(divide11.longValue());
            //逾期7天内还款率
            //逾期7天内总还款率 按笔数
            BigDecimal divide12 = BigDecimal.valueOf(repaymentS2Count * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS2CountAll(divide12.longValue());
            //用户逾期7天内还款率 按金额
            BigDecimal divide13 = BigDecimal.valueOf(repaymentS2Amount * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS2AmountAll(divide13.longValue());
            //老用户逾期7天内还款率 按笔数
            BigDecimal divide14 = BigDecimal.valueOf(repaymentS2CountOld * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS2CountOld(divide14.longValue());
            //老用户逾期7天内还款率 按金额
            BigDecimal divide15 = BigDecimal.valueOf(repaymentS2AmountOld * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS2AmountOld(divide15.longValue());
            //新用户逾期7天内还款率 按笔数
            BigDecimal divide16 = BigDecimal.valueOf(repaymentS2CountNew * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS2CountNew(divide16.longValue());
            //新用户逾期7天内还款率 按金额
            BigDecimal divide17 = BigDecimal.valueOf(repaymentS2AmountNew * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS2AmountNew(divide17.longValue());
            //逾期30天内还款率
            //逾期30天内总还款率 按笔数
            BigDecimal divide18 = BigDecimal.valueOf(repaymentS3Count * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS3CountAll(divide18.longValue());
            //逾期30天内总还款率 按金额
            BigDecimal divide19 = BigDecimal.valueOf(repaymentS3Amount * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS3AmountAll(divide19.longValue());
            //老用户逾期30天内还款率 按笔数
            BigDecimal divide20 = BigDecimal.valueOf(repaymentS3CountOld * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS3CountOld(divide20.longValue());
            //老用户逾期30天内还款率 按金额
            BigDecimal divide21 = BigDecimal.valueOf(repaymentS3AmountOld * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS3AmountOld(divide21.longValue());
            //新用户逾期30天内还款率 按笔数
            BigDecimal divide22 = BigDecimal.valueOf(repaymentS3CountNew * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS3CountNew(divide22.longValue());
            //新用户逾期30天内还款率 按金额
            BigDecimal divide23 = BigDecimal.valueOf(repaymentS3AmountNew * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepaymentRateS3AmountNew(divide23.longValue());
            report.setAllRepayCount(allRepayCount);
            report.setAllRepayAmount(allRepayAmount);
            report.setNormalCount(normalRepaymentCount);
            report.setNormalAmount(normalRepaymentAmount);
        } else {
            return report;
        }
        return report;
    }

    @Override
    @Transactional
    public void renewalToday(String firstRepaymentTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reportDateStart", firstRepaymentTime);
        //查询本日还款统计是否已存在
        List<ReportRepayment> reports = reportRepaymentDAO.queryReportInfo(params);

        //处理报告数据
        ReportRepaymentTitleVO repaymentTitleVO = new ReportRepaymentTitleVO();
        if (reports.size() > 0) {
            repaymentTitleVO = renewalReportRepayment(reports.get(0), firstRepaymentTime);
        } else {
            repaymentTitleVO = renewalReportRepayment(null, firstRepaymentTime);
        }
        ReportRepayment report = new BaseConverter<ReportRepaymentTitleVO, ReportRepayment>().convert(repaymentTitleVO, ReportRepayment.class);

        //已存在，更新数据
        if (reports.size() == 1) {
            report.setId(reports.get(0).getId());
            reportRepaymentDAO.updateByPrimaryKeySelective(report);
        } else if (reports.size() == 0) {
            //不存在，创建
            reportRepaymentDAO.insertSelective(report);
        } else {
            log.error("本日还款续期统计异常---------->reportRepaymentToday end " + firstRepaymentTime);
            return;
        }
    }

    /**
     * 处理每日续期
     *
     * @param reportRepayment
     * @param firstRepaymentTime
     * @return
     */
    public ReportRepaymentTitleVO renewalReportRepayment(ReportRepayment reportRepayment, String firstRepaymentTime) {
        //应还款总数量
        Long allRepaymentCount = reportRepayment.getAllRepaymentCount();
        //前老用户应还款笔数
        Long expireCountOld = reportRepayment.getExpireCountOld();
        //当前新用户应还笔数
        Long expireCountNew = reportRepayment.getExpireCountNew();

        //应还款金额
        Long allRepaymentAmount = reportRepayment.getAllRepaymentAmount();
        //当前老用户应还款金额
        Long expireAmountOld = reportRepayment.getExpireAmountOld();
        //当前新用户应还金额
        Long expireAmountNew = reportRepayment.getExpireAmountNew();

        //今日正常还款笔数
        Long normalRepaymentCount = reportRepayment.getNormalRepaymentCount();
        //今日新用户正常还款笔数
        Long normalCountNew = reportRepayment.getNormalCountNew();
        //今日老用户正常还款笔数
        Long normalCountOld = reportRepayment.getNormalCountOld();

        //今日正常还款金额
        Long normalRepaymentAmount = reportRepayment.getNormalRepaymentAmount();
        //今日新用户正常还款金额
        Long normalAmountNew = reportRepayment.getNormalAmountNew();
        //今日老用户正常还款金额
        Long normalAmountOld = reportRepayment.getNormalAmountOld();

        // 根据客户类型查询今日应还订单的续期
        List<OrderRenewalVO> renewal = orderRenewalMapper.countExpireRenewal(firstRepaymentTime);
        log.info("今日总续期单数{}", renewal.size());
/*        if(reportRepayment != null){
            renewal = orderRenewalMapper.countNowExpireRenewal(firstRepaymentTime);
            log.info("当前10分钟续期单数{}",renewal.size());
        }*/
        // 续期数目（成功数量）
        Integer renewalNumber = 0;
        // 续期金额 (当日续期金额=当日续期成功订单利息之和+当日续期手续费之和)
        Long renewalAmount = 0L;
        for (OrderRenewalVO renewalVO : renewal) {
            //应还总数
            allRepaymentCount = allRepaymentCount + renewalVO.getNum();
            //应还金额
            allRepaymentAmount = allRepaymentAmount + renewalVO.getExpireAmount();
            //续期数目
            renewalNumber = renewalNumber + renewalVO.getNum();
            //续期金额
            renewalAmount = renewalAmount + renewalVO.getRenewalAmount();
            //新老用户应还 是否是老用户：0:新用户；1:老用户
            if (renewalVO.getCustomerType() != null && renewalVO.getCustomerType() == 1) {
                expireCountOld = expireCountOld + renewalVO.getNum();
                expireAmountOld = expireAmountOld + renewalVO.getExpireAmount();
            } else if (renewalVO.getCustomerType() != null && renewalVO.getCustomerType() == 0) {
                expireCountNew = expireCountNew + renewalVO.getNum();
                expireAmountNew = expireAmountNew + renewalVO.getExpireAmount();
            }
            //今日续期笔数
            normalRepaymentCount = normalRepaymentCount + renewalVO.getNum();
            //今日续期金额
            normalRepaymentAmount = normalRepaymentAmount + renewalVO.getRenewalAmount();
            // 新旧用户续期数量 0:新用户；1:老用户
            if (1 == renewalVO.getCustomerType()) {
                normalCountOld = normalCountOld + renewalVO.getNum();
                normalAmountOld = normalAmountOld + renewalVO.getRenewalAmount();
            } else if (0 == renewalVO.getCustomerType()) {
                normalCountNew = normalCountNew + renewalVO.getNum();
                normalAmountNew = normalAmountNew + renewalVO.getRenewalAmount();
            }
        }
        // 封装数据
        ReportRepaymentTitleVO report = new ReportRepaymentTitleVO();
        report.setReportDate(firstRepaymentTime);
        if (allRepaymentCount > 0 && allRepaymentAmount > 0) {
            //应还款总笔数
            report.setAllRepaymentCount(allRepaymentCount);
            //应还款总金额
            report.setAllRepaymentAmount(allRepaymentAmount);
            //新老用户应还
            report.setExpireCountNew(expireCountNew);
            report.setExpireCountOld(expireCountOld);
            report.setExpireAmountNew(expireAmountNew);
            report.setExpireAmountOld(expireAmountOld);
            report.setRenewalNumber(renewalNumber);
            report.setRenewalAmount(renewalAmount);

            //今日正常还款总笔数
            report.setNormalRepaymentCount(normalRepaymentCount);
            //今日正常还款总金额
            report.setNormalRepaymentAmount(normalRepaymentAmount);
            //当前正常还款率（笔数）
            BigDecimal divide = BigDecimal.valueOf(normalRepaymentCount * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepayRate(divide.longValue());
            //当前正常还款率（金额）
            BigDecimal divide1 = BigDecimal.valueOf(normalRepaymentAmount * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepayAmountRate(divide1.longValue());
            //当前老用户正常还款笔数
            report.setNormalCountOld(normalCountOld);
            //当前老用户正常还款金额
            report.setNormalAmountOld(normalAmountOld);
            //老用户正常还款率 按笔数
            BigDecimal divide2 = BigDecimal.valueOf(normalCountOld * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepayRateOld(divide2.longValue());
            //老用户正常还款率 按金额
            BigDecimal divide3 = BigDecimal.valueOf(normalAmountOld * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepayAmountRateOld(divide3.longValue());
            //当前新用户正常还款笔数
            report.setNormalCountNew(normalCountNew);
            //当前新用户正常还款金额
            report.setNormalAmountNew(normalAmountNew);
            //新用户正常还款率 按笔数
            BigDecimal divide4 = BigDecimal.valueOf(normalCountNew * 10000).divide(BigDecimal.valueOf(allRepaymentCount), 0, RoundingMode.HALF_UP);
            report.setRepayRateNew(divide4.longValue());
            //新用户正常还款率 按金额
            BigDecimal divide5 = BigDecimal.valueOf(normalAmountNew * 10000).divide(BigDecimal.valueOf(allRepaymentAmount), 0, RoundingMode.HALF_UP);
            report.setRepayAmountRateNew(divide5.longValue());

            report.setAllRepayCount(normalRepaymentCount);
            report.setAllRepayAmount(normalRepaymentAmount);
            report.setNormalCount(normalRepaymentCount - renewalNumber);
            report.setNormalAmount(normalRepaymentAmount - renewalAmount);
        } else {
            return report;
        }
        log.info("每日续期还款任务结束：" + firstRepaymentTime);
        return report;
    }
}
