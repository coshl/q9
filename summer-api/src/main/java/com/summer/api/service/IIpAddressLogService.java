package com.summer.api.service;

import com.summer.dao.entity.IpAddressLog;
import com.summer.dao.entity.IpAddressLogQuery;
import com.summer.dao.entity.dailystatics.DailyStatisticsChannelProduct;

/**
 * 落地页登录访问日志Service接口
 */
public interface IIpAddressLogService {
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
    int insert(IpAddressLog record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(IpAddressLog record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    IpAddressLog selectByPrimaryKey(Long id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(IpAddressLog record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(IpAddressLog record);

    /**
     * PV Uv统计
     *
     * @param query
     * @return
     */
    DailyStatisticsChannelProduct getPvUv(IpAddressLogQuery query);
}
