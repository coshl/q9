package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserDecrypt;

public class OrderCollectionCallerVo extends BaseUserDecrypt {

    private Integer id;

  //  private String realName;

   /// private String phone;
    private Integer userId;

    private Integer repaymentAmount;

    private Integer loanTerm;

    private Integer principalAmount;

    private Integer lateFee;

    private Integer paidAmount;

    private String loanTime;

    private String repaymentTime;

    private String lastRepaymentTime;

    private String remark;

    private String idCard;

    private Integer userStatus;

    private String channelName;

    private Integer repaymentStatus;

    private String repaymentStatusStr;

    private String today;

    private String userName;

    private String annotation;

    private String taskId;

    //是否是老用户：0、新用户；1；老用户'
    private Integer customerType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }


    /*    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }*/

  /*  public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
*/
    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Integer principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Integer getLateFee() {
        return lateFee;
    }

    public void setLateFee(Integer lateFee) {
        this.lateFee = lateFee;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public String getLastRepaymentTime() {
        return lastRepaymentTime;
    }

    public void setLastRepaymentTime(String lastRepaymentTime) {
        this.lastRepaymentTime = lastRepaymentTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public Integer getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(Integer repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
        if (repaymentStatus == 2) {
            this.repaymentStatusStr = "已还款";
        } else if (!this.today.equals(this.lastRepaymentTime)) {
            this.repaymentStatusStr = "已续期";
        } else {
            this.repaymentStatusStr = "待还款";
        }
    }

    public String getRepaymentStatusStr() {
        return repaymentStatusStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
