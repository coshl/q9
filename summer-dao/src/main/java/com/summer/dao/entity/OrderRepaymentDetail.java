/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class OrderRepaymentDetail {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 原订单表ID  默认：0
     */
    private Integer borrowId;

    /**
     * 总还款记录表ID  默认：0
     */
    private Integer repaymentId;

    /**
     * 借款用户id  默认：0
     */
    private Integer userId;

    /**
     * 还款金额  默认：0
     */
    private Integer paidAmount;

    /**
     * 备注
     */
    private String remark;
    private String payTimeChg;

    /**
     * 三方订单流水
     */
    private String thirdOrderNo;

    /**
     * 我方订单汇总id  默认：0
     */
    private String orderNo;

    /**
     * 支付时间  默认：1970-01-01 08:00:01
     */
    private Date payTime;

    /**
     * 支付失败原因
     */
    private String payTip;
    private String payUrl;
    private String createTimeChg;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 操作人  默认：0
     */
    private Integer operatorUserId;

    /**
     * 付款状态 0未知 1付款中 2付款成功 3付款失败   默认：0
     */
    private Byte status;

    /**
     * 还款类型 0正常 1逾期 2提前  默认：0
     */
    private Byte overdue;

    /**
     * 付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下  默认：0
     */
    private Byte payType;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public OrderRepaymentDetail() {
    }

    public OrderRepaymentDetail(Integer borrowId, Integer repaymentId, Integer userId, Integer paidAmount,
                                String remark, String thirdOrderNo, String orderNo, Date payTime, String payTip,
                                Date createTime, Date updateTime, Integer operatorUserId, Byte status, Byte overdue,
                                Byte payType) {
        this.borrowId = borrowId;
        this.repaymentId = repaymentId;
        this.userId = userId;
        this.paidAmount = paidAmount;
        this.remark = remark;
        this.thirdOrderNo = thirdOrderNo;
        this.orderNo = orderNo;
        this.payTime = payTime;
        this.payTip = payTip;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.operatorUserId = operatorUserId;
        this.status = status;
        this.overdue = overdue;
        this.payType = payType;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    public String getPayTimeChg() {
        return payTimeChg;
    }

    public void setPayTimeChg(String payTimeChg) {
        this.payTimeChg = payTimeChg;
    }

    /**
     * 获取 自增ID order_repayment_detail.id
     *
     * @return 自增ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 自增ID order_repayment_detail.id
     *
     * @param id 自增ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 原订单表ID order_repayment_detail.borrow_id
     *
     * @return 原订单表ID
     */
    public Integer getBorrowId() {
        return borrowId;
    }

    /**
     * 设置 原订单表ID order_repayment_detail.borrow_id
     *
     * @param borrowId 原订单表ID
     */
    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    /**
     * 获取 总还款记录表ID order_repayment_detail.repayment_id
     *
     * @return 总还款记录表ID
     */
    public Integer getRepaymentId() {
        return repaymentId;
    }

    /**
     * 设置 总还款记录表ID order_repayment_detail.repayment_id
     *
     * @param repaymentId 总还款记录表ID
     */
    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
    }

    /**
     * 获取 借款用户id order_repayment_detail.user_id
     *
     * @return 借款用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 借款用户id order_repayment_detail.user_id
     *
     * @param userId 借款用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 还款金额 order_repayment_detail.paid_amount
     *
     * @return 还款金额
     */
    public Integer getPaidAmount() {
        return paidAmount;
    }

    /**
     * 设置 还款金额 order_repayment_detail.paid_amount
     *
     * @param paidAmount 还款金额
     */
    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    /**
     * 获取 备注 order_repayment_detail.remark
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 order_repayment_detail.remark
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 三方订单流水 order_repayment_detail.third_order_no
     *
     * @return 三方订单流水
     */
    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    /**
     * 设置 三方订单流水 order_repayment_detail.third_order_no
     *
     * @param thirdOrderNo 三方订单流水
     */
    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo == null ? null : thirdOrderNo.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取 支付时间 order_repayment_detail.pay_time
     *
     * @return 支付时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 设置 支付时间 order_repayment_detail.pay_time
     *
     * @param payTime 支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取 支付失败原因 order_repayment_detail.pay_tip
     *
     * @return 支付失败原因
     */
    public String getPayTip() {
        return payTip;
    }

    /**
     * 设置 支付失败原因 order_repayment_detail.pay_tip
     *
     * @param payTip 支付失败原因
     */
    public void setPayTip(String payTip) {
        this.payTip = payTip == null ? null : payTip.trim();
    }

    /**
     * 获取 创建时间 order_repayment_detail.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 order_repayment_detail.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 order_repayment_detail.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 order_repayment_detail.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 操作人 order_repayment_detail.operator_user_id
     *
     * @return 操作人
     */
    public Integer getOperatorUserId() {
        return operatorUserId;
    }

    /**
     * 设置 操作人 order_repayment_detail.operator_user_id
     *
     * @param operatorUserId 操作人
     */
    public void setOperatorUserId(Integer operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    /**
     * 获取 付款状态 0未知 1付款中 2付款成功 3付款失败  order_repayment_detail.status
     *
     * @return 付款状态 0未知 1付款中 2付款成功 3付款失败
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 付款状态 0未知 1付款中 2付款成功 3付款失败  order_repayment_detail.status
     *
     * @param status 付款状态 0未知 1付款中 2付款成功 3付款失败
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 还款类型 0正常 1逾期 2提前 order_repayment_detail.overdue
     *
     * @return 还款类型 0正常 1逾期 2提前
     */
    public Byte getOverdue() {
        return overdue;
    }

    /**
     * 设置 还款类型 0正常 1逾期 2提前 order_repayment_detail.overdue
     *
     * @param overdue 还款类型 0正常 1逾期 2提前
     */
    public void setOverdue(Byte overdue) {
        this.overdue = overdue;
    }

    /**
     * 获取 付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下 order_repayment_detail.pay_type
     *
     * @return 付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下
     */
    public Byte getPayType() {
        return payType;
    }

    /**
     * 设置 付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下 order_repayment_detail.pay_type
     *
     * @param payType 付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下
     */
    public void setPayType(Byte payType) {
        this.payType = payType;
    }
}