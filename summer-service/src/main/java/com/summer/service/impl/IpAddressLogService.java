package com.summer.service.impl;

import com.summer.dao.entity.IpAddressLog;
import com.summer.dao.entity.IpAddressLogQuery;
import com.summer.dao.entity.dailystatics.DailyStatisticsChannelProduct;
import com.summer.dao.mapper.IpAddressLogDAO;
import com.summer.api.service.IIpAddressLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IpAddressLogService implements IIpAddressLogService {
    @Resource
    private IpAddressLogDAO ipAddressLogDAO;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ipAddressLogDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(IpAddressLog record) {
        return ipAddressLogDAO.insert(record);
    }

    @Override
    public int insertSelective(IpAddressLog record) {
        return ipAddressLogDAO.insertSelective(record);
    }

    @Override
    public IpAddressLog selectByPrimaryKey(Long id) {
        return ipAddressLogDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(IpAddressLog record) {
        return ipAddressLogDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(IpAddressLog record) {
        return ipAddressLogDAO.updateByPrimaryKey(record);
    }

    @Override
    public DailyStatisticsChannelProduct getPvUv(IpAddressLogQuery query) {
        return ipAddressLogDAO.getPvUv(query);
    }
}
