/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class UserShortMessage {
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

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 上传时间
     */
    private Date creatTime;

    /**
     * 获取 序列号 user_short_message.id
     *
     * @return 序列号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 序列号 user_short_message.id
     *
     * @param id 序列号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 短信内容 user_short_message.message_content
     *
     * @return 短信内容
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置 短信内容 user_short_message.message_content
     *
     * @param messageContent 短信内容
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    /**
     * 获取 短信时间 user_short_message.message_date
     *
     * @return 短信时间
     */
    public String getMessageDate() {
        return messageDate;
    }

    /**
     * 设置 短信时间 user_short_message.message_date
     *
     * @param messageDate 短信时间
     */
    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate == null ? null : messageDate.trim();
    }

    /**
     * 获取 手机号 user_short_message.phone
     *
     * @return 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号 user_short_message.phone
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 用户ID user_short_message.user_id
     *
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户ID user_short_message.user_id
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 上传时间 user_short_message.creat_time
     *
     * @return 上传时间
     */
    public Date getCreatTime() {
        return creatTime;
    }

    /**
     * 设置 上传时间 user_short_message.creat_time
     *
     * @param creatTime 上传时间
     */
    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
}