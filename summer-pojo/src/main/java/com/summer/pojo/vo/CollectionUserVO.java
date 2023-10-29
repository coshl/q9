package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserDecrypt;
import com.summer.util.Constant;
import com.summer.util.MoneyChangeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Desc:
 * Created by tl on 2019/1/4
 */
public class CollectionUserVO extends BaseUserDecrypt {
    private Long id;
   // private String realName;
    private String userName;
    private String groupLevel;
    private String loanTimeChg;
    private String createTimeChg;
    private String operate;
    private String detail;
    private String repaymentTimeChg;
    private String paidTimeChg;
    private Integer applyAmount;
    private Integer paidAmount;
    private Integer principalAmount;
    private Integer lateFee;
    private Integer lateDay;
    private Integer loanTerm;
    private Integer overdueCount;
    private double reductionAmount;
    private double reduceAmount;
    private Byte auditStatus;
    private Byte payStatus;
    private Byte payType;
    private String payTypeName;
    private String payStatusName;
    private String remark;
    private String auditStatusName;

    //身份证号码
  //  private String idCard;
    //用户状态
    private Integer userStatus;
    //用户id
    private Integer userId;

    /**
     * 运营商报告taskid
     */
    private String taskId;
    //cs情况
    private String collectionTag;
    //备注
    private String collectionRemark;

    private String collectionDescription;

    private Integer detailId;

    public String getPaidTimeChg() {
        return paidTimeChg;
    }

    public void setPaidTimeChg(String paidTimeChg) {
        this.paidTimeChg = paidTimeChg;
    }

    public double getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(double reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    //申请金额除100 转换元，导出报表时用
    private String applyAmountNormal;
    //应还金额（转换元，导出报表时用）
    private String principalAmountNormal;
    //滞纳金
    private String lateFeeNormal;
    //减免滞纳金
    private String reductionAmountNormal;
    //已还金额
    private String paidAmountNormal;


    public String getPaidAmountNormal() {
        return paidAmountNormal;
    }

    public String getReductionAmountNormal() {
        return reductionAmountNormal;
    }

    public String getLateFeeNormal() {
        return lateFeeNormal;
    }

    public String getApplyAmountNormal() {
        return applyAmountNormal;
    }

    public String getPrincipalAmountNormal() {
        return principalAmountNormal;
    }

    public double getReductionAmount() {
        return reductionAmount;
    }

    public void setReductionAmount(double reductionAmount) {
        this.reductionAmount = reductionAmount;
        this.reductionAmountNormal = MoneyChangeUtil.moneyChangeDollar(reductionAmount).toString();
    }

    public String getAuditStatusName() {
        return auditStatusName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
        if (null != this.auditStatus && 1 == this.auditStatus) {
            this.auditStatusName += "," + this.remark;
        }
    }

    public String getPayStatusName() {
        return payStatusName;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public CollectionUserVO() {
    }

    public CollectionUserVO(String userName, String createTimeChg, String operate, String detail) {
        this.userName = userName;
        this.createTimeChg = createTimeChg;
        this.operate = operate;
        this.detail = detail;
    }

    public String getCreateTimeChg() {
        return createTimeChg;
    }

    public void setCreateTimeChg(String createTimeChg) {
        this.createTimeChg = createTimeChg;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLoanTimeChg() {
        return loanTimeChg;
    }

    public void setLoanTimeChg(String loanTimeChg) {
        this.loanTimeChg = loanTimeChg;
    }

    public String getRepaymentTimeChg() {
        return repaymentTimeChg;
    }

    public void setRepaymentTimeChg(String repaymentTimeChg) {
        this.repaymentTimeChg = repaymentTimeChg;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
        this.paidAmountNormal = MoneyChangeUtil.moneyChangeDollar(paidAmount).toString();
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
        this.payTypeName = Constant.PAY_TYPE_MAP.get(payType);
    }


    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
        this.auditStatusName = Constant.AUDIT_STATUS_MAP.get(auditStatus);
        if (null != this.remark) {
            if (1 == auditStatus) {
                this.auditStatusName += "," + this.remark;
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

/*

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = Constant.groupNameMap.get(groupLevel);
    }

    public Integer getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Integer applyAmount) {
        this.applyAmount = applyAmount;
        this.applyAmountNormal = MoneyChangeUtil.moneyChangeDollar(applyAmount).toString();
    }

    public Integer getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Integer principalAmount) {
        this.principalAmount = principalAmount;
        this.principalAmountNormal = MoneyChangeUtil.moneyChangeDollar(principalAmount).toString();
    }

    public Integer getLateFee() {
        return lateFee;
    }

    public void setLateFee(Integer lateFee) {
        this.lateFee = lateFee;
        this.lateFeeNormal = MoneyChangeUtil.moneyChangeDollar(lateFee).toString();
    }

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Integer overdueCount) {
        this.overdueCount = overdueCount;
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
        this.payStatusName = Constant.REPAY_STATUS_MAP.get(payStatus);
    }

  /*  public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }*/

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCollectionTag() {
        return collectionTag;
    }

    public void setCollectionTag(String collectionTag) {
        this.collectionTag = collectionTag;
    }

    public String getCollectionRemark() {
        return collectionRemark;
    }

    public void setCollectionRemark(String collectionRemark) {
        this.collectionRemark = collectionRemark;
    }

    public String getCollectionDescription() {
        return collectionDescription;
    }

    public void setCollectionDescription(String collectionDescription) {
        this.collectionDescription = collectionDescription;
        if (StringUtils.isNotBlank(collectionDescription)) {
            if (collectionDescription.startsWith(",")) {
                this.collectionDescription = collectionDescription.replaceFirst(",", "");
            }

        }
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
