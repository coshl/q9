package com.summer.api.service;

import com.summer.dao.entity.AuthRuleConfig;

import java.util.List;

/**
 * 认证规则配置的Service接口类
 */
public interface IAuthRuleConfigService {
    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加对象所有字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(AuthRuleConfig record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(AuthRuleConfig record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    AuthRuleConfig selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(AuthRuleConfig record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(AuthRuleConfig record);

    /**
     * 查询所有的认证列表
     *
     * @return
     */
    List<AuthRuleConfig> findAllAuthRule();
}
