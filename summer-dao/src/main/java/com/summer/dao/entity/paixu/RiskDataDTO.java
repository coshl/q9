/**
 * Copyright 2019 bejson.com
 */
package com.summer.dao.entity.paixu;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.Present;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-01-15 15:29:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class RiskDataDTO implements Serializable {

    private String mobile;
    private String idNum;
    private String gender;
    private String apply_time;
    private String operter_type;
    private Present present;
    private Job job;
    private List<Score> score;
    private List<Emergency_contacts> emergency_contacts;
    private JSONObject rawInfo;

    private JSONObject rawReport;
    private List<Contact> contact;
    private List<CallRecords> callRecords;
    private List<UserSms> userSms;
    private List<App> app;

    public RiskDataDTO() {
    }

    public RiskDataDTO(String mobile, String idNum, String gender, String apply_time,
                       Present present, List<Emergency_contacts> emergency_contacts, JSONObject rawInfo,
                       JSONObject rawReport, List<Contact> contact) {
        this.mobile = mobile;
        this.idNum = idNum;
        this.gender = gender;
        this.apply_time = apply_time;
        this.present = present;
        this.emergency_contacts = emergency_contacts;
        this.rawInfo = rawInfo;
        this.rawReport = rawReport;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "RiskDataDTO{" +
                "mobile='" + mobile + '\'' +
                ", idNum='" + idNum + '\'' +
                ", gender='" + gender + '\'' +
                ", apply_time='" + apply_time + '\'' +
                ", present=" + present +
                ", emergency_contacts=" + emergency_contacts +
                ", rawInfo=" + rawInfo +
                ", rawReport=" + rawReport +
                ", contact=" + contact +
                '}';
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setOperter_type(String operter_type) {
        this.operter_type = operter_type;
    }

    public String getOperter_type() {
        return operter_type;
    }

    public void setPresent(Present present) {
        this.present = present;
    }

    public Present getPresent() {
        return present;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

    public void setScore(List<Score> score) {
        this.score = score;
    }

    public List<Score> getScore() {
        return score;
    }

    public void setEmergency_contacts(List<Emergency_contacts> emergency_contacts) {
        this.emergency_contacts = emergency_contacts;
    }

    public List<Emergency_contacts> getEmergency_contacts() {
        return emergency_contacts;
    }

    public JSONObject getRawInfo() {
        return rawInfo;
    }

    public void setRawInfo(JSONObject rawInfo) {
        this.rawInfo = rawInfo;
    }

    public JSONObject getRawReport() {
        return rawReport;
    }

    public void setRawReport(JSONObject rawReport) {
        this.rawReport = rawReport;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setCallRecords(List<CallRecords> callRecords) {
        this.callRecords = callRecords;
    }

    public List<CallRecords> getCallRecords() {
        return callRecords;
    }

    public void setUserSms(List<UserSms> userSms) {
        this.userSms = userSms;
    }

    public List<UserSms> getUserSms() {
        return userSms;
    }

    public void setApp(List<App> app) {
        this.app = app;
    }

    public List<App> getApp() {
        return app;
    }

}