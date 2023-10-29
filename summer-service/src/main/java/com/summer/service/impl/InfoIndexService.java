package com.summer.service.impl;

import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.UserCardInfo;
import com.summer.dao.mapper.IUserCardInfoDao;
import com.summer.dao.mapper.InfoIndexInfoDao;
import com.summer.api.service.IInfoIndexService;
import com.summer.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * APP首页信息Service接口实现类
 */
@Service
public class InfoIndexService implements IInfoIndexService {


    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Resource
    private IUserCardInfoDao userCardInfoDao;


    @Override
    public void authBank(HashMap<String, Object> map) {
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));

        HashMap<String, Object> bankMap = new HashMap<String, Object>();
        bankMap.put("USER_ID", Integer.parseInt(String.valueOf(map.get("userId"))));
        UserCardInfo cardInfo = userCardInfoDao.searchUserCardInfo(bankMap);
        if (null != cardInfo) {
            if (StringUtils.isNotBlank(cardInfo.getCardNo())) {
                indexInfo.setBankNo(cardInfo.getCardNo());
            }
        }
        indexInfo.setAuthBank(Constant.STATUS_INT_VALID);
        infoIndexInfoDao.updateIndexInfoByUserId(indexInfo);
    }

    @Override
    public void authBankOld(HashMap<String, Object> map) {
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));

        HashMap<String, Object> bankMap = new HashMap<String, Object>();
        bankMap.put("USER_ID", Integer.parseInt(String.valueOf(map.get("userId"))));
        UserCardInfo cardInfo = userCardInfoDao.searchUserCardInfo(bankMap);
        if (null != cardInfo) {
            if (StringUtils.isNotBlank(cardInfo.getCardNo())) {
                indexInfo.setBankNo(cardInfo.getCardNo());
            }
        }
        indexInfo.setAuthBank(Constant.STATUS_INT_VALID);
        infoIndexInfoDao.updateIndexInfoByUserIdOld(indexInfo);
    }
}
