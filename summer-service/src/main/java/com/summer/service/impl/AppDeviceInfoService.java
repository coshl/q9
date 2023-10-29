package com.summer.service.impl;

import com.summer.dao.entity.PhoneDeviceInfo;
import com.summer.dao.mapper.PhoneDeviceInfoDAO;
import com.summer.api.service.IAppDeviceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ls
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2019/5/27 18:01
 */
@Service
public class AppDeviceInfoService implements IAppDeviceInfoService {
    @Resource
    private PhoneDeviceInfoDAO phoneDeviceInfoDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return phoneDeviceInfoDAO.deleteByPrimaryKey(id);
    }


    @Override
    public int insert(PhoneDeviceInfo record) {
        return phoneDeviceInfoDAO.insert(record);
    }

    @Override
    public int insertSelective(PhoneDeviceInfo record) {
        return phoneDeviceInfoDAO.insertSelective(record);
    }

    @Override
    public PhoneDeviceInfo selectByPrimaryKey(Integer id) {
        return phoneDeviceInfoDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PhoneDeviceInfo record) {
        return phoneDeviceInfoDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PhoneDeviceInfo record) {
        return phoneDeviceInfoDAO.updateByPrimaryKey(record);
    }
}
