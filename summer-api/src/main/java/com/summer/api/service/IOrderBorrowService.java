package com.summer.api.service;


import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.OrderBorrow;
import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.dto.OrderPushRiskDTO;
import com.summer.pojo.vo.BorrowUserVO;
import com.summer.pojo.vo.OrderBorrowVo;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
public interface IOrderBorrowService {

    /**
     * 修改订单
     *
     * @param orderBorrow
     */
    int updateSelective(OrderBorrow orderBorrow);

    int cancelLoan(String[] ids);

    /**
     * 审核订单列表
     *
     * @param params
     * @return
     */
    PageInfo<BorrowUserVO> queryWithUser(Map<String, Object> params);

    /**
     * 动态插入新借款订单
     *
     * @param record
     * @return
     */
    int insertSelective(OrderBorrow record);

    /**
     * 通过主键查找借款订单
     *
     * @param id
     * @return
     */
    OrderBorrow selectByPrimaryKey(Integer id);

    /**
     * app端根据参数查询借款订单的方法
     *
     * @param param
     * @return
     */
    List<OrderBorrow> selectByParams(Map<String, Object> param);

    void autoLoan(String borrowIdStr);

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

    List<OrderBorrowVo> findByIdCard(String idCard);

    void reviewDispatch(List<OrderBorrow> borrows, List<PlatformUser> callers);

    /**
     * 根据逾期状态查询
     *
     * @param userId
     * @return
     */
    List<OrderBorrow> findByOverdueStatus(Integer userId);

    /**
     * 根据用户ID查询订单
     * @param user_id
     * @return
     */
     OrderBorrow selectByUserId(Integer user_id);
}
