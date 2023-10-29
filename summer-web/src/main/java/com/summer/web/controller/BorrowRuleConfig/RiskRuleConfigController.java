package com.summer.web.controller.BorrowRuleConfig;

import com.alibaba.fastjson.JSONObject;
import com.summer.annotation.Log;
import com.summer.enums.BackConfigParamsEnum;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.RiskRuleConfig;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.pojo.dto.UpdateRiskRuleDto;
import com.summer.api.service.IBackConfigParamsService;
import com.summer.api.service.risk.IRiskRuleConfigService;
import com.summer.util.*;
import com.summer.util.log.StringUtils;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;


/**
 * 风控规则配置
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1.0/api")
public class RiskRuleConfigController extends BaseController {

    @Resource
    private IRiskRuleConfigService riskRuleConfigService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取风控规则列表
     *
     * @return
     */
    @GetMapping("/risk/rule")
    public Object getRiskRule(HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        List<RiskRuleConfig> allRiskRule = riskRuleConfigService.findAllRiskRule();
        HashMap<Object, Object> reuslMap = new HashMap<>();
        BackConfigParamsVo scoreGateway = backConfigParamsService.findBySysKey(Constant.SCORE_GATEWAY);
        BackConfigParamsVo autoScoreGateway = backConfigParamsService.findBySysKey(BackConfigParamsEnum.AUTO_SCORE_GATEWAY.getKey());
        BackConfigParamsVo natureChannel = backConfigParamsService.findBySysKey(Constant.NATURE_CHANNEL);
        BackConfigParamsVo reviewSwitch = backConfigParamsDao.findBySysKey("REVIEW_JS");
        BackConfigParamsVo scoreType = backConfigParamsDao.findBySysKey("mobile_switch");
        // BackConfigParamsVo oldScore = backConfigParamsService.findBySysKey(Constant.OLD_SCORE);
        BackConfigParamsVo overdueRepayment = backConfigParamsDao.findBySysKey(Constant.OVERDUE_REPAYMENT_KEY);
        BackConfigParamsVo mzSwitch = backConfigParamsDao.findBySysKey("mz_switch");
        BackConfigParamsVo usdt_df = backConfigParamsDao.findBySysKey("usdt_df");
        BackConfigParamsVo repayBf = backConfigParamsDao.findBySysKey("repay_bf");
        BackConfigParamsVo passwordSwitch = backConfigParamsDao.findBySysKey(Constant.APP_UPDATE_PASSWORD);
        //初审风控规则
        reuslMap.put("riskList", allRiskRule);
        //复审风控分数
        reuslMap.put("risk", scoreGateway);
        reuslMap.put("riskAutoScoreGetWay", autoScoreGateway);
        //复审自然分数
        reuslMap.put("nature", natureChannel);
        reuslMap.put("reviewSwitch", reviewSwitch);
        reuslMap.put("riskChoose", scoreType);
        //逾期是否可以复贷开关
        reuslMap.put("overdueSwitch", overdueRepayment);
        //魔杖开关
        reuslMap.put("mzSwitch", mzSwitch);
        reuslMap.put("usdt_df", usdt_df);
        reuslMap.put("repayBf", repayBf);
        reuslMap.put("repayBf", repayBf);
        reuslMap.put("oldCustomCheck", backConfigParamsDao.findBySysKey(BackConfigParamsEnum.OLD_CUSTOMER_CHECK.getKey()));
        reuslMap.put("passwordSwitch", passwordSwitch);

        // 新用户自动申请借款
        BackConfigParamsVo autoBorrow = backConfigParamsDao.findBySysKey("auto_borrow");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(autoBorrow.getStrValue()) && autoBorrow.getStrValue().equals("0")) {
            autoBorrow.setSysValue(0);
        } else {
            autoBorrow.setSysValue(1);
        }
        reuslMap.put("autoBorrow", autoBorrow);
        // 老用户自动申请借款
        BackConfigParamsVo oldAuto = backConfigParamsDao.findBySysKey("old_auto");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(oldAuto.getStrValue()) && oldAuto.getStrValue().equals("0")) {
            oldAuto.setSysValue(0);
        } else {
            oldAuto.setSysValue(1);
        }
        reuslMap.put("oldAuto", oldAuto);
        BackConfigParamsVo backConfig = backConfigParamsService.findBySysKey(Constant.OCR_SELFILE);
        if(backConfig !=null){
            reuslMap.put("orcSwitch", backConfig);
        }

        //老用户风控分数
        // reuslMap.put("oldScore",oldScore);

        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, reuslMap);
    }

    /**
     * 修改风控规则(v1.0版本暂时只允许修改启用或者关闭该规则)
     *
     * @return
     */
    @Log(title = "修改风控规则状态")
    @PutMapping("/update/risk/rule")
    public Object updateRiskRule(
            @NotNull(message = "风控规则的id不能为空")
                    Integer id,
            @NotNull(message = "风控规则的状态不能为空") Integer status, HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        log.info("【修改风控规则状态】-----phone={},ip={},id={},status={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), id, status);
        RiskRuleConfig riskRuleConfig = new RiskRuleConfig();
        riskRuleConfig.setId(id);
        byte state = Byte.parseByte(status.toString());
        riskRuleConfig.setStatus(state);
        int isSucc = riskRuleConfigService.updateByPrimaryKeySelective(riskRuleConfig);
        if (isSucc > 0) {
            return StatusResultUtil.isSucc(state);
        }
        return StatusResultUtil.isBad(state);
    }

    /**
     * 人工复审开关
     *
     * @return
     */
    @Log(title = "修改人工复审开关")
    @RequestMapping("/update/risk/switch")
    public Object reviewSwitch(@RequestBody String jsonData, HttpServletRequest request) {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "updateRiskSwitch_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("修改人工复审开关密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        Map<String, Object> param = JSONObject.parseObject(jsonData);
        Object status = param.get("status");
        if (null == status) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        String password = (String) param.get("password");
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("修改人工复审开关密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }

        //  log.info("【修改人工复审开关】-----phone={},ip={},status={}",platformUser.getPhoneNumber(),IpAdrressUtil.getIpAdrress(request),status);
        int statusInt = Integer.parseInt(status.toString());
        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setSysKey("REVIEW_JS");
        backConfigParams.setSysValue(statusInt == 1 ? "1" : "0");
        int isSucc = backConfigParamsDao.updateBySyskey(backConfigParams);
        if (isSucc > 0) {
            return CallBackResult.returnJson("操作成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /**
     * 老客是否需要过风控
     *
     * @return
     */
    @Log(title = "老客是否需要过风控")
    @RequestMapping("/update/risk/oldCustomerCheck")
    public Object oldCustomerCheck(@RequestBody String jsonData, HttpServletRequest request) {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "updateRiskSwitch_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("修改人工复审开关密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        Map<String, Object> param = JSONObject.parseObject(jsonData);
        Object status = param.get("status");
        if (null == status) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        String password = (String) param.get("password");
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("修改人工复审开关密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
        } else {
            redisUtil.del(key);
        }

        //  log.info("【修改人工复审开关】-----phone={},ip={},status={}",platformUser.getPhoneNumber(),IpAdrressUtil.getIpAdrress(request),status);
        int statusInt = Integer.parseInt(status.toString());
        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setSysKey(BackConfigParamsEnum.OLD_CUSTOMER_CHECK.getKey());
        backConfigParams.setSysValue(statusInt == 1 ? "1" : "0");
        int isSucc = backConfigParamsDao.updateBySyskey(backConfigParams);
        if (isSucc > 0) {
            return CallBackResult.returnJson("操作成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /**
     * 风控类型
     *
     * @return
     */
    @Log(title = "风控类型修改")
    @GetMapping("/update/risk/choose")
    public Object choose(Integer status, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        log.info("【风控类型修改】-----phone={},ip={},status={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), status);
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        if (null == status) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setSysKey("mobile_switch");

        backConfigParams.setSysValue(status.toString());
        int isSucc = backConfigParamsDao.updateBySyskey(backConfigParams);
        if (isSucc > 0) {
            return CallBackResult.returnJson("操作成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /**
     * 一键关闭风控规则
     *
     * @param status
     * @return
     */
    @Log(title = "一键开启或关闭风控规则")
    @PostMapping("/delete/risk")
    public Object updateAllState(@Validated @RequestBody UpdateRiskRuleDto status, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登陆失效，请重新登陆！");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        log.info("【一键开启或关闭风控规则】-----phone={},ip={},param={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), JSONObject.toJSONString(status));
        byte state = Byte.parseByte(status.getStatus().toString());
        int isSucc = riskRuleConfigService.updateAllState(state);
        if (isSucc > 0) {
            return StatusResultUtil.unifyIsSucc(state);
        }
        return StatusResultUtil.unifyIsBad(state);
    }

    /**
     * 更新风控阈值
     *
     * @return
     */
    @Log(title = "更新复审风控规则")
    @PostMapping("/updateRiskScore")
    public String updateRiskScore(@RequestBody String jsonData, HttpServletRequest request) {
        String successMsg = Constant.UPDATE_SUCC;
        Map<String, Object> param = JSONObject.parseObject(jsonData);

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "登录失效");
        }

        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        log.info("【更新复审风控规则】-----phone={},ip={},param={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), param);

        //风控分数
        Object risk = param.get("risk");
        //达自动放款阈值风控分数
        Object riskAutoScoreGetWay = param.get("riskAutoScoreGetWay");
        //自然流量风控分数
        Object nature = param.get("nature");
        //逾期是否可以复贷开关
        Object overdueSwitch = param.get("overdueSwitch");
        //魔杖开关
        Object mzSwitch = param.get("mzSwitch");
        //续期开关
        Object renewalSwitch = param.get("renewalSwitch");
        //支付开关
        Object repaySwitch = param.get("repaySwitch");
        // 利息前置/后置开关
        Object interest = param.get("interest");
        //APP用户密码重置开关
        Object passwordSwitch = param.get("passwordSwitch");


        List<BackConfigParams> backConfigParams = new ArrayList<>();

        //APP用户密码重置开关
        if (null != passwordSwitch) {
            // 超管密码
            String pass = backConfigParamsDao.findStrValue("password");
            // 密码错误次数验证
            String key = "APP_passwordSwitch_" + platformUser.getPhoneNumber() + "_" + pass + "_" + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
            Object times = redisUtil.getObject(key);
            if (times != null && (Integer) times > 5) {
                log.info("更新复审风控规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
            }
            String password = (String) param.get("password");
            if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
                log.info("更新复审风控规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
                // 密码失败次数记录
                LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
            } else {
                redisUtil.del(key);
            }
            BackConfigParamsVo passwordConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(passwordSwitch), BackConfigParamsVo.class);
            BackConfigParams riskbackConfigParams = new BackConfigParams();
            if (null != passwordConfigParamsVo) {
                riskbackConfigParams.setId(passwordConfigParamsVo.getId());
                riskbackConfigParams.setSysValue(passwordConfigParamsVo.getSysValue().toString());
                backConfigParams.add(riskbackConfigParams);
            }

        }


        //风控分数
        if (null != risk || Objects.nonNull(riskAutoScoreGetWay)) {
            // 超管密码
            String pass = backConfigParamsDao.findStrValue("password");
            // 密码错误次数验证
            String key = "updateRiskScore_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
            Object times = redisUtil.getObject(key);
            if (times != null && (Integer) times > 5) {
                log.info("更新复审风控规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
            }
            String password = (String) param.get("password");
            if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
                log.info("更新复审风控规则密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
                // 密码失败次数记录
                LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
                return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "账号或密码错误！");
            } else {
                redisUtil.del(key);
            }
            BackConfigParamsVo riskbackConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(risk), BackConfigParamsVo.class);
            BackConfigParams riskbackConfigParams = new BackConfigParams();
            if (null != riskbackConfigParamsVo) {
                riskbackConfigParams.setId(riskbackConfigParamsVo.getId());
                riskbackConfigParams.setSysValue(riskbackConfigParamsVo.getSysValue().toString());
                backConfigParams.add(riskbackConfigParams);
            }

            // 风控分达到自动放款分更新
            BackConfigParamsVo riskAutoScoreGetWaybackConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(riskAutoScoreGetWay), BackConfigParamsVo.class);
            BackConfigParams riskAutoScoreGetWaybackConfigParams = new BackConfigParams();
            if (null != riskAutoScoreGetWaybackConfigParamsVo) {
                riskAutoScoreGetWaybackConfigParams.setId(riskAutoScoreGetWaybackConfigParamsVo.getId());
                riskAutoScoreGetWaybackConfigParams.setSysValue(riskAutoScoreGetWaybackConfigParamsVo.getSysValue().toString());
                backConfigParams.add(riskAutoScoreGetWaybackConfigParams);
            }
        }

        //自然渠道分数
        if (null != nature) {
            BackConfigParamsVo naturebackConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(nature), BackConfigParamsVo.class);
            BackConfigParams naturebackConfigParams = new BackConfigParams();
            if (null != naturebackConfigParamsVo) {
                naturebackConfigParams.setId(naturebackConfigParamsVo.getId());
                naturebackConfigParams.setSysValue(naturebackConfigParamsVo.getSysValue().toString());
                backConfigParams.add(naturebackConfigParams);
            }
        }
        //修改逾期是否可以复贷开关
        if (null != overdueSwitch) {
            BackConfigParamsVo overdueSwitchVo = JSONObject.parseObject(JSONObject.toJSONString(overdueSwitch), BackConfigParamsVo.class);
            BackConfigParams overdueSwitchParams = new BackConfigParams();
            if (null != overdueSwitchVo) {
                overdueSwitchParams.setId(overdueSwitchVo.getId());
                overdueSwitchParams.setSysValue(overdueSwitchVo.getSysValue().toString());
                backConfigParams.add(overdueSwitchParams);
            }
        }
        //修改mz认证开关
        if (null != mzSwitch) {
            BackConfigParamsVo mzConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(mzSwitch), BackConfigParamsVo.class);
            if (null != mzConfigParamsVo) {
                BackConfigParams mzSwitchParams = new BackConfigParams();
                mzSwitchParams.setId(mzConfigParamsVo.getId());
                mzSwitchParams.setSysValue(mzConfigParamsVo.getSysValue().toString());
                backConfigParams.add(mzSwitchParams);
            }
        }

        //续期开关
        if (null != renewalSwitch) {
            BackConfigParamsVo renewalConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(renewalSwitch), BackConfigParamsVo.class);
            if (null != renewalSwitch) {
                BackConfigParams renewalSwitchParams = new BackConfigParams();
                renewalSwitchParams.setId(renewalConfigParamsVo.getId());
                renewalSwitchParams.setSysValue(renewalConfigParamsVo.getSysValue().toString());
                String sysVale = renewalConfigParamsVo.getSysValue().toString();
                if ("0".equals(sysVale)) {
                    successMsg = "续期开启成功！";
                } else {
                    successMsg = "续期关闭成功！";
                }
                backConfigParams.add(renewalSwitchParams);
            }
        }
        //支付宝开关
        if (null != repaySwitch) {
            BackConfigParamsVo repaySwitchConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(repaySwitch), BackConfigParamsVo.class);
            BackConfigParams repaySwitchParams = new BackConfigParams();
            repaySwitchParams.setId(repaySwitchConfigParamsVo.getId());
            String value = repaySwitchConfigParamsVo.getSysValue().toString();
            repaySwitchParams.setSysValue(value);
            if ("0".equals(value)) {
                successMsg = "支付宝还款关闭成功！";
            } else {
                successMsg = "支付宝还款开启成功！";
            }
            backConfigParams.add(repaySwitchParams);
        }

        // 利息前置/后置开关
        if (null != interest) {
            BackConfigParamsVo repaySwitchConfigParamsVo = JSONObject.parseObject(JSONObject.toJSONString(interest), BackConfigParamsVo.class);
            BackConfigParams repaySwitchParams = new BackConfigParams();
            repaySwitchParams.setId(repaySwitchConfigParamsVo.getId());
            String value = repaySwitchConfigParamsVo.getSysValue().toString();
            repaySwitchParams.setSysValue(value);
            if ("0".equals(value)) {
                successMsg = "利息前置设置成功！";
            } else {
                successMsg = "利息后置设置成功！";
            }
            backConfigParams.add(repaySwitchParams);
        }


        //老用户风控分数
       /* BackConfigParamsVo oldScoreParamsVo = JSONObject.parseObject(JSONObject.toJSONString(oldScore), BackConfigParamsVo.class);
        BackConfigParams oldScorebackConfigParams = new BackConfigParams();
        if (null!=oldScoreParamsVo){
            oldScorebackConfigParams.setId(oldScoreParamsVo.getId());
            oldScorebackConfigParams.setSysValue(oldScoreParamsVo.getSysValue().toString());
            backConfigParams.add(oldScorebackConfigParams);
        }*/

        int isSucc = backConfigParamsService.updateValue(backConfigParams);
        if (isSucc > 0) {
            if (null != nature || null != risk) {
                //风控相关缓存进Redis
                HashMap<String, Object> params = new HashMap<>();
                params.put("sysType", Constant.BACK_CONFIG_TYPE);
                List<BackConfigParams> BackConfigParams = backConfigParamsService.findParams(params);
                if (null != BackConfigParams && BackConfigParams.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                    Map<String, String> backConfigMap = new HashMap<>();
                    for (BackConfigParams backConfigParam : BackConfigParams) {
                        backConfigMap.put(backConfigParam.getSysKey(), backConfigParam.getSysValue());
                    }
                    //把类型为：RISK_PAIXU 数据存入Redis
                    redisUtil.set(Constant.SCORE_GATEWAY_REDIS_KEY, JSONObject.toJSONString(backConfigMap));
                }
            }
            return CallBackResult.returnJson(CallBackResult.SUCCESS, successMsg);
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UPDATE_FAILD);
    }


    /**
     * 新老用户自动审核开关
     *
     * @return
     */
    @Log(title = "新老用户自动审核开关")
    @RequestMapping("/update/risk/auto")
    public Object updateAuto(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        // 超管权限
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        // 超管密码
        String pass = backConfigParamsDao.findStrValue("password");
        // 密码错误次数验证
        String key = "updateRiskSwitch_" + platformUser.getPhoneNumber() + pass + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times > 5) {
            log.info("修改新老用户自动审核开关密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "密码错误次数超限");
        }
        Map<String, Object> param = JSONObject.parseObject(jsonData);
        // 验证超管密码
        String password = (String) param.get("password");
        if (org.apache.commons.lang3.StringUtils.isBlank(password) || !password.equals(pass)) {
            log.info("修改新老用户自动审核开关密码错误，phone={},times={}", platformUser.getPhoneNumber(), times);
            // 密码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "密码错误！");
        } else {
            redisUtil.del(key);
        }
        // 更新配置
        Integer type = (Integer) param.get("type");
        String value = (String) param.get("value");
        if (null == type || StringUtils.isBlank(value)) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }

        BackConfigParams backConfigParams = new BackConfigParams();
        // 新用户自动申请借款
        if (type == 0) {
            backConfigParams.setSysKey("auto_borrow");
            backConfigParams.setSysValue(value);
        } else if (type == 1) {
            // 老用户自动申请借款
            backConfigParams.setSysKey("old_auto");
            backConfigParams.setSysValue(value);
        }
        if (backConfigParamsDao.updateBySyskey(backConfigParams) > 0) {
            return CallBackResult.returnJson("操作成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }


    /**
     * 获取黑名单列表
     *
     * @return
     */
    @RequestMapping("/risk/blackList")
    public Object getRiskBlackList(HttpServletRequest request) throws Exception {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        HashMap<Object, Object> reuslMap = new HashMap<>();
        BackConfigParamsVo nbvo = backConfigParamsDao.findBySysKey("user_Black_List");          //内部黑名单
        BackConfigParamsVo csvo = backConfigParamsDao.findBySysKey("caishen_black_switch");     //中央黑名单
        BackConfigParamsVo nyvo = backConfigParamsDao.findBySysKey("ningyuan_black_switch");    //宁远黑名单
        BackConfigParamsVo mbvo = backConfigParamsDao.findBySysKey("menghua_black_switch");     //梦华黑名单
        reuslMap.put("nbSwitch", nbvo);
        reuslMap.put("csSwitch", csvo);
        reuslMap.put("nySwitch", nyvo);
        reuslMap.put("mbSwitch", mbvo);

        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, reuslMap);
    }

    /**
     * 风控类型
     *
     * @return
     */
    @Log(title = "黑名单开关修改")
    @RequestMapping("/risk/upBlackSwitch")
    public Object upBlackSwitch(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        Map<String, Object> param = JSONObject.parseObject(jsonData);
        Object status = param.get("status");
        if (null == status) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        int id = Integer.parseInt(param.get("id").toString());
        log.info("【黑名单类型修改】-----操作人={},ip={},黑名单类型={},状态={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), id, status);
        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setId(id);
        backConfigParams.setSysValue(status.toString());
        List<BackConfigParams> bcpList = new ArrayList<>();
        bcpList.add(backConfigParams);
        int isSucc = backConfigParamsDao.updateValue(bcpList);
        if (isSucc > 0) {
            return CallBackResult.returnJson("操作成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");
    }

    /**
     * 人脸识别修改
     *
     * @return
     */
    @Log(title = "人脸识别修改")
    @RequestMapping("/risk/upOrcSwitch")
    public Object upOrcSwitch(@RequestBody String jsonData, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "无权操作");
        }
        Map<String, Object> param = JSONObject.parseObject(jsonData);
        Object status = param.get("status");
        if (null == status) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        int id = Integer.parseInt(param.get("id").toString());
        log.info("【人脸识别修改】-----操作人={},ip={},黑名单类型={},状态={}", platformUser.getPhoneNumber(), IpAdrressUtil.getIpAdrress(request), id, status);
        BackConfigParams backConfigParams = new BackConfigParams();
        backConfigParams.setId(id);
        backConfigParams.setSysValue(status.toString());
        List<BackConfigParams> bcpList = new ArrayList<>();
        bcpList.add(backConfigParams);
        int isSucc = backConfigParamsDao.updateValue(bcpList);
        if (isSucc > 0) {
            return CallBackResult.returnJson("操作成功");
        }
        return CallBackResult.returnJson(CallBackResult.BUSINESS_DEFEAT, "操作失败");




    }

}
