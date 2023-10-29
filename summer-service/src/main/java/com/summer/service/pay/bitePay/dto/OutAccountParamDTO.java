package com.summer.service.pay.bitePay.dto;


import com.summer.service.pay.bitePay.constants.Constant;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出账类下单接口请求参数
 *
 *
 * @since 2022/4/19
 */
@Data
public class OutAccountParamDTO implements Serializable {
    private static final long serialVersionUID = -1212869540814225819L;

    public OutAccountParamDTO() {
        this.appid = Constant.APP_ID;
        this.type = Constant.OUTACCOUNT;
        this.apikey = Constant.API_KEY;
        this.weburl = Constant.WEB_URL;
        this.notifyType = Constant.NOTIFY_TYPE;
    }

    public OutAccountParamDTO(int appid, String type, String apikey, String weburl) {
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
     * 订单号
     */
    private String outTradeNo;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String pidnum;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 收款银行
     */
    private String bankname;
    /**
     * 收款账号
     */
    private String bankid;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 通知结果方式
     */
    private String notifyType;
    /**
     * 签名
     *  加密方式：md5({将签名字段以字典排序后格式为URL参数}&key={apikey密钥：登陆后台获取})；
     *  例：appid={appid}&bankid={bankid}&bankname={bankname}&money={money}&name={name}&out_trade_no={out_trade_no}&pidnum={pidnum}&type=outaccount&key={apikey密钥：登陆后台获取}
     *
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
