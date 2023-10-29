/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MD5Util
 * Author:   Liubing
 * Date:     2018/5/17 14:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.summer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;


public class MD5Util {

    private static final Logger log = LoggerFactory.getLogger(MD5Util.class);

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MessageDigest不支持MD5Util", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * md5签名
     * <p>
     * 按参数名称升序，将参数值进行连接 签名
     *
     * @param appSecret
     * @param params
     * @return
     */
    public static String sign(String appSecret, TreeMap<String, String> params) {
        StringBuilder paramValues = new StringBuilder();
        params.put("appSecret", appSecret);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramValues.append(entry.getValue());
        }
        String secret = md5(paramValues.toString());
        return md5(secret);
    }

    /**
     * GetMD5Code:(md5加密). <br/>
     *
     * @param param 需要加密的字段
     * @return 加密后的字段
     * @author wz
     */
    public static String GetMD5Code(String param) {
        String resultString = null;
        try {
            resultString = new String(param);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(param.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * byteToString:(转换字节数组为16进制字串). <br/>
     *
     * @param bByte byte数组
     * @return 返回转换字节数组为16进制字串
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * byteToArrayString:(返回形式为数字跟字符串). <br/>
     *
     * @param bByte byte
     * @return 返回形式为数字跟字符串
     * @author wz
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 32;
        int iD2 = iRet % 32;
        return DIGITS[iD1] + DIGITS[iD2];
    }

    /**
     * 全局数组
     */
    private static final String[] DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "H", "i",
            "j", "k", "l", "m", "n", "~", "$", "@", "%", "*", "#", "&", "!"};

    /**
     * 请求参数签名验证
     *
     * @param appSecret
     * @param request
     * @return true 验证通过 false 验证失败
     * @throws Exception
     */
    public static boolean verifySign(String appSecret, HttpServletRequest request) throws Exception {
        TreeMap<String, String> params = new TreeMap<String, String>();

        String signStr = request.getParameter("sign");
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paramName = enu.nextElement().trim();
            if (!"sign".equals(paramName)) {
                params.put(paramName, URLDecoder.decode(request.getParameter(paramName), "UTF-8"));
            }
        }
        String result = sign(appSecret, params);
        if (!result.equals(signStr)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("qazwsxedc");
        System.out.println(md5(sb.toString()));
    }
}
