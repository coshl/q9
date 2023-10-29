package com.summer.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PlateformChannelReport {
    /**
     */
    private Integer id;

    /**
     * 统计时间
     */
    private Date reportTime;

    /**
     * 渠道id
     */
    private Integer channelid;

    /**
     * 下载量  默认：0
     */
    private Integer downCount;

    /**
     * 登录人数  默认：0
     */
    private Integer loginCount;

    /**
     * 注册量  默认：0
     */
    private Integer registerCount;

    /**
     * 身份认证  默认：0
     */
    private Integer idcardCertificationCount;

    /**
     * 个人信息认证  默认：0
     */
    private Integer personalInformationCount;

    /**
     * 芝麻分认证人数  默认：0
     */
    private Integer sesameCount;

    /**
     * 运营商认证人数  默认：0
     */
    private Integer operatorCount;

    /**
     * 工作信息人数  默认：0
     */
    private Integer companyCount;

    /**
     * 申请借款人数  默认：0
     */
    private Integer borrowApplyCount;

    /**
     * 申请成功人数
     */
    private Integer borrowSucCount;

    /**
     * 放款人数  默认：0
     */
    private Integer loanCount;

    /**
     * 系数  默认：0.8000
     */
    private BigDecimal dedutionCoefficient;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;
    /**
     * 银行卡认证
     */
    private Integer authBankCount;
    //渠道注册量
    private Integer channelRegisterCount;
    //渠道申请量
    private Integer channelApplyCount;
    //渠道放款量
    private Integer channelLoanCount;
    //命中系统黑名单
    private Integer hitSystemBlack;
    //命中外部三方黑名单
    private Integer hitOutBlack;
    //渠道方命中系统黑名单数量
    private Integer channelSystemBlack;
    //渠道命中外部黑名单数量
    private Integer channelOutBlack;
    //渠道ios注册数量
    private Integer channelIos;
    //渠道安卓注册数量
    private Integer channelAnZuo;
    //渠道短信数量
    private Integer channelDuanXin;

    public Integer getChannelDuanXin() {
        return channelDuanXin;
    }

    public void setChannelDuanXin(Integer channelDuanXin) {
        this.channelDuanXin = channelDuanXin;
    }

    public Integer getChannelIos() {
        return channelIos;
    }

    public void setChannelIos(Integer channelIos) {
        this.channelIos = channelIos;
    }

    public Integer getChannelAnZuo() {
        return channelAnZuo;
    }

    public void setChannelAnZuo(Integer channelAnZuo) {
        this.channelAnZuo = channelAnZuo;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * 获取 plateform_channel_report.id
     *
     * @return plateform_channel_report.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 plateform_channel_report.id
     *
     * @param id plateform_channel_report.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 渠道id plateform_channel_report.channelid
     *
     * @return 渠道id
     */
    public Integer getChannelid() {
        return channelid;
    }

    /**
     * 设置 渠道id plateform_channel_report.channelid
     *
     * @param channelid 渠道id
     */
    public void setChannelid(Integer channelid) {
        this.channelid = channelid;
    }

    /**
     * 获取 下载量 plateform_channel_report.down_count
     *
     * @return 下载量
     */
    public Integer getDownCount() {
        return downCount;
    }

    /**
     * 设置 下载量 plateform_channel_report.down_count
     *
     * @param downCount 下载量
     */
    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    /**
     * 获取 登录人数 plateform_channel_report.login_count
     *
     * @return 登录人数
     */
    public Integer getLoginCount() {
        return loginCount;
    }

    /**
     * 设置 登录人数 plateform_channel_report.login_count
     *
     * @param loginCount 登录人数
     */
    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    /**
     * 获取 注册量 plateform_channel_report.register_count
     *
     * @return 注册量
     */
    public Integer getRegisterCount() {
        return registerCount;
    }

    /**
     * 设置 注册量 plateform_channel_report.register_count
     *
     * @param registerCount 注册量
     */
    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    /**
     * 获取 身份认证 plateform_channel_report.idcard_certification_count
     *
     * @return 身份认证
     */
    public Integer getIdcardCertificationCount() {
        return idcardCertificationCount;
    }

    /**
     * 设置 身份认证 plateform_channel_report.idcard_certification_count
     *
     * @param idcardCertificationCount 身份认证
     */
    public void setIdcardCertificationCount(Integer idcardCertificationCount) {
        this.idcardCertificationCount = idcardCertificationCount;
    }

    /**
     * 获取 个人信息认证 plateform_channel_report.personal_information_count
     *
     * @return 个人信息认证
     */
    public Integer getPersonalInformationCount() {
        return personalInformationCount;
    }

    /**
     * 设置 个人信息认证 plateform_channel_report.personal_information_count
     *
     * @param personalInformationCount 个人信息认证
     */
    public void setPersonalInformationCount(Integer personalInformationCount) {
        this.personalInformationCount = personalInformationCount;
    }

    /**
     * 获取 芝麻分认证人数 plateform_channel_report.sesame_count
     *
     * @return 芝麻分认证人数
     */
    public Integer getSesameCount() {
        return sesameCount;
    }

    /**
     * 设置 芝麻分认证人数 plateform_channel_report.sesame_count
     *
     * @param sesameCount 芝麻分认证人数
     */
    public void setSesameCount(Integer sesameCount) {
        this.sesameCount = sesameCount;
    }

    /**
     * 获取 运营商认证人数 plateform_channel_report.operator_count
     *
     * @return 运营商认证人数
     */
    public Integer getOperatorCount() {
        return operatorCount;
    }

    /**
     * 设置 运营商认证人数 plateform_channel_report.operator_count
     *
     * @param operatorCount 运营商认证人数
     */
    public void setOperatorCount(Integer operatorCount) {
        this.operatorCount = operatorCount;
    }

    /**
     * 获取 工作信息人数 plateform_channel_report.company_count
     *
     * @return 工作信息人数
     */
    public Integer getCompanyCount() {
        return companyCount;
    }

    /**
     * 设置 工作信息人数 plateform_channel_report.company_count
     *
     * @param companyCount 工作信息人数
     */
    public void setCompanyCount(Integer companyCount) {
        this.companyCount = companyCount;
    }

    /**
     * 获取 申请借款人数 plateform_channel_report.borrow_apply_count
     *
     * @return 申请借款人数
     */
    public Integer getBorrowApplyCount() {
        return borrowApplyCount;
    }

    /**
     * 设置 申请借款人数 plateform_channel_report.borrow_apply_count
     *
     * @param borrowApplyCount 申请借款人数
     */
    public void setBorrowApplyCount(Integer borrowApplyCount) {
        this.borrowApplyCount = borrowApplyCount;
    }

    /**
     * 获取 申请成功人数 plateform_channel_report.borrow_suc_count
     *
     * @return 申请成功人数
     */
    public Integer getBorrowSucCount() {
        return borrowSucCount;
    }

    /**
     * 设置 申请成功人数 plateform_channel_report.borrow_suc_count
     *
     * @param borrowSucCount 申请成功人数
     */
    public void setBorrowSucCount(Integer borrowSucCount) {
        this.borrowSucCount = borrowSucCount;
    }

    /**
     * 获取 放款人数 plateform_channel_report.loan_count
     *
     * @return 放款人数
     */
    public Integer getLoanCount() {
        return loanCount;
    }

    /**
     * 设置 放款人数 plateform_channel_report.loan_count
     *
     * @param loanCount 放款人数
     */
    public void setLoanCount(Integer loanCount) {
        this.loanCount = loanCount;
    }

    /**
     * 获取 系数 plateform_channel_report.dedution_coefficient
     *
     * @return 系数
     */
    public BigDecimal getDedutionCoefficient() {
        return dedutionCoefficient;
    }

    /**
     * 设置 系数 plateform_channel_report.dedution_coefficient
     *
     * @param dedutionCoefficient 系数
     */
    public void setDedutionCoefficient(BigDecimal dedutionCoefficient) {
        this.dedutionCoefficient = dedutionCoefficient;
    }

    /**
     * 获取 创建时间 plateform_channel_report.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 plateform_channel_report.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 plateform_channel_report.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 plateform_channel_report.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAuthBankCount() {
        return authBankCount;
    }

    public void setAuthBankCount(Integer authBankCount) {
        this.authBankCount = authBankCount;
    }

    public Integer getChannelRegisterCount() {
        return channelRegisterCount;
    }

    public void setChannelRegisterCount(Integer channelRegisterCount) {
        this.channelRegisterCount = channelRegisterCount;
    }

    public Integer getChannelApplyCount() {
        return channelApplyCount;
    }

    public void setChannelApplyCount(Integer channelApplyCount) {
        this.channelApplyCount = channelApplyCount;
    }

    public Integer getChannelLoanCount() {
        return channelLoanCount;
    }

    public void setChannelLoanCount(Integer channelLoanCount) {
        this.channelLoanCount = channelLoanCount;
    }

    public Integer getHitSystemBlack() {
        return hitSystemBlack;
    }

    public void setHitSystemBlack(Integer hitSystemBlack) {
        this.hitSystemBlack = hitSystemBlack;
    }

    public Integer getHitOutBlack() {
        return hitOutBlack;
    }

    public void setHitOutBlack(Integer hitOutBlack) {
        this.hitOutBlack = hitOutBlack;
    }

    public Integer getChannelSystemBlack() {
        return channelSystemBlack;
    }

    public void setChannelSystemBlack(Integer channelSystemBlack) {
        this.channelSystemBlack = channelSystemBlack;
    }

    public Integer getChannelOutBlack() {
        return channelOutBlack;
    }

    public void setChannelOutBlack(Integer channelOutBlack) {
        this.channelOutBlack = channelOutBlack;
    }
}