package com.summer.web.controller.indexCache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.*;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.util.Constant;
import com.summer.util.RedisUtil;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.web.controller.appUser.IndexInfoController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存数据的类
 */
@Slf4j
@Component
public class IndexCacheData {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IInfoNoticeService infoNoticeService;
    @Resource
    private IPlatformBannerService platformBannerService;
    @Resource
    private IAppVersionInfoService appVersionInfoService;
    @Resource
    private IUserMoneyRateService userMoneyRateService;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;

    /**
     * 查询用户未登录的系统配置信息
     *
     * @return
     */
    public String cacheIndexData(Integer chaannelId) {
        Map<String, Object> redisCacheMap = new HashMap<>();
        //查询首页借款数据
        LoanRuleConfig loanRuleConfig = selectLoanRule(chaannelId);
        if (null != loanRuleConfig) {
            //系统默认借款金额
            redisCacheMap.put("loanAmount", (loanRuleConfig.getLoanAmount() / Constant.CENT_CHANGE_DOLLAR));
            //默认借款期限
            redisCacheMap.put("expire", loanRuleConfig.getExpire());
            //借款日利率
            BigDecimal dayInterest = new BigDecimal((loanRuleConfig.getBorrowInterest() / loanRuleConfig.getExpire()) * Constant.DAY_INTEREST_CHANGE);
            redisCacheMap.put("dayInterest", dayInterest.setScale(2, BigDecimal.ROUND_HALF_UP));
            //消息中心数量
            redisCacheMap.put("messageNum", selectMsgCenterNoticesSize());
            //借款记录
            redisCacheMap.put("userLoanLogList", selectLoanLogList());
            //首页图片
            redisCacheMap.put("bannerList", selectImgUrl());
            //未登录的标记
            redisCacheMap.put("indexflag", Constant.INDEX_UN_LOGON_SHOW_FLAG);
        }
        //还款金额计算方式，0表示：申请金额+服务费+利息，1表示：还款金额=申请金额
        Integer ctSwitch = 0;
        BackConfigParamsVo bySysKey = backConfigParamsDao.findBySysKey("ct_switch");
        if (null != bySysKey) {
            ctSwitch = bySysKey.getSysValue();
        }
        redisCacheMap.put("ctSwitch", ctSwitch);
        return JSON.toJSONString(redisCacheMap);
    }

    /**
     * 用户登录过后首页配置数据（没有订单）
     *
     * @return
     */
    public Map<String, Object> cacheUserLoginIndexInfo(UserInfo userInfo) throws Exception {
        //获得公共的数据（图片集合、借款记录、消息中心总数，是否更新版本）
        Map<String, Object> redisCacheMap = resultDefaultData(userInfo.getChannelId());

        //查询该用户的费率表
        UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
        //查询系统贷款规则：根据贷款规则配置生成费率信息
        LoanRuleConfig loanRuleConfig = selectLoanRule(userInfo.getChannelId());
        //如果等于空，说明是新用户还没有费率信息，
        if (null == userMoneyRate) {
            if (null != loanRuleConfig) {
                userMoneyRate = new UserMoneyRate();
                userMoneyRate.setUserId(userInfo.getId());
                //设置服务费
                userMoneyRate.setServiceCharge(loanRuleConfig.getServiceCharge());
                //设置最大借款金额
                userMoneyRate.setMaxAmount(loanRuleConfig.getLoanAmount());
                //设置借款利息
                userMoneyRate.setAccrual(loanRuleConfig.getBorrowInterest());
                // 插入user_money_rate
                userMoneyRateService.insert(userMoneyRate);
            }
        }

        redisCacheMap.replace("expire", loanRuleConfig.getExpire());
        //允许的借款金额
        redisCacheMap.put("loanAmount", userMoneyRate.getMaxAmount() / Constant.DOLLAR_CHANGE_PENNY);

        BigDecimal dayInterest = new BigDecimal((loanRuleConfig.getBorrowInterest() / loanRuleConfig.getExpire() * Constant.DAY_INTEREST_CHANGE));
        redisCacheMap.put("dayInterest", dayInterest.setScale(2, BigDecimal.ROUND_HALF_UP));
        //认证的数量
        InfoIndexInfo infoIndexInfo = selectAuthCoount(userInfo.getId());
        redisCacheMap.put("authCount", null == infoIndexInfo ? null : infoIndexInfo.getAuthCount());
        //不存在借款的订单的标记,替换掉原来的
        redisCacheMap.replace("indexflag", null == infoIndexInfo ? 0 : Integer.parseInt(infoIndexInfo.getBorrowStatus()));
        String chatUrl = backConfigParamsDao.findStrValue("app_chatUrl");
        redisCacheMap.put("chatUrl",chatUrl);
        redisCacheMap.put("faceId",null);
        redisCacheMap.put("secretId",null);
        //返回认证进度
        return redisCacheMap;
    }

    /**
     * 查询消息中心数量
     *
     * @return
     */
    public int selectMsgCenterNoticesSize() {
        List<InfoNotice> msgCenterNotices = infoNoticeService.findMsgCenterNotice();
        if (null != msgCenterNotices && msgCenterNotices.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            //消息的数量
            return msgCenterNotices.size();
        } else {
            return Constant.NOTICE_DEFAULT_COUNT;
        }
    }

    /**
     * 查询借款记录（对应首页轮播的小公告）
     *
     * @return
     */
    public List<InfoNotice> selectLoanLogList() {
        List<InfoNotice> loanLog = new ArrayList<>();
        Map<String, Object> carouselNoticeParam = new HashMap<>();
//        carouselNoticeParam.put("status", Constant.INDEX_NOTICE_STATUS);
        //查询首页轮播的借款记录
        carouselNoticeParam.put("noticeType", Constant.INDEX_NOTICE_BOX_TYPE);
        loanLog = infoNoticeService.findLoanLog(carouselNoticeParam);
        if (CollectionUtils.isEmpty(loanLog) || loanLog.get(0).getStatus() == 0) {
            carouselNoticeParam.put("noticeType", Constant.INDEX_NOTICE_CAROUSEL_TYPE);
            loanLog = infoNoticeService.findLoanLog(carouselNoticeParam);
        }
        return loanLog;
    }

    /**
     * 查询首页图片地址
     *
     * @return
     */
    public List<PlatformBanner> selectImgUrl() {
        //查询首页图片地址
        Map<String, Object> bannerParam = new HashMap<>();
        //1移动端
        bannerParam.put("equementType", Constant.BANNER_EQUEMENT_TYPE);
        //0表示首页
        bannerParam.put("type", Constant.BANNER_CHANNEL_ID);
        //启用
        bannerParam.put("status", Constant.BANNER_STATUS);
        List<PlatformBanner> imgUrls = platformBannerService.findIndexImgByParam(bannerParam);
        return imgUrls;
    }

    /**
     * 查询统计信息认证数量
     *
     * @return
     */
    public InfoIndexInfo selectAuthCoount(Integer userId) {
        return infoIndexInfoService.selectByPrimaryKey(userId);
    }

    /**
     * 更新缓存系统数据的的方法
     *
     * @return
     */
    public void updateDefaultCache() {
        String newCacheIndexData = cacheIndexData(0);
        redisUtil.set(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY, newCacheIndexData);

    }

    /**
     * 查询贷款规则配置
     *
     * @return
     */
    public LoanRuleConfig selectLoanRule(Integer channelId) {
        //查询首页借款数据
        return loanRuleConfigService.findByChannelId(channelId == null ? 0 : channelId);
    }

    /**
     * 返回系统默认数据
     *
     * @return
     */
    public Map<String, Object> resultDefaultData(Integer channelId) throws Exception {

        String indexJson = cacheIndexData(channelId);
        /*if (redisUtil.hasKey(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY)) {
            //缓存里有就直接取缓存里的数据
            indexJson = redisUtil.get(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY);
        } else {//没有去查数据库并同时存缓存
            indexJson = cacheIndexData(channelId);
            redisUtil.set(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY, indexJson);
        }*/
        Map<String, Object> resultMap = JSONObject.parseObject(indexJson);
        return resultMap;
    }
}
