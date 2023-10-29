package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.MoneyChangeUtil;

import java.math.BigDecimal;

/**
 * Desc:
 * Created by tl on 2018/12/21
 */
public class RenewalUserVO extends BaseUserDecrypt {
    private Long id;
    private String createTimeChg;
    private Integer renewalFee;


    private Integer repaymentAmount;
    private Integer paidAmount;
    private Integer amountToPay;

    private Byte renewalDay;
    private String repaymentTimeChg;
    private String remark;
    private String third;

    private Byte status;

    private Byte payType;


    private Byte renewalNo;

    private String outTradeNo;



    private String reviewer;

    //应还金额还款、除100（导出报表时）
    private String repaymentAmountNormal;
    //续期费除100(导出报表时)
    private String renewalFeeNormal;
    //支付方式（报表导出）
    private String payTypeNormal;
    //续期状态
    private String statusNormal;

    //渠道
    private String channelName;

    //申请金额
    private Integer applyMoney;
    //申请金额
    private Double applyAmount = 0.00;

    public Double getApplyAmount() {
        return applyAmount;
    }

    //续期类型
    private Integer renewalType;

    private String renewalTypeName;

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getRenewalTypeName() {
        return renewalTypeName;
    }

    public Integer getRenewalType() {
        return renewalType;
    }

    public void setRenewalType(Integer renewalType) {
        this.renewalType = renewalType;
        //'续期类型：0=未知，1=线上续期，2=线下续期
        if (null != renewalType) {
            if (1 == renewalType) {
                this.renewalTypeName = "线上续期";
            } else if (2 == renewalType) {
                this.renewalTypeName = "线下续期";
            } else {
                this.renewalTypeName = "未知";
            }
        } else {
            this.renewalTypeName = "未知";
        }
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getStatusNormal() {
        return statusNormal;
    }

    public String getPayTypeNormal() {
        return payTypeNormal;
    }

    public String getRepaymentAmountNormal() {
        return repaymentAmountNormal;
    }

    public String getRenewalFeeNormal() {
        return renewalFeeNormal;
    }

    public Integer getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Integer amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    public Integer getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(Integer renewalFee) {
        this.renewalFee = renewalFee;
        this.renewalFeeNormal = MoneyChangeUtil.moneyChangeDollar(renewalFee).toString();
    }

    public Integer getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Integer repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
        this.repaymentAmountNormal = MoneyChangeUtil.moneyChangeDollar(repaymentAmount).toString();
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Byte getRenewalDay() {
        return renewalDay;
    }

    public void setRenewalDay(Byte renewalDay) {
        this.renewalDay = renewalDay;
    }

    public String getRepaymentTimeChg() {
        return repaymentTimeChg;
    }

    public void setRepaymentTimeChg(String repaymentTimeChg) {
        this.repaymentTimeChg = repaymentTimeChg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
        if (null != status) {
            switch (status) {
                case 0:
                    this.statusNormal = "待付款";
                    break;
                case 1:
                    this.statusNormal = "续期中";
                    break;
                case 2:
                    this.statusNormal = "续期成功";
                    break;
                case 3:
                    this.statusNormal = "已取消";
                    break;
                default:
                    this.statusNormal = "待付款";
                    break;

            }
        }
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
        if (null != payType) {
            switch (payType) {
                case 0:
                    this.payTypeNormal = "未知";
                    break;
                case 1:
                    this.payTypeNormal = "支付宝";
                    break;
                case 2:
                    this.payTypeNormal = "微信";
                    break;
                case 3:
                    this.payTypeNormal = "银行卡";
                    break;
                case 4:
                    this.payTypeNormal = "其他";
                    break;
                default:
                    this.payTypeNormal = "未知";
                    break;
            }
        }
    }

    public Byte getRenewalNo() {
        return renewalNo;
    }

    public void setRenewalNo(Byte renewalNo) {
        this.renewalNo = renewalNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }



    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}
