package com.summer.service.impl;

import com.summer.dao.entity.SourceH5;
import com.summer.dao.mapper.SourceH5Mapper;
import com.summer.api.service.SourceH5Service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * --Service
 */
@Service("sourceH5ServiceImpl")
public class SourceH5ServiceImpl implements SourceH5Service {

    @Resource
    private SourceH5Mapper sourceH5Mapper;

    @Override
    public SourceH5 findById(Long id) throws Exception {
        return null;
    }

    @Override
    public SourceH5 save(SourceH5 sourceH5) throws Exception {
        return null;
    }

    @Override
    public SourceH5 update(SourceH5 sourceH5) throws Exception {
        return null;
    }

    @Override
    public SourceH5 findByDomainName(String domainName) throws Exception {
        return null;
    }
}
