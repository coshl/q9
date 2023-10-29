/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class OrderCollectionReduction {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 催收订单ID（借款编号）  默认：0
     */
    private Integer collectionId;
    private Integer repaymentId;

    /**
     * 减免人员ID  默认：0
     */
    private Integer operatorUserId;

    /**
     * 借款用户ID  默认：0
     */
    private Integer userId;

    /**
     * 减免金额  默认：0
     */
    private Integer reductionAmount;

    /**
     * 审核状态 0待审核、1拒绝、2通过  默认：0
     */
    private Byte auditStatus;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;
    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private String createTimeChg;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public OrderCollectionReduction() {
    }

    public OrderCollectionReduction(Integer collectionId, Integer operatorUserId, Integer userId,
                                    Integer reductionAmount, Byte auditStatus, Date createTime, Date updateTime,
                                    String remark, Integer repaymentId) {
        this.collectionId = collectionId;
        this.operatorUserId = operatorUserId;
        this.userId = userId;
        this.reductionAmount = reductionAmount;
        this.auditStatus = auditStatus;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.repaymentId = repaymentId;
    }

    public Integer getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    /**
     * 获取 自增id order_collection_reduction.id
     *
     * @return 自增id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 自增id order_collection_reduction.id
     *
     * @param id 自增id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 催收订单ID（借款编号） order_collection_reduction.collection_id
     *
     * @return 催收订单ID（借款编号）
     */
    public Integer getCollectionId() {
        return collectionId;
    }

    /**
     * 设置 催收订单ID（借款编号） order_collection_reduction.collection_id
     *
     * @param collectionId 催收订单ID（借款编号）
     */
    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * 获取 减免人员ID order_collection_reduction.operator_user_id
     *
     * @return 减免人员ID
     */
    public Integer getOperatorUserId() {
        return operatorUserId;
    }

    /**
     * 设置 减免人员ID order_collection_reduction.operator_user_id
     *
     * @param operatorUserId 减免人员ID
     */
    public void setOperatorUserId(Integer operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    /**
     * 获取 借款用户ID order_collection_reduction.user_id
     *
     * @return 借款用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 借款用户ID order_collection_reduction.user_id
     *
     * @param userId 借款用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 减免金额 order_collection_reduction.reduction_amount
     *
     * @return 减免金额
     */
    public Integer getReductionAmount() {
        return reductionAmount;
    }

    /**
     * 设置 减免金额 order_collection_reduction.reduction_amount
     *
     * @param reductionAmount 减免金额
     */
    public void setReductionAmount(Integer reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    /**
     * 获取 审核状态 0待审核、1拒绝、2通过 order_collection_reduction.audit_status
     *
     * @return 审核状态 0待审核、1拒绝、2通过
     */
    public Byte getAuditStatus() {
        return auditStatus;
    }

    /**
     * 设置 审核状态 0待审核、1拒绝、2通过 order_collection_reduction.audit_status
     *
     * @param auditStatus 审核状态 0待审核、1拒绝、2通过
     */
    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取 创建时间 order_collection_reduction.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 order_collection_reduction.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 order_collection_reduction.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 order_collection_reduction.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 备注 order_collection_reduction.remark
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 order_collection_reduction.remark
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}