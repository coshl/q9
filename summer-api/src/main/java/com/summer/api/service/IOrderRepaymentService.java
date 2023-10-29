package com.summer.api.service;

import com.summer.dao.entity.*;
import com.summer.pojo.vo.PaymentUserVO;
import com.summer.pojo.vo.RepaymentRecoredVo;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2019/1/3
 */
public interface IOrderRepaymentService {
    /**
     * 线下还款
     *
     * @param orderRepayment
     * @param payTimeObj
     * @param remark
     * @param thirdOrderNo
     * @param payTypeObj
     * @param paidAmount
     */
    void payOffline(OrderRepayment orderRepayment, Object payTimeObj, String remark, String thirdOrderNo,
                    Object payTypeObj, int paidAmount, Integer backId);

    void insertByBorrorOrder(OrderBorrow borrowOrder);

    boolean repay(OrderRepayment re, OrderRepaymentDetail detail);


    /**
     * 续期
     *
     * @param repayment
     * @param record
     * @return
     */
    void renewal(OrderRepayment repayment, OrderRenewal record);

    boolean insertByBorrorOrder2(OrderBorrow borrowOrder);

    /**
     * 根据参数查询还款订单
     *
     * @param params
     * @return
     */
    List<OrderRepayment> findOrderPrepaymentByParam(Map<String, Object> params);

    /**
     * 查询APP还款记录
     *
     * @param userId
     * @return
     */
    List<RepaymentRecoredVo> findOrderRecordByParam(Integer userId, int type);

    /**
     * 根据还款查询APP还款记录详情
     *
     * @param
     * @return
     */
    OrderRepayment findOrderRecordDetailById(Integer orderId);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    OrderRepayment selectByPrimaryKey(Integer id);

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
     * 更新逾期信息
     *
     * @param repayment
     */
    void overdue(OrderRepayment repayment, LoanRuleConfig loanConfig);

    /**
     * 提额
     *
     * @param re
     */
    void upLimit(OrderRepayment re);


    /**
     * 根据还款时间查询逾期还款
     *
     * @param params
     * @return
     */
    List<OrderRepayment> selectRepaymentByPaidTime(Map<String, Object> params);

    List<PaymentUserVO> findParams(Map<String, Object> params);

    OrderRepayment selectByBorrowId(Integer borrowId);
    
    /**
     * 修改用户服务费
     * @param userMoneyRate
     * @return
     */
    int updateServiceCharge(UserMoneyRate userMoneyRate);
}
