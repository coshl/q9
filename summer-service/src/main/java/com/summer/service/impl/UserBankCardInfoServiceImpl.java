package com.summer.service.impl;

import com.summer.dao.entity.UserCardInfo;
import com.summer.dao.mapper.IUserCardInfoDao;
import com.summer.api.service.IUserBankCardInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户银行卡信息的Service接口实现类
 */
@Service
@Transactional
public class UserBankCardInfoServiceImpl implements IUserBankCardInfoService {

    @Resource
    private IUserCardInfoDao userBankCardInfoMapper;

    @Override
    public UserCardInfo selectUserBankCard(Integer id) {
        return userBankCardInfoMapper.selectUserBankCard(id);
    }

    @Override
    public UserCardInfo searchUserCardInfo(HashMap<String, Object> map) {
        return userBankCardInfoMapper.searchUserCardInfo(map);
    }

    @Override
    public int saveUserCardInfo(UserCardInfo userCardInfo) {
        return userBankCardInfoMapper.saveUserCardInfo(userCardInfo);
    }

    @Override
    public List<UserCardInfo> findUserCardByUserId(Map<String, Object> map) {
        return userBankCardInfoMapper.findUserCardByUserId(map);
    }

    @Override
    public int updateUserCardInfo(UserCardInfo userCardInfo) {
        return userBankCardInfoMapper.updateUserCardInfo(userCardInfo);
    }

    @Override
    public int deleteUserCardInfo(int id) {
        return userBankCardInfoMapper.deleteUserCardInfo(id);
    }

}
