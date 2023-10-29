package com.summer.api.service;

import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.vo.OrderCollectionCallerVo;

import java.util.List;
import java.util.Map;

/**
 * 分配
 */
public interface OrderCollectionCallerService {

    String selectRepaymentDistribution(Map<String, Object> params);

    List<OrderCollectionCallerVo> findParams(Map<String, Object> params);

    String updateDistribution(Map<String, Object> params);

    String transfer(String jsonData, PlatformUser platformUser);
}
