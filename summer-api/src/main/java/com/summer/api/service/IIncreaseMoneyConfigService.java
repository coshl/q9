package com.summer.api.service;

import com.summer.dao.entity.IncreaseMoneyConfig;
import com.summer.pojo.vo.IncreaseMoneyConfigVo;

import java.util.List;

/**
 * 提额降息配置的Service接口类
 */
public interface IIncreaseMoneyConfigService {
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
    int insert(IncreaseMoneyConfig record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(IncreaseMoneyConfig record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    IncreaseMoneyConfig selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(IncreaseMoneyConfig record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(IncreaseMoneyConfig record);

    /**
     * 根据类型查询提额降息
     *
     * @return
     */
    List<IncreaseMoneyConfigVo> findAllIncreaseConfig(Byte type);

    /**
     * 一键开启或者关闭提额
     *
     * @param status
     * @return
     */
    int updateByStatus(Integer status);
}
