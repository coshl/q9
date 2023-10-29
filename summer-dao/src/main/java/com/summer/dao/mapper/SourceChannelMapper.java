package com.summer.dao.mapper;

import com.summer.dao.entity.SourceChannel;

public interface SourceChannelMapper {

    SourceChannel findByCode(String code);
}
