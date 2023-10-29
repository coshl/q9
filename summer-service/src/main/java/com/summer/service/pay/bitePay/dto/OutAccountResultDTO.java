package com.summer.service.pay.bitePay.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 出账类下单接口返回值
 *
 *
 * @since 2022/4/19
 */
@Data
public class OutAccountResultDTO implements Serializable {
    private static final long serialVersionUID = -7705625881852320970L;

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
}
