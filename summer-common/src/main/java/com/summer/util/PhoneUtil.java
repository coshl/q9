package com.summer.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ls
 * @Title:
 * @date 2019/2/22 16:44
 */
public class PhoneUtil {

    public static boolean isPhone(String fixedPhone) {
        String reg = Constant.PHONE_REGULAR;
        return Pattern.matches(reg, fixedPhone);
    }

    /**
     * 给手机号增加掩码
     */
    public static String maskPhoneNum(String phoneNumber) {
        if (StringUtils.isNotEmpty(phoneNumber) && phoneNumber.length() == 11) {
            // 把手机号中间4位掩码处理
            phoneNumber = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return phoneNumber;
    }

    /**
     * 得到不带86开头的号码
     *
     * @param number
     * @return
     */
    public static String getNoWith86Number(String number) {
        String regular = number;
        if (StringUtils.isNotBlank(regular)) {
            // 去掉+号
            while(regular.startsWith("+")) {
                regular = regular.substring(1);
            }
            // 去掉+号
            while(regular.startsWith("-")) {
                regular = regular.substring(1);
            }
            // 号码以0开始,去掉前缀
            while(regular.startsWith("0")) {
                regular = regular.substring(1);
            }
            // 号码以86开始,去掉前缀
            while(regular.startsWith("86")) {
                regular = regular.substring(2);
            }
        }
        return regular;
    }

    /**
     * 得到带86开头的号码
     *
     * @param number
     * @return
     * @throws Exception
     */
    public static String getWith86Number(String number){
        String regular = getNoWith86Number(number);
        if(StringUtils.isNotBlank(regular)){
            regular = "86"+getNoWith86Number(number);
        }
        return regular;
    }
}
