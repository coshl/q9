package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.LinkfaceCompareResults;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.entity.UserMoXieDataWithBLOBs;
import com.summer.dao.mapper.InfoIndexInfoDao;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.dao.mapper.UserMoXieDataDAO;
import com.summer.enums.MobileAuthStatus;
import com.summer.pojo.dto.PersonInfoDto;
import com.summer.api.service.IInfoIndexInfoService;
import com.summer.api.service.IInfoIndexService;
import com.summer.api.service.IUserInfoService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.impl.thirdpart.zhimi.HttpClientUtil;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.util.log.StringUtils;
import com.summer.pojo.vo.BorrowUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Desc:用户的Service接口
 * Created by tl on 2018/12/20
 */
@Service
public class UserInfoService implements IUserInfoService {

    private static Logger log = LoggerFactory.getLogger(UserInfoService.class);

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IInfoIndexService infoIndexService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private InfoIndexInfoDao infoIndexInfoDao;
    @Resource
    private UserMoXieDataDAO userMoXieDataDAO;

    @Override
    public PageInfo<BorrowUserVO> queryWithBorrow(Map<String, Object> params) {
        List<BorrowUserVO> list = userInfoMapper.findOrder(params);
        return new PageInfo<>(list);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserInfo record) {
        return userInfoMapper.insert(record);
    }

    @Override
    public List<BorrowUserVO> findOrder(Map<String, Object> params) {
        return userInfoMapper.findOrder(params);
    }


    @Override
    public int insertSelective(UserInfo record) {
        return userInfoMapper.insertSelective(record);
    }

    @Override
    public UserInfo selectByPrimaryKey(Integer id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        return userInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<UserInfo> selectSimple(String phone, String idCard) {
        return userInfoMapper.selectSimple(phone, idCard);
    }

    @Override
    public int updateByPrimaryKey(UserInfo record) {
        return userInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<UserInfo> findParams(Map<String, Object> params) {
        return userInfoMapper.findParams(params);
    }

    @Override
    public int addBlackUser(HashMap<String, Object> params) {
        return userInfoMapper.addBlackUser(params);
    }

    @Override
    public int deleteBlackUser(Integer id) {
        return userInfoMapper.deleteBlackUser(id);
    }

    @Override
    public UserInfo findByPhone(String phone) {
        return userInfoMapper.findByPhone(phone);
    }


    /**
     * 修改运营商报告认证状态和用户认证状态
     * */
    @Transactional
    @Override
    public void updateMobileAuthenticStatusAndData(Integer userId, Integer status, Byte authenticStatus, Integer mxId, Integer mxAuthStatus, String crawlerid) {
        UserMoXieDataWithBLOBs updateData = new UserMoXieDataWithBLOBs();
        updateData.setId(mxId);
        // taskId是爬虫id
        updateData.setTaskId(crawlerid);
        // 改成认证中
        updateData.setMxAuthStatus(mxAuthStatus);
        userMoXieDataDAO.updateByPrimaryKeySelective(updateData);

        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(userId);
        indexInfo.setAuthMobile(status);
        infoIndexInfoDao.updateIndexInfoByUserId(indexInfo);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setMobileAuthentic(status.byteValue());
        userInfo.setAuthenticStatus(authenticStatus);
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    /**
     * 将运营商认证状态修改
     */
    @Transactional
    @Override
    public void updateMobileAuthenticStatus(Integer userId, Integer status) {
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(userId);
        indexInfo.setAuthMobile(status);
        infoIndexInfoDao.updateIndexInfoByUserId(indexInfo);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setMobileAuthentic(status.byteValue());
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public void updateAuthenticStatus(UserInfo userInfo) {
        userInfoMapper.updateAuthenticStatus(userInfo);
    }

    @Override
    public int updateBlackUser(Map<String, Object> params) {
        UserInfo userInfo = userInfoMapper.findByPhone(params.get("phone").toString());
        if (userInfo == null || userInfo.getId() == null) {
            return 0;
        } else {
            UserInfo userInfo1 = new UserInfo();
            userInfo1.setId(userInfo.getId());
            //status 0拉黑，1取消拉黑
            Integer status = (Integer) params.get("status");
            if (status == 0) {
                userInfo1.setStatus((byte) 1);
            } else if (status == 1) {
                userInfo1.setStatus((byte) 0);
            }
            return userInfoMapper.updateByPrimaryKeySelective(userInfo1);
        }
    }

    @Override
    public int updateByUserId(HashMap<String, Object> params) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId((Integer) params.get("id"));
        userInfo.setRealName((String) params.get("realName"));
        userInfo.setIdCard((String) params.get("idCard"));
        return userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public PageInfo<BorrowUserVO> queryWithBorrowNew(Map<String, Object> params) {
        List<BorrowUserVO> list = userInfoMapper.findOrderNew(params);
        return new PageInfo<>(list);
    }

    @Override
    public int updateSelectiveByApi(JSONObject userInfo) {
        return userInfoMapper.updateSelectiveByApi(userInfo);
    }

    @Override
    public UserInfo selectByYdhOrderNo(String ydhOrderNo) {
        return userInfoMapper.selectByYdhOrderNo(ydhOrderNo);
    }

    @Override
    public List<UserInfo> selectByIdCard(String idCard) {
        return userInfoMapper.selectByIdCard(idCard);
    }

    @Override
    public int updateUserDeviceModelAndSystemVersion(UserInfo userInfo) {
		return userInfoMapper.updateUserDeviceModelAndSystemVersion(userInfo);
	}

	@Override
	public int insertLinkFaceCompareresult(LinkfaceCompareResults linkfaceCompareResults) {
		return userInfoMapper.insertLinkFaceCompareresult(linkfaceCompareResults);
	}

	@Override
	public Integer selectUserLinkFaceCount(String idCard) {
		return userInfoMapper.selectUserLinkFaceCount(idCard);
	}
}
