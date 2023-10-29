package com.summer.dao.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 渠道实体类
 */
public class PlateformChannel implements Serializable {


    private static final long serialVersionUID = 6281357354281598576L;
    private Integer id;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 渠道商管理员联系方式
     */
    private String managerPhone;
    /**
     * 渠道商经理名字
     */
    private String managerName;
    /**
     * 系统操作人
     */
    private Integer plateformUserId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 合作价格
     */
    private Integer price;
    /**
     * 结算模式
     */
    private Integer paymentMode;
    /**
     * 结算方式
     */
    private Integer paymentType;
    /**
     * 合作模式
     */
    private Integer cooperationMode;
    /**
     * 渠道扣量比例
     */
    private BigDecimal decreasePercentage;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

    private Date createTime;

    private Date updateTime;

    /**
     * 投放链接
     */
    private String link;

    /**
     * 渠道后台连接
     */
    private String backStage;
    /**
     * 冻结状态：冻结状态：0表示未冻结，1表示冻结
     */
    private Integer freezeStatus;
    /**
     * 禁用状态：0表示未禁用，1表示禁用
     */
    private Integer forbiddenStatus;
    /***/
    private String channelCode;
    /**
     * 微信打开链接开关：0表示允许，1表示禁用
     */
    private Integer wechatSwitch;
    /**
     * 贷超打开链接开关：0表示允许，1表示禁用
     */
    private Integer creditSuperSwitch;
    /**
     * 浏览器打开链接开关：0表示允许，1表示禁用
     */
    private Integer browserSwitch;
    /**
     * 风控类型：4=pb风控，5=mb风控
     */
    private Integer riskType;
    /**
     * 风控分数
     */
    private String riskScore;
    /**
     * pc浏览器打开开关
     */
    private Integer pcSwitch;
    /**
     * 风控拒绝开关
     */
    private Integer riskSwitch;

    //防撸开关 0=开启，1关闭
    private Integer stripSwitch;
    //审核开关 0=总开关，1手动待人工复审，2自动审核
    private Integer auditSwitch;

    public Integer getPcSwitch() {
        return pcSwitch;
    }

    public void setPcSwitch(Integer pcSwitch) {
        this.pcSwitch = pcSwitch;
    }

    public Integer getRiskType() {
        return riskType;
    }

    public void setRiskType(Integer riskType) {
        this.riskType = riskType;
    }

    public String getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(String riskScore) {
        this.riskScore = riskScore;
    }

    public Integer getBrowserSwitch() {
        return browserSwitch;
    }

    public void setBrowserSwitch(Integer browserSwitch) {
        this.browserSwitch = browserSwitch;
    }

    public Integer getCreditSuperSwitch() {
        return creditSuperSwitch;
    }

    public void setCreditSuperSwitch(Integer creditSuperSwitch) {
        this.creditSuperSwitch = creditSuperSwitch;
    }

    public Integer getWechatSwitch() {
        return wechatSwitch;
    }

    public void setWechatSwitch(Integer wechatSwitch) {
        this.wechatSwitch = wechatSwitch;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBackStage() {
        return backStage;
    }

    public void setBackStage(String backStage) {
        this.backStage = backStage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Integer getPlateformUserId() {
        return plateformUserId;
    }

    public void setPlateformUserId(Integer plateformUserId) {
        this.plateformUserId = plateformUserId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(Integer cooperationMode) {
        this.cooperationMode = cooperationMode;
    }

    public BigDecimal getDecreasePercentage() {
        return decreasePercentage;
    }

    public void setDecreasePercentage(BigDecimal decreasePercentage) {
        this.decreasePercentage = decreasePercentage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(Integer freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public Integer getForbiddenStatus() {
        return forbiddenStatus;
    }

    public void setForbiddenStatus(Integer forbiddenStatus) {
        this.forbiddenStatus = forbiddenStatus;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getRiskSwitch() {
        return riskSwitch;
    }

    public void setRiskSwitch(Integer riskSwitch) {
        this.riskSwitch = riskSwitch;
    }

    public Integer getStripSwitch() {
        return stripSwitch;
    }

    public void setStripSwitch(Integer stripSwitch) {
        this.stripSwitch = stripSwitch;
    }

    public Integer getAuditSwitch() {
        return auditSwitch;
    }

    public void setAuditSwitch(Integer auditSwitch) {
        this.auditSwitch = auditSwitch;
    }
}