package com.summer.service.pay.bitePay.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 入账还款查询接口返回值
 *
 *
 * @since 2022/4/19
 */
@Data
public class SearchInAccountResultDTO implements Serializable {
    private static final long serialVersionUID = -6655939234421957012L;

    /**
     * 0为失败 1 为成功
     */
    private String status;
    /**
     * 原始收款金额
     */
    private BigDecimal oldmoney;
    /**
     * 实际收款金额
     */
    private BigDecimal money;
    /**
     * 错误时提示消息
     */
    private String msg;
    /**
     * 0为失败 1 为成功
     */
    private String sign;
}
