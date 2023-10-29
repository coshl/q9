/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CallBackResultUtil
 * Author:   Liubing
 * Date:     2018/5/16 22:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.summer.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 *
 */
public class ApiResult implements Serializable {

    private static final long serialVersionUID = -8522488218902709763L;


    // header 常量定义
    public static final Integer SUCCESS = 0;//状态成功
    public static final Integer ABRUPT_ABNORMALITY = 500;//系统异常
    public static final Integer BUSINESS_ERROR = 609;
    public static final Integer BUSINESS_DEFEAT = 809;
    public static final Integer LOGIN_IS_PAST = -2; //登录失效
    public static final String MSG_ERROR = "系统异常";
    public static final String MSG_SUCCESS = "OK";

    private Integer code;
    private String msg;
    private Object data;

    public ApiResult() {
    }

    public ApiResult(Object... datas) {
        if (datas != null) {
            if (datas.length == 1) {
                this.data = datas[0];
            } else if (datas.length > 1) {
                this.data = datas;
            }
        }
        this.code = SUCCESS;
        this.msg = MSG_SUCCESS;
    }

    public ApiResult(Integer code, String msg, Object... datas) {
        if (datas != null) {
            if (datas.length == 1) {
                this.data = datas[0];
            } else if (datas.length > 1) {
                this.data = datas;
            }
        } else {
            this.data = "";
        }
        this.msg = msg;
        this.code = code;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static String returnJson(Object... datas) {
        ApiResult callBackResult = new ApiResult(datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

    public static String returnJson(Integer code, String msg, Object... datas) {
        ApiResult callBackResult = new ApiResult(code, msg, datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

    public static ApiResult returnSuccessJson(String res) {
        JSONObject map = new JSONObject();
        map.put("repay_result", res);
        ApiResult callBackResult = new ApiResult(SUCCESS, MSG_SUCCESS, null);
        callBackResult.setData(map);
        return callBackResult;
    }

    public static ApiResult returnErrorJson(String res, String msg) {
        JSONObject map = new JSONObject();
        map.put("repay_result", res);
        ApiResult callBackResult = new ApiResult(ABRUPT_ABNORMALITY, msg, null);
        callBackResult.setData(map);
        return callBackResult;
    }
}
