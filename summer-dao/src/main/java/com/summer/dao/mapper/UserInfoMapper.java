package com.summer.dao.mapper;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.LinkfaceCompareResults;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.entity.UserUpdate;
import com.summer.pojo.vo.BorrowUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息Mapper接口
 * 2019/1/8
 */
public interface UserInfoMapper {

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
     * 根据传入参数查找用户对应的订单信息
     *
     * @param params
     * @return
     */
    List<BorrowUserVO> findOrder(Map<String, Object> params);

    /**
     * 动态插入用户信息
     *
     * @param record
     * @return
     */
    int insertSelective(UserInfo record);

    /**
     * 根据主键查找用户信息
     *
     * @param id
     * @return
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * 根据参数简单查询
     *
     * @param phone
     * @param idCard
     * @return
     */
    List<UserInfo> selectSimple(@Param("phone") String phone, @Param("idCard") String idCard);

    /**
     * 根据主键更新用户信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserInfo record);


    /**
     * 通过主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserInfo record);

    /**
     * 根据参数查找用户信息
     *
     * @param params
     * @return
     */
    List<UserInfo> findParams(Map<String, Object> params);


    UserInfo selectByUserId(int id);

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

    Long statisticByDay(Integer number);

    Long countReloan();

    Long countRenew();

    Long countTotal();

    Long countOverdue();

    Long countLoan();

    Long countRenewal();

    Long countMaturityLoan();

    Long registerByDay(Map<String, Object> map);

    Long loanByDay(Map<String, Object> map);

    Long overdueByDay(Map<String, Object> map);

    void updateAuthenticStatus(UserInfo userInfo);

    Long selectReloan(Integer borrowId);

    List<BorrowUserVO> queryIos(Map<String, Object> params);

    /**
     * 根据魔杖认证状态查询未认证的用户
     *
     * @return
     */
    List<UserInfo> selectByMzStatus();

    /**
     * 根据传入参数查找用户对应的订单信息
     *
     * @param params
     * @return
     */
    List<BorrowUserVO> findOrderNew(Map<String, Object> params);

    /**
     * 有的花注册用户
     *
     * @param record
     * @return
     */
    int updateSelectiveByApi(JSONObject record);

    List<UserInfo> selectByIdCard(String idCard);

    /**
     * 通过有的花订单号查询订单信息
     *
     * @param ydhOrderNo
     * @return
     */
    UserInfo selectByYdhOrderNo(String ydhOrderNo);

    /**
     * 通过渠道查询用户id集合
     *
     * @param channelId
     * @return
     */
    List<Integer> findByChannelId(Integer channelId);

    /**
     * 根据注册时间查询注册人数
     *
     * @param params
     * @return
     */
    Integer selectNumByTime(Map<String, Object> params);

    String selectChannelById(Integer id);

    UserInfo findPwdById(Integer id);


    List<UserUpdate> findUpdateUser(Map<String, Object> param);

    /**
     * 修改用户设备型号、系统版本
     * @param userInfo
     * @return
     */
    int updateUserDeviceModelAndSystemVersion(UserInfo userInfo);

    /* Long countDue();*/

    int insertLinkFaceCompareresult(LinkfaceCompareResults linkfaceCompareResults);

    Integer selectUserLinkFaceCount(String idCard);
}
