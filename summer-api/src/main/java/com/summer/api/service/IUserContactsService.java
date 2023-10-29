package com.summer.api.service;

import com.summer.dao.entity.UserContacts;

import java.util.List;
import java.util.Map;

/**
 * 用户通讯录Service接口
 */
public interface IUserContactsService {
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
    int insert(UserContacts record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(UserContacts record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    UserContacts selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(UserContacts record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(UserContacts record);

    /**
     * 根据id查询通讯录
     *
     * @param userId
     * @return
     */
    List<UserContacts> findContatsByUserId(Integer userId);

    /**
     * 根据参数查找
     *
     * @param map
     * @return
     */
    List<UserContacts> selectUserContacts(Map<String, Object> map);

    /**
     * 根据用户id删除通讯录
     *
     * @param userId
     * @return
     */
    int deleteByUserId(Integer userId);

    /**
     * 批量插入
     *
     * @param item
     * @return
     */
    int insertBatchSelective(List<UserContacts> item);
}
