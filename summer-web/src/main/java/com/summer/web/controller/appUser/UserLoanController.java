package com.summer.web.controller.appUser;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.*;
import com.summer.enums.YesOrNo;
import com.summer.pojo.vo.IncreaseMoneyConfigVo;
import com.summer.queue.QueueConstans;
import com.summer.service.impl.IncreaseMoneyConfigService;
import com.summer.service.impl.black.JuGuangApi;
import com.summer.service.impl.black.JuGuangEnum;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.*;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.mq.OrderProducer;
import com.summer.util.*;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户借款Controller
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/v1.0/api/app")
public class UserLoanController extends BaseController {

    @Autowired
    private IUserInfoService userInfoService;
    @Resource
    private IOrderBorrowService orderBorrowService;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private IUserBankCardInfoService userBankCardInfoService;
    @Resource
    private IUserMoneyRateService userMoneyRateService;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private IAuthRuleConfigService authRuleConfigService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private IOrderRepaymentService orderRepaymentService;
    @Resource
    private OrderProducer orderProducer;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private UserAppSoftwareDAO userAppSoftwareDAO;
    @Resource
    IncreaseMoneyConfigService increaseMoneyConfigService;
    @Autowired
    private JuGuangApi juGuangApi;

    /**
     * to 借款《1》（排除不能借款的状态，获得能够借款的数据，再把允许借款数据带入确认借款的界面）
     * （需求有变：2019-03-21：URL不使用了 但是方法在借款处要使用到）
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/apply/{applyMoney}/{period}")
    public String confirmLoan(HttpServletRequest request,
                              @NotNull(message = "借款金额不能为空")
                              @PathVariable("applyMoney")
                              Integer applyMoney,
                              @NotNull(message = "借款期限不能为空")
                              @PathVariable("period")
                              Integer period) throws Exception {
        UserInfo userInfo = redisUser(request);
        return doConfirm(applyMoney, period, userInfo);
    }

    private String doConfirm(@PathVariable("applyMoney") @NotNull(message = "借款金额不能为空") Integer applyMoney, @PathVariable("period") @NotNull(message = "借款期限不能为空") Integer period, UserInfo userInfo) throws Exception {
        if (null != userInfo) {
            //返回认证标记flag的map
            Map<String, Object> resultFlagMap = new HashMap<>();
            //查询Info_Index_Info存在订单
            Map<String, Object> infoIndexParam = new HashMap<>();
            infoIndexParam.put("userId", userInfo.getId());
            infoIndexParam.put("borrowStatus", Constant.EXIST_BORROW_STATUS);
            List<InfoIndexInfo> infoIndexInfos = infoIndexInfoService.selectByParam(infoIndexParam);
            //存在正在进行的借款订单:不允许借款
            if (null != infoIndexInfos && infoIndexInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.HAS_UNCOMPLETE_ORDER_INFO);
            }

            //如果没有正在借款的订单，看是否老用户，如果是老用户就需要重新认证：身份认证，个人信息认证，运营商认证，银行卡认证
            UserInfo newUserInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
            if (null == newUserInfo) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }
            byte customerType = newUserInfo.getCustomerType();
            //如果是老用户，判断上次认证完成时间距离今日时间是否大于15天了
            Date date = new Date();
            if (Constant.OLD_CUSTOMER_TYPE == customerType) {
                InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(newUserInfo.getId());
                //按正常流程，因为银行卡是最后的认证，所以以银行卡认证时间为上次认证完成完时间，
                int day = DateUtil.daysBetween(infoIndexInfo.getAuthBankTime(), date);
                if (day >= Constant.HIT_RISK_INTERVAL_DAY) {
                    //  infoIndexInfoService.updateUserInfoAndInfoIndexInfo(newUserInfo,request, response);
                }
            }

            OrderBorrow orderBorrow = new OrderBorrow();
            orderBorrow.setUserId(userInfo.getId());
            orderBorrow.setCreateTime(new Date());
            // orderBorrow.setStatus(ConstantByOrderState.TENSTATUS);
            List<OrderBorrow> byUserOrderParam = orderBorrowService.findByUserOrderParam(orderBorrow);
            //一天只能生产一条订单当天已还款后可以再借，如果今天已经有订单了，就不允许再借了
            UserAppSoftware userId = userAppSoftwareDAO.findUserId(1);
            int a = 0;
            if (null != userId) {
                a = Integer.parseInt(userId.getVersionCode());
            }
            if (null != byUserOrderParam && byUserOrderParam.size() > a) {
                resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.HAS_MORE_ORDER, resultFlagMap);
            }
            LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(userInfo.getChannelId());
            if (null == loanConfig) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }
            //查询认证规则(auth_rule_config表)
            List<AuthRuleConfig> allAuthRule = authRuleConfigService.findAllAuthRule();
            if (CollectionUtils.isEmpty(allAuthRule)) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }

            if (null != newUserInfo) {
                /**1、以下根据借款配置规则进行判断*/
                String loanRuleJudgeData = loanRuleJudge(loanConfig, newUserInfo, applyMoney, period);
                if (null != loanRuleJudgeData) {
                    return loanRuleJudgeData;
                }

                /**2、根据用户表信息、用户费率 表信息进行判断*/
                //大于信用金额，不允许
                if (applyMoney > (newUserInfo.getAmountAvailable())) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "你当前剩余额度不足" + applyMoney + "元");
                }

                //查询该用户的费率表(user_money_rate表)
                UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
                if (null == userMoneyRate) {
                    userMoneyRate = new UserMoneyRate();
                    userMoneyRate.setUserId(newUserInfo.getId());
                    userMoneyRate.setMaxAmount(loanConfig.getLoanAmount());
                    userMoneyRate.setServiceCharge(loanConfig.getServiceCharge());
                    userMoneyRate.setAccrual(loanConfig.getBorrowInterest());
                    int insertState = userMoneyRateService.insert(userMoneyRate);
                    if (insertState > 0) {
                        userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
                    }
                }
                //判断可借金额
                log.info("loan ------start,userId={},phone={},maxAmount={},applyMoney={}", userInfo.getId(), userInfo.getPhone(), userMoneyRate.getMaxAmount(), applyMoney);
                if (applyMoney > (userMoneyRate.getMaxAmount() / Constant.DOLLAR_CHANGE_PENNY)) {
                    String resultInfo = Constant.LOAN_MONEY_LIMIT + (userMoneyRate.getMaxAmount() / Constant.DOLLAR_CHANGE_PENNY) + "元";
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, resultInfo);
                }

                /**3、以下根据认证配置规则进行判断是否允许借款*/

                //以下根据认证配置规则进行判断
                String authRuleData = authRuleJudge(allAuthRule, newUserInfo, resultFlagMap);
                if (null != authRuleData) {
                    return authRuleData;
                }

                //根据用户id和银行卡有效状态查询银行卡（user_card_info）
                Map<String, Object> findCardParam = new HashMap<String, Object>();
                findCardParam.put("userId", userInfo.getId());
                findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
                findCardParam.put("limit", 1);
                List<UserCardInfo> userCardByUserId = userBankCardInfoService.findUserCardByUserId(findCardParam);
                /**4、允许借款*/
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.ALLOW_LOAN_INFO, putResultDate(loanConfig, applyMoney, period, userMoneyRate, userCardByUserId, newUserInfo));

            } else {//用户登录信息失效，请重新登录
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }
        } else {//用户登录信息失效，请重新登录
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
    }

    /**
     * 确认提交借款申请
     *
     * @param request
     * @return
     */
    @GetMapping("/loan/{applyMoney}/{period}")
    public Object loan(HttpServletRequest request, HttpServletResponse response,
                       @NotNull(message = "借款金额不能为空")
                       @PathVariable("applyMoney")
                       Integer applyMoney,
                       @PathVariable("period")
                       @NotNull(message = "借款期限不能为空")
                       Integer period) throws Exception {
        UserInfo userInfo = redisUser(request);
        return doLoan(applyMoney, period, userInfo);

    }

    private Object doLoan(@PathVariable("applyMoney") @NotNull(message = "借款金额不能为空") Integer applyMoney, @NotNull(message = "借款期限不能为空") @PathVariable("period") Integer period, UserInfo userInfo) throws Exception {
        CallBackResult callBackResult = new CallBackResult();
        Date date = new Date();

        if (null == userInfo) {
            callBackResult.setCode(CallBackResult.LOGIN_IS_PAST);
            callBackResult.setMessage(Constant.USER_MSG_NOT_EXIST);
            return callBackResult;
        } else {
            log.info("loan ------start,userId={},phone={},startTime={},applyMoney={}", userInfo.getId(), userInfo.getPhone(), DateUtil.formatTimeYmdHms(date), applyMoney);
            String key = Constant.APP_LOAN_APPLY + userInfo.getId();
            boolean exist = redisUtil.hasKey(key);
            if (exist) {
                callBackResult.setCode(Constant.RESULT_BAD_STATUS);
                callBackResult.setMessage("订单处理中，请稍后再试！");
                return callBackResult;
            }
            //30秒过后自动过期
            redisUtil.set(key, applyMoney + "_" + period, Constant.APP_LOAN_TIME);
            Map<String, Object> resultFlagMap = new HashMap<>();
            OrderBorrow orderBorrow = new OrderBorrow();
            orderBorrow.setUserId(userInfo.getId());
            orderBorrow.setCreateTime(date);
            //  orderBorrow.setStatus(ConstantByOrderState.TENSTATUS);
            List<OrderBorrow> byUserOrderParam = orderBorrowService.findByUserOrderParam(orderBorrow);
            //一天只能生产一条订单(当天已还款后可以再借)，如果今天已经有订单了，就不允许再借了
            UserAppSoftware userId = userAppSoftwareDAO.findUserId(1);
            int a = 0;
            if (null != userId) {
                a = Integer.parseInt(userId.getVersionCode());
            }
            if (null != byUserOrderParam && byUserOrderParam.size() > a) {
                resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                callBackResult.setCode(Constant.RESULT_BAD_STATUS);
                callBackResult.setMessage(Constant.HAS_MORE_ORDER);
                callBackResult.setData(resultFlagMap);
            } else {
                //查询Info_Index_Info存在订单
                Map<String, Object> infoIndexParam = new HashMap<>();
                infoIndexParam.put("userId", userInfo.getId());
                infoIndexParam.put("borrowStatus", Constant.EXIST_BORROW_STATUS);
                List<InfoIndexInfo> infoIndexInfos = infoIndexInfoService.selectByParam(infoIndexParam);
                //存在正在进行的借款订单:不允许借款
                if (null != infoIndexInfos && infoIndexInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                    callBackResult.setCode(Constant.RESULT_BAD_STATUS);
                    callBackResult.setMessage(Constant.HAS_UNCOMPLETE_ORDER_INFO);
                    callBackResult.setData(resultFlagMap);
                } else {
                    //再次判断允许借款的条件（防止非法用户跳过首页借款申请按钮，直接进入借款页面）
                    String allowLoanStrData = doConfirm(applyMoney, period, userInfo);
                    Map<String, Object> allowLoanData = JSONObject.parseObject(allowLoanStrData);
                    int code = (Integer) allowLoanData.get("code");
                    String message = (String) allowLoanData.get("message");
                    Object data = allowLoanData.get("data");
                    //如果状态码不为200，并且message信息不为：“允许申请借款”，表示说明借款条件不成熟（就按借款判断方法confirmLoan(request, applyMoney, period)的判断为不允许的数据进行返回）
                    if (!Constant.ALLOW_LOAN_INFO.equals(message)) {
                        callBackResult.setCode(code);
                        callBackResult.setMessage(message);
                        callBackResult.setData(data);
                    } else {
                        /**借款总服务费率*/
                        Double apr_Fee;
                        UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
                        /**2019-03-03生成在订单中的服务费率为：（服务费+实际利息）*/
                        if (null != userMoneyRate) {
                            //老客贷款费率计算，根据复贷规则计算
                            if (userInfo.getCustomerType() == YesOrNo.YES.getValue().byteValue()) {
                                if (userMoneyRate.getRepetitionTimes().byteValue() > 0) {
                                    List<IncreaseMoneyConfigVo> allIncreaseConfig = increaseMoneyConfigService.findAllIncreaseConfig(Constant.REPETITION_TYPE);
                                    for (IncreaseMoneyConfigVo entity : allIncreaseConfig) {
                                        if (entity.getAchieveTimes().equals(userMoneyRate.getRepetitionTimes())) {
                                            if (entity.getServiceMoney() != 0 && entity.getReduceInterest() != 0) {
                                                log.info("老客复贷，按贷款规则降息处理");
                                                double serviceMoney = new BigDecimal(entity.getServiceMoney() / 100).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                                                userMoneyRate.setServiceCharge(serviceMoney);
                                                double accrual = new BigDecimal(entity.getReduceInterest() / 100).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                                                userMoneyRate.setAccrual(accrual);
                                                int res = userMoneyRateService.update(userMoneyRate);
                                                if (res > 0)
                                                    log.info("老客复贷降息成功,userId:{},服务费率:{},利息费率:{}", userInfo.getId(), serviceMoney, accrual);
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            apr_Fee = userMoneyRate.getServiceCharge() + userMoneyRate.getAccrual();

                        } else {
                            apr_Fee = ConstantByOrderState.DEFAULT_ARP_FEE;
                        }


                        UserInfo newUserInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
                        if (null != newUserInfo) {
                            //根据用户id和银行卡有效状态查询银行卡（user_bank_card_info）
                            Map<String, Object> findCardParam = new HashMap<String, Object>();
                            findCardParam.put("userId", newUserInfo.getId());
                            findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
                            findCardParam.put("limit", 1);
                            List<UserCardInfo> userCardByUserId = userBankCardInfoService.findUserCardByUserId(findCardParam);
                            //判断已有银行卡信息
                            Double loanFee = 0.0;
                            if (null != userCardByUserId && userCardByUserId.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                                UserCardInfo userBankCardInfo = userCardByUserId.get(0);
                                //借款服务费（= 总的借款服务费率<:服务费率 + 借款利息> * 申请金额）
                                loanFee = (apr_Fee * Constant.CHANGE_MULTIPLE) * (applyMoney / Constant.CHANGE_MULTIPLE);
                                //获取借款天数
                                if (null != userMoneyRate && userMoneyRate.getLoanTerm() != null && userMoneyRate.getLoanTerm() > 0) {
                                    period = userMoneyRate.getLoanTerm();
                                }
                                //生成借款订单
                                callBackResult = saveLoanOrder(apr_Fee, newUserInfo, applyMoney, loanFee.intValue(), period, userBankCardInfo, userMoneyRate, newUserInfo);
                                // 申请成功时将订单信息同步到数据中心
                                try {
                                    pushApplyOrder(userInfo);
                                } catch (Exception e) {
                                    log.error("共债申请通知异常:{}", e);
                                }
                            } else {//银行卡不存在
                                //银行卡未绑定的标记
                                resultFlagMap.put("flag", ConstantByOrderState.BANK_UNAUTHTH);
                                callBackResult.setCode(Constant.RESULT_BAD_STATUS);
                                callBackResult.setMessage(Constant.BANK_IS_NOT_EXISTS);
                                callBackResult.setData(resultFlagMap);
                            }
                        } else {//用户信息未查询到 说明用户登录失效
                            callBackResult.setCode(CallBackResult.LOGIN_IS_PAST);
                            callBackResult.setMessage(Constant.USER_MSG_NOT_EXIST);
                        }
                    }
                }
            }
            log.info("loan ------end,userId={},phone={},startTime={},applyMoney={},result={}", userInfo.getId(), userInfo.getPhone(), DateUtil.formatTimeYmdHms(date), applyMoney, JSONObject.toJSONString(callBackResult));

            return callBackResult;
        }

    }

    @Async
    public void pushApplyOrder(UserInfo user) {
        log.info("申请成功时将订单信息同步到数据中心返回参数------res={}", "res");
        // 将申请成功的用户订单信息推到数据中心
        List<Map<String, String>> userList = new ArrayList<>();
        Map map = new HashMap();
        map.put("name", user.getRealName());
        map.put("phone", user.getPhone());
        map.put("idNo", user.getIdCard());
        userList.add(map);
        juGuangApi.uploadJointDebt(userList, JuGuangEnum.APPLY.getValue(), 0);
    }

    /**
     * 允许去借款返回借款界面数据的方法
     *
     * @param applyMoney //借款金额
     * @param period     //借款期限
     * @param //用户银行卡信息
     * @return
     * @throws Exception
     */
    public Map<String, Object> putResultDate(LoanRuleConfig loanRuleConfig, Integer applyMoney, Integer period, UserMoneyRate userMoneyRate, List<UserCardInfo> userBankCardInfos, UserInfo userInfo) throws Exception {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        //1.用户的实际费率表中可申请的借款金额
        resultMap.put("applyMoney", applyMoney);
        //系统的允许的最小借款金额
        resultMap.put("minMoney", null != loanRuleConfig.getLoanAmount() ? loanRuleConfig.getLoanAmount() / Constant.CENT_CHANGE_DOLLAR : Constant.APPLY_MIN_MONEY);
        //2.借款期限
        resultMap.put("period", period);
        //3.借款服务费率
        resultMap.put("serviceCharge", userMoneyRate.getServiceCharge());
        //4.借款利息
        resultMap.put("borrowInterest", userMoneyRate.getAccrual());
        if (null != userBankCardInfos && userBankCardInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            UserCardInfo userBankCardInfo = userBankCardInfos.get(0);
            //6.到账银行卡尾号后四位
            resultMap.put("bankInfo", userBankCardInfo.getBankName() + "(" + StringUtil.subString(userBankCardInfo.getCardNo(), 4) + ")");
        } else {
            resultMap.put("bankInfo", "");
        }
        //7.借款允许的标记
        resultMap.put("flag", ConstantByOrderState.LOAN_ALLOW_FLAG);
        //还款金额计算方式，1表示：申请金额+服务费+利息（利息后置），0表示：还款金额=申请金额(利息前置)
        Integer interest = 0;
        BackConfigParamsVo bySysKey = backConfigParamsDao.findBySysKey("interest");
        if (null != bySysKey) {
            interest = bySysKey.getSysValue();
        }
        resultMap.put("interest", interest);
        return resultMap;
    }

    /**
     * 保存订单号，订单生成成功后并进入风控（消息队列中）
     *
     * @param newUserInfo      //通过token在Redis中取出的用户在数据库查出来的新用户信息
     * @param applyMoney       //申请金额
     * @param loanFee          //服务费
     * @param period           //借款期限
     * @param userBankCardInfo //用户银行卡信息
     * @return
     */
    public CallBackResult saveLoanOrder(Double apr_Fee, UserInfo newUserInfo, Integer applyMoney, Integer loanFee, Integer period, UserCardInfo userBankCardInfo, UserMoneyRate userMoneyRate, UserInfo userInfo) throws Exception {
        CallBackResult callBackResult = new CallBackResult();
        Date nowDate = new Date();
        OrderBorrow orderBorrow = new OrderBorrow();
        //创建时间
        orderBorrow.setCreateTime(nowDate);
        //1.用户id
        orderBorrow.setUserId(newUserInfo.getId());
        //该订单申请时的客户类型
        orderBorrow.setCustomerType(userInfo.getCustomerType());
        Double apr_fee = Double.parseDouble(apr_Fee.toString());
        //2.借款服务费率乘10000转换为分
        apr_fee = apr_fee * Constant.ARP_FEE;
        //3.用户总服务费率
        orderBorrow.setFeeApr(apr_fee.intValue());
        //4.申请金额（传入值的单位：分）
        orderBorrow.setApplyAmount((applyMoney * Constant.DOLLAR_CHANGE_PENNY));
        //5.借款服务费
        orderBorrow.setLoanFee(loanFee * Constant.DOLLAR_CHANGE_PENNY);
        //6.借款的期限v1.0版本目前默认按天算（后期若有改动：需要APP用户选择方式传入参数来定具体单位借款的类型('0:按天，1,：按月，2：按年')和借款期限的单位(几天、几月、几年)）
        orderBorrow.setLoanTerm(period);
        //7.实际到账金额（=申请金额 - 服务费）* 100 转换为分
        // 利息前置/后置开关
        BackConfigParamsVo interest = backConfigParamsService.findBySysKey(Constant.INTEREST);
        if (interest == null || interest.getSysValue() == 0) {
            // 前置
            orderBorrow.setIntoMoney((applyMoney - loanFee) * Constant.DOLLAR_CHANGE_PENNY);
            log.info("UserLoanController 利息前置" + orderBorrow.getIntoMoney());
        } else {
            // 后置
            orderBorrow.setIntoMoney(applyMoney * Constant.DOLLAR_CHANGE_PENNY);
            log.info("UserLoanController 利息后置" + orderBorrow.getIntoMoney());
        }
        //8.设置银行卡id
        orderBorrow.setBankCardId(userBankCardInfo.getId());
        //9.生成订单流水号
        String orderNo = GenerateNo.nextOrdId();
        log.info("订单申请订单号 no=" + orderNo);
        orderBorrow.setOutTradeNo(orderNo);
        // orderBorrow.setFlowNo(orderNo);
        //设置下初审时间(避免状态在待初审时显示：1970的时间)
        orderBorrow.setTrialTime(nowDate);

        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(newUserInfo.getChannelId());
        //滞纳金利率，单位为万分之几（读取loan_rule_config表配置文件）
        if (null != loanConfig) {
            Double overdueRate = loanConfig.getOverdueRate() * Constant.ARP_FEE;
            orderBorrow.setLateFeeApr(overdueRate.shortValue());
        }

        //10.服务费率
        if (null != userMoneyRate) {
            Double serviceCharge = userMoneyRate.getServiceCharge() * orderBorrow.getApplyAmount();
            //user_Money_rate中的服务费
            orderBorrow.setServiceCharge(serviceCharge.intValue());
            Double accrual = userMoneyRate.getAccrual() * orderBorrow.getApplyAmount();
            //user_Money_rate中的利息
            orderBorrow.setAccrual(accrual.intValue());
        } else {
            orderBorrow.setServiceCharge(ConstantByOrderState.DEFAULT_SERVICECHARGE);
            orderBorrow.setAccrual(ConstantByOrderState.DEFAULT_ACCRUAL_);
        }
        orderBorrow.setCreateDate(nowDate);
        Map<String, Object> resultFlagMap = new HashMap<>();
        OrderBorrow orderBorrowNew = new OrderBorrow();
        orderBorrowNew.setUserId(userInfo.getId());
        orderBorrowNew.setCreateTime(nowDate);
        orderBorrowNew.setStatus(ConstantByOrderState.TENSTATUS);
        List<OrderBorrow> byUserOrderParam = orderBorrowService.findByUserOrderParam(orderBorrowNew);
        //一天只能生产一条订单(当天已还款后可以再借)，如果今天已经有订单了，就不允许再借了
        if (null != byUserOrderParam && byUserOrderParam.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
            callBackResult.setCode(Constant.RESULT_BAD_STATUS);
            callBackResult.setMessage(Constant.HAS_UNCOMPLETE_ORDER_INFO);
            callBackResult.setData(resultFlagMap);
            return callBackResult;
        }
        // 此处生成订单
        int orderId = orderBorrowService.insertSelective(orderBorrow);
        Map<String, Object> resultMap = new HashMap<>();
        RiskCreditUser risk = new RiskCreditUser();
        risk.setUserId(newUserInfo.getId());
        risk.setAssetId(orderId);
        risk.setCardNum(newUserInfo.getIdCard());
        risk.setUserName(newUserInfo.getRealName());
        risk.setAge((int) newUserInfo.getAge());
        risk.setEducation((int) newUserInfo.getEducation());
        risk.setSex((int) newUserInfo.getSex());
        risk.setUserPhone(newUserInfo.getPhone());

        risk.setAmountAddsum(new BigDecimal(0 / 100.00));
        risk.setMoneyAmount(new BigDecimal(applyMoney));
        orderBorrowMapper.insertRiskUser(risk);
        //10.还需放入消息队列中进行风控处理，并返回进度
        if (orderId > 0) {
            InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
            infoIndexInfo.setUserId(userInfo.getId());
            infoIndexInfo.setBorrowStatus("1");
            //修改借款状态为1：存在借款
            infoIndexInfoService.updateByPrimaryKeySelective(infoIndexInfo);
            OrderBorrow newOrderBorrow = orderBorrowService.selectByPrimaryKey(orderId);
            String newOrderBorrowStr = null;
            if (null != newOrderBorrow) {
                newOrderBorrowStr = JSONObject.toJSONString(newOrderBorrow);
            }
            byte orderBorrowStatus = newOrderBorrow.getStatus();
            if (0 == orderBorrowStatus) {
                //放入消息队列
                orderProducer.sendNewOrder(newOrderBorrowStr);
            }
            //订单成功的标记
            resultMap.put("flag", ConstantByOrderState.ORDER_PRODUCT_SUCC);


            /**异步统计订单生成量*/
            channelAsyncCountService.applyCount(userInfo, nowDate, orderBorrow.getCustomerType());

            //先判断是否开启了魔杖开关
            /**异步获取魔杖数据*/
          /*  BackConfigParamsVo bySysKey = backConfigParamsDao.findBySysKey("mz_switch");
            if (null!=bySysKey && null !=bySysKey.getSysValue() ){
                //如果打开才需要去认证
                if (bySysKey.getSysValue() == Constant.MZ_SWITCH){
                    mzDataService.saveMzData(userInfo.getId());
                }
            }*/
            callBackResult.setCode(CallBackResult.SUCCESS);
            callBackResult.setMessage(Constant.LOAN_APPLY_SUCCESS);

        } else {
            //订单失败的标记
            resultMap.put("flag", ConstantByOrderState.ORDER_PRODUCT_FAILD);
            callBackResult.setCode(CallBackResult.SUCCESS);
            callBackResult.setMessage(Constant.LOAN_APPLY_BAD);
            callBackResult.setData(resultMap);

        }
        log.info("userloanApply ------endSuccess,userId={},phone={},startTime={},applyMoney={},orderCreateTime={}", userInfo.getId(), userInfo.getPhone(), DateUtil.formatTimeYmdHms(nowDate), applyMoney, DateUtil.formatTimeYmdHms(orderBorrow.getCreateTime()));
        return callBackResult;
    }

    /**
     * 根据借款规则判断用户是否可允许借款
     *
     * @param loanConfig
     * @param userInfo
     * @param applyMoney
     * @param period
     * @return
     */
    public String loanRuleJudge(LoanRuleConfig loanConfig, UserInfo userInfo, int applyMoney, int period) {
        /**1、目前1.0版本最低借款金额不能低于1500*/

        int loanAmount = loanConfig.getLoanAmount() / Constant.CENT_CHANGE_DOLLAR;
        if (applyMoney < loanAmount) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "最低借款金额不能低于" + loanAmount + "元");
        }
        /**2、目前1.0版本最低借款期限不能少于7天*/
        if (period < loanConfig.getExpire()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "最低借款期限不能低于" + loanConfig.getExpire() + "天");
        }
        /**3、拒贷规则（命中黑名单多少天后才能再借（天）0 表示永远不能再借）*/
        if (loanConfig.getHitRiskAllowBorrowDay() == Constant.HIT_BLACK_STATE) {
            //命中本系统的黑名单
            if (Constant.USER_BLANK_STATUS == userInfo.getStatus()) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "黑名单用户，暂不能借款");
            }
        }
        //查询只要还存在待还的订单，都不允许再借
        List<OrderRepayment> stayRepayment = orderRepaymentService.findStayRepayment(userInfo.getId());
        if (null != stayRepayment && stayRepayment.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "你有未完成的订单，暂不借款！");
        }

        /**4、正常还款是否可以复贷：0表示"是" 1表示不能否（上次的订单）*/
        if (loanConfig.getNormalRepaymentRepetitionLoan() == Constant.NOMAL_REPAYMENT_STATE) {
            //查询还款表(order_repayment)中的，最近一条逾期还款
            Map<String, Object> orderParam = new HashMap<>();
            orderParam.put("userId", userInfo.getId());
            orderParam.put("createTime", new Date());
            List<OrderRepayment> orderRepayments = orderRepaymentService.findOrderPrepaymentByParam(orderParam);
            if (null != orderRepayments && orderRepayments.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                OrderRepayment orderRepayment = orderRepayments.get(0);
                if (Constant.NOMAL_REPAYMENT_STATES == orderRepayment.getStatus()) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "暂不能借款");
                }
            }
        }
        Map<String, Object> selectOrderParam = new HashMap<>();
        selectOrderParam.put("userId", userInfo.getId());
        selectOrderParam.put("createTime", new Date());
        //查询出最近一条订单
        List<OrderBorrow> orderBorrows = orderBorrowService.selectByParams(selectOrderParam);
        /**5、逾期还款：逾期7天以下还款是否可以复贷{"overdueDay":"7","isAllowLoan":"1" }（isAllowLoan：0表示否不能再借，1表示是允许再借）*/
        Map<String, Object> OverdueRepetitionLoan = JSONObject.parseObject(loanConfig.getOverdueRepaymentRepetitionLoan());
        //5.1返逾期7天以下还款是否可以复贷（0表示不能复贷，1表示可以复贷）
        loanConfig.setIsAllowLoan((Integer) OverdueRepetitionLoan.get("isAllowLoan"));
        //5.2当isAllowLoan=1：表示逾期可以复贷时，必须同时满足是逾期天数小于overdueDay（目前是7）天以下的
        loanConfig.setOverdueDay((Integer) OverdueRepetitionLoan.get("overdueDay"));
        //如果isAllowLoan = 0 永远不能再借
        if (Constant.NOT_IS_ALLOW_LOAN_STATE == loanConfig.getIsAllowLoan()) {
            //判断订单状态是否逾期还款
            if (null != orderBorrows && orderBorrows.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                OrderBorrow orderBorrow = orderBorrows.get(0);
                if (Constant.BORROW_ORDER_OVERDUE_REPAYMENT_STATUS == orderBorrow.getStatus()) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "逾期还款用户，暂不能借款");
                }
            }
        }
        //如果isAllowLoan = 1 允许逾期还款再借，但是逾期天数必须在7天以下
        if (Constant.IS_ALLOW_LOAN_STATE == loanConfig.getIsAllowLoan()) {
            //查询还款表(order_repayment)中的，最近一条还款记录
            Map<String, Object> orderParam = new HashMap<>();
            orderParam.put("userId", userInfo.getId());
            orderParam.put("createTime", new Date());
            List<OrderRepayment> orderRepayments = orderRepaymentService.findOrderPrepaymentByParam(orderParam);
            if (null != orderRepayments && orderRepayments.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                OrderRepayment orderRepayment = orderRepayments.get(0);
                //如果逾期还款
                if (Constant.OVERDUE_REPAYMENT_STATE == orderRepayment.getStatus()) {
                    //逾期天数大于
                    if (orderRepayment.getLateDay() >= loanConfig.getOverdueDay()) {
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "上次借款逾期" + loanConfig.getOverdueDay() + "天，暂不能借款");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据认证规则配置判断借款条件
     *
     * @return
     */
    public String authRuleJudge(List<AuthRuleConfig> allAuthRule, UserInfo newUserInfo, Map<String, Object> resultFlagMap) throws Exception {
        //基本信息认证规则
        AuthRuleConfig basicAuthentic = null;
        //紧急联系人认证规则
        AuthRuleConfig emergencyAuthentic = null;
        //运营商认证规则
        AuthRuleConfig mobileAuthentic = null;
        //芝麻认证规则
        AuthRuleConfig zmAuthentic = null;
        //银行卡认证
        AuthRuleConfig bankAuthentic = null;
        for (AuthRuleConfig authRuleConfig : allAuthRule) {
            if ("basicAuthentic".equals(authRuleConfig.getAuthRuleKey())) {
                basicAuthentic = authRuleConfig;
            }
            if ("emergencyAuthentic".equals(authRuleConfig.getAuthRuleKey())) {
                emergencyAuthentic = authRuleConfig;
            }
            if ("mobileAuthentic".equals(authRuleConfig.getAuthRuleKey())) {
                mobileAuthentic = authRuleConfig;
            }
            if ("zmAuthentic".equals(authRuleConfig.getAuthRuleKey())) {
                zmAuthentic = authRuleConfig;
            }
            if ("bankAuthentic".equals(authRuleConfig.getAuthRuleKey())) {
                bankAuthentic = authRuleConfig;
            }
        }


        Integer id = newUserInfo.getId();
        InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(id);
        if (null == infoIndexInfo) {
            resultFlagMap.put("flag", ConstantByOrderState.BASE_UNCOMPLETE_FLAG);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UNCOMPLETE_PERSON_AUTH, resultFlagMap);
        }

        String riskType = userInfoMapper.selectChannelById(id);
        if (StringUtils.isNotBlank(riskType) && Constant.MOBILE_TYPE_BLUE == Integer.parseInt(riskType)) {
            resultFlagMap.put("flag", ConstantByOrderState.MOBILE_UNAUTH);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, "非渠道用户借款", resultFlagMap);
        }

        //1、当基本信息认证规则状态为0时，表示启用了基本信息认证规则
        if (null != basicAuthentic && Constant.AUTH_RULE_STATUS == basicAuthentic.getStatus()) {
            //个人信息认证为0
            if (Constant.AUTHENTIC_UNCOMPLETED_STATUS == infoIndexInfo.getAuthInfo()) {
                //个人信息未认证的标记
                resultFlagMap.put("flag", ConstantByOrderState.BASE_UNCOMPLETE_FLAG);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UNCOMPLETE_PERSON_AUTH, resultFlagMap);
            }
        }
        //2、当紧急联系人认证规则状态为0时，表示启用了紧急联系人认证规则
        if (null != emergencyAuthentic && emergencyAuthentic.getStatus() == Constant.AUTH_RULE_STATUS) {
            //紧急联系人认证为0
            if (Constant.AUTHENTIC_UNCOMPLETED_STATUS == infoIndexInfo.getAuthContacts()) {
                //紧急联系人未认证标记
                resultFlagMap.put("flag", ConstantByOrderState.CONTACTS_UNAUTH_FLAG);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UNCOMPLETE_EMERGENCY_AUTH, resultFlagMap);
            }
        }

        //3、当手机运营商认证规则状态为0时，表示启用了手机运营商认证规则
        if (null != mobileAuthentic && mobileAuthentic.getStatus() == Constant.AUTH_RULE_STATUS) {
            //手机运营商认证为0
            if (Constant.AUTHENTIC_UNCOMPLETED_STATUS == infoIndexInfo.getAuthMobile()) {
                //运营商未认证的标记
                resultFlagMap.put("flag", ConstantByOrderState.MOBILE_UNAUTH);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UNCOMPLETE_MOBILE_AUTH, resultFlagMap);
            }
        }
        //4、当芝麻分认证规则状态为0时，表示启用了芝麻分认证规则
        if (null != zmAuthentic && zmAuthentic.getStatus() == Constant.AUTH_RULE_STATUS) {
            //芝麻信用认证为0 未认证
            if (Constant.AUTHENTIC_UNCOMPLETED_STATUS == infoIndexInfo.getAuthSesame()) {
                //芝麻分未认证的标记
                resultFlagMap.put("flag", ConstantByOrderState.ZHIMA_UNAUTHTH);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UNCOMPLETE_ZM_AUTH, resultFlagMap);
            }
        }
        //5、当银行卡认证规则状态为0时，表示启用了银行卡认证规则
        if (null != bankAuthentic && bankAuthentic.getStatus() == Constant.AUTH_RULE_STATUS) {
            //未绑定银行卡
            if (Constant.AUTHENTIC_UNCOMPLETED_STATUS == infoIndexInfo.getAuthBank()) {
                //银行卡未绑定的标记
                resultFlagMap.put("flag", ConstantByOrderState.BANK_UNAUTHTH);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.PLEASE_BIND_BANK_CARD, resultFlagMap);
            }
        }
        return null;
    }

    /**
     * 修改用户认证信息
     *
     * @param userInfo
     */
    public void updateUserInfo(UserInfo userInfo) {
        if (null != userInfo && null != userInfo.getRealCount()) {
            byte realCount = userInfo.getRealCount();
            Integer realCoun = null;
            if (realCount == 0) {
                realCoun = (int) realCount + 2;
            }
            if (realCount >= 1) {
                realCoun = (int) realCount + 1;
            }
            UserInfo updateUserInfo = new UserInfo();
            updateUserInfo.setId(userInfo.getId());
            if (null != realCoun) {
                updateUserInfo.setRealCount(Byte.parseByte(realCoun.toString()));
            }
            userInfoMapper.updateByPrimaryKeySelective(updateUserInfo);
        }
    }


    /**
     * 首页和认证中心申请借款
     *
     * @param request
     * @return
     */
    @RequestMapping("/apply2")
    public String confirmLoan2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo userInfo = redisUser(request);
        return autoBorrow(userInfo.getId().toString());
    }

    @JmsListener(destination = QueueConstans.autoBorrowQueue)
    public void doBorrow(String userId) throws Exception {
        log.info("autoBorrow invoke start userId={}", userId);
        UserInfo userInfo = userInfoService.selectByPrimaryKey(Integer.parseInt(userId));
        if (userInfo == null) {
            log.error("autoBorrow invoke userInfo==null userId={}", userId);
            return;
        }
        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(userInfo.getChannelId());
        UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
        if (userMoneyRate == null) {
            Integer channelId = userInfo.getChannelId();
            LoanRuleConfig loanRuleConfig =
                    loanRuleConfigService.findByChannelId(channelId == null ? 0 : channelId);
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
            //查询系统贷款规则：根据贷款规则配置生成费率信息
            /*log.error("autoBorrow invoke userMoneyRate==null userId={}", userId);
            return;*/
        }
        Integer period = Constant.RENEWAL_EXPIRE;
        if (null != loanConfig && null != loanConfig.getExpire()) {
            period = loanConfig.getExpire();
        }

        Integer applyMoney = userMoneyRate.getMaxAmount() / Constant.CENT_CHANGE_DOLLAR;
        Object s = doLoan(applyMoney, period, userInfo);
        log.info("autoBorrow return userId={},res={}", userId, JSON.toJSONString(s));
    }

    private String autoBorrow(String userId) throws Exception {

        UserInfo userInfo = userInfoService.selectByPrimaryKey(Integer.parseInt(userId));
        if (null != userInfo) {
            UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
            if (null != userMoneyRate) {
                //返回认证标记flag的map
                Map<String, Object> resultFlagMap = new HashMap<>();
                //查询Info_Index_Info存在订单
                Map<String, Object> infoIndexParam = new HashMap<>();
                infoIndexParam.put("userId", userInfo.getId());
                infoIndexParam.put("borrowStatus", Constant.EXIST_BORROW_STATUS);
                List<InfoIndexInfo> infoIndexInfos = infoIndexInfoService.selectByParam(infoIndexParam);
                //存在正在进行的借款订单:不允许借款
                if (null != infoIndexInfos && infoIndexInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.HAS_UNCOMPLETE_ORDER_INFO, resultFlagMap);
                }
                //如果没有正在借款的订单，看是否老用户，如果是老用户就需要重新认证：身份认证，个人信息认证，运营商认证，银行卡认证
                UserInfo newUserInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
                if (null == newUserInfo) {
                    return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
                }

                //查询只要还存在待还的订单，都不允许再借
                List<OrderRepayment> stayRepayment = orderRepaymentService.findStayRepayment(userInfo.getId());
                if (null != stayRepayment && stayRepayment.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.HAS_UNCOMPLETE_ORDER_INFO, resultFlagMap);
                }

                byte customerType = newUserInfo.getCustomerType();
                //如果是老用户，判断上次认证完成时间距离今日时间是否大于15天了
                Date date = new Date();
                if (Constant.OLD_CUSTOMER_TYPE == customerType) {
                    InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(newUserInfo.getId());
                    //按正常流程，因为银行卡是最后的认证，所以以银行卡认证时间为上次认证完成完时间，
                    if (null != infoIndexInfo && null != infoIndexInfo.getAuthBankTime()) {
                        int day = DateUtil.daysBetween(infoIndexInfo.getAuthBankTime(), date);
                        if (day >= Constant.HIT_RISK_INTERVAL_DAY) {
                            //infoIndexInfoService.updateUserInfoAndInfoIndexInfo(newUserInfo,request, response);
                        }
                    }

                }
                OrderBorrow orderBorrow = new OrderBorrow();
                orderBorrow.setUserId(userInfo.getId());
                orderBorrow.setCreateTime(new Date());
                //  orderBorrow.setStatus(ConstantByOrderState.TENSTATUS);
                List<OrderBorrow> byUserOrderParam = orderBorrowService.findByUserOrderParam(orderBorrow);
                //一天只能生产一条订单当天已还款后可以再借，如果今天已经有订单了，就不允许再借了
                UserAppSoftware userAppSoftware = userAppSoftwareDAO.findUserId(1);
                int a = 0;
                if (null != userAppSoftware) {
                    a = Integer.parseInt(userAppSoftware.getVersionCode());
                }
                if (null != byUserOrderParam && byUserOrderParam.size() > a) {
                    resultFlagMap.put("flag", ConstantByOrderState.HAS_ORDER);
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.HAS_MORE_ORDER, resultFlagMap);
                }

                //先查询系统配置逾期过的用户是否可以复贷
                BackConfigParamsVo overdueRepayment = backConfigParamsDao.findBySysKey(Constant.OVERDUE_REPAYMENT_KEY);
                if (null != overdueRepayment && null != overdueRepayment.getSysValue()) {
                    int sysValue = overdueRepayment.getSysValue();
                    //如果为0表示不能复贷，1表示允许复贷
                    if (sysValue == Constant.OVERDUE_REPAYMENT_STATUS) {
                        //如果用户逾期过，就不能再借款了
                        List<OrderBorrow> byOverdueStatus = orderBorrowService.findByOverdueStatus(newUserInfo.getId());
                        if (null != byOverdueStatus && byOverdueStatus.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.HAS_OVERDUE_ORDER_INFO);
                        }
                    }
                }
                //查询认证规则(auth_rule_config表)
                List<AuthRuleConfig> allAuthRule = authRuleConfigService.findAllAuthRule();
                if (CollectionUtils.isEmpty(allAuthRule)) {
                    return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
                }
                //根据用户id和银行卡有效状态查询银行卡（user_card_info）
                Map<String, Object> findCardParam = new HashMap<String, Object>();
                findCardParam.put("userId", userInfo.getId());
                findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
                findCardParam.put("limit", 1);
                List<UserCardInfo> userCardByUserId = userBankCardInfoService.findUserCardByUserId(findCardParam);
                /**3、以下根据认证配置规则进行判断是否允许借款*/
                //以下根据认证配置规则进行判断
                String authRuleData = authRuleJudge(allAuthRule, userInfo, resultFlagMap);
                if (null != authRuleData) {
                    return authRuleData;
                }
                //查贷款规则
               /* Map<String, Object> loanRuleParams = new HashMap<>();
                loanRuleParams.put("status", Constant.LOAN_RULE_STATUS);
                //查询贷款规则(loan_rule_config表)
                LoanRuleConfig loanConfig = loanRuleConfigService.findLoanConfigByParams(loanRuleParams);*/
                LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(newUserInfo.getChannelId());
                Integer period = Constant.RENEWAL_EXPIRE;
                if (null != loanConfig && null != loanConfig.getExpire()) {
                    period = loanConfig.getExpire();
                }

                Integer applyMoney = userMoneyRate.getMaxAmount() / Constant.CENT_CHANGE_DOLLAR;
                Map<String, Object> resultDate = putResultDate(loanConfig, applyMoney, period, userMoneyRate, userCardByUserId, userInfo);

                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultDate);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 扣款协议
     *
     * @param request
     * @return
     */
    @RequestMapping("/agKk")
    public String agKk(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
            if (null != userMoneyRate) {

                //如果没有正在借款的订单，看是否老用户，如果是老用户就需要重新认证：身份认证，个人信息认证，运营商认证，银行卡认证
                UserInfo newUserInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
                if (null == newUserInfo) {
                    return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
                }

                //查贷款规则
               /* Map<String, Object> loanRuleParams = new HashMap<>();
                loanRuleParams.put("status", Constant.LOAN_RULE_STATUS);
                //查询贷款规则(loan_rule_config表)
                LoanRuleConfig loanConfig = loanRuleConfigService.findLoanConfigByParams(loanRuleParams);*/
//                LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(newUserInfo.getChannelId());
//                Map<String, Object> findCardParam = new HashMap<String, Object>();
//                findCardParam.put("userId", userInfo.getId());
//                findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
//                findCardParam.put("limit", 1);
//                List<UserCardInfo> userCardByUserId = userBankCardInfoService.findUserCardByUserId(findCardParam);
//
//                Integer applyMoney = userMoneyRate.getMaxAmount() / Constant.CENT_CHANGE_DOLLAR;
                Map<String, Object> resultDate = new HashMap<>();
//                //未签约协议
//                String ag_company = backConfigParamsDao.findStrValue("ag_company");
//
//                String kkurl = getkkPdf(newUserInfo, userCardByUserId, ag_company);
                resultDate.put("kk", "#");
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultDate);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 借款协议
     *
     * @param request
     * @return
     */
    @RequestMapping("/agJk")
    public String agJk(HttpServletRequest request, HttpServletResponse response, Integer applyMoney) throws Exception {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            UserMoneyRate userMoneyRate = userMoneyRateService.findByUserId(userInfo.getId());
            if (null != userMoneyRate) {

                //如果没有正在借款的订单，看是否老用户，如果是老用户就需要重新认证：身份认证，个人信息认证，运营商认证，银行卡认证
                UserInfo newUserInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
                if (null == newUserInfo) {
                    return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
                }

                //查贷款规则
               /* Map<String, Object> loanRuleParams = new HashMap<>();
                loanRuleParams.put("status", Constant.LOAN_RULE_STATUS);
                //查询贷款规则(loan_rule_config表)
                LoanRuleConfig loanConfig = loanRuleConfigService.findLoanConfigByParams(loanRuleParams);*/
//                LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(newUserInfo.getChannelId());
//                Map<String, Object> findCardParam = new HashMap<String, Object>();
//                findCardParam.put("userId", userInfo.getId());
//                findCardParam.put("status", Constant.USER_BANKCARD_STATUS);
//                findCardParam.put("limit", 1);
//                List<UserCardInfo> userCardByUserId = userBankCardInfoService.findUserCardByUserId(findCardParam);
//                if (applyMoney == null) {
//                    applyMoney = userMoneyRate.getMaxAmount() / Constant.CENT_CHANGE_DOLLAR;
//                }
                Map<String, Object> resultDate = new HashMap<>();
//                //未签约协议
//                String ag_company = backConfigParamsDao.findStrValue("ag_company");
//
//                String jkurl = getjkPdf(userMoneyRate, newUserInfo, loanConfig, applyMoney, ag_company);
                resultDate.put("jk", "#");
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultDate);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

}
