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
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;

/**
 *
 */
public class CallBackResult<T> implements Serializable {

    private static final long serialVersionUID = -8522488818902709763L;


    // header 常量定义
    public static final Integer SUCCESS = 200;//状态成功
    public static final Integer PERMISSION_DENIED = 403;//没有权限
    public static final Integer ABRUPT_ABNORMALITY = 500;//系统异常
    public static final Integer BUSINESS_ERROR = 609;

    public static final Integer GUGE = 100;//GU
    public static final Integer BUSINESS_WAIT = 709;
    public static final Integer BUSINESS_INIT = 909;
    public static final Integer BUSINESS_DEFEAT = 809;
    public static final Integer CREATED = 201;//新建或修改数据成功
    public static final Integer NO_CONTENT_SUCCESS = 204;//删除数据成功
    public static final Integer INVALID_REQUEST = 400;//新建或修改数据的操作失败
    public static final Integer INVALID_REQUEST_FAIL = 402;//删除失败
    public static final Integer LOGIN_IS_PAST = -2; //登录失效
    public static final String MSG_ERROR = "系统异常";
    public static final String MSG_SUCCESS = "成功";
    public static final String MSG_NO_PERMISSION = "没有签名";
    public static final String MSG_BAD_PERMISSION = "签名错误";
    public static final String MSG_DATA_EMPTY = "暂无数据";
    public static final String MSG_CREATED = "新建或更新数据成功";
    public static final String MSG_NO_CONTENT_SUCCESS = "删除成功";
    public static final String MSG_INVALID_REQUEST = "新建或更新数据失败";
    public static final String MSG_INVALID_REQUEST_FAIL = "删除失败";
    public static final String MSG_OPERATE_FAIL = "操作失败";
    /**
     * 请求参数错误
     */
    public static final Integer PARAM_IS_ERROR = 501;
    public static final String PARAM_IS_EXCEPTION_MSG = "请求参数不合法";


    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    public CallBackResult() {
    }

    public CallBackResult(Object... datas) {
        if (datas != null) {
            if (datas.length == 1) {
                this.data = datas[0];
            } else if (datas.length > 1) {
                this.data = datas;
            }
        }
        this.code = SUCCESS;
        this.message = MSG_SUCCESS;
        this.success = Boolean.TRUE;
    }

    public CallBackResult(Integer code, String msg, Boolean success, Object... datas) {
        if (datas != null) {
            if (datas.length == 1) {
                this.data = datas[0];
            } else if (datas.length > 1) {
                this.data = datas;
            }
        } else {
            this.data = "";
        }
        this.message = msg;
        this.code = code;
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public static String returnJson(Object... datas) {
        CallBackResult callBackResult = new CallBackResult(datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

    public static String returnJson(Integer code, String msg, Object... datas) {
        CallBackResult callBackResult = new CallBackResult(code, msg, null, datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

    public static String returnJson(Integer code, String msg, Boolean success, Object... datas) {
        CallBackResult callBackResult = new CallBackResult(code, msg, success, null, datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

    public static String returnSuccessJson() {
        CallBackResult callBackResult = new CallBackResult(SUCCESS, MSG_SUCCESS, Boolean.TRUE, null);
        return JSONObject.toJSONString(callBackResult);
    }

    public static String returnErrorJson() {
        CallBackResult callBackResult = new CallBackResult(ABRUPT_ABNORMALITY, MSG_ERROR, Boolean.FALSE, null);
        return JSONObject.toJSONString(callBackResult);
    }

    public static CallBackResult ok(Object... datas) {
        return new CallBackResult(SUCCESS, "success", Boolean.TRUE, datas);
    }

    public static CallBackResult ok() {
        return new CallBackResult(200, MSG_SUCCESS, Boolean.TRUE, null);
    }

    public static CallBackResult okMsg(String msg) {
        return new CallBackResult(200, msg, Boolean.TRUE, null);
    }

    public static CallBackResult fail(Integer code, String msg) {
        return new CallBackResult(code, msg, Boolean.FALSE, null);
    }

    public static CallBackResult fail(String msg) {
        return new CallBackResult(ABRUPT_ABNORMALITY, msg, Boolean.FALSE, null);
    }

    public static CallBackResult fail() {
        return new CallBackResult(ABRUPT_ABNORMALITY, MSG_OPERATE_FAIL, Boolean.FALSE, null);
    }

}
