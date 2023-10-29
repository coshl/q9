package com.summer.dao.mapper;

import com.summer.dao.entity.Configuration;

public interface ConfigurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Configuration record);

    int insertSelective(Configuration record);

    Configuration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Configuration record);

    int updateByPrimaryKey(Configuration record);

}