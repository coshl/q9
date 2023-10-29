/* https://github.com/12641561 */
package com.summer.dao.entity;

import com.summer.pojo.vo.CountLoanByUserTypeVO;

import java.util.Date;
import java.util.List;

/**
 * LoanReport
 * Description:
 *
 * @author: GeZhuo
 * Date: 2019/2/27
 */
public class LoanReport {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 放款总单数  默认：0
     */
    private Integer loanOrderCount;

    /**
     * 放款总额  默认：0
     */
    private Long moneyAmountCount;

    /**
     * 老用户放款单数  默认：0
     */
    private Integer oldLoanOrderCount;

    /**
     * 老用户放款总额  默认：0
     */
    private Long oldLoanMoneyCount;

    /**
     * 新用户放款单数  默认：0
     */
    private Integer newLoanOrderCount;

    /**
     * 新用户放款金额  默认：0
     */
    private Long newLoanMoneyCount;

    /**
     * 7天期限放款单数  默认：0
     */
    private Integer loanSevendayCount;

    /**
     * 14天期限放款单数  默认：0
     */
    private Integer loanFourteendayCount;

    /**
     * 21天期限放款单数  默认：0
     */
    private Integer loanTwentyoneCount;

    /**
     * 7天期限放款总额  默认：0
     */
    private Long sevendayMoenyCount;

    /**
     * 14天期限放款总额  默认：0
     */
    private Long fourteendayMoneyCount;

    /**
     * 21天期限放款总额  默认：0
     */
    private Integer twentyonedayMoneyCount;

    /**
     * 报表日期
     */
    private Date reportDate;

    /**
     * 建表时间
     */
    private Date createdAt;

    /**
     * 借款成功人数  默认：0
     */
    private Integer borrowSucCount;

    /**
     * 注册人数  默认：0
     */
    private Integer registerCount;

    /**
     * 借款人数  默认：0
     */
    private Integer borrowApplyCount;

    public LoanReport() {
    }

    public LoanReport(Integer loanOrderCount, Long moneyAmountCount, Date reportDate, Date createdAt,
                      Integer registerCount, Integer borrowApplyCount, List<CountLoanByUserTypeVO> vos) {
        this.loanOrderCount = loanOrderCount;
        this.moneyAmountCount = moneyAmountCount;
        this.reportDate = reportDate;
        this.createdAt = createdAt;
        this.registerCount = registerCount;
        this.borrowApplyCount = borrowApplyCount;
        this.borrowSucCount = loanOrderCount;
        for (CountLoanByUserTypeVO vo : vos) {
            if (vo.getCustomerType() == 0) {
                this.newLoanOrderCount = vo.getNum();
                this.newLoanMoneyCount = vo.getLoanAmount();
            } else if (vo.getCustomerType() == 1) {
                this.oldLoanOrderCount = vo.getNum();
                this.oldLoanMoneyCount = vo.getLoanAmount();
            }
        }
    }

    /**
     * 获取 主键 loan_report.id
     *
     * @return 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 loan_report.id
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 放款总单数 loan_report.loan_order_count
     *
     * @return 放款总单数
     */
    public Integer getLoanOrderCount() {
        return loanOrderCount;
    }

    /**
     * 设置 放款总单数 loan_report.loan_order_count
     *
     * @param loanOrderCount 放款总单数
     */
    public void setLoanOrderCount(Integer loanOrderCount) {
        this.loanOrderCount = loanOrderCount;
    }

    /**
     * 获取 放款总额 loan_report.money_amount_count
     *
     * @return 放款总额
     */
    public Long getMoneyAmountCount() {
        return moneyAmountCount;
    }

    /**
     * 设置 放款总额 loan_report.money_amount_count
     *
     * @param moneyAmountCount 放款总额
     */
    public void setMoneyAmountCount(Long moneyAmountCount) {
        this.moneyAmountCount = moneyAmountCount;
    }

    /**
     * 获取 老用户放款单数 loan_report.old_loan_order_count
     *
     * @return 老用户放款单数
     */
    public Integer getOldLoanOrderCount() {
        return oldLoanOrderCount;
    }

    /**
     * 设置 老用户放款单数 loan_report.old_loan_order_count
     *
     * @param oldLoanOrderCount 老用户放款单数
     */
    public void setOldLoanOrderCount(Integer oldLoanOrderCount) {
        this.oldLoanOrderCount = oldLoanOrderCount;
    }

    /**
     * 获取 老用户放款总额 loan_report.old_loan_money_count
     *
     * @return 老用户放款总额
     */
    public Long getOldLoanMoneyCount() {
        return oldLoanMoneyCount;
    }

    /**
     * 设置 老用户放款总额 loan_report.old_loan_money_count
     *
     * @param oldLoanMoneyCount 老用户放款总额
     */
    public void setOldLoanMoneyCount(Long oldLoanMoneyCount) {
        this.oldLoanMoneyCount = oldLoanMoneyCount;
    }

    /**
     * 获取 新用户放款单数 loan_report.new_loan_order_count
     *
     * @return 新用户放款单数
     */
    public Integer getNewLoanOrderCount() {
        return newLoanOrderCount;
    }

    /**
     * 设置 新用户放款单数 loan_report.new_loan_order_count
     *
     * @param newLoanOrderCount 新用户放款单数
     */
    public void setNewLoanOrderCount(Integer newLoanOrderCount) {
        this.newLoanOrderCount = newLoanOrderCount;
    }

    /**
     * 获取 新用户放款金额 loan_report.new_loan_money_count
     *
     * @return 新用户放款金额
     */
    public Long getNewLoanMoneyCount() {
        return newLoanMoneyCount;
    }

    /**
     * 设置 新用户放款金额 loan_report.new_loan_money_count
     *
     * @param newLoanMoneyCount 新用户放款金额
     */
    public void setNewLoanMoneyCount(Long newLoanMoneyCount) {
        this.newLoanMoneyCount = newLoanMoneyCount;
    }

    /**
     * 获取 7天期限放款单数 loan_report.loan_sevenday_count
     *
     * @return 7天期限放款单数
     */
    public Integer getLoanSevendayCount() {
        return loanSevendayCount;
    }

    /**
     * 设置 7天期限放款单数 loan_report.loan_sevenday_count
     *
     * @param loanSevendayCount 7天期限放款单数
     */
    public void setLoanSevendayCount(Integer loanSevendayCount) {
        this.loanSevendayCount = loanSevendayCount;
    }

    /**
     * 获取 14天期限放款单数 loan_report.loan_fourteenday_count
     *
     * @return 14天期限放款单数
     */
    public Integer getLoanFourteendayCount() {
        return loanFourteendayCount;
    }

    /**
     * 设置 14天期限放款单数 loan_report.loan_fourteenday_count
     *
     * @param loanFourteendayCount 14天期限放款单数
     */
    public void setLoanFourteendayCount(Integer loanFourteendayCount) {
        this.loanFourteendayCount = loanFourteendayCount;
    }

    /**
     * 获取 21天期限放款单数 loan_report.loan_twentyOne_count
     *
     * @return 21天期限放款单数
     */
    public Integer getLoanTwentyoneCount() {
        return loanTwentyoneCount;
    }

    /**
     * 设置 21天期限放款单数 loan_report.loan_twentyOne_count
     *
     * @param loanTwentyoneCount 21天期限放款单数
     */
    public void setLoanTwentyoneCount(Integer loanTwentyoneCount) {
        this.loanTwentyoneCount = loanTwentyoneCount;
    }

    /**
     * 获取 7天期限放款总额 loan_report.sevenday_moeny_count
     *
     * @return 7天期限放款总额
     */
    public Long getSevendayMoenyCount() {
        return sevendayMoenyCount;
    }

    /**
     * 设置 7天期限放款总额 loan_report.sevenday_moeny_count
     *
     * @param sevendayMoenyCount 7天期限放款总额
     */
    public void setSevendayMoenyCount(Long sevendayMoenyCount) {
        this.sevendayMoenyCount = sevendayMoenyCount;
    }

    /**
     * 获取 14天期限放款总额 loan_report.fourteenday_money_count
     *
     * @return 14天期限放款总额
     */
    public Long getFourteendayMoneyCount() {
        return fourteendayMoneyCount;
    }

    /**
     * 设置 14天期限放款总额 loan_report.fourteenday_money_count
     *
     * @param fourteendayMoneyCount 14天期限放款总额
     */
    public void setFourteendayMoneyCount(Long fourteendayMoneyCount) {
        this.fourteendayMoneyCount = fourteendayMoneyCount;
    }

    /**
     * 获取 21天期限放款总额 loan_report.twentyOneday_money_count
     *
     * @return 21天期限放款总额
     */
    public Integer getTwentyonedayMoneyCount() {
        return twentyonedayMoneyCount;
    }

    /**
     * 设置 21天期限放款总额 loan_report.twentyOneday_money_count
     *
     * @param twentyonedayMoneyCount 21天期限放款总额
     */
    public void setTwentyonedayMoneyCount(Integer twentyonedayMoneyCount) {
        this.twentyonedayMoneyCount = twentyonedayMoneyCount;
    }

    /**
     * 获取 报表日期 loan_report.report_date
     *
     * @return 报表日期
     */
    public Date getReportDate() {
        return reportDate;
    }

    /**
     * 设置 报表日期 loan_report.report_date
     *
     * @param reportDate 报表日期
     */
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * 获取 建表时间 loan_report.created_at
     *
     * @return 建表时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置 建表时间 loan_report.created_at
     *
     * @param createdAt 建表时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取 借款成功人数 loan_report.borrow_suc_count
     *
     * @return 借款成功人数
     */
    public Integer getBorrowSucCount() {
        return borrowSucCount;
    }

    /**
     * 设置 借款成功人数 loan_report.borrow_suc_count
     *
     * @param borrowSucCount 借款成功人数
     */
    public void setBorrowSucCount(Integer borrowSucCount) {
        this.borrowSucCount = borrowSucCount;
    }

    /**
     * 获取 注册人数 loan_report.register_count
     *
     * @return 注册人数
     */
    public Integer getRegisterCount() {
        return registerCount;
    }

    /**
     * 设置 注册人数 loan_report.register_count
     *
     * @param registerCount 注册人数
     */
    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    /**
     * 获取 借款人数 loan_report.borrow_apply_count
     *
     * @return 借款人数
     */
    public Integer getBorrowApplyCount() {
        return borrowApplyCount;
    }

    /**
     * 设置 借款人数 loan_report.borrow_apply_count
     *
     * @param borrowApplyCount 借款人数
     */
    public void setBorrowApplyCount(Integer borrowApplyCount) {
        this.borrowApplyCount = borrowApplyCount;
    }

    @Override
    public String toString() {
        return "LoanReport{" +
                "id=" + id +
                ", loanOrderCount=" + loanOrderCount +
                ", moneyAmountCount=" + moneyAmountCount +
                ", oldLoanOrderCount=" + oldLoanOrderCount +
                ", oldLoanMoneyCount=" + oldLoanMoneyCount +
                ", newLoanOrderCount=" + newLoanOrderCount +
                ", newLoanMoneyCount=" + newLoanMoneyCount +
                ", loanSevendayCount=" + loanSevendayCount +
                ", loanFourteendayCount=" + loanFourteendayCount +
                ", loanTwentyoneCount=" + loanTwentyoneCount +
                ", sevendayMoenyCount=" + sevendayMoenyCount +
                ", fourteendayMoneyCount=" + fourteendayMoneyCount +
                ", twentyonedayMoneyCount=" + twentyonedayMoneyCount +
                ", reportDate=" + reportDate +
                ", createdAt=" + createdAt +
                ", borrowSucCount=" + borrowSucCount +
                ", registerCount=" + registerCount +
                ", borrowApplyCount=" + borrowApplyCount +
                '}';
    }
}