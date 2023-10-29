/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.CountCollectionAssessment;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface CountCollectionAssessmentDAO {
    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 添加对象所有字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(CountCollectionAssessment record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(CountCollectionAssessment record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    CountCollectionAssessment selectByPrimaryKey(Long id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(CountCollectionAssessment record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(CountCollectionAssessment record);

    /**
     * 根据条件查询每日催收报表
     *
     * @param params
     * @return
     */
    List<CountCollectionAssessment> findParams(Map<String, Object> params);

    void insertList(List<CountCollectionAssessment> list);
}