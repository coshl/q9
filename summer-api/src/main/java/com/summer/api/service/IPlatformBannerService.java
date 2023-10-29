package com.summer.api.service;

import com.summer.dao.entity.PlatformBanner;

import java.util.List;
import java.util.Map;

/**
 * 平台广告Service接口类
 */
public interface IPlatformBannerService {
    /**
     * 根据参数查询平台广告
     *
     * @param map
     * @return
     */
    List<PlatformBanner> findIndexImgByParam(Map<String, Object> map);
}
