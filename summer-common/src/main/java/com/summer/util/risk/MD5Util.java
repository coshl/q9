package com.summer.util.risk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MD5Util {

    private static final Logger LOG = LoggerFactory.getLogger(MD5Util.class);
    private static final char hexDigIts[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5
     *
     * @param bytes
     * @return
     */
    public static String md5(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigIts[byte0 >>> 4 & 0xf];
                str[k++] = hexDigIts[byte0 & 0xf];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * MD5[16位]
     *
     * @param bytes
     * @return
     */
    public static String md5For16(byte[] bytes) {
        return md5(bytes).substring(8, 24);
    }

    /**
     * 排序方法
     *
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String sort(String token, String nonce, String timestamp) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }
        return sbuilder.toString();
    }
}
