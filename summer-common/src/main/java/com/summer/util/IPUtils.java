package com.summer.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Date;

/**
 * Created by jinyaoyuan on 2018/9/13.
 */
@Slf4j
public class IPUtils {
    /**
     * 获取客户端IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//            if ("127.0.0.1".equals(ip)) {
//                // 根据网卡取本机配置的IP
//                InetAddress inet = null;
//                try {
//                    inet = InetAddress.getLocalHost();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                ip = inet.getHostAddress();
//            }
//        }
//
//        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
//        if (ip != null && ip.length() > 15) {
//            if (ip.indexOf(",") > 0) {
//                ip = ip.substring(0, ip.indexOf(","));
//            }
//        }

        return ip;
    }

    /**
     * 每天次数 ip限制
     *
     * @param redisUtil
     * @return
     */
    public static boolean limitTimesIP(RedisUtil redisUtil, String ipAdrress, String prekey, int times) {

        String dateFormat = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        String key = prekey + dateFormat + ipAdrress;
        long cs = redisUtil.incr(key, 1);
        int time = 60 * 60 * 24;
        boolean expire = redisUtil.expire(key, time);
        if (!expire) {
            boolean expire1 = redisUtil.expire(key, time);
            if (!expire1) {
                log.error(prekey + " expire error ip=" + ipAdrress);
            }
        }
        log.info(prekey + "每天次数,ip={},times={}", ipAdrress, cs);
        if (cs > times) {
            log.info(prekey + "每天次数超限,ip={},cs={},times={}", ipAdrress, cs, times);
            return true;
        }
        return false;
    }
}
