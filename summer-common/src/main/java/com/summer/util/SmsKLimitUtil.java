package com.summer.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 短信相关工具类
 */
@Slf4j
public class SmsKLimitUtil {

    private static final String SMS_LIMIT_KEY = "SMS_LIMIT_KEY_";
    private static final String SMS_TIMES = "IP_TIMES_";
    private static final String SMS_MINUTE = "SMS_MINUTE_";
    private static final String EXPIRE = "EXPIRE_";

    /**
     * 短信次数限制
     *
     * @param redisUtil   传进来的Redis 避免该工具类注入Redis 需要同时把该类注入Spring容器，
     * @param phonenumber 发送的手机号码
     * @param type        发送的类型（根据需求是否需要类型）
     * @param expireTime  过期时间
     * @param tims
     * @return
     */
    public static boolean limitTimes(RedisUtil redisUtil, String phonenumber, Integer type, Long expireTime, Integer tims) {
        log.info("次数限制开始------phone={},type={}", phonenumber, type);
        String limitKey = SMS_LIMIT_KEY + phonenumber;
        if (null != type) {
            limitKey = limitKey + "_" + type;
        }
        saveCaptchDate(limitKey, redisUtil, phonenumber, type, expireTime, tims);
        //同上 把IP地址换成时间
        if (!redisUtil.hasKey(limitKey)) {
            redisUtil.set(limitKey, "1");
            redisUtil.expire(limitKey, expireTime);
            return true;
        }
        int num = Integer.valueOf(redisUtil.get(limitKey));
        if (num < tims) {
            redisUtil.set(limitKey, String.valueOf(num + 1));
            redisUtil.expire(limitKey, expireTime);
            return true;
        }
        return false;
    }

    /**
     * 落地页注册 ip限制
     *
     * @param redisUtil
     * @param request
     * @return  1 - 多余请求  2 - ip限制  3 - 正常请求
     */
    public static int limitTimesIP(RedisUtil redisUtil, HttpServletRequest request,String phoneNumber) {
        String os = getOsAndBrowserInfo(request);
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        log.info("sms device={},ip={}", os, ipAdrress);
      /*  if (!"Android".equals(os) && !"IPhone".equals(os) && !"Mac".equals(os)) {
            log.info("验证码设备未通过,ip={},os={}", ipAdrress, os);
            return 2;
        }*/
        String dateFormatMin = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm");
        String minkey = SMS_MINUTE+ phoneNumber+"_" + ipAdrress + "_" + dateFormatMin;
        long minIn = redisUtil.incr(minkey, 1);
        log.info("验证码每分钟发送超过次数,ip={},times={}", ipAdrress, minIn);
        if (minIn > 1) {
            log.info("验证码每分钟发送超过次数1,ip={},times={}", ipAdrress, minIn);
            log.info("执行返回");
            return 1;
        }
        int timeIn = 20;
        boolean expireIn = redisUtil.expire(minkey, timeIn);
        if (!expireIn) {
            boolean expire1 = redisUtil.expire(minkey, timeIn);
            if (!expire1) {
                log.error("sms expireIn error ip=" + ipAdrress);
            }
        }


        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        String key = SMS_MINUTE+ phoneNumber+"_" + ipAdrress + "_" + dateFormat;
        long cs = redisUtil.incr(key, 1);
        int time = 60 * 60 * 24;
        boolean expire = redisUtil.expire(key, time);
        if (!expire) {
            boolean expire1 = redisUtil.expire(key, time);
            if (!expire1) {
                log.error("sms expire error ip=" + ipAdrress);
            }
        }
        log.info("验证码每天发送超过次数,ip={},times={}", ipAdrress, cs);
        if (cs > 20) {
            log.info("验证码每天发送超过次数5,ip={},times={}", ipAdrress, cs);
            return 2;
        }
        return 3;
    }

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     *
     * @param request
     * @return
     */
    public static String getOsAndBrowserInfo(HttpServletRequest request) {
        String browserDetails = request.getHeader("User-Agent");
        String userAgent = browserDetails;

        String os = "";
        //=================OS Info=======================
        if (StringUtils.isNotBlank(userAgent)) {
            if (userAgent.toLowerCase().contains("windows")) {
                os = "Windows";
            } else if (userAgent.toLowerCase().contains("mac")) {
                os = "Mac";
            } else if (userAgent.toLowerCase().contains("x11")) {
                os = "Unix";
            } else if (userAgent.toLowerCase().contains("android")) {
                os = "Android";
            } else if (userAgent.toLowerCase().contains("iphone")) {
                os = "IPhone";
            } else {
                os = "UnKnown, More-Info: " + userAgent;
            }
        } else {
            os = "UnKnown, More-Info: " + userAgent;
        }
        return os;
    }

    /**
     * 同时保存一个限制短信条数的时间，通过该时间来判断，如果当前时间和redis保存的时间大于等于1，就清除该用户验证码的限制次数，重新计算
     *
     * @param limitKey
     * @param redisUtil
     * @param phonenumber
     * @param type
     * @param expireTime
     * @param tims
     */
    public static void saveCaptchDate(String limitKey, RedisUtil redisUtil, String phonenumber, Integer type, Long expireTime, Integer tims) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            //保存时间的key
            String key = EXPIRE + limitKey;
            if (redisUtil.hasKey(limitKey) && redisUtil.hasKey(key)) {
                String dateStr = redisUtil.get(key);
                if (StringUtils.isNotBlank(dateStr)) {
                    //提前保存到redis的时间
                    Date passDate = dateFormat.parse(dateStr);
                    int day = DateUtil.daysBetween(passDate, date);
                    if (day >= 1) {
                        //如果天数大于等于1天，就清除过去的限制次数
                        redisUtil.del(limitKey);
                        redisUtil.del(key);
                        log.info("验证码删除限制次数成功------phone={},type={}", phonenumber, type);
                        return;
                    }
                }
            } else {
                String nowDate = dateFormat.format(date);
                redisUtil.set(key, nowDate, expireTime);
                log.info("验证码设置时间验证码发送的时间------phone={},type={}", phonenumber, type);
            }
        } catch (Exception e) {
            log.info("验证码判断过期时间异常------phone={},type={}", phonenumber, type);
            return;
        }
    }


    /**
     * 00:00后清除所有的验证码过期时间
     *
     * @param redisUtil
     * @param phonenumber
     * @param type
     * @return
     */
    public static Object captchExpire(RedisUtil redisUtil, String phonenumber, Integer type) {
        String limitKey = SMS_LIMIT_KEY + phonenumber;
        if (null != type) {
            limitKey = limitKey + "_" + type;
        }
        if (redisUtil.hasKey(limitKey)) {
            redisUtil.del(limitKey);
            redisUtil.del(EXPIRE + limitKey);
            log.info("验证码次数限制手动删除成功----------------phone={},type={}", phonenumber, type);
        }
        return CallBackResult.returnSuccessJson();
    }

    /**
     * APP，后台登陆，修改密码短信发送ip限制
     *
     * @param redisUtil
     * @param request
     * @return
     */
    public static boolean limitTimesIPByCommon(RedisUtil redisUtil, HttpServletRequest request) {
        String os = getOsAndBrowserInfo(request);
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        log.info("【APP】sms device={},ip={}", os, ipAdrress);

        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        String key = SMS_TIMES + ipAdrress + "_" + dateFormat;
        long cs = redisUtil.incr(key, 1);
        int time = 60 * 60 * 24;
        boolean expire = redisUtil.expire(key, time);
        if (!expire) {
            boolean expire1 = redisUtil.expire(key, time);
            if (!expire1) {
                log.error("sms expire error ip=" + ipAdrress);
            }
        }
        log.info("验证码每天发送超过次数,ip={},times={}", ipAdrress, cs);
        if (cs > 50) {
            log.info("验证码每天发送超过次数5,ip={},times={}", ipAdrress, cs);
            return true;
        }
        return false;
    }

    /**
     * 删除ip
     *
     * @param redisUtil
     * @return
     */
    public static void delIp(RedisUtil redisUtil, String ipAdrress) {
        log.info("delIp,ip={}", ipAdrress);
        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        String key = SMS_TIMES + ipAdrress + "_" + dateFormat;
        redisUtil.del(key);
    }



    /**
     * APP，后台登陆，修改密码短信发送ip限制
     *
     * @param redisUtil
     * @param request
     * @return
     */
    public static boolean limitTimesIPApp(RedisUtil redisUtil, HttpServletRequest request) {
        String os = getOsAndBrowserInfo(request);
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        log.info("【APP】sms device={},ip={}", os, ipAdrress);

        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        String key = SMS_TIMES + ipAdrress + "_" + dateFormat;
        long cs = redisUtil.incr(key, 1);
        int time = 60 * 60 * 24;
        boolean expire = redisUtil.expire(key, time);
        if (!expire) {
            boolean expire1 = redisUtil.expire(key, time);
            if (!expire1) {
                log.error("sms expire error ip=" + ipAdrress);
            }
        }
        log.info("验证码每天发送超过次数,ip={},times={}", ipAdrress, cs);
        if (cs > 10) {
            log.info("验证码每天发送超过次数5,ip={},times={}", ipAdrress, cs);
            return true;
        }
        return false;
    }
}
