package com.summer.util;

import com.google.common.collect.Maps;
import com.summer.util.risk.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 32127 on 2019/7/7.
 */
@Slf4j
public class UsdtSignUtil {


    public static final String ACCESS_KEY = "accessKey";

    public static final String TIMESTAMP_KEY = "timestamp";

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static final String SPLIT_SYMBOL = ".";

    private static final String ENC = "UTF-8";

    /**
     * 请求参数的key和value进行URLEncoder
     *
     * @param str 值
     * @return Encoder值
     */
    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, ENC);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取待签名的字符串
     *
     * @param paramMap 请求参数的map
     * @return 签名的字符串
     */
    public static String getParamString(Map<String, Object> paramMap) {
        //对所有参数（去除accesskey）顺序排序后做URLencode后做拼接(用.连接，参数没有值留空也参与拼接)
        TreeMap<String, Object> treeMap = Maps.newTreeMap();
        treeMap.putAll(paramMap);
        treeMap.remove(ACCESS_KEY);
        treeMap.remove("sign");

        StringBuilder sb = new StringBuilder();
        //把map中的集合拼接成字符串
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            //key值参与encode
            String key = encode(entry.getKey());
            //如果value没有值，则设置为空串，参与拼接字符串
            String value = StringUtils.defaultString(String.valueOf(entry.getValue()), "");
            if (StringUtils.isNotBlank(value)) {
                //如果value不是空串，则参与encode
                value = encode(String.valueOf(entry.getValue()));
            }
            sb.append(key).append("=").append(value).append(SPLIT_SYMBOL);
        }

        String paramStr = sb.toString();

        if (sb.length() > 0) {
            paramStr = sb.substring(0, sb.length() - 1);
        }

        return paramStr;
    }

    public static String getJsonString(Map<String, Object> paramMap) {


        StringBuilder sb = new StringBuilder();
        //把map中的集合拼接成字符串
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            //key值参与encode
            String key = entry.getKey();
            //如果value没有值，则设置为空串，参与拼接字符串
            String value = StringUtils.defaultString(String.valueOf(entry.getValue()), "");

            sb.append(key).append("=").append(value).append("&&");
        }

        String paramStr = sb.toString();

        if (sb.length() > 0) {
            paramStr = sb.substring(0, sb.length() - 2);
        }

        return paramStr;
    }

    /**
     * 按照规则生成签名值
     *
     * @param nonce            nounce值
     * @param accessEncryptKey 秘钥
     * @param paramMap         请求参数
     * @return 签名值
     */
    public static String getAccesskey(String nonce, String accessEncryptKey, Map<String, Object> paramMap) {

        //获取待签名字符串
        String paramString = getParamString(paramMap);
        //accesskey=md5(A.B).tolower()
        //A=nonce
        //B=HmacSHA1(待签名字符串，access_encrypt_key)
        String accesskey = "";
        try {
            String hmacStr = getSHA1(paramString, accessEncryptKey);
            log.info("【accesskey】--------------nonce={},encrypt={},hmac={},param={}", nonce, accessEncryptKey, hmacStr, paramString);
            //accesskey = DigestUtils.md5Hex((nonce + SPLIT_SYMBOL + hmacStr).getBytes()).toLowerCase();
            log.info("【nonce + SPLIT_SYMBOL + hmacStr 参数】----------------key2={}", (nonce + SPLIT_SYMBOL + hmacStr));
            log.info("【nonce + SPLIT_SYMBOL + hmacStr 参数】----------------key={}", (nonce + SPLIT_SYMBOL + hmacStr).getBytes());
            accesskey = MD5Util.md5((nonce + SPLIT_SYMBOL + hmacStr).getBytes()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accesskey;
    }

    /**
     * HmacSHA1加密算法
     *
     * @param paramString      原始字符串
     * @param accessEncryptKey 秘钥
     * @return 加密字符串
     */
    public static String getSHA1(String paramString, String accessEncryptKey) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(new javax.crypto.spec.SecretKeySpec(accessEncryptKey.getBytes(ENC), HMAC_SHA1_ALGORITHM));
            byte[] signData = mac.doFinal(paramString.getBytes(ENC));
            return new sun.misc.BASE64Encoder().encode(signData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "cash=1.mark=mark.money=500.00.payType=2.platformCode=10459682.thirdOrderId=201908289980000088.timestamp=1566978516391.userAccount=10459682.userId=42433";
        String accesskey = "";
        try {
            String hmacStr = getSHA1(str, "2a29b010eb808820394c10a12bbc43a2");
            log.info("hmacStr param {}", hmacStr);
            accesskey = DigestUtils.md5Hex(("869894" + SPLIT_SYMBOL + hmacStr).getBytes()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(accesskey);
    }
}
