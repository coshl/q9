/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.CollectionStatistics;
import com.summer.dao.entity.CountCollectionAssessment;
import com.summer.dao.entity.OrderCollection;
import com.summer.pojo.vo.CollectionUserVO;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface OrderCollectionDAO {
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
    int insert(OrderCollection record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(OrderCollection record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    OrderCollection selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(OrderCollection record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(OrderCollection record);

    List<CollectionUserVO> findParams(Map<String, Object> params);

    List<Map<String, Object>> countOverdue();

    List<OrderCollection> selectSimple(Map<String, Object> map);

    List<OrderCollection> selectByIds(String[] split);

    List<CountCollectionAssessment> statisticByDay(Map<String, Object> map);

    /**
     * 根据借款订单，还款订单id，查询
     *
     * @param orderCollection
     * @return
     */
    OrderCollection findByCollectionRepaymentId(OrderCollection orderCollection);

    /**
     * 根据时间查询逾期催收催回情况
     *
     * @param params
     * @return
     */
    List<CollectionStatistics> selectRepaymentByTime(Map<String, Object> params);

    List<CollectionStatistics> selectByTime(Map<String, Object> map);
}