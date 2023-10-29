package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.MoneyChangeUtil;

import java.math.BigDecimal;

/**
 * Desc:
 * Created by tl on 2019/1/3
 */
public class PaymentDetailUserVO extends BaseUserDecrypt {
    private Long id;
    private String payTimeChg;
    private Integer repaymentAmount;
    private Integer loanTerm;

    private String repaymentTime;
    private Integer paidAmount;
    private Byte status;
    private Byte overdue;
    private Byte payType;
    private String payTip;
    private String thirdOrderNo;
    private String orderNo;

   // private String realName;
    private Integer forwardCount;
    private Integer overdueCount;


    private String reviewer;
    //还款状态（报表导出时使用）
    private String statusNormal;
    //应还金额（报表导出时使用））
    private String repaymentAmountNormal;
    //实际还款金额（报表导出时使用））
    private String paidAmountNormal;
    //支付方式（报表导出时使用）
    private String payTypeNormal;
    //还款类型
    private String overdueNormal;

    //渠道
    private String channelName;
    //客户类型
    private Integer customerType;

    private String customerTypeName;

    private Integer applyMoney;

    private Double applyAmount = 0.00;

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

    private Integer repaymentId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(Integer repaymentId) {
        this.repaymentId = repaymentId;
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

    public Byte getAuditStatus() {
        return AuditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        AuditStatus = auditStatus;
    }

    public String getStartRepayAmount() {
        return StartRepayAmount;
    }

    public void setStartRepayAmount(String startRepayAmount) {
        StartRepayAmount = startRepayAmount;
    }

    public Double getApplyAmount() {
        return applyAmount;
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

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
        if (null != customerType) {

            if (0 == customerType) {
                this.customerTypeName = "新客";
            } else if (1 == customerType) {
                this.customerTypeName = "老客";
            }

        }

    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getOverdueNormal() {
        return overdueNormal;
    }

    public String getPayTypeNormal() {
        return payTypeNormal;
    }

    public String getRepaymentAmountNormal() {
        return repaymentAmountNormal;
    }

    public String getPaidAmountNormal() {
        return paidAmountNormal;
    }

    public String getStatusNormal() {
        return statusNormal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayTimeChg() {
        return payTimeChg;
    }

    public void setPayTimeChg(String payTimeChg) {
        this.payTimeChg = payTimeChg;
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
        this.repaymentAmountNormal = MoneyChangeUtil.moneyChangeDollar(repaymentAmount).toString();
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
        this.paidAmountNormal = MoneyChangeUtil.moneyChangeDollar(paidAmount).toString();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
        //'付款状态 0未知 1付款中 2付款成功 3付款失败
        if (null != status) {
            switch (status) {
                case 0:
                    this.statusNormal = "待付款";
                    break;
                case 1:
                    this.statusNormal = "付款中";
                    break;
                case 2:
                    this.statusNormal = "付款成功";
                    break;
                case 3:
                    this.statusNormal = "已取消";
                    break;
                default:

                    break;
            }
        } else {
            this.statusNormal = "待付款";
        }
    }

    public Byte getOverdue() {
        return overdue;
    }

    public void setOverdue(Byte overdue) {
        this.overdue = overdue;
        //还款类型 0正常 1逾期 2提前
        if (null != overdue) {
            switch (overdue) {
                case 0:
                    this.overdueNormal = "正常";
                    break;
                case 1:
                    this.overdueNormal = "逾期";
                    break;
                case 2:
                    this.overdueNormal = "提前";
                    break;
                default:
                    break;
            }
        } else {
            this.overdueNormal = "正常";
        }

    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
        //付款方式 0未知 1代扣 2微信线上 3银行卡线上 4支付宝线上 5微信线下 6银行卡线下 7支付宝线下
        if (null != payType) {
            switch (payType) {
                case 0:
                    this.payTypeNormal = "未知";
                    break;
                case 1:
                    this.payTypeNormal = "代扣";
                    break;
                case 2:
                    this.payTypeNormal = "微信线上";
                    break;
                case 3:
                    this.payTypeNormal = "银行卡线上";
                    break;
                case 4:
                    this.payTypeNormal = "支付宝线上";
                    break;
                case 5:
                    this.payTypeNormal = "微信线下";
                    break;
                case 6:
                    this.payTypeNormal = "银行卡线下";
                    break;
                case 7:
                    this.payTypeNormal = "支付宝线下";
                    break;
                default:
                    this.payTypeNormal = "未知";
                    break;
            }
        } else {
            this.payTypeNormal = "未知";
        }
    }

    public String getPayTip() {
        return payTip;
    }

    public void setPayTip(String payTip) {
        this.payTip = payTip;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }
/*

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
*/


    public Integer getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
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
}
