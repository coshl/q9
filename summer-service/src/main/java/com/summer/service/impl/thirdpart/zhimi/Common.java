/**
 *
 */
package com.summer.service.impl.thirdpart.zhimi;

import java.util.Map;

public class Common {

    public static final String CHARSET = "UTF-8";

    //请求URL
    public static final String URL = "http://47.110.34.83/pay.htm";

    public static final String key = "2a6a5345-c250-4ac5-8a45-15a88c862375";

    public static String create_sign(Map<String, String> param) {
        System.out.println("拼接签名原串-----------------------------------------");
        StringBuffer paramstr = new StringBuffer();
        for (String pkey : param.keySet()) {
            String pvalue = param.get(pkey);
            paramstr.append(pkey + "=" + pvalue + "&"); // 签名原串，

        }
        String paramSrc = paramstr.substring(0, paramstr.length() - 1);
        paramSrc = paramSrc + key;
        return MD5Utils.ecodeByMD5(paramSrc);
    }


}
