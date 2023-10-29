package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.Constant;
import com.summer.util.MoneyChangeUtil;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
public class BorrowUserVO extends BaseUserDecrypt {

    private Integer id;
    private Integer pid;
    private Integer borrowId;
    private Integer applyAmount;
    //可借金额
    private Integer amountAvailable;
    private Integer loanTerm;
    private Integer loanFee;
    private Integer intoMoney;
    private Integer isWhite;
    private Integer loanAmount;
    //客户端类型：0表示未知，1表示安卓，2表示IOS
    private Integer clientType;
    //是否是老用户：0：新用户；1；老用户
    private Integer customerType;
    private Integer creditLevel;
    private Byte status;
    private Byte borrowStatus;
    private Byte auditStatus;
    private String auditStatusName;
    private String loanStatusName;
    private String orderStatusName;
    private Byte authenticStatus;
    private Byte hitRiskTimes;
    private String trialTimeChg;
    private String authenticStatusName;
    private String blackReason;
    private String createTimeChg;
    private String loanTimeChg;
    private String applyTimeChg;
    private String reviewRemark;
    private String outTradeNo;
   // private String realName;
    private String channelName;
    private Integer renewalCount;
    private Integer reloanCount;
    //逾期次数
    private Integer overdueCount;
    private Integer bankCardId;
    private Integer serviceCharge;
    //分配的审核人员
    private String reviewer;
    //实际审核人员
    private String loanReviewUser;
    private String payRemark;
    private String flowNo;
    private String bankAccount;
    private String trialRemark;
    private String loanReviewRemark;
    private String borrowStatusName;
    private Integer interest;
    //申请金额导出报表除100
    private String applyAmountNormal;
    //批复金额
    private String amountAvailableNormal;
    //实际到账
    private String intoMoneyNormal;
    //状态导出报表时
    private String statusNormal;
    //用户ID
    private Integer userId;
    private Integer isBlack;
    //提额的开关
    private Integer increaseStatus;
    //起飞建议
    private String advice;

    //身份证号
   // private String idCard;

    //用户注册时的IP
    private String createIp;

    private String loanReviewTime;
    /**
     * 运营商报告taskid
     */
    private String taskId;

    public Integer getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(Integer isBlack) {
        this.isBlack = isBlack;
    }

    public Integer getIsWhite() {
        return isWhite;
    }

    public void setIsWhite(Integer isWhite) {
        this.isWhite = isWhite;

    }

    public String getLoanStatusName() {
        return loanStatusName;
    }

    public Integer getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(Integer creditLevel) {
        this.creditLevel = creditLevel;
        if (null != creditLevel && null != auditStatus) {
            if (auditStatus > 5 && creditLevel == 1) {
                this.auditStatusName = "人审通过";
            }
            if (auditStatus > 5 && creditLevel == 2) {
                this.auditStatusName = "机审通过";
            }
        }
    }

    public Integer getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Integer overdueCount) {
        this.overdueCount = overdueCount;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
        if (null != advice) {
            this.auditStatusName += "，" + advice;
            this.borrowStatusName += "，" + advice;
        }

    }

    public String getStatusNormal() {
        return statusNormal;
    }

    public String getApplyAmountNormal() {
        return applyAmountNormal;
    }

    public String getAmountAvailableNormal() {
        return amountAvailableNormal;
    }

    public String getIntoMoneyNormal() {
        return intoMoneyNormal;
    }


    private double interestNormal;

    public double getInterestNormal() {
        return interestNormal;
    }

    public String getAuditStatusName() {
        return auditStatusName;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
        this.auditStatusName = Constant.BORROW_FRONT_MAP.get(auditStatus);
        this.loanStatusName = Constant.LOAN_STATUS_MAP.get(auditStatus);
        if (null != creditLevel) {
            if (auditStatus > 5 && creditLevel == 1) {
                this.auditStatusName = "人审通过";
            }
            if (auditStatus > 5 && creditLevel == 2) {
                this.auditStatusName = "机审通过";
            }
        }
        if (auditStatus > 7) {
            this.loanStatusName = "放款成功";
        }

        if (null != this.trialRemark) {
            if (1 == auditStatus || 0 == auditStatus) {
                this.auditStatusName += "," + this.trialRemark;
            }
        }
        if (null != this.trialRemark && auditStatus > 2) {
            this.auditStatusName += "," + this.loanReviewRemark;
        }


    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
        if (pid != null && pid == 0) {
            this.reviewer = "机审";
        }
    }

    public String getBorrowStatusName() {
        return borrowStatusName;
    }

    public String getTrialRemark() {
        return trialRemark;
    }

    public void setTrialRemark(String trialRemark) {
        this.trialRemark = trialRemark;
        if (null != this.borrowStatus && null != trialRemark) {
            if (0 == this.borrowStatus || 1 == this.borrowStatus) {
                this.borrowStatusName += "," + this.trialRemark;
                this.orderStatusName += "," + this.trialRemark;
            }
        }

        if (null != this.auditStatus && null != trialRemark) {
            if (0 == this.auditStatus || 1 == this.auditStatus || -2 == this.auditStatus) {
                this.auditStatusName += "," + this.trialRemark;
            }
        }

    }

    public String getLoanReviewRemark() {
        return loanReviewRemark;
    }

    public void setLoanReviewRemark(String loanReviewRemark) {
        this.loanReviewRemark = loanReviewRemark;
        if (null != this.borrowStatus && (this.borrowStatus > 2) && null != loanReviewRemark) {
            this.borrowStatusName += "," + this.loanReviewRemark;
        }
        if (null != this.borrowStatus && (this.borrowStatus == 4 || this.borrowStatus == 5) && null != loanReviewRemark) {
            this.orderStatusName += "," + this.loanReviewRemark;
        }
        if (null != this.auditStatus && null != this.loanReviewRemark && (this.auditStatus > 2)) {
            this.auditStatusName += "," + this.loanReviewRemark;
        }

    }

    public String getAuthenticStatusName() {
        return authenticStatusName;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Byte getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(Byte borrowStatus) {
        this.borrowStatus = borrowStatus;
        this.borrowStatusName = Constant.USER_FRONT_MAP.get(borrowStatus);
        this.orderStatusName = Constant.NEW_USER_FRONT_MAP.get(borrowStatus);
        if (null != this.trialRemark) {
            if (0 == borrowStatus || 1 == borrowStatus || -2 == borrowStatus) {
                this.borrowStatusName += "," + this.trialRemark;
                this.orderStatusName += "," + this.trialRemark;
            }
        }
        if ((borrowStatus == 7 || borrowStatus == 6) && null != this.payRemark) {
            this.borrowStatusName += "," + this.payRemark;
            this.orderStatusName += "," + this.payRemark;
            this.loanStatusName += "," + this.payRemark;
        }
        if (null != this.loanReviewRemark) {
            if (borrowStatus > 2) {
                this.borrowStatusName += "," + this.loanReviewRemark;
            }
            if (null != this.borrowStatus && (this.borrowStatus == 4 || this.borrowStatus == 5) && null != loanReviewRemark) {
                this.orderStatusName += "," + this.loanReviewRemark;
            }
        }

    }

    public Byte getAuthenticStatus() {
        return authenticStatus;
    }

    public void setAuthenticStatus(Byte authenticStatus) {
        this.authenticStatus = authenticStatus;
        this.authenticStatusName = Constant.AUTHENTIC_STATUS_MAP.get(authenticStatus);
    }

    public String getBlackReason() {
        return blackReason;
    }

    public void setBlackReason(String blackReason) {
        this.blackReason = blackReason;
    }

    public String getApplyTimeChg() {
        return applyTimeChg;
    }

    public void setApplyTimeChg(String applyTimeChg) {
        this.applyTimeChg = applyTimeChg;
    }

    public Integer getReloanCount() {
        return reloanCount;
    }

    public void setReloanCount(Integer reloanCount) {
        this.reloanCount = reloanCount;
    }


    public Integer getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Integer serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public Integer getInterest() {
        return interest;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
        if (null != interest) {
            this.interestNormal = interest / 100.0;
        }
    }

    public String getTrialTimeChg() {
        return trialTimeChg;
    }

    public void setTrialTimeChg(String trialTimeChg) {
        this.trialTimeChg = trialTimeChg;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    public String getLoanTimeChg() {
        return loanTimeChg;
    }

    public void setLoanTimeChg(String loanTimeChg) {
        this.loanTimeChg = loanTimeChg;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(Integer loanFee) {
        this.loanFee = loanFee;
    }

    public Integer getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(Integer intoMoney) {
        this.intoMoney = intoMoney;
        this.intoMoneyNormal = MoneyChangeUtil.moneyChangeDollar(intoMoney).toString();
    }


    public Integer getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Integer bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        if (((borrowStatus != null && (borrowStatus == 7 || borrowStatus == 6)) || (auditStatus != null && (auditStatus == 7 || auditStatus == 6))) && null != payRemark) {
//            if(payRemark.contains("余额不足")){
//                payRemark="余额不足";
//            }
            this.borrowStatusName += "," + payRemark;
            this.orderStatusName += "," + payRemark;
            this.loanStatusName += "," + payRemark;
        }
        this.payRemark = payRemark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Integer applyAmount) {
        this.applyAmount = applyAmount;
        this.applyAmountNormal = MoneyChangeUtil.moneyChangeDollar(applyAmount).toString();

    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
        this.amountAvailableNormal = MoneyChangeUtil.moneyChangeDollar(amountAvailable).toString();

    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getHitRiskTimes() {
        return hitRiskTimes;
    }

    public void setHitRiskTimes(Byte hitRiskTimes) {
        this.hitRiskTimes = hitRiskTimes;
    }


    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
/*
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }*/


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
        if (pid != null && pid == 0) {
            this.reviewer = "机审";
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIncreaseStatus() {
        return increaseStatus;
    }

    public void setIncreaseStatus(Integer increaseStatus) {
        this.increaseStatus = increaseStatus;
    }

    public String getLoanReviewUser() {
        return loanReviewUser;
    }

    public void setLoanReviewUser(String loanReviewUser) {
        this.loanReviewUser = loanReviewUser;
    }

   /* public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }*/

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getLoanReviewTime() {
        return loanReviewTime;
    }

    public void setLoanReviewTime(String loanReviewTime) {
        this.loanReviewTime = loanReviewTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
