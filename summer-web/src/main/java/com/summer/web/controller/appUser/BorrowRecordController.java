package com.summer.web.controller.appUser;

import com.summer.api.service.*;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.IUserCardInfoDao;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.util.*;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.RepaymentRecoredVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP还款记录、还款详情页
 */
@Slf4j
@Validated
@RestController
@RequestMapping("v1.0/api/app")
public class BorrowRecordController extends BaseController {
    @Resource
    private IOrderRepaymentService orderRepaymentService;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private IOrderBorrowService orderBorrowService;
    @Resource
    private IOrderRenewalService orderRenewalService;
    @Resource
    private IUserCardInfoDao userCardInfoDao;

    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;

    /**
     * 获取还款记录
     *
     * @param request
     * @param type
     * @return
     */
    @GetMapping("/record/{type}")
    public String getRecord(HttpServletRequest request,
                            @PathVariable("type")
                            @NotNull(message = "获取借款类型不能为空")
                                    Integer type) {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            //类型为1时查询待还款记录
            if (Constant.FIND_WAIT_ORDER_TYPE == type) {
                List<RepaymentRecoredVo> orderRecordByParam = orderRepaymentService.findOrderRecordByParam(userInfo.getId(), type);
                if (null != orderRecordByParam && orderRecordByParam.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, orderRecordByParam);
                } else {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
                }
                //状态为2时、查询已还款
            } else if (Constant.FIND_REPAYMENT_ORDER_TYPE == type) {
                List<RepaymentRecoredVo> orderRecordByParam = orderRepaymentService.findOrderRecordByParam(userInfo.getId(), type);
                if (null != orderRecordByParam && orderRecordByParam.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, orderRecordByParam);
                } else {
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
                }
            }
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.WITHOUT_LOAN_TYPE);
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    private UserCardInfo hasChanjieBindCard(Integer userId) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        // 查询用户的借款入账账户信息
        param.put("userId", userId); //
        param.put("type", "2"); // 借记卡
        param.put("status", "1"); // 状态成功
        List<UserCardInfo> cardInfo = userCardInfoDao.findUserCardByUserId(param);//查询有效的用户银行卡信息
        UserCardInfo userCardInfo;
        if (cardInfo != null && cardInfo.size() > 0) {
            userCardInfo = cardInfo.get(0);
            if (StringUtils.isNoneBlank(userCardInfo.getCardNo())) {
                return userCardInfo;
            }
        }
        return null;
    }

    /**
     * 获取还款详情
     *
     * @param request
     * @param orderId
     * @return
     */
    @GetMapping("/repayment/detail")
    public String getRepaymentDetail(HttpServletRequest request,
                                     // @NotNull(message = "订单id不能为空")
                                     Integer orderId) throws Exception {
        if (null == orderId) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "订单号不能为空");
        }

        UserInfo userInfo = redisUser(request);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cardBinded", 1);
        if (null != userInfo) {
            UserCardInfo cardInfo = hasChanjieBindCard(userInfo.getId());
            if (cardInfo == null) {
                // 如果未绑卡，则要求前端进行跳转
                resultMap.put("cardBinded", 0);
            }
            OrderRepayment orderRecordDetail = orderRepaymentService.findOrderRecordDetailById(orderId);
            if (null != orderRecordDetail) {
                OrderBorrow orderBorrow = orderBorrowService.selectByPrimaryKey(orderRecordDetail.getBorrowId());
                if (null != orderBorrow) {
                    //金额转换元
                    BigDecimal changeFee = new BigDecimal(100.0);
                    int reductionMoney = orderRecordDetail.getReduceAmount();
                    //减免金额
                    resultMap.put("reductionMoney", 0 != reductionMoney ? new BigDecimal(reductionMoney).divide(changeFee, 2, BigDecimal.ROUND_HALF_UP) : 0);
                    //还款订单id
                    resultMap.put("reOrderId", orderRecordDetail.getId());
                    /**应还金额（应还金额 = 总待还金额 - 已还金额 - 减免金额 ）*/
                    Double repaymentAmount = (orderRecordDetail.getRepaymentAmount() - orderRecordDetail.getPaidAmount() - reductionMoney) / Constant.CHANGE_CENT;
                    BigDecimal repaymentAmounts = new BigDecimal(repaymentAmount);
                    resultMap.put("repaymentAmount", repaymentAmounts.setScale(2, BigDecimal.ROUND_HALF_UP));
                    //如果是还款页面访问就返回实际还款时间/否则就按预计还款时间 和实际还款金额
                    byte status = orderRecordDetail.getStatus();
                    if (ConstantByOrderState.REPAYMENT_STATUS_SECOND == status || ConstantByOrderState.REPAYMENT_STATUS_FORU == status || ConstantByOrderState.REPAYMENT_STATUS_AHEAD == status) {
                        //如果已还款（显示实际还款金额）
                        BigDecimal paidAmount = new BigDecimal((orderRecordDetail.getPaidAmount() / Constant.CHANGE_CENT));
                        resultMap.replace("repaymentAmount", paidAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                        //还款时间
                        resultMap.put("repaymentTime", DateUtil.formatTimeYmd(orderRecordDetail.getPaidTime()));
                    } else {
                        //还款时间
                        resultMap.put("repaymentTime", DateUtil.formatTimeYmd(orderRecordDetail.getRepaymentTime()));
                    }
                    //借款时间
                    resultMap.put("loanTime", DateUtil.formatTimeYmd(orderBorrow.getCreateTime()));
                    //借款天数
                    resultMap.put("loanTerm", DateUtil.daysBetween(orderRecordDetail.getCreateTime(), orderRecordDetail.getRepaymentTime()) + 1);
                    //待还款倒计时
                    resultMap.put("repaymentDownTime", DateUtil.daysBetween(new Date(), orderRecordDetail.getRepaymentTime()));
                    //逾期天数
                    resultMap.put("lateDay", orderRecordDetail.getLateDay());

                    //本金
                    BigDecimal principalAmount = new BigDecimal(orderRecordDetail.getPrincipalAmount());
                    resultMap.put("principalAmount", principalAmount.divide(changeFee, 2, BigDecimal.ROUND_HALF_UP));
                    //利息
                    BigDecimal accrual = new BigDecimal(orderBorrow.getAccrual());
                    resultMap.put("accrual", accrual.divide(changeFee, 2, BigDecimal.ROUND_HALF_UP));
                    //借款服务费
                    BigDecimal serviceCharge = new BigDecimal(orderBorrow.getServiceCharge());
                    resultMap.put("serviceCharge", serviceCharge.divide(changeFee, 2, BigDecimal.ROUND_HALF_UP));
                    //延期次数通过还款id查询续期表中多少条
                    List<OrderRenewal> OrderRenewals = orderRenewalService.findByRepaymentId(orderRecordDetail.getId());
                    if (null != OrderRenewals && OrderRenewals.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                        resultMap.put("renewalsTimes", OrderRenewals.size());
                    } else {
                        resultMap.put("renewalsTimes", 0);
                    }
                    //罚息
                    Integer lateFeeAmount = orderRecordDetail.getLateFee();
                    resultMap.put("lateFee", null != lateFeeAmount ? new BigDecimal(lateFeeAmount).divide(changeFee, 2, BigDecimal.ROUND_HALF_UP) : 0);

                    //我方订单号
                    resultMap.put("orderNum", orderBorrow.getOutTradeNo());
                    //状态
                    resultMap.put("status", orderRecordDetail.getStatus());
                    resultMap.put("userId", userInfo.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("borrowId", orderBorrow.getId());
                    resultMap.put("kk", "#");
                    resultMap.put("jk", "#");
                    //续期开关
                    BackConfigParamsVo renewalSwitchConfig = backConfigParamsService.findBySysKey(Constant.RENEWAL_SWITCH);
                    //为了不影响之前的版本，暂时默认允许续期 续期开关，0表示开启，1表示关闭
                    int renewalSwitch = 0;
                    if (null != renewalSwitchConfig) {
                        renewalSwitch = renewalSwitchConfig.getSysValue();
                    }
                    resultMap.put("renewalSwitch", renewalSwitch);

                    //支付宝开关，默认银行卡还款
                    int repaySwitch = 0;
                    BackConfigParamsVo repaySwitchConfig = backConfigParamsService.findBySysKey(Constant.REPAY_SWITCH);
                    if (null != repaySwitchConfig) {
                        repaySwitch = repaySwitchConfig.getSysValue();
                    }
                    resultMap.put("repaySwitch", repaySwitch);
                    String banckInfo = "";
                    if (null != cardInfo) {
                        if (StringUtils.isNotBlank(cardInfo.getBankName())) {
                            banckInfo = cardInfo.getBankName();
                        }
                        if (StringUtils.isNotBlank(cardInfo.getCardNo())) {
                            banckInfo += "(" + StringUtil.subString(cardInfo.getCardNo(), 4) + ")";
                        }
                    }
                    //还款金额计算方式，0表示：还款金额=申请金额(利息前置)，1表示：申请金额+服务费+利息（利息后置）
                    Integer interest = 0;
                    BackConfigParamsVo bySysKey = backConfigParamsDao.findBySysKey("interest");
                    if (null != bySysKey) {
                        interest = bySysKey.getSysValue();
                    }
                    resultMap.put("interest", interest);

                    log.info("获取订单还款详情页面------orderId={},getBankInfo={}", orderId, cardInfo);
                    resultMap.put("bankInfo", banckInfo);
                    return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
                }
            }
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_DATA_EMPTY);
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 获取续期详情
     *
     * @return
     */
    @GetMapping("/renewal/detail")
    public Object getRenewalDetail(HttpServletRequest request,
                                   //  @NotNull(message = "订单id不能为空")
                                   Integer orderId) throws Exception {
        if (null == orderId) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "订单号不能为空");
        }
        UserInfo userInfo = redisUser(request);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cardBinded", 1);
        if (null != userInfo) {
            LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(userInfo.getChannelId());
            /**根据贷款规则配置，判断最多续期次数*/
            Object renewwal = renewwalTimes(loanConfig, orderId);
            if (null != renewwal) {
                return renewwal;
            }
            UserCardInfo cardInfo = hasChanjieBindCard(userInfo.getId());
            if (cardInfo == null) {
                // 如果未绑卡，则要求前端进行跳转
                resultMap.put("cardBinded", 0);
            }
            OrderRepayment orderRepayment = orderRepaymentService.findOrderRecordDetailById(orderId);
            if (null != orderRepayment) {

                //金额转换元
                BigDecimal changeFee = new BigDecimal(100.0);
                //减免金额
                int reduceAmount = orderRepayment.getReduceAmount();
                resultMap.put("reductionMoney", 0 != reduceAmount ? new BigDecimal(reduceAmount).divide(changeFee, 2, BigDecimal.ROUND_HALF_UP) : 0);
                //还款订单ID
                resultMap.put("orderRepaymentId", orderRepayment.getId());
                //原还款日
                resultMap.put("repaymentTime", DateUtil.formatTimeYmd(orderRepayment.getRepaymentTime()));
                //续期天数(如果系统配置为空，就用默认续期期限6天)
                Integer renewalExpire = null != loanConfig && null != loanConfig.getRenewalExpire() ? loanConfig.getRenewalExpire() : Constant.RENEWAL_EXPIRE;
                //新还款日(=原还款日+系统允许的延期天数)
                resultMap.put("newRepaymentTime", DateUtil.formatTimeYmd(DateUtil.addDay(orderRepayment.getRepaymentTime(), renewalExpire)));
                /**到期应还金额（应还金额 = 总待还金额 - 已还金额 - 减免金额 ）*/
                Double repaymentAmount = (orderRepayment.getRepaymentAmount() - orderRepayment.getPaidAmount() - reduceAmount) / Constant.CHANGE_CENT;
                BigDecimal repaymentAmounts = new BigDecimal(repaymentAmount);
                resultMap.put("repaymentAmount", repaymentAmounts.setScale(2, BigDecimal.ROUND_HALF_UP));
                //延期还款手续费
                Double renewalFee = Constant.RENEWA_FEE;
                if (null != loanConfig) {
                    renewalExpire = loanConfig.getRenewalExpire();
                    renewalFee = loanConfig.getRenewalFee();
                }
                //延期天数
                resultMap.put("renewalExpire", renewalExpire);
                //续期手续费
                BigDecimal renewal = new BigDecimal(renewalFee);
                resultMap.put("renewalFee", renewal.setScale(2, BigDecimal.ROUND_HALF_DOWN));
                //延期还款服务费
                BigDecimal feeAmount = new BigDecimal((orderRepayment.getFeeAmount() / Constant.CHANGE_CENT));
                resultMap.put("feeAmount", feeAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                resultMap.put("userId", userInfo.getId());
                //支付宝开关，默认银行卡还款
                int repaySwitch = 0;
                BackConfigParamsVo repaySwitchConfig = backConfigParamsService.findBySysKey(Constant.REPAY_SWITCH);
                if (null != repaySwitchConfig) {
                    repaySwitch = repaySwitchConfig.getSysValue();
                }
                resultMap.put("repaySwitch", repaySwitch);
                //银行卡信息
                resultMap.put("bankInfo", cardInfo.getBankName() + "(" + StringUtil.subString(cardInfo.getCardNo(), 4) + ")");
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
            }
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, CallBackResult.MSG_DATA_EMPTY);
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 续期次数判断
     *
     * @param loanConfig
     * @param orderId
     * @return
     */
    public Object renewwalTimes(LoanRuleConfig loanConfig, Integer orderId) {
        if (null != loanConfig) {
            int hightestRenewal = loanConfig.getHightestRenewal();
            //最多续期次数，如果是0表示无限续期
            if (hightestRenewal != 0) {
                //延期次数通过还款id查询续期表中多少条
                List<OrderRenewal> OrderRenewals = orderRenewalService.findByRepaymentId(orderId);
                if (null != OrderRenewals && OrderRenewals.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    //续期过的次数
                    int renewalsTimes = OrderRenewals.size();
                    if (renewalsTimes >= hightestRenewal) {
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "当前订单续期次数达到上限，暂时无法续期！");
                    }
                }
            }
        }
        return null;
    }

}
