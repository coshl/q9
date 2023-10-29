package com.summer.service.impl;

import com.summer.dao.entity.IncreaseMoneyConfig;
import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.dao.mapper.LoanRuleConfigDAO;
import com.summer.api.service.ILoanRuleConfigService;
import com.summer.util.Constant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class LoanRuleConfigService implements ILoanRuleConfigService {
    @Resource
    private LoanRuleConfigDAO loanRuleConfigDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return loanRuleConfigDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(LoanRuleConfig record) {
        return loanRuleConfigDAO.insert(record);
    }

    @Override
    public int insertSelective(LoanRuleConfig record) {
        return loanRuleConfigDAO.insertSelective(record);
    }

    @Override
    public LoanRuleConfig selectByPrimaryKey(Integer id) {
        return loanRuleConfigDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(LoanRuleConfig record) {
        loanRuleConfigDAO.updateLoanAmount(record);
        return loanRuleConfigDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(LoanRuleConfig record) {
        return loanRuleConfigDAO.updateByPrimaryKey(record);
    }

    @Override
    public LoanRuleConfig findLoanConfigByParams(Map<String, Object> map) {
        LoanRuleConfig loanConfig = loanRuleConfigDAO.findLoanConfigByParams(map);
        return loanConfig;
    }

    @Override
    public LoanRuleConfig findByChannelId(Integer channelId) {
        LoanRuleConfig byChannelId = loanRuleConfigDAO.findByChannelId(channelId);
        /**如果按用户的渠道id未查询到对应的费率，就查询渠道id=0的全局的费率配置**/
        if (null != byChannelId) {
            return byChannelId;
        } else {
            return loanRuleConfigDAO.findByChannelId(Constant.CHANNELRULE_ID);
        }
    }

    @Override
    public LoanRuleConfig findByUserId(Integer userId) {
        LoanRuleConfig byChannelId = loanRuleConfigDAO.findByUserId(userId);
        /**如果按用户的id未查询到对应的费率，就查询渠道id=0的全局的费率配置**/
        if (null != byChannelId) {
            return byChannelId;
        } else {
            return loanRuleConfigDAO.findByChannelId(Constant.CHANNELRULE_ID);
        }
    }

	@Override
	public UserMoneyRate findUserMoneyInfo(Integer userId) {
		return loanRuleConfigDAO.findUserMoneyInfo(userId);
	}

	@Override
	public IncreaseMoneyConfig findIncreaseMoneyConfig(Integer achieveTimes) {
		return loanRuleConfigDAO.findIncreaseMoneyConfig(achieveTimes);
	}

	@Override
	public Integer findIncreaseMoneyConfigCount() {
		return loanRuleConfigDAO.findIncreaseMoneyConfigCount();
	}

	@Override
	public Integer findMinLoanMoney(Integer userId) {
		return loanRuleConfigDAO.findMinLoanMoney(userId);
	}

	@Override
	public Integer findMaxLoanMoney(Integer userId) {
		return loanRuleConfigDAO.findMaxLoanMoney(userId);
	}

	@Override
	public Integer findOrderStatus(Integer userId) {
		return loanRuleConfigDAO.findOrderStatus(userId);
	}
}
