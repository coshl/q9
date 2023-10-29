/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class OrderCollectionDetail {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 催收订单ID（借款编号）  默认：0
     */
    private Integer collectionId;

    /**
     * 催收员ID  默认：0
     */
    private Integer collectionUserId;

    /**
     * 借款用户ID  默认：0
     */
    private Integer userId;

    /**
     * 联系人类型 0未知 1: 紧急联系人 2:通讯录联系人  默认：0
     */
    private Byte contactType;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人关系 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他  默认：0
     */
    private Byte relation;

    /**
     * 联系人电话
     */
    private String contactPhone;
    private String createTimeChg;

    /**
     * 施压等级  默认：0
     */
    private Byte pressLevel;

    /**
     * 当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典  默认：0
     */
    private Byte status;

    /**
     * 催收类型(0待催收 1电话催收、2短信催收)  默认：0
     */
    private Byte collectionType;

    /**
     * 催收到的金额  默认：0
     */
    private Integer collectedAmount;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 催收情况
     */
    private String collectionTag;

    /**
     * 备注
     */
    private String remark;

    public OrderCollectionDetail() {
    }

    public OrderCollectionDetail(Integer collectionId, Integer collectionUserId, Integer userId, Byte contactType,
                                 String contactName, Byte relation, String contactPhone, Byte pressLevel, Byte status
            , Byte collectionType, Integer collectedAmount, Date createTime, Date updateTime, String collectionTag,
                                 String remark) {
        this.collectionId = collectionId;
        this.collectionUserId = collectionUserId;
        this.userId = userId;
        this.contactType = contactType;
        this.contactName = contactName;
        this.relation = relation;
        this.contactPhone = contactPhone;
        this.pressLevel = pressLevel;
        this.status = status;
        this.collectionType = collectionType;
        this.collectedAmount = collectedAmount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.collectionTag = collectionTag;
        this.remark = remark;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    /**
     * 获取 自增id order_collection_detail.id
     *
     * @return 自增id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 自增id order_collection_detail.id
     *
     * @param id 自增id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 催收订单ID（借款编号） order_collection_detail.collection_id
     *
     * @return 催收订单ID（借款编号）
     */
    public Integer getCollectionId() {
        return collectionId;
    }

    /**
     * 设置 催收订单ID（借款编号） order_collection_detail.collection_id
     *
     * @param collectionId 催收订单ID（借款编号）
     */
    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * 获取 催收员ID order_collection_detail.collection_user_id
     *
     * @return 催收员ID
     */
    public Integer getCollectionUserId() {
        return collectionUserId;
    }

    /**
     * 设置 催收员ID order_collection_detail.collection_user_id
     *
     * @param collectionUserId 催收员ID
     */
    public void setCollectionUserId(Integer collectionUserId) {
        this.collectionUserId = collectionUserId;
    }

    /**
     * 获取 借款用户ID order_collection_detail.user_id
     *
     * @return 借款用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 借款用户ID order_collection_detail.user_id
     *
     * @param userId 借款用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 联系人类型 0未知 1: 紧急联系人 2:通讯录联系人 order_collection_detail.contact_type
     *
     * @return 联系人类型 0未知 1: 紧急联系人 2:通讯录联系人
     */
    public Byte getContactType() {
        return contactType;
    }

    /**
     * 设置 联系人类型 0未知 1: 紧急联系人 2:通讯录联系人 order_collection_detail.contact_type
     *
     * @param contactType 联系人类型 0未知 1: 紧急联系人 2:通讯录联系人
     */
    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    /**
     * 获取 联系人姓名 order_collection_detail.contact_name
     *
     * @return 联系人姓名
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置 联系人姓名 order_collection_detail.contact_name
     *
     * @param contactName 联系人姓名
     */
    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    /**
     * 获取 联系人关系 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他 order_collection_detail.relation
     *
     * @return 联系人关系 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他
     */
    public Byte getRelation() {
        return relation;
    }

    /**
     * 设置 联系人关系 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他 order_collection_detail.relation
     *
     * @param relation 联系人关系 0未知1父亲,2母亲,3本人,4亲人,5朋友,6同事,7其他
     */
    public void setRelation(Byte relation) {
        this.relation = relation;
    }

    /**
     * 获取 联系人电话 order_collection_detail.contact_phone
     *
     * @return 联系人电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置 联系人电话 order_collection_detail.contact_phone
     *
     * @param contactPhone 联系人电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    /**
     * 获取 施压等级 order_collection_detail.press_level
     *
     * @return 施压等级
     */
    public Byte getPressLevel() {
        return pressLevel;
    }

    /**
     * 设置 施压等级 order_collection_detail.press_level
     *
     * @param pressLevel 施压等级
     */
    public void setPressLevel(Byte pressLevel) {
        this.pressLevel = pressLevel;
    }

    /**
     * 获取 当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典 order_collection_detail.status
     *
     * @return 当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典 order_collection_detail.status
     *
     * @param status 当前催收状态 0待催收、1催收中、2承诺还款、3委外中、4委外成功、5催收成功 字典
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 催收类型(0待催收 1电话催收、2短信催收) order_collection_detail.collection_type
     *
     * @return 催收类型(0待催收 1电话催收 、 2短信催收)
     */
    public Byte getCollectionType() {
        return collectionType;
    }

    /**
     * 设置 催收类型(0待催收 1电话催收、2短信催收) order_collection_detail.collection_type
     *
     * @param collectionType 催收类型(0待催收 1电话催收、2短信催收)
     */
    public void setCollectionType(Byte collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * 获取 催收到的金额 order_collection_detail.collected_amount
     *
     * @return 催收到的金额
     */
    public Integer getCollectedAmount() {
        return collectedAmount;
    }

    /**
     * 设置 催收到的金额 order_collection_detail.collected_amount
     *
     * @param collectedAmount 催收到的金额
     */
    public void setCollectedAmount(Integer collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    /**
     * 获取 创建时间 order_collection_detail.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 order_collection_detail.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 order_collection_detail.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 order_collection_detail.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 催收情况 order_collection_detail.collection_tag
     *
     * @return 催收情况
     */
    public String getCollectionTag() {
        return collectionTag;
    }

    /**
     * 设置 催收情况 order_collection_detail.collection_tag
     *
     * @param collectionTag 催收情况
     */
    public void setCollectionTag(String collectionTag) {
        this.collectionTag = collectionTag == null ? null : collectionTag.trim();
    }

    /**
     * 获取 备注 order_collection_detail.remark
     *
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 order_collection_detail.remark
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}