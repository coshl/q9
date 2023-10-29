package com.summer.api.service.channel;

import com.summer.dao.entity.OrderBorrow;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.UserInfo;

import java.util.Date;

/**
 * 渠道相关统计异步插入数据（进量统计、还款统计、渠道人员统计、渠道统计）
 */
public interface IChannelAsyncCountService {

    /**
     * 注册成功统计
     *
     * @param userId
     * @param channelId
     */
    void registerIsSuccCount(Integer userId, Integer channelId, Date nowTime);

    /**
     * 登录成功统计
     *
     * @param userInfo
     * @param nowTime
     */
    void loginIsSuccCount(UserInfo userInfo, Date nowTime);

    /**
     * 个人信息认证成功统计(对应后台的身份认证)
     *
     * @param userInfo
     * @param nowTime
     */
    void personInfoAuthIsSuccCount(UserInfo userInfo, Date nowTime);

    /**
     * 紧急联系人认证（后台的对应个人信息认证）
     *
     * @param userInfo
     * @param nowTime
     */
    void contactsAuthIsSuccCount(UserInfo userInfo, Date nowTime);

    /**
     * 运营商认证成功
     *
     * @param userInfo
     * @param nowTime
     */
    void mobileAuthIsSuccCount(UserInfo userInfo, Date nowTime);

    /**
     * 芝麻认证统计
     *
     * @param userInfo
     * @param nowTime
     */
    void zhiMaAuthIsSuccCount(UserInfo userInfo, Date nowTime);

    /**
     * 银行卡认证
     *
     * @param userInfo
     * @param nowTime
     */
    void bankAuthIsSuccCount(UserInfo userInfo, Date nowTime);

    /**
     * 工作信息认证统计
     *
     * @param userInfo
     * @param nowTime
     */
    void workInfoAuthIsSuccCount(UserInfo userInfo, Date nowTime);


    /**
     * 申请人数统计
     *
     * @param userInfo
     * @param nowTime
     * @param customerType 申请时客户类型
     */
    void applyCount(UserInfo userInfo, Date nowTime, Byte customerType);


    /**
     * 放款成功统计
     *
     * @param userInfo
     * @param nowTime
     * @param customerType 放款成功，传入借款订单中的客户类型
     */
    void loanIsSuccCount(UserInfo userInfo, Date nowTime, int customerType);


    /**
     * APP下载量
     *
     * @param userInfo
     * @param nowTime
     */
    void appDownloadCount(UserInfo userInfo, Date nowTime);

    /**
     * 渠道PV uv统计,定时任务统计
     *
     * @param time
     */
    void pvUvCount(Date time);

    /**
     * 渠道人员：投放连接数统计
     *
     * @param nowTime
     * @param plateformUserId
     * @param channelId
     */
    void deliveryConnectionCount(Date nowTime, Integer plateformUserId, Integer channelId);

    /**
     * 渠道PV uv统计 (异步统计)
     *
     * @param time
     * @param plateformChannel
     */
    void pvUvAsyncCount(PlateformChannel plateformChannel, Date time);

    /**
     * 补充渠道放款人数数据
     */

    void channelData(Integer day);

    /**
     * 命中系统黑名单
     *
     * @param userInfo
     */
    void hitSystemBlack(UserInfo userInfo, Date nowTime);

    /**
     * 命中外部三方黑名单
     *
     * @param userInfo
     * @param nowTime
     */
    void hitOutBlack(UserInfo userInfo, Date nowTime);

    /**
     * 命中系统黑名单
     *
     * @param userInfo
     */
    void hitSystemBlackRe(UserInfo userInfo, Date nowTime);

    /**
     * 命中外部三方黑名单
     *
     * @param userInfo
     * @param nowTime
     */
    void hitOutBlackRe(UserInfo userInfo, Date nowTime);

    /**
     * 放款成功时将订单信息同步到数据中心
     *
     * @param userInfo
     * @param orderBorrow
     */
    void pushInformation(UserInfo userInfo, OrderBorrow orderBorrow);

}
