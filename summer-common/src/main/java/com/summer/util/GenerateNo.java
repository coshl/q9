package com.summer.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 产生各种单号
 *
 * @author zhushuai
 */
public class GenerateNo {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private static int indx = 10;

    public static synchronized int nextIndx() {
        if (indx > 999) {
            indx = 10;
        }
        return indx++;
    }

    /**
     * 生成充值流水号，是E开始+ 用户ID+当前的年月日时分秒+6位不重复的随机数
     *
     * @return
     */
    public static String payRecordNo(Integer userId) {
        try {
            String pre = "E";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());
            String randomString = String.valueOf(Math.random()).substring(2).substring(0, 6);

            return pre + userId + dateString + randomString;
        } catch (Exception e) {
            System.out.println("生成充值流水号出错====" + e.toString());
        }
        return "";
    }

    /**
     * 生成充值流水号，是pre开始+ 用户ID+当前的年月日时分秒+6位不重复的随机数
     *
     * @return
     */
    public static String payRecordNo(String pre) {
        try {
            if (pre == null) {
                pre = "E";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());
            String randomString = String.valueOf(Math.random()).substring(2).substring(0, 6);

            return pre + dateString + randomString;
        } catch (Exception e) {
            System.out.println("生成充值流水号出错====" + e.toString());
        }
        return "";
    }

    /**
     * 获取纯数字唯一订单号
     *
     * @return
     */
    public static String nextOrdId() {
        long time = System.currentTimeMillis();
        int end = (int) (Math.random() * 10);
        return String.valueOf(nextIndx())
                .concat(String.valueOf(time))
                .concat(String.valueOf(end));

    }


    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        str = str.replaceAll("-", "").replaceAll("[a-zA-Z]", "");
        int end = (int) ((Math.random() + 1) * 10000);
        // 去掉"-"符号
        return str.substring(0, 10) + end;

    }


    /**
     * 获取纯数字唯一订单号10位数，有问题，需要完善。不一定唯一
     *
     * @return
     */
    public static String nextOrdIdTen() {
        long time = System.currentTimeMillis();
        System.out.println(time);
        return String.valueOf(time).substring(3, 13);
//    	  int end = (int) (Math.random() * 10);
//    	  return String.valueOf(nextIndx())
//    			  .concat(String.valueOf(time))
//    			  .concat(String.valueOf(end));
//    			  
    }

    public static void main(String[] args) throws Exception {
//    	 System.out.println(GenerateNo.payRecordNo("A"));
//    	 System.out.println(payRecordNo("B"));
//    	 System.out.println(payRecordNo("C"));
//    	 System.out.println(nextOrdIdTen());
      /*  for (int i = 0; i < 10; i++) {

            System.out.println(nextOrdId());
        }*/

        String charAndNumr = getCharAndNumr(4);
        System.out.println(charAndNumr);

    }

    /**
     * 生成充值流水号16位，是当前的年月日时分秒+2位不重复的随机数
     *
     * @return
     */
    public static String payRecordNo() {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());
            String randomString = String.valueOf(Math.random()).substring(2).substring(0, 2);

            return dateString + randomString;
        } catch (Exception e) {
            System.out.println("生成充值流水号出错====" + e.toString());
        }
        return "";
    }

    /**
     * 生成VipCard，是VIP开始+ 用户ID+当前的年月日时分秒+6位不重复的随机数
     *
     * @return
     */
    public static String vipCard(Integer userId) {
        try {
            String pre = "VIP";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());
            String randomString = String.valueOf(Math.random()).substring(2).substring(0, 6);
            return pre + userId + dateString + randomString;
        } catch (Exception e) {
            System.out.println("生成VIPCard出错====" + e.toString());
        }
        return "";
    }

    /**
     * 生成标编号 生成规则，标种编号+发标人ID+年月日时分少+6位随机数
     * 2014-1-9
     * cjx
     */
    public static String getBorrowNo(String borrowTypeNo, Integer userId) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());
            String randomString = String.valueOf(Math.random()).substring(2).substring(0, 6);

            return borrowTypeNo + userId + dateString + randomString;
        } catch (Exception e) {
            System.out.println("生成标编号出错====" + e.toString());
        }
        return "";
    }


    /**
     * 获得num位订单号
     *
     * @param num
     * @return
     */
    public static String generateShortUuid(int num) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < num; i++) {
            String str = uuid.substring(i, i + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static List<String> generatePassword(int number) {
        List<String> listString = new ArrayList<>(); // 用于存放返回值
        List<Integer> length = null; // 字符串长度
        StringBuffer sb = new StringBuffer(); // 中间变量
        int control = 0; // 控制个数
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"};
        while (true) {
            // 控制结束
            if (control == number) {
                break;
            }
            String uuid = UUID.randomUUID().toString().replace("-", "");
            sb.setLength(0);
            // 拼凑字符串
            for (int i = 0; i < 8; i++) {
                String str = uuid.substring(i * 3, (i * 3 + 3));
                //将str字符串转换为16进制，获得其值
                int x = Integer.parseInt(str, 16);
                //取余：x % 0x3E--0x3E = 3*16 + 14 = 62, 其中chars有62个字符
                sb.append(chars[x % 0x3E]);
            }
            listString.add(sb.toString());
            control++;
        }
        return listString;
    }

    /**
     * 生成渠道编码,
     *
     * @param num
     * @return
     */
    public static String generateChannelCode(int num, int channelId) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < num; i++) {
            String str = uuid.substring(i, i + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer = shortBuffer.append(charsChannel[x % 0x34]);
        }
        shortBuffer.append(channelId);
        return shortBuffer.toString();
    }

    public static String[] charsChannel = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 根据时间生成快钱绑卡，发送验证码的外部跟踪编号
     *
     * @return
     */
    public static String outOrderNo() {
        return "KQ" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 快钱绑卡时发送验证码的客户号前缀
     *
     * @return
     */
    public static String generateCustomerId() {
        return "CID" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 随机生成数字和字母
     * @param length
     * @return
     */
    public static String getCharAndNumr(int length) {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();

    }




}
