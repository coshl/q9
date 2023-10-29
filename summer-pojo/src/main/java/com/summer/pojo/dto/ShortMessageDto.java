package com.summer.pojo.dto;

/**
 * 用户短信上传参数dto
 */
public class ShortMessageDto {
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
