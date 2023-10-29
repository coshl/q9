package com.summer.pojo.dto;

import java.io.Serializable;

/**
 * 参数内容校验返回的类
 *
 * @author Administrator
 */
public class ArgumentInvalidResult implements Serializable {

    private static final long serialVersionUID = -94964037957338419L;
    /**
     * 默认信息
     */
    private String DefaultMessage;
    /**
     * 校验的字段
     */
    private String Field;
    /**
     * 被拒绝的值
     */
    private Object RejectedValue;


    public String getDefaultMessage() {
        return DefaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        DefaultMessage = defaultMessage;
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public Object getRejectedValue() {
        return RejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        RejectedValue = rejectedValue;
    }
}
