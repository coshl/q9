/**
 * Copyright 2019 bejson.com
 */
package com.summer.dao.entity;

/**
 * Auto-generated: 2019-01-15 15:29:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Present {

    private String province;
    private String city;
    private String liveAddr;

    public Present() {
    }

    public Present(String province, String city, String liveAddr) {
        this.province = province;
        this.city = city;
        this.liveAddr = liveAddr;
    }

    @Override
    public String toString() {
        return "Present{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", liveAddr='" + liveAddr + '\'' +
                '}';
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setLiveAddr(String liveAddr) {
        this.liveAddr = liveAddr;
    }

    public String getLiveAddr() {
        return liveAddr;
    }

}