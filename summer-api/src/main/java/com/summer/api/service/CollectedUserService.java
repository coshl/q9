package com.summer.api.service;

import java.util.Map;

/**
 * 逾期用户
 */
public interface CollectedUserService {

    /**
     * 新增或修改逾期黑名单记录
     *
     * @param params
     * @return
     */
    int insertOrUpdateCollectedUser(Map<String, Object> params);
}
