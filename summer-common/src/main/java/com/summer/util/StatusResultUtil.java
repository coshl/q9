package com.summer.util;

/**
 * 规则开启或者关闭成功失败返回工具类
 */
public class StatusResultUtil {
    /**
     * 规则状态操作成功
     *
     * @param state
     * @return
     */
    public static String isSucc(byte state) {
        if (1 == state) {
            return CallBackResult.returnJson(CallBackResult.CREATED, "关闭成功");
        } else {
            return CallBackResult.returnJson(CallBackResult.CREATED, "开启成功");
        }
    }

    /**
     * 规则状态操作失败
     *
     * @param state
     * @return
     */
    public static String isBad(byte state) {
        if (1 == state) {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "关闭失败");
        } else {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "开启失败");
        }
    }

    /**
     * 规则状态一键操作成功
     *
     * @param state
     * @return
     */
    public static String unifyIsSucc(byte state) {
        if (1 == state) {
            return CallBackResult.returnJson(CallBackResult.CREATED, "一键关闭成功");
        } else {
            return CallBackResult.returnJson(CallBackResult.CREATED, "一键开启成功");
        }
    }

    /**
     * 规则状态一键操作失败
     *
     * @param state
     * @return
     */
    public static String unifyIsBad(byte state) {
        if (1 == state) {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "一键关闭失败");
        } else {
            return CallBackResult.returnJson(CallBackResult.INVALID_REQUEST, "一键开启失败");
        }
    }
}
