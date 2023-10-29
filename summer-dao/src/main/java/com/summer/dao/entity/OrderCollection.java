/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class OrderCollection {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 借款用户ID  默认：0
     */
    private Integer userId;

    /**
     * 借款订单id  默认：0
     */
    private Integer borrowId;

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
     * 当前逾期等级（即逾期字典分组）  默认：0
     */
    private Byte currentOverdueLevel;

    /**
     * 逾期天数  默认：0
     */
    private Integer overdueDay;

    /**
     * s1审批人ID  默认：0
     */
    private Integer m1ApproveId;

    /**
     * s1审批人操作状态（1，已操作过，0未操作过）  默认：0
     */
    private Byte m1OperateStatus;

    /**
     * s2审批人ID  默认：0
     */
    private Integer m2ApproveId;

    /**
     * s2审批人操作状态（1，已操作过，0未操作过）  默认：0
     */
    private Byte m2OperateStatus;

    /**
     * m1-m2审批人ID  默认：0
     */
    private Integer m3ApproveId;

    /**
     * m1-m2审批人操作状态（1，已操作过，0未操作过）  默认：0
     */
    private Byte m3OperateStatus;

    /**
     * m2-m3审批人ID  默认：0
     */
    private Integer m4ApproveId;

    /**
     * m2-m3审批人操作状态（1，已操作过，0未操作过）  默认：0
     */
    private Byte m4OperateStatus;

    /**
     * m3+审批人ID  默认：0
     */
    private Integer m5ApproveId;

    /**
     * m3+审批人操作状态（1，已操作过，0未操作过）  默认：0
     */
    private Byte m5OperateStatus;

    /**
     * 催收状态 0待催收 1催收中 2承诺还款3 待催收（外派）4催收成功 6减免申请 7审核成功 8审核拒绝  默认：0
     */
    private Byte status;

    /**
     * 承诺还款时间  默认：1970-01-01 08:00:01
     */
    private Date promiseRepaymentTime;

    /**
     * 最后催收时间  默认：1970-01-01 08:00:01
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
    private Date dispatchTime;

    /**
     * 操作人
     */
    private String operatorName;
    private String orderNo;
    private String promiseRepaymentTimeChg;

    /**
     * 备注
     */
    private String remark;

    /**
     * 委外机构ID  默认：0
     */
    private Short outsideCompanyId;

    /**
     * 标记删除 1删除 0有效  默认：0
     */
    private Byte deleted;

    /**
     * 标记重要程度 越大越重  默认：0
     */
    private Byte importantWeight;

    /**
     * 借款人姓名
     */
    private String loanRealName;

    /**
     * 借款人手机号
     */
    private String loanUserPhone;

    /**
     * 累计实收金额  默认：0
     */
    private Integer realMoney;

    /**
     * 累计减免滞纳金  默认：0
     */
    private Integer reductionMoney;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPromiseRepaymentTimeChg() {
        return promiseRepaymentTimeChg == null ? "" : promiseRepaymentTimeChg;
    }

    public void setPromiseRepaymentTimeChg(String promiseRepaymentTimeChg) {
        this.promiseRepaymentTimeChg = promiseRepaymentTimeChg;
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    /**
     * 获取 自增ID order_collection.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID order_collection.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 借款用户ID order_collection.user_id
     *
     * @return 借款用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 借款用户ID order_collection.user_id
     *
     * @param userId 借款用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 借款订单id order_collection.borrow_id
     *
     * @return 借款订单id
     */
    public Integer getBorrowId() {
        return borrowId;
    }

    /**
     * 设置 借款订单id order_collection.borrow_id
     *
     * @param borrowId 借款订单id
     */
    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    /**
     * 获取 总还款记录表ID order_collection.repayment_id
     *
     * @return 总还款记录表ID
     */
    public Integer getRepaymentId() {
        return repaymentId;
    }

    /**
     * 设置 总还款记录表ID order_collection.repayment_id
     *
     * @param repaymentId 总还款记录表ID
     */
    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
    }

    /**
     * 获取 上一催收员ID order_collection.last_collection_user_id
     *
     * @return 上一催收员ID
     */
    public Integer getLastCollectionUserId() {
        return lastCollectionUserId;
    }

    /**
     * 设置 上一催收员ID order_collection.last_collection_user_id
     *
     * @param lastCollectionUserId 上一催收员ID
     */
    public void setLastCollectionUserId(Integer lastCollectionUserId) {
        this.lastCollectionUserId = lastCollectionUserId;
    }

    /**
     * 获取 当前催收员ID order_collection.current_collection_user_id
     *
     * @return 当前催收员ID
     */
    public Integer getCurrentCollectionUserId() {
        return currentCollectionUserId;
    }

    /**
     * 设置 当前催收员ID order_collection.current_collection_user_id
     *
     * @param currentCollectionUserId 当前催收员ID
     */
    public void setCurrentCollectionUserId(Integer currentCollectionUserId) {
        this.currentCollectionUserId = currentCollectionUserId;
    }

    /**
     * 获取 当前逾期等级（即逾期字典分组） order_collection.current_overdue_level
     *
     * @return 当前逾期等级（即逾期字典分组）
     */
    public Byte getCurrentOverdueLevel() {
        return currentOverdueLevel;
    }

    /**
     * 设置 当前逾期等级（即逾期字典分组） order_collection.current_overdue_level
     *
     * @param currentOverdueLevel 当前逾期等级（即逾期字典分组）
     */
    public void setCurrentOverdueLevel(Byte currentOverdueLevel) {
        this.currentOverdueLevel = currentOverdueLevel;
    }

    /**
     * 获取 逾期天数 order_collection.overdue_day
     *
     * @return 逾期天数
     */
    public Integer getOverdueDay() {
        return overdueDay;
    }

    /**
     * 设置 逾期天数 order_collection.overdue_day
     *
     * @param overdueDay 逾期天数
     */
    public void setOverdueDay(Integer overdueDay) {
        this.overdueDay = overdueDay;
    }

    /**
     * 获取 s1审批人ID order_collection.m1_approve_id
     *
     * @return s1审批人ID
     */
    public Integer getM1ApproveId() {
        return m1ApproveId;
    }

    /**
     * 设置 s1审批人ID order_collection.m1_approve_id
     *
     * @param m1ApproveId s1审批人ID
     */
    public void setM1ApproveId(Integer m1ApproveId) {
        this.m1ApproveId = m1ApproveId;
    }

    /**
     * 获取 s1审批人操作状态（1，已操作过，0未操作过） order_collection.m1_operate_status
     *
     * @return s1审批人操作状态（1，已操作过，0未操作过）
     */
    public Byte getM1OperateStatus() {
        return m1OperateStatus;
    }

    /**
     * 设置 s1审批人操作状态（1，已操作过，0未操作过） order_collection.m1_operate_status
     *
     * @param m1OperateStatus s1审批人操作状态（1，已操作过，0未操作过）
     */
    public void setM1OperateStatus(Byte m1OperateStatus) {
        this.m1OperateStatus = m1OperateStatus;
    }

    /**
     * 获取 s2审批人ID order_collection.m2_approve_id
     *
     * @return s2审批人ID
     */
    public Integer getM2ApproveId() {
        return m2ApproveId;
    }

    /**
     * 设置 s2审批人ID order_collection.m2_approve_id
     *
     * @param m2ApproveId s2审批人ID
     */
    public void setM2ApproveId(Integer m2ApproveId) {
        this.m2ApproveId = m2ApproveId;
    }

    /**
     * 获取 s2审批人操作状态（1，已操作过，0未操作过） order_collection.m2_operate_status
     *
     * @return s2审批人操作状态（1，已操作过，0未操作过）
     */
    public Byte getM2OperateStatus() {
        return m2OperateStatus;
    }

    /**
     * 设置 s2审批人操作状态（1，已操作过，0未操作过） order_collection.m2_operate_status
     *
     * @param m2OperateStatus s2审批人操作状态（1，已操作过，0未操作过）
     */
    public void setM2OperateStatus(Byte m2OperateStatus) {
        this.m2OperateStatus = m2OperateStatus;
    }

    /**
     * 获取 m1-m2审批人ID order_collection.m3_approve_id
     *
     * @return m1-m2审批人ID
     */
    public Integer getM3ApproveId() {
        return m3ApproveId;
    }

    /**
     * 设置 m1-m2审批人ID order_collection.m3_approve_id
     *
     * @param m3ApproveId m1-m2审批人ID
     */
    public void setM3ApproveId(Integer m3ApproveId) {
        this.m3ApproveId = m3ApproveId;
    }

    /**
     * 获取 m1-m2审批人操作状态（1，已操作过，0未操作过） order_collection.m3_operate_status
     *
     * @return m1-m2审批人操作状态（1，已操作过，0未操作过）
     */
    public Byte getM3OperateStatus() {
        return m3OperateStatus;
    }

    /**
     * 设置 m1-m2审批人操作状态（1，已操作过，0未操作过） order_collection.m3_operate_status
     *
     * @param m3OperateStatus m1-m2审批人操作状态（1，已操作过，0未操作过）
     */
    public void setM3OperateStatus(Byte m3OperateStatus) {
        this.m3OperateStatus = m3OperateStatus;
    }

    /**
     * 获取 m2-m3审批人ID order_collection.m4_approve_id
     *
     * @return m2-m3审批人ID
     */
    public Integer getM4ApproveId() {
        return m4ApproveId;
    }

    /**
     * 设置 m2-m3审批人ID order_collection.m4_approve_id
     *
     * @param m4ApproveId m2-m3审批人ID
     */
    public void setM4ApproveId(Integer m4ApproveId) {
        this.m4ApproveId = m4ApproveId;
    }

    /**
     * 获取 m2-m3审批人操作状态（1，已操作过，0未操作过） order_collection.m4_operate_status
     *
     * @return m2-m3审批人操作状态（1，已操作过，0未操作过）
     */
    public Byte getM4OperateStatus() {
        return m4OperateStatus;
    }

    /**
     * 设置 m2-m3审批人操作状态（1，已操作过，0未操作过） order_collection.m4_operate_status
     *
     * @param m4OperateStatus m2-m3审批人操作状态（1，已操作过，0未操作过）
     */
    public void setM4OperateStatus(Byte m4OperateStatus) {
        this.m4OperateStatus = m4OperateStatus;
    }

    /**
     * 获取 m3+审批人ID order_collection.m5_approve_id
     *
     * @return m3+审批人ID
     */
    public Integer getM5ApproveId() {
        return m5ApproveId;
    }

    /**
     * 设置 m3+审批人ID order_collection.m5_approve_id
     *
     * @param m5ApproveId m3+审批人ID
     */
    public void setM5ApproveId(Integer m5ApproveId) {
        this.m5ApproveId = m5ApproveId;
    }

    /**
     * 获取 m3+审批人操作状态（1，已操作过，0未操作过） order_collection.m5_operate_status
     *
     * @return m3+审批人操作状态（1，已操作过，0未操作过）
     */
    public Byte getM5OperateStatus() {
        return m5OperateStatus;
    }

    /**
     * 设置 m3+审批人操作状态（1，已操作过，0未操作过） order_collection.m5_operate_status
     *
     * @param m5OperateStatus m3+审批人操作状态（1，已操作过，0未操作过）
     */
    public void setM5OperateStatus(Byte m5OperateStatus) {
        this.m5OperateStatus = m5OperateStatus;
    }

    /**
     * 获取 催收状态 0待催收 1催收中 2承诺还款3 待催收（外派）4催收成功 6减免申请 7审核成功 8审核拒绝 order_collection.status
     *
     * @return 催收状态 0待催收 1催收中 2承诺还款3 待催收（外派）4催收成功 6减免申请 7审核成功 8审核拒绝
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 催收状态 0待催收 1催收中 2承诺还款3 待催收（外派）4催收成功 6减免申请 7审核成功 8审核拒绝 order_collection.status
     *
     * @param status 催收状态 0待催收 1催收中 2承诺还款3 待催收（外派）4催收成功 6减免申请 7审核成功 8审核拒绝
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 承诺还款时间 order_collection.promise_repayment_time
     *
     * @return 承诺还款时间
     */
    public Date getPromiseRepaymentTime() {
        return promiseRepaymentTime;
    }

    /**
     * 设置 承诺还款时间 order_collection.promise_repayment_time
     *
     * @param promiseRepaymentTime 承诺还款时间
     */
    public void setPromiseRepaymentTime(Date promiseRepaymentTime) {
        this.promiseRepaymentTime = promiseRepaymentTime;
    }

    /**
     * 获取 最后催收时间 order_collection.last_collection_time
     *
     * @return 最后催收时间
     */
    public Date getLastCollectionTime() {
        return lastCollectionTime;
    }

    /**
     * 设置 最后催收时间 order_collection.last_collection_time
     *
     * @param lastCollectionTime 最后催收时间
     */
    public void setLastCollectionTime(Date lastCollectionTime) {
        this.lastCollectionTime = lastCollectionTime;
    }

    /**
     * 获取 创建时间 order_collection.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 order_collection.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 order_collection.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 order_collection.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 操作人 order_collection.operator_name
     *
     * @return 操作人
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置 操作人 order_collection.operator_name
     *
     * @param operatorName 操作人
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * 获取 备注 order_collection.remark
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 order_collection.remark
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 委外机构ID order_collection.outside_company_id
     *
     * @return 委外机构ID
     */
    public Short getOutsideCompanyId() {
        return outsideCompanyId;
    }

    /**
     * 设置 委外机构ID order_collection.outside_company_id
     *
     * @param outsideCompanyId 委外机构ID
     */
    public void setOutsideCompanyId(Short outsideCompanyId) {
        this.outsideCompanyId = outsideCompanyId;
    }

    /**
     * 获取 标记删除 1删除 0有效 order_collection.deleted
     *
     * @return 标记删除 1删除 0有效
     */
    public Byte getDeleted() {
        return deleted;
    }

    /**
     * 设置 标记删除 1删除 0有效 order_collection.deleted
     *
     * @param deleted 标记删除 1删除 0有效
     */
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取 标记重要程度 越大越重 order_collection.important_weight
     *
     * @return 标记重要程度 越大越重
     */
    public Byte getImportantWeight() {
        return importantWeight;
    }

    /**
     * 设置 标记重要程度 越大越重 order_collection.important_weight
     *
     * @param importantWeight 标记重要程度 越大越重
     */
    public void setImportantWeight(Byte importantWeight) {
        this.importantWeight = importantWeight;
    }

    /**
     * 获取 借款人姓名 order_collection.loan_real_name
     *
     * @return 借款人姓名
     */
    public String getLoanRealName() {
        return loanRealName;
    }

    /**
     * 设置 借款人姓名 order_collection.loan_real_name
     *
     * @param loanRealName 借款人姓名
     */
    public void setLoanRealName(String loanRealName) {
        this.loanRealName = loanRealName == null ? null : loanRealName.trim();
    }

    /**
     * 获取 借款人手机号 order_collection.loan_user_phone
     *
     * @return 借款人手机号
     */
    public String getLoanUserPhone() {
        return loanUserPhone;
    }

    /**
     * 设置 借款人手机号 order_collection.loan_user_phone
     *
     * @param loanUserPhone 借款人手机号
     */
    public void setLoanUserPhone(String loanUserPhone) {
        this.loanUserPhone = loanUserPhone == null ? null : loanUserPhone.trim();
    }

    /**
     * 获取 累计实收金额 order_collection.real_money
     *
     * @return 累计实收金额
     */
    public Integer getRealMoney() {
        return realMoney;
    }

    /**
     * 设置 累计实收金额 order_collection.real_money
     *
     * @param realMoney 累计实收金额
     */
    public void setRealMoney(Integer realMoney) {
        this.realMoney = realMoney;
    }

    /**
     * 获取 累计减免滞纳金 order_collection.reduction_money
     *
     * @return 累计减免滞纳金
     */
    public Integer getReductionMoney() {
        return reductionMoney;
    }

    /**
     * 设置 累计减免滞纳金 order_collection.reduction_money
     *
     * @param reductionMoney 累计减免滞纳金
     */
    public void setReductionMoney(Integer reductionMoney) {
        this.reductionMoney = reductionMoney;
    }

}