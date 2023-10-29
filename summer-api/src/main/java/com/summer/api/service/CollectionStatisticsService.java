package com.summer.api.service;

import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.vo.CollectionStatisticsVo;

import java.util.List;
import java.util.Map;

/**
 * 催收业务统计
 */
public interface CollectionStatisticsService {

    /**
     * 查询催收业务统计
     *
     * @param jsonData
     * @param platformUser
     * @return
     */
    String queryCollectionStatistics(String jsonData, PlatformUser platformUser);

    List<CollectionStatisticsVo> findByParams(Map<String, Object> params);
}
