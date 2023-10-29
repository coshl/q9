package com.summer.dao.mapper;

import com.summer.dao.entity.OrderRepayment;
import com.summer.dao.entity.UserBlackList;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.pojo.vo.CountLoanByUserTypeVO;
import com.summer.pojo.vo.PaymentUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderRepaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRepayment record);

    int insertSelective(OrderRepayment record);

    OrderRepayment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRepayment record);

    int updateByPrimaryKey(OrderRepayment record);

    List<PaymentUserVO> findParams(Map<String, Object> params);

    /**
     * 根据参数查询还款订单
     *
     * @param params
     * @return
     */
    List<OrderRepayment> findOrderPrepaymentByParam(Map<String, Object> params);

    List<OrderRepayment> selectSimple(Map<String, Object> params);

    List<OrderRepayment> findToPay(Map<String, Object> params);

    /**
     * 根据用户id查询待还款记录
     *
     * @param userId
     * @return
     */
    List<OrderRepayment> findStayRepayment(Integer userId);

    /**
     * 根据用户id查询已还款订单
     *
     * @param userId
     * @return
     */
    List<OrderRepayment> findRepayment(Integer userId);

    /**
     * 查询待还款总人数
     */
    List<Map<String, Object>> findStayRepaymentCount(Map<String, Object> map);

    /**
     * 查询老用户待还款总人数
     */
    List<Map<String, Object>> findStayOldRepaymentCount(Map<String, Object> map);

    /**
     * 查询新用户待还款总人数
     */
    List<Map<String, Object>> findStayNewRepaymentCount(Map<String, Object> map);

    /**
     * 逾期总数量
     */
    List<Map<String, Object>> findOverdueRepaymentCount(Map<String, Object> map);

    /**
     * 老用户逾期总数量
     */
    List<Map<String, Object>> findOldOverdueRepaymentCount(Map<String, Object> map);

    /**
     * 新用户逾期总数量
     */
    List<Map<String, Object>> findNewOverdueRepaymentCount(Map<String, Object> map);

    /**
     * 当日已还统计
     */
    List<Map<String, Object>> findRepaymentCount(Map<String, Object> map);

    Map<String, Object> statisticByDay(Map<String, Object> map);

    Map<String, Object> countByDay(Integer number);

    Map<String, Object> countAll(Map<String, Object> params);

    Long countToday(Map<String, Object> params);

    /**
     * 异步统计放款量
     */
    Integer findLoanSuccCount(Map<String, Object> map);

    /**
     * 逾期总数量
     */
    List<Map<String, Object>> findTotalOverdueCount(Map<String, Object> map);

    /**
     * 老用户逾期总数量
     */
    List<Map<String, Object>> findOldTotalOverdueCount(Map<String, Object> map);

    /**
     * 新用户逾期总数量
     */
    List<Map<String, Object>> findNewTotalOverdueCount(Map<String, Object> map);

    List<CountLoanByUserTypeVO> countUserByDay(Integer number);

    /**
     * 新用户还款数量
     */
    List<Map<String, Object>> findNewRepaymentCount(Map<String, Object> map);

    /**
     * 老用户还款数量
     */
    List<Map<String, Object>> findOldRepaymentCount(Map<String, Object> map);


    /**
     * 续期总数量
     */
    List<Map<String, Object>> findRenewalCount(Map<String, Object> map);

    /**
     * 新用户续期数量，在新用户统计已还款时，要加上所有新用户续期的
     */
    List<Map<String, Object>> findNewRenewalCount(Map<String, Object> map);

    /**
     * 老用户续期数量, 在老用户统计已还款时，要加上所有老用户续期的
     */
    List<Map<String, Object>> findOldRenewalCount(Map<String, Object> map);

    /**
     * 今日续期数量 （续期创建时间是今日的）
     */
    List<Map<String, Object>> findTodayRenewalCount(Map<String, Object> map);

    /**
     * 今日续期数量 （续期创建时间是今日的）
     */
    List<Map<String, Object>> findNewTodayRenewalCount(Map<String, Object> map);

    /**
     * 今日续期数量 （续期创建时间是今日的）
     */
    List<Map<String, Object>> findOldTodayRenewalCount(Map<String, Object> map);

    /**
     * 提前续期续期数量（续期创建时间不是在今天的） 在计算总待还时，就要加上提前续期的。还款时间是今日，但是有在今日续期的不用加，因为他的还款时间在今日，在根据今日还款时间统计的时候已经统计过了
     */
    List<Map<String, Object>> findAheadRenewalCount(Map<String, Object> map);

    /**
     * 新提前续期续期数量
     */
    List<Map<String, Object>> findNewAheadRenewalCount(Map<String, Object> map);

    /**
     * 老提前续期续期数量
     */
    List<Map<String, Object>> findOldAheadRenewalCount(Map<String, Object> map);

    /**
     * 查询逾期次数
     *
     * @param userId
     * @return
     */
    Integer selectOverdueNum(Integer userId);

    /**
     * 查询最大逾期天数
     *
     * @param userId
     * @return
     */
    Integer selectOverdueMax(Integer userId);

    /**
     * 查询逾期用户
     *
     * @param overdue
     * @return
     */
    List<UserBlackList> selectByOverdue(Integer overdue);

    /**
     * 根据还款时间查询逾期还款
     *
     * @param params
     * @return
     */
    List<OrderRepayment> selectRepaymentByPaidTime(Map<String, Object> params);

    /**
     * 根据借款订单id查询还款订单
     *
     * @param borrowId
     * @return
     */
    OrderRepayment selectByBorrowId(@Param("borrowId") Integer borrowId);

    /**
     * 根据逾期天数查询订单
     * @param lateDay
     * @return
     */
    List<Map<String, String>> selectByLateDay(@Param("lateDay") Integer lateDay);

    /**
     * 查询今日到期订单
     * @return
     */
    List<Map<String, String>> selectTodayOrder();

    /**
     * 修改用户服务费
     * @param userMoneyRate
     * @return
     */
    int updateServiceCharge(UserMoneyRate userMoneyRate);

    /**
     * 修改用户还款时间
     * @param record
     * @return
     */
    int updateRepaymentDate(OrderRepayment record);

    int orderId(Integer repaymentId);
}
