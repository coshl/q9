package com.summer.dao.convert;

import com.summer.dao.entity.LoanReport;
import com.summer.pojo.vo.LoanReportVO;

/**
 * LoanReportConvert
 * Description:
 *
 * @author : GeZhuo
 * Date: 2019/2/27
 */
public class LoanReportConvert extends BaseConverter<LoanReport, LoanReportVO> {
    @Override
    protected void convert(LoanReport from, LoanReportVO to) {
        //转成百分比（保留两位小数）
        //设置申请成功率
        /*if (!from.getBorrowApplyCount().equals(0)) {
            to.setBorrowRate(new BigDecimal(from.getLoanOrderCount() * 100.0f / from.getBorrowApplyCount()).setScale(2,
                    BigDecimal.ROUND_DOWN).floatValue());
        }
        //设置申请率
        if (!from.getRegisterCount().equals(0)) {
            to.setApplyRate(new BigDecimal(from.getBorrowApplyCount() * 100.0f / from.getRegisterCount()).setScale(2,BigDecimal.ROUND_DOWN).floatValue());
        }
        to.setMoneyAmountCount(new BigDecimal(from.getMoneyAmountCount()).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN));
        //转换日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        to.setReportDate(sdf.format(from.getReportDate()));
        super.convert(from, to);*/
    }
}
