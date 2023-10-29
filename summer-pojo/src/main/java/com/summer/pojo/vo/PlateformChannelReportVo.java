package com.summer.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 我方渠道统计列表
 *
 */
public class PlateformChannelReportVo {

    /**
     *
     */
    private Integer id;

    /**
     * 统计时间
     */
    private String reportTime;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 下载量  默认：0
     */
    private Integer downCount;

    /**
     * 下载率
     */
    private float downConversion;

    /**
     * 登录人数  默认：0
     */
    private Integer loginCount;

    /**
     * 登录率
     */
    private float loginConversion;

    /**
     * 注册量  默认：0
     */
    private Integer registerCount;

    /**
     * (临时交换字段意义:身份证、个人信息、芝麻认证、运营商认证、银行卡认证、工作信息认证)
     * 身份认证率
     */
    private float idcardCertificationCount;

    /**
     * 身份认证  默认：0
     */
    private Integer idcardCertificationConversion;

    /**
     * 个人信息认证率
     */
    private float personalInformationCount;

    /**
     * 个人信息认证  默认：0
     */
    private Integer personalInformationConversion;

    /**
     * 芝麻分认证率
     */
    private float sesameCount;

    /**
     * 芝麻分认证人数  默认：0
     */
    private Integer sesameConversion;

    /**
     * 运营商认证率
     */
    private float operatorCount;

    /**
     * 运营商认证人数  默认：0
     */
    private Integer operatorConversion;

    /**
     * 银行卡认证率
     */
    private float authBankCount;

    /**
     * 银行卡认证人数
     */
    private Integer authBankConversion;

    /**
     * 工作信息认证率
     */
    private float companyCount;

    /**
     * 工作信息人数  默认：0
     */
    private Integer companyConversion;
    /**
     * 申请借款人数  默认：0
     */
    private Integer borrowApplyCount;

    /**
     * 申请率
     */
    private Float borrowApplyConversion;

    /**
     * 系数
     */
    private BigDecimal dedutionCoefficient;

    /**
     * 放款人数  默认：0
     */
    private Integer loanCount;

    /**
     * 放款率
     */
    private Float loanConversion;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 平均申请率
     */
    private float averageApplicationRate;

    /**
     * 平均放款率
     */
    private float averageLoanRate;

    //命中系统黑名单
    private Integer hitSystemBlack;
    //命中外部三方黑名单
    private Integer hitOutBlack;

    //命中系统黑名单
    private float hitSystemBlackRate;
    //命中外部三方黑名单
    private float hitOutBlackRate;
    //命中黑名单总数
    private Integer blackTotal;

    //命中黑名单率（总的）
    private float blackTotalRate;

    private Integer channelIos;//ios数量

    private Integer channelAnZuo;//安卓数量

    private Integer channelDuanXin;//短信数量
    private Integer channelDuanXinLv;//短信率
    private Integer channelIosLv;//ios率
    private Integer channelAnZuoLv;//安卓率

    public Integer getChannelIosLv() {

        return channelIosLv;
    }

    public void setChannelIosLv(Integer channelIosLv) {
        this.channelIosLv = channelIosLv;
    }

    public Integer getChannelAnZuoLv() {
        return channelAnZuoLv;
    }

    public void setChannelAnZuoLv(Integer channelAnZuoLv) {
        this.channelAnZuoLv = channelAnZuoLv;
    }

    public Integer getChannelDuanXinLv() {
        return channelDuanXinLv;
    }

    public void setChannelDuanXinLv(Integer channelDuanXinLv) {
        this.channelDuanXinLv = channelDuanXinLv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getDownCount() {
        return downCount;
    }

    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    public float getDownConversion() {
        return downConversion;
    }

    public void setDownConversion(float downConversion) {
        this.downConversion = downConversion;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public float getLoginConversion() {
        return loginConversion;
    }

    public void setLoginConversion(float loginConversion) {
        this.loginConversion = loginConversion;
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public float getIdcardCertificationCount() {
        return idcardCertificationCount;
    }

    public void setIdcardCertificationCount(float idcardCertificationCount) {
        this.idcardCertificationCount = idcardCertificationCount;
    }

    public Integer getIdcardCertificationConversion() {
        return idcardCertificationConversion;
    }

    public void setIdcardCertificationConversion(Integer idcardCertificationConversion) {
        this.idcardCertificationConversion = idcardCertificationConversion;
    }

    public float getPersonalInformationCount() {
        return personalInformationCount;
    }

    public void setPersonalInformationCount(float personalInformationCount) {
        this.personalInformationCount = personalInformationCount;
    }

    public Integer getPersonalInformationConversion() {
        return personalInformationConversion;
    }

    public void setPersonalInformationConversion(Integer personalInformationConversion) {
        this.personalInformationConversion = personalInformationConversion;
    }

    public float getSesameCount() {
        return sesameCount;
    }

    public void setSesameCount(float sesameCount) {
        this.sesameCount = sesameCount;
    }

    public Integer getSesameConversion() {
        return sesameConversion;
    }

    public void setSesameConversion(Integer sesameConversion) {
        this.sesameConversion = sesameConversion;
    }

    public float getOperatorCount() {
        return operatorCount;
    }

    public void setOperatorCount(float operatorCount) {
        this.operatorCount = operatorCount;
    }

    public Integer getOperatorConversion() {
        return operatorConversion;
    }

    public void setOperatorConversion(Integer operatorConversion) {
        this.operatorConversion = operatorConversion;
    }

    public float getAuthBankCount() {
        return authBankCount;
    }

    public void setAuthBankCount(float authBankCount) {
        this.authBankCount = authBankCount;
    }

    public Integer getAuthBankConversion() {
        return authBankConversion;
    }

    public void setAuthBankConversion(Integer authBankConversion) {
        this.authBankConversion = authBankConversion;
    }

    public float getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(float companyCount) {
        this.companyCount = companyCount;
    }

    public Integer getCompanyConversion() {
        return companyConversion;
    }

    public void setCompanyConversion(Integer companyConversion) {
        this.companyConversion = companyConversion;
    }

    public Integer getBorrowApplyCount() {
        return borrowApplyCount;
    }

    public void setBorrowApplyCount(Integer borrowApplyCount) {
        this.borrowApplyCount = borrowApplyCount;
    }

    public Float getBorrowApplyConversion() {
        return borrowApplyConversion;
    }

    public void setBorrowApplyConversion(Float borrowApplyConversion) {
        this.borrowApplyConversion = borrowApplyConversion;
    }

    public BigDecimal getDedutionCoefficient() {
        return dedutionCoefficient;
    }

    public void setDedutionCoefficient(BigDecimal dedutionCoefficient) {
        this.dedutionCoefficient = dedutionCoefficient;
    }

    public Integer getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(Integer loanCount) {
        this.loanCount = loanCount;
    }

    public Float getLoanConversion() {
        return loanConversion;
    }

    public void setLoanConversion(Float loanConversion) {
        this.loanConversion = loanConversion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public float getAverageApplicationRate() {
        return averageApplicationRate;
    }

    public void setAverageApplicationRate(float averageApplicationRate) {
        this.averageApplicationRate = averageApplicationRate;
    }

    public float getAverageLoanRate() {
        return averageLoanRate;
    }

    public void setAverageLoanRate(float averageLoanRate) {
        this.averageLoanRate = averageLoanRate;
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

    public float getHitSystemBlackRate() {
        return hitSystemBlackRate;
    }

    public void setHitSystemBlackRate(float hitSystemBlackRate) {
        this.hitSystemBlackRate = hitSystemBlackRate;
    }

    public float getHitOutBlackRate() {
        return hitOutBlackRate;
    }

    public void setHitOutBlackRate(float hitOutBlackRate) {
        this.hitOutBlackRate = hitOutBlackRate;
    }

    public Integer getBlackTotal() {
        return blackTotal;
    }

    public void setBlackTotal(Integer blackTotal) {
        this.blackTotal = blackTotal;
    }

    public float getBlackTotalRate() {
        return blackTotalRate;
    }

    public void setBlackTotalRate(float blackTotalRate) {
        this.blackTotalRate = blackTotalRate;
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

    public Integer getChannelDuanXin() {
        return channelDuanXin;
    }

    public void setChannelDuanXin(Integer channelDuanXin) {
        this.channelDuanXin = channelDuanXin;
    }
}
