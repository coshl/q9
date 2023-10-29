package com.summer.service.impl;

import com.summer.dao.entity.CollectedUser;
import com.summer.dao.mapper.CollectedUserDAO;
import com.summer.api.service.CollectedUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 逾期用户
 */
@Service
public class CollectedUserServiceImpl implements CollectedUserService {

    @Resource
    private CollectedUserDAO collectedUserDAO;

    @Override
    public int insertOrUpdateCollectedUser(Map<String, Object> params) {
        String phone = (String) params.get("phone");
        CollectedUser collectedUser = collectedUserDAO.selectByUserPhone(params);
        int result = 0;
        if (collectedUser == null || collectedUser.getId() == null) {
            collectedUser = new CollectedUser();
            collectedUser.setUserPhone(phone);
            collectedUser.setIdNumber((String) params.get("idCard"));
            collectedUser.setOverduedays((Integer) params.get("lateDay"));
            collectedUser.setRealname((String) params.get("realName"));
            collectedUser.setStatus((Integer) params.get("status"));
            result = collectedUserDAO.insertSelective(collectedUser);
        } else {
            collectedUser.setStatus((Integer) params.get("status"));
            result = collectedUserDAO.updateByPrimaryKeySelective(collectedUser);
        }
        return result;
    }
}
