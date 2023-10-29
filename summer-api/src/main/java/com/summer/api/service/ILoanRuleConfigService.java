package com.summer.api.service;

import com.summer.dao.entity.IncreaseMoneyConfig;
import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.UserMoneyRate;

import java.util.Map;

/**
 * 借款规则配置Service接口
 */
public interface ILoanRuleConfigService {
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
    int insert(LoanRuleConfig record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(LoanRuleConfig record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    LoanRuleConfig selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(LoanRuleConfig record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(LoanRuleConfig record);

    /**
     * 根据条件查询借款规则
     *
     * @param map
     * @return
     */
    LoanRuleConfig findLoanConfigByParams(Map<String, Object> map);

    /**
     * 根据渠道id查询贷款规则
     *
     * @param channelId
     * @return
     */
    LoanRuleConfig findByChannelId(Integer channelId);

    /**
     * 根据用户id查询最低借款金额
     * @param userId
     * @return
     */
    LoanRuleConfig findByUserId(Integer userId);
    
    /**
     * 根据用户id查询用户复贷数跟最大借款金额
     * @param userId
     * @return
     */
    UserMoneyRate findUserMoneyInfo(Integer userId);
    
    /**
     * 根据复贷次数查询复贷金额
     * @return
     */
    IncreaseMoneyConfig findIncreaseMoneyConfig(Integer achieveTimes);
    
    /**
     * 根据复贷等级总数
     * @return
     */
    Integer findIncreaseMoneyConfigCount();
    
    /**
     * 查询用户最低可借金额
     * @param userId
     * @return
     */
    Integer findMinLoanMoney(Integer userId);
    
    /**
     * 查询用户最高可借金额
     * @param userId
     * @return
     */
    Integer findMaxLoanMoney(Integer userId);
    
    /**
     * 查询用户订单状态（是否已还款）
     * @param userId
     * @return
     */
    Integer findOrderStatus(Integer userId);
}
