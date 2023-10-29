package com.summer.pojo.vo;

/**
 * @program: summer
 * @description: ${description}
 * @author: jql
 * @create: 2019-03-15 14:12
 */

import java.util.Date;

/* https://github.com/12641561 */
public class ReportRepaymentTitleVO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 统计日期
     */
    private String reportDate;

    /**
     * 当前应还款笔总数  默认：0
     */
    private Long allRepaymentCount;

    /**
     * 当前老用户应还款笔数  默认：0
     */
    private Long expireCountOld;

    /**
     * 当前新用户应还笔数  默认：0
     */
    private Long expireCountNew;

    /**
     * 当前新用户正常还款笔数  默认：0
     */
    private Long normalRepaymentCount;

    /**
     * 当前新用户正常还款笔数  默认：0
     */
    private Long normalCountNew;

    /**
     * 当前老用户正常还款笔数  默认：0
     */
    private Long normalCountOld;

    /**
     * 当前正常还款率  默认：0
     */
    private Long repayRate;

    /**
     * 老用户正常还款率  默认：0
     */
    private Long repayRateOld;

    /**
     * 新用户正常还款率  默认：0
     */
    private Long repayRateNew;

    /**
     * 逾期3天内还款数
     */
    private Long repaymentS1Count;

    /**
     * 逾期3天内还款金额
     */
    private Long repaymentS1Amount;


    /**
     * 逾期3天内总还款率  默认：0
     */
    private Long repaymentRateS1CountAll;

    /**
     * 新用户逾期3天内还款率  默认：0
     */
    private Long repaymentRateS1CountNew;

    /**
     * 老用户逾期3天内还款率  默认：0
     */
    private Long repaymentRateS1CountOld;

    /**
     * 逾期7天内还款数
     */
    private Long repaymentS2Count;

    /**
     * 逾期7天内还款金额
     */
    private Long repaymentS2Amount;

    /**
     * 逾期7天内总还款率  默认：0
     */
    private Long repaymentRateS2CountAll;

    /**
     * 新用户逾期7天内还款率  默认：0
     */
    private Long repaymentRateS2CountNew;

    /**
     * 老用户逾期7天内还款率  默认：0
     */
    private Long repaymentRateS2CountOld;

    /**
     * 逾期30天内还款数
     */
    private Long repaymentS3Count;

    /**
     * 逾期30天内还款金额
     */
    private Long repaymentS3Amount;

    /**
     * 逾期30天内总还款率  默认：0
     */
    private Long repaymentRateS3CountAll;

    /**
     * 新用户逾期30天内还款率  默认：0
     */
    private Long repaymentRateS3CountNew;

    /**
     * 老用户逾期30天内还款率  默认：0
     */
    private Long repaymentRateS3CountOld;

    /**
     * 当前应还款笔总金额  默认：0
     */
    private Long allRepaymentAmount;

    /**
     * 当前老用户应还款金额  默认：0
     */
    private Long expireAmountOld;

    /**
     * 当前新用户应还金额  默认：0
     */
    private Long expireAmountNew;

    /**
     * 当前用户正常还款金额  默认：0
     */
    private Long normalRepaymentAmount;

    /**
     * 当前新用户正常还款金额  默认：0
     */
    private Long normalAmountNew;

    /**
     * 当前老用户正常还款金额  默认：0
     */
    private Long normalAmountOld;

    /**
     * 当前正常还款率 按金额  默认：0
     */
    private Long repayAmountRate;

    /**
     * 老用户正常还款率 按金额  默认：0
     */
    private Long repayAmountRateOld;

    /**
     * 新用户正常还款率 按金额  默认：0
     */
    private Long repayAmountRateNew;

    /**
     * 用户逾期3天内还款率 按金额  默认：0
     */
    private Long repaymentRateS1AmountAll;

    /**
     * 新用户逾期3天内还款率 按金额  默认：0
     */
    private Long repaymentRateS1AmountNew;

    /**
     * 老用户逾期3天内还款率 按金额  默认：0
     */
    private Long repaymentRateS1AmountOld;

    /**
     * 用户逾期7天内还款率 按金额  默认：0
     */
    private Long repaymentRateS2AmountAll;

    /**
     * 新用户逾期7天内还款率 按金额  默认：0
     */
    private Long repaymentRateS2AmountNew;

    /**
     * 老用户逾期7天内还款率 按金额  默认：0
     */
    private Long repaymentRateS2AmountOld;

    /**
     * 新用户逾期21天内还款率 按金额  默认：0
     */
    private Long repaymentRateS3AmountNew;

    /**
     * 老用户逾期21天内还款率 按金额  默认：0
     */
    private Long repaymentRateS3AmountOld;

    /**
     * 用户逾期30天内还款率 按金额   默认：0
     */
    private Long repaymentRateS3AmountAll;

    /**
     * 渠道名称  默认：0
     */
    private Integer channelId;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 已经还款数量  默认：0
     */
    private Long allRepayCount;

    /**
     * 已经还款总额  默认：0
     */
    private Long allRepayAmount;

    /**
     * 生息中数量  默认：0
     */
    private Long allOverdueCount;

    /**
     * 生息中总额  默认：0
     */
    private Long allOverdueAmount;

    /**
     * S1级逾期率金额  默认：0
     */
    private Long overdueRateS1Amount;

    /**
     * S2级逾期率金额  默认：0
     */
    private Long overdueRateS2Amount;

    /**
     * S3级逾期率金额  默认：0
     */
    private Long overdueRateS3Amount;

    /**
     * S2级逾期率数量  默认：0
     */
    private Long overdueRateS2Count;

    /**
     * S3级逾期率数量  默认：0
     */
    private Long overdueRateS3Count;

    /**
     * M3级逾期率金额  默认：0
     */
    private Long overdueRateM3Amount;

    /**
     * M3级逾期率数量  默认：0
     */
    private Long overdueRateM3Count;

    //续期数目
    private Integer renewalNumber;

    //续期金额
    private Long renewalAmount;

    //真正的正常还款笔数
    private Long normalCount;

    //真正的正常还款金额
    private Long normalAmount;

    /**
     * 获取 主键 report_repayment.id
     *
     * @return 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 report_repayment.id
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 统计日期 report_repayment.report_date
     *
     * @return 统计日期
     */
    public String getReportDate() {
        return reportDate;
    }

    /**
     * 设置 统计日期 report_repayment.report_date
     *
     * @param reportDate 统计日期
     */
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate == null ? null : reportDate.trim();
    }

    /**
     * 获取 当前应还款笔总数 report_repayment.all_repayment_count
     *
     * @return 当前应还款笔总数
     */
    public Long getAllRepaymentCount() {
        return allRepaymentCount;
    }

    /**
     * 设置 当前应还款笔总数 report_repayment.all_repayment_count
     *
     * @param allRepaymentCount 当前应还款笔总数
     */
    public void setAllRepaymentCount(Long allRepaymentCount) {
        this.allRepaymentCount = allRepaymentCount;
    }

    /**
     * 获取 当前老用户应还款笔数 report_repayment.expire_count_old
     *
     * @return 当前老用户应还款笔数
     */
    public Long getExpireCountOld() {
        return expireCountOld;
    }

    /**
     * 设置 当前老用户应还款笔数 report_repayment.expire_count_old
     *
     * @param expireCountOld 当前老用户应还款笔数
     */
    public void setExpireCountOld(Long expireCountOld) {
        this.expireCountOld = expireCountOld;
    }

    /**
     * 获取 当前新用户应还笔数 report_repayment.expire_count_new
     *
     * @return 当前新用户应还笔数
     */
    public Long getExpireCountNew() {
        return expireCountNew;
    }

    /**
     * 设置 当前新用户应还笔数 report_repayment.expire_count_new
     *
     * @param expireCountNew 当前新用户应还笔数
     */
    public void setExpireCountNew(Long expireCountNew) {
        this.expireCountNew = expireCountNew;
    }

    /**
     * 获取 当前新用户正常还款笔数 report_repayment.normal_repayment_count
     *
     * @return 当前新用户正常还款笔数
     */
    public Long getNormalRepaymentCount() {
        return normalRepaymentCount;
    }

    /**
     * 设置 当前新用户正常还款笔数 report_repayment.normal_repayment_count
     *
     * @param normalRepaymentCount 当前新用户正常还款笔数
     */
    public void setNormalRepaymentCount(Long normalRepaymentCount) {
        this.normalRepaymentCount = normalRepaymentCount;
    }

    /**
     * 获取 当前新用户正常还款笔数 report_repayment.normal_count_new
     *
     * @return 当前新用户正常还款笔数
     */
    public Long getNormalCountNew() {
        return normalCountNew;
    }

    /**
     * 设置 当前新用户正常还款笔数 report_repayment.normal_count_new
     *
     * @param normalCountNew 当前新用户正常还款笔数
     */
    public void setNormalCountNew(Long normalCountNew) {
        this.normalCountNew = normalCountNew;
    }

    /**
     * 获取 当前老用户正常还款笔数 report_repayment.normal_count_old
     *
     * @return 当前老用户正常还款笔数
     */
    public Long getNormalCountOld() {
        return normalCountOld;
    }

    /**
     * 设置 当前老用户正常还款笔数 report_repayment.normal_count_old
     *
     * @param normalCountOld 当前老用户正常还款笔数
     */
    public void setNormalCountOld(Long normalCountOld) {
        this.normalCountOld = normalCountOld;
    }

    /**
     * 获取 当前正常还款率 report_repayment.repay_rate
     *
     * @return 当前正常还款率
     */
    public Long getRepayRate() {
        return repayRate;
    }

    /**
     * 设置 当前正常还款率 report_repayment.repay_rate
     *
     * @param repayRate 当前正常还款率
     */
    public void setRepayRate(Long repayRate) {
        this.repayRate = repayRate;
    }

    /**
     * 获取 老用户正常还款率 report_repayment.repay_rate_old
     *
     * @return 老用户正常还款率
     */
    public Long getRepayRateOld() {
        return repayRateOld;
    }

    /**
     * 设置 老用户正常还款率 report_repayment.repay_rate_old
     *
     * @param repayRateOld 老用户正常还款率
     */
    public void setRepayRateOld(Long repayRateOld) {
        this.repayRateOld = repayRateOld;
    }

    /**
     * 获取 新用户正常还款率 report_repayment.repay_rate_new
     *
     * @return 新用户正常还款率
     */
    public Long getRepayRateNew() {
        return repayRateNew;
    }

    /**
     * 设置 新用户正常还款率 report_repayment.repay_rate_new
     *
     * @param repayRateNew 新用户正常还款率
     */
    public void setRepayRateNew(Long repayRateNew) {
        this.repayRateNew = repayRateNew;
    }

    /**
     * 获取 逾期3天内总还款率 report_repayment.repayment_rate_s1_count_all
     *
     * @return 逾期3天内总还款率
     */
    public Long getRepaymentRateS1CountAll() {
        return repaymentRateS1CountAll;
    }

    /**
     * 设置 逾期3天内总还款率 report_repayment.repayment_rate_s1_count_all
     *
     * @param repaymentRateS1CountAll 逾期3天内总还款率
     */
    public void setRepaymentRateS1CountAll(Long repaymentRateS1CountAll) {
        this.repaymentRateS1CountAll = repaymentRateS1CountAll;
    }

    /**
     * 获取 新用户逾期3天内还款率 report_repayment.repayment_rate_s1_count_new
     *
     * @return 新用户逾期3天内还款率
     */
    public Long getRepaymentRateS1CountNew() {
        return repaymentRateS1CountNew;
    }

    /**
     * 设置 新用户逾期3天内还款率 report_repayment.repayment_rate_s1_count_new
     *
     * @param repaymentRateS1CountNew 新用户逾期3天内还款率
     */
    public void setRepaymentRateS1CountNew(Long repaymentRateS1CountNew) {
        this.repaymentRateS1CountNew = repaymentRateS1CountNew;
    }

    /**
     * 获取 老用户逾期3天内还款率 report_repayment.repayment_rate_s1_count_old
     *
     * @return 老用户逾期3天内还款率
     */
    public Long getRepaymentRateS1CountOld() {
        return repaymentRateS1CountOld;
    }

    /**
     * 设置 老用户逾期3天内还款率 report_repayment.repayment_rate_s1_count_old
     *
     * @param repaymentRateS1CountOld 老用户逾期3天内还款率
     */
    public void setRepaymentRateS1CountOld(Long repaymentRateS1CountOld) {
        this.repaymentRateS1CountOld = repaymentRateS1CountOld;
    }

    /**
     * 获取 逾期7天内总还款率 report_repayment.repayment_rate_s2_count_all
     *
     * @return 逾期7天内总还款率
     */
    public Long getRepaymentRateS2CountAll() {
        return repaymentRateS2CountAll;
    }

    /**
     * 设置 逾期7天内总还款率 report_repayment.repayment_rate_s2_count_all
     *
     * @param repaymentRateS2CountAll 逾期7天内总还款率
     */
    public void setRepaymentRateS2CountAll(Long repaymentRateS2CountAll) {
        this.repaymentRateS2CountAll = repaymentRateS2CountAll;
    }

    /**
     * 获取 新用户逾期7天内还款率 report_repayment.repayment_rate_s2_count_new
     *
     * @return 新用户逾期7天内还款率
     */
    public Long getRepaymentRateS2CountNew() {
        return repaymentRateS2CountNew;
    }

    /**
     * 设置 新用户逾期7天内还款率 report_repayment.repayment_rate_s2_count_new
     *
     * @param repaymentRateS2CountNew 新用户逾期7天内还款率
     */
    public void setRepaymentRateS2CountNew(Long repaymentRateS2CountNew) {
        this.repaymentRateS2CountNew = repaymentRateS2CountNew;
    }

    /**
     * 获取 老用户逾期7天内还款率 report_repayment.repayment_rate_s2_count_old
     *
     * @return 老用户逾期7天内还款率
     */
    public Long getRepaymentRateS2CountOld() {
        return repaymentRateS2CountOld;
    }

    /**
     * 设置 老用户逾期7天内还款率 report_repayment.repayment_rate_s2_count_old
     *
     * @param repaymentRateS2CountOld 老用户逾期7天内还款率
     */
    public void setRepaymentRateS2CountOld(Long repaymentRateS2CountOld) {
        this.repaymentRateS2CountOld = repaymentRateS2CountOld;
    }

    /**
     * 获取 逾期30天内总还款率 report_repayment.repayment_rate_s3_count_all
     *
     * @return 逾期30天内总还款率
     */
    public Long getRepaymentRateS3CountAll() {
        return repaymentRateS3CountAll;
    }

    /**
     * 设置 逾期30天内总还款率 report_repayment.repayment_rate_s3_count_all
     *
     * @param repaymentRateS3CountAll 逾期30天内总还款率
     */
    public void setRepaymentRateS3CountAll(Long repaymentRateS3CountAll) {
        this.repaymentRateS3CountAll = repaymentRateS3CountAll;
    }

    /**
     * 获取 新用户逾期30天内还款率 report_repayment.repayment_rate_s3_count_new
     *
     * @return 新用户逾期30天内还款率
     */
    public Long getRepaymentRateS3CountNew() {
        return repaymentRateS3CountNew;
    }

    /**
     * 设置 新用户逾期30天内还款率 report_repayment.repayment_rate_s3_count_new
     *
     * @param repaymentRateS3CountNew 新用户逾期30天内还款率
     */
    public void setRepaymentRateS3CountNew(Long repaymentRateS3CountNew) {
        this.repaymentRateS3CountNew = repaymentRateS3CountNew;
    }

    /**
     * 获取 老用户逾期30天内还款率 report_repayment.repayment_rate_s3_count_old
     *
     * @return 老用户逾期30天内还款率
     */
    public Long getRepaymentRateS3CountOld() {
        return repaymentRateS3CountOld;
    }

    /**
     * 设置 老用户逾期30天内还款率 report_repayment.repayment_rate_s3_count_old
     *
     * @param repaymentRateS3CountOld 老用户逾期30天内还款率
     */
    public void setRepaymentRateS3CountOld(Long repaymentRateS3CountOld) {
        this.repaymentRateS3CountOld = repaymentRateS3CountOld;
    }

    /**
     * 获取 当前应还款笔总金额 report_repayment.all_repayment_amount
     *
     * @return 当前应还款笔总金额
     */
    public Long getAllRepaymentAmount() {
        return allRepaymentAmount;
    }

    /**
     * 设置 当前应还款笔总金额 report_repayment.all_repayment_amount
     *
     * @param allRepaymentAmount 当前应还款笔总金额
     */
    public void setAllRepaymentAmount(Long allRepaymentAmount) {
        this.allRepaymentAmount = allRepaymentAmount;
    }

    /**
     * 获取 当前老用户应还款金额 report_repayment.expire_amount_old
     *
     * @return 当前老用户应还款金额
     */
    public Long getExpireAmountOld() {
        return expireAmountOld;
    }

    /**
     * 设置 当前老用户应还款金额 report_repayment.expire_amount_old
     *
     * @param expireAmountOld 当前老用户应还款金额
     */
    public void setExpireAmountOld(Long expireAmountOld) {
        this.expireAmountOld = expireAmountOld;
    }

    /**
     * 获取 当前新用户应还金额 report_repayment.expire_amount_new
     *
     * @return 当前新用户应还金额
     */
    public Long getExpireAmountNew() {
        return expireAmountNew;
    }

    /**
     * 设置 当前新用户应还金额 report_repayment.expire_amount_new
     *
     * @param expireAmountNew 当前新用户应还金额
     */
    public void setExpireAmountNew(Long expireAmountNew) {
        this.expireAmountNew = expireAmountNew;
    }

    /**
     * 获取 当前用户正常还款金额 report_repayment.normal_repayment_amount
     *
     * @return 当前用户正常还款金额
     */
    public Long getNormalRepaymentAmount() {
        return normalRepaymentAmount;
    }

    /**
     * 设置 当前用户正常还款金额 report_repayment.normal_repayment_amount
     *
     * @param normalRepaymentAmount 当前用户正常还款金额
     */
    public void setNormalRepaymentAmount(Long normalRepaymentAmount) {
        this.normalRepaymentAmount = normalRepaymentAmount;
    }

    /**
     * 获取 当前新用户正常还款金额 report_repayment.normal_amount_new
     *
     * @return 当前新用户正常还款金额
     */
    public Long getNormalAmountNew() {
        return normalAmountNew;
    }

    /**
     * 设置 当前新用户正常还款金额 report_repayment.normal_amount_new
     *
     * @param normalAmountNew 当前新用户正常还款金额
     */
    public void setNormalAmountNew(Long normalAmountNew) {
        this.normalAmountNew = normalAmountNew;
    }

    /**
     * 获取 当前老用户正常还款金额 report_repayment.normal_amount_old
     *
     * @return 当前老用户正常还款金额
     */
    public Long getNormalAmountOld() {
        return normalAmountOld;
    }

    /**
     * 设置 当前老用户正常还款金额 report_repayment.normal_amount_old
     *
     * @param normalAmountOld 当前老用户正常还款金额
     */
    public void setNormalAmountOld(Long normalAmountOld) {
        this.normalAmountOld = normalAmountOld;
    }

    /**
     * 获取 当前正常还款率 按金额 report_repayment.repay_amount_rate
     *
     * @return 当前正常还款率 按金额
     */
    public Long getRepayAmountRate() {
        return repayAmountRate;
    }

    /**
     * 设置 当前正常还款率 按金额 report_repayment.repay_amount_rate
     *
     * @param repayAmountRate 当前正常还款率 按金额
     */
    public void setRepayAmountRate(Long repayAmountRate) {
        this.repayAmountRate = repayAmountRate;
    }

    /**
     * 获取 老用户正常还款率 按金额 report_repayment.repay_amount_rate_old
     *
     * @return 老用户正常还款率 按金额
     */
    public Long getRepayAmountRateOld() {
        return repayAmountRateOld;
    }

    /**
     * 设置 老用户正常还款率 按金额 report_repayment.repay_amount_rate_old
     *
     * @param repayAmountRateOld 老用户正常还款率 按金额
     */
    public void setRepayAmountRateOld(Long repayAmountRateOld) {
        this.repayAmountRateOld = repayAmountRateOld;
    }

    /**
     * 获取 新用户正常还款率 按金额 report_repayment.repay_amount_rate_new
     *
     * @return 新用户正常还款率 按金额
     */
    public Long getRepayAmountRateNew() {
        return repayAmountRateNew;
    }

    /**
     * 设置 新用户正常还款率 按金额 report_repayment.repay_amount_rate_new
     *
     * @param repayAmountRateNew 新用户正常还款率 按金额
     */
    public void setRepayAmountRateNew(Long repayAmountRateNew) {
        this.repayAmountRateNew = repayAmountRateNew;
    }

    /**
     * 获取 用户逾期3天内还款率 按金额 report_repayment.repayment_rate_s1_amount_all
     *
     * @return 用户逾期3天内还款率 按金额
     */
    public Long getRepaymentRateS1AmountAll() {
        return repaymentRateS1AmountAll;
    }

    /**
     * 设置 用户逾期3天内还款率 按金额 report_repayment.repayment_rate_s1_amount_all
     *
     * @param repaymentRateS1AmountAll 用户逾期3天内还款率 按金额
     */
    public void setRepaymentRateS1AmountAll(Long repaymentRateS1AmountAll) {
        this.repaymentRateS1AmountAll = repaymentRateS1AmountAll;
    }

    /**
     * 获取 新用户逾期3天内还款率 按金额 report_repayment.repayment_rate_s1_amount_new
     *
     * @return 新用户逾期3天内还款率 按金额
     */
    public Long getRepaymentRateS1AmountNew() {
        return repaymentRateS1AmountNew;
    }

    /**
     * 设置 新用户逾期3天内还款率 按金额 report_repayment.repayment_rate_s1_amount_new
     *
     * @param repaymentRateS1AmountNew 新用户逾期3天内还款率 按金额
     */
    public void setRepaymentRateS1AmountNew(Long repaymentRateS1AmountNew) {
        this.repaymentRateS1AmountNew = repaymentRateS1AmountNew;
    }

    /**
     * 获取 老用户逾期3天内还款率 按金额 report_repayment.repayment_rate_s1_amount_old
     *
     * @return 老用户逾期3天内还款率 按金额
     */
    public Long getRepaymentRateS1AmountOld() {
        return repaymentRateS1AmountOld;
    }

    /**
     * 设置 老用户逾期3天内还款率 按金额 report_repayment.repayment_rate_s1_amount_old
     *
     * @param repaymentRateS1AmountOld 老用户逾期3天内还款率 按金额
     */
    public void setRepaymentRateS1AmountOld(Long repaymentRateS1AmountOld) {
        this.repaymentRateS1AmountOld = repaymentRateS1AmountOld;
    }

    /**
     * 获取 用户逾期7天内还款率 按金额 report_repayment.repayment_rate_s2_amount_all
     *
     * @return 用户逾期7天内还款率 按金额
     */
    public Long getRepaymentRateS2AmountAll() {
        return repaymentRateS2AmountAll;
    }

    /**
     * 设置 用户逾期7天内还款率 按金额 report_repayment.repayment_rate_s2_amount_all
     *
     * @param repaymentRateS2AmountAll 用户逾期7天内还款率 按金额
     */
    public void setRepaymentRateS2AmountAll(Long repaymentRateS2AmountAll) {
        this.repaymentRateS2AmountAll = repaymentRateS2AmountAll;
    }

    /**
     * 获取 新用户逾期7天内还款率 按金额 report_repayment.repayment_rate_s2_amount_new
     *
     * @return 新用户逾期7天内还款率 按金额
     */
    public Long getRepaymentRateS2AmountNew() {
        return repaymentRateS2AmountNew;
    }

    /**
     * 设置 新用户逾期7天内还款率 按金额 report_repayment.repayment_rate_s2_amount_new
     *
     * @param repaymentRateS2AmountNew 新用户逾期7天内还款率 按金额
     */
    public void setRepaymentRateS2AmountNew(Long repaymentRateS2AmountNew) {
        this.repaymentRateS2AmountNew = repaymentRateS2AmountNew;
    }

    /**
     * 获取 老用户逾期7天内还款率 按金额 report_repayment.repayment_rate_s2_amount_old
     *
     * @return 老用户逾期7天内还款率 按金额
     */
    public Long getRepaymentRateS2AmountOld() {
        return repaymentRateS2AmountOld;
    }

    /**
     * 设置 老用户逾期7天内还款率 按金额 report_repayment.repayment_rate_s2_amount_old
     *
     * @param repaymentRateS2AmountOld 老用户逾期7天内还款率 按金额
     */
    public void setRepaymentRateS2AmountOld(Long repaymentRateS2AmountOld) {
        this.repaymentRateS2AmountOld = repaymentRateS2AmountOld;
    }

    /**
     * 获取 新用户逾期21天内还款率 按金额 report_repayment.repayment_rate_s3_amount_new
     *
     * @return 新用户逾期21天内还款率 按金额
     */
    public Long getRepaymentRateS3AmountNew() {
        return repaymentRateS3AmountNew;
    }

    /**
     * 设置 新用户逾期21天内还款率 按金额 report_repayment.repayment_rate_s3_amount_new
     *
     * @param repaymentRateS3AmountNew 新用户逾期21天内还款率 按金额
     */
    public void setRepaymentRateS3AmountNew(Long repaymentRateS3AmountNew) {
        this.repaymentRateS3AmountNew = repaymentRateS3AmountNew;
    }

    /**
     * 获取 老用户逾期21天内还款率 按金额 report_repayment.repayment_rate_s3_amount_old
     *
     * @return 老用户逾期21天内还款率 按金额
     */
    public Long getRepaymentRateS3AmountOld() {
        return repaymentRateS3AmountOld;
    }

    /**
     * 设置 老用户逾期21天内还款率 按金额 report_repayment.repayment_rate_s3_amount_old
     *
     * @param repaymentRateS3AmountOld 老用户逾期21天内还款率 按金额
     */
    public void setRepaymentRateS3AmountOld(Long repaymentRateS3AmountOld) {
        this.repaymentRateS3AmountOld = repaymentRateS3AmountOld;
    }

    /**
     * 获取 用户逾期30天内还款率 按金额  report_repayment.repayment_rate_s3_amount_all
     *
     * @return 用户逾期30天内还款率 按金额
     */
    public Long getRepaymentRateS3AmountAll() {
        return repaymentRateS3AmountAll;
    }

    /**
     * 设置 用户逾期30天内还款率 按金额  report_repayment.repayment_rate_s3_amount_all
     *
     * @param repaymentRateS3AmountAll 用户逾期30天内还款率 按金额
     */
    public void setRepaymentRateS3AmountAll(Long repaymentRateS3AmountAll) {
        this.repaymentRateS3AmountAll = repaymentRateS3AmountAll;
    }

    /**
     * 获取 渠道名称 report_repayment.channel_id
     *
     * @return 渠道名称
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置 渠道名称 report_repayment.channel_id
     *
     * @param channelId 渠道名称
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * 获取 创建时间 report_repayment.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 report_repayment.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 report_repayment.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 report_repayment.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 已经还款数量 report_repayment.all_repay_count
     *
     * @return 已经还款数量
     */
    public Long getAllRepayCount() {
        return allRepayCount;
    }

    /**
     * 设置 已经还款数量 report_repayment.all_repay_count
     *
     * @param allRepayCount 已经还款数量
     */
    public void setAllRepayCount(Long allRepayCount) {
        this.allRepayCount = allRepayCount;
    }

    /**
     * 获取 已经还款总额 report_repayment.all_repay_amount
     *
     * @return 已经还款总额
     */
    public Long getAllRepayAmount() {
        return allRepayAmount;
    }

    /**
     * 设置 已经还款总额 report_repayment.all_repay_amount
     *
     * @param allRepayAmount 已经还款总额
     */
    public void setAllRepayAmount(Long allRepayAmount) {
        this.allRepayAmount = allRepayAmount;
    }

    /**
     * 获取 生息中数量 report_repayment.all_overdue_count
     *
     * @return 生息中数量
     */
    public Long getAllOverdueCount() {
        return allOverdueCount;
    }

    /**
     * 设置 生息中数量 report_repayment.all_overdue_count
     *
     * @param allOverdueCount 生息中数量
     */
    public void setAllOverdueCount(Long allOverdueCount) {
        this.allOverdueCount = allOverdueCount;
    }

    /**
     * 获取 生息中总额 report_repayment.all_overdue_amount
     *
     * @return 生息中总额
     */
    public Long getAllOverdueAmount() {
        return allOverdueAmount;
    }

    /**
     * 设置 生息中总额 report_repayment.all_overdue_amount
     *
     * @param allOverdueAmount 生息中总额
     */
    public void setAllOverdueAmount(Long allOverdueAmount) {
        this.allOverdueAmount = allOverdueAmount;
    }

    /**
     * 获取 S1级逾期率金额 report_repayment.overdue_rate_s1_amount
     *
     * @return S1级逾期率金额
     */
    public Long getOverdueRateS1Amount() {
        return overdueRateS1Amount;
    }

    /**
     * 设置 S1级逾期率金额 report_repayment.overdue_rate_s1_amount
     *
     * @param overdueRateS1Amount S1级逾期率金额
     */
    public void setOverdueRateS1Amount(Long overdueRateS1Amount) {
        this.overdueRateS1Amount = overdueRateS1Amount;
    }

    /**
     * 获取 S2级逾期率金额 report_repayment.overdue_rate_s2_amount
     *
     * @return S2级逾期率金额
     */
    public Long getOverdueRateS2Amount() {
        return overdueRateS2Amount;
    }

    /**
     * 设置 S2级逾期率金额 report_repayment.overdue_rate_s2_amount
     *
     * @param overdueRateS2Amount S2级逾期率金额
     */
    public void setOverdueRateS2Amount(Long overdueRateS2Amount) {
        this.overdueRateS2Amount = overdueRateS2Amount;
    }

    /**
     * 获取 S3级逾期率金额 report_repayment.overdue_rate_s3_amount
     *
     * @return S3级逾期率金额
     */
    public Long getOverdueRateS3Amount() {
        return overdueRateS3Amount;
    }

    /**
     * 设置 S3级逾期率金额 report_repayment.overdue_rate_s3_amount
     *
     * @param overdueRateS3Amount S3级逾期率金额
     */
    public void setOverdueRateS3Amount(Long overdueRateS3Amount) {
        this.overdueRateS3Amount = overdueRateS3Amount;
    }

    /**
     * 获取 S2级逾期率数量 report_repayment.overdue_rate_s2_count
     *
     * @return S2级逾期率数量
     */
    public Long getOverdueRateS2Count() {
        return overdueRateS2Count;
    }

    /**
     * 设置 S2级逾期率数量 report_repayment.overdue_rate_s2_count
     *
     * @param overdueRateS2Count S2级逾期率数量
     */
    public void setOverdueRateS2Count(Long overdueRateS2Count) {
        this.overdueRateS2Count = overdueRateS2Count;
    }

    /**
     * 获取 S3级逾期率数量 report_repayment.overdue_rate_s3_count
     *
     * @return S3级逾期率数量
     */
    public Long getOverdueRateS3Count() {
        return overdueRateS3Count;
    }

    /**
     * 设置 S3级逾期率数量 report_repayment.overdue_rate_s3_count
     *
     * @param overdueRateS3Count S3级逾期率数量
     */
    public void setOverdueRateS3Count(Long overdueRateS3Count) {
        this.overdueRateS3Count = overdueRateS3Count;
    }

    /**
     * 获取 M3级逾期率金额 report_repayment.overdue_rate_m3_amount
     *
     * @return M3级逾期率金额
     */
    public Long getOverdueRateM3Amount() {
        return overdueRateM3Amount;
    }

    /**
     * 设置 M3级逾期率金额 report_repayment.overdue_rate_m3_amount
     *
     * @param overdueRateM3Amount M3级逾期率金额
     */
    public void setOverdueRateM3Amount(Long overdueRateM3Amount) {
        this.overdueRateM3Amount = overdueRateM3Amount;
    }

    /**
     * 获取 M3级逾期率数量 report_repayment.overdue_rate_m3_count
     *
     * @return M3级逾期率数量
     */
    public Long getOverdueRateM3Count() {
        return overdueRateM3Count;
    }

    /**
     * 设置 M3级逾期率数量 report_repayment.overdue_rate_m3_count
     *
     * @param overdueRateM3Count M3级逾期率数量
     */
    public void setOverdueRateM3Count(Long overdueRateM3Count) {
        this.overdueRateM3Count = overdueRateM3Count;
    }

    public Long getRepaymentS1Count() {
        return repaymentS1Count;
    }

    public void setRepaymentS1Count(Long repaymentS1Count) {
        this.repaymentS1Count = repaymentS1Count;
    }

    public Long getRepaymentS1Amount() {
        return repaymentS1Amount;
    }

    public void setRepaymentS1Amount(Long repaymentS1Amount) {
        this.repaymentS1Amount = repaymentS1Amount;
    }

    public Long getRepaymentS2Count() {
        return repaymentS2Count;
    }

    public void setRepaymentS2Count(Long repaymentS2Count) {
        this.repaymentS2Count = repaymentS2Count;
    }

    public Long getRepaymentS2Amount() {
        return repaymentS2Amount;
    }

    public void setRepaymentS2Amount(Long repaymentS2Amount) {
        this.repaymentS2Amount = repaymentS2Amount;
    }

    public Long getRepaymentS3Count() {
        return repaymentS3Count;
    }

    public void setRepaymentS3Count(Long repaymentS3Count) {
        this.repaymentS3Count = repaymentS3Count;
    }

    public Long getRepaymentS3Amount() {
        return repaymentS3Amount;
    }

    public void setRepaymentS3Amount(Long repaymentS3Amount) {
        this.repaymentS3Amount = repaymentS3Amount;
    }

    public Integer getRenewalNumber() {
        return renewalNumber;
    }

    public void setRenewalNumber(Integer renewalNumber) {
        this.renewalNumber = renewalNumber;
    }

    public Long getRenewalAmount() {
        return renewalAmount;
    }

    public void setRenewalAmount(Long renewalAmount) {
        this.renewalAmount = renewalAmount;
    }

    public Long getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(Long normalCount) {
        this.normalCount = normalCount;
    }

    public Long getNormalAmount() {
        return normalAmount;
    }

    public void setNormalAmount(Long normalAmount) {
        this.normalAmount = normalAmount;
    }
}
