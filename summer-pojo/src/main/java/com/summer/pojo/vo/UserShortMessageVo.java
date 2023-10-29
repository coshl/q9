package com.summer.pojo.vo;

public class UserShortMessageVo {
    /**
     * 序列号
     */
    private Integer id;

    /**
     * 短信内容
     */
    private String messageContent;

    /**
     * 短信时间
     */
    private String messageDate;

    /**
     * 手机号
     */
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}