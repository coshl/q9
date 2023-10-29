package com.summer.api.service;

import com.summer.dao.entity.OrderCollectionReduction;

import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/1/5
 */
public interface IOrderReductionService {
    /**
     * 减免确认
     *
     * @param params
     * @param orderCollectionReduction
     */
    void reductConfirm(Map<String, Object> params, OrderCollectionReduction orderCollectionReduction, Integer platformUserId);

    /**
     * 减免确认
     *
     * @param params
     * @param orderCollectionReduction
     */
    void reductRepay(Map<String, Object> params, OrderCollectionReduction orderCollectionReduction, Integer platformUserId);
}
