/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class OrderCollectionCaller {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 用户id  默认：0
     */
    private Integer userId;

    /**
     * 总还款记录表ID  默认：0
     */
    private Integer repaymentId;

    /**
     * 上一催收员ID  默认：0
     */
    private Integer lastCollectionUserId;

    /**
     * 当前催收员ID  默认：0
     */
    private Integer currentCollectionUserId;

    /**
     * 催收状态 0待催收 1催收中 2承诺还款 4催收成功 6减免申请 7审核成功 8审核拒绝  默认：0
     */
    private Byte status;

    /**
     * 承诺还款时间
     */
    private Date promiseRepaymentTime;

    /**
     * 最后催收时间
     */
    private Date lastCollectionTime;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 操作人
     */
    private String operatorName;

    /**
     * 催收情况
     */
    private String remark;

    /**
     * 标记删除 1删除 0有效  默认：0
     */
    private Byte deleted;

    /**
     * 派单时间
     */
    private Date dispatchTime;

    /**
     * 分配次数（续借客户直接让以前的客服跟进）  默认：1
     */
    private Integer allocationNumber;

    //备注
    private String annotation;

    /**
     * 获取 自增ID order_collection_caller.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID order_collection_caller.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 用户id order_collection_caller.user_id
     *
     * @return 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户id order_collection_caller.user_id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 总还款记录表ID order_collection_caller.repayment_id
     *
     * @return 总还款记录表ID
     */
    public Integer getRepaymentId() {
        return repaymentId;
    }

    /**
     * 设置 总还款记录表ID order_collection_caller.repayment_id
     *
     * @param repaymentId 总还款记录表ID
     */
    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
    }

    /**
     * 获取 上一催收员ID order_collection_caller.last_collection_user_id
     *
     * @return 上一催收员ID
     */
    public Integer getLastCollectionUserId() {
        return lastCollectionUserId;
    }

    /**
     * 设置 上一催收员ID order_collection_caller.last_collection_user_id
     *
     * @param lastCollectionUserId 上一催收员ID
     */
    public void setLastCollectionUserId(Integer lastCollectionUserId) {
        this.lastCollectionUserId = lastCollectionUserId;
    }

    /**
     * 获取 当前催收员ID order_collection_caller.current_collection_user_id
     *
     * @return 当前催收员ID
     */
    public Integer getCurrentCollectionUserId() {
        return currentCollectionUserId;
    }

    /**
     * 设置 当前催收员ID order_collection_caller.current_collection_user_id
     *
     * @param currentCollectionUserId 当前催收员ID
     */
    public void setCurrentCollectionUserId(Integer currentCollectionUserId) {
        this.currentCollectionUserId = currentCollectionUserId;
    }

    /**
     * 获取 催收状态 0待催收 1催收中 2承诺还款 4催收成功 6减免申请 7审核成功 8审核拒绝 order_collection_caller.status
     *
     * @return 催收状态 0待催收 1催收中 2承诺还款 4催收成功 6减免申请 7审核成功 8审核拒绝
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 催收状态 0待催收 1催收中 2承诺还款 4催收成功 6减免申请 7审核成功 8审核拒绝 order_collection_caller.status
     *
     * @param status 催收状态 0待催收 1催收中 2承诺还款 4催收成功 6减免申请 7审核成功 8审核拒绝
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 承诺还款时间 order_collection_caller.promise_repayment_time
     *
     * @return 承诺还款时间
     */
    public Date getPromiseRepaymentTime() {
        return promiseRepaymentTime;
    }

    /**
     * 设置 承诺还款时间 order_collection_caller.promise_repayment_time
     *
     * @param promiseRepaymentTime 承诺还款时间
     */
    public void setPromiseRepaymentTime(Date promiseRepaymentTime) {
        this.promiseRepaymentTime = promiseRepaymentTime;
    }

    /**
     * 获取 最后催收时间 order_collection_caller.last_collection_time
     *
     * @return 最后催收时间
     */
    public Date getLastCollectionTime() {
        return lastCollectionTime;
    }

    /**
     * 设置 最后催收时间 order_collection_caller.last_collection_time
     *
     * @param lastCollectionTime 最后催收时间
     */
    public void setLastCollectionTime(Date lastCollectionTime) {
        this.lastCollectionTime = lastCollectionTime;
    }

    /**
     * 获取 创建时间 order_collection_caller.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 order_collection_caller.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 order_collection_caller.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 order_collection_caller.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 操作人 order_collection_caller.operator_name
     *
     * @return 操作人
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置 操作人 order_collection_caller.operator_name
     *
     * @param operatorName 操作人
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * 获取 备注 order_collection_caller.remark
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 order_collection_caller.remark
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 标记删除 1删除 0有效 order_collection_caller.deleted
     *
     * @return 标记删除 1删除 0有效
     */
    public Byte getDeleted() {
        return deleted;
    }

    /**
     * 设置 标记删除 1删除 0有效 order_collection_caller.deleted
     *
     * @param deleted 标记删除 1删除 0有效
     */
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取 派单时间 order_collection_caller.dispatch_time
     *
     * @return 派单时间
     */
    public Date getDispatchTime() {
        return dispatchTime;
    }

    /**
     * 设置 派单时间 order_collection_caller.dispatch_time
     *
     * @param dispatchTime 派单时间
     */
    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    /**
     * 获取 分配次数（续借客户直接让以前的客服跟进） order_collection_caller.allocation_number
     *
     * @return 分配次数（续借客户直接让以前的客服跟进）
     */
    public Integer getAllocationNumber() {
        return allocationNumber;
    }

    /**
     * 设置 分配次数（续借客户直接让以前的客服跟进） order_collection_caller.allocation_number
     *
     * @param allocationNumber 分配次数（续借客户直接让以前的客服跟进）
     */
    public void setAllocationNumber(Integer allocationNumber) {
        this.allocationNumber = allocationNumber;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}