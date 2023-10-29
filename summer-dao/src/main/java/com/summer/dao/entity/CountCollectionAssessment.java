/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日催收报表
 *
 * @author GeZhuo
 */
public class CountCollectionAssessment {
    /**
     * 主键
     */
    private Long id;

    /**
     * 催收员编号
     */
    private String personId;

    /**
     * 催收员姓名
     */
    private String personName;

    /**
     * 催单公司编号
     */
    private String companyId;

    /**
     * 催单公司名称
     */
    private String companyTitle;

    /**
     * 催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+）
     */
    private String groupId;

    /**
     * 催收员分组name
     */
    private String groupName;

    /**
     * 分组id
     */
    private String orderGroupId;

    /**
     * 订单分组name
     */
    private String orderGroupName;

    /**
     * 本金金额，元
     */
    private BigDecimal loanMoney;

    /**
     * 已还金额，元
     */
    private BigDecimal repaymentMoney;

    /**
     * 未还金额，元
     */
    private BigDecimal notYetRepaymentMoney;

    /**
     * 本金还款率，元
     */
    private BigDecimal repaymentRate;

    /**
     * 迁徙率，元  默认：-1.00
     */
    private BigDecimal migrateRate;

    /**
     * 滞纳金总额，元
     */
    private BigDecimal penalty;

    /**
     * 滞纳金已还款，元
     */
    private BigDecimal repaymentPenalty;

    /**
     * 滞纳金未还款，元
     */
    private BigDecimal notRepaymentPenalty;

    /**
     * 滞纳金回款率，元
     */
    private BigDecimal penaltyRepaymentRate;

    /**
     * 利息总金额，元
     */
    private BigDecimal interest;

    /**
     * 利息已还款，元
     */
    private BigDecimal repaymentInterest;

    /**
     * 利息未还款，元
     */
    private BigDecimal notRepaymentInterest;

    /**
     * 利息回款率，元
     */
    private BigDecimal interestRepaymentRate;

    /**
     * 订单总数  默认：0
     */
    private Integer orderTotal;

    /**
     * 已处理订单数  默认：0
     */
    private Integer disposeOrderNum;

    /**
     * 风控标记订单量  默认：0
     */
    private Integer riskOrderNum;

    /**
     * 已还款订单数  默认：0
     */
    private Integer repaymentOrderNum;

    /**
     * 订单还款率
     */
    private BigDecimal repaymentOrderRate;

    /**
     * 统计时间
     */
    private Date countDate;

    /**
     * 获取 主键 count_collection_assessment.id
     *
     * @return 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 count_collection_assessment.id
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 催收员编号 count_collection_assessment.person_id
     *
     * @return 催收员编号
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * 设置 催收员编号 count_collection_assessment.person_id
     *
     * @param personId 催收员编号
     */
    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
    }

    /**
     * 获取 催收员姓名 count_collection_assessment.person_name
     *
     * @return 催收员姓名
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * 设置 催收员姓名 count_collection_assessment.person_name
     *
     * @param personName 催收员姓名
     */
    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }

    /**
     * 获取 催单公司编号 count_collection_assessment.company_id
     *
     * @return 催单公司编号
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 设置 催单公司编号 count_collection_assessment.company_id
     *
     * @param companyId 催单公司编号
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * 获取 催单公司名称 count_collection_assessment.company_title
     *
     * @return 催单公司名称
     */
    public String getCompanyTitle() {
        return companyTitle;
    }

    /**
     * 设置 催单公司名称 count_collection_assessment.company_title
     *
     * @param companyTitle 催单公司名称
     */
    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle == null ? null : companyTitle.trim();
    }

    /**
     * 获取 催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+） count_collection_assessment.group_id
     *
     * @return 催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+）
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 设置 催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+） count_collection_assessment.group_id
     *
     * @param groupId 催收员分组（3，4，5，6，7对应S1，S2，M1-M2，M2-M3，M3+对应1-10,11-30（1），1个月-2个月，2个月-3个月，3个月+）
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    /**
     * 获取 催收员分组name count_collection_assessment.group_name
     *
     * @return 催收员分组name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置 催收员分组name count_collection_assessment.group_name
     *
     * @param groupName 催收员分组name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * 获取 分组id count_collection_assessment.order_group_id
     *
     * @return 分组id
     */
    public String getOrderGroupId() {
        return orderGroupId;
    }

    /**
     * 设置 分组id count_collection_assessment.order_group_id
     *
     * @param orderGroupId 分组id
     */
    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId == null ? null : orderGroupId.trim();
    }

    /**
     * 获取 订单分组name count_collection_assessment.order_group_name
     *
     * @return 订单分组name
     */
    public String getOrderGroupName() {
        return orderGroupName;
    }

    /**
     * 设置 订单分组name count_collection_assessment.order_group_name
     *
     * @param orderGroupName 订单分组name
     */
    public void setOrderGroupName(String orderGroupName) {
        this.orderGroupName = orderGroupName == null ? null : orderGroupName.trim();
    }

    /**
     * 获取 本金金额，元 count_collection_assessment.loan_money
     *
     * @return 本金金额，元
     */
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    /**
     * 设置 本金金额，元 count_collection_assessment.loan_money
     *
     * @param loanMoney 本金金额，元
     */
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }

    /**
     * 获取 已还金额，元 count_collection_assessment.repayment_money
     *
     * @return 已还金额，元
     */
    public BigDecimal getRepaymentMoney() {
        return repaymentMoney;
    }

    /**
     * 设置 已还金额，元 count_collection_assessment.repayment_money
     *
     * @param repaymentMoney 已还金额，元
     */
    public void setRepaymentMoney(BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }

    /**
     * 获取 未还金额，元 count_collection_assessment.not_yet_repayment_money
     *
     * @return 未还金额，元
     */
    public BigDecimal getNotYetRepaymentMoney() {
        return notYetRepaymentMoney;
    }

    /**
     * 设置 未还金额，元 count_collection_assessment.not_yet_repayment_money
     *
     * @param notYetRepaymentMoney 未还金额，元
     */
    public void setNotYetRepaymentMoney(BigDecimal notYetRepaymentMoney) {
        this.notYetRepaymentMoney = notYetRepaymentMoney;
    }

    /**
     * 获取 本金还款率，元 count_collection_assessment.repayment_rate
     *
     * @return 本金还款率，元
     */
    public BigDecimal getRepaymentRate() {
        return repaymentRate;
    }

    /**
     * 设置 本金还款率，元 count_collection_assessment.repayment_rate
     *
     * @param repaymentRate 本金还款率，元
     */
    public void setRepaymentRate(BigDecimal repaymentRate) {
        this.repaymentRate = repaymentRate;
    }

    /**
     * 获取 迁徙率，元 count_collection_assessment.migrate_rate
     *
     * @return 迁徙率，元
     */
    public BigDecimal getMigrateRate() {
        return migrateRate;
    }

    /**
     * 设置 迁徙率，元 count_collection_assessment.migrate_rate
     *
     * @param migrateRate 迁徙率，元
     */
    public void setMigrateRate(BigDecimal migrateRate) {
        this.migrateRate = migrateRate;
    }

    /**
     * 获取 滞纳金总额，元 count_collection_assessment.penalty
     *
     * @return 滞纳金总额，元
     */
    public BigDecimal getPenalty() {
        return penalty;
    }

    /**
     * 设置 滞纳金总额，元 count_collection_assessment.penalty
     *
     * @param penalty 滞纳金总额，元
     */
    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    /**
     * 获取 滞纳金已还款，元 count_collection_assessment.repayment_penalty
     *
     * @return 滞纳金已还款，元
     */
    public BigDecimal getRepaymentPenalty() {
        return repaymentPenalty;
    }

    /**
     * 设置 滞纳金已还款，元 count_collection_assessment.repayment_penalty
     *
     * @param repaymentPenalty 滞纳金已还款，元
     */
    public void setRepaymentPenalty(BigDecimal repaymentPenalty) {
        this.repaymentPenalty = repaymentPenalty;
    }

    /**
     * 获取 滞纳金未还款，元 count_collection_assessment.not_repayment_penalty
     *
     * @return 滞纳金未还款，元
     */
    public BigDecimal getNotRepaymentPenalty() {
        return notRepaymentPenalty;
    }

    /**
     * 设置 滞纳金未还款，元 count_collection_assessment.not_repayment_penalty
     *
     * @param notRepaymentPenalty 滞纳金未还款，元
     */
    public void setNotRepaymentPenalty(BigDecimal notRepaymentPenalty) {
        this.notRepaymentPenalty = notRepaymentPenalty;
    }

    /**
     * 获取 滞纳金回款率，元 count_collection_assessment.penalty_repayment_rate
     *
     * @return 滞纳金回款率，元
     */
    public BigDecimal getPenaltyRepaymentRate() {
        return penaltyRepaymentRate;
    }

    /**
     * 设置 滞纳金回款率，元 count_collection_assessment.penalty_repayment_rate
     *
     * @param penaltyRepaymentRate 滞纳金回款率，元
     */
    public void setPenaltyRepaymentRate(BigDecimal penaltyRepaymentRate) {
        this.penaltyRepaymentRate = penaltyRepaymentRate;
    }

    /**
     * 获取 利息总金额，元 count_collection_assessment.interest
     *
     * @return 利息总金额，元
     */
    public BigDecimal getInterest() {
        return interest;
    }

    /**
     * 设置 利息总金额，元 count_collection_assessment.interest
     *
     * @param interest 利息总金额，元
     */
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    /**
     * 获取 利息已还款，元 count_collection_assessment.repayment_interest
     *
     * @return 利息已还款，元
     */
    public BigDecimal getRepaymentInterest() {
        return repaymentInterest;
    }

    /**
     * 设置 利息已还款，元 count_collection_assessment.repayment_interest
     *
     * @param repaymentInterest 利息已还款，元
     */
    public void setRepaymentInterest(BigDecimal repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    /**
     * 获取 利息未还款，元 count_collection_assessment.not_repayment_interest
     *
     * @return 利息未还款，元
     */
    public BigDecimal getNotRepaymentInterest() {
        return notRepaymentInterest;
    }

    /**
     * 设置 利息未还款，元 count_collection_assessment.not_repayment_interest
     *
     * @param notRepaymentInterest 利息未还款，元
     */
    public void setNotRepaymentInterest(BigDecimal notRepaymentInterest) {
        this.notRepaymentInterest = notRepaymentInterest;
    }

    /**
     * 获取 利息回款率，元 count_collection_assessment.interest_repayment_rate
     *
     * @return 利息回款率，元
     */
    public BigDecimal getInterestRepaymentRate() {
        return interestRepaymentRate;
    }

    /**
     * 设置 利息回款率，元 count_collection_assessment.interest_repayment_rate
     *
     * @param interestRepaymentRate 利息回款率，元
     */
    public void setInterestRepaymentRate(BigDecimal interestRepaymentRate) {
        this.interestRepaymentRate = interestRepaymentRate;
    }

    /**
     * 获取 订单总数 count_collection_assessment.order_total
     *
     * @return 订单总数
     */
    public Integer getOrderTotal() {
        return orderTotal;
    }

    /**
     * 设置 订单总数 count_collection_assessment.order_total
     *
     * @param orderTotal 订单总数
     */
    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
        this.repaymentOrderRate = BigDecimal.ZERO;

        if (repaymentOrderNum != null && orderTotal > 0) {

            this.repaymentOrderRate = new BigDecimal(repaymentOrderNum / orderTotal).multiply(new BigDecimal(100)).setScale(2,
                    BigDecimal.ROUND_DOWN);
        }
    }

    /**
     * 获取 已处理订单数 count_collection_assessment.dispose_order_num
     *
     * @return 已处理订单数
     */
    public Integer getDisposeOrderNum() {
        return disposeOrderNum;
    }

    /**
     * 设置 已处理订单数 count_collection_assessment.dispose_order_num
     *
     * @param disposeOrderNum 已处理订单数
     */
    public void setDisposeOrderNum(Integer disposeOrderNum) {
        this.disposeOrderNum = disposeOrderNum;
    }

    /**
     * 获取 风控标记订单量 count_collection_assessment.risk_order_num
     *
     * @return 风控标记订单量
     */
    public Integer getRiskOrderNum() {
        return riskOrderNum;
    }

    /**
     * 设置 风控标记订单量 count_collection_assessment.risk_order_num
     *
     * @param riskOrderNum 风控标记订单量
     */
    public void setRiskOrderNum(Integer riskOrderNum) {
        this.riskOrderNum = riskOrderNum;
    }

    /**
     * 获取 已还款订单数 count_collection_assessment.repayment_order_num
     *
     * @return 已还款订单数
     */
    public Integer getRepaymentOrderNum() {
        return repaymentOrderNum;
    }

    /**
     * 设置 已还款订单数 count_collection_assessment.repayment_order_num
     *
     * @param repaymentOrderNum 已还款订单数
     */
    public void setRepaymentOrderNum(Integer repaymentOrderNum) {
        this.repaymentOrderNum = repaymentOrderNum;
        this.repaymentOrderRate = BigDecimal.ZERO;

        if (orderTotal != null && orderTotal > 0) {

            this.repaymentOrderRate =
                    new BigDecimal(repaymentOrderNum * 100.0 / orderTotal).setScale(2,
                            BigDecimal.ROUND_DOWN);
        }
    }

    /**
     * 获取 订单还款率 count_collection_assessment.repayment_order_rate
     *
     * @return 订单还款率
     */
    public BigDecimal getRepaymentOrderRate() {
        return repaymentOrderRate;
    }


    /**
     * 获取 统计时间 count_collection_assessment.count_date
     *
     * @return 统计时间
     */
    public Date getCountDate() {
        return countDate;
    }

    /**
     * 设置 统计时间 count_collection_assessment.count_date
     *
     * @param countDate 统计时间
     */
    public void setCountDate(Date countDate) {
        this.countDate = countDate;
    }
}