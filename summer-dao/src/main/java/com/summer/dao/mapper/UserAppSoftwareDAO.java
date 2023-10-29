/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.UserAppSoftware;

import java.util.List;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface UserAppSoftwareDAO {
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
    int insert(UserAppSoftware record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(UserAppSoftware record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    UserAppSoftware selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(UserAppSoftware record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(UserAppSoftware record);

    /**
     * 批量插入数据
     *
     * @param item
     * @return
     */
    int insertBatchSelective(List<UserAppSoftware> item);

    /**
     * 根据用户id查询用户应用
     *
     * @param userId
     * @return
     */
    List<UserAppSoftware> findByUserId(Integer userId);

    /**
     * 根据用户id查询用户应用
     *
     * @param id
     * @return
     */
    UserAppSoftware findUserId(Integer userId);

    /**
     * 根据用户id删除用户已上传APP应用
     *
     * @param userId
     * @return
     */
    int deleteByUserId(Integer userId);
}