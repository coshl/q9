package com.summer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取时间或转换为字符串
 */
public class TimeUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 增加一天
     *
     * @param endTime
     * @return
     */
    public static String addOneDay(String endTime) {
        if (PublicUtil.isEmpty(endTime)) {
            return null;
        }
        Date date = null;
        try {
            date = simpleDateFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime();
        return simpleDateFormat.format(date);
    }

    /**
     * date当天的0点的时间戳
     */
    public static Date dateHourZero(Date date) {

        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(Calendar.HOUR_OF_DAY, 0);
        cld.set(Calendar.MINUTE, 0);
        cld.set(Calendar.SECOND, 0);

        return cld.getTime();
    }

    public static Date dateLastDay(Date date) {

        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.add(Calendar.DAY_OF_MONTH, -1);
        return cld.getTime();
    }

    /**
     * date明天的0点的时间戳
     */
    public static Date nextDayHourZero(Date date) {

        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(Calendar.HOUR_OF_DAY, 0);
        cld.set(Calendar.MINUTE, 0);
        cld.set(Calendar.SECOND, 0);
        cld.add(Calendar.DATE, 1);
        return cld.getTime();
    }

    /**
     * date半小时之前
     */
    public static Date halfHourBefore(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.add(Calendar.MINUTE, 30);
        return cld.getTime();
    }

    /**
     * 昨天下午18点
     */
    public static Date dateLastDayH18(Date date) {

        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.add(Calendar.DAY_OF_MONTH, -1);
        cld.set(Calendar.MINUTE, 0);
        cld.set(Calendar.HOUR_OF_DAY, 18);
        return cld.getTime();
    }

    /**
     * 今天上午9点
     */
    public static Date todayHourNine(Date date) {

        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        cld.set(Calendar.MINUTE, 0);
        cld.set(Calendar.HOUR_OF_DAY, 9);
        return cld.getTime();
    }

    /**
     * date的yyyy-MM-dd格式
     */
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date.getTime());
        return dateStr;
    }

    /**
     * date的 HH-mm格式
     */
    public static String timeToStringHHmm(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        String dateStr = format.format(date.getTime());
        return dateStr;
    }

    /**
     * 判断格式化的时间 如：2018-09-29 是否是今天
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isToday(String startTime, String endTime) {

        Date now = new Date();
        String today = TimeUtil.dateToString(now);
        boolean todayFlag = false;

        if (startTime != null &&
                endTime != null &&
                today.compareTo(startTime) >= 0 &&
                today.compareTo(endTime) <= 0) {
            todayFlag = true;
        } else if (startTime != null &&
                endTime != null &&
                today.compareTo(startTime) <= 0 &&
                today.compareTo(endTime) >= 0) {
            todayFlag = true;
        }
        if (startTime == null) {
            todayFlag = true;
        }
        return todayFlag;
    }

    /**
     * hh-mm格式的时间变成今日的日期
     *
     * @param hhmmStr
     * @return
     */
    public static Date HHmmtoDateToday(String hhmmStr) {
        Date date = new Date();
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        hhmmStr.split("-");
        Integer hour = Integer.valueOf(hhmmStr.split("-")[0]);
        Integer minute = Integer.valueOf(hhmmStr.split("-")[1]);
        cld.set(Calendar.MINUTE, minute);
        cld.set(Calendar.HOUR_OF_DAY, hour);
        return cld.getTime();
    }
}
