package com.summer.service.pay.bitePay.dto;


import com.summer.service.pay.bitePay.constants.Constant;
import lombok.Data;

import java.io.Serializable;

/**
 * 入账还款查询接口请求参数
 *
 *
 * @since 2022/4/19
 */
@Data
public class SearchInAccountParamDTO implements Serializable {
    private static final long serialVersionUID = -8786485303076739397L;

    public SearchInAccountParamDTO() {
        this.appid = Constant.APP_ID;
        this.type = Constant.SEARCHINACCOUNT;
        this.apikey = Constant.API_KEY;
        this.weburl = Constant.WEB_URL;
    }

    public SearchInAccountParamDTO(int appid, String type, String apikey, String weburl) {
        this.appid = appid;
        this.type = type;
        this.apikey = apikey;
        this.weburl = weburl;
    }

    /**
     * 请求值
     */
    private String type;
    /**
     * 应用ID
     */
    private Integer appid;
    /**
     * 商户订单号(长度30字符)
     */
    private String outTradeNo;
    /**
     * 签名
     *  加密方式：md5({将签名字段以字典排序后格式为URL参数}&key={apikey密钥：登陆后台获取})；
     */
    private String sign;
    /**
     * apikey
     */
    private String apikey;
    /**
     * 接口地址
     */
    private String weburl;
}
