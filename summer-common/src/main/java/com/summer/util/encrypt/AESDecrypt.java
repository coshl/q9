package com.summer.util.encrypt;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Slf4j
public class AESDecrypt {

    // 密钥

    public static String key = "ds2jaHn67w810001";
    private static String charset = "utf-8";
    // 偏移量
    private static int offset = 16;
    private static String transformation = "AES/CBC/PKCS5Padding";
    private static String algorithm = "AES";

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        return encrypt(content, key);
    }

    /**
     * 解密
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        return decrypt(content, key);
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    public static String encrypt(String content, String key) {
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
            Cipher cipher = Cipher.getInstance(transformation);
            byte[] byteContent = content.getBytes(charset);
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return new Base64().encodeToString(result); // 加密
        } catch (Exception e) {
            log.error("【加密】异常----content={},e={}", content, e);
        }
        return null;
    }

    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     * @throws Exception
     */
    public static String decrypt(String content, String key) {
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化
            byte[] result = cipher.doFinal(new Base64().decode(content));
            return new String(result); // 解密
        } catch (Exception e) {
            log.error("【解密】异常----content={},e={}", content, e);
        }
        return null;
    }

    /**
     * 对map里的手机号加密
     */
    public static void encryptMapPhone(Map<String, Object> param) {
        Object phoneNumber = param.get("phoneNumber");
        if (phoneNumber != null) {
            param.replace("phoneNumber", encrypt(phoneNumber.toString()));
        }

        Object phone = param.get("phone");
        if (phone != null) {
            param.replace("phone", encrypt(phone.toString()));
        }
        Object realName = param.get("realName");
        if (null != realName) {
            param.replace("realName", encrypt(realName.toString()));
        }

    }

    public static void main(String[] args) throws Exception {
        //String phone = "*124#";
        // idCard = "H5TLED4xURxrB7kAmcbX5w==";

        String key = "ds2jaHn67w810019";
//        System.out.println(encrypt("18858784838", key) +","+encrypt("18977565600", key)+","+encrypt("14795354625", key));
        System.out.println("+++++++++++++++++++++++++");


        key = "ds2jaHn67w810014";
        System.out.println("加密：" + encrypt("", key));
        System.out.println("解密：" + decrypt("mPmsuMztsI+Q6rF+LGt9Kg==", key));
        System.out.println(decrypt("cDvc8VUdLuKmPjCKUK2i0A==", key));

        key = "ds2jaHn67w810035";
        System.out.println(encrypt("18977565600", key));
        //  System.out.println(decrypt("OA7L4ikbZEzgSSZmLnDcfw==", key));

        key = "ds2jaHn67w810035";
        System.out.println(encrypt("14795354625", key));
        System.out.println(encrypt("18318253749"));


        //String decrypt = decrypt("CF7TrhE9fhSFwNRIDaH5aQ==", "ds2jaHn67w8njab1");
        // System.out.println(decrypt);
    }
}
