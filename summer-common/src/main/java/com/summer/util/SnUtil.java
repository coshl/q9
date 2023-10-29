package com.summer.util;

public class SnUtil {

    private static final String SN_TYPE_ORDER = "10";

    public static String getOrderSn(long memberId) {
        String memberStr = Long.toHexString(memberId);
        while (memberStr.length() < 5) {
            memberStr = "0" + memberStr;
        }
        long timeMillis = System.currentTimeMillis() / 1000;
        String sn = SN_TYPE_ORDER + memberStr + timeMillis;
        return sn;
    }

    public static String getPaymentSn(String phone) {
        long timeMillis = System.currentTimeMillis() / 1000;
        String sn = SN_TYPE_ORDER + phone + timeMillis;
        return sn;
    }

    public static String getPaymentSn(String phone, String type) {
        long timeMillis = System.currentTimeMillis() / 1000;
        String sn = type + phone + timeMillis;
        return sn;
    }
}
