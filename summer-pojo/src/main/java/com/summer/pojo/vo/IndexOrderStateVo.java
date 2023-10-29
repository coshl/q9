package com.summer.pojo.vo;

/**
 * 订单状态进度显示VO
 */
public class IndexOrderStateVo {
    /**
     * 订单状态
     */
    private String orderState;
    /**
     * 订单信息
     */
    private String orderInfo;
    /**
     * 通用时间
     */
    private String orderTime;
    /**
     * 还款倒计时、逾期天数、
     */
    private String nextLoanTime;
    /**
     * 订单显示的第几条
     */
    private String sort;


    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getNextLoanTime() {
        return nextLoanTime;
    }

    public void setNextLoanTime(String nextLoanTime) {
        this.nextLoanTime = nextLoanTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
