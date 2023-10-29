package com.summer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.summer.api.service.*;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.IRiskCreditUserDao;
import com.summer.dao.mapper.OrderBorrowMapper;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.enums.*;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.queue.QueueConstans;
import com.summer.service.IRiskFkContentService;
import com.summer.service.mq.OrderProducer;
import com.summer.service.yys.BoxApi;
import com.summer.util.Constant;
import com.summer.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * 类描述： 此类的所有更新方法，都会先调用insert方法.insert方法内部逻辑是，先根据userId查询数据库中是否有该记录<br>
 * 如果没有该记录则插入一条仅包含userId、用户名、身份证号码、性别、年龄的数据 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-12-12 下午05:14:12 <br>
 */
@Service
@Slf4j
public class RiskCreditUserService implements IRiskCreditUserService {
    @Resource
    private IRiskCreditUserDao riskCreditUserDao;
    @Resource
    private OrderProducer orderProducer;
    @Resource
    private IInfoIndexService infoIndexService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private RiskFkContentService riskFkContentService;
    @Resource
    private RiskGongzhaiContentService riskGongZhaiContentService;
    @Resource
    private PlateformChannelService plateformChannelService;
    @Resource
    private UserShortMessageService userShortMessageService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IRiskFkContentService iRiskFkContentService;

    /**
     * 复审机审
     */
    @JmsListener(destination = QueueConstans.riskQueue)
    public void reviewMq(String borrowIdStr) {
        log.info("----------riskQueue reviewMq start：borrowIdStr=" + borrowIdStr);
        try {
            /**
             *         rcu.id as id,
             *         rcu.user_id as userId,
             *         rcu.asset_id as assetId,
             *         rcu.user_name as userName,
             *         rcu.card_num as cardNum,
             *         rcu.user_phone as userPhone,
             *         abo.out_trade_no as outOrderId,
             *         abo.create_time as createTime
             * */
            HashMap<String, Object> params = riskCreditUserDao.findForRisk(borrowIdStr);
            log.info("----------riskQueue reviewMq start：params=" + params);
            if (params != null) {
                review((Integer) params.get("id"), (Integer) params.get("userId"), (Integer) params.get("assetId"));
            }
        } catch (Exception e) {
            log.error("reviewMq error ", e);
        }
    }


    /**
     * 复审主要逻辑
     *
     * @param rcuId    征信表主键ID，更新征信表使用
     * @param userId   用户表主键ID(更新用户表专用)
     * @param borrowId 借款信息表的主键ID(订单表id)
     */
    @Override
    public void review(Integer rcuId, Integer userId, Integer borrowId) {
        log.info("===============================复审主要逻辑开始");
        String lockKey = Constant.RISK_RE_PUSH_ + borrowId;
        try {
            UserInfo user = userInfoMapper.selectByPrimaryKey(userId);
            if (ObjectUtils.isNull(rcuId, userId, borrowId)) {
                throw new IllegalArgumentException("复审必要参数为空");
            }

            BackConfigParamsVo oldCustomerCheck =  backConfigParamsDao.findBySysKey(BackConfigParamsEnum.OLD_CUSTOMER_CHECK.getKey());
            if (Objects.equals(oldCustomerCheck.getStrValue(),YesOrNo.No.getStrValue()))//不需要过风控
            {
                if (user.getCustomerType() == YesOrNo.YES.getValue().byteValue())
                {
                    log.info("老客不需要过风控");
                    updateReviewResult(rcuId, borrowId, 0,
                            CreditLevelEnum.MACHINE_AUDIT_PASS.getValue(),
                            RiskStatusEnum.RISK_STATUS_PASS.getValue(),
                            OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getValue(),
                            "老客通过机审,需人工复审" + OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getDesc());
                    return;
                }
            }


            /** 复审机审规则过滤 start */
            log.info("执行复审过滤规则:{}", "[同一身份证注册多个手机号]");
            // 同一身份证限制
            List<UserInfo> userInfos = userInfoMapper.selectByIdCard(user.getIdCard());
            if (CollectionUtils.isNotEmpty(userInfos)) {
                for (UserInfo userInfo : userInfos) {
                    if (!StringUtils.equals(user.getPhone(), userInfo.getPhone())) {
                        log.info("复审过滤规则:{}", "[同一身份证注册多个手机号],命中");
                        // 同一身份证注册多个手机号 复审拒绝
                        updateReviewResult(rcuId, borrowId, 0,
                                CreditLevelEnum.MACHINE_AUDIT_REJECTION.getValue(),
                                RiskStatusEnum.RISK_STATUS_REJUCT.getValue(),
                                OrderBorrowStatusEnum.RE_MACHINE_AUDIT_REJECT.getValue(),
                                "同一身份证注册多个手机号," + OrderBorrowStatusEnum.RE_MACHINE_AUDIT_REJECT.getDesc());
                        return;
                    }
                }
            }

            log.info("执行复审过滤规则:{}", "[风控分阈值过滤]");
            // 获取全局预设指迷分
            BackConfigParamsVo scoreGateway = backConfigParamsDao.findBySysKey(BackConfigParamsEnum.SCORE_GATEWAY.getKey());
            // 获取渠道预设指迷分
            PlateformChannel plateformChannel = plateformChannelService.selectByPrimaryKey(user.getChannelId());
            // 获取用户指迷分
            BigDecimal riskScore = riskFkContentService.getRiskScoreByUserId(userId).getData();
            if (Objects.nonNull(scoreGateway) || Objects.nonNull(plateformChannel)) {
                if (Objects.isNull(riskScore)) {
                    log.info("用户ID:[{}];没有生成风控分,返回待机审",userId);
                    RiskFkContent riskFkContent = iRiskFkContentService.getFirstByUserIdAndType(userId,Integer.valueOf(MobileSwitch.BOX.getValue()));
                    if(!riskFkContent.getTaskId().isEmpty()){
                        log.info("用户ID:[{}];开始获取BOX风控分",userId);
                        String score = BoxApi.boxQueryScore(riskFkContent.getTaskId());
                        if(StringUtils.isNotBlank(score)){
                            riskFkContent.setScore(BigDecimal.valueOf(Long.parseLong(score)));
                            iRiskFkContentService.updateById(riskFkContent);
                            riskScore = BigDecimal.valueOf(Long.parseLong(score));
                        }
                    }else {
                        return;
                    }
                     /*updateReviewResult(rcuId, borrowId, 0,
                            CreditLevelEnum.MACHINE_AUDIT_REJECTION.getValue(),
                            RiskStatusEnum.RISK_STATUS_REJUCT.getValue(),
                            OrderBorrowStatusEnum.RE_MACHINE_AUDIT_REJECT.getValue(),
                            "指迷分未生成," + OrderBorrowStatusEnum.RE_MACHINE_AUDIT_REJECT.getDesc());*/
                }else {

                }
                String logStr = "风控分";
                // 渠道分
                String channelScore = plateformChannel.getRiskScore();
                // 全局分
                String score = scoreGateway.getStrValue();
                if (StringUtils.isNoneEmpty(channelScore)) {
                    score = channelScore;
                    logStr = "渠道风控分";
                }
                if (riskScore.doubleValue() < Double.valueOf(score)) {
                    log.info("用户ID:[{}];{}{},低于{},复审驳回", userId, logStr, riskScore, score);
                    updateReviewResult(rcuId, borrowId, 0,
                            CreditLevelEnum.MACHINE_AUDIT_REJECTION.getValue(),
                            RiskStatusEnum.RISK_STATUS_REJUCT.getValue(),
                            OrderBorrowStatusEnum.RE_MACHINE_AUDIT_REJECT.getValue(),
                            logStr + "低于[" + score + "]," + OrderBorrowStatusEnum.RE_MACHINE_AUDIT_REJECT.getDesc());
                    return;
                }
            }
            /** 复审机审规则过滤 end */
            // 命中待人工审核的规则
            // 短信中包含"逾期"关键字
            Boolean smsYqFlag = Boolean.FALSE;
            List<UserShortMessage> userShortMessageList = userShortMessageService.findByUserId(userId);
            for (UserShortMessage userShortMessage : userShortMessageList) {
                if (userShortMessage.getMessageContent().contains("逾期")) {
                    smsYqFlag = Boolean.TRUE;
                    break;
                }
            }
            log.info("短信中是否含有逾期:{}", smsYqFlag);
            // TODO 判断共债中是否有“逾期”
            // 机审通过是否需要人工审核控制 1-待人工审核;0-直接放款
            BackConfigParamsVo reviewJs = backConfigParamsDao.findBySysKey(BackConfigParamsEnum.REVIEW_JS.getKey());
            // 聚财宝审核结果更新逻辑
                BackConfigParamsVo autoScoreGateway = backConfigParamsDao.findBySysKey(BackConfigParamsEnum.AUTO_SCORE_GATEWAY.getKey());
                Boolean scoreResult = Boolean.FALSE;
                if (Objects.nonNull(autoScoreGateway.getSysValue())) {
                    // 用户指迷分大于自动放款预设分
                    scoreResult = riskScore.doubleValue() >= autoScoreGateway.getSysValue();
                }
                if (Objects.equals(YesOrNo.No.getValue(), reviewJs.getSysValue()) && scoreResult) {
                    log.info("通过机审且用户分,大于{};borrowId:{}", autoScoreGateway.getSysValue(), borrowId);
                    if (smsYqFlag) {
                        log.info("短信列表中包含关键词[逾期];borrowId:{}", borrowId);
                        updateReviewResult(rcuId, borrowId, 0,
                                CreditLevelEnum.MACHINE_AUDIT_PASS.getValue(),
                                RiskStatusEnum.RISK_STATUS_PASS.getValue(),
                                OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getValue(),
                                "短信列表中包含关键词[逾期]," + OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getDesc());
                    } else {
                        updateReviewResult(rcuId, borrowId, 0,
                                CreditLevelEnum.MACHINE_AUDIT_PASS.getValue(),
                                RiskStatusEnum.RISK_STATUS_PASS.getValue(),
                                OrderBorrowStatusEnum.SEND_LOAN_RUN.getValue(),
                                "通过机审且用户分,大于" + autoScoreGateway.getSysValue() + "," + OrderBorrowStatusEnum.SEND_LOAN_RUN.getDesc());
                    }
                } else if (Objects.equals(YesOrNo.No.getValue(), reviewJs.getSysValue())) {
                    // 这里只需要做是否开启自动放款开关判断，以上如果不超过渠道分会拦截，过了的到这都是已经达到渠道分值的
                    // 如果自动放款关，且小于自动放款峰值，判断是否有短信逾期，有就人工审核，没有自动放款
                    if (smsYqFlag) {
                        log.info("短信列表中包含关键词[逾期];borrowId:{}", borrowId);
                        updateReviewResult(rcuId, borrowId, 0,
                                CreditLevelEnum.MACHINE_AUDIT_PASS.getValue(),
                                RiskStatusEnum.RISK_STATUS_PASS.getValue(),
                                OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getValue(),
                                "短信列表中包含关键词[逾期]," + OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getDesc());
                    } else {
                        log.info("通过机审,且没有逾期记录;borrowId:{}", borrowId);
                        // 人工复审开关关闭，自动放款
                        updateReviewResult(rcuId, borrowId, 0,
                                CreditLevelEnum.MACHINE_AUDIT_PASS.getValue(),
                                RiskStatusEnum.RISK_STATUS_PASS.getValue(),
                                OrderBorrowStatusEnum.SEND_LOAN_RUN.getValue(),
                                "通过机审,且没有逾期记录," + OrderBorrowStatusEnum.SEND_LOAN_RUN.getDesc());
                    }
                } else {
                    log.info("通过机审;borrowId:{}", borrowId);
                    updateReviewResult(rcuId, borrowId, 0,
                            CreditLevelEnum.MACHINE_AUDIT_PASS.getValue(),
                            RiskStatusEnum.RISK_STATUS_PASS.getValue(),
                            OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getValue(),
                            "通过机审," + OrderBorrowStatusEnum.RE_MANUAL_AUDIT_WAIT.getDesc());
                }

            try {
                log.info("机审通过，生成共债报告:{}", userId);
                // 生成共债报告
                /*String zmgz = backConfigParamsDao.findStrValue("zmgz");//之谜共债
                if (!Objects.equals(zmgz, YesOrNo.No.getStrValue()))
                {
                    log.info("调用之谜共债");
                    riskGongZhaiContentService.initGongzhaiReport(userId);
                }*/
            } catch (Exception e) {
                log.error("生成共债报告失败:{}", e);
            }
        } finally {
            // 释放锁
            redisUtil.del(lockKey);
        }
    }

    /**
     * @param rcuId 更新复审结果
     */
    private void updateReviewResult(Integer rcuId, Integer borrowId, Integer loanReviewUserId, Integer creditLevel, Integer riskStatus, Integer status, String remark) {
        // 更新订单状态
        orderBorrowMapper.updateAssetsSuc(borrowId, loanReviewUserId, status, creditLevel, remark);//borrow表
        // 更新征信表状态
        riskCreditUserDao.updateRiskStatus(new RiskCreditUser(rcuId, riskStatus, remark));
        if (Objects.equals(status, OrderBorrowStatusEnum.SEND_LOAN_RUN.getValue())) {
            // 放款
            orderProducer.sendLoan(borrowId + "", null);
        }
    }

    @Override
    public int updateZmScore(RiskCreditUser riskCreditUser) {
        return riskCreditUserDao.updateUserZmScore(riskCreditUser);
    }

    @Override
    public int updateTdDetail(RiskCreditUser riskCreditUser) {
        return riskCreditUserDao.updateTdDetail(riskCreditUser);
    }

    @Override
    public int updateBqs(RiskCreditUser riskCreditUser) {
        return riskCreditUserDao.updateBqs(riskCreditUser);
    }

    @Override
    public int updateJy(RiskCreditUser riskCreditUser) {
        return riskCreditUserDao.updateJy(riskCreditUser);
    }

    @Override
    public int updateMg(RiskCreditUser riskCreditUser) {
        return riskCreditUserDao.updateMg(riskCreditUser);
    }

    @Override
    public int updateJxl(RiskCreditUser riskCreditUser) {
        // update by xbc 20181211
        riskCreditUserDao.updateJxlAndDays(riskCreditUser);
        return riskCreditUserDao.updateUserJxl(riskCreditUser);
    }

    @Override
    public List<RiskRuleProperty> findRuleProperty(HashMap<String, Object> params) {
        return riskCreditUserDao.findRuleProperty(params);
    }

    @Override
    public List<RiskCreditUser> findByUserId(Integer userId) {
        return riskCreditUserDao.findByUserId(userId);
    }

    @Override
    public Integer findRiskStatus(Integer orderId) {
        RiskCreditUser riskCreditUser = riskCreditUserDao.findByBorrowId(orderId);
        if (riskCreditUser == null)
            return 0;
        return riskCreditUser.getRiskStatus();
    }
}
