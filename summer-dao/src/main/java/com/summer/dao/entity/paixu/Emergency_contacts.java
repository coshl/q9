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
public class Emergency_contacts {

    private String relation;
    private String name;
    private String phone;

    public Emergency_contacts() {
    }

    public Emergency_contacts(String relation, String name, String phone) {
        this.relation = relation;
        this.name = name;
        this.phone = phone;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

}