package com.summer.dao.mapper;

import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;

public class BaseDao {

    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return this.sqlSessionTemplate;
    }

}
