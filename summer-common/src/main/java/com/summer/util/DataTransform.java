package com.summer.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数据转换的类
 */
public class DataTransform {

    /**
     * 缩小100倍
     *
     * @param data
     * @return
     */
    public static BigDecimal channgeSmall(Integer data) {
        if (null != data) {
            BigDecimal changeUnit = new BigDecimal(100);
            BigDecimal needData = new BigDecimal(data);
            return needData.divide(changeUnit, 2, BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }

    /**
     * 缩小100倍
     *
     * @param data
     * @return
     */
    public static BigDecimal channgeSmall(Double data) {
        if (null != data) {
            BigDecimal changeUnit = new BigDecimal(100);
            BigDecimal needData = new BigDecimal(data);
            return needData.divide(changeUnit, 2, BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }


    /**
     * 扩大100倍
     *
     * @param data
     * @return
     */
    public static Double changeBigDouble(double data) {
        DecimalFormat df = new DecimalFormat("0.00");
        double newData = data * 100.0;
        String formatData = df.format(newData);
        return Double.parseDouble(formatData);
    }

    /**
     * 缩小100倍
     *
     * @param data
     * @return
     */
    public static Double changeSmallDouble(double data) {
        DecimalFormat df = new DecimalFormat("0.0000");
        double newData = data / 100.0;
        String formatData = df.format(newData);
        return Double.parseDouble(formatData);
    }
}
