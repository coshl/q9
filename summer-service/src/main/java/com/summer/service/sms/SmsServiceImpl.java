package com.summer.service.sms;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.AppBase;
import com.summer.dao.entity.PhoneCodeSend;
import com.summer.dao.mapper.PhoneCodeSendDAO;
import com.summer.enums.SmsContentKeyEnum;
import com.summer.enums.SmsSwitch;
import com.summer.dao.mapper.AppBaseDAO;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.queue.QueueConstans;
import com.summer.service.sms.mch.*;
import com.summer.util.Constant;
import com.summer.util.RedisUtil;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 短信业务处理层
 */
@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private AppBaseDAO appBaseDAO;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBackConfigParamsDao backConfigParamsDao;
    @Autowired
    private SmsJuGuangMsg smsJuGuangMsg;  //聚光短信，只用于发送短信验证码
    @Autowired
    private PhoneCodeSendDAO PhoneCodeSendDAO;

    /**
     * 短信批量发送,业务类短信
     *
     * @param phones  手机号码
     * @param content
     */
    public Boolean batchSend(String phones, String content) {
        AppBase appBase = appBaseDAO.selectByPrimaryKey(1);
        if (content.contains("#appName#")) {
            content = content.replace("#appName#", appBase.getName());
        }
        Map<String, String> redisMap = getRedisMap();
        // 短信通道配置
        String smsnotice = redisMap.get("smsnotice.switch");
        String title = redisMap.get(SmsContentKeyEnum.SMS_SIGN.getValue());
        //聚光发送短信
        if(Objects.equals(SmsSwitch.JUGUANG.getValue().toString(), smsnotice)) {
            log.info("聚光短信发送:{},{}{}", phones, title, content);
            //return smsJuGuangMsg.sendSms(phones, title, content);
            return false;
        }
        return false;
    }

    /**
     * 催收短信
     *
     * @param phones
     * @param contentInfo
     * @return
     */
    public Boolean batchSendRemind(String phones, String contentInfo) {
        AppBase appBase = appBaseDAO.selectByPrimaryKey(1);
        String content = contentInfo;
        if (contentInfo.contains("#appName#")) {
            content = content.replace("#appName#", appBase.getName());
        }
        Map<String, String> redisMap = getRedisMap();
        // 短信通道配置
        String smsnotice = redisMap.get("smsnotice.switch");
        String title = redisMap.get(SmsContentKeyEnum.SMS_SIGN.getValue());
        //smgw发送短信
        if(Objects.equals(SmsSwitch.SMGW.getValue().toString(), smsnotice)) {
        	log.info("smgw短信发送:{},{}{}", phones, title, content);
            //return smsSMGWSendMsg.sendSms(phones, title, content);
            return false;
        }
        return false;
    }

    /**
     * 短信验证码
     *
     * @param phones          手机号
     * @param code
     * @param isCheckPhoneNum 是否开启发送次数限制
     * @param type
     */
    public Boolean sendCode(String phones, String code, boolean isCheckPhoneNum, Integer type) {
        log.info("验证码:{}", code);
        //登录注册发送验证码次数限制
        if (isCheckPhoneNum) {
            if (StringUtils.isBlank(phones) || phones.length() < 11) {
                return false;
            }
            //同一手机一分钟只能请求一次
            if (getSendPhoneInRedis(phones + "_" + type)) {
                return true;
            }
        }
        setSendPhone(phones + "_" + type);
        //短信验证码
        Map<String, String> redisMap = getRedisMap();
        String content = redisMap.get(SmsContentKeyEnum.SMS_SENDCONTENT.getValue());
        if (content.contains("#code#")) {
            content = content.replace("#code#", code);
        }
        String title = redisMap.get(SmsContentKeyEnum.SMS_SIGN.getValue());
        // 短信通道配置
        String smsSwitch = redisMap.get("sms.switch");
        //短信入库
        PhoneCodeSend phoneCodeSend = new PhoneCodeSend();
        phoneCodeSend.setCode(code);
        phoneCodeSend.setPhone(phones);
        phoneCodeSend.setContent(content);
        PhoneCodeSendDAO.insert(phoneCodeSend);
        //聚光发送短信
        if(Objects.equals(SmsSwitch.JUGUANG.getValue().toString(), smsSwitch)) {
            log.info("聚光验证码短信发送:{},{}{}", phones, title, content);
            return smsJuGuangMsg.sendJuGuangSms(phones, title, content);
        }
        //HTTP发送短信
        if(Objects.equals(SmsSwitch.HTTPSMS.getValue().toString(), smsSwitch)) {
            log.info("HTTP验证码短信发送:{},{}{}", phones, title, content);
            return smsJuGuangMsg.sendHttpSms(phones, title, content);
        }
        //华信云发送短信
        if (Objects.equals(SmsSwitch.HUAXINYUN.getValue().toString(), smsSwitch)) {
            log.info("华信云验证码短信发送:{},{}{}", phones, title, content);
            try {
                return smsJuGuangMsg.sendHuaXinSms(phones, title, code);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        //新猫短信
        if (Objects.equals(SmsSwitch.XINMAO.getValue().toString(), smsSwitch)) {
            log.info("新猫短信验证码短信发送:{},{}{}", phones, title, content);
            try {
                return smsJuGuangMsg.sendxinmaoSms(phones, title, code);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return false;
    }

    /**
     *
     */
    @JmsListener(destination = QueueConstans.sendSmsCodeQueue)
    public void dealSmsCodeTask(String data) {
        JSONObject param = JSONObject.parseObject(data);
        sendCode(param.getString("phone"), param.getString("code"), param.getBoolean("isCheckPhoneNum"), param.getInteger("type"));
    }

    /**
     * 缓存短信内容
     *
     * @return
     */
    public Map<String, String> getRedisMap() {
        Map<String, String> keys = new HashMap<>();
        if (redisUtil.hasKey(Constant.SMS_SERVICE)) {
            String backParam = redisUtil.get(Constant.SMS_SERVICE);
            keys = JSONObject.parseObject(backParam, Map.class);
        } else {
            //风控相关缓存进Redis
            List<BackConfigParamsVo> groups = backConfigParamsDao.findByGroup(Constant.SMS_SERVICE);
            if (!CollectionUtils.isEmpty(groups)) {
                for (BackConfigParamsVo backConfigParam : groups) {
                    keys.put(backConfigParam.getSysKey(), backConfigParam.getStrValue());
                }
            }
            redisUtil.set(Constant.SMS_SERVICE, JSONObject.toJSONString(keys));
        }
        return keys;
    }

    private boolean getSendPhoneInRedis(String phone) {
        String s = redisUtil.get(phone);
        if (s != null) {
            // 重复未发送，限制次数减一
            String limitKey = SMS_LIMIT_KEY + phone;
            if (redisUtil.get(limitKey) == null) {
                return false;
            }
            int num = Integer.valueOf(redisUtil.get(limitKey));
            if (num < Constant.LIMIT_TIMES) {
                redisUtil.set(limitKey, String.valueOf(num - 1));
                log.info(SMS_LIMIT_KEY + phone + ":" + num);
                redisUtil.expire(limitKey, Constant.PHONE_CAPTCHA_EXPIRATION_TIME);
                return true;
            }
        }
        return false;
    }

    private boolean setSendPhone(String phone) {
        return redisUtil.set(phone, phone, 60);
    }
}
