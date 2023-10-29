package com.summer.dao.mapper;

import com.summer.dao.entity.OrderBorrow;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.RiskCreditUser;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.pojo.dto.OrderPushRiskDTO;
import com.summer.pojo.vo.BorrowPhoneListVO;
import com.summer.pojo.vo.BorrowUserVO;
import com.summer.pojo.vo.FinanceStatisticVO;
import com.summer.pojo.vo.OrderBorrowVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderBorrowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderBorrow record);

    void insertSelective(OrderBorrow record);

    OrderBorrow selectByPrimaryKey(Integer id);

    OrderBorrow selectOneLoan();

    int updateByPrimaryKeySelective(OrderBorrow record);

    int updateByPrimaryKey(OrderBorrow record);

    List<BorrowUserVO> findParams(Map<String, Object> params);

    List<OrderBorrow> selectAll();

    OrderBorrow selectByUserId(Integer user_id);

    /**
     * 获取渠道名
     *
     * @param id
     * @return
     */
    PlateformChannel selectChannelById(Integer id);

    /**
     * 根据参数查询借款订单
     *
     * @param param
     * @return
     */
    List<OrderBorrow> selectByParams(Map<String, Object> param);


    /**
     * 根据参数简单查询
     *
     * @param params
     * @return
     */
    List<OrderBorrow> selectSimple(Map<String, Object> params);

    List<OrderBorrow> selectRisk();

    /**
     * 借款对账
     *
     * @param params
     * @return
     */
    List<FinanceStatisticVO> findStatistic(Map<String, Object> params);

    List<Map<String, Object>> countStatistic();

    int insertRiskUser(RiskCreditUser risk);

    /**
     * 获取所有借款手机号码(去重)
     */
    List<BorrowPhoneListVO> findPhone();

    Long statisticByDay(Integer number);

    List<Map<String, Object>> selectRecord(Map<String, Object> map);

    /**
     * 查询最新的订单
     *
     * @param userId
     * @return
     */
    OrderBorrow recentlyOrder(Integer userId);

    /**
     * 根据用户id，和客户端类型查询该用户借款订单
     *
     * @param orderBorrow
     * @return
     */
    List<OrderBorrow> findByUserIdCustermType(OrderBorrow orderBorrow);

    /**
     * 根据用户id和订单id查询
     *
     * @param orderPushRiskDTO
     * @return
     */
    OrderBorrow findByUserIdOrderId(OrderPushRiskDTO orderPushRiskDTO);

    /**
     * 根据用户id，和客户端类型查询该用户借款订单
     *
     * @param orderBorrow
     * @return
     */
    List<OrderBorrow> findByUserOrderParam(OrderBorrow orderBorrow);

    Map<String, Object> countAll(Map<String, Object> params);

    /**
     * 查询被拒绝过的订单
     *
     * @param orderBorrow
     * @return
     */
    int selectRefuseedOrder(OrderBorrow orderBorrow);

    List<OrderBorrowVo> findByIdCard(String idCard);

    /**
     * 查询未分配给审核人员的待人工审核订单
     *
     * @return
     */
    List<OrderBorrow> findPendingReview();

    /**
     * 客户待审核订单批量分配
     *
     * @param borrows
     */
    void updateBatch(List<OrderBorrow> borrows);

    /**
     * 根据逾期状态查询
     *
     * @param userId
     * @return
     */
    List<OrderBorrow> findByOverdueStatus(Integer userId);

    OrderBorrow selectBystatus(Integer status);

    void updateLoan(String[] id);

    void updateLoanFail(String[] id);

    List<OrderBorrow> selectForLoan(String[] id);

    List<OrderBorrow> checkLoan(Map<String, Object> params);

    List<BorrowUserVO> orderList(Map<String, Object> params);

    //    int cancelLoan(Integer id);
    int cancelLoan(Map<String, Object> map);

    OrderBorrow findByFlowNo(String outTradeNo);

    List<OrderBorrow> selectCancel(String[] id);

    Integer findOrderCount(Map<String, Object> param);

    //统计正常还款，逾期还款的订单数
    Integer findNormalRepay(Integer userId);

    /**
     * 查询用户第一笔订单
     *
     * @param userId
     * @return
     */
    OrderBorrow findByFirstOrder(Integer userId);

    /**
     * 更新借款申请为机审通过
     *
     * @param loanReviewRemark 机审备注<br>
     * @param id               借款申请主键ID<br>
     * @param loanReviewUserId 放款审核人userId
     * @param status           状态：
     *                         -1:取消放款;
     *                         0:待初审(待机审);
     *                         1:初审驳回;
     *                         2:初审通过;
     *                         3:待人工审核
     *                         4:复审机审驳回
     *                         5:复审人审拒绝;
     *                         6:放款中;
     *                         7:放款失败;
     *                         8:已放款，还款中;
     *                         9:部分还款;
     *                         10:已还款;
     *                         11:已逾期;
     *                         12:已坏账，
     *                         13逾期已还款；
     * @return
     */
    int updateAssetsSuc(@Param("id") Integer id, @Param("loanReviewUserId") Integer loanReviewUserId, @Param("status") Integer status, @Param("creditLevel") Integer creditLevel, @Param("loanReviewRemark") String loanReviewRemark);
    
    
    /**
     * 修改用户申请金额
     * @param record
     * @return
     */
    int updateApplicationAmount(OrderBorrow record);
    
    
    /**
     * 查询用户服务费跟利率
     * @param userId
     * @return
     */
    UserMoneyRate findUsersRate(Integer userId);
}