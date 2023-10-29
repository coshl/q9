package com.summer.web.controller.appUser;


import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.*;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.LoanRuleConfigDAO;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.service.impl.BackConfigParamsService;
import com.summer.web.controller.BaseController;
import com.summer.web.controller.indexCache.IndexCacheData;
import com.summer.dao.entity.*;
import com.summer.util.*;
import com.summer.pojo.vo.IndexOrderStateVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * APP首页信息Controller
 */
@Slf4j
@RestController
@RequestMapping("/v1.0/api/app")
public class IndexInfoController extends BaseController {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private IndexCacheData cacheIndexData;
    @Autowired
    private IUserInfoService userInfoService;
    @Resource
    private IOrderBorrowService orderBorrowService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private IOrderRepaymentService orderRepaymentService;
    @Resource
    private IUserBankCardInfoService userBankCardInfoService;
    @Resource
    private IUserMoneyRateService userMoneyRateService;
    @Resource
    private IInfoNoticeService infoNoticeService;
    @Resource
    private IRiskCreditUserService IRiskCreditUserService;
    @Resource
    private IBackConfigParamsDao iBackConfigParamsDao;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private LoanRuleConfigDAO loanRuleConfigDAO;
    @Value("${app.name}")
    private String name;


    /**
     * 获取首页数据
     *
     * @param request
     * @param
     * @return
     */
    @GetMapping("/index")
    public Object gotoIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 说明用户登录过了
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            Map<String, Object> res = findUserMoneyInfo(userInfo);
            String merchantFlag = iBackConfigParamsDao.findStrValue("appId");
            UserInfo newUserInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
            // 获取用户最大金额
            if (null != newUserInfo) {

                //查询Info_Index_Info表借款状态为1的订单
                Map<String, Object> infoIndexParam = new HashMap<>();
                infoIndexParam.put("userId", userInfo.getId());
                infoIndexParam.put("borrowStatus", Constant.EXIST_BORROW_STATUS);
                List<InfoIndexInfo> infoIndexInfos = infoIndexInfoService.selectByParam(infoIndexParam);
                //有未完成的借款订单(根据状态展示借款订单的进度)
                if (null != infoIndexInfos && infoIndexInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {

                    //查询出最近一条订单
                    OrderBorrow orderBorrow = orderBorrowService.recentlyOrder(newUserInfo.getId());
                    //订单不为空，返回订单进度信息
                    if (null != orderBorrow) {

                        Map<String, Object> resultMap = resultUserLoanStateInfo(orderBorrow, userInfo, infoIndexInfos.get(0), request, response);
                        resultMap.put("isPass", false);
                        Integer riskStatus = IRiskCreditUserService.findRiskStatus(orderBorrow.getId());
                        log.info("riskStatus={}", riskStatus);
                        //TODO 复审驳回，待人工复审 的所有订单显示成放款中 过渡
                        if (orderBorrow.getStatus() == ConstantByOrderState.THREESTATUS) {
                            resultMap.put("isPass", true);
                        }
                        resultMap.put("minAmount", res.get("minAmount"));
                        resultMap.put("maxAmount", res.get("maxAmount"));
                        resultMap.put("status", res.get("status"));
                        resultMap.put("merchantFlag", merchantFlag);

                        return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.INDEX_VISIT_SUCCESS, resultMap);
                    } else {
                        //订单为空，说明该用户没有过借款订单，返回登录过后（没有借款订单的）的数据
                        Map<String, Object> newRsultMap = cacheIndexData.cacheUserLoginIndexInfo(newUserInfo);
                        newRsultMap.put("minAmount", res.get("minAmount"));
                        newRsultMap.put("maxAmount", res.get("maxAmount"));
                        newRsultMap.put("status", res.get("status"));
                        newRsultMap.put("merchantFlag", merchantFlag);
                        return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.INDEX_VISIT_SUCCESS, newRsultMap);
                    }
                } else {//没有正在借款的订单
                    /**查询该用户对应的借款费率user_money_rate表（为保证后台贷款规则配置进行提额、降息修改后的数据实时性，用户登录过后的个人首页费率数据不做缓存）*/
                    Map<String, Object> newRsultMap = cacheIndexData.cacheUserLoginIndexInfo(newUserInfo);
                    newRsultMap.put("minAmount", res.get("minAmount"));
                    newRsultMap.put("maxAmount", res.get("maxAmount"));
                    newRsultMap.put("status", res.get("status"));
                    newRsultMap.put("merchantFlag", merchantFlag);
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.INDEX_VISIT_SUCCESS, newRsultMap);
                }
            } else {//用户信息不存在

                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.INDEX_VISIT_SUCCESS, resultDefaultData(0));
            }
        } else {//未登录，返回系统默认配置信息

            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.INDEX_VISIT_SUCCESS, resultDefaultData(0));
        }

    }

    /**
     * 返回借款订单进度的方法
     *
     * @param orderBorrow
     * @return
     */
    public Map<String, Object> resultUserLoanStateInfo(OrderBorrow orderBorrow, UserInfo userInfo, InfoIndexInfo infoIndexInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获得公共的数据（图片集合、借款记录、消息中心总数，是否更新版本）
        Map<String, Object> resultOrderStatusData = resultDefaultData(userInfo.getChannelId());
        //删除无用的key
        resultOrderStatusData.remove("loanAmount");
        resultOrderStatusData.remove("expire");
        resultOrderStatusData.remove("dayInterest");

        BigDecimal applyAmount = new BigDecimal(orderBorrow.getApplyAmount() / 100.0);
        resultOrderStatusData.put("applyAmount", applyAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        //当前时间
        Date nowTime = new Date();
        //存在借款的标记
        resultOrderStatusData.replace("indexflag", Integer.parseInt(infoIndexInfo.getBorrowStatus()));
        resultOrderStatusData.put("id", orderBorrow.getId());
        if (StringUtils.isNotBlank(orderBorrow.getCardNo()) && orderBorrow.getCardNo().contains("http")) {
            resultOrderStatusData.put("getMoney", 0);
        } else {
            resultOrderStatusData.put("getMoney", 0);
        }
        /**返回的订单集合*/
        List<IndexOrderStateVo> indexOrderStateVos = new ArrayList<>();
        IndexOrderStateVo indexOrderStateVo = new IndexOrderStateVo();
        /**申请第一条数据*/
        //申请状态
        indexOrderStateVo.setOrderState("申请提交 ");
        //申请时间
        indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getCreateTime()));
        //申请金额
        Integer applyMoney = orderBorrow.getApplyAmount() / Constant.CENT_CHANGE_DOLLAR;
        BigDecimal applyMoneys = new BigDecimal(applyMoney);
        //服务费
        BigDecimal serviceChargeApply = new BigDecimal(orderBorrow.getServiceCharge());
        BigDecimal changeFee = new BigDecimal(100);
        BigDecimal serviceCharge = serviceChargeApply.divide(changeFee, 2, BigDecimal.ROUND_HALF_UP);
        //借款利息
        BigDecimal accrualApply = new BigDecimal(orderBorrow.getAccrual());
        BigDecimal accrual = accrualApply.divide(changeFee, 2, BigDecimal.ROUND_HALF_UP);
        //申请信息
        indexOrderStateVo.setOrderInfo("申请金额:" + applyMoney + "元 " + " 服务费:" + serviceCharge.toString() + "元" + " 利息:" + accrual.toString() + "元");
        indexOrderStateVo.setSort("1");
        indexOrderStateVos.add(indexOrderStateVo);
        Integer hitRiskAllowBorrowDay = loanRuleConfigDAO.findByChannelId(Constant.CHANNELRULE_ID).getHitRiskAllowBorrowDay();
        switch (orderBorrow.getStatus()) {
            //0初审状态
            case ConstantByOrderState.ZEROSTATUS:
                /**第二条进度*/

                indexOrderStateVo = new IndexOrderStateVo();
                //审核状态
                indexOrderStateVo.setOrderState(ConstantByOrderState.BEGING_AUDIT_STATUS);
                //借款订单状态的信息
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.BEGING_AUDIT_INFO);
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getCreateTime()));
                indexOrderStateVo.setSort("2");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //1:初审驳回
            case ConstantByOrderState.FIRSTSTATUS:
                //初审被拒后饭后首页的数据
                //命中初审风控后，15天后需要重新认证后才能再借款
                //hitRiskAllowBorrowDay为0永久不能申请
                if (hitRiskAllowBorrowDay == 0) {
                    resultOrderStatusData.put("nextLoanTime", 90);
                    resultOrderStatusData.replace("indexflag", Constant.ORDER_REFUSE_INDEX_FLAG);
                    return resultOrderStatusData;
                }
                Integer intervalDays = DateUtil.daysBetween(orderBorrow.getTrialTime(), nowTime);
                if (intervalDays >= hitRiskAllowBorrowDay) {
                    infoIndexInfoService.updateUserInfoAndInfoIndexInfo(userInfo, request, response);
                    InfoIndexInfo infoIndex = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
                    if (null != infoIndex) {
                        resultOrderStatusData.replace("indexflag", Integer.parseInt(infoIndex.getBorrowStatus()));
                    } else {
                        resultOrderStatusData.replace("indexflag", 0);
                    }
                    return resultOrderStatusData;
                } else {
                    //预计允许时间借款时间
                    Date predictTime = DateUtil.addDay(orderBorrow.getTrialTime(), hitRiskAllowBorrowDay);
                    //倒计时（预计时间-当前时间）
                    Integer nextLoanTime = DateUtil.daysBetween(nowTime, predictTime);
                    resultOrderStatusData.put("nextLoanTime", nextLoanTime);
                    resultOrderStatusData.replace("indexflag", Constant.ORDER_REFUSE_INDEX_FLAG);
                    return resultOrderStatusData;
                }
                //2初审通过,
            case ConstantByOrderState.TWOSTATUS:
                /**第二条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.FIRST_AUDIT_STATUS_IS_SUCCESS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.TRIAL_PASS_INFO);
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getTrialTime()));
                indexOrderStateVo.setSort("2");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //3待人工复审
            case ConstantByOrderState.THREESTATUS:
                /**展示在第二条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.REVIEW_AUDIT_STATUS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.REVIEW_INFO);
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getTrialTime()));
                indexOrderStateVo.setSort("2");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //4机审复审拒绝
            case ConstantByOrderState.FOURSTATUS:
                //复审被拒后返回首页的数据
                //hitRiskAllowBorrowDay为0永久不能申请
                if (hitRiskAllowBorrowDay == 0) {
                    resultOrderStatusData.put("nextLoanTime", 90);
                    resultOrderStatusData.replace("indexflag", Constant.ORDER_REFUSE_INDEX_FLAG);
                    return resultOrderStatusData;
                }
                //命中复审风控后，15天后需要重新认证后才能再借款（以初审时间为准）
                Integer intervalDays2 = DateUtil.daysBetween(orderBorrow.getTrialTime(), nowTime);
                if (intervalDays2 >= hitRiskAllowBorrowDay) {
                    //把之前的认证信息清空，显示借款申请按钮
                    infoIndexInfoService.updateUserInfoAndInfoIndexInfo(userInfo, request, response);
                    InfoIndexInfo infoIndex = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
                    if (null != infoIndex) {
                        resultOrderStatusData.replace("indexflag", Integer.parseInt(infoIndex.getBorrowStatus()));
                    } else {
                        resultOrderStatusData.replace("indexflag", 0);
                    }
                    return resultOrderStatusData;
                } else {
                    //预计允许时间借款时间
                    Date predictTime = DateUtil.addDay(orderBorrow.getTrialTime(), hitRiskAllowBorrowDay);
                    //倒计时（预计时间-当前时间）
                    Integer nextLoanTime = DateUtil.daysBetween(nowTime, predictTime);
                    resultOrderStatusData.put("nextLoanTime", nextLoanTime);
                    resultOrderStatusData.replace("indexflag", Constant.ORDER_REFUSE_INDEX_FLAG);
                    return resultOrderStatusData;
                }
                //5 人工复审核拒绝(v1.0.3版本)
            case ConstantByOrderState.FIVESTATUS:
                /**展示在第二条进度*/
                   /* indexOrderStateVo = new IndexOrderStateVo();
                    indexOrderStateVo.setOrderState(ConstantByOrderState.REVIEW_AUDIT_STATUS_IS_FAIL);
                    indexOrderStateVo.setOrderInfo(ConstantByOrderState.REVIEW_PASS_INFO);
                    //复审通过时间
                    indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getReviewTime()));
                    indexOrderStateVo.setSort("2");
                    indexOrderStateVos.add(indexOrderStateVo);*/
                //hitRiskAllowBorrowDay为0永久不能申请
                if (hitRiskAllowBorrowDay == 0) {
                    resultOrderStatusData.put("nextLoanTime", 90);
                    resultOrderStatusData.replace("indexflag", Constant.ORDER_REFUSE_INDEX_FLAG);
                    return resultOrderStatusData;
                }
                //复审被拒后返回首页的数据
                //命中复审风控后，15天后需要重新认证后才能再借款（以初审时间为准）
                Integer intervalDays3 = DateUtil.daysBetween(orderBorrow.getTrialTime(), nowTime);
                if (intervalDays3 >= hitRiskAllowBorrowDay) {
                    //把之前的认证信息清空，显示借款申请按钮
                    infoIndexInfoService.updateUserInfoAndInfoIndexInfo(userInfo, request, response);
                    InfoIndexInfo infoIndex = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
                    if (null != infoIndex) {
                        resultOrderStatusData.replace("indexflag", Integer.parseInt(infoIndex.getBorrowStatus()));
                    } else {
                        resultOrderStatusData.replace("indexflag", 0);
                    }
                    return resultOrderStatusData;
                } else {
                    //预计允许时间借款时间
                    Date predictTime = DateUtil.addDay(orderBorrow.getTrialTime(), hitRiskAllowBorrowDay);
                    //倒计时（预计时间-当前时间）
                    Integer nextLoanTime = DateUtil.daysBetween(nowTime, predictTime);
                    resultOrderStatusData.put("nextLoanTime", nextLoanTime);
                    resultOrderStatusData.replace("indexflag", Constant.ORDER_REFUSE_INDEX_FLAG);
                    return resultOrderStatusData;
                }
                //6:放款中
            case ConstantByOrderState.SIXSTATUS:
                /**展示第二条进度*/
                putTrialBy(indexOrderStateVos, orderBorrow);
                /**第三条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.MAKEING_LOANS_STATUS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.REMITTANCE_INFO);
                //放款审核通过时间
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getLoanReviewTime()));
                indexOrderStateVo.setSort("3");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //7:放款失败
            case ConstantByOrderState.SEVENSTATUS:
                /**展示在第二条进度 通过成功的时候的状态*/
                putTrialBy(indexOrderStateVos, orderBorrow);
                /**第三条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.MAKE_LOANS_STATUS_IS_BAD);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.REMITTANCE_FAILD_INFO);
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getLoanReviewTime()));
                indexOrderStateVo.setSort("3");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //8已放款，待还款
            case ConstantByOrderState.EIGHTSTATUS:
                /**第二条进度*/
                putTrialBy(indexOrderStateVos, orderBorrow);
                /**第三条进度*/
                putReview(indexOrderStateVos, orderBorrow);
                /**第四条进度*/
                indexOrderStateVo = new IndexOrderStateVo();

                indexOrderStateVo.setOrderState(ConstantByOrderState.MAKE_LOANS_STATUS_IS_SUCCESS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.REMITTANCE_SUCC_INFO);
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getLoanReviewTime()));
                //距离还款倒计时：预计还款时间-当前时间 （通过借款订单id查询还款表order_repayment）
                indexOrderStateVo.setNextLoanTime(repaymentCountDown(userInfo.getId(), orderBorrow.getId(), resultOrderStatusData));
                indexOrderStateVo.setSort("4");
                indexOrderStateVos.add(indexOrderStateVo);


                break;
            //9:部分还款
            case ConstantByOrderState.NINESTATUS:
                /**第二条条进度*/
                putTrialBy(indexOrderStateVos, orderBorrow);
                /**第三条条进度*/
                putReview(indexOrderStateVos, orderBorrow);
                /**第四条条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.MAKE_LOANS_STATUS_IS_SUCCESS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.REMITTANCE_SUCC_INFO);

                //距离还款倒计时：预计还款时间-当前时间 （通过借款订单id查询还款表order_repayment）
                indexOrderStateVo.setNextLoanTime(repaymentCountDown(userInfo.getId(), orderBorrow.getId(), resultOrderStatusData));

                /**上次还款时间*/
                //查询还款表
                OrderRepayment orderRepaymentTime = findOrderRepayment(userInfo.getId(), orderBorrow.getId());
                indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmd(orderRepaymentTime.getPaidTime()));
                indexOrderStateVo.setSort("4");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //11:已逾期
            case ConstantByOrderState.ELEVENSTATUS:
                /**第二条进度*/
                putTrialBy(indexOrderStateVos, orderBorrow);
                /**第三条进度*/
                putReview(indexOrderStateVos, orderBorrow);
                /**第四条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.OVERDUE_STATUS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.OVERDUE_INFO);
                //通过用户id和借款订单id查询逾期天数
                Integer lateDay = getLateDay(userInfo.getId(), orderBorrow.getId(), resultOrderStatusData);
                indexOrderStateVo.setNextLoanTime(lateDay.toString());
                indexOrderStateVo.setSort("4");
                indexOrderStateVo.setOrderTime("");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            //12:已坏账（属于逾期天数大于规定时间未还款的）
            case ConstantByOrderState.TWELVESTATUS:
                /**第二条进度*/
                putTrialBy(indexOrderStateVos, orderBorrow);
                /**第三条进度*/
                putReview(indexOrderStateVos, orderBorrow);
                /**第四条进度*/
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.OVERDUE_STATUS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.OVERDUE_INFO);
                //通过用户id和借款订单id查询逾期天数
                Integer lateDay2 = getLateDay(userInfo.getId(), orderBorrow.getId(), resultOrderStatusData);
                indexOrderStateVo.setNextLoanTime(lateDay2.toString());
                indexOrderStateVo.setSort("4");
                indexOrderStateVo.setOrderTime("");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
            default://否则默认显示审核中
                indexOrderStateVo = new IndexOrderStateVo();
                indexOrderStateVo.setOrderState(ConstantByOrderState.BEGING_AUDIT_STATUS);
                indexOrderStateVo.setOrderInfo(ConstantByOrderState.BEGING_AUDIT_INFO);
                indexOrderStateVo.setSort("2");
                indexOrderStateVos.add(indexOrderStateVo);
                break;
        }
        resultOrderStatusData.put("orderStateList", indexOrderStateVos);
        String chatUrl = iBackConfigParamsDao.findStrValue("app_chatUrl");
        resultOrderStatusData.put("chatUrl", chatUrl);
        resultOrderStatusData.put("faceId", null);
        resultOrderStatusData.put("secretId", null);
        return resultOrderStatusData;
    }


    /**
     * 返回系统默认数据
     *
     * @return
     */
    public Map<String, Object> resultDefaultData(Integer channelId) throws Exception {

        String indexJson = cacheIndexData.cacheIndexData(channelId);
        /*if (redisUtil.hasKey(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY)) {
            //缓存里有就直接取缓存里的数据
            indexJson = redisUtil.get(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY);
        } else {//没有去查数据库并同时存缓存
            indexJson = cacheIndexData.cacheIndexData(channelId);
            redisUtil.set(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY, indexJson);
        }*/

        Map<String, Object> resultMap = JSONObject.parseObject(indexJson);
        String chatUrl = iBackConfigParamsDao.findStrValue("app_chatUrl");
        String merchantFlag = infoIndexInfoService.selectMerchantFlag();
        resultMap.put("chatUrl", chatUrl);
        resultMap.put("minAmount", 10000);
        resultMap.put("maxAmount", 50000);
        resultMap.put("merchantFlag", merchantFlag);
        resultMap.put("status", 0);
        resultMap.put("faceId", null);
        resultMap.put("secretId", null);
        return resultMap;
    }

    /**
     * put初审通过的状态信息
     */
    public void putTrialBy(List<IndexOrderStateVo> indexOrderStateVos, OrderBorrow orderBorrow) {
        /**第二条进度*/
        IndexOrderStateVo indexOrderStateVo = new IndexOrderStateVo();
        indexOrderStateVo.setOrderState(ConstantByOrderState.REVIEW_AUDIT_STATUS_IS_SUCCESS);
        indexOrderStateVo.setOrderInfo(ConstantByOrderState.TRIAL_PASS_INFO);
        //初审通过时间
        indexOrderStateVo.setOrderTime(DateUtil.formatTimeYmdHm(orderBorrow.getTrialTime()));
        indexOrderStateVo.setSort("2");
        indexOrderStateVos.add(indexOrderStateVo);

    }

    /**
     * 打款成功状态
     *
     * @param indexOrderStateVos
     * @param orderBorrow
     * @return
     */
    public void putReview(List<IndexOrderStateVo> indexOrderStateVos, OrderBorrow orderBorrow) {
        /**第三条条进度*/
        IndexOrderStateVo indexOrderStateVo = new IndexOrderStateVo();
        indexOrderStateVo.setOrderState(ConstantByOrderState.MAKE_LOANS_STATUS_IS_SUCC);

        indexOrderStateVo.setOrderInfo(ConstantByOrderState.MAKE_LOANS_STATUS_SUCC_INFO + bankInfo(orderBorrow.getUserId()));
        //放款时间
        String loanTime = loanTime(orderBorrow.getUserId(), orderBorrow.getId());
        indexOrderStateVo.setOrderTime(loanTime);
        indexOrderStateVo.setSort("3");
        indexOrderStateVos.add(indexOrderStateVo);
    }

    /**
     * 返回银行卡信息
     *
     * @param userId
     * @return
     */
    public String bankInfo(Integer userId) {
        //根据用户id和银行卡有效状态查询银行卡（user_card_info）
        Map<String, Object> findCardParam = new HashMap<String, Object>();
        findCardParam.put("userId", userId);
        findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
        findCardParam.put("limit", 1);
        List<UserCardInfo> userCardByUserId = userBankCardInfoService.findUserCardByUserId(findCardParam);
        if (null != userCardByUserId && userCardByUserId.size() > 0) {
            UserCardInfo userBankCardInfo = userCardByUserId.get(0);
            return userBankCardInfo.getBankName() + "(" + StringUtil.subString(userBankCardInfo.getCardNo(), 4) + ")";
        }
        return "";
    }


    /**
     * 还款倒计时
     *
     * @param userId
     * @return
     */
    public String repaymentCountDown(Integer userId, Integer orderId, Map<String, Object> resultOrderStatusData) {
        OrderRepayment orderRepayment = findOrderRepayment(userId, orderId);
        if (null != orderRepayment && null != orderRepayment.getRepaymentTime()) {
            Integer countDown = DateUtil.daysBetween(new Date(), orderRepayment.getRepaymentTime());
            resultOrderStatusData.put("orderId", orderRepayment.getId());
            return countDown.toString();

        }
        return "";
    }

    /**
     * 上次还款时间
     *
     * @param userId
     * @return
     */
    public OrderRepayment findOrderRepayment(Integer userId, Integer orderId) {
        Map<String, Object> orderParam = new HashMap<>();
        orderParam.put("userId", userId);
        orderParam.put("borrowId", orderId);
        List<OrderRepayment> stayOrderRepayments = orderRepaymentService.findOrderPrepaymentByParam(orderParam);
        if (null != stayOrderRepayments && stayOrderRepayments.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            return stayOrderRepayments.get(0);
        }
        return null;
    }

    /**
     * 获取逾期天数
     */
    public Integer getLateDay(Integer userId, Integer orderId, Map<String, Object> resultOrderStatusData) {
        OrderRepayment orderRepayment = findOrderRepayment(userId, orderId);
        if (null != orderRepayment) {
            resultOrderStatusData.put("orderId", orderRepayment.getId());
            return orderRepayment.getLateDay();
        }
        return null;
    }

    /**
     * 放款时间
     *
     * @param userId
     * @param orderId
     * @return
     */
    public String loanTime(Integer userId, Integer orderId) {
        //查询还款表
        OrderRepayment orderRepayment = findOrderRepayment(userId, orderId);
        //放款时间=还款表创建的时间
        if (null != orderRepayment) {
            return DateUtil.formatTimeYmdHm(orderRepayment.getCreateTime());
        }
        return "";
    }


    /**
     * APP我的页面数据
     *
     * @return
     */
    @GetMapping("/myIndex")
    public String MyIndex(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {

            UserInfo newUser = userInfoService.selectByPrimaryKey(userInfo.getId());
            Map<String, Object> resultMap = new HashMap<>();
            if (null != newUser) {
                resultMap.put("phone", "尾号" + StringUtil.subString(newUser.getPhone(), 4));
                UserMoneyRate byUserId = userMoneyRateService.findByUserId(newUser.getId());
                if (null != byUserId) {
                    //总额度
                    resultMap.put("amountMax", byUserId.getMaxAmount() / Constant.CENT_CHANGE_DOLLAR);
                } else {
                    //总额度
                    resultMap.put("amountMax", Constant.ALLOW_TOTAL_MONEY);
                }
                //可借金额
                InfoIndexInfo infoIndexInfo1 = infoIndexInfoService.selectByPrimaryKey(newUser.getId());
                resultMap.put("amountAvailable", 0);
                if (null != infoIndexInfo1) {
                    String borrowStatus = infoIndexInfo1.getBorrowStatus();
                    int status = Integer.parseInt(borrowStatus);
                    //存在订单
                    if (1 == status) {
                        //查询最近一条借款订单
                        OrderBorrow orderBorrow = orderBorrowService.recentlyOrder(newUser.getId());
                        if (null != orderBorrow) {
                            byte orderStatus = orderBorrow.getStatus();
                            //如果已订单在（已放款、部分还款、已逾期、或者已坏账显示申请金额）
                            if (ConstantByOrderState.EIGHTSTATUS == orderStatus || ConstantByOrderState.ELEVENSTATUS == orderStatus || ConstantByOrderState.TWELVESTATUS == orderStatus || ConstantByOrderState.NINESTATUS == orderStatus) {
                                resultMap.replace("amountAvailable", orderBorrow.getApplyAmount() / Constant.CENT_CHANGE_DOLLAR);
                            }
                        }
                    }
                }
                //查询Info_Index_Info
                Map<String, Object> infoIndexParam = new HashMap<>();
                infoIndexParam.put("userId", userInfo.getId());
                List<InfoIndexInfo> infoIndexInfos = infoIndexInfoService.selectByParam(infoIndexParam);
                if (null != infoIndexInfos && infoIndexInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    InfoIndexInfo infoIndexInfo = infoIndexInfos.get(0);
                    if (Constant.INFO_INDEX_UNAUTH == infoIndexInfo.getAuthInfo() || Constant.INFO_INDEX_UNAUTH == infoIndexInfo.getAuthContacts() || Constant.INFO_INDEX_UNAUTH == infoIndexInfo.getAuthMobile()) {
                        resultMap.put("myFlag", Constant.INDEX_BASE_UNAUTH);
                    } else {
                        //银行卡未绑定跳银行卡绑定页面
                        if (Constant.INFO_INDEX_UNAUTH == infoIndexInfo.getAuthBank()) {
                            resultMap.put("myFlag", Constant.INDEX_BANK_UNAUTH);
                        } else {
                            //银行卡绑定了 ，跳到获取银行卡页面
                            resultMap.put("myFlag", Constant.INDEX_BANK_AUTH);
                        }
                    }
                } else {
                    resultMap.put("myFlag", Constant.INDEX_BASE_UNAUTH);
                }
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 查询弹框公告
     *
     * @return
     */
    @PostMapping("/getNotice")
    public String getNotice(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 查询弹框公告
//        map.put("status", Constant.INDEX_NOTICE_STATUS);
        map.put("noticeType", Constant.INDEX_NOTICE_BOX_TYPE);
        List<InfoNotice> notice = infoNoticeService.selectNotice(map);
        return CallBackResult.returnJson(notice);
    }


    public Map<String, Object> findUserMoneyInfo(UserInfo userInfo) {

        Map<String, Object> resultMap = new HashMap<>();
        Integer status = loanRuleConfigService.findOrderStatus(userInfo.getId());
        if (null == status) {
            status = 0;
        }
        if (status == 1) {
            Integer minAmount = loanRuleConfigService.findMinLoanMoney(userInfo.getId());
            Integer maxAmount = loanRuleConfigService.findMaxLoanMoney(userInfo.getId());
            resultMap.put("minAmount", minAmount);
            resultMap.put("maxAmount", maxAmount / 100);
        } else {
            resultMap.put("minAmount", 20000);
            resultMap.put("maxAmount", 20000);
        }
        resultMap.put("status", status);
        return resultMap;
    }


    @PostMapping("/kefu")
    public String kefu(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        /*BackConfigParamsVo kefuNameVo =  iBackConfigParamsDao.findBySysKey("sms.service.title");
        BackConfigParamsVo kefuUrlVo =  iBackConfigParamsDao.findBySysKey("wait_url");*/
        //String kefuName =  iBackConfigParamsDao.findStrValue("sms.service.title");;

//        String kefuUrl = iBackConfigParamsDao.findStrValue("wait_url");
//        ;
//        //http://47.74.48.135:8077/chatIndex?kefu_id=kefu2&refer=
//        kefuUrl = kefuUrl + name;
//        if (userInfo != null) {
//            kefuUrl = kefuUrl + userInfo.getPhone() + userInfo.getRealName();
//        }
        //修改URL数据源
        String kefuUrl = iBackConfigParamsDao.findStrValue("app_chatUrl");
        if (userInfo != null) {
            String nameString = StringUtils.isBlank(userInfo.getRealName()) ? "未实名用户" : userInfo.getRealName();
            kefuUrl = kefuUrl + "&visiter_name=" + nameString + "&visiter_mobile=" + userInfo.getPhone();
        }

        /*Map data = new HashMap();
        data.put("kefuUrl",kefuUrl);*/
        return CallBackResult.returnJson(kefuUrl);
    }

    @PostMapping("/kefu2")
    public String kefu2(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        BackConfigParams wait_url = iBackConfigParamsDao.configParams("wait_url");
        String kefuUrl = wait_url.getSysValue();
        if (userInfo != null) {
            //https://app.test.com/chat/text/chat_0h8I0g.html? extradata
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vipid", userInfo.getPhone());
            jsonObject.put("name", StringUtils.isBlank(userInfo.getRealName()) ? userInfo.getPhone() : userInfo.getRealName());
            jsonObject.put("phone", userInfo.getPhone());

//            String message = "{\"vipid\":\"" + userInfo.getPhone() + "\",\"name\":\"" + message1 + "\",\"phone\":\"" + userInfo.getPhone() + "\"}";
            String key = wait_url.getSysValueBig();
            //加解密用到的密钥
            String encryptMsg = KfUtils.encrypt(key, jsonObject.toString());
            kefuUrl = wait_url.getSysValue() + "?extradata=" + encryptMsg;
        }


        return CallBackResult.returnJson(kefuUrl);
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vipid", "1");
        jsonObject.put("name", "2");
        jsonObject.put("phone", "userInfo.getPhone()");
        String message = jsonObject.toString();
        String encryptMsg = KfUtils.encrypt("e67P7Nlm3MXHs41Y4wsa8olF5tPq2E21", message);
        String kefuUrl = "https://d1c8w9.com/chat/text/chat_0UkR1o.html" + "?extradata=" + encryptMsg;
        System.out.println(kefuUrl);
    }
}
