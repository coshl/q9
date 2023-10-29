package com.summer.service.pay.bitePay.constants;

/**
 * 第三方接口API常量
 *
 *
 * @since 2022/4/19
 */
public final class Constant {
    Constant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 接口地址
     */
    public static final String WEB_URL = "http://www.bitespay.co/api/";

    /**
     * 应用ID
     */
    public static final int APP_ID = 136;

    /**
     * 回调类型
     */
    public static final String NOTIFY_TYPE = "2";

    /**
     * 回调地址
     */
    public static final String NOTIFY_OUT_URL = "v1.0/api/changJiePay/btzfDfBack";

    /**
     * apikey密钥
     */
    public static final String API_KEY = "z8Ez1VK0qf58hQHhsXc59B6zrlQTztqN";

    /**
     * 请求值type
     */
     public static final String OUTACCOUNT = "outaccount";
     public static final String INACCOUNT = "inaccount";
     public static final String SEARCHINACCOUNT = "searchinaccount";
}
