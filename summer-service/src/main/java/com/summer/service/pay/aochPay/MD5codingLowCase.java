package com.summer.service.pay.aochPay;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * MD5
 *
 * @author gaoyuhai
 */
public class MD5codingLowCase {

    private static final Logger logger = LoggerFactory.getLogger(MD5codingLowCase.class);

    // MD5  32位 小写
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("utf-8");

            byte[] md5Bytes = md5.digest(byteArray);

            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();
        } catch (Exception e) {
            logger.error(e.toString(), e);
            //System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Date date = new Date();
        String passMD5 = "";
        passMD5 = new MD5codingLowCase().code(String.valueOf(date.getTime()));
//		System.out.println(passMD5);

        /**
         * “app_key=qzbauzh8cl2qzd9qssnkrfly4t4nk6m6&batch_num=ces001&content=【流浪猫】尊敬的用户，本次验证码为123456，请勿泄露！&dest_id=15267797477&dest_id=13968880707&mission_num=test001&mission_num=test002&nonce_str=5iliulang&sms_type=verify_code&time_stamp=20160520170033&app_secret=a1df1b975bd9efc6a0d6d7d4c496f1007164”
         *
         * "app_key=qzbauzh8cl2qzd9qssnkrfly4t4nk6m6&batch_num=ces001&content=【流浪猫】尊敬的用户，本次验证码为123456，请勿泄露！&dest_id=15267797477&dest_id=13968880707&mission_num=test001&mission_num=test002&nonce_str=5iliulang&sms_type=verify_code&time_stamp=20160520170033&app_secret=a1df1b975bd9efc6a0d6d7d4c496f1007164"
         */

        String string = "app_key=qzbauzh8cl2qzd9qssnkrfly4t4nk6m6&batch_num=5iliulang20160825101845&content=【流浪猫】尊敬的用户，本次验证码为fbc1，请勿泄露！&dest_id=18721140734&mission_num=verify_code20160825101845&nonce_str=5iliulang&sms_type=verify_code&time_stamp=20160825101845&app_secret=a1df1b975bd9efc6a0d6d7d4c496f1007164";
//		String xxx = "app_key=qzbauzh8cl2qzd9qssnkrfly4t4nk6m6";
        String str = new MD5codingLowCase().MD5(String.valueOf(string));
        System.out.println(str);//ba3fdb2966e1b4975e5f098a513b3b66
        //ba3fdb2966e1b4975e5f098a513b3b66


        //06c0a87ef48adbe5554473e24db240ee
    }

    public String code(String str) throws NoSuchAlgorithmException {
        MessageDigest alga;
        String myinfo = str;
        alga = MessageDigest.getInstance("MD5");
        alga.update(myinfo.getBytes());
        byte[] digesta = alga.digest();
        String hs = "";
        String stmp = "";
        for (int n = 0; n < digesta.length; n++) {
            stmp = (Integer.toHexString(digesta[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

    public String getMD5key(String ticketId, String visitDate) {
        MD5codingLowCase m = new MD5codingLowCase();
        try {
            return m.code(m
                    .code(ticketId + visitDate + "FE0VXC93MRADLKFO32559FREWEQDFBVZX"));
        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }

}
