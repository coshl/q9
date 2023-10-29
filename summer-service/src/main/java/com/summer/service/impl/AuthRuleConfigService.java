package com.summer.service.impl;

import com.summer.dao.entity.AuthRuleConfig;
import com.summer.dao.mapper.AuthRuleConfigDAO;
import com.summer.api.service.IAuthRuleConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认证规则配置的Service接口实现类
 */
@Service
public class AuthRuleConfigService implements IAuthRuleConfigService {
    @Resource
    private AuthRuleConfigDAO authRuleConfigDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return authRuleConfigDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AuthRuleConfig record) {
        return authRuleConfigDAO.insert(record);
    }

    @Override
    public int insertSelective(AuthRuleConfig record) {
        return authRuleConfigDAO.insertSelective(record);
    }

    @Override
    public AuthRuleConfig selectByPrimaryKey(Integer id) {
        return authRuleConfigDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AuthRuleConfig record) {
        return authRuleConfigDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AuthRuleConfig record) {
        return authRuleConfigDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<AuthRuleConfig> findAllAuthRule() {
        return authRuleConfigDAO.findAllAuthRule();
    }
}
