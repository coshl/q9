package com.summer.api.service;


import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.pojo.dto.LoanRuleConfigDto;

import java.util.HashMap;

/**
 * 用户借款金额评估Service接口
 */

public interface IUserMoneyRateService {
    /**
     * 添加新的用户借款金额评估
     *
     * @param
     * @return
     */
    int insert(UserMoneyRate userMoneyRate);

    /**
     * 更新用户借款金额评估
     *
     * @param
     * @return
     */
    int update(UserMoneyRate userMoneyRate);


    /**
     * 根据id查询用户借款
     *
     * @param
     * @return
     */
    UserMoneyRate findByUserId(Integer uid);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 根据正常/逾期还款次数更新用户借款费率
     *
     * @param
     * @return
     */
    void updateAmountAccrualByTimes(LoanRuleConfigDto loanRuleConfigDto) throws Exception;

    /**
     * 更新最大金额，服务费，借款利息
     *
     * @param userMoneyRate
     * @param loanRuleConfig
     * @return
     */
    void updateMaxAmountAndServiceCharge(UserMoneyRate userMoneyRate, LoanRuleConfig loanRuleConfig);

    /**
     * 根据用户ID修改用户借款信息
     *
     * @param params
     * @return
     */
    int updateByUserId(HashMap<String, Object> params);


    /**
     * 生成用户费率表
     *
     * @return
     */
    int saveUserRate(Integer userId, Integer channelId);

    /**
     * 通过渠道id更新最大金额，
     *
     * @param nowAmount
     * @param channelId
     */
    void updateMaxAmountByChannel(Integer nowAmount, Integer channelId);
}
