package com.summer.dao.mapper;

import com.summer.dao.entity.HelpCenter;

public interface HelpCenterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HelpCenter record);

    int insertSelective(HelpCenter record);

    HelpCenter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HelpCenter record);

    int updateByPrimaryKeyWithBLOBs(HelpCenter record);

    int updateByPrimaryKey(HelpCenter record);

    int selcetColumn();
}