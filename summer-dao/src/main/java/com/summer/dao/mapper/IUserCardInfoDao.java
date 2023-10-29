package com.summer.dao.mapper;

import com.summer.dao.entity.UserCardInfo;
import com.summer.dao.entity.UserCardInfoUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈银行信息〉
 *
 * @author Liubing
 * @create 2018/4/25
 * @since 1.0.0
 */
public interface IUserCardInfoDao {

    UserCardInfo selectUserBankCard(Integer id);

    UserCardInfo searchUserCardInfo(HashMap<String, Object> map);

    int saveUserCardInfo(UserCardInfo userCardInfo);

    //
    List<UserCardInfo> findUserCardByUserId(Map<String, Object> map);

    //
    int updateUserCardInfo(UserCardInfo userCardInfo);

    //
    int deleteUserCardInfo(int id);

    List<UserCardInfoUpdate> findAllBank();

}
