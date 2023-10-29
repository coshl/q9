package com.summer.api.service;

import com.summer.dao.entity.UserCardInfo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户银行卡信息的Service接口
 */
public interface IUserBankCardInfoService {

    UserCardInfo selectUserBankCard(Integer id);

    UserCardInfo searchUserCardInfo(HashMap<String, Object> map);

    int saveUserCardInfo(UserCardInfo userCardInfo);

    //
    List<UserCardInfo> findUserCardByUserId(Map<String, Object> map);

    //
    int updateUserCardInfo(UserCardInfo userCardInfo);

    //
    int deleteUserCardInfo(int id);

}
