package com.summer.service.pay.bitePay.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 入账类下单接口返回值
 *
 *
 * @since 2022/4/19
 */
@Data
public class InAccountResultDTO implements Serializable {
    private static final long serialVersionUID = -6117436467463723110L;

    /**
     * 状态：0为失败 1 为成功
     */
    private String status;
    /**
     * 系统回返订单
     */
    private String transactionId;
    /**
     * 错误时提示消息
     */
    private String msg;
    /**
     * 收款页面URL
     */
    private String payurl;
}
