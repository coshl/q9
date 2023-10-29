package com.summer.service.impl;

import com.summer.dao.entity.UserShortMessage;
import com.summer.dao.mapper.UserShortMessageDAO;
import com.summer.api.service.IUserShortMessageService;
import com.summer.pojo.vo.UserShortMessageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户短信Service接口实现类
 */
@Service
public class UserShortMessageService implements IUserShortMessageService {
    @Resource
    private UserShortMessageDAO userShortMessageDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userShortMessageDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserShortMessage record) {
        return userShortMessageDAO.insert(record);
    }

    @Override
    public int insertSelective(UserShortMessage record) {
        return userShortMessageDAO.insertSelective(record);
    }

    @Override
    public UserShortMessage selectByPrimaryKey(Integer id) {
        return userShortMessageDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserShortMessage record) {
        return userShortMessageDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserShortMessage record) {
        return userShortMessageDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<UserShortMessage> findByUserId(Integer userId) {
        return userShortMessageDAO.findByUserId(userId);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return userShortMessageDAO.deleteByUserId(userId);
    }

    @Override
    public int insertBatchSelective(List<UserShortMessage> item) {
        return userShortMessageDAO.insertBatchSelective(item);
    }

    @Override
    public List<UserShortMessageVo> selectByPhone(Map<String, Object> params) {
        return userShortMessageDAO.selectByPhone(params);
    }
}
