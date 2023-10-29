/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.CollectionStatistics;
import com.summer.dao.entity.OrderCollectionCaller;
import com.summer.pojo.vo.OrderCollectionCallerVo;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface OrderCollectionCallerDAO {
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
    int insert(OrderCollectionCaller record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(OrderCollectionCaller record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    OrderCollectionCaller selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(OrderCollectionCaller record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(OrderCollectionCaller record);

    /**
     * 根据还款订单id查询当日催收人员催款列表是否已有,当日催收人员role_id=10
     *
     * @param repaymentId
     * @return
     */
    OrderCollectionCaller selectByRepaymentId(Integer repaymentId);

    int insertBatchSelective(List<OrderCollectionCaller> orders);

    List<OrderCollectionCallerVo> findParams(Map<String, Object> params);

    /**
     * 根据时间查询当日催收催回情况
     *
     * @param params
     * @return
     */
    List<CollectionStatistics> selectRepaymentByTime(Map<String, Object> params);

    int updateBatch(List<OrderCollectionCaller> lists);

    /**
     * 查询当日已分配的还款订单id
     */
    List<Integer> selectToday();

    /**
     * 根据时间查询当日催收催回的续期
     *
     * @param params
     * @return
     */
    List<CollectionStatistics> selectRenewalByTime(Map<String, Object> params);
}