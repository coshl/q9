/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class IndexReport {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 报告针对数据的日期
     */
    private String reportDate;

    /**
     * 当日放款成功总额  默认：0.00
     */
    private BigDecimal loanedMoney;

    /**
     * 当日还款成功总额  默认：0.00
     */
    private BigDecimal paidMoney;

    /**
     * 当日续期成功总额  默认：0.00
     */
    private BigDecimal renewalMoney;

    /**
     * 当日待还总额  默认：0.00
     */
    private BigDecimal payMoney;

    /**
     * 当日逾期总额  默认：0.00
     */
    private BigDecimal overdueMoney;

    /**
     * 当日注册用户数  默认：0
     */
    private Integer registerNum;

    /**
     * 当日放款成功用户数  默认：0
     */
    private Integer loanedNum;

    /**
     * 当日逾期用户数  默认：0
     */
    private Integer overdueNum;

    /**
     * 添加时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 最后更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 1.有效数据；2；无效数据  默认：1
     */
    private Byte status;

    /**
     * 0未知,1.新用户；2；老用户,3全部  默认：0
     */
    private Byte customerType;

    public IndexReport() {
    }

    public IndexReport(String reportDate, BigDecimal loanedMoney, BigDecimal paidMoney, BigDecimal renewalMoney,
                       BigDecimal payMoney, BigDecimal overdueMoney, Integer registerNum, Integer loanedNum,
                       Integer overdueNum, Date createTime, Date updateTime, Byte status, Byte customerType) {
        this.reportDate = reportDate;
        this.loanedMoney = loanedMoney;
        this.paidMoney = paidMoney;
        this.renewalMoney = renewalMoney;
        this.payMoney = payMoney;
        this.overdueMoney = overdueMoney;
        this.registerNum = registerNum;
        this.loanedNum = loanedNum;
        this.overdueNum = overdueNum;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
        this.customerType = customerType;
    }

    /**
     * 获取 主键 index_report.id
     *
     * @return 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 index_report.id
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 报告针对数据的日期 index_report.report_date
     *
     * @return 报告针对数据的日期
     */
    public String getReportDate() {
        return reportDate;
    }

    /**
     * 设置 报告针对数据的日期 index_report.report_date
     *
     * @param reportDate 报告针对数据的日期
     */
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate == null ? null : reportDate.trim();
    }

    /**
     * 获取 当日放款成功总额 index_report.loaned_money
     *
     * @return 当日放款成功总额
     */
    public BigDecimal getLoanedMoney() {
        return loanedMoney;
    }

    /**
     * 设置 当日放款成功总额 index_report.loaned_money
     *
     * @param loanedMoney 当日放款成功总额
     */
    public void setLoanedMoney(BigDecimal loanedMoney) {
        this.loanedMoney = loanedMoney;
    }

    /**
     * 获取 当日还款成功总额 index_report.paid_money
     *
     * @return 当日还款成功总额
     */
    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    /**
     * 设置 当日还款成功总额 index_report.paid_money
     *
     * @param paidMoney 当日还款成功总额
     */
    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    /**
     * 获取 当日续期成功总额 index_report.renewal_money
     *
     * @return 当日续期成功总额
     */
    public BigDecimal getRenewalMoney() {
        return renewalMoney;
    }

    /**
     * 设置 当日续期成功总额 index_report.renewal_money
     *
     * @param renewalMoney 当日续期成功总额
     */
    public void setRenewalMoney(BigDecimal renewalMoney) {
        this.renewalMoney = renewalMoney;
    }

    /**
     * 获取 当日待还总额 index_report.pay_money
     *
     * @return 当日待还总额
     */
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    /**
     * 设置 当日待还总额 index_report.pay_money
     *
     * @param payMoney 当日待还总额
     */
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    /**
     * 获取 当日逾期总额 index_report.overdue_money
     *
     * @return 当日逾期总额
     */
    public BigDecimal getOverdueMoney() {
        return overdueMoney;
    }

    /**
     * 设置 当日逾期总额 index_report.overdue_money
     *
     * @param overdueMoney 当日逾期总额
     */
    public void setOverdueMoney(BigDecimal overdueMoney) {
        this.overdueMoney = overdueMoney;
    }

    /**
     * 获取 当日注册用户数 index_report.register_num
     *
     * @return 当日注册用户数
     */
    public Integer getRegisterNum() {
        return registerNum;
    }

    /**
     * 设置 当日注册用户数 index_report.register_num
     *
     * @param registerNum 当日注册用户数
     */
    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

    /**
     * 获取 当日放款成功用户数 index_report.loaned_num
     *
     * @return 当日放款成功用户数
     */
    public Integer getLoanedNum() {
        return loanedNum;
    }

    /**
     * 设置 当日放款成功用户数 index_report.loaned_num
     *
     * @param loanedNum 当日放款成功用户数
     */
    public void setLoanedNum(Integer loanedNum) {
        this.loanedNum = loanedNum;
    }

    /**
     * 获取 当日逾期用户数 index_report.overdue_num
     *
     * @return 当日逾期用户数
     */
    public Integer getOverdueNum() {
        return overdueNum;
    }

    /**
     * 设置 当日逾期用户数 index_report.overdue_num
     *
     * @param overdueNum 当日逾期用户数
     */
    public void setOverdueNum(Integer overdueNum) {
        this.overdueNum = overdueNum;
    }

    /**
     * 获取 添加时间 index_report.create_time
     *
     * @return 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 添加时间 index_report.create_time
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 最后更新时间 index_report.update_time
     *
     * @return 最后更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 最后更新时间 index_report.update_time
     *
     * @param updateTime 最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 1.有效数据；2；无效数据 index_report.status
     *
     * @return 1.有效数据；2；无效数据
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 1.有效数据；2；无效数据 index_report.status
     *
     * @param status 1.有效数据；2；无效数据
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 0未知,1.新用户；2；老用户,3全部 index_report.customer_type
     *
     * @return 0未知, 1.新用户；2；老用户,3全部
     */
    public Byte getCustomerType() {
        return customerType;
    }

    /**
     * 设置 0未知,1.新用户；2；老用户,3全部 index_report.customer_type
     *
     * @param customerType 0未知,1.新用户；2；老用户,3全部
     */
    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }
}