package com.summer.service.impl;

import com.summer.dao.entity.UserAppSoftware;
import com.summer.dao.mapper.UserAppSoftwareDAO;
import com.summer.api.service.IUserAppSoftwareService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户APP应用Service接口
 */
@Service
public class UserAppSoftwareService implements IUserAppSoftwareService {
    @Resource
    private UserAppSoftwareDAO userAppSoftwareDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userAppSoftwareDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserAppSoftware record) {
        return userAppSoftwareDAO.insert(record);
    }

    @Override
    public int insertSelective(UserAppSoftware record) {
        return userAppSoftwareDAO.insertSelective(record);
    }

    @Override
    public UserAppSoftware selectByPrimaryKey(Integer id) {
        return userAppSoftwareDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserAppSoftware record) {
        return userAppSoftwareDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserAppSoftware record) {
        return userAppSoftwareDAO.updateByPrimaryKey(record);
    }

    @Override
    public int insertBatchSelective(List<UserAppSoftware> item) {
        return userAppSoftwareDAO.insertBatchSelective(item);
    }

    @Override
    public List<UserAppSoftware> findByUserId(Integer userId) {
        return userAppSoftwareDAO.findByUserId(userId);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return userAppSoftwareDAO.deleteByUserId(userId);
    }
}
