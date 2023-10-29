package com.summer.service.impl;

import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.dao.mapper.LoanRuleConfigDAO;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.dao.mapper.UserMoneyRateMapper;
import com.summer.pojo.dto.IncreaseMoneyConfigDto;
import com.summer.pojo.dto.LoanRuleConfigDto;
import com.summer.api.service.IUserMoneyRateService;
import com.summer.util.Constant;
import com.summer.util.DataTransform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户借款金额Service实现类
 */
@Slf4j
@Service
public class UserMoneyRateServiceImpl implements IUserMoneyRateService {

    @Resource
    private UserMoneyRateMapper userMoneyRateMapper;
    @Resource
    private LoanRuleConfigDAO loanRuleConfigDAO;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public int insert(UserMoneyRate userMoneyRate) {
        return userMoneyRateMapper.insert(userMoneyRate);
    }

    @Override
    public int update(UserMoneyRate userMoneyRate) {
        return userMoneyRateMapper.update(userMoneyRate);
    }

    @Override
    public UserMoneyRate findByUserId(Integer uid) {
        return userMoneyRateMapper.findByUserId(uid);
    }

    @Override
    public int deleteById(Integer id) {
        return userMoneyRateMapper.deleteById(id);
    }

    @Override
    @Async
    public void updateAmountAccrualByTimes(LoanRuleConfigDto loanRuleConfigDto) throws Exception {
        Date nowTime = new Date();
        List<IncreaseMoneyConfigDto> increaseMoneyConfigs = loanRuleConfigDto.getIncreaseMoneyConfigs();
        for (IncreaseMoneyConfigDto increaseMoneyConfigDto : increaseMoneyConfigs) {
            int increaseType = increaseMoneyConfigDto.getIncreaseType();
            /**按正常还款方式提额降息 */

            if (Constant.INCREASE_TYPE == increaseType) {
                /**确定提额方式未确定*/
                UserMoneyRate userMoneyRate = new UserMoneyRate();
                userMoneyRate.setMaxAmount(increaseMoneyConfigDto.getRepetitionInreaseMoney() * Constant.DOLLAR_CHANGE_PENNY);
                userMoneyRate.setAccrual(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getReduceInterest()));
                userMoneyRate.setServiceCharge(DataTransform.changeSmallDouble(loanRuleConfigDto.getServiceCharge()));
                userMoneyRate.setUpdateTime(nowTime);
                userMoneyRate.setNormalRepaymentTimes(increaseMoneyConfigDto.getAchieveTimes());
                userMoneyRateMapper.updateAmountAccrualByTimes(userMoneyRate);
                /**按逾期还款方式提额降息*/
            } else if (Constant.OVERDUE_TYPE == increaseType) {
                UserMoneyRate userMoneyRate = new UserMoneyRate();
                /**提额方式未确定*/
                userMoneyRate.setMaxAmount(increaseMoneyConfigDto.getRepetitionInreaseMoney() * Constant.DOLLAR_CHANGE_PENNY);
                userMoneyRate.setAccrual(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getReduceInterest()));
                userMoneyRate.setServiceCharge(DataTransform.changeSmallDouble(loanRuleConfigDto.getServiceCharge()));
                userMoneyRate.setUpdateTime(nowTime);
                userMoneyRate.setOverdueRepaymentTimes(increaseMoneyConfigDto.getAchieveTimes());
                userMoneyRateMapper.updateAmountAccrualByTimes(userMoneyRate);
                /**按复贷此时方式提额降息*/
            } else if (Constant.REPETIT_TYPE == increaseType) {
                int chieveTimes = increaseMoneyConfigDto.getAchieveTimes();
                //为0次的不修改
                if (chieveTimes != 0) {
                    UserMoneyRate userMoneyRate = new UserMoneyRate();
                    userMoneyRate.setMaxAmount(increaseMoneyConfigDto.getRepetitionInreaseMoney() * Constant.DOLLAR_CHANGE_PENNY);
                    userMoneyRate.setAccrual(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getReduceInterest()));
                    userMoneyRate.setServiceCharge(DataTransform.changeSmallDouble(loanRuleConfigDto.getServiceCharge()));
                    userMoneyRate.setUpdateTime(nowTime);
                    userMoneyRate.setRepetitionTimes(increaseMoneyConfigDto.getAchieveTimes());
                    userMoneyRateMapper.updateAmountAccrualByTimes(userMoneyRate);
                }
            }
        }

    }

    @Override
    public void updateMaxAmountAndServiceCharge(UserMoneyRate userMoneyRate, LoanRuleConfig loanRuleConfig) {
        //修改小于等于原来允许申请的金额那部分
        if (null != loanRuleConfig) {
            //主要针对新用户修改
            userMoneyRate.setBeforeMaxAmount(loanRuleConfig.getLoanAmount());
            userMoneyRateMapper.updateMaxAmountAndServiceCharge(userMoneyRate);
            //用于单独修改利息和服务费(包括了老用户)
            //  UserMoneyRate updateuserMoneyRate = new UserMoneyRate();
            //借款利息
            //  updateuserMoneyRate.setAccrual(userMoneyRate.getAccrual());
            //服务费
            //  updateuserMoneyRate.setServiceCharge(userMoneyRate.getServiceCharge());
            //更新时间
            //   updateuserMoneyRate.setUpdateTime(userMoneyRate.getUpdateTime());
            //   userMoneyRateMapper.updateMaxAmountAndServiceCharge(updateuserMoneyRate);
        }
    }

    @Override
    public int updateByUserId(HashMap<String, Object> params) {
        return userMoneyRateMapper.updateByUserId(params);
    }

    @Override
    public int saveUserRate(Integer userId, Integer channelId) {
        if (null != userId && null != channelId) {
            LoanRuleConfig loanRuleConfig = loanRuleConfigDAO.findByChannelId(channelId);
            if (null != loanRuleConfig) {
                UserMoneyRate userMoneyRate = new UserMoneyRate();
                userMoneyRate.setUserId(userId);
                //设置服务费
                userMoneyRate.setServiceCharge(loanRuleConfig.getServiceCharge());
                //设置最大借款金额
                userMoneyRate.setMaxAmount(loanRuleConfig.getLoanAmount());
                //设置借款利息
                userMoneyRate.setAccrual(loanRuleConfig.getBorrowInterest());
                // 插入user_money_rate
                return userMoneyRateMapper.insert(userMoneyRate);
            }
        }
        return 0;
    }

    @Async
    @Override
    public void updateMaxAmountByChannel(Integer nowAmount, Integer channelId) {
        List<Integer> channelIds = userInfoMapper.findByChannelId(channelId);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("nowAmount", nowAmount * Constant.CENT_CHANGE_DOLLAR);
        paramMap.put("channelIds", channelIds);
        log.info("修改却道金额的参数====paramMap" + paramMap);
        if (!CollectionUtils.isEmpty(channelIds)) {
            int status = userMoneyRateMapper.updateMaxAmountByChannel(paramMap);
            log.info("修改却道金额的,影响的行数------status={}", status);
        }
    }
}
