package com.summer.service.impl.thirdpart.zhimi;

import com.summer.service.impl.thirdpart.zhimi.base64.Base64Util;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class AESUtil {

    private static final String ENCODE = "UTF-8";
    private static final String FORMAT = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    /**
     * 加密 ECB
     */
    public static String encryption(String content, String secretKey) {
        if (secretKey == null) {
            return null;
        }
        if (secretKey.length() % 16 != 0) {
            return null;
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] raw = secretKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            String base64String = Base64Util.base64Encode(encrypted);
            return base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密
     */
    public static String decode(String str, String skey) {
        try {
            if (str == null) {
                return null;
            }

            byte[] data = Base64Util.base64DecodeToArray(str);

            Security.addProvider(new BouncyCastleProvider());

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            SecretKeySpec secretKeySpec = new SecretKeySpec(skey.getBytes(ENCODE), FORMAT);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return new String(cipher.doFinal(data), ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String key="ca3iTpRgfVdzk1bqNNUDLNTQonmjiyer";
        String abc = encryption("{\"realname\":\"张三\",\"mobile\":\"11111111\",\"idcard\":\"23122344857232X\",\"card_account\":\"12374xxxxxxx7747474\"}", key);
        System.out.println(abc);
        String def = decode(abc, key);
        System.out.println(def);
    }
}
