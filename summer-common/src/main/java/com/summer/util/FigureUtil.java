package com.summer.util;

/**
 * 数字判断工具类
 */
public class FigureUtil {

    /**
     * 判断是不是100的整数倍
     *
     * @param figure
     * @return
     */
    public static boolean isMulByhundred(Integer figure) {
        if (null != figure) {
            if (figure % 100 != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断金额的范围
     *
     * @param money
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public static boolean isGthundred(Integer money, Integer limitStart, Integer limitEnd) {
        if (null != money) {
            //不能大于某个数 如果为空，就默认不能大于10000
            if (null != limitEnd) {
                if (money > limitEnd) {
                    return true;
                }
            } else { //如果限制为空就 默认限制 金额
                if (money > 10000) {
                    return true;
                }
            }
            //不能小于某个数 ，如果为空就默认不能小与0
            if (null != limitStart) {
                if (money < limitStart) {
                    return true;
                }
            } else { //如果限制为空就 默认不能小于0
                if (money < 0) {
                    return true;
                }
            }
        }
        return false;
    }


}
