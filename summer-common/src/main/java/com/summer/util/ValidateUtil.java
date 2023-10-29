package com.summer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验手机号码是否合法.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateUtil {

    /**
     * 校验手机号码是否合法.
     *
     * @param mobile the mobile
     * @return the boolean
     */
    public static boolean isMobileNumber(final String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        // final String reg =  "^((\\+?86)|(\\(\\+86\\)))?(13[0-9][0-9]{8}|14[0-9]{9}|15[0-9][0-9]{8}|16[0-9][0-9]{8}|17[0-9][0-9]{8}|18[0-9][0-9]{8}|19[0-9][0-9]{8})$";
        final String reg = Constant.PHONE_REGULAR;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static boolean veryPwd(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        final String reg = Constant.PASSWORD_REGULAR;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //TODO 00 密码
    public static void main(String[] args) {
        boolean b = veryPwd("as7752100");
        System.out.println(b);
        boolean b1 = veryPwd("c5Wg_2sX3f_g6Vd4_");
        System.out.println(b1);
        System.out.println(MD5Util.md5("as7752100" + "IPg5ue"));
    }
}
