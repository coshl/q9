package com.summer.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class AesUtils {
    private static final String CHARSET_NAME = "UTF-8";


    /**
     * 加密
     *
     * @param input 输入
     * @param key   关键
     * @return {@code String}
     */
    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(CHARSET_NAME), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes(CHARSET_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(crypted);
    }

    /**
     * 解密
     *
     * @param input 输入
     * @param key   关键
     * @return {@code String}
     */
    public static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(CHARSET_NAME), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(input.getBytes(CHARSET_NAME)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(output);
    }
}
