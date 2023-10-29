package com.summer.dao.mapper;


import com.summer.dao.entity.SourceH5;

public interface SourceH5Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SourceH5 record);

    int insertSelective(SourceH5 record);

    SourceH5 selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SourceH5 record);

    int updateByPrimaryKey(SourceH5 record);
}