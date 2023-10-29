package com.summer.api.service;

import com.summer.dao.entity.UserBlackList;
import com.summer.pojo.vo.UserBlackListVo;

import java.util.List;
import java.util.Map;

/**
 * 用户黑名单Service接口
 */
public interface IUserBlackListService {
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
    int insert(UserBlackList record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(UserBlackList record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    UserBlackList selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(UserBlackList record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(UserBlackList record);

    /**
     * 通过手机号查询黑名单
     *
     * @param phone
     * @return
     */
    UserBlackList findByPhone(String phone);

    List<UserBlackListVo> findByParam(Map<String, Object> param);
}
