package com.summer.util;

import java.util.Map;

public class JsonResult {

    public static String SUCCESS = "0";
    /**
     * 返回代码
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 扩展信息
     */
    private Object data;

    private Map<String, String> paramsMap;


    public boolean isSuccessed() {
        return getCode().equals(SUCCESS);
    }

    public JsonResult(String code) {
        this.code = code;
    }

    public JsonResult() {
    }

    public JsonResult(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
