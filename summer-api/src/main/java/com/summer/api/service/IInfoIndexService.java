package com.summer.api.service;

import com.summer.dao.entity.InfoIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP首页信息Service接口
 */
public interface IInfoIndexService {

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authBank(HashMap<String, Object> map);

    /**
     * 用户信息认证
     *
     * @param map
     */
    void authBankOld(HashMap<String, Object> map);
}
