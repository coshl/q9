package com.summer.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/***
 * 利率转换工具类
 */
public class RateChangeUtil {

    public static final BigDecimal changeMoney = new BigDecimal(100.0);
    public static final DecimalFormat df = new DecimalFormat("0.00");
    public static final BigDecimal dollarZero = new BigDecimal(0);

    /**
     * 每日还款统计，利率保留2位小数
     *
     * @param rate
     * @return
     */
    public static BigDecimal rateChange(Long rate) {
        if (null != rate) {
            long moneyInt = rate;
            if (moneyInt != 0) {
                BigDecimal moneyBig = new BigDecimal(rate);
                return moneyBig.divide(changeMoney, 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return dollarZero;
    }
}
