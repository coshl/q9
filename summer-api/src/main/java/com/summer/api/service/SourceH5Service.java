package com.summer.api.service;

import com.summer.dao.entity.SourceH5;

/**
 * h5列表
 */
public interface SourceH5Service {

    SourceH5 findById(Long id) throws Exception;

    SourceH5 save(SourceH5 sourceH5) throws Exception;

    SourceH5 update(SourceH5 sourceH5) throws Exception;

    SourceH5 findByDomainName(String domainName) throws Exception;
}
