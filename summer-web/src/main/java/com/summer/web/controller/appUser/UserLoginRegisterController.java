package com.summer.web.controller.appUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.api.service.*;
import com.summer.dao.entity.*;
import com.summer.dao.mapper.PlateformChannelReportDAO;
import com.summer.web.controller.BaseController;
import com.summer.group.SavePassword;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.pojo.dto.AppUpdatePasswordDTO;
import com.summer.pojo.dto.AppUserLoginDto;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.util.log.StringUtils;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户登录注册Controller
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/v1.0/api/app")
public class UserLoginRegisterController extends BaseController {

    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private SourceUserLoginInfoService sourceUserLoginInfoService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private ILoanRuleConfigService loanRuleConfigService;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private IPlateformChannelService plateformChannelService;
    @Resource
    private IPlateformUserService plateformUserService;
    @Resource
    protected IBackConfigParamsDao backConfigParamsDao;
    @Resource
    private PlateformChannelReportDAO plateformChannelReportDAO;

    /**
     * APP登录注册合并
     *
     * @return
     */
    @PostMapping(value = "/login")
    public Object loginAndRegister(HttpServletRequest request, @Validated @RequestBody AppUserLoginDto appUserLoginDto) throws Exception {
        String phoneNumber = appUserLoginDto.getPhoneNumber();
        Date nowTime = new Date();
        // 验证码不能为空
        if (StringUtils.isBlank(appUserLoginDto.getSmsCode())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPATCH_NOT_NULL);
        }

        // app登陆密码错误次数验证
        String key = "app_login_" + phoneNumber + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        Object times = redisUtil.getObject(key);
        if (times != null && (Integer) times >= 5) {
            log.info("app登陆，phoneNumber={},ip={},smsCode={}", phoneNumber, IPUtils.getIpAddr(request), appUserLoginDto.getSmsCode());
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "登录验证码错误次数超限");
        }
        //校验验证码失效状态
        String capatch = phoneNumber + Constant.PHONE_CAPTCHA_SUFFIX;
        if (!redisUtil.hasKey(capatch)) {
            // 验证码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
        }
        //校验验证码
        if (!appUserLoginDto.getSmsCode().equals(redisUtil.get(capatch))) {
            // 验证码失败次数记录
            LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
        }
        // 登录验证码正确，清除次数
        redisUtil.del(key);
        //查询用户信息
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        //用户信息存在，说明该用户已注册过，直接走登录
        if (null != userInfo) {
            Map<String, Object> param = new HashMap<>();
            param.put("nowTime", nowTime);
            param.put("channelId", userInfo.getChannelId());
            PlateformChannelReport plateformChannelReport = plateformChannelReportDAO.selectByParams(param);
            //存在渠道统计数据
            if (null != plateformChannelReport) {
                PlateformChannelReport newPlateformChannelReport = new PlateformChannelReport();
                newPlateformChannelReport.setId(plateformChannelReport.getId());
                //短信+1
                newPlateformChannelReport.setChannelDuanXin(plateformChannelReport.getChannelDuanXin() + 1);
                //更新时间
                newPlateformChannelReport.setUpdateTime(nowTime);
                plateformChannelReportDAO.updateByPrimaryKeySelective(newPlateformChannelReport);
                log.info("ChannelSyncCountService.plateformChannelReportCount渠道短信统计----->更新成功 channelId={},nowTime={}", userInfo.getChannelId(), DateUtil.formatTimeYmdHms(nowTime));
            }
            //用户被删除，不允许登录
            byte status = userInfo.getStatus();
            if (Constant.DELETE_TYPE == status) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
            }
            InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
            //如果infoIndexInfo不存在(这里是防止落地页登录没有生成该表数据，在登录时生成)
            if (null == infoIndexInfo) {
                //插入一条
                infoIndexInfo = new InfoIndexInfo();
                infoIndexInfo.setUserId(userInfo.getId());
                infoIndexInfoService.insertSelective(infoIndexInfo);
            }
            int clientType = userInfo.getClientType();
            if (0 == clientType) {
                //修改之前的客户类型(把0未知的修改了，1表示安卓，2表示IOS)
                String clientTypeStr = appUserLoginDto.getClientType();
                int cType = 0;
                if (null != clientTypeStr) {
                    cType = Integer.parseInt(clientTypeStr);
                }
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setId(userInfo.getId());
                newUserInfo.setClientType(cType);
                userInfoService.updateByPrimaryKeySelective(newUserInfo);
            }

            //获取登录成功要返回的数据
            String loginResultData = loginResultData(userInfo, request, appUserLoginDto);
            //登录成功
            if (null != loginResultData) {
                //修改用户设备型号、系统版本
                UserInfo info = new UserInfo();
                String deviceModel = appUserLoginDto.getDeviceModel();
                //log.info("用户登录设备型号{}==========",deviceModel);
                String systemVersion = appUserLoginDto.getSystemVersion();
                //log.info("用户登系统版本{}==========",systemVersion);
                String appVersion = appUserLoginDto.getAppVersion();
                //log.info("用户App版本号{}==========",appVersion);
                info.setId(userInfo.getId());
                info.setDeviceModel(deviceModel == null ? "" : deviceModel);
                info.setSystemVersion(systemVersion == null ? "" : systemVersion);
                info.setAppVersion(appVersion == null ? "" : appVersion);
                userInfoService.updateUserDeviceModelAndSystemVersion(info);    //修改用户设备型号、系统版本
                //修改客户类型

                /**异步统计登录量*/
                channelAsyncCountService.loginIsSuccCount(userInfo, new Date());
                //成功过后，清除验证码
                cleanCapatch(capatch);
                return loginResultData;
            } else {//登录失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.LOGIN_IS_BAD);
            }
        } else {
            if (!backConfigParamsDao.findStrValue("appId").equals("101"))//非聚财宝
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.ERROR_USER_LOGIN);
            //根据渠道编码查询该渠道是否允许注册
            String channelCode = redisUtil.get("jieyou_number_" + phoneNumber);

            if (org.springframework.util.StringUtils.isEmpty(channelCode))
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.ERROR_USER_LOGIN);

            log.info("APP端注册流程---------channelCode={},phone={},clientType={}",
                    channelCode, phoneNumber, appUserLoginDto.getClientType());
            if (null == channelCode) {
                //渠道编码错误
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
            }
            PlateformChannel plateformChannel = plateformChannelService.findByChannelCode(channelCode);
            if (null == plateformChannel) {
                //渠道编码错误
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
            }
            int forbiddenStatus = plateformChannel.getForbiddenStatus();
            //渠道被禁用
            if (forbiddenStatus == Constant.CHANNEL_FORBIDDEN) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CHANNEL_CODE_ERRER);
            }

            UserInfo newUserInfo = new UserInfo();
            //设置手机号码
            newUserInfo.setPhone(phoneNumber);
            //注册ip
            newUserInfo.setCreateIp(IPUtils.getIpAddr(request));
            //注册时间
            newUserInfo.setCreateTime(nowTime);
            //注册的设备号
            newUserInfo.setEquipmentNumber(appUserLoginDto.getDeviceId());
            //设置客户端类型
            newUserInfo.setClientType(Integer.parseInt(appUserLoginDto.getClientType()));
            //渠道id
            newUserInfo.setChannelId(plateformChannel.getId());
            int saveState = userInfoService.insertSelective(newUserInfo);
            log.info("注册新用户信息{}==========", newUserInfo);
            if (saveState > 0) {
                //查询刚注册成的用户信息
                UserInfo newestUserInfo = userInfoService.findByPhone(phoneNumber);
                //如果查出用户为空
                if (null == newestUserInfo) {
                    //注册失败
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.REGISTER_IS_BAD);
                }
                //插入用户不为空，插入表info_index_info数据
                InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
                infoIndexInfo.setUserId(newestUserInfo.getId());
                infoIndexInfo.setAuthCount(Constant.AUTH_COUNT);
                infoIndexInfoService.insertSelective(infoIndexInfo);

                //注册完成过后，直接继续走登录成功
                //获取登录成功要返回的数据
                String loginResultData = loginResultData(newestUserInfo, request, appUserLoginDto);
                //登录成功
                if (null != loginResultData) {
                    //**异步统计登录量*//*
                    //channelAsyncCountService.loginIsSuccCount(newestUserInfo,new Date());
                    //**异步统计注册量*//*
                    channelAsyncCountService.registerIsSuccCount(newestUserInfo.getId(),
                            newestUserInfo.getChannelId(), newUserInfo.getCreateTime());

                    //成功过后，清除验证码
                    cleanCapatch(capatch);
                    return loginResultData;
                } else {//登录失败
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.LOGIN_IS_BAD);
                }
            }
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_EXISTS);
        }


      /*  //用户信息不存在，走注册同时进行用户登录
        UserInfo newUserInfo = new UserInfo();
        //设置手机号码
        newUserInfo.setPhone(appUserLoginDto.getPhoneNumber());
        //注册ip
        newUserInfo.setCreateIp(IPUtils.getIpAddr(request));
        //注册时间
        newUserInfo.setCreateTime(nowTime);
        //注册的设备号
        newUserInfo.setEquipmentNumber(appUserLoginDto.getDeviceId());
        //设置客户端类型
        newUserInfo.setClientType(Integer.parseInt(appUserLoginDto.getClientType()));
        PlateformChannel plateformChannel = plateformChannelService.selectByChannelName(appUserLoginDto.getChannelCode());
        if (null!=plateformChannel){
            //注册渠道id
            newUserInfo.setChannelId(plateformChannel.getId());
        }else {
            //未查询到，说明该渠道被删除了或者不存在，需要插入一条自然渠道
            PlateformChannel plateformChannelApp = new PlateformChannel();
            //渠道名称
            plateformChannelApp.setChannelName(appUserLoginDto.getChannelCode());
            //默认上线状态
            plateformChannelApp.setStatus(Constant.CHANNEL_ONLINE);
            //默认扣量比例
            plateformChannelApp.setDecreasePercentage(Constant.DECREASE_ERCENTAGE);
            //设置自然流量默认账号，未第一个管理员
            PlatformUser firstAdmin = plateformUserService.findFirstAdmin();
            if (null!=firstAdmin){
                //设置管理员id
                plateformChannelApp.setPlateformUserId(firstAdmin.getId());
                //设置管理员账户
                plateformChannelApp.setAccount(firstAdmin.getPhoneNumber());
                //设置后台链接
                plateformChannelApp.setBackStage(channelLinkConfig.getBackStage());
                //渠道商经理手机号
                plateformChannelApp.setManagerPhone(firstAdmin.getPhoneNumber());
                //经理名字
                plateformChannelApp.setManagerName(firstAdmin.getUserName());
            }
            //默认合作模式
            plateformChannelApp.setCooperationMode(Constant.COOPERATIONMOD);
            //结算方式
            plateformChannelApp.setPaymentMode(Constant.PAYMENTMODE);
            //结算类型
            plateformChannelApp.setPaymentType(Constant.PAYMENTTYPE);
            //默认价格
            Double price =(Constant.CHANNEL_PRICE * Constant.CENT_CHANGE_DOLLAR);
            plateformChannelApp.setPrice(price.intValue());
            plateformChannelService.insertSelective(plateformChannelApp);
            //渠道编码
            String channelCode =  GenerateNo.generateChannelCode(5,plateformChannelApp.getId());
            plateformChannelApp.setChannelCode(channelCode);
            plateformChannelService.updateByPrimaryKeySelective(plateformChannelApp);
            //插入新增的自然却道id ，记得配置Mapper文件 返回主键配置（useGeneratedKeys="true" keyProperty="id"）
            newUserInfo.setChannelId(plateformChannelApp.getId());
        }
        int saveState = userInfoService.insertSelective(newUserInfo);
        if (saveState > 0){
            //查询刚注册成的用户信息
            UserInfo newestUserInfo = userInfoService.findByPhone(appUserLoginDto.getPhoneNumber());
            //如果查出用户为空
            if (null == newestUserInfo){
                //登录失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.LOGIN_IS_BAD);
            }
            //**异步统计注册量*//*
            channelAsyncCountService.registerIsSuccCount(newestUserInfo.getId(),newestUserInfo.getChannelId(),newUserInfo.getCreateTime());
            //插入用户不为空，插入表info_index_info数据
            InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
            infoIndexInfo.setUserId(newestUserInfo.getId());
            infoIndexInfo.setAuthCount(Constant.AUTH_COUNT);
            infoIndexInfoService.insertSelective(infoIndexInfo);
            //获取登录成功要返回的数据
            String loginResultData = loginResultData(newestUserInfo, request, appUserLoginDto);
            //登录成功
            if (null != loginResultData){
                return loginResultData;
            }else {//登录失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.LOGIN_IS_BAD);
            }
        }else {//登录失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.LOGIN_IS_BAD);
        }*/

    }

    /**
     * APP密码登录
     *
     * @return
     */
    @PostMapping(value = "/loginNew")
    public Object loginAndRegisterNew(HttpServletRequest request, @Validated @RequestBody AppUserLoginDto appUserLoginDto) throws Exception {
        String phoneNumber = appUserLoginDto.getPhoneNumber();
//        Date nowTime = new Date();
        // 使用密码登录时，密码不能为空
        if (StringUtils.isBlank(appUserLoginDto.getPassword())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PASSWORD_NOT_NULL);
        }
        //查询用户信息
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        //用户信息存在，说明该用户已注册过，直接走登录
        if (null != userInfo) {
            // app登陆密码错误次数验证
            String password = userInfo.getPassword();
            String key = "app_loginNew_" + password + phoneNumber + DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
            Object times = redisUtil.getObject(key);
            if (times != null && (Integer) times >= 5) {
                log.info("app登陆验证码错误，phoneNumber={},ip={},smsCode={}", phoneNumber, IPUtils.getIpAddr(request), appUserLoginDto.getSmsCode());
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "登录密码错误次数超限");
            }
            // 校验密码
            String passeord = MD5Util.md5(appUserLoginDto.getPassword() + userInfo.getLoginPwdSalt());
            if (StringUtils.isBlank(passeord) || !passeord.equals(password)) {
                // 密码失败次数记录
                LimitTimesUtil.incr(60 * 60 * 24, redisUtil, key);
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PASSWORD_IS_ERROR);
            } else {
                // 登录密码正确，清除次数
                redisUtil.del(key);
            }
            //用户被删除，不允许登录
            byte status = userInfo.getStatus();
            if (Constant.DELETE_TYPE == status) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
            }
            InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
            //如果infoIndexInfo不存在(这里是防止落地页登录没有生成该表数据，在登录时生成)
            if (null == infoIndexInfo) {
                //插入一条
                infoIndexInfo = new InfoIndexInfo();
                infoIndexInfo.setUserId(userInfo.getId());
                infoIndexInfoService.insertSelective(infoIndexInfo);
            }
            int clientType = userInfo.getClientType();
            if (0 == clientType) {
                //修改之前的客户类型(把0未知的修改了，1表示安卓，2表示IOS)
                String clientTypeStr = appUserLoginDto.getClientType();
                int cType = 0;
                if (null != clientTypeStr) {
                    cType = Integer.parseInt(clientTypeStr);
                }
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setId(userInfo.getId());
                newUserInfo.setClientType(cType);
                userInfoService.updateByPrimaryKeySelective(newUserInfo);
            }

            //获取登录成功要返回的数据
            String loginResultData = loginResultData(userInfo, request, appUserLoginDto);
            //登录成功
            if (null != loginResultData) {
                //修改客户类型

                /**异步统计登录量*/
                channelAsyncCountService.loginIsSuccCount(userInfo, new Date());
                return loginResultData;
            } else {//登录失败
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.LOGIN_IS_BAD);
            }
        } else {
            // 现在不能用APP直接注册，用户不存在直接返回失败
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_EXISTS);
        }
    }


    /**
     * 登录要返回的数据
     *
     * @param userInfo
     * @param request
     * @param userLoginParamDto
     * @return
     * @throws Exception
     */
    public String loginResultData(UserInfo userInfo, HttpServletRequest request, AppUserLoginDto userLoginParamDto) throws Exception {
        //生成用户登录  唯一标识token
        String token = JwtUtil.generateToken(userInfo.getPhone(), userInfo.getId().longValue());
        //用户对象转换JSON字符串
        String userInfoJSONStr = JSON.toJSONString(userInfo);
        //token过期时间一周
        boolean isSucc = redisUtil.set(Constant.APP_TOKEN_PREFIX + token, userInfoJSONStr, Constant.TOKEN_EXPIRATION_TIME);
        //用户信息存入Redis成功，说明登录成功，才能把token等信息返回去
        if (isSucc) {
            //保存登录日志
            savaLoginLog(request, userInfo, userLoginParamDto);
            //登录成功后要返回的数据
            Map<String, Object> resultMap = new HashMap<String, Object>();
            //手机号码
            resultMap.put("phoneNumber", userInfo.getPhone());
            //'性别 0未知 1男 2女 3其他
            resultMap.put("sex", userInfo.getSex());
            //真实姓名
            if (StringUtils.isBlank(userInfo.getRealName()) || null == userInfo.getRealName()) {
                resultMap.put("realName", "null");
            } else {
                resultMap.put("realName", userInfo.getRealName());
            }
            //token
            resultMap.put("token", token);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.LOGIN_IS_SUCCESS, resultMap);
        }
        return null;
    }

    /**
     * 保存登录日志
     *
     * @param request
     * @param userLoginParamDto
     * @throws Exception
     */
    public void savaLoginLog(HttpServletRequest request, UserInfo userInfo, AppUserLoginDto userLoginParamDto) throws Exception {
        List<SourceUserLoginInfo> loginInfos = sourceUserLoginInfoService.findByPhone(userLoginParamDto.getPhoneNumber());
        SourceUserLoginInfo userLoginInfo = new SourceUserLoginInfo();
        if (null != loginInfos && loginInfos.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            //设置手机号
            userLoginInfo.setPhoneNumber(userInfo.getPhone());
            //设置ip
            userLoginInfo.setIpAddress(IPUtils.getIpAddr(request));
            //登录次数
            userLoginInfo.setLoginTimes(loginInfos.get(0).getLoginTimes() + 1);
            //客户端类型
            userLoginInfo.setClientType(userInfo.getClientType());
            //设置渠道id
            userLoginInfo.setSourceChannelCode(userInfo.getChannelId());
            //用户注册时间
            userLoginInfo.setUserCreatTime(userInfo.getCreateTime());
            //保存用户登录信息JSON数据
            userLoginInfo.setUserLoginInfo(JSONObject.toJSONString(userLoginParamDto));
            //更新source_user_login_info表
            sourceUserLoginInfoService.updateByPhoneNumber(userLoginInfo);
        } else {
            //手机号码
            userLoginInfo.setPhoneNumber(userInfo.getPhone());
            //设置ip
            userLoginInfo.setIpAddress(IPUtils.getIpAddr(request));
            //设置渠道id
            userLoginInfo.setSourceChannelCode(userInfo.getChannelId());
            //登录次数
            userLoginInfo.setLoginTimes(1);
            //注册时间
            userLoginInfo.setUserCreatTime(userInfo.getCreateTime());
            //保存用户登录信息JSON数据
            userLoginInfo.setUserLoginInfo(JSONObject.toJSONString(userLoginParamDto));
            //客户端类型
            userLoginInfo.setClientType(userInfo.getClientType());
            //插入source_user_login_info表
            sourceUserLoginInfoService.save(userLoginInfo);
        }
    }

    /**
     * 注销功能
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public String loginOut(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        //存在这个token
        if (redisUtil.hasKey(Constant.APP_TOKEN_PREFIX + token)) {
            //删除token
            redisUtil.del(Constant.APP_TOKEN_PREFIX + token);
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.LOGIN_OUT_IS_SUCCESS);
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.LOGIN_OUT_IS_SUCCESS);
    }

    /**
     * app用户忘记密码时修改密码
     *
     * @param updatePasswordDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/updatePassword")
    public String updatePassword(@Validated({SavePassword.class}) @RequestBody AppUpdatePasswordDTO updatePasswordDTO, HttpServletRequest request) throws Exception {

        BackConfigParamsVo updatePassword = backConfigParamsDao.findBySysKey(Constant.APP_UPDATE_PASSWORD);
        if (null == updatePassword) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "暂停密码重置功能！");
        }
        /**密码修改博功能，1表示开启，2关闭*/
        Integer sysValue = updatePassword.getSysValue();
        if (sysValue == 2) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "暂停密码重置功能！");
        }
        //  String phoneNumber = ;
        String phoneNumber = updatePasswordDTO.getPhoneNumber();
        String password = updatePasswordDTO.getPassword();
        String smsCode = updatePasswordDTO.getSmsCode();
        log.info("app用户忘记密码时修改密码-------phoneNumber={},password={},smsCode={},ip={}", phoneNumber, password, smsCode, IPUtils.getIpAddr(request));
        // 手机号正则校验
        if (!ValidateUtil.isMobileNumber(updatePasswordDTO.getPhoneNumber())) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "手机号错误！");
        }

        // 同一手机号每天只能修改5次
        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        if (LimitTimesUtil.limitTimes(60 * 60 * 24, redisUtil, "APP_updatePassword_" + phoneNumber + dateFormat, 3)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "修改密码次数超限");
        }

        // 修改密码类型
        Integer passwordType = updatePasswordDTO.getPasswordType();
        if (passwordType == null || (passwordType != 0 && passwordType != 1)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PARAM_ERROR);
        }
        //验证码的KEY
        String capatch = phoneNumber + Constant.PHONE_CAPTCHA_SUFFIX;
        // 忘记密码，修改密码，需要填写验证码
        if (passwordType.equals(0)) {

            // 验证码是否为空
            if (StringUtils.isBlank(smsCode)) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPATCH_NOT_NULL);
            }

            // 校验验证码失效状态
            if (!redisUtil.hasKey(capatch)) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_NOT_EXIST);
            }
            // 校验验证码
            if (!smsCode.equals(redisUtil.get(capatch))) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.CAPTCHA_IS_ERROR);
            }
        } else {//否则就需要校验token
            UserInfo userInfo = redisUser(request);
            if (null == userInfo) {
                return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
            }

        }
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        if (null != userInfo) {
            UserInfo newUserInfo = new UserInfo();
            newUserInfo.setId(userInfo.getId());
            // 随机盐
            String randomSalt = RandomUtil.getRandomSalt(6);
            // 密码：MD5（密码+盐）

            if (!ValidateUtil.veryPwd(password)) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PARAM_ERROR);
            }
            newUserInfo.setPassword(MD5Util.md5(password + randomSalt));
            // 设置盐
            newUserInfo.setLoginPwdSalt(randomSalt);
            int status = userInfoService.updateByPrimaryKeySelective(newUserInfo);
            if (status > 0) {
                // 密码修改成功
                //成功过后，清除验证码
                cleanCapatch(capatch);
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.PASSWORD_UPDATE_SUCC);
            }
        }
        // 密码修改失败
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.PASSWORD_UPDATE_FAILD);
    }

    /**
     * 成功后清除验证码
     *
     * @param
     * @param
     */
    @Async
    public void cleanCapatch(String capatchKey) {
        //用户登陆成功，验证码清除
        if (redisUtil.hasKey(capatchKey)) {
            redisUtil.del(capatchKey);
        }
    }
}
