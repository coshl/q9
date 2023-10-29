package com.summer.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Desc:
 * Created by tl on 2019/1/3
 */
@Slf4j
public class DateUtil {
    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date passDate = dateFormat.parse("2022-10-10"); //-7
        Date repayDate = dateFormat.parse("2022-11-10 20:48:26"); //-7
        //int day = DateUtil.daysBetween(passDate, date);
        int day = DateUtil.daysBetween(repayDate, passDate);
        System.out.println(day);
    }

    /**
     * 计算时间差（单位：分钟）
     *
     * @return
     */
    public static long minutesBetween(Date date) {
        try {
            Date cur = new Date();
            long diff = cur.getTime() - date.getTime();
            //计算两个时间之间差了多少分钟
            long minutes = diff / (1000 * 60);
            return minutes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentDate(String aFormat) {
        String tDate = new SimpleDateFormat(aFormat).format(new java.util.Date(
                System.currentTimeMillis()));
        return tDate;
    }

    /**
     * 获取时间 format 格式
     *
     * @return
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String time = df.format(date);
        return time;
    }

    /**
     * 时间相加
     */
    public static Date addDay(Date date, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 月份相加
     */
    public static Date addMonth(Date date, int month) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 格式化日期：（yyyy-MM-dd）
     *
     * @return
     */
    public static String formatTimeYmd(Date time) {
        if (null != time) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(time);
        }
        return "";
    }

    /**
     * 格式化日期（yyyy-MM-dd HH:mm:ss）
     *
     * @param time
     * @return
     */
    public static String formatTimeYmdHms(Date time) {
        if (null != time) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(time);
        }
        return "";
    }

    public static String formatTimeYmdHm(Date time) {
        if (null != time) {
            SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateF.format(time);
        }
        return "";
    }

    public static Date parseTimeYmd(String time) {
        try {
            if (null != time) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(time);
            }
        } catch (Exception e) {
            log.error("parseTimeYmd", e);
        }
        return null;
    }

    /**
     * 根据选中的时间向前推多少天
     * selectTime 选择的时间
     * days 向前推的天数 （一般为负数）
     */
    public static Date dateSubtraction(Date selectTime, int days) {
        // 日期处理模块 (将日期加上某些天或减去天数)返回字符串
        // java.util包
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTime(selectTime);
        // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, days);
        return canlendar.getTime();
    }


    public static Date parseTimeYmd1(String time) {
        try {
            if (null != time) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                return dateFormat.parse(time);
            }
        } catch (Exception e) {
            log.error("parseTimeYmd1", e);
        }
        return null;
    }

    /**
     * 格式化日期：（yyyyMMdd）
     *
     * @return
     */
    public static String formatTimeYMD(Date time) {
        if (null != time) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            return dateFormat.format(time);
        }
        return "";
    }

    /**
     * 格式化日期：（yyyyMMddHHmmss）
     *
     * @return
     */
    public static String formatTimeYMDHms(Date time) {
        if (null != time) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            return dateFormat.format(time);
        }
        return "";
    }

    public static Date parseTimeYmdhms(String time) {
        try {
            if (null != time) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateFormat.parse(time);
            }
        } catch (Exception e) {
            log.error("parseTimeYmd", e);
        }
        return null;
    }

    /**
     * 计算两个时间相差的秒数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calLastedTime(Date startDate, Date endDate) {
        int lastMin = 0;
        try {
            if (null != startDate && null != endDate) {
                long startTime = startDate.getTime();
                long endTime = endDate.getTime();
                lastMin = (int) ((endTime - startTime) / 1000);
                return lastMin;
            }
            return lastMin;
        } catch (Exception e) {
            log.error("DateUtil.calLastedTime 计算两个时间相差秒数异常-----error,startTime={},endTime={}", startDate, endDate);
            e.printStackTrace();
            return lastMin;
        }
    }

    private static SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 计算月份差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDifMonth(String startDate, String endDate) {
        int monthDif = 0;
        try {
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                Date createTime = sdfYmd.parse(startDate);
                Date nowTime = sdfYmd.parse(endDate);
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                start.setTime(createTime);
                end.setTime(nowTime);
                int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
                int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
                monthDif = Math.abs(month + result);
            }
            return monthDif;
        } catch (Exception e) {
            log.error("DateUtils.getDifMonth----error   计算两个时间相差的月份异常");
            e.printStackTrace();
            return monthDif;
        }
    }

    /**
     * 获取几分钟之前的时间
     *
     * @param oldTime
     * @param time    分钟
     * @return
     */
    public static String getDate(Date oldTime, int time, String format) {
        long l = oldTime.getTime() - (time * 60 * 1000);
        Date date = new Date(l);
        return getDateFormat(date, format);
    }

    public static String formatYmd(Date time) {
        try {
            if (null != time) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                return dateFormat.format(time);
            }
        } catch (Exception e) {
            log.error("parseTimeYmd", e);
        }
        return null;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 判断是否是今天
     * @param date
     * @return
     */
    public static boolean isNow(Date date){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String toDay =sdf.format(now);
        String day =sdf.format(date);
        if(toDay.equals(day)){
            return true;
        }
        return false;
    }

    public static String getDate(Integer date ) {
        if (date<60) {
            return date+"秒";
        }else if (date>60&&date<3600) {
            int m = date/60;
            int s = date%60;
            return m+"分"+s+"秒";
        }else {
            int h = date/3600;
            int m = (date%3600)/60;
            int s = (date%3600)%60;
            return h+"小时"+m+"分"+s+"秒";
        }

    }
}
