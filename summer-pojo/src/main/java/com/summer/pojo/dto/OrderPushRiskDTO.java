package com.summer.pojo.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单重推DTO
 */
public class OrderPushRiskDTO implements Serializable {
    private static final long serialVersionUID = -2310852049709586523L;
    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Integer orderId;
    //用户id
    private Integer userId;
    /**
     * 订单状态
     */
    @NotNull(message = "订单状态不能为空")
    private Integer status;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
