package com.summer.pojo.vo;

import java.util.Date;

public class DailyStatisticsChannelLendVo {

    /**
     */
    private Integer id;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 还款时间
     */
    private Date repaymentTime;

    /**
     * 总单量  默认：0
     */
    private Integer sumNumber = 0;

    /**
     * 已还款  默认：0
     */
    private Integer repaymentNumber = 0;

    /**
     * 逾期量  默认：0
     */
    private Integer overdueNumber = 0;

    /**
     * 总逾期率
     */
    private float overdueRate = 0;

    /**
     * 新用户待还笔数  默认：0
     */
    private Integer firstBorrow = 0;

    /**
     * 新用户逾期量  默认：0
     */
    private Integer firstBorrowOverdue = 0;

    /**
     * 新用户逾期率
     */
    private float firstOverdueRate = 0;

    /**
     * 老用户待还笔数  默认：0
     */
    private Integer oldNumber = 0;

    /**
     * 老用户逾期笔数  默认：0
     */
    private Integer oldOverdueNumber = 0;

    /**
     * 老用户逾期率
     */
    private float oldOverdueRate = 0;

    /**
     * 总待还笔数
     */
    private Integer repaymentSum = 0;

    /**
     * 新用户待还笔数
     */
    private Integer firstBorrowSum = 0;

    /**
     * 老用户待还笔数
     */
    private Integer oldNumberSum = 0;

    /**
     * 总逾期笔数
     */
    private Integer overdueNumberSum = 0;

    /**
     * 新用户总逾期笔数
     */
    private Integer firstBorrowOverdueSum = 0;

    /**
     * 老用户总逾期笔数
     */
    private Integer oldOverdueNumberSum = 0;

    /**
     * 总逾期率
     */
    private float overdueSumRate = 0;

    /**
     * 新用户总逾期率
     */
    private float firstOverdueSumRate = 0;

    /**
     * 新用户总逾期率
     */
    private float oldOverdueSumRate = 0;

    /**
     * 新用户已还数量
     */
    private Integer newRepaymentNumber = 0;
    /**
     * 老用户已还数量
     */
    private Integer oldRepaymentNumber = 0;

    /**
     * 新用户还款率
     */
    private float newRepaymentRate = 0;
    /**
     * 老用户还款率
     */
    private float oldRepaymentRate = 0;

    /**
     * 续期总数
     */
    private Integer renewalNumber = 0;
    /**
     * 新用户续期数
     */
    private Integer newRenewalNumber = 0;
    /**
     * 老用户续期数
     */
    private Integer oldRenewalNumber = 0;

    /**
     * 提前续期数量
     */
    private Integer todayRenewalNumber = 0;
    /**
     * 今日续期数
     */
    private Integer aheadRenewalNumber = 0;

    /**
     * 新用户今日续期数
     */
    private Integer newTodayRenewalNumber = 0;
    /**
     * 老用户今日续期数
     */
    private Integer oldTodayRenewalNumber = 0;
    /**
     * 新用户提前续期数
     */
    private Integer newAheadRenewalNumber = 0;
    /**
     * 老用户提前续期数量
     */
    private Integer oldAheadRenewalNumber = 0;

    public float getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(float overdueRate) {
        this.overdueRate = overdueRate;
    }

    public float getFirstOverdueRate() {
        return firstOverdueRate;
    }

    public void setFirstOverdueRate(float firstOverdueRate) {
        this.firstOverdueRate = firstOverdueRate;
    }

    public float getOldOverdueRate() {
        return oldOverdueRate;
    }

    public void setOldOverdueRate(float oldOverdueRate) {
        this.oldOverdueRate = oldOverdueRate;
    }

    public float getOverdueSumRate() {
        return overdueSumRate;
    }

    public void setOverdueSumRate(float overdueSumRate) {
        this.overdueSumRate = overdueSumRate;
    }

    public float getFirstOverdueSumRate() {
        return firstOverdueSumRate;
    }

    public void setFirstOverdueSumRate(float firstOverdueSumRate) {
        this.firstOverdueSumRate = firstOverdueSumRate;
    }

    public float getOldOverdueSumRate() {
        return oldOverdueSumRate;
    }

    public void setOldOverdueSumRate(float oldOverdueSumRate) {
        this.oldOverdueSumRate = oldOverdueSumRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Integer getSumNumber() {
        return sumNumber;
    }

    public void setSumNumber(Integer sumNumber) {
        this.sumNumber = sumNumber;
    }

    public Integer getRepaymentNumber() {
        return repaymentNumber;
    }

    public void setRepaymentNumber(Integer repaymentNumber) {
        this.repaymentNumber = repaymentNumber;
    }

    public Integer getOverdueNumber() {
        return overdueNumber;
    }

    public void setOverdueNumber(Integer overdueNumber) {
        this.overdueNumber = overdueNumber;
    }

    public Integer getFirstBorrow() {
        return firstBorrow;
    }

    public void setFirstBorrow(Integer firstBorrow) {
        this.firstBorrow = firstBorrow;
    }

    public Integer getFirstBorrowOverdue() {
        return firstBorrowOverdue;
    }

    public void setFirstBorrowOverdue(Integer firstBorrowOverdue) {
        this.firstBorrowOverdue = firstBorrowOverdue;
    }

    public Integer getOldNumber() {
        return oldNumber;
    }

    public void setOldNumber(Integer oldNumber) {
        this.oldNumber = oldNumber;
    }

    public Integer getOldOverdueNumber() {
        return oldOverdueNumber;
    }

    public void setOldOverdueNumber(Integer oldOverdueNumber) {
        this.oldOverdueNumber = oldOverdueNumber;
    }

    public Integer getRepaymentSum() {
        return repaymentSum;
    }

    public void setRepaymentSum(Integer repaymentSum) {
        this.repaymentSum = repaymentSum;
    }

    public Integer getFirstBorrowSum() {
        return firstBorrowSum;
    }

    public void setFirstBorrowSum(Integer firstBorrowSum) {
        this.firstBorrowSum = firstBorrowSum;
    }

    public Integer getOldNumberSum() {
        return oldNumberSum;
    }

    public void setOldNumberSum(Integer oldNumberSum) {
        this.oldNumberSum = oldNumberSum;
    }

    public Integer getOverdueNumberSum() {
        return overdueNumberSum;
    }

    public void setOverdueNumberSum(Integer overdueNumberSum) {
        this.overdueNumberSum = overdueNumberSum;
    }

    public Integer getFirstBorrowOverdueSum() {
        return firstBorrowOverdueSum;
    }

    public void setFirstBorrowOverdueSum(Integer firstBorrowOverdueSum) {
        this.firstBorrowOverdueSum = firstBorrowOverdueSum;
    }

    public Integer getOldOverdueNumberSum() {
        return oldOverdueNumberSum;
    }

    public void setOldOverdueNumberSum(Integer oldOverdueNumberSum) {
        this.oldOverdueNumberSum = oldOverdueNumberSum;
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

    public float getNewRepaymentRate() {
        return newRepaymentRate;
    }

    public void setNewRepaymentRate(float newRepaymentRate) {
        this.newRepaymentRate = newRepaymentRate;
    }

    public float getOldRepaymentRate() {
        return oldRepaymentRate;
    }

    public void setOldRepaymentRate(float oldRepaymentRate) {
        this.oldRepaymentRate = oldRepaymentRate;
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
