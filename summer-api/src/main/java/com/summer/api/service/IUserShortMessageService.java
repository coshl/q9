package com.summer.api.service;

import com.summer.dao.entity.UserShortMessage;
import com.summer.pojo.vo.UserShortMessageVo;

import java.util.List;
import java.util.Map;

/**
 * 用户短信的Service接口
 */
public interface IUserShortMessageService {
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
    int insert(UserShortMessage record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(UserShortMessage record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    UserShortMessage selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(UserShortMessage record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(UserShortMessage record);

    /**
     * 根据用户ID查询用户短信
     *
     * @param userId
     * @return
     */
    List<UserShortMessage> findByUserId(Integer userId);

    /**
     * 根据用户id删除短信
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
    int insertBatchSelective(List<UserShortMessage> item);

    List<UserShortMessageVo> selectByPhone(Map<String, Object> params);
}
