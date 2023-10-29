package com.summer.pojo.dto;


import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 添加渠道DTO
 */
public class PlateformChannelDTO {
    /**
     * 渠道名称
     */
    @NotBlank(message = "渠道名称不能为空")
    private String channelName;
    /**
     * 合作价格
     */

    private Double price;
    /**
     * 渠道商经理名：合作联系人
     */

    private String managerName;
    /**
     * 合作联系人电话：也是乙方账号
     */
    @NotBlank(message = "合作联系人电话不能为空")

    private String managerPhone;
    /**
     * 扣量系数
     */

    private BigDecimal decreasePercentage;
    /**
     * 结算方式
     */

    private Integer paymentType;
    /**
     * 结算模式
     */

    private Integer paymentMode;
    /**
     * 合作模式
     */

    private Integer cooperationMode;
    /**
     * 公司名称
     */

    private String companyName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 操作人id
     */
    private Integer platefromUserId;


    /**
     * 风控类型：4=pb风控，5=mb风控
     */
    private Integer riskType;
    /**
     * 风控分数
     */
    private String riskScore;
    //借款期限
    private Integer expire;
    //借款服务费
    private Double serviceCharge;
    //借款利息
    private Double borrowInterest;
    // 续期期限
    private Integer renewalExpire;

    /**
     * pc浏览器打开开关
     */
    private Integer pcSwitch;

    /**
     * 风控拒绝开关
     */
    private Integer riskSwitch;

    //该渠道首借金额
    private Integer loanAmount;

    //防撸开关 0=开启，1关闭
    private Integer stripSwitch;
    //审核开关 0=总开关，1手动待人工复审，2自动审核
    private Integer auditSwitch;

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getRiskSwitch() {
        return riskSwitch;
    }

    public void setRiskSwitch(Integer riskSwitch) {
        this.riskSwitch = riskSwitch;
    }

    public Integer getPcSwitch() {
        return pcSwitch;
    }

    public void setPcSwitch(Integer pcSwitch) {
        this.pcSwitch = pcSwitch;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getBorrowInterest() {
        return borrowInterest;
    }

    public void setBorrowInterest(Double borrowInterest) {
        this.borrowInterest = borrowInterest;
    }

    public Integer getRenewalExpire() {
        return renewalExpire;
    }

    public void setRenewalExpire(Integer renewalExpire) {
        this.renewalExpire = renewalExpire;
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public BigDecimal getDecreasePercentage() {
        return decreasePercentage;
    }

    public void setDecreasePercentage(BigDecimal decreasePercentage) {
        this.decreasePercentage = decreasePercentage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getCooperationMode() {
        return cooperationMode;
    }

    public void setCooperationMode(Integer cooperationMode) {
        this.cooperationMode = cooperationMode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPlatefromUserId() {
        return platefromUserId;
    }

    public void setPlatefromUserId(Integer platefromUserId) {
        this.platefromUserId = platefromUserId;
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
