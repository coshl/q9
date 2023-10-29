/* https://github.com/12641561 */
package com.summer.dao.mapper.dailystatistics;

import com.summer.dao.entity.dailystatics.DailyStatisticsChannelLend;
import com.summer.pojo.vo.ChannelLendTotalVO;
import com.summer.pojo.vo.DailyStatisticsChannelLendVo;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface DailyStatisticsChannelLendDAO {
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
    int insert(DailyStatisticsChannelLend record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(DailyStatisticsChannelLend record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    DailyStatisticsChannelLend selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(DailyStatisticsChannelLend record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(DailyStatisticsChannelLend record);

    /**
     * 渠道还款统计
     *
     * @param params
     * @return
     */
    List<DailyStatisticsChannelLendVo> findParams(Map<String, Object> params);

    /**
     * 批量更新
     *
     * @param dailyStatisticsChannelLend
     * @return
     */
    int updateBatchByPrimaryKeySelective(List<DailyStatisticsChannelLend> dailyStatisticsChannelLend);

    /**
     * 批量插入
     *
     * @param dailyStatisticsChannelLend
     * @return
     */
    int insertBatchSelective(List<DailyStatisticsChannelLend> dailyStatisticsChannelLend);

    /**
     * 根据还款时间和渠道id查询
     *
     * @param map
     * @return
     */
    DailyStatisticsChannelLend selectByParams(Map<String, Object> map);

    /**
     * 用户待还笔数
     *
     * @return
     */
    ChannelLendTotalVO findStayTotal();

    /**
     * 用户逾期总笔数
     *
     * @return
     */
    int findOverdueTotal();

    /**
     * 新用户逾期笔数
     *
     * @return
     */
    int findNewOverdueTotal();

    /**
     * 老用户逾期笔数
     *
     * @return
     */
    int findOldOverdueTotal();

    /**
     * 总待还
     *
     * @return
     */
    int findAllStayCount();

    /**
     * 老用户总待还
     *
     * @return
     */
    int findAllStayCountOld();

    /**
     * 新用户总待还
     *
     * @return
     */
    int findAllStayCountNew();

    /**
     * 查询所有还款统计的
     *
     * @param map
     * @return
     */
    List<DailyStatisticsChannelLend> selectAllByParams(Map<String, Object> map);
}