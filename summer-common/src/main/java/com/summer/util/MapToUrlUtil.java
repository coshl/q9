package com.summer.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;

public class MapToUrlUtil {
    /**
     * 将map转换成url
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
    public static String getParmeString(Map<String, String> map) {
        System.out.println("参数："+map.toString());
        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, String> item : infoIds) {
                if(!item.getKey().equals("sign")){
                    if (item.getKey() != null || item.getKey() != "") {
                        String key = item.getKey();
                        String val = String.valueOf(item.getValue());
                        if (!(val.equals("") || val == null || val.equals("null"))) {
                            sb.append(key + "=" + val + "&");
                        }
                    }
                }
            }
            result = sb.toString();
            result=result.substring(0,result.length()-1);
            System.out.println("参数排序："+result);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * 将url参数转换成map
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Map signMap = new TreeMap();
        signMap.put("merchant_id", "139");
        signMap.put("amount", "10000");
        signMap.put("notify_url", "www.baidu.com");
        signMap.put("out_order_no", "X0000048");
        signMap.put("pay_type", "1");
        signMap.put("account_no", "6258741236584");
        signMap.put("account_name", "市长");
        signMap.put("bank_name", "邮政银行");
        String result = getUrlParamsByMap(signMap);
        System.out.println(result);
        String sign = MD5.sign(result, "&key=6gmDug5ngt8PdhRZWyS3l4SlpuHgNWZk", "utf-8").toUpperCase();
        System.out.println(sign);
    }

}
