package com.summer.service.impl;

import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.InfoIndexInfoDao;
import com.summer.api.service.IInfoIndexInfoService;
import com.summer.api.service.IUserInfoService;
import com.summer.util.Constant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户认证统计的Service接口类实现类
 */
@Service
public class InfoIndexInfoService implements IInfoIndexInfoService {
    @Resource
    private InfoIndexInfoDao infoIndexInfoDAO;
    @Resource
    private IUserInfoService userInfoService;

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        return infoIndexInfoDAO.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(InfoIndexInfo record) {
        return infoIndexInfoDAO.insert(record);
    }

    @Override
    public int insertSelective(InfoIndexInfo record) {
        return infoIndexInfoDAO.insertSelective(record);
    }

    @Override
    public InfoIndexInfo selectByPrimaryKey(Integer userId) {
        return infoIndexInfoDAO.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(InfoIndexInfo record) {
        return infoIndexInfoDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(InfoIndexInfo record) {
        return infoIndexInfoDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<InfoIndexInfo> selectByParam(Map<String, Object> params) {
        return infoIndexInfoDAO.selectByParam(params);
    }

    @Override
    public void updateUserInfoAndInfoIndexInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
        //把之前的认证信息清空，然后再把给个标记 ，显示借款申请按钮
        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.setId(userInfo.getId());
        //修改实名认证状态为0：未认证
        updateUserInfo.setRealAuthentic(Constant.CHANGE_UNAUTH_STATE);
        //修改基本信息认证状态为0:未认证
        updateUserInfo.setBankAuthentic(Constant.CHANGE_UNAUTH_STATE);
        //修改紧急联系人认证状态为0：未认证
        updateUserInfo.setEmergencyAuthentic(Constant.CHANGE_UNAUTH_STATE);
        //修改运营商认证状态为0：未认证
        updateUserInfo.setMobileAuthentic(Constant.CHANGE_UNAUTH_STATE);
        //修改芝麻认证状态为0：未认证
        updateUserInfo.setZmStatus(Constant.CHANGE_UNAUTH_STATE);
        //修改认证状态
        updateUserInfo.setAuthenticStatus(Constant.CHANGE_UNAUTH_STATE);
        //修改性别为未知0
        updateUserInfo.setSex(Constant.USER_UNKONWN_STATE);
        //修改教育为未知
        updateUserInfo.setEducation(Constant.USER_UNKONWN_STATE);
        //修改现居地
        updateUserInfo.setPresentAddress(Constant.CHANGE_AUTH_INFO_NULL);
        //修改现居地详细地址
        updateUserInfo.setPresentAddressDetail(Constant.CHANGE_AUTH_INFO_NULL);
        //修改第一联系人电话为空
        updateUserInfo.setFirstContactPhone(Constant.CHANGE_AUTH_INFO_NULL);
        //修改第一联系人姓名为空
        updateUserInfo.setFirstContactName(Constant.CHANGE_AUTH_INFO_NULL);
        //修改与第一联系人姓名为未知
        updateUserInfo.setFirstContactRelation(Constant.USER_UNKONWN_STATE);
        //修改第二联系人姓名为空
        updateUserInfo.setSecondContactName(Constant.CHANGE_AUTH_INFO_NULL);
        //修改第二联系人电话为空
        updateUserInfo.setSecondContactPhone(Constant.CHANGE_AUTH_INFO_NULL);
        //修改与第二联系人关系为未知
        updateUserInfo.setSecondContactRelation(Constant.USER_UNKONWN_STATE);
        //修改头像地址为空
        updateUserInfo.setHeadPortrait(Constant.CHANGE_AUTH_INFO_NULL);
        //修改身份证号码为空
        updateUserInfo.setIdCard(Constant.CHANGE_AUTH_INFO_NULL);
        //修改身份证正面图片地址为空
        updateUserInfo.setIdcardImgZ(Constant.CHANGE_AUTH_INFO_NULL);
        //修改身份证反面图片地址为空
        updateUserInfo.setIdcardImgF(Constant.CHANGE_AUTH_INFO_NULL);
        //修改芝麻分为0
        updateUserInfo.setZmScore(Constant.ZMSCORE);
        //修改芝麻行业关注度黑名单为否
        //修改芝麻借贷逾期记录数AA001借贷逾期的记录数为0
        updateUserInfo.setZmOverNum(Constant.ZM_OVER_NUM);
        //修改逾期未支付记录数，包括AD001 逾期未支付、AE001 逾期未支付的记录总数为0
        updateUserInfo.setZmUnpayOverNum(Constant.ZM_UNPAY_OVER_NUM);
        //修改花呗额度为0
        updateUserInfo.setMyHb(Constant.MY_HB);
        //修改省份为空
        updateUserInfo.setProvince(Constant.CHANGE_AUTH_INFO_NULL);
        //修改现居地详细信息为空
        updateUserInfo.setPresentAddressDistinct(Constant.CHANGE_AUTH_INFO_NULL);
        //修改现居地地图的纬度为空
        updateUserInfo.setPresentLatitude(Constant.CHANGE_AUTH_INFO_NULL);
        //修改现居地址经度为空
        updateUserInfo.setPresentLongitude(Constant.CHANGE_AUTH_INFO_NULL);
        //魔杖认证为未认证
        updateUserInfo.setMzStatus(0);
        //更新user_info表
        userInfoService.updateByPrimaryKeySelective(updateUserInfo);

        InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
        infoIndexInfo.setUserId(userInfo.getId());
        //个人信息认证为0
        infoIndexInfo.setAuthInfo(Constant.CHANGE_INFOINDEX_UNAUTH_STATE);
        //修改银行卡认证为0
        // infoIndexInfo.setAuthBank(Constant.CHANGE_INFOINDEX_UNAUTH_STATE);
        infoIndexInfo.setAuthBankTime(new Date());
        //修改紧急联系人认证为0
        infoIndexInfo.setAuthContacts(Constant.CHANGE_INFOINDEX_UNAUTH_STATE);
        //修改认证总数为0
        infoIndexInfo.setAuthCount(Constant.CHANGE_INFOINDEX_UNAUTH_STATE);
        //修改运营商认证为0
        infoIndexInfo.setAuthMobile(Constant.CHANGE_INFOINDEX_UNAUTH_STATE);
        //修改芝麻认证为0
        infoIndexInfo.setAuthSesame(Constant.CHANGE_INFOINDEX_UNAUTH_STATE);
        //修改借款状态为不存在借款订单，可以展示申请借款按钮
        infoIndexInfo.setBorrowStatus(Constant.NOT_EXIST_BORROW_STATUS);
        //更新info_Index_Info表
        updateByPrimaryKeySelective(infoIndexInfo);
    }

	@Override
	public int insertBankBindFailureInfo(int userId, String userName, String info,String code) {
		return infoIndexInfoDAO.insertBankBindFailureInfo(userId, userName, info,code);
	}

	@Override
	public String selectMerchantFlag() {
		return infoIndexInfoDAO.selectMerchantFlag();
	}
}
