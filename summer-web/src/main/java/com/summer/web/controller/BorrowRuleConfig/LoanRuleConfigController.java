package com.summer.web.controller.BorrowRuleConfig;

import com.alibaba.fastjson.JSONObject;
import com.summer.annotation.Log;
import com.summer.api.service.IBackConfigParamsService;
import com.summer.api.service.IIncreaseMoneyConfigService;
import com.summer.api.service.ILoanRuleConfigService;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.IncreaseMoneyConfig;
import com.summer.dao.entity.LoanRuleConfig;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.UserMoneyRate;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.pojo.dto.DeleteIncreaseMoneyDto;
import com.summer.pojo.dto.IncreaseMoneyConfigDto;
import com.summer.pojo.dto.LoanRuleConfigDto;
import com.summer.util.*;

import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.pojo.vo.IncreaseMoneyConfigVo;
import com.summer.pojo.vo.LoanRuleConfigVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贷款规则控制器
 *
 * @author
 */

@Slf4j
@RestController
@RequestMapping("/v1.0/api")
public class LoanRuleConfigController extends BaseController {

    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private IIncreaseMoneyConfigService iIncreaseMoneyConfigService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;

    /**
     * 获取借款配置
     *
     * @return
     */
    @GetMapping("/loan/rule")
    public Object getLoanRule(HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        //查询根据复贷次数提额配置数据
        List<IncreaseMoneyConfigVo> allIncreaseConfig = iIncreaseMoneyConfigService.findAllIncreaseConfig(Constant.REPETITION_TYPE);
     /*   Map<String, Object> params = new HashMap<>();
        params.put("status",Constant.INCRESE_MONNEY_CONFIG_STATUS);*/
        //查询贷款规则
        /* LoanRuleConfig loanConfig = loanRuleConfigService.findLoanConfigByParams(params);*/
        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(Constant.CHANNELRULE_ID);
        Map<String, Object> resultMap = new HashMap<>();
        if (null != loanConfig) {
            LoanRuleConfigVo loanRuleConfigVo = new LoanRuleConfigVo();
            //id
            loanRuleConfigVo.setId(loanConfig.getId());
            //期限
            loanRuleConfigVo.setExpire(loanConfig.getExpire());
            //借款利息费率
            loanRuleConfigVo.setBorrowInterest(DataTransform.changeBigDouble(loanConfig.getBorrowInterest()));
            //借款服务费费率
            loanRuleConfigVo.setServiceCharge(DataTransform.changeBigDouble(loanConfig.getServiceCharge()));
            //最多借款
            loanRuleConfigVo.setLoanAmount(loanConfig.getLoanAmount() / Constant.CENT_CHANGE_DOLLAR);
            //滞纳金额利率（占总金额的百分比）
            loanRuleConfigVo.setOverdueRate(DataTransform.changeBigDouble(loanConfig.getOverdueRate()));
            //最高滞纳金额费率（占总金额的百分比）
            loanRuleConfigVo.setHighestOverdueRate(DataTransform.changeSmallDouble(loanConfig.getHighestOverdueRate()));
            //最高续期期限(0表示无限续期)
            loanRuleConfigVo.setHightestRenewal(loanConfig.getHightestRenewal());
            //命中风控（0表示永久不能再借：备注{“ 1以上表示命中风控XX天后能再借 ” }）
            loanRuleConfigVo.setHitRiskAllowBorrowDay(loanConfig.getHitRiskAllowBorrowDay());
            //命中黑名单（0表示永久不能再借 ：备注{“ 1以上表示命中黑名单XX天后能再借 ” }）
            loanRuleConfigVo.setHitBlackAllowBorrowDay(loanConfig.getHitBlackAllowBorrowDay());
            //正常还款是否可以复贷（0表示是，1表示否）
            loanRuleConfigVo.setNormalRepaymentRepetitionLoan(loanConfig.getNormalRepaymentRepetitionLoan());
            //续期服务费
            loanRuleConfigVo.setRenewalFee(loanConfig.getRenewalFee());
            //续期期限
            loanRuleConfigVo.setRenewalExpire(loanConfig.getRenewalExpire());

            Map<String, Object> OverdueRepetitionLoan = JSONObject.parseObject(loanConfig.getOverdueRepaymentRepetitionLoan());
            //返回逾期7天以下还款是否可以复贷（0表示不能复贷，1表示可以复贷）
            loanRuleConfigVo.setIsAllowLoan((Integer) OverdueRepetitionLoan.get("isAllowLoan"));
            //当isAllowLoan=1：表示逾期可以复贷时，必须同时满足是逾期天数小于overdueDay（目前是7）天以下的
            loanRuleConfigVo.setOverdueDay((Integer) OverdueRepetitionLoan.get("overdueDay"));
            resultMap.put("loanConfig", loanRuleConfigVo);
            //返回根据复贷次数提额降息集合
            resultMap.put("repetitionIncreaseList", allIncreaseConfig);
            //续期开关
            BackConfigParamsVo renewalSwitch = backConfigParamsService.findBySysKey(Constant.RENEWAL_SWITCH);
            loanRuleConfigVo.setRenewalSwitch(renewalSwitch);
            //支付开关
            BackConfigParamsVo repaySwitch = backConfigParamsService.findBySysKey(Constant.REPAY_SWITCH);
            loanRuleConfigVo.setRepaySwitch(repaySwitch);
            // 利息前置/后置开关
            BackConfigParamsVo interest = backConfigParamsService.findBySysKey(Constant.INTEREST);
            loanRuleConfigVo.setInterest(interest);
            // resultMap.put("renewalSwitch",renewalSwitch);
            BackConfigParamsVo bcpvo = backConfigParamsService.findBySysKey("retreat_Money");
            if(null != bcpvo){
                resultMap.put("retreatMoney",bcpvo);
            }

            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
    }

    /**
     * 修改贷款规则(根据正常还款次数和逾期坏款次数  ,目前根据复贷次数)
     *
     * @param loanRuleConfigDto
     * @return
     * @throws Exception
     */
    @Log(title = "保存贷款规则")
    @PostMapping("/update/loan/rule")
    public Object update(@Validated @RequestBody LoanRuleConfigDto loanRuleConfigDto, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        // log.info("【保存贷款规则】-----phone={},ip={},param={}",platformUser.getPhoneNumber(),IpAdrressUtil.getIpAdrress(request), JSONObject.toJSONString(loanRuleConfigDto));
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "updateLoanRule_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("保存贷款规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        //密码校验
        String password = loanRuleConfigDto.getPassword();
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("保存贷款规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }

        if (!FigureUtil.isMulByhundred(loanRuleConfigDto.getLoanAmount())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "借款金额必须是100的整数倍");
        }
        //金额上下限，限制
        String moneylimit = moneylimit(loanRuleConfigDto.getLoanAmount());
        if (null != moneylimit) {
            return moneylimit;
        }

        LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(0);
        LoanRuleConfig loanRuleConfig = new LoanRuleConfig();
        //设置规则的id
        loanRuleConfig.setId(loanRuleConfigDto.getId());
        //设置最多借款金额（转换为分:借款金额 * 100 ）
        loanRuleConfig.setLoanAmount(loanRuleConfigDto.getLoanAmount() * Constant.DOLLAR_CHANGE_PENNY);
        //设置借款期限
        loanRuleConfig.setExpire(loanRuleConfigDto.getExpire());
        //借款服务费
        loanRuleConfig.setServiceCharge(DataTransform.changeSmallDouble(loanRuleConfigDto.getServiceCharge()));
        //设置借款利息
        loanRuleConfig.setBorrowInterest(DataTransform.changeSmallDouble(loanRuleConfigDto.getBorrowInterest()));
        //设置逾期一天的利率
        loanRuleConfig.setOverdueRate(DataTransform.changeSmallDouble(loanRuleConfigDto.getOverdueRate()));
        //设置续期期限
        loanRuleConfig.setRenewalExpire(loanRuleConfigDto.getRenewalExpire());
        //最高滞纳金额（滞纳金额 < 最高滞纳金额）
        loanRuleConfig.setHighestOverdueRate(DataTransform.changeBigDouble(loanRuleConfigDto.getHighestOverdueRate()));
        //设置续期费（主要指手续费）
        loanRuleConfig.setRenewalFee(loanRuleConfigDto.getRenewalFee());
        //最多续期
        loanRuleConfig.setHightestRenewal(loanRuleConfigDto.getHightestRenewal());
        //设置命中风控允许借款天数（0表示永久不能再借）
        loanRuleConfig.setHitRiskAllowBorrowDay(loanRuleConfigDto.getHitRiskAllowBorrowDay());
        //设置命中黑明单后允许借款天数（0表示永久不能再借）
        loanRuleConfig.setHitBlackAllowBorrowDay(loanRuleConfigDto.getHitBlackAllowBorrowDay());
        //设置正常还款是否可以复贷（0表示是：可以复贷，1表示否：不能复贷）
        loanRuleConfig.setNormalRepaymentRepetitionLoan(loanRuleConfigDto.getNormalRepaymentRepetitionLoan());
        HashMap<String, Object> overdueRepaymentMap = new HashMap<>();
        //逾期7天以下还款是否可以复贷（0表示不能复贷，1表示可以复贷）
        overdueRepaymentMap.put("isAllowLoan", loanRuleConfigDto.getIsAllowLoan());
        //当isAllowLoan=1：表示逾期可以复贷时，必须同时满足是逾期天数小于overdueDay（目前是7）天以下的
        overdueRepaymentMap.put("overdueDay", loanRuleConfigDto.getOverdueDay());
        loanRuleConfig.setOverdueRepaymentRepetitionLoan(JSONObject.toJSONString(overdueRepaymentMap));
        /**更新提额降息信息表*/
        int status = updateIncreaseMoney(loanRuleConfigDto.getIncreaseMoneyConfigs());
        /**提额的成功，再继续修改借款规则表loan_rule_config*/
        int state = loanRuleConfigService.updateByPrimaryKeySelective(loanRuleConfig);
        if (status > 0 && state > 0) {
            UserMoneyRate userMoneyRate = new UserMoneyRate();
            //最大借款金额
            userMoneyRate.setMaxAmount(loanRuleConfig.getLoanAmount());
            //服务费费率
            //  userMoneyRate.setServiceCharge(loanRuleConfig.getServiceCharge());
            //利息费率
            // userMoneyRate.setAccrual(loanRuleConfig.getBorrowInterest());
            //更新时间
            userMoneyRate.setUpdateTime(new Date());
            //修改用户费率表信息
            // userMoneyRateService.updateMaxAmountAndServiceCharge(userMoneyRate,loanConfig);
            /**根据修改的贷款规则配置复贷达到的次数去更新用户费率表user_money_rate（已在还款成功时，进行了提额）*/
            //userMoneyRateService.updateAmountAccrualByTimes(loanRuleConfigDto);
            //把对应风控分数存入Redis，供应复审使用
            if (!CollectionUtils.isEmpty(loanRuleConfigDto.getIncreaseMoneyConfigs())) {
                Map<String, String> keys = new HashMap<>();
                if (redisUtil.hasKey(Constant.SCORE_GATEWAY_REDIS_KEY)) {
                    String score = redisUtil.get(Constant.SCORE_GATEWAY_REDIS_KEY);
                    keys = JSONObject.parseObject(score, Map.class);
                } else {
                    //把之前新用户风控相关缓存进Redis
                    HashMap<String, Object> sysParams = new HashMap<>();
                    sysParams.put("sysType", Constant.BACK_CONFIG_TYPE);
                    List<BackConfigParams> BackConfigParams = backConfigParamsService.findParams(sysParams);
                    if (!CollectionUtils.isEmpty(BackConfigParams)) {
                        for (BackConfigParams backConfigParam : BackConfigParams) {
                            keys.put(backConfigParam.getSysKey(), backConfigParam.getSysValue());
                        }
                    }
                }
                for (IncreaseMoneyConfigDto increaseMoneyConfigDto : loanRuleConfigDto.getIncreaseMoneyConfigs()) {
                    keys.put("OLD_SCORE_" + increaseMoneyConfigDto.getAchieveTimes(), increaseMoneyConfigDto.getRiskScore() + "");
                }
                //把类型为：RISK_PAIXU 数据存入Redis
                redisUtil.set(Constant.SCORE_GATEWAY_REDIS_KEY, JSONObject.toJSONString(keys));
            }
            //每次修改成功需要先清除下之前的首页配置数据缓存
            deleteIndexInfo();
            return CallBackResult.returnJson(CallBackResult.CREATED, CallBackResult.MSG_SUCCESS);
        }
        return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, CallBackResult.MSG_INVALID_REQUEST);
    }

    /**
     * 添加，更新提额的方法（最新需求目前根据复贷次数，   最初需求是根据正常还款次数和逾期还款次数）
     */
    public int updateIncreaseMoney(List<IncreaseMoneyConfigDto> increaseMoneyConfigs) throws Exception {
        Date nowTime = new Date();
        //新增或更新影响状态
        int status = 0;
        for (IncreaseMoneyConfigDto increaseMoneyConfigDto : increaseMoneyConfigs) {
            //当提额降息id为空时 表示新增了一条提额降息规则
            if (null == increaseMoneyConfigDto.getId()) {
                IncreaseMoneyConfig increaseMoneyConfig = new IncreaseMoneyConfig();
                //复贷达到的次数
                increaseMoneyConfig.setAchieveTimes(increaseMoneyConfigDto.getAchieveTimes());
                //提升后的金额
                increaseMoneyConfig.setRepetitionInreaseMoney(increaseMoneyConfigDto.getRepetitionInreaseMoney() * Constant.DOLLAR_CHANGE_PENNY);
                //降低后的利息
                increaseMoneyConfig.setReduceInterest(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getReduceInterest()));
                //提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）
                increaseMoneyConfig.setIncreaseType(increaseMoneyConfigDto.getIncreaseType());
                //创建时间
                increaseMoneyConfig.setCreateTime(nowTime);
                //风控分数
                increaseMoneyConfig.setRiskScore(increaseMoneyConfigDto.getRiskScore());
                //状态
                increaseMoneyConfig.setStatus(increaseMoneyConfigDto.getStatus());
                //服务费率
                increaseMoneyConfig.setServiceMoney(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getServiceMoney()));
                /**increase_money_config表*/
                status = iIncreaseMoneyConfigService.insertSelective(increaseMoneyConfig);
            } else {
                //反之 id 不为空，表示在修改原有的提额降息数据
                IncreaseMoneyConfig increaseMoneyConfig = new IncreaseMoneyConfig();
                increaseMoneyConfig.setId(increaseMoneyConfigDto.getId());
                //复贷达到的次数
                increaseMoneyConfig.setAchieveTimes(increaseMoneyConfigDto.getAchieveTimes());
                //提升后的金额
                increaseMoneyConfig.setRepetitionInreaseMoney(increaseMoneyConfigDto.getRepetitionInreaseMoney() * Constant.DOLLAR_CHANGE_PENNY);
                //降低后的利息
                increaseMoneyConfig.setReduceInterest(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getReduceInterest()));
                //提额类型，（0表示正常还款次数，1表示逾期还款次数,2表示复贷次数）
                increaseMoneyConfig.setIncreaseType(increaseMoneyConfigDto.getIncreaseType());
                //更新时间
                increaseMoneyConfig.setUpdateTime(nowTime);
                //风控分数
                increaseMoneyConfig.setRiskScore(increaseMoneyConfigDto.getRiskScore());
                //状态
                increaseMoneyConfig.setStatus(increaseMoneyConfigDto.getStatus());
                //服务费率
                increaseMoneyConfig.setServiceMoney(DataTransform.changeSmallDouble(increaseMoneyConfigDto.getServiceMoney()));
                /**increase_money_config表*/
                status = iIncreaseMoneyConfigService.updateByPrimaryKeySelective(increaseMoneyConfig);
            }
        }
        return status;
    }

    /**
     * 修改贷款规则配置后，需要删除下原来的首页缓存数据(新修改的数据才会实时展示在首页（指用户未登录时的首页配置数据），
     * 否则需要手动在Redis客户端进行删除对应的首页配置数据删除，)
     *
     * @return
     */

    public void deleteIndexInfo() {
        if (redisUtil.hasKey(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY)) {
            redisUtil.del(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY);
        }
    }

    /**
     * 删除提额降息
     *
     * @param
     * @return
     */
    @Log(title = "删除提额信息")
    @PostMapping("/delete/increaseMoney/rule")
    public String deleteIncreaseMoney(@RequestBody DeleteIncreaseMoneyDto deleteIncreaseMoneyDto, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能删除
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        int isSucc = iIncreaseMoneyConfigService.deleteByPrimaryKey(deleteIncreaseMoneyDto.getId());
        if (isSucc > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_NO_CONTENT_SUCCESS);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, CallBackResult.MSG_INVALID_REQUEST_FAIL);
    }

    /**
     * 修改提额信息
     *
     * @param increaseMoneyConfig
     * @return
     */
    @Log(title = "修改提额信息")
    @PostMapping("/increase/update/status")
    public String updateIncrease(@RequestBody IncreaseMoneyConfig increaseMoneyConfig, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        Integer id = increaseMoneyConfig.getId();
        increaseMoneyConfig.setUpdateTime(new Date());
        if (null == id) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }
        int isSucc = iIncreaseMoneyConfigService.updateByPrimaryKeySelective(increaseMoneyConfig);
        if (isSucc > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }

    /**
     * 一键开启或者关闭
     *
     * @return
     */
    @Log(title = "自动提额开关")
    @GetMapping("/create/update/status")
    public String updateIncreaseState(@NotNull(message = "参数不合法") Integer status, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        int isSucc = iIncreaseMoneyConfigService.updateByStatus(status);
        if (isSucc > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }

    /**
     * 借款金额限制
     *
     * @param loanMoney
     * @return
     */
    public String moneylimit(Integer loanMoney) {
        Integer limitStart = 0;
        Integer limitend = 10000;
        //最小金额
        BackConfigParamsVo loanLimitStart = backConfigParamsDao.findBySysKey("loan_limitStart");
        if (null != loanLimitStart) {
            limitStart = loanLimitStart.getSysValue();
        }
        //最大金额
        BackConfigParamsVo loanLimitEnd = backConfigParamsDao.findBySysKey("loan_limitEnd");
        if (null != loanLimitEnd) {
            limitend = loanLimitEnd.getSysValue();
        }

        if (FigureUtil.isGthundred(loanMoney, limitStart, limitend)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "保存失败，借款金额必须大于" + limitStart + "，小于" + limitend + "元！");
        }
        return null;
    }


    @GetMapping("/loan/findUserMoneyInfo")
    @ResponseBody
    public Object findUserMoneyInfo(@RequestParam Integer userId) {
    	if(null == userId || "".equals(userId)) {
    		return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "userId不能为空");
    	}
    	Map<String, Object> resultMap = new HashMap<>();
    	int i = 0;
    	Integer count = loanRuleConfigService.findIncreaseMoneyConfigCount();
    	UserMoneyRate userMoneyRate = loanRuleConfigService.findUserMoneyInfo(userId);
    	LoanRuleConfig loanConfig = loanRuleConfigService.findByChannelId(Constant.CHANNELRULE_ID);
    	if(userMoneyRate.getRepetitionTimes() == count) {
    		i = userMoneyRate.getRepetitionTimes();
    	}else {
    		i = i + 1;
    	}
    	IncreaseMoneyConfig increaseMoneyConfig = loanRuleConfigService.findIncreaseMoneyConfig(userMoneyRate.getRepetitionTimes() + i);
    	if(userMoneyRate.getMaxAmount() < 0 || userMoneyRate.getMaxAmount() == null) {
    		resultMap.put("amount", loanConfig.getLoanAmount());
    		resultMap.put("maxAmount", loanConfig.getLoanAmount() + (null != increaseMoneyConfig ? increaseMoneyConfig.getRepetitionInreaseMoney() : 0));
    	}else {
    		resultMap.put("amount", userMoneyRate.getMaxAmount());
    		resultMap.put("maxAmount", userMoneyRate.getMaxAmount() + (null != increaseMoneyConfig ? increaseMoneyConfig.getRepetitionInreaseMoney() : 0));
    	}
		return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
	}


    /**
     * 修改提额信息
     *
     * @param
     * @return
     */
    @Log(title = "修改退款金额")
    @PostMapping("/update/retreatMoney")
    public String UpretreatMoney(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管才能修改
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "对不起，您没有该权限！");
        }
        Map<String, Object> param = JSONObject.parseObject(jsonData);

        BackConfigParams vo = new BackConfigParams();
        vo.setId(Integer.parseInt(param.get("id").toString()));
        vo.setSysValue(param.get("money").toString());
        log.info("修改退款金额，id={},money={}",param.get("id").toString(),param.get("money").toString());
        int isSucc = backConfigParamsDao.updateById(vo);
        if (isSucc > 0) {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.UPDATE_SUCC);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }

}
