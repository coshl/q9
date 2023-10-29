package com.summer.api.service;


import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.ReportRepayment;
import com.summer.pojo.vo.ReportRepaymentTitleVO;

import java.util.Map;

public interface IReportRepaymentService {
    /**
     * 根据日期查询还款报告信息
     *
     * @param params
     * @return 返回还款报告信息
     */
    PageInfo<ReportRepayment> queryReport(Map<String, Object> params);

    ReportRepaymentTitleVO queryReportTitle(Map<String, Object> params);

    /**
     * 生成或更新本日实际还的以前的统计
     *
     * @param firstRepaymentTime
     * @param beginTime
     * @param endTime
     */
    void reportRepayment(String firstRepaymentTime, String beginTime, String endTime);

    /**
     * 生成或更新本日还款统计
     *
     * @param firstRepaymentTime
     */
    void reportRepaymentToday(String firstRepaymentTime, Integer ty);

    /**
     * 生成或更新本日续期
     *
     * @param firstRepaymentTime
     */
    void renewalToday(String firstRepaymentTime);
}
