package com.summer.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ResultInfo {

    //bool类型。请求状态	true: 发生错误; false: 请求正常
    Boolean error;
    //提示信息
    String info;
    //int类型。返回码
    Integer code;
    Object data;

    public ResultInfo(Boolean error, String info, Integer code, Object data) {
        this.error = error;
        this.info = info;
        this.code = code;
        this.data = data;
    }

    public static String resultJson(Boolean error, String info, Integer code, Object data) {
        ResultInfo resultInfo = new ResultInfo(error, info, code, data);
        return JSONObject.toJSONString(resultInfo, SerializerFeature.WriteMapNullValue);
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
