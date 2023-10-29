/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

/**
 * 用户借款表
 */
public class UserSeal {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 用户id  默认：0
     */
    private Integer userId;

    /**
     * 借款金额
     */
    private String phone;

    /**
     * 借款天数
     */
    private String accountId;

    /**
     * 借款利率
     */
    private String sealData;

    /**
     * 状态  默认：0
     */
    private Byte status;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    public UserSeal() {
    }

    public UserSeal(Integer userId, String phone, String accountId, String sealData) {
        this.userId = userId;
        this.phone = phone;
        this.accountId = accountId;
        this.sealData = sealData;
    }

    /**
     * 获取 自增ID user_seal.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID user_seal.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 用户id user_seal.user_id
     *
     * @return 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户id user_seal.user_id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 手机号码 user_seal.phone
     *
     * @return 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号码 user_seal.phone
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 e签宝account_id user_seal.account_id
     *
     * @return e签宝account_id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置 e签宝account_id user_seal.account_id
     *
     * @param accountId e签宝account_id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * 获取 e签宝seal_data user_seal.seal_data
     *
     * @return e签宝seal_data
     */
    public String getSealData() {
        return sealData;
    }

    /**
     * 设置 e签宝seal_data user_seal.seal_data
     *
     * @param sealData e签宝seal_data
     */
    public void setSealData(String sealData) {
        this.sealData = sealData == null ? null : sealData.trim();
    }

    /**
     * 获取 状态 user_seal.status
     *
     * @return 状态
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 状态 user_seal.status
     *
     * @param status 状态
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 创建时间 user_seal.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 user_seal.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 user_seal.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 user_seal.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}