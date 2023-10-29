/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.PlatformChannelStaff;
import com.summer.pojo.vo.ChannelStaffVo;
import com.summer.pojo.vo.FinanceStatisticVO;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface PlatformChannelStaffDAO {
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
    int insert(PlatformChannelStaff record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(PlatformChannelStaff record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    PlatformChannelStaff selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(PlatformChannelStaff record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(PlatformChannelStaff record);

    /**
     * 查询渠道人员统计
     *
     * @param params
     * @return
     */
    List<ChannelStaffVo> findParams(Map<String, Object> params);

    List<FinanceStatisticVO> findStatistic(Map<String, Object> params);

    List<Map<String, Object>> countStatistic();

    /**
     * 根据统计时间和渠道id查询
     *
     * @param param
     * @return
     */
    PlatformChannelStaff selectByReportTimeChannelId(Map<String, Object> param);
}