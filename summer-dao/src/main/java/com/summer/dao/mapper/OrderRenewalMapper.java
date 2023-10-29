package com.summer.dao.mapper;

import com.summer.dao.entity.OrderRenewal;
import com.summer.dao.entity.RepaymentInfo;
import com.summer.pojo.vo.FinanceStatisticVO;
import com.summer.pojo.vo.OrderRenewalVO;
import com.summer.pojo.vo.RenewalUserVO;

import java.util.List;
import java.util.Map;

public interface OrderRenewalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderRenewal record);

    int insertSelective(OrderRenewal record);

    OrderRenewal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderRenewal record);

    int updateByPrimaryKey(OrderRenewal record);

    List<RenewalUserVO> findParams(Map<String, Object> params);

    List<OrderRenewal> selectSimple(Map<String, Object> params);

    List<FinanceStatisticVO> findStatistic(Map<String, Object> params);

    List<Map<String, Object>> countStatistic();

    List<OrderRenewal> findByRepaymentId(Integer repaymentId);

    Map<String, Object> statisticByDay(Map<String, Object> map);

    Map<String, Object> countAll(Map<String, Object> params);

    void updatePaying(OrderRenewal record);

    void updateBatch(List<RepaymentInfo> repayments);

    /**
     * 统计总的续期费 统计规则 ；续期表中 old_repayment_time时间是今日时间，并且续期成功的订单总和
     *
     * @param firstRepaymentTime
     * @return
     */
    Long countRenewalFree(String firstRepaymentTime);

    /**
     * 统计当天总的续期手续费
     *
     * @param firstRepaymentTime
     * @return
     */
    Long countRenewalServiceFee(String firstRepaymentTime);

    /**
     * 续期成功数量
     *
     * @param firstRepaymentTime
     * @return
     */
    Integer countRenewalNum(String firstRepaymentTime);

    /**
     * 今日应还订单的续期应还金额
     */
    Long countRenewalExpireAmount();

    /***根据客户类型查询今日应还订单的续期*/
    List<OrderRenewalVO> countExpireRenewal(String firstRepaymentTime);

    OrderRenewal selectLatestByRepaymentId(Integer repaymentId);

    List<OrderRenewalVO> countNowExpireRenewal(String firstRepaymentTime);


}