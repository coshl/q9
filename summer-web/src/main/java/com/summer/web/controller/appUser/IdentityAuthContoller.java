package com.summer.web.controller.appUser;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.cloudauth20190307.models.DescribeFaceVerifyResponse;
import com.aliyun.oss.OSSClient;
import com.summer.api.service.IBackConfigParamsService;
import com.summer.api.service.IUserOcrService;
import com.summer.dao.entity.UserOcrData;
import com.summer.dao.entity.UserOcrDataWithBLOBs;
import com.summer.enums.OcrStatusEnum;
import com.summer.enums.UserAuthStatus;
import com.summer.enums.YesOrNo;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.service.face.AliFaceApi;
import com.summer.service.manager.UserOcrDataManager;
import com.summer.util.*;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.InfoIndexInfo;
import com.summer.dao.entity.LinkfaceCompareResults;
import com.summer.dao.entity.UserInfo;
import com.summer.pojo.dto.PersonInfoDto;
import com.summer.api.service.IInfoIndexInfoService;
import com.summer.api.service.IUserInfoService;
import com.summer.api.service.channel.IChannelAsyncCountService;
import com.summer.service.impl.thirdpart.zhimi.HttpClientUtil;
import com.summer.util.log.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * app 身份认证、个人信息认证、认证中心
 */
@Validated
@RestController
@RequestMapping("/v1.0/api/app")
@Slf4j
public class IdentityAuthContoller extends BaseController {

    @Value("${qiniu.pathPrefix}")
    private String pathPrefix;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IInfoIndexInfoService infoIndexInfoService;
    @Resource
    private IChannelAsyncCountService channelAsyncCountService;
    @Resource
    private IUserOcrService userOcrService;
    @Autowired
    private UserOcrDataManager userOcrDataManager;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private AliFaceApi aliFaceApi;

    /**
     * 获取身份认证信息接口
     * (没有用到)
     *
     * @param request
     * @return
     */
    @GetMapping("/idcard")
    public String getIdcardImgUrl(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            UserInfo newUser = userInfoService.selectByPrimaryKey(userInfo.getId());
            if (null != newUser) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("idcardImgZ", newUser.getIdcardImgZ());
                resultMap.put("idcardImgF", newUser.getIdcardImgF());
                resultMap.put("headPortrait", newUser.getHeadPortrait());
                resultMap.put("realName", newUser.getRealName());
                resultMap.put("idCard", newUser.getIdCard());
                resultMap.put("idCardAddress", newUser.getIdCardAddress());
                resultMap.put("idCardPeriod", newUser.getIdCardPeriod());
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 保存身份证图片地址（个人信息认证）
     *
     * @return
     */
    @PostMapping("/upload/idcard")
    public String uploadIdCardImg(HttpServletRequest request, @Validated @RequestBody PersonInfoDto personInfoDto) throws Exception {
        log.info("==============人脸检测==================");
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            userInfo = userInfoService.selectByPrimaryKey(userInfo.getId());
            if (null != userInfo) {
                Date nowTime = new Date();
                UserInfo newUserInfo = new UserInfo();
                newUserInfo.setId(userInfo.getId());
                //身份证正面地址
                newUserInfo.setIdcardImgZ(personInfoDto.getIdcardImgZ());
                //身份证反面图片地址
                newUserInfo.setIdcardImgF(pathPrefix + personInfoDto.getIdcardImgF());
                //人脸图片
                newUserInfo.setHeadPortrait(personInfoDto.getHeadPortrait());
                //真实姓名
                newUserInfo.setRealName(personInfoDto.getRealName());
                //身份证号
                newUserInfo.setIdCard(personInfoDto.getIdCard());
                //身份证地址
                newUserInfo.setIdCardAddress(personInfoDto.getIdCardAddress());
                //身份证有效期
                newUserInfo.setIdCardPeriod(personInfoDto.getIdCardPeriod());
                //OCR人脸识别订单号
                newUserInfo.setOcrOrder(personInfoDto.getOcrOrder());
                //实名认证时间
                newUserInfo.setRealAuthenticTime(nowTime);
                //年龄
                newUserInfo.setAge(AgeUtil.getAge(personInfoDto.getIdCard()));
//                //实名认证状态
//                newUserInfo.setRealAuthentic(Constant.REAL_AUTH_STATUS);
                byte realCount = userInfo.getRealCount();
                if (realCount == 0) {
                    //认证次数(设置成1，表示第一次认证，渠道统计的时候只在第一次统计：防止用户多次认证，渠道显示认证人数大于注册人数)
                    newUserInfo.setRealCount(Constant.AUTH_COUNT_TIMES);
                } else {
                    //原来的次数上认证次数+1
                    Integer reCount = (int) realCount + 1;
                    newUserInfo.setRealCount(reCount.byteValue());
                }
                //认证状态（0 未认证 1、身份认证 2、个人信息认证,3运营商认证 4 银行卡绑定 ）'
                userInfo.setAuthenticStatus(Constant.REAL_AUTH_STATUS);
                userInfoService.updateAuthenticStatus(userInfo);
                //newUserInfo.setAuthenticStatus(Constant.REAL_AUTH_STATUS);
                //修改用户身份证信息
                int isSucc = userInfoService.updateByPrimaryKeySelective(newUserInfo);
                log.info("人脸isSucc =={}",isSucc);
                if (isSucc > 0) {
                    //成功后，修改info_index_info 个人信息认证状态
                    InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
                    infoIndexInfo.setUserId(userInfo.getId());
                    infoIndexInfo.setAuthInfo(Constant.CHANGE_INFOINDEX_AUTH_STATE);
                    infoIndexInfo.setAuthInfoTime(nowTime);
                    int isSuccState = infoIndexInfoService.updateByPrimaryKeySelective(infoIndexInfo);
                    log.info("人脸isSuccState =={}",isSuccState);
                    if (isSuccState > 0) {
                        BackConfigParamsVo backConfig = backConfigParamsService.findBySysKey(Constant.OCR_SELFILE);
                        log.info("人脸开关 =={}",backConfig.getStrValue());
                        //TODO linkface 逻辑删除 8.22
                        /**异步统计认证个人信息认证量*/
                        channelAsyncCountService.personInfoAuthIsSuccCount(userInfo, nowTime);
                        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS);
                    }
                }
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTH_IS_BAD);
            }
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 获取认证中心数据
     *
     * @param request
     * @return
     */
    @GetMapping("/authcenter")
    public String getAuthCenter(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        if (null != userInfo) {
            InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
            if (null != infoIndexInfo) {
                Map<String, Object> resultMap = new HashMap<>();
                //个人信息认证
                if (infoIndexInfo.getAuthInfo() != YesOrNo.YES.getValue() && redisUtil.hasKey(Constant.USER_INDEX_AUTHINFO + userInfo.getId())) {
                    resultMap.put("authInfo", redisUtil.getObject(Constant.USER_INDEX_AUTHINFO + userInfo.getId()).toString());
                } else {
                    resultMap.put("authInfo", infoIndexInfo.getAuthInfo());
                }
                //紧急联系人认证
                resultMap.put("suthContacts", infoIndexInfo.getAuthContacts());
                //银行卡认证
                resultMap.put("authBank", infoIndexInfo.getAuthBank());
                //运营商认证
                if (infoIndexInfo.getAuthMobile() != YesOrNo.YES.getValue() && redisUtil.hasKey(Constant.USER_INDEX_AUTHMOBILE + userInfo.getId())) {
                    resultMap.put("authMobile", redisUtil.get(Constant.USER_INDEX_AUTHMOBILE + userInfo.getId()));
                } else {
                    resultMap.put("authMobile", infoIndexInfo.getAuthMobile());
                }
                //芝麻认证
                resultMap.put("authSesame", infoIndexInfo.getAuthSesame());
                return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, resultMap);
            }
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.AUTH_IS_BAD);
        }
        return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
    }

    /**
     * 获取人脸认证TOKEN
     */
    //TODO 00 人脸
    @GetMapping("/getOcrToken")
    public CallBackResult getOcrToken(HttpServletRequest request) {
        UserInfo userInfo = redisUser(request);
        String metaInfo = request.getParameter("metaInfo");
        if (userInfo == null) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Boolean lockOcr = redisUtil.setIfAbsent(Constant.OCR_PREFIX + userInfo.getId(), "0", 1, TimeUnit.SECONDS);
        if (!lockOcr) {
            return CallBackResult.fail("操作过于频繁,请稍后再试");
        }
        InfoIndexInfo infoIndexInfo = infoIndexInfoService.selectByPrimaryKey(userInfo.getId());
        if (Objects.equals(infoIndexInfo.getAuthInfo(), YesOrNo.YES.getValue())) {
            return CallBackResult.fail("已经实名认证过了");
        }
        long curTime = new Date().getTime();
        String token = aliFaceApi.initFaceVerify(userInfo, metaInfo);
        long curtime1 = new Date().getTime();
        log.info("OCR获取TOKEN结果：{},url:{},请求参数:{},请求时间:{}", token, "url", metaInfo,curtime1 - curTime);
        if (curtime1 - curTime > 2000)
            log.warn("OCR获取TOKEN请求时长超2秒，具体响应是时间:{}",curtime1 - curTime);
        if (StringUtils.isNotEmpty(token)) {
            // 获取token即初始化一次token记录
            UserOcrDataWithBLOBs userOcrDataWithBLOBs = new UserOcrDataWithBLOBs();
            userOcrDataWithBLOBs.setToken(token);
            userOcrDataWithBLOBs.setStatus(OcrStatusEnum.INIT_TOKEN.getValue());
            userOcrDataWithBLOBs.setUserId(userInfo.getId());
            userOcrDataWithBLOBs.setPhone(userInfo.getPhone());
            userOcrService.insertSelective(userOcrDataWithBLOBs);
            return CallBackResult.ok(token);
        }
        return CallBackResult.fail(Constant.RESULT_BAD_STATUS, "请求异常，请重新操作");
    }

    /**
     * TODO 00 保存人脸
     * 保存身份证图片地址（个人信息认证）
     *
     * @return
     */
    @PostMapping("/authIdcard")
    public CallBackResult authIdcard(@RequestHeader(value = "Authorization") String authorization, @RequestBody String authBody) throws Exception {
        log.info("authIdcard参数authBody:{}", authBody);
        JSONObject parameters = JSONObject.parseObject(authBody);
        String token = parameters.getString("token");
        if (StringUtils.isBlank(token)) {
            log.info("人脸token为空:{}", token);
            return CallBackResult.fail(CallBackResult.PARAM_IS_EXCEPTION_MSG);
        }

        UserInfo userInfo = redisUser(authorization);
        if (Objects.isNull(userInfo)) {
            return CallBackResult.fail("用户不存在");
        }
        UserOcrDataWithBLOBs userOcrDataWithBLOBs = userOcrService.selectByUserIdAndToken(userInfo.getId(), token);
        if (Objects.isNull(userOcrDataWithBLOBs)) {
            log.info("人脸请重新发起实名认证:{}", userOcrDataWithBLOBs);
            return CallBackResult.fail("请重新发起实名认证");
        }
        // 直接调接口获取报告，没获取到就改认证中待定时任务调取补偿
        redisUtil.set(Constant.USER_INDEX_AUTHINFO + userInfo.getId(), 3, 120);
        Boolean authResult = userOcrService.ocrAuth(userOcrDataWithBLOBs);

        if (authResult) {
            log.info("人脸认证结果已出:{}", authResult);
            redisUtil.del(Constant.USER_INDEX_AUTHINFO + userInfo.getId());
            UserInfo userInfoPhoto = userInfoService.findByPhone(userInfo.getPhone());
            String ocrPhoto =  userInfoPhoto.getHeadPortrait();
            String idCardImgF = userInfoPhoto.getIdcardImgF();
            String idCardImgZ = userInfoPhoto.getIdcardImgZ();
            Map data = new HashMap();
            data.put("ocrPhoto",ocrPhoto);
            data.put("idCardImgF",idCardImgF);
            data.put("idCardImgZ",idCardImgZ);
            return CallBackResult.ok(data);//"认证结果已出,请返回查看"
        }
        Date nowTime = new Date();
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setId(userInfo.getId());
        // 实名认证状态
        newUserInfo.setRealAuthentic(UserAuthStatus.RUN_AUTH.getValue().byteValue());
        //成功后，修改info_index_info 个人信息认证状态
        InfoIndexInfo infoIndexInfo = new InfoIndexInfo();
        infoIndexInfo.setUserId(userInfo.getId());
        infoIndexInfo.setAuthInfo(UserAuthStatus.RUN_AUTH.getValue());
        infoIndexInfo.setAuthInfoTime(nowTime);
        userOcrDataManager.authRunning(userOcrDataWithBLOBs.getId(), newUserInfo, infoIndexInfo);
        return CallBackResult.ok("认证中，请等待");
    }

}
