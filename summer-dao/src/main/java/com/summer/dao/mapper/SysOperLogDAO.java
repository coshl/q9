/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.SysOperLog;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface SysOperLogDAO {
    /**
     * 根据ID删除
     *
     * @param operId 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Long operId);

    /**
     * 添加对象所有字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(SysOperLog record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(SysOperLog record);

    /**
     * 根据ID查询
     *
     * @param operId 主键ID
     * @return 返回查询的结果
     */
    SysOperLog selectByPrimaryKey(Long operId);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(SysOperLog record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(SysOperLog record);
}