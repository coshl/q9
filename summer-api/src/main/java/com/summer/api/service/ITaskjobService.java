package com.summer.api.service;


import com.summer.dao.entity.IndexReport;

import java.util.Date;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
public interface ITaskjobService {
    /**
     * 首页报表统计
     */
    IndexReport statisticIndex(Date date, boolean insert);

    /**
     * 自动派单
     */
    void handleDispatchTask();

    /**
     * 还款提前一天提醒
     */
    void repaySmsRemind();

    /**
     * 还款当天提醒
     */
    void repaySmsRemindNow();

    /**
     * 还款逾期提醒
     */
    void repaySmsOverdue();

    /**
     * 催收统计
     */
    void statisticCollect(Date date);

    /**
     * 自动审核
     */
    void autoReview();

    /**
     * 更新逾期
     */
    void overdue();

    /**
     * 将今日应还款订单自动派单给当日催收
     */
    void autoDispatchWaiter();

    /**
     * 将待审核订单自动派单给审核人员（每15分钟一次）
     */
    void reviewDispatch();

    /**
     * 每15分钟更新一次调用风控花费
     */
//    void updateBilling();

    /**
     * 每10分钟更新一次催收统计
     *
     * @param now
     * @param beginTime
     * @param endTime
     */
    void updateCollectionStatistics(Date now, String beginTime, String endTime);

    /**
     * 每1小数更新一次逾期催收统计
     *
     * @param now
     * @param beginTime
     * @param endTime
     */
    void updateCollectionStatisticsOverdue(Date now, String beginTime, String endTime);

    Map<String, Object> findbalance();

    void pushOverdueInformation();

    Integer deleteByTime(Date now);
}
