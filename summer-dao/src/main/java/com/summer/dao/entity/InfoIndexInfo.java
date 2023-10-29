/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class InfoIndexInfo {
    /**
     */
    private Integer userId;

    /**
     * 最大额度  默认：100000
     */
    private String cardAmount;

    /**
     * 最小额度  默认：150000
     */
    private String amountMin;

    /**
     * 利率  默认：0.098,0.15
     */
    private String rate;

    /**
     * 个人信息  默认：0
     */
    private Integer authInfo;

    /**
     * 个人信息认证时间  默认：CURRENT_TIMESTAMP
     */
    private Date authInfoTime;

    /**
     * 联系人  默认：0
     */
    private Integer authContacts;

    /**
     * 紧急联系人认证时间  默认：CURRENT_TIMESTAMP
     */
    private Date authContactsTime;

    /**
     * 银行卡  默认：0
     */
    private Integer authBank;

    /**
     * 绑卡时间  默认：CURRENT_TIMESTAMP
     */
    private Date authBankTime;

    /**
     * 芝麻信用  默认：0
     */
    private Integer authSesame;

    /**
     * 芝麻认证时间  默认：CURRENT_TIMESTAMP
     */
    private Date authSesameTime;

    /**
     * 手机运营商  默认：0
     */
    private Integer authMobile;

    /**
     * 认证总数  默认：5
     */
    private Integer authCount;

    /**
     * 借款状态,1：存在借款 0：不存在  默认：0
     */
    private String borrowStatus;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 0:无效 1:有效  默认：1
     */
    private String status;

    /**
     * 获取 info_index_info.user_id
     *
     * @return info_index_info.user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 info_index_info.user_id
     *
     * @param userId info_index_info.user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 最大额度 info_index_info.card_amount
     *
     * @return 最大额度
     */
    public String getCardAmount() {
        return cardAmount;
    }

    /**
     * 设置 最大额度 info_index_info.card_amount
     *
     * @param cardAmount 最大额度
     */
    public void setCardAmount(String cardAmount) {
        this.cardAmount = cardAmount == null ? null : cardAmount.trim();
    }

    /**
     * 获取 最小额度 info_index_info.amount_min
     *
     * @return 最小额度
     */
    public String getAmountMin() {
        return amountMin;
    }

    /**
     * 设置 最小额度 info_index_info.amount_min
     *
     * @param amountMin 最小额度
     */
    public void setAmountMin(String amountMin) {
        this.amountMin = amountMin == null ? null : amountMin.trim();
    }

    /**
     * 获取 利率 info_index_info.rate
     *
     * @return 利率
     */
    public String getRate() {
        return rate;
    }

    /**
     * 设置 利率 info_index_info.rate
     *
     * @param rate 利率
     */
    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    /**
     * 获取 个人信息 info_index_info.auth_info
     *
     * @return 个人信息
     */
    public Integer getAuthInfo() {
        return authInfo;
    }

    /**
     * 设置 个人信息 info_index_info.auth_info
     *
     * @param authInfo 个人信息
     */
    public void setAuthInfo(Integer authInfo) {
        this.authInfo = authInfo;
    }

    /**
     * 获取 个人信息认证时间 info_index_info.auth_info_time
     *
     * @return 个人信息认证时间
     */
    public Date getAuthInfoTime() {
        return authInfoTime;
    }

    /**
     * 设置 个人信息认证时间 info_index_info.auth_info_time
     *
     * @param authInfoTime 个人信息认证时间
     */
    public void setAuthInfoTime(Date authInfoTime) {
        this.authInfoTime = authInfoTime;
    }

    /**
     * 获取 联系人 info_index_info.auth_contacts
     *
     * @return 联系人
     */
    public Integer getAuthContacts() {
        return authContacts;
    }

    /**
     * 设置 联系人 info_index_info.auth_contacts
     *
     * @param authContacts 联系人
     */
    public void setAuthContacts(Integer authContacts) {
        this.authContacts = authContacts;
    }

    /**
     * 获取 紧急联系人认证时间 info_index_info.auth_contacts_time
     *
     * @return 紧急联系人认证时间
     */
    public Date getAuthContactsTime() {
        return authContactsTime;
    }

    /**
     * 设置 紧急联系人认证时间 info_index_info.auth_contacts_time
     *
     * @param authContactsTime 紧急联系人认证时间
     */
    public void setAuthContactsTime(Date authContactsTime) {
        this.authContactsTime = authContactsTime;
    }

    /**
     * 获取 银行卡 info_index_info.auth_bank
     *
     * @return 银行卡
     */
    public Integer getAuthBank() {
        return authBank;
    }

    /**
     * 设置 银行卡 info_index_info.auth_bank
     *
     * @param authBank 银行卡
     */
    public void setAuthBank(Integer authBank) {
        this.authBank = authBank;
    }

    /**
     * 获取 绑卡时间 info_index_info.auth_bank_time
     *
     * @return 绑卡时间
     */
    public Date getAuthBankTime() {
        return authBankTime;
    }

    /**
     * 设置 绑卡时间 info_index_info.auth_bank_time
     *
     * @param authBankTime 绑卡时间
     */
    public void setAuthBankTime(Date authBankTime) {
        this.authBankTime = authBankTime;
    }

    /**
     * 获取 芝麻信用 info_index_info.auth_sesame
     *
     * @return 芝麻信用
     */
    public Integer getAuthSesame() {
        return authSesame;
    }

    /**
     * 设置 芝麻信用 info_index_info.auth_sesame
     *
     * @param authSesame 芝麻信用
     */
    public void setAuthSesame(Integer authSesame) {
        this.authSesame = authSesame;
    }

    /**
     * 获取 芝麻认证时间 info_index_info.auth_sesame_time
     *
     * @return 芝麻认证时间
     */
    public Date getAuthSesameTime() {
        return authSesameTime;
    }

    /**
     * 设置 芝麻认证时间 info_index_info.auth_sesame_time
     *
     * @param authSesameTime 芝麻认证时间
     */
    public void setAuthSesameTime(Date authSesameTime) {
        this.authSesameTime = authSesameTime;
    }

    /**
     * 获取 手机运营商 info_index_info.auth_mobile
     *
     * @return 手机运营商
     */
    public Integer getAuthMobile() {
        return authMobile;
    }

    /**
     * 设置 手机运营商 info_index_info.auth_mobile
     *
     * @param authMobile 手机运营商
     */
    public void setAuthMobile(Integer authMobile) {
        this.authMobile = authMobile;
    }

    /**
     * 获取 认证总数 info_index_info.auth_count
     *
     * @return 认证总数
     */
    public Integer getAuthCount() {
        return authCount;
    }

    /**
     * 设置 认证总数 info_index_info.auth_count
     *
     * @param authCount 认证总数
     */
    public void setAuthCount(Integer authCount) {
        this.authCount = authCount;
    }

    /**
     * 获取 借款状态,1：存在借款 0：不存在 info_index_info.borrow_status
     *
     * @return 借款状态, 1：存在借款 0：不存在
     */
    public String getBorrowStatus() {
        return borrowStatus;
    }

    /**
     * 设置 借款状态,1：存在借款 0：不存在 info_index_info.borrow_status
     *
     * @param borrowStatus 借款状态,1：存在借款 0：不存在
     */
    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus == null ? null : borrowStatus.trim();
    }

    /**
     * 获取 银行卡号 info_index_info.bank_no
     *
     * @return 银行卡号
     */
    public String getBankNo() {
        return bankNo;
    }

    /**
     * 设置 银行卡号 info_index_info.bank_no
     *
     * @param bankNo 银行卡号
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    /**
     * 获取 0:无效 1:有效 info_index_info.status
     *
     * @return 0:无效 1:有效
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置 0:无效 1:有效 info_index_info.status
     *
     * @param status 0:无效 1:有效
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

}