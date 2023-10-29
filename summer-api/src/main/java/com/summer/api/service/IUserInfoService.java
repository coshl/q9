package com.summer.api.service;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.LinkfaceCompareResults;
import com.summer.dao.entity.UserInfo;
import com.summer.pojo.vo.BorrowUserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
public interface IUserInfoService {
    /**
     * 分页查询用户信息
     *
     * @param params
     * @return
     */
    PageInfo<BorrowUserVO> queryWithBorrow(Map<String, Object> params);

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入用户信息
     *
     * @param record
     * @return
     */
    int insert(UserInfo record);

    /**
     * 根据传入参数查找用户信息集合
     *
     * @param params
     * @return
     */
    List<BorrowUserVO> findOrder(Map<String, Object> params);

    /**
     * @param record
     * @return
     */
    int insertSelective(UserInfo record);

    /**
     * 根据主键查询用户
     *
     * @param id
     * @return
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * 通过选中的主键更新用户信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserInfo record);

    /**
     * 根据手机号码查询
     *
     * @param phone
     * @param idCard
     * @return
     */
    List<UserInfo> selectSimple(String phone, String idCard);

    /**
     * 根据主键更新用户信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserInfo record);

    /**
     * 根据Map中存入的参数查询用户信息
     *
     * @param params
     * @return
     */
    List<UserInfo> findParams(Map<String, Object> params);

    /**
     * 添加黑名单
     *
     * @param params
     * @return
     */
    int addBlackUser(HashMap<String, Object> params);

    /**
     * 删除黑名单
     *
     * @param id
     * @return
     */
    int deleteBlackUser(Integer id);


    /**
     * 根据手机号码查询用户
     *
     * @param phone
     * @return
     */
    UserInfo findByPhone(String phone);

    void updateMobileAuthenticStatusAndData(Integer userId, Integer status, Byte authenticStatus,
                                            Integer mxId, Integer mxAuthStatus, String crawlerid);

    void updateMobileAuthenticStatus(Integer userId, Integer status);

    //修改认证状态
    void updateAuthenticStatus(UserInfo userInfo);

    /**
     * 修改用户状态
     *
     * @param params
     * @return
     */
    int updateBlackUser(Map<String, Object> params);

    /**
     * 修改身份信息
     *
     * @param params
     * @return
     */
    int updateByUserId(HashMap<String, Object> params);

    /**
     * 新系统分页查询用户信息
     *
     * @param params
     * @return
     */
    PageInfo<BorrowUserVO> queryWithBorrowNew(Map<String, Object> params);

    /**
     * 有的花注册用户
     *
     * @param userInfo
     * @return
     */
    int updateSelectiveByApi(JSONObject userInfo);

    /**
     * 通过有的花订单号查询订单信息
     *
     * @param ydhOrderNo
     * @return
     */
    UserInfo selectByYdhOrderNo(String ydhOrderNo);

    /**
     * 通过身份证查询用户信息
     *
     * @param idCard
     * @return
     */
    List<UserInfo> selectByIdCard(String idCard);
    
    /**
     * 修改用户设备型号、系统版本
     * @param userInfo
     * @return
     */
    int updateUserDeviceModelAndSystemVersion(UserInfo userInfo);
    
    int insertLinkFaceCompareresult(LinkfaceCompareResults linkfaceCompareResults);
    
    Integer selectUserLinkFaceCount(String idCard);
}
