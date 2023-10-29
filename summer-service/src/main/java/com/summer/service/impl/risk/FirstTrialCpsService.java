package com.summer.service.impl.risk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.RiskFkContentService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.api.service.risk.IFirstTrialCpsService;
import com.summer.api.service.thirdpart.IOperatorMoXieService;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.enums.*;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.TotalAmountVO;
import com.summer.queue.QueueConstans;
import com.summer.service.IUserMobileDataRecordService;
import com.summer.service.impl.MerchantService;
import com.summer.service.impl.UserInfoService;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.impl.thirdpart.zhimi.HttpClientUtil;
import com.summer.service.mq.OrderProducer;
import com.summer.service.yys.wuhua.WuHuaApi;
import com.summer.util.*;
import com.summer.util.risk.ConstantByRisk;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ： FirstTrialCpsService
 * @Description ：
 * @Author：
 * @Date ：2019/10/18 14:51
 * @Version ：V1.0
 **/
@Slf4j
@Service
public class FirstTrialCpsService implements IFirstTrialCpsService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RiskRuleConfigDAO riskRuleConfigDAO;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private OrderProducer orderProducer;
    @Resource
    private UserContactsDAO userContactsDAO;
    @Resource
    private RiskTrialCountDAO riskTrialCountDAO;
    @Resource
    private IOperatorMoXieService operatorMoXieService;
    @Resource
    private IRiskCreditUserDao riskCreditUserDao;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;

    @Resource
    private IBackConfigParamsDao backConfigParamsDao;

    // 同时获取运营商原始数据和报告
    @Value("${mobile.xinPan.appId}")
    private String xinPanAppId;
    @Value("${mobile.xinPan.appKey}")
    private String xinPanAppKey;
    @Value("${mobile.xinPan.rawDataUrl}")
    private String xinPanRawDataUrl;
    @Value("${mobile.xinPan.reportDataUrl}")
    private String xinPanReportDataUrl;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private PlateformChannelMapper plateformChannelMapper;

    @Resource
    private UserBlackListDAO userBlackListDAO;

    @Resource
    private BlackApi blackApi;

    @Resource
    private RiskFkContentService riskFkContentService;

    @Resource
    MoneyRecordMapper moneyRecordMapper;

    @Resource
    private UserMoXieDataDAO userMoXieDataDAO;

    @Resource
    private IUserMobileDataRecordService userMobileDataRecordService;

    @Resource
    private WuHuaApi wuHuaApi;

    private static final String GAODE_KEY = "bee391a303a76220943f645af25d779a";

    /**
     * 机审
     */
    //TODO 00 机审
    @Transactional
    @JmsListener(destination = QueueConstans.newOrderQueue)
    @Override
    public void riskFirstTrial(String jsonData) {
        log.info("start invoke FirstTrialMoxieService.riskFirstTrial  ----初审开始 jsonData={}", jsonData);
        //  try {
        /**1、判断传入的订单JSON数据是否为空*/
        if (null == jsonData) {
            log.info("FirstTrialMoxieService.riskFirstTrial ------->传入订单JSON字符串数据为空");
            return;
        }

        /**2、判断订单是否为空*/
        OrderBorrow orderBorrow = JSONObject.parseObject(jsonData, OrderBorrow.class);
        if (null == orderBorrow) {
            log.info("FirstTrialMoxieService.riskFirstTrial ------->订单信息为空");
            return;
        }
        String lockKey = Constant.RISK_RE_PUSH_ + orderBorrow.getId();
        Boolean result = redisUtil.setIfAbsent(lockKey, orderBorrow.getUserId().toString(), 35, TimeUnit.SECONDS);
        if (!result) {
            long expire = redisUtil.getExpire(lockKey);
            log.info("初审进行中,请等待{}s:{}", expire, lockKey);
            return;
        }

        /**查询风控余额 TODO 费用中心没搭，暂时先让他过去*/
        //boolean isSucc = true;// riskBalance(orderBorrow);
        String modeFlag = moneyRecordMapper.getMode();
        if (modeFlag.equals("1")) {
            TotalAmountVO modeOneIsOpen = moneyRecordMapper.selectTotalAmout();
            if (modeOneIsOpen.getCurrentMmoney().intValue() <= -5000) {
                log.info("初审风控余额查询----isSucc={}", true);
                orderBorrow.setTrialRemark("风控余额不足,暂停机审,请充值后重新机审!");
                updateOrderTrialInfo(orderBorrow);
                log.info("当前初审风控金额已到达最大欠款金额5000,请充值！------>isSucc={}", false);
                log.info("初审风控余额查询--结束方法--isSucc={}", true);
                return;
            }
        } else {
            TotalAmountVO modeTwoIsOpen = moneyRecordMapper.findTotalMoney();
            if (modeTwoIsOpen.getCurrentMmoney().intValue() <= -5000) {
                log.info("初审风控余额查询----isSucc={}", true);
                orderBorrow.setTrialRemark("风控余额不足,暂停机审,请充值后重新机审!");
                updateOrderTrialInfo(orderBorrow);
                log.info("当前初审风控金额已到达最大欠款金额5000,请充值！------>isSucc={}", false);
                log.info("初审风控余额查询--结束方法--isSucc={}", true);
                return;
            }
        }
//        log.info("初审风控余额查询----isSucc={}", true);
//        //if (isSucc) {
//        int isOpen = merchantService.getModeIsOpen();
//        if(isOpen == Constant.WIND_CONTROL_MONEY_ISOPEN) {
//            orderBorrow.setTrialRemark("风控余额不足,暂停机审,请充值后重新机审!");
//            updateOrderTrialInfo(orderBorrow);
//        	log.info("当前初审风控金额已到达最大欠款金额5000,请充值！------>isSucc={}", false);
//            return;
//        }
//        log.info("初审风控余额查询--结束方法--isSucc={}", true);
        //}

        /**3、根据借款订单中的用户ID查询用户信息*/
        UserInfo userInfo = userInfoMapper.selectByUserId(orderBorrow.getUserId());
        if (null == userInfo) {
            orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
            orderBorrow.setTrialRemark("该订单用户信息不存在");
            updateOrderTrialInfo(orderBorrow);
            log.info("FirstTrialMoxieService.riskFirstTrial ------->用户信息为空 order={},userId={}", orderBorrow.getId(), orderBorrow.getUserId());
            return;
        }


        // 商户内部黑名单开关1 关闭黑名单 2开启黑名单
        BackConfigParamsVo user_black_list = backConfigParamsDao.findBySysKey("user_Black_List");
        if (user_black_list.getSysValue() == 2) {
            log.info("============ 初审[{}]:商户内部黑名单校验 ============");
            Integer sysValue = user_black_list.getSysValue();
            //1 关闭黑名单 2开启黑名单
            if (sysValue != 1) {
                UserBlackList userBlackList = userBlackListDAO.findByPhone(userInfo.getPhone());
                if (null != userBlackList) {
                    if (userBlackList.getStatus() == 0) {
                        log.info("内部黑名单用户,初审驳回");
                        //命中黑名单：初审驳回
                        orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
                        orderBorrow.setTrialRemark("内部黑名单用户");
                        updateOrderTrialInfo(orderBorrow);
                        return;
                    }
                }
            }
        }

        BackConfigParamsVo csvo = backConfigParamsDao.findBySysKey("caishen_black_switch");
        if (csvo.getSysValue() == 2) {
            // 中央黑名单校验
            try {
                //Boolean success = blackListServer.isExist(userInfo.getPhone(), userInfo.getIdCard()).getSuccess();
                Boolean success = JuGuangApi.queryBlack(userInfo.getIdCard(),userInfo.getPhone());
                if (success) {
                    // 命中黑名单
                    //命中黑名单：初审驳回
                    orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
                    orderBorrow.setTrialRemark("中央黑名单用户");
                    updateOrderTrialInfo(orderBorrow);
                    //加入内部黑名单
                    UserBlackList userBlackList = new UserBlackList();
                    userBlackList.setIdCard(userInfo.getIdCard());
                    userBlackList.setPhone(userInfo.getPhone());
                    userBlackList.setUserName(userInfo.getRealName());
                    userBlackList.setStatus(0);
                    userBlackList.setRemark("中央黑名单用户");
                    userBlackList.setCreateTime(new Date());
                    userBlackListDAO.insert(userBlackList);
                    //异步统计命中系统黑名单数量
                    channelAsyncCountService.hitSystemBlack(userInfo, new Date());
                    return;
                }
            } catch (Exception e) {
                log.error("中央黑名单校验异常:{}", e);
            }
        }

        int customerType = orderBorrow.getCustomerType();
        //订单状态
        byte orderStatus = orderBorrow.getStatus();
        //新客外部黑名单 TODO 连接费用中心的功能，先隐藏
        if (customerType == 0) {
            log.info("============ 初审[{}]:新客外部黑名单校验 ============", ConstantByRisk.R_BLACK001);
            //根据黑名单key查询对应规则
            RiskRuleConfig byKey = riskRuleConfigDAO.findByKey(ConstantByRisk.R_BLACK001);
            if (null != byKey) {
                Byte status = byKey.getStatus();
                if (0 == status) {
                    // 达到逾期自动导入黑名单
                    BackConfigParamsVo overdueDayInsert = backConfigParamsDao.findBySysKey("overdue_day_insert");
                    Map<String, Object> param = new HashMap<>();
                    param.put("phone", userInfo.getPhone());
                    param.put("idCard", userInfo.getIdCard());
                    //传入当前的配置的逾期天数
                    if (null != overdueDayInsert) {
                        param.put("lateDay", overdueDayInsert.getSysValue());
                    }
                    String outsideblackRes = blackApi.isOutsidBlack(userInfo.getIdCard(), userInfo.getPhone(),userInfo.getId());
                    log.info("请求数据中心黑名单返回参数------outsideblackRes={}", outsideblackRes);
                    if (outsideblackRes != null) {
                        //命中外部黑名单：初审驳回
                        orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
                        orderBorrow.setTrialRemark("外部黑名单用户");
                        updateOrderTrialInfo(orderBorrow);
                        //命中黑名单人数
                        countHitRule(byKey, orderStatus);
                        //异步统计命中系统黑名单数量
                        channelAsyncCountService.hitSystemBlack(userInfo, new Date());

                        //加入内部黑名单
                        UserBlackList userBlackList = new UserBlackList();
                        userBlackList.setIdCard(userInfo.getIdCard());
                        userBlackList.setPhone(userInfo.getPhone());
                        if (StringUtils.isNotBlank(userInfo.getRealName()))
                            userBlackList.setUserName(userInfo.getRealName());
                        userBlackList.setUserName(userInfo.getRealName());
                        userBlackList.setStatus(0);
                        userBlackList.setRemark(outsideblackRes);
                        userBlackList.setCreateTime(new Date());
                        userBlackListDAO.insert(userBlackList);
                        return;
                    }

                    //开启黑名单规则，统计通过黑名单的人数
                    countPassRule(byKey, orderStatus);
                }
            }
        }


        /**4、根据借款订单中的客户类型，判断是否新老用户（新用户过黑名单，不走初审风控）*/
        /**4.1新用户，走初审风控*/
        log.info("============ 初审:根据借款订单中的客户类型，判断是否新老用户（新用户过黑名单，不走初审风控） ============");
        if (customerType != 0) {
            /**4.2老用户走黑名单,暂时就只走我们用户表的黑名单*/
            byte status = userInfo.getStatus();
            if (ConstantByRisk.USER_STATUS_BLANK == status) {
                //命中黑名单：初审驳回
                orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
                orderBorrow.setTrialRemark("内部黑名单用户");
                updateOrderTrialInfo(orderBorrow);
                return;
            }
            //老用户正常的话，直接修改借款订单表中的状态为2：放款中
            orderBorrow.setStatus(Constant.TRIAL_PASS_STATUS);
            orderBorrow.setTrialRemark("审核通过");
            updateOrderTrialInfo(orderBorrow);
            // 延长锁时间
            redisUtil.setIfAbsent(lockKey, orderBorrow.getUserId().toString(), 35, TimeUnit.SECONDS);
            return;
        }
        //渠道直接拒绝的用户
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(userInfo.getChannelId());
        if (null != plateformChannel) {
            int riskSwitch = plateformChannel.getRiskSwitch();
            //如果渠道配置了直接拒绝，
            if (riskSwitch == 1) {
                log.info("渠道直接拒绝的用户,初审驳回");
                orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
                orderBorrow.setTrialRemark("渠道直接拒绝的用户");
                updateOrderTrialInfo(orderBorrow);
                log.info("FirstTrialMoxieService.riskFirstTrial ------->用户信息为空 order={},userId={}", orderBorrow.getId(), orderBorrow.getUserId());
                return;
            }
        }

        // 初审规则过滤
        /**5、查询出有效的风控规则:risk_rule_config表*/
        List<RiskRuleConfig> riskRuleByStatus = riskRuleConfigDAO.findRiskRuleByStatus(Constant.RULE_EFFECTIVE_STATUS);
        if (CollectionUtils.isEmpty(riskRuleByStatus)) {
            /**5.2规则列表为空，说明初审风控规则被全部关闭，直接通过，进入复审*/
            orderBorrow.setStatus(Constant.TRIAL_PASS_STATUS);
            orderBorrow.setTrialRemark("初审风控规则全部关闭，初审通过");
            updateOrderTrialInfo(orderBorrow);
            // 延长锁时间
            redisUtil.setIfAbsent(lockKey, orderBorrow.getUserId().toString(), 35, TimeUnit.SECONDS);
            return;
        }

        /**5.1不为空，根据查询出来的规则进行判断*/
        //5.1.1根据订单中用户id查询通讯录*/
        List<UserContacts> userContactsList = userContactsDAO.findContatsByUserId(orderBorrow.getUserId());
        /**6、设置订单的状态为1：初审驳回*/
        orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
        /**7、遍历所有开启的风控规则*/
        UserMoXieDataWithBLOBs userMoXieData = operatorMoXieService.getUserMoXieData(userInfo.getId());
        if (Objects.isNull(userMoXieData)) {
            log.info("没有运营商爬虫回调记录,初审驳回:{}", userInfo.getId());
            orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
            orderBorrow.setTrialRemark("审核数据暂未获取到,请重推审核");
            updateOrderTrialInfo(orderBorrow);
            return;
        } else if (Objects.nonNull(userMoXieData) && StringUtils.isAnyBlank(userMoXieData.getMxRaw(), userMoXieData.getMxReport())) {
            // 初审这里拉报告生成运营商报告数据
            log.info("星盘运营商id"+ userMoXieData.getId().toString());
            userMoXieData = pullMobileReport(userInfo.getId(), userMoXieData.getId(), userMoXieData.getTaskId(),String.valueOf(userMoXieData.getStatus()));
            /*log.info("星盘运营商getMxRaw"+ userMoXieData.getMxRaw());
            log.info("星盘运营商getMxReport"+ userMoXieData.getMxReport());*/
            if (Objects.isNull(userMoXieData)) {
                log.info("没有运营商爬虫回调记录,初审驳回1:{}", userInfo.getId());
                orderBorrow.setStatus(Constant.TRIAL_REJECT_STATUS);
                orderBorrow.setTrialRemark("没有运营商爬虫回调记录1,初审驳回");
                updateOrderTrialInfo(orderBorrow);
                return;
            }
        }

        for (RiskRuleConfig riskRuleConfig : riskRuleByStatus) {
            //获取规则中的值
            Map<String, Object> ruleValueMap = JSONObject.parseObject(riskRuleConfig.getRuleValue());
            //获取规则的key
            String ruleKey = riskRuleConfig.getRuleKey();
            log.info("当前规则:{}-{}", ruleKey, riskRuleConfig.getRuleDescript());

            /**7.1、通讯录联系人数量低于20个*/
            if (ConstantByRisk.R_TXL_KEY1.equals(ruleKey)) {
                //统计通讯录数量(排除了不合法的电话，包括座机电话)
                int contactCount = userContactsDAO.countContactsPhone(userInfo.getId());
                int intValue = (int) ruleValueMap.get("intValue");
                if (contactCount < intValue) {
                    log.info("通讯录联系人数量低于{},初审驳回", intValue);
                    orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                    updateOrderTrialInfo(orderBorrow);
                    //统计命中该规则的订单的数量
                    countHitRule(riskRuleConfig, orderStatus);
                    return;
                } else {
                    //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                    countPassRule(riskRuleConfig, orderStatus);
                    continue;
                }
            }
            /**7.2、通讯录命中敏感词汇贷或借*/
            if (ConstantByRisk.R_TXL_KEY2.equals(riskRuleConfig.getRuleKey())) {
                String strValueArray = (String) ruleValueMap.get("strValueArray");
                int intValue = (int) ruleValueMap.get("intValue");
                if (CollectionUtils.isEmpty(userContactsList)) {
                    log.info("通讯录命中敏感词为空,{}", userContactsList);
                                /*orderBorrow.setTrialRemark("未上传通讯录");
                                updateOrderTrialInfo(orderBorrow);
                                //统计命中该规则的订单的数量
                                countHitRule(riskRuleConfig);
                                return;*/
                    continue;
                } else {
                    //统计命中敏感词汇的的次数
                    int hitCount = 0;
                    for (UserContacts userContacts : userContactsList) {
                        if (userContacts == null || null == userContacts.getContactName()) {
                            continue;
                        }
                        //获取规则中的值敏感词进行分割
                        String[] strValue = strValueArray.split("，");
                        for (int i = 0; i < strValue.length; i++) {
                            if (userContacts.getContactName().contains(strValue[i])) {
                                hitCount++;
                            }
                        }
                    }
                    //命中的次数大于规则中的数
                    if (hitCount > intValue) {
                        log.info("通讯录命中敏感词命中的次数大于规则中的数,{}", intValue);
                        orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                        updateOrderTrialInfo(orderBorrow);
                        //统计命中该规则的订单的数量
                        countHitRule(riskRuleConfig, orderStatus);
                        return;
                    } else {
                        //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                        countPassRule(riskRuleConfig, orderStatus);
                        continue;
                    }
                }
            }

            /**7.3、通讯录涉及：警官  警察  警方 法官*/
            if (ConstantByRisk.R_TXL_KEY3.equals(riskRuleConfig.getRuleKey())) {
                String strValueArray = (String) ruleValueMap.get("strValueArray");
                if (CollectionUtils.isEmpty(userContactsList)) {
                    log.info("未上传通讯录");
                                /*orderBorrow.setTrialRemark("未上传通讯录");
                                updateOrderTrialInfo(orderBorrow);
                                //统计命中该规则的订单的数量
                                countHitRule(riskRuleConfig);
                                return;*/
                    continue;
                } else {
                    boolean pass = false;
                    for (UserContacts userContacts : userContactsList) {
                        if (userContacts == null || null == userContacts.getContactName()) {
                            continue;
                        }
                        //获取规则中的值敏感词进行分割
                        String[] strValue = strValueArray.split("，");
                        for (int i = 0; i < strValue.length; i++) {
                            if (userContacts.getContactName().contains(strValue[i])) {
                                pass = true;
                            }
                        }
                    }
                    //如果命中了
                    if (pass) {
                        orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                        updateOrderTrialInfo(orderBorrow);
                        //统计命中该规则的订单的数量
                        countHitRule(riskRuleConfig, orderStatus);
                        log.info("通讯录涉及：警官  警察  警方 法官");
                        return;
                    } else {
                        //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                        countPassRule(riskRuleConfig, orderStatus);
                        continue;
                    }
                }
            }
            /**7.4判断年龄维度*/
            if (ConstantByRisk.R_LN.equals(riskRuleConfig.getRuleKey())) {
                int maxValue = (int) ruleValueMap.get("maxValue");
                int minValue = (int) ruleValueMap.get("minValue");
                if (null == userInfo) {
                               /* orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                                updateOrderTrialInfo(orderBorrow);
                                *//**统计命中该规则的订单的数量*//*
                                countHitRule(riskRuleConfig);
                                return;*/
                    continue;
                } else {
                    if (userInfo.getAge() < minValue || userInfo.getAge() > maxValue) {
                        orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                        updateOrderTrialInfo(orderBorrow);
                        /**统计命中该规则的订单的数量*/
                        countHitRule(riskRuleConfig, orderStatus);
                        return;
                    } else {
                        //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                        countPassRule(riskRuleConfig, orderStatus);
                        continue;
                    }
                }
            }

            /**7.10、不符合的地区*/
            if (ConstantByRisk.R_DQ.equals(riskRuleConfig.getRuleKey())) {
                //Present present = getPresent(userInfo);
                Present present = new Present();
                String idCardAddress = userInfo.getIdCardAddress();
                //截取下省，尽可能避免被后面的小地名命中 目前发现最长的： 新疆维吾尔自治区 暂时截取0到8
                if (StringUtils.isNotBlank(idCardAddress) && idCardAddress.length() >= 8) {
                    idCardAddress = idCardAddress.substring(0, 8);
                }

                present.setProvince(idCardAddress);
                present.setCity(userInfo.getIdCardAddress());
                if (null == present.getProvince() || null == present.getCity()) {
                    continue;
                }
                //使用身份证地址，去模糊匹配规则
                Boolean regionMatched = regionJudge(JSONObject.parseObject(riskRuleConfig.getRuleValue()), present.getProvince(), present.getCity());
                if (regionMatched) {
                    orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript() + ":" + present.getProvince() + present.getCity() + present.getLiveAddr());
                    updateOrderTrialInfo(orderBorrow);
                    //统计命中该规则的订单的数量
                    countHitRule(riskRuleConfig, orderStatus);
                    return;
                } else {
                    //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                    countPassRule(riskRuleConfig, orderStatus);
                    continue;
                }
            }

            //JSONObject mxReportJSON = JSONObject.parseObject(userMoXieData.getMxReport());
            //JSONObject mxRawJSON = JSONObject.parseObject(userMoXieData.getMxRaw());
            /**7.12 身份证号码是否与运营商数据匹配*/
            log.info("身份证号码是否与运营商数据匹配");
            /*if (ConstantByRisk.R_YYS6.equals(ruleKey)) {
                //5.1.3获取运营商报告
                if (mxReportJSON.isEmpty()) {
                    continue;
                }
                // 信息核对（身份证和运营商 是否匹配）
                String idcard_matching = mxReportJSON.getJSONObject("applier_info").getString("");
                if (StringUtils.isBlank(idcard_matching)) {
                    continue;
                }
                if (StringUtils.equalsIgnoreCase(userInfo.getIdCard(), idcard_matching)) {
                    orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                    updateOrderTrialInfo(orderBorrow);
                    *//**统计命中该规则的订单的数量*//*
                    countHitRule(riskRuleConfig, orderStatus);
                    return;
                } else {
                    //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                    countPassRule(riskRuleConfig, orderStatus);
                    continue;
                }
            }*/

            /**7.13 姓名是否与运营商数据匹配*/
           /* if (ConstantByRisk.R_YYS7.equals(ruleKey)) {
                //5.1.3获取指迷返回的报告
                if (mxReportJSON.isEmpty()) {
                    continue;
                }
                //信息核对(姓名与运营商是否匹配)
                String name_matching = mxReportJSON.getJSONObject("applier_info").getString("name");
                if (StringUtils.isBlank(name_matching)) {
                    continue;
                }
                if (!StringUtils.equalsIgnoreCase(userInfo.getRealName(), name_matching)) {
                    orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript());
                    updateOrderTrialInfo(orderBorrow);
                    *//**统计命中该规则的订单的数量*//*
                    countHitRule(riskRuleConfig, orderStatus);
                    return;
                } else {
                    //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                    countPassRule(riskRuleConfig, orderStatus);
                    continue;
                }
            }*/
            /**入网时长过滤*/
           /* if (ConstantByRisk.R_YYS8.equals(ruleKey)) {
                //5.1.3获取指迷返回的报告
                if (mxReportJSON.isEmpty()) {
                    continue;
                }
                Object minValue = ruleValueMap.get("minValue");
                if (Objects.isNull(minValue)) {
                    continue;
                }
                int minValueInt = Integer.parseInt(String.valueOf(minValue));
                String date = mxRawJSON.getJSONObject("data").getJSONObject("basicInfo").getString("joinDate");
                if (StringUtils.isEmpty(date)) {
                    orderBorrow.setTrialRemark("入网时长为空，初审驳回");
                    updateOrderTrialInfo(orderBorrow);
                    *//**统计命中该规则的订单的数量*//*
                    countHitRule(riskRuleConfig, orderStatus);
                    return;
                }
                Date joinDate = DateUtil.parseTimeYmd(date);
                Date addMonth = DateUtil.addMonth(joinDate, minValueInt);
                if (addMonth.after(new Date())) {
                    orderBorrow.setTrialRemark(riskRuleConfig.getRuleDescript() + "初审驳回");
                    updateOrderTrialInfo(orderBorrow);
                    *//**统计命中该规则的订单的数量*//*
                    countHitRule(riskRuleConfig, orderStatus);
                    return;
                } else {
                    //TODO 结束本次循环，进入下次循环 ,统计当天通过该规则的数量
                    countPassRule(riskRuleConfig, orderStatus);
                    continue;
                }
            }*/
        }

        log.info("未命中风控规则，初审通过");
        /**8、循环过所有的规则后，如果都没有被拒，直接通过，进入复审*/
        orderBorrow.setStatus(Constant.TRIAL_PASS_STATUS);
        orderBorrow.setTrialRemark("未命中风控规则，初审通过");
        try {
            // 生成风控报告
            log.info("生成风控报告");
            riskFkContentService.initReportByUserId(userInfo.getId());
        } catch (Exception e) {
            log.error("生成风控报告失败:{}", e);
        }
        updateOrderTrialInfo(orderBorrow);
        // 延长锁时间
        redisUtil.setIfAbsent(lockKey, orderBorrow.getUserId().toString(), 35, TimeUnit.SECONDS);
        return;
    }


    /**
     * TODO 00 拉取运营商报告
     */
    private UserMoXieDataWithBLOBs pullMobileReport(Integer userId, Integer mxId, String crawlerid,String mobileSwitch) {
        try {
            // 调用具体的运营商api获取原始数据
            int res = 0;
            if (Objects.equals(mobileSwitch, MobileSwitch.FUYGS.getValue())) {
                return null;
            }
            else if (Objects.equals(mobileSwitch, MobileSwitch.XINGPAN.getValue())) {
                log.info("开始调用星盘运营商接口获取运营商数据:{},token数据:{}", userId,crawlerid);
                Map params = new TreeMap();
                params.put("token", crawlerid);//  爬虫token
                Map heards = new TreeMap();
                heards.put("appId", xinPanAppId);//"8008869";// 商户 id
                heards.put("appKey", xinPanAppKey);// 商户key
                heards.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                //获取通话记录（与数据）接口
                String xinPanRawData = HttpClientUtil.postForm(xinPanRawDataUrl,heards,params);
                //JSONObject xinPanRawDataJsonObject = JSONObject.parseObject(xinPanRawData);
                //获取通话报告接口
                String xinPanReportData = HttpClientUtil.postForm(xinPanReportDataUrl,heards,params);
                JSONObject xinPanReportDataJsonObject = JSONObject.parseObject(xinPanReportData);
                if(!xinPanReportDataJsonObject.getString("code").equals("SUCCESS")){
                    return null;
                }
                // TODO 上传阿里云获得存储路径，格式：商户pid+userid+爬虫id+后缀名
                UserMoXieDataWithBLOBs updateDataPath = new UserMoXieDataWithBLOBs();
                updateDataPath.setId(mxId);
                updateDataPath.setMxRaw(xinPanRawData);
                updateDataPath.setMxReport(xinPanReportDataJsonObject.getString("data"));
                updateDataPath.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
                res = userMoXieDataDAO.updateByPrimaryKeySelective(updateDataPath);
                log.info("运营商认证拉取报告后日志更新:{}", userId);
                userMobileDataRecordService.updateRecordByUserIdAndMxId(userId.longValue(), mxId.longValue(), 5);
                if (res != 1)
                    return null;
                return updateDataPath;
            }else if (Objects.equals(mobileSwitch, MobileSwitch.WUHUA.getValue())) {
                log.info("开始调用五花运营商接口获取运营商数据:{},authId数据:{}", userId,crawlerid);
                String reportResult = wuHuaApi.doQueryUserReport(crawlerid);
                //String dataResult = wuHuaApi.doQueryUserReport(crawlerid);
                // TODO 上传阿里云获得存储路径，格式：商户pid+userid+爬虫id+后缀名
                //String urlName = AliOssUtils.uploadReport(null,"report/" + userId+crawlerid + "_wuhua.txt",reportResult);
                UserMoXieDataWithBLOBs updateDataPath = new UserMoXieDataWithBLOBs();
                updateDataPath.setId(mxId);
                updateDataPath.setMxRaw(reportResult);
                updateDataPath.setMxReport(reportResult);
                updateDataPath.setMxAuthStatus(MobileAuthStatus.AUTH_SUCCESS.getValue());
                res = userMoXieDataDAO.updateByPrimaryKeySelective(updateDataPath);
                log.info("运营商认证拉取报告后日志更新:{}", userId);
                userMobileDataRecordService.updateRecordByUserIdAndMxId(userId.longValue(), mxId.longValue(), 5);
                if (res != 1)
                    return null;
                return updateDataPath;
            }
        } catch (Exception e) {
            log.error("运营商报告获取异常:{}", e);
            return null;
        }
        return null;
    }

    /**
     * 更新订单初审信息的方法
     *
     * @param orderBorrow
     */
    @Transactional(timeout = 180)
    public void updateOrderTrialInfo(OrderBorrow orderBorrow) {
        Date nowTime = new Date();
        orderBorrow.setTrialTime(nowTime);
        orderBorrow.setUpdateTime(nowTime);
        //借款订单表
        int isSucc = orderBorrowMapper.updateByPrimaryKeySelective(orderBorrow);
        if (isSucc > 0) {
            byte status = orderBorrow.getStatus();
            if (Constant.TRIAL_REJECT_STATUS == status) {
                //初审驳回
                updateRiskCredit(orderBorrow, nowTime, 3);
            } else if (Constant.TRIAL_PASS_STATUS == status || Constant.TRIAL_LOAN_STATUS == status) {
                //初审通过
                updateRiskCredit(orderBorrow, nowTime, 2);
                //复审消息队列 （应该在修改完状态过后）
                Integer borrowId = orderBorrow.getId();
                //暂时换定时任务
                orderProducer.sendRisk(borrowId + "");
                log.info("----------riskQueue sendRisk end：borrowIdStr=" + borrowId);

            }
        }
        //贷超状态推送
    }

    public void updateRiskCredit(OrderBorrow orderBorrow, Date nowTime, Integer status) {
        //初审驳回
        RiskCreditUser riskCreditUser = new RiskCreditUser();
        riskCreditUser.setUserId(orderBorrow.getUserId());
        riskCreditUser.setAssetId(orderBorrow.getId());
        riskCreditUser.setRiskTime(nowTime);
        riskCreditUser.setRiskRemark(orderBorrow.getTrialRemark());
        riskCreditUser.setRiskStatus(status);
        riskCreditUserDao.updateFirstRiskStatus(riskCreditUser);
    }

    /**
     * 统计命中该规则的方法
     *
     * @param riskRuleConfig
     */
    public void countHitRule(RiskRuleConfig riskRuleConfig, byte status) {
        //只在第一次待初审时统计一次，防止重推重复统计
        if (status != 0) {
            return;
        }
        Date nowTime = new Date();
        //命中的统计
        Map<String, Object> selectCountParam = new HashMap<>();
        selectCountParam.put("countType", Constant.HIT_RULE_COUNTS_TYPE);
        selectCountParam.put("ruleId", riskRuleConfig.getId());
        selectCountParam.put("updateTime", nowTime);
        //查询risk_trial_count
        RiskTrialCount riskTrialCount = riskTrialCountDAO.findByCountType(selectCountParam);
        //如果已有
        if (null != riskTrialCount) {
            riskTrialCount.setHitCount(riskTrialCount.getHitCount() + 1);
            riskRuleConfig.setUpdateTime(nowTime);
            //更新risk_trial_count
            riskTrialCountDAO.updateByPrimaryKeySelective(riskTrialCount);
        } else {//如果没有
            RiskTrialCount newriskTrialCount = new RiskTrialCount();
            newriskTrialCount.setRuleId(riskRuleConfig.getId());
            newriskTrialCount.setHitCount(1);
            newriskTrialCount.setCountType(Constant.HIT_RULE_COUNTS_TYPE);
            newriskTrialCount.setRuleDescript(riskRuleConfig.getRuleDescript());
            newriskTrialCount.setCreateTime(nowTime);
            newriskTrialCount.setUpdateTime(nowTime);
            //新增risk_trial_count
            riskTrialCountDAO.insertSelective(newriskTrialCount);
        }
    }

    /**
     * 统计通过该规则的方法
     *
     * @param riskRuleConfig
     */
    public void countPassRule(RiskRuleConfig riskRuleConfig, byte status) {
        //只在第一次待初审时统计一次，防止重推重复统计
        if (status != 0) {
            return;
        }
        Date nowTime = new Date();
        Map<String, Object> selectCountParam = new HashMap<>();
        selectCountParam.put("countType", Constant.PASS_COUNT_TYPE);
        selectCountParam.put("ruleId", riskRuleConfig.getId());
        selectCountParam.put("updateTime", nowTime);
        //查询risk_trial_count
        RiskTrialCount riskTrialCount = riskTrialCountDAO.findByCountType(selectCountParam);
        if (null != riskTrialCount) {
            riskTrialCount.setHitCount(riskTrialCount.getHitCount() + 1);
            riskRuleConfig.setUpdateTime(nowTime);
            riskTrialCountDAO.updateByPrimaryKeySelective(riskTrialCount);
        } else {
            RiskTrialCount newriskTrialCount = new RiskTrialCount();
            newriskTrialCount.setRuleId(riskRuleConfig.getId());
            newriskTrialCount.setHitCount(1);
            newriskTrialCount.setCountType(Constant.PASS_COUNT_TYPE);
            newriskTrialCount.setRuleDescript(riskRuleConfig.getRuleDescript());
            newriskTrialCount.setCreateTime(nowTime);
            newriskTrialCount.setUpdateTime(nowTime);
            riskTrialCountDAO.insertSelective(newriskTrialCount);
        }
    }

    /**
     * 获取地址信息
     *
     * @param user
     * @return
     */
    private Present getPresent(UserInfo user) {
        String presentAddressDistinct = user.getPresentAddressDistinct();
        String presentLongitudee = user.getPresentLongitude();
        String presentLatitudea = user.getPresentLatitude();
        String city = "";
        String province = "";
        if (StringUtils.isNotBlank(presentLongitudee) && StringUtils.isNotBlank(presentLatitudea)) {
            int length = presentLongitudee.length();
            if (presentLongitudee.substring(presentLongitudee.indexOf(".") + 1, length).length() > 6) {
                presentLongitudee = presentLongitudee.substring(0, presentLongitudee.indexOf(".") + 7);
            }
            int length1 = presentLatitudea.length();
            String substring = presentLatitudea.substring(presentLatitudea.indexOf(".") + 1, length1);
            if (substring.length() > 6) {
                presentLatitudea = presentLatitudea.substring(0, presentLatitudea.indexOf(".") + 7);
            }
            Map<String, String> stringStringMap = getAdd(presentLongitudee + "," + presentLatitudea);
            if (MapUtils.isNotEmpty(stringStringMap)) {
                province = stringStringMap.get("province");
                if (stringStringMap.containsKey("city")) {
                    city = stringStringMap.get("city");
                } else {
                    city = province;
                }
            }
            if (StringUtils.isBlank(province)) {
                province = user.getProvince();
            }
            if (StringUtils.isNotBlank(city) && StringUtils.isNotBlank(province)) {
                return new Present(province, city, presentAddressDistinct);
            }
        }
        return null;
    }

    /**
     * @param location
     * @return
     */
    public Map<String, String> getAdd(String location) {
        //lat 小  log  大
        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "https://restapi.amap.com/v3/geocode/regeo?key=" + GAODE_KEY + "&location=" + location;
        String res = "";
        try {
            URL url = new URL(urlString);
            res = HttpsUtil.doGet(url.toString(), null, "utf-8", false);
        } catch (Exception e) {
            log.error("error in wapaction,and e is " + e.getMessage());
        }
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(res)) {
            com.alibaba.fastjson.JSONObject root = JSON.parseObject(res);
            if (root.containsKey("regeocode")) {
                com.alibaba.fastjson.JSONObject dataJson = root.getJSONObject("regeocode");
                if (dataJson.containsKey("formatted_address")) {
                    map.put("formatted_address", dataJson.getString("formatted_address"));
                }
                if (dataJson.containsKey("addressComponent")) {
                    com.alibaba.fastjson.JSONObject addressComponent = dataJson.getJSONObject("addressComponent");
                    if (addressComponent.containsKey("province")) {
                        String province = addressComponent.getString("province");
                        map.put("province", province);
                        String json = addressComponent.getString("city");
                        map.put("city", json);
                        if (("[]".equals(json))) {
                            map.put("city", province);
                            if (province.contains("省")) {
                                map.put("city", addressComponent.getString("district"));
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 判断规则里地区的方法
     *
     * @param data
     * @param provice
     * @param userCity
     * @return
     */
    public boolean regionJudge(JSONObject data, String provice, String userCity) {
        //规则中的省市JSON数据
        Set<String> keySet = data.keySet();
        for (String key : keySet) {
            Object ruleData = data.get(key);
            if (ruleData instanceof JSONObject) {
                JSONObject ruleDataJson = (JSONObject) ruleData;
                Set<String> ruleProvices = ruleDataJson.keySet();
                for (String ruleProvice : ruleProvices) {
                    //遍历规则中的省，是否等于用户的省
                    if (provice.contains(ruleProvice)) {
                        //通过省获取对应的城市，如果城市为空，表示整个省都拒绝
                        if ("".equals(ruleDataJson.get(ruleProvice).toString()) || ruleDataJson.get(ruleProvice) == null) {
                            return true;
                        } else {
                            //通过省获取对应的城市，并且遍历是否匹配了用户的身份证地址
                            String proCity = ruleDataJson.get(ruleProvice).toString();
                            String[] ruleCity = proCity.split("\\|");
                            for (String city : ruleCity) {
                                if (userCity.contains(city)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else if (ruleData instanceof JSONArray) {
                JSONArray dataJsonJSONArray = (JSONArray) ruleData;
                for (Object object2 : dataJsonJSONArray) {
                    regionJudge((JSONObject) object2, provice, userCity);
                }
            }

        }
        return false;
    }
}
