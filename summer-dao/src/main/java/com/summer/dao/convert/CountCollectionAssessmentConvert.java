package com.summer.dao.convert;

import com.summer.dao.entity.CountCollectionAssessment;
import com.summer.pojo.vo.CountCollectionAssessmentVO;

import java.math.BigDecimal;

/**
 * CountCollectionAssessmentConvert
 * Description:
 *
 * @author : GeZhuo
 * Date: 2019/2/27
 */
public class CountCollectionAssessmentConvert extends BaseConverter<CountCollectionAssessment, CountCollectionAssessmentVO> {
    @Override
    protected void convert(CountCollectionAssessment from, CountCollectionAssessmentVO to) {
        //转化催回率，以百分比为单位(按照人数之比)
        super.convert(from, to);
        Integer repaymentOrderNum = from.getRepaymentOrderNum();
        Integer orderTotal = from.getOrderTotal();
        to.setRepaymentOrderRate(BigDecimal.ZERO);
        if (orderTotal > 0) {

            to.setRepaymentOrderRate(new BigDecimal(repaymentOrderNum / orderTotal).multiply(new BigDecimal(100)).setScale(2,
                    BigDecimal.ROUND_DOWN));
        }
    }
}
