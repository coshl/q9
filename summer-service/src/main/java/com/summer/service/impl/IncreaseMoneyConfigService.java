package com.summer.service.impl;

import com.summer.dao.entity.IncreaseMoneyConfig;
import com.summer.dao.mapper.IncreaseMoneyConfigDAO;
import com.summer.api.service.IIncreaseMoneyConfigService;
import com.summer.pojo.vo.IncreaseMoneyConfigVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提额降息配置接口的实现类
 */
@Service
public class IncreaseMoneyConfigService implements IIncreaseMoneyConfigService {
    @Resource
    private IncreaseMoneyConfigDAO increaseMoneyConfigDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return increaseMoneyConfigDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(IncreaseMoneyConfig record) {
        return increaseMoneyConfigDAO.insert(record);
    }

    @Override
    public int insertSelective(IncreaseMoneyConfig record) {
        return increaseMoneyConfigDAO.insertSelective(record);
    }

    @Override
    public IncreaseMoneyConfig selectByPrimaryKey(Integer id) {
        return increaseMoneyConfigDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(IncreaseMoneyConfig record) {
        return increaseMoneyConfigDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(IncreaseMoneyConfig record) {
        return increaseMoneyConfigDAO.updateByPrimaryKey(record);
    }


    @Override
    public List<IncreaseMoneyConfigVo> findAllIncreaseConfig(Byte type) {
        return increaseMoneyConfigDAO.findAllIncrease(type);
    }

    @Override
    public int updateByStatus(Integer status) {
        return increaseMoneyConfigDAO.updateByStatus(status);
    }
}
