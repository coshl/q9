package com.summer.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据年龄计算工具类
 */
@Slf4j
public class AgeUtil {

    public static Integer getAge(String IdNO) {
        int leh = IdNO.length();
        String dates = "0";
        if (leh == 18) {
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            Integer u = Integer.parseInt(year) - Integer.parseInt(dates);
            // 判断u是否在byte范围内
            if (u > Byte.MAX_VALUE || u < Byte.MIN_VALUE) {
                return Integer.parseInt("0");
            }
            return Integer.parseInt(u.toString());
        } else {
            log.info("start invoke AgeUtil.getAge---->身份证号码不是18位");
            return Integer.parseInt(dates);
        }

    }
}
