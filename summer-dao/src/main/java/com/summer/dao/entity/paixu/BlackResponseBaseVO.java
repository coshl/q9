package com.summer.dao.entity.paixu;

public class BlackResponseBaseVO {

    /**
     * 结果编码
     */
    String code;

    /**
     * 描述信息
     */
    String message;
    String data;

    /**
     * 签名
     */
    String sign;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
