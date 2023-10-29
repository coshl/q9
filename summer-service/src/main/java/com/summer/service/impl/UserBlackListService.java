package com.summer.service.impl;

import com.summer.dao.entity.UserBlackList;
import com.summer.dao.mapper.UserBlackListDAO;
import com.summer.api.service.IUserBlackListService;
import com.summer.pojo.vo.UserBlackListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserBlackListService implements IUserBlackListService {

    @Resource
    private UserBlackListDAO userBlackListDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userBlackListDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserBlackList record) {
        return userBlackListDAO.insert(record);
    }

    @Override
    public int insertSelective(UserBlackList record) {
        return userBlackListDAO.insertSelective(record);
    }

    @Override
    public UserBlackList selectByPrimaryKey(Integer id) {
        return userBlackListDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserBlackList record) {
        return userBlackListDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserBlackList record) {
        return userBlackListDAO.updateByPrimaryKey(record);
    }

    @Override
    public UserBlackList findByPhone(String phone) {
        return userBlackListDAO.findByPhone(phone);
    }

    @Override
    public List<UserBlackListVo> findByParam(Map<String, Object> param) {
        return userBlackListDAO.findByParam(param);
    }
}
