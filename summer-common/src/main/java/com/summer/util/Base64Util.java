package com.summer.util;


import java.io.UnsupportedEncodingException;


import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

public class Base64Util {
    private static Logger logger = LoggerFactory.getLogger(Base64Util.class);
    private static final String charset = "utf-8";

    /**
     * 解密
     *
     * @param data
     * @return
     * @author fanddong
     */
    public static String decode(String data) {
        try {
            if (null == data) {
                return null;
            }

            return new String(Base64.decodeBase64(data.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("字符串：%s，解密异常", data));
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @return
     * @author fanddong
     */
    public static byte[] decode2byte(String data) {
        try {
            if (null == data) {
                return null;
            }
            return Base64.decodeBase64(data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("字符串：%s，解密异常", data));
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @return
     * @author fanddong
     */
    public static byte[] decode(byte[] data) {
        try {
            if (null == data) {
                return null;
            }
            return Base64.decodeBase64(data);
        } catch (Exception e) {
            logger.error(String.format("字符串：%s，解密异常", data));
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @author fanddong
     */
    public static String encode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.encodeBase64(data.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("字符串：%s，加密异常", data));
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @author fanddong
     */
    public static String encode(byte[] data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.encodeBase64(data), charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("字符串：%s，加密异常", data));
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        String pa="123456";
        String encode = encode(pa);
        System.out.println(encode);
        String decode = decode("Q0tvazhFdHU=");
        System.out.println(decode);
    }
}
