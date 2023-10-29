/* https://github.com/12641561 */
package com.summer.dao.entity.dailystatics;

import java.util.Date;

public class DailyStatisticsChannelLend {
    /**
     */
    private Integer id;

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 还款时间
     */
    private Date repaymentTime;

    /**
     * 总单量  默认：0
     */
    private Integer sumNumber;

    /**
     * 已还款  默认：0
     */
    private Integer repaymentNumber;

    /**
     * 逾期量  默认：0
     */
    private Integer overdueNumber;

    /**
     * 新用户待还笔数  默认：0
     */
    private Integer firstBorrow;

    /**
     * 新用户逾期量  默认：0
     */
    private Integer firstBorrowOverdue;

    /**
     * 老用户待还笔数  默认：0
     */
    private Integer oldNumber;

    /**
     * 老用户逾期笔数  默认：0
     */
    private Integer oldOverdueNumber;
    /**
     * 总的逾期笔数
     */
    private Integer totalOverdueCount;
    /**
     * 新用户总逾期笔数
     */
    private Integer newTotalOverdueCount;
    /**
     * 老用户总逾期笔数
     */
    private Integer oldTotalOverdueCount;
    /**
     * 新用户已还数量
     */
    private Integer newRepaymentNumber;
    /**
     * 老用户已还数量
     */
    private Integer oldRepaymentNumber;
    /**
     * 续期总数
     */
    private Integer renewalNumber;
    /**
     * 新用户续期数
     */
    private Integer newRenewalNumber;
    /**
     * 老用户续期数
     */
    private Integer oldRenewalNumber;
    /**
     * 今日续期数量
     */
    private Integer todayRenewalNumber;
    /**
     * 提前续期数
     */
    private Integer aheadRenewalNumber;
    /**
     * 新用户今日续期数
     */
    private Integer newTodayRenewalNumber;
    /**
     * 老用户今日续期数
     */
    private Integer oldTodayRenewalNumber;
    /**
     * 新用户提前续期数
     */
    private Integer newAheadRenewalNumber;
    /**
     * 老用户提前续期数量
     */
    private Integer oldAheadRenewalNumber;

    /**
     * 获取 daily_statistics_channel_lend.id
     *
     * @return daily_statistics_channel_lend.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 daily_statistics_channel_lend.id
     *
     * @param id daily_statistics_channel_lend.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 渠道id daily_statistics_channel_lend.channel_id
     *
     * @return 渠道id
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置 渠道id daily_statistics_channel_lend.channel_id
     *
     * @param channelId 渠道id
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * 获取 还款时间 daily_statistics_channel_lend.repayment_time
     *
     * @return 还款时间
     */
    public Date getRepaymentTime() {
        return repaymentTime;
    }

    /**
     * 设置 还款时间 daily_statistics_channel_lend.repayment_time
     *
     * @param repaymentTime 还款时间
     */
    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    /**
     * 获取 总单量 daily_statistics_channel_lend.sum_number
     *
     * @return 总单量
     */
    public Integer getSumNumber() {
        return sumNumber;
    }

    /**
     * 设置 总单量 daily_statistics_channel_lend.sum_number
     *
     * @param sumNumber 总单量
     */
    public void setSumNumber(Integer sumNumber) {
        this.sumNumber = sumNumber;
    }

    /**
     * 获取 已还款 daily_statistics_channel_lend.repayment_number
     *
     * @return 已还款
     */
    public Integer getRepaymentNumber() {
        return repaymentNumber;
    }

    /**
     * 设置 已还款 daily_statistics_channel_lend.repayment_number
     *
     * @param repaymentNumber 已还款
     */
    public void setRepaymentNumber(Integer repaymentNumber) {
        this.repaymentNumber = repaymentNumber;
    }

    /**
     * 获取 逾期量 daily_statistics_channel_lend.overdue_number
     *
     * @return 逾期量
     */
    public Integer getOverdueNumber() {
        return overdueNumber;
    }

    /**
     * 设置 逾期量 daily_statistics_channel_lend.overdue_number
     *
     * @param overdueNumber 逾期量
     */
    public void setOverdueNumber(Integer overdueNumber) {
        this.overdueNumber = overdueNumber;
    }

    /**
     * 获取 新用户待还笔数 daily_statistics_channel_lend.first_borrow
     *
     * @return 新用户待还笔数
     */
    public Integer getFirstBorrow() {
        return firstBorrow;
    }

    /**
     * 设置 新用户待还笔数 daily_statistics_channel_lend.first_borrow
     *
     * @param firstBorrow 新用户待还笔数
     */
    public void setFirstBorrow(Integer firstBorrow) {
        this.firstBorrow = firstBorrow;
    }

    /**
     * 获取 新用户逾期量 daily_statistics_channel_lend.first_borrow_overdue
     *
     * @return 新用户逾期量
     */
    public Integer getFirstBorrowOverdue() {
        return firstBorrowOverdue;
    }

    /**
     * 设置 新用户逾期量 daily_statistics_channel_lend.first_borrow_overdue
     *
     * @param firstBorrowOverdue 新用户逾期量
     */
    public void setFirstBorrowOverdue(Integer firstBorrowOverdue) {
        this.firstBorrowOverdue = firstBorrowOverdue;
    }

    /**
     * 获取 老用户待还笔数 daily_statistics_channel_lend.old_number
     *
     * @return 老用户待还笔数
     */
    public Integer getOldNumber() {
        return oldNumber;
    }

    /**
     * 设置 老用户待还笔数 daily_statistics_channel_lend.old_number
     *
     * @param oldNumber 老用户待还笔数
     */
    public void setOldNumber(Integer oldNumber) {
        this.oldNumber = oldNumber;
    }

    /**
     * 获取 老用户逾期笔数 daily_statistics_channel_lend.old_overdue_number
     *
     * @return 老用户逾期笔数
     */
    public Integer getOldOverdueNumber() {
        return oldOverdueNumber;
    }

    /**
     * 设置 老用户逾期笔数 daily_statistics_channel_lend.old_overdue_number
     *
     * @param oldOverdueNumber 老用户逾期笔数
     */
    public void setOldOverdueNumber(Integer oldOverdueNumber) {
        this.oldOverdueNumber = oldOverdueNumber;
    }

    public Integer getTotalOverdueCount() {
        return totalOverdueCount;
    }

    public void setTotalOverdueCount(Integer totalOverdueCount) {
        this.totalOverdueCount = totalOverdueCount;
    }

    public Integer getNewTotalOverdueCount() {
        return newTotalOverdueCount;
    }

    public void setNewTotalOverdueCount(Integer newTotalOverdueCount) {
        this.newTotalOverdueCount = newTotalOverdueCount;
    }

    public Integer getOldTotalOverdueCount() {
        return oldTotalOverdueCount;
    }

    public void setOldTotalOverdueCount(Integer oldTotalOverdueCount) {
        this.oldTotalOverdueCount = oldTotalOverdueCount;
    }

    public Integer getNewRepaymentNumber() {
        return newRepaymentNumber;
    }

    public void setNewRepaymentNumber(Integer newRepaymentNumber) {
        this.newRepaymentNumber = newRepaymentNumber;
    }

    public Integer getOldRepaymentNumber() {
        return oldRepaymentNumber;
    }

    public void setOldRepaymentNumber(Integer oldRepaymentNumber) {
        this.oldRepaymentNumber = oldRepaymentNumber;
    }

    public Integer getRenewalNumber() {
        return renewalNumber;
    }

    public void setRenewalNumber(Integer renewalNumber) {
        this.renewalNumber = renewalNumber;
    }

    public Integer getNewRenewalNumber() {
        return newRenewalNumber;
    }

    public void setNewRenewalNumber(Integer newRenewalNumber) {
        this.newRenewalNumber = newRenewalNumber;
    }

    public Integer getOldRenewalNumber() {
        return oldRenewalNumber;
    }

    public void setOldRenewalNumber(Integer oldRenewalNumber) {
        this.oldRenewalNumber = oldRenewalNumber;
    }

    public Integer getTodayRenewalNumber() {
        return todayRenewalNumber;
    }

    public void setTodayRenewalNumber(Integer todayRenewalNumber) {
        this.todayRenewalNumber = todayRenewalNumber;
    }

    public Integer getAheadRenewalNumber() {
        return aheadRenewalNumber;
    }

    public void setAheadRenewalNumber(Integer aheadRenewalNumber) {
        this.aheadRenewalNumber = aheadRenewalNumber;
    }

    public Integer getNewTodayRenewalNumber() {
        return newTodayRenewalNumber;
    }

    public void setNewTodayRenewalNumber(Integer newTodayRenewalNumber) {
        this.newTodayRenewalNumber = newTodayRenewalNumber;
    }

    public Integer getOldTodayRenewalNumber() {
        return oldTodayRenewalNumber;
    }

    public void setOldTodayRenewalNumber(Integer oldTodayRenewalNumber) {
        this.oldTodayRenewalNumber = oldTodayRenewalNumber;
    }

    public Integer getNewAheadRenewalNumber() {
        return newAheadRenewalNumber;
    }

    public void setNewAheadRenewalNumber(Integer newAheadRenewalNumber) {
        this.newAheadRenewalNumber = newAheadRenewalNumber;
    }

    public Integer getOldAheadRenewalNumber() {
        return oldAheadRenewalNumber;
    }

    public void setOldAheadRenewalNumber(Integer oldAheadRenewalNumber) {
        this.oldAheadRenewalNumber = oldAheadRenewalNumber;
    }
}