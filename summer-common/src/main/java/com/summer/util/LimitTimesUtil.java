package com.summer.util;


import com.summer.util.log.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 次数限制工具类
 */
@Slf4j
public class LimitTimesUtil {

    /**
     * 次数限制
     *
     * @param time      限制的时间
     * @param redisUtil
     * @param key       redis的key
     * @param times     限制的次数
     * @return
     */
    public static boolean limitTimes(Integer time, RedisUtil redisUtil, String key, int times) {
        long cs = redisUtil.incr(key, 1);
        boolean expire = redisUtil.expire(key, time);
        if (!expire) {
            boolean expire1 = redisUtil.expire(key, time);
            if (!expire1) {
                log.info("limitTimes prekey", key);
            }
        }
        log.info("每天次数,prekey={},times={}", key, cs);
        if (cs > times) {
            return true;
        }
        return false;
    }

    /**
     * 缓存中记录递增次数
     *
     * @param time      缓存时间
     * @param redisUtil
     * @param key       键
     */
    public static void incr(Integer time, RedisUtil redisUtil, String key) {
        long cs = redisUtil.incr(key, 1);
        boolean expire = redisUtil.expire(key, time);
        if (!expire) {
            boolean expire1 = redisUtil.expire(key, time);
            if (!expire1) {
                log.info("limitTimes prekey", key);
            }
        }
        log.info("每天次数,prekey={},times={}", key, cs);
    }


}
