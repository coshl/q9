/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.OrderRepaymentDetail;
import com.summer.pojo.vo.FinanceStatisticVO;
import com.summer.pojo.vo.PaymentDetailUserVO;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface OrderRepaymentDetailDAO {
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
    int insert(OrderRepaymentDetail record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(OrderRepaymentDetail record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    OrderRepaymentDetail selectByPrimaryKey(Long id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(OrderRepaymentDetail record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(OrderRepaymentDetail record);

    List<PaymentDetailUserVO> findParams(Map<String, Object> params);

    List<OrderRepaymentDetail> selectSimple(Map<String, Object> params);

    List<FinanceStatisticVO> findStatistic(Map<String, Object> params);

    List<Map<String, Object>> countStatistic();

    void updatePaying(OrderRepaymentDetail orderRepaymentDetail);

    OrderRepaymentDetail selectLatestByRepaymentId(Integer repaymentId);

    OrderRepaymentDetail selectLatestByRepay(Map<String, Object> params);

    OrderRepaymentDetail selectLatestByRefund(Map<String, Object> params);
}