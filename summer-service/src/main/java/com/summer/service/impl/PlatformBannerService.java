package com.summer.service.impl;

import com.summer.dao.entity.PlatformBanner;
import com.summer.dao.mapper.PlatformBannerMapper;
import com.summer.api.service.IPlatformBannerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 平台广告Service接口的实现类
 */
@Service
public class PlatformBannerService implements IPlatformBannerService {

    @Resource
    private PlatformBannerMapper platformBannerMapper;

    @Override
    public List<PlatformBanner> findIndexImgByParam(Map<String, Object> map) {
        return platformBannerMapper.findIndexImgByParam(map);
    }
}
