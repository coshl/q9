package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.UserInfo;
import com.summer.api.service.IPlateformChannelService;
import com.summer.api.service.IPlateformUserService;
import com.summer.api.service.IUserInfoService;
import com.summer.service.mq.OrderProducer;
import com.summer.service.sms.ISmsService;
import com.summer.util.*;
import com.summer.util.encrypt.AESDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1.0/api")
public class SendSMSController {

    @Autowired
    private ISmsService smsService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IPlateformUserService plateformUserService;
    @Resource
    private IPlateformChannelService plateformChannelService;
    @Value("${spring.profiles.active}")
    private String env;
    @Resource
    private OrderProducer orderProducer;

    /**
     * 获取手机验证码
     *
     * @param phoneNumber
     * @param type
     * @param channelCode 渠道落地页注册时使用
     * @return
     * @throws Exception
     */
    @GetMapping("/sms/{phoneNumber}/{type}")
    public Object sendPhoneCaptcha(
            @PathVariable(value = "phoneNumber")
            @NotBlank(message = "手机号" + Constant.PARAM_IS_NOT_BALANK)
            @Pattern(regexp = Constant.PHONE_REGULAR, message = Constant.PHONE_IS_ILLEGAL)
                    String phoneNumber,
            @PathVariable(value = "type")
            @NotNull(message = "短信发送类型" + Constant.PARAM_IS_NOT_BALANK)
                    Integer type, String channelCode, HttpServletRequest request, String timestamp, String rsa) throws Exception {
        log.info("【短信验证码】---------phone={},type={},channelCode={},ip={}", phoneNumber, type, channelCode, IpAdrressUtil.getIpAdrress(request));
        String captchaRedisKey;
        int mobile_code;
        String phoneCaptcha;
        switch (type) {
            //类型为1 （用在单独注册，一键登录注册时）
            case Constant.PHONE_CAPTCHA_REGISTER_TYPE:
                //查询用户信息(用户信息不存在，就不给发短信)
                UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
                if (null == userInfo) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, "该账号未注册，请注册后再登录");
                }
                //用户被删除，不允许登录
                byte statusUser = userInfo.getStatus();
                if (Constant.DELETE_TYPE == statusUser) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
                }

                //短信次数限制
                boolean isExpier = SmsKLimitUtil.limitTimes(redisUtil, phoneNumber, type, Constant.PHONE_CAPTCHA_EXPIRATION_TIME, Constant.LIMIT_TIMES);
                if (!isExpier) {
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTCHA_NO_FAILURE);
                }
                mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
                phoneCaptcha = String.valueOf(mobile_code);
                captchaRedisKey = phoneNumber + Constant.PHONE_CAPTCHA_SUFFIX;
                break;
            //修改登录密码时(暂时未使用)
            case Constant.PHONE_CAPTCHA_UPDATE_PASSWORD_TYPE:
                mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
                phoneCaptcha = String.valueOf(mobile_code);
                captchaRedisKey = phoneNumber + Constant.PHONE_CAPTCHA_UPDATE_PASSWORD_SUFFIX;
                break;
            //修改交易密码时（暂时未使用）
            case Constant.PHONE_CAPTCHA_UPDATE_PAY_PASSWORD_TYPE:
                mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
                phoneCaptcha = String.valueOf(mobile_code);
                captchaRedisKey = phoneNumber + Constant.PHONE_CAPTCHA_UPDATE_PAY_PASSWORD_SUFFIX;
                break;
            //落地页用户注册时发送的验证码
           /* case Constant.PHONE_CAPTCHA_LUODIYE_TYPE:

                //短信注册校验
                if(StringUtils.isBlank(timestamp)||StringUtils.isBlank(rsa)){
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.VERIFY_ERRER);
                }

                String md5 = DigestUtils.md5Hex(timestamp + phoneNumber + "szss");
                if(!rsa.equals(md5)){
                    log.info("sms sign error phone={},timestamp={},rsa={},md5={}",phoneNumber,timestamp,rsa,md5);
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.VERIFY_ERRER);
                }

                long l = System.currentTimeMillis();
                long diff = l - Long.parseLong(timestamp);
                if(diff >10*60000){
                    log.info("sms time over phone={},timestamp={},diff={}",phoneNumber,timestamp,diff);
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.VERIFY_ERRER);
                }
                log.info("sms md5 pass  phone={},timestamp={},rsa={},ip={}",phoneNumber,timestamp,rsa, IpAdrressUtil.getIpAdrress(request));

                if (null==channelCode){
                    //渠道编码错误
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.CHANNEL_CODE_ERRER);
                }
                PlateformChannel plateformChannel = plateformChannelService.findByChannelCode(channelCode);
                if (null == plateformChannel){
                    //渠道编码错误
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.CHANNEL_CODE_ERRER);
                }
                int forbiddenStatus = plateformChannel.getForbiddenStatus();
                //渠道被禁用
                if (forbiddenStatus == Constant.CHANNEL_FORBIDDEN){
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.CHANNEL_CODE_ERRER);
                }
                UserInfo byPhone = userInfoService.findByPhone(phoneNumber);
                if (null!=byPhone){
                    *//**如果是被删除的用户，就不能在发送短信注册了*//*
                    byte status = byPhone.getStatus();
                    if (status == Constant.DELETE_TYPE){
                       return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.FOR_REGISTER_INFO);
                    }
                    return CallBackResult.returnJson(Constant.H5_OlD_USER,Constant.H5_USER_REGISTER);
                }

                boolean hit = SmsKLimitUtil.limitTimesIP(redisUtil, request);
                if(hit){
                    return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.VERIFY_ERRER);
                }
                log.info("sms ip pass  phone={},timestamp={},rsa={},ip={}",phoneNumber,timestamp,rsa, IpAdrressUtil.getIpAdrress(request));
                    //短信次数限制
                    boolean isExpier2 = SmsKLimitUtil.limitTimes(redisUtil,phoneNumber, type,Constant.PHONE_CAPTCHA_EXPIRATION_TIME,Constant.LIMIT_TIMES);
                    if (!isExpier2){
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS,Constant.SMS_CAPTCHA_NO_FAILURE);
                    }
                    mobile_code =  (int)((Math.random()*9+1)*1000);
                    phoneCaptcha= String.valueOf(mobile_code);
                    captchaRedisKey = phoneNumber+Constant.SQUEEZE_PAGE_REGISTER_SUFFIX;

            break;*/
            //后台忘记密码发送的验证码
            case Constant.PHONE_CAPTCHA_UPDATE_PASSWORD:
                PlatformUser byPhoneNumber = plateformUserService.findByPhoneNumber(phoneNumber);
                if (null != byPhoneNumber) {
                    //短信次数限制
                    boolean isExpier3 = SmsKLimitUtil.limitTimes(redisUtil, phoneNumber, type, Constant.PHONE_CAPTCHA_EXPIRATION_TIME, Constant.LIMIT_TIMES);
                    if (!isExpier3) {
                        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTCHA_NO_FAILURE);
                    }
                    mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
                    phoneCaptcha = String.valueOf(mobile_code);
                    captchaRedisKey = phoneNumber + Constant.BACK_UPDATE_PASSSWORD_SUFFIX;
                } else {
                    return CallBackResult.returnJson(Constant.PLATFROM_NOT_EXIST, Constant.PLATFROM_NOT_EXIST_INFO);
                }
                break;


            default:
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTHCA_TYPE_IS_ERROR);

        }
        Boolean isSuccess = smsService.sendCode(phoneNumber, phoneCaptcha, true, type);
        boolean setSucc = redisUtil.set(captchaRedisKey, phoneCaptcha, Constant.PHONE_CAPTCHA_EXPIRATION);
        if (isSuccess) {
            if (setSucc) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
            }
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTHCA_SEND_BAD);
    }


    /**
     * APP发送短信验证码
     *
     * @return
     */
    @RequestMapping("/sms/mobile/{phoneNumber}")
    public Object sendAPPCode(@PathVariable(value = "phoneNumber")
                              @NotBlank(message = "手机号" + Constant.PARAM_IS_NOT_BALANK)
                              @Pattern(regexp = Constant.PHONE_REGULAR, message = Constant.PHONE_IS_ILLEGAL)
                                      String phoneNumber, HttpServletRequest request, String timestamp, String rsa) {
        log.info("【短信发送】APP------phoneNumber={},ip={}", phoneNumber, IpAdrressUtil.getIpAdrress(request));
        String captchaRedisKey;
        int mobile_code;
        String phoneCaptcha;
        //app端 ip限制
        // boolean hit = SmsKLimitUtil.limitTimesIPByCommon(redisUtil, request);
        //IP限制
        captchaRedisKey = phoneNumber + Constant.PHONE_CAPTCHA_SUFFIX;
        int hit = SmsKLimitUtil.limitTimesIP(redisUtil, request,phoneNumber);
        if (hit == 1 & redisUtil.hasKey(captchaRedisKey)) { //一分钟内其他的请求当作已经处理
            log.info("app登录、修改密码，一分钟内获取验证多次获取验证码，不执行获取验证码操作");
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
        }
        if (hit == 2) {//一个手机号
            log.info("app登录、修改密码，同手机同ip获取验证码超20次，限制ip");
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.IP_LIMIT);
        }
        log.info("sms ip pass  phone={},timestamp={},rsa={},ip={}", phoneNumber, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));
        //短信次数限制
        boolean isExpier = SmsKLimitUtil.limitTimes(redisUtil, phoneNumber, 1, Constant.PHONE_CAPTCHA_EXPIRATION_TIME, Constant.LIMIT_TIMES);
        if (!isExpier) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTCHA_NO_FAILURE);
        }
        //短信注册校验
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(rsa)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        String md5 = DigestUtils.md5Hex(timestamp + phoneNumber + "szss");
        if (!rsa.equals(md5)) {
            log.info("sms sign error phone={},timestamp={},rsa={},md5={}", phoneNumber, timestamp, rsa, md5);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        long l = System.currentTimeMillis();
        long diff = l - Long.parseLong(timestamp);
        if (diff > 10 * 60000) {
            log.info("sms time over phone={},timestamp={},diff={}", phoneNumber, timestamp, diff);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        log.info("sms md5 pass  phone={},timestamp={},rsa={},ip={}", phoneNumber, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));

        //查询用户信息(用户信息不存在，就不给发短信)
        UserInfo userInfo = userInfoService.findByPhone(phoneNumber);
        if (null == userInfo) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_EXISTS);
        }
        //用户被删除，不允许登录
        byte statusUser = userInfo.getStatus();
        if (Constant.DELETE_TYPE == statusUser) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.USER_NAME_NOT_EXISTS);
        }

        mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
        if (StringUtils.equalsIgnoreCase(env, "dev")) {
            //phoneCaptcha = "0000";
        	phoneCaptcha = String.valueOf(mobile_code);
        } else {
            phoneCaptcha = String.valueOf(mobile_code);
        }


        boolean setSucc = redisUtil.set(captchaRedisKey, phoneCaptcha, Constant.PHONE_CAPTCHA_EXPIRATION);
            if (setSucc) {
                JSONObject param = new JSONObject();
                param.put("phone",phoneNumber);
                param.put("code",phoneCaptcha);
                param.put("isCheckPhoneNum",true);
                param.put("type",4);
                orderProducer.createSendSmsCodeTask(param.toJSONString());
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
            }


        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTHCA_SEND_BAD);
    }

    /**
     * 落地页的
     *
     * @return
     */
    @RequestMapping("/sms/ldy/{phoneNumber}")
    public Object sendLdyCode(@PathVariable(value = "phoneNumber")
                              @NotBlank(message = "手机号" + Constant.PARAM_IS_NOT_BALANK)
                              @Pattern(regexp = Constant.PHONE_REGULAR, message = Constant.PHONE_IS_ILLEGAL)
                                      String phoneNumber, String channelCode, HttpServletRequest request, String timestamp, String rsa) {
        log.info("【短信发送】落地页------phoneNumber={},channelCode={},ip={}", phoneNumber, channelCode, IpAdrressUtil.getIpAdrress(request));
        String captchaRedisKey;
        int mobile_code;
        String phoneCaptcha;
        captchaRedisKey = phoneNumber + Constant.SQUEEZE_PAGE_REGISTER_SUFFIX;
        //IP限制
        int hit = SmsKLimitUtil.limitTimesIP(redisUtil, request,phoneNumber);
        if (hit == 1)  {
            log.info("H5落地页请求注册验证码，一分钟内获取验证多次获取验证码，不执行获取验证码操作");
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
        }
        if (hit == 2) {
            log.info("H5落地页请求注册验证码，同手机同ip获取验证码超20次，限制ip");
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.IP_LIMIT);
        }
        log.info("sms ip pass  phone={},timestamp={},rsa={},ip={}", phoneNumber, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));
        //短信次数限制
        boolean isExpier2 = SmsKLimitUtil.limitTimes(redisUtil, phoneNumber, 4, Constant.PHONE_CAPTCHA_EXPIRATION_TIME, Constant.LIMIT_TIMES);
        if (!isExpier2) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTCHA_NO_FAILURE);
        }
        //短信注册校验
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(rsa)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        String md5 = DigestUtils.md5Hex(timestamp + phoneNumber + "szss");
        if (!rsa.equals(md5)) {
            log.info("sms sign error phone={},timestamp={},rsa={},md5={}", phoneNumber, timestamp, rsa, md5);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        long l = System.currentTimeMillis();
        long diff = l - Long.parseLong(timestamp);
        if (diff > 10 * 60000) {
            log.info("sms time over phone={},timestamp={},diff={}", phoneNumber, timestamp, diff);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        log.info("sms md5 pass  phone={},timestamp={},rsa={},ip={}", phoneNumber, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));

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
        UserInfo byPhone = userInfoService.findByPhone(phoneNumber);
        if (null != byPhone) {
            /**如果是被删除的用户，就不能在发送短信注册了*/
            byte status = byPhone.getStatus();
            if (status == Constant.DELETE_TYPE) {
                return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.FOR_REGISTER_INFO);
            }
            return CallBackResult.returnJson(Constant.H5_OlD_USER, Constant.H5_USER_REGISTER);
        }
        mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
        phoneCaptcha = String.valueOf(mobile_code);
        //Boolean isSuccess = smsService.sendCode(phoneNumber, phoneCaptcha, true, 4);
        boolean setSucc = redisUtil.set(captchaRedisKey, phoneCaptcha, Constant.PHONE_CAPTCHA_EXPIRATION);
        if (setSucc) {
            JSONObject param = new JSONObject();
            param.put("phone",phoneNumber);
            param.put("code",phoneCaptcha);
            param.put("isCheckPhoneNum",true);
            param.put("type",4);
            orderProducer.createSendSmsCodeTask(param.toJSONString());
            return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
        }

        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTHCA_SEND_BAD);
    }

    /**
     * 后台修改密码的
     *
     * @return
     */
    @RequestMapping("/sms/pwd/{phoneNumber}")
    public Object sendBackPwdCode(@PathVariable(value = "phoneNumber")
                                  @NotBlank(message = "手机号" + Constant.PARAM_IS_NOT_BALANK)
                                  @Pattern(regexp = Constant.PHONE_REGULAR, message = Constant.PHONE_IS_ILLEGAL)
                                          String phoneNumber, HttpServletRequest request, String timestamp, String rsa) {
        log.info("【短信发送】后台修改密码------phoneNumber={},ip={}", phoneNumber, IpAdrressUtil.getIpAdrress(request));
        String captchaRedisKey;
        int mobile_code;
        String phoneCaptcha;
        //后台修改密码 ip限制
        boolean hit = SmsKLimitUtil.limitTimesIPByCommon(redisUtil, request);
        if (hit) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.IP_LIMIT_BACK);
        }

        //短信次数限制
        boolean isExpier3 = SmsKLimitUtil.limitTimes(redisUtil, phoneNumber, 5, Constant.PHONE_CAPTCHA_EXPIRATION_TIME, Constant.LIMIT_TIMES);
        if (!isExpier3) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTCHA_NO_FAILURE);
        }

        //短信注册校验
        if (StringUtils.isBlank(timestamp) || StringUtils.isBlank(rsa)) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        String md5 = DigestUtils.md5Hex(timestamp + phoneNumber + "szss");
        if (!rsa.equals(md5)) {
            log.info("sms sign error phone={},timestamp={},rsa={},md5={}", phoneNumber, timestamp, rsa, md5);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }

        long l = System.currentTimeMillis();
        long diff = l - Long.parseLong(timestamp);
        if (diff > 20 * 60000) {
            log.info("sms time over phone={},timestamp={},diff={}", phoneNumber, timestamp, diff);
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.VERIFY_ERRER);
        }
        log.info("sms md5 pass  phone={},timestamp={},rsa={},ip={}", phoneNumber, timestamp, rsa, IpAdrressUtil.getIpAdrress(request));


        PlatformUser byPhoneNumber = plateformUserService.findByPhoneNumber(phoneNumber);
        if (null != byPhoneNumber) {

            //mobile_code = (int) ((Math.random() * 9 + 1) * 10);
            //phoneCaptcha = String.valueOf(mobile_code);
            phoneCaptcha = GenerateNo.getCharAndNumr(4);
            captchaRedisKey = phoneNumber + Constant.BACK_UPDATE_PASSSWORD_SUFFIX;
        } else {
            return CallBackResult.returnJson(Constant.PLATFROM_NOT_EXIST, Constant.PLATFROM_NOT_EXIST_INFO);
        }

        Boolean isSuccess = smsService.sendCode(phoneNumber, phoneCaptcha, true, 5);
        boolean setSucc = redisUtil.set(captchaRedisKey, phoneCaptcha, Constant.PHONE_CAPTCHA_EXPIRATION);
        if (isSuccess) {
            if (setSucc) {
                return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.SMS_CAPTHCA_SEND_SUCCESS);
            }
        }
        return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.SMS_CAPTHCA_SEND_BAD);
    }


}
