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
public class Contact {

    private String contact_name;
    private String contact_phone;
    private String update_time;

    public Contact() {
    }

    public Contact(String contact_name, String contact_phone, String update_time) {
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.update_time = update_time;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

}