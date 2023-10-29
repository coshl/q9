package com.summer.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

public class ApiCallBackResult implements Serializable {

    private static final long serialVersionUID = -4817985120879856272L;
    private Integer code;
    private String msg;
    private Object data;

    public ApiCallBackResult() {
    }

    public ApiCallBackResult(Object... datas) {
        if (datas != null) {
            if (datas.length == 1) {
                this.data = datas[0];
            } else if (datas.length > 1) {
                this.data = datas;
            }
        }
    }

    public ApiCallBackResult(Integer code, String msg, Object... datas) {
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
        ApiCallBackResult callBackResult = new ApiCallBackResult(datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

    public static String returnJson(Integer code, String msg, Object... datas) {
        ApiCallBackResult callBackResult = new ApiCallBackResult(code, msg, datas);
        return JSONObject.toJSONString(callBackResult, SerializerFeature.WriteMapNullValue);
    }

}
