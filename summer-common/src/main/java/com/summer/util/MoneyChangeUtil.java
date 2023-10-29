package com.summer.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 金钱转换工具类
 */
public class MoneyChangeUtil {


    public static final BigDecimal changeMoney = new BigDecimal(100.0);
    public static final DecimalFormat df = new DecimalFormat("0.00");
    public static final BigDecimal dollarZero = new BigDecimal(0);

    /**
     * 格式化元
     *
     * @param data
     * @return
     */
    public static Double changeSmallDouble(double data) {
        String formatData = df.format(data);
        return Double.parseDouble(formatData);
    }

    /**
     * 金钱分转换元，(自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeDollar(Integer money) {
        if (null != money) {
            int moneyInt = money;
            if (moneyInt != 0) {
                BigDecimal moneyBig = new BigDecimal(money);
                return moneyBig.divide(changeMoney, 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return dollarZero;
    }

    /**
     * 金钱分转换元，(自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeDollar(Long money) {
        if (null != money) {
            long moneyInt = money;
            if (moneyInt != 0) {
                BigDecimal moneyBig = new BigDecimal(money);
                return moneyBig.divide(changeMoney, 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return dollarZero;
    }

    /**
     * 金钱分转换元，(自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeDollar(Float money) {
        if (null != money) {
            float moneyInt = money;
            if (moneyInt != 0) {
                BigDecimal moneyBig = new BigDecimal(money);
                return moneyBig.divide(changeMoney, 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return dollarZero;
    }

    /**
     * 金钱分转换元，自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeDollar(Double money) {
        if (null != money) {
            double moneyInt = money;
            if (moneyInt != 0) {
                BigDecimal moneyBig = new BigDecimal(money);
                return moneyBig.divide(changeMoney, 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return dollarZero;
    }

    /**
     * 元转换分自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeCent(Double money) {
        if (null != money) {
            BigDecimal moneyBig = new BigDecimal(money);
            return moneyBig.multiply(changeMoney);
        }
        return dollarZero;
    }

    /**
     * 元转换分 （自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeCent(Integer money) {
        if (null != money) {
            BigDecimal moneyBig = new BigDecimal(money);
            return moneyBig.multiply(changeMoney);
        }
        return dollarZero;
    }

    /**
     * 元转换分 （自己在调用时，根据需要类型进行转换)
     *
     * @param money
     * @return
     */
    public static BigDecimal moneyChangeCent(Float money) {
        if (null != money) {
            BigDecimal moneyBig = new BigDecimal(money);
            return moneyBig.multiply(changeMoney);
        }
        return dollarZero;
    }
}
