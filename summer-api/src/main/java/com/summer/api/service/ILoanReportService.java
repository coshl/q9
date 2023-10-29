package com.summer.api.service;

import com.summer.pojo.vo.LoanReportVO;

import java.util.List;
import java.util.Map;

/**
 * ILoanReportService
 * 贷款报表
 *
 * @author : GeZhuo
 * Date: 2019/2/25
 */
public interface ILoanReportService {

    /**
     * 查询每日贷款报表
     *
     * @param params
     * @return
     */
    public List<LoanReportVO> findParams(Map<String, Object> params);

    /**
     * 查询贷款总额
     *
     * @param params
     * @return
     */
    public Map<String, Object> findLoanMoneySum(Map<String, Object> params);

    /**
     * 生成或更新本日放款统计
     *
     * @param number 天数，0为当天
     * @return
     */
    int statisticLoanReport(Integer number);


    /**
     * 查询放款还款统计，调用存储过程
     *
     * @param
     * @return
     */
    public Map<String, Object> getReport(String begin,String end);
}
