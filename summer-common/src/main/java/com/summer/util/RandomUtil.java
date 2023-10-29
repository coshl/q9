package com.summer.util;

import java.util.Random;

/**
 * 随机数工具类
 */
public class RandomUtil {

    /**
     * 获取六位随机salt值（数字加大小写字母）
     *
     * @return
     */
    public static String getRandomSalt(int number) {
        Random random = new Random();
        random.nextInt();
        String str = "";
        char[] ch = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (int i = 0; i < number; i++) {
            char num = ch[random.nextInt(ch.length)];
            str += num;
        }
        return str;
    }

    /**
     * 获取4位随机数
     *
     * @return
     */
    public static String getCode() {
        String code = "";
        for (int i = 0; i < 4; i++) {
            code = code + (int) (Math.random() * 10);
        }
        return code;
    }

    /**
     * 获取随机字母
     *
     * @return
     */
    public static String getRandomLetter(int number) {
        Random random = new Random();
        random.nextInt();
        String str = "";
        char[] ch = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (int i = 0; i < number; i++) {
            char num = ch[random.nextInt(ch.length)];
            str += num;
        }
        return str;
    }
}
