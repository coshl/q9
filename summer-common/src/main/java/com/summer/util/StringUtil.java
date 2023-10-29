package com.summer.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串处理的工具类
 */
public class StringUtil {

    public static Integer counter =0;
    /**
     * 截取后几位
     *
     * @param str
     * @return
     */
    public static String subString(String str, Integer number) {
        if (StringUtils.isNotBlank(str)) {
            return str.substring(str.length() - number, str.length());
        }
        return "";
    }

    /**
     * 判断str1中包含str2的个数
     * @param str1
     * @param str2
     * @return counter
     */
    public static int countStr(String str1, String str2) {
        if (str1.indexOf(str2) == -1) {
            return 0;
        } else if (str1.indexOf(str2) != -1) {
            counter++;
            countStr(str1.substring(str1.indexOf(str2) +
                    str2.length()), str2);
            return counter;
        }
        return 0;
    }

}
