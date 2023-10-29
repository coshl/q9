package com.summer.pojo.vo;

/**
 * 渠道还款统计，总数统计
 */
public class ChannelLendTotalVO {
    //总待还笔数
    private Integer repaymentSum = 0;
    //信用用户待还笔数
    private Integer firstBorrowSum = 0;
    //老用户待还笔数
    private Integer oldNumberSum = 0;


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

}
