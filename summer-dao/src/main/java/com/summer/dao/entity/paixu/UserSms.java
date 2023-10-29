/**
 * Copyright 2019 bejson.com
 */
package com.summer.dao.entity.paixu;


/**
 * Auto-generated: 2019-01-15 15:29:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserSms {

    private String user_mobile;
    private String peer_number;
    private String content;
    private String sms_time;
    private int sms_type;
    private String update_time;

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setPeer_number(String peer_number) {
        this.peer_number = peer_number;
    }

    public String getPeer_number() {
        return peer_number;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setSms_time(String sms_time) {
        this.sms_time = sms_time;
    }

    public String getSms_time() {
        return sms_time;
    }

    public void setSms_type(int sms_type) {
        this.sms_type = sms_type;
    }

    public int getSms_type() {
        return sms_type;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

}