package com.summer.dao.mapper;


import com.summer.dao.entity.UserMoneyRate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户借款金额评估Mapper接口
 */

public interface UserMoneyRateMapper {
    /**
     * 添加
     *
     * @param
     * @return
     */
    public int insert(UserMoneyRate userMoneyRate);

    /**
     * 更新
     *
     * @param
     * @return
     */
    public int update(UserMoneyRate userMoneyRate);

    /**
     * 查询
     *
     * @param
     * @return
     */
    public UserMoneyRate findByUserId(Integer uid);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据正常还款/逾期还款/复贷次数，更新借款利率
     *
     * @param userMoneyRate
     */
    int updateAmountAccrualByTimes(UserMoneyRate userMoneyRate);

    /**
     * 根据参数查询
     *
     * @param param
     * @return
     */
    List<UserMoneyRate> findByParam(Map<String, Object> param);

    /**
     * 更新最大金额，服务费，借款利息
     *
     * @param userMoneyRate
     * @return
     */
    int updateMaxAmountAndServiceCharge(UserMoneyRate userMoneyRate);

    /**
     * 根据用户ID修改用户借款信息
     *
     * @param params
     * @return
     */
    int updateByUserId(HashMap<String, Object> params);

    /**
     * 更新最大金额，服务费，借款利息
     *
     * @param map
     * @return
     */
    int updateMaxAmountByChannel(Map<String, Object> map);
}
