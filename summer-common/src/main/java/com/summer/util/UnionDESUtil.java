package com.summer.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;

public class UnionDESUtil {

    private final static String secretKey = "UTF-8";

    public static void main(String[] args) throws Exception {
        String secretKey = "af4bd50a21fe0851a24c7bf3";
        JSONObject dataJson = new JSONObject();
        //dataJson.put("idno_md5", "9b5eaa1f623ed663c9e6969e2b1ed04f");
        dataJson.put("phone_md5", "13695770653");
        dataJson.put("city", "浙江省/杭州市");
        String dataStr = encrypt(dataJson.toJSONString(), secretKey);

        JSONObject reqJson = new JSONObject();
        reqJson.put("data", dataStr);
        System.out.println("请求参数：" + reqJson);
        System.out.println("解密开始：");
        String decryptStr = decrypt(reqJson.getString("data"), secretKey);
        System.out.println("解密结果：" + decryptStr);
    }

    private static String encrypt(String data, String secretKey) throws Exception {
        byte[] encrpyted = tripleDES(Cipher.ENCRYPT_MODE, data.getBytes(StandardCharsets.UTF_8), secretKey.getBytes());
        return new String(Base64.encodeBase64(encrpyted));
    }

    public static String decrypt(String data, String secretKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(data); // Base64 解码
        byte[] decrypted = tripleDES(Cipher.DECRYPT_MODE, decoded, secretKey.getBytes());// 3DES 解密
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static byte[] tripleDES(int opmode, byte[] data, byte[] secretKey) throws Exception {
        return cipher("DESede", "DESede/CBC/PKCS5Padding", opmode, data, "01234567".getBytes(), secretKey);
    }

    private static byte[] cipher(String algorithm, String transformation, int opmode, byte[] data, byte[] iv,
                                 byte[] secretKey) throws Exception {
        Key key = SecretKeyFactory.getInstance(algorithm).generateSecret(new DESedeKeySpec(secretKey));
        IvParameterSpec spec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, key, spec);
        return cipher.doFinal(data);
    }
}

