package com.summer.service.impl;

import com.summer.dao.entity.UserContacts;
import com.summer.dao.mapper.UserContactsDAO;
import com.summer.api.service.IUserContactsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户通讯录Service接口实现类
 */
@Service
public class UserContactsService implements IUserContactsService {
    @Resource
    private UserContactsDAO userContactsDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userContactsDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserContacts record) {
        return userContactsDAO.insert(record);
    }

    @Override
    public int insertSelective(UserContacts record) {
        return userContactsDAO.insertSelective(record);
    }

    @Override
    public UserContacts selectByPrimaryKey(Integer id) {
        return userContactsDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserContacts record) {
        record.setCreateTime(new Date());
        return userContactsDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserContacts record) {
        return userContactsDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<UserContacts> findContatsByUserId(Integer userId) {
        return userContactsDAO.findContatsByUserId(userId);
    }

    @Override
    public List<UserContacts> selectUserContacts(Map<String, Object> map) {
        return userContactsDAO.selectUserContacts(map);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return userContactsDAO.deleteByUserId(userId);
    }

    @Override
    public int insertBatchSelective(List<UserContacts> item) {
        return userContactsDAO.insertBatchSelective(item);
    }
}
