package com.summer.api.service;

import com.summer.dao.entity.OrderRenewal;

import java.util.List;

/**
 * Desc:
 * Created by tl on 2018/12/21
 */
public interface IOrderRenewalService {
    /**
     * 根据还款id查询续期记录
     *
     * @param repaymentId
     * @return
     */
    List<OrderRenewal> findByRepaymentId(Integer repaymentId);

    List<OrderRenewal> findByBorrowId(Integer borrowId);
}
