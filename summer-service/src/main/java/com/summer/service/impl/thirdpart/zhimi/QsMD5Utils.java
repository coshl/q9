package com.summer.service.impl.thirdpart.zhimi;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class QsMD5Utils {
    private static final char[] HEX_DIGITS = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    public static String hexdigest(String content) {
        if (content == null) {
            return null;
        }

        try {
            return hexdigest(content.getBytes(Charset.forName("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String hexdigest(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(input);
        byte[] buffer = messageDigest.digest();
        return bytesToStr(buffer);
    }

    public static String bytesToStr(byte[] buffer) {
        char[] resultBuffer = new char[32];
        int i = 0;
        int j = 0;
        while (true) {
            if (i >= 16)
                return new String(resultBuffer);
            int k = buffer[i];
            int m = j + 1;
            resultBuffer[j] = HEX_DIGITS[(0xF & k >>> 4)];
            j = m + 1;
            resultBuffer[m] = HEX_DIGITS[(k & 0xF)];
            i++;
        }
    }

    public static void main(String[] args) {
        /*String customerAmount = "14.28";
        String customerAmountCny =  "100.00";
        String outOrderId =  "202003051506243030484180340";
        String orderId =  "2003050306287964084870230016";
        String signType =  "MD5";
        String status =  "success";
        String sign =  "0f6e2c5808924f3faedfb4166bcc8ea8";
        String appSecret = "de3dc74249ce89ef3d7ceca8b73d6cfc";
        String str = customerAmount+customerAmountCny+outOrderId+orderId+signType+status+appSecret;

        System.out.println(sign);
        System.out.println(sign1);
        System.out.print(sign1.equals(sign));*/
        System.out.println(getHeader("customerAmountCny=100.00&orderId=2003050443287988398571040768&outOrderId" +
                "=202003051643035570817457305&sign=d48da683b40dd713e51f407bd90d1af2&signType=MD5&customerAmount=14" +
                ".28&status=success"));
    }

 public static Map getHeader(String url){
        Map<String ,String> map = new HashMap<String, String>();
        //int start = url.indexOf("?");
       // if(start > 0){
           // String str = url.substring(start + 1);
            String[] paramsArr = url.split("&");
            for(String param : paramsArr){
                String[] temp = param.split("=");
                map.put(temp[0],temp[1]);
           // }
        }
        return map;
 }

    public static String md5Str(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("utf-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }

    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

}
