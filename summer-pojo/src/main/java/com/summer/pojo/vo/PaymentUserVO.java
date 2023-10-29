package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.MoneyChangeUtil;

import java.math.BigDecimal;

/**
 * Desc:
 * Created by tl on 2019/1/2
 */
public class PaymentUserVO extends BaseUserDecrypt {
    private Integer id;
    private Integer repaymentAmount;
    private Integer loanTerm;
    private Integer repaymentInterval;
    private String outTradeNo;
    private String repaymentType;
    private Integer repayType;
    private Integer status;
    private Integer userId;
    private String repaymentTimeChg;

   // private String realName;
    private Integer renewalCount;
    private Integer overdueCount;
    private String reviewer;

    private String channelName;
    //客户类型
    private Integer customerType;
    //客服类型
    private String customerTypeName;
    //申请金额
    private Integer applyMoney;
    //申请金额转换为元
    private Double applyAmount = 0.00;
    private Double feeAmount = 0.00;
    private Double intoMoney = 0.00;

    /**
     * 修改应还金额备注
     */
    private String remark;
    /**
     * 修改的上一次的金额
     */
    private String beforeRepayAmount;
    /**
     * 通过的状态
     */
    private Byte AuditStatus;
    /**
     * 最开始的金额
     */
    private String StartRepayAmount;
    //已还金额
    private Integer paidAmount;
    //减免金额
    private Integer reduceAmount;

    public Double getIntoMoney() {
        return intoMoney;
    }

    public void setIntoMoney(Integer intoMoney) {
        this.intoMoney = new BigDecimal(intoMoney / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = new BigDecimal(feeAmount / 100.0).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Integer reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public String getStartRepayAmount() {
        return StartRepayAmount;
    }

    public void setStartRepayAmount(String startRepayAmount) {
        StartRepayAmount = startRepayAmount;
    }

    public Byte getAuditStatus() {
        return AuditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        AuditStatus = auditStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBeforeRepayAmount() {
        return beforeRepayAmount;
    }

    public void setBeforeRepayAmount(String beforeRepayAmount) {
        this.beforeRepayAmount = beforeRepayAmount;
    }

    public Double getApplyAmount() {
        return applyAmount;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public Integer getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(Integer applyMoney) {
        this.applyMoney = applyMoney;
        if (null != applyMoney) {
            BigDecimal applyMoneyDec = new BigDecimal(applyMoney);
            BigDecimal change = new BigDecimal(100.0);
            this.applyAmount = applyMoneyDec.divide(change, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
        if (null != customerType) {
            if (0 == customerType) {
                this.customerTypeName = "新用户";
            } else if (1 == customerType) {
                this.customerTypeName = "老用户";
            }
        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    //应还金额（除100，导出报表时使用）
    private String repaymentAmountNormal;


    public String getRepaymentAmountNormal() {
        return repaymentAmountNormal;
    }


    public Integer getRepayType() {
        return repayType;
    }

    public void setRepayType(Integer repayType) {
        this.repayType = repayType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
        if (null != repaymentAmount) {
            //已还金额或者减免的金额
            Integer repayedMoney = 0;
            if (null != this.paidAmount) {
                repayedMoney += this.paidAmount;
            }
            if (null != this.reduceAmount) {
                repayedMoney += this.reduceAmount;
            }
            //最终应还金额 = 应还总金额-减免金额-已还金额
            this.repaymentAmount -= repayedMoney;
        }
        this.repaymentAmountNormal = MoneyChangeUtil.moneyChangeDollar(this.repaymentAmount).toString();
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getRepaymentInterval() {
        return repaymentInterval;
    }

    public void setRepaymentInterval(Integer repaymentInterval) {
        this.repaymentInterval = repaymentInterval;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getRepaymentTimeChg() {
        return repaymentTimeChg;
    }

    public void setRepaymentTimeChg(String repaymentTimeChg) {
        this.repaymentTimeChg = repaymentTimeChg;
    }
/*
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }*/


    public Integer getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(Integer renewalCount) {
        this.renewalCount = renewalCount;
    }

    public Integer getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Integer overdueCount) {
        this.overdueCount = overdueCount;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    
}
