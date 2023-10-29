package com.summer.pojo.dto;

import com.summer.group.HandleFeedbackInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ls
 * @Title:
 * @date 2019/2/25 10:14
 */
public class YunYingShangDTO {
    private Integer call_if_whole_day_3m;
    private Integer dialed_time_3m;
    private String city;
    private Integer call_cnt_morning_3m;
    private Integer call_cnt_night_3m;
    private Integer call_cnt_6m;
    private Integer call_cnt_evening_6m;
    private Integer call_cnt_weekday_3m;
    private Integer call_cnt_holiday_6m;
    private String peer_num;
    private Integer call_cnt_weekend_3m;
    private Integer call_cnt_afternoon_6m;
    private String p_relation;
    private String trans_start;
    private Integer call_time_3m;
    private Integer dial_cnt_3m;
    private Integer dial_time_3m;
    private Integer call_cnt_morning_6m;
    private Integer call_cnt_weekend_6m;
    private Integer call_cnt_holiday_3m;
    private String group_name;
    private Integer call_cnt_evening_3m;
    private Integer call_if_whole_day_6m;
    private Integer dialed_time_6m;
    private Integer call_cnt_weekday_6m;
    private Integer dialed_cnt_3m;
    private Integer call_cnt_night_6m;
    private Integer call_cnt_noon_3m;
    private Integer call_cnt_1m;
    private Integer dialed_cnt_6m;
    private Integer call_cnt_afternoon_3m;
    private String company_name;
    private Integer call_cnt_3m;
    private Integer call_cnt_noon_6m;
    private Integer dial_time_6m;
    private Integer dial_cnt_6m;
    private Integer call_cnt_1w;
    private Integer call_time_6m;
    private String trans_end;

    public Integer getCall_if_whole_day_3m() {
        return call_if_whole_day_3m;
    }

    public void setCall_if_whole_day_3m(Integer call_if_whole_day_3m) {
        this.call_if_whole_day_3m = call_if_whole_day_3m;
    }

    public Integer getDialed_time_3m() {
        return dialed_time_3m;
    }

    public void setDialed_time_3m(Integer dialed_time_3m) {
        this.dialed_time_3m = dialed_time_3m;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCall_cnt_morning_3m() {
        return call_cnt_morning_3m;
    }

    public void setCall_cnt_morning_3m(Integer call_cnt_morning_3m) {
        this.call_cnt_morning_3m = call_cnt_morning_3m;
    }

    public Integer getCall_cnt_night_3m() {
        return call_cnt_night_3m;
    }

    public void setCall_cnt_night_3m(Integer call_cnt_night_3m) {
        this.call_cnt_night_3m = call_cnt_night_3m;
    }

    public Integer getCall_cnt_6m() {
        return call_cnt_6m;
    }

    public void setCall_cnt_6m(Integer call_cnt_6m) {
        this.call_cnt_6m = call_cnt_6m;
    }

    public Integer getCall_cnt_evening_6m() {
        return call_cnt_evening_6m;
    }

    public void setCall_cnt_evening_6m(Integer call_cnt_evening_6m) {
        this.call_cnt_evening_6m = call_cnt_evening_6m;
    }

    public Integer getCall_cnt_weekday_3m() {
        return call_cnt_weekday_3m;
    }

    public void setCall_cnt_weekday_3m(Integer call_cnt_weekday_3m) {
        this.call_cnt_weekday_3m = call_cnt_weekday_3m;
    }

    public Integer getCall_cnt_holiday_6m() {
        return call_cnt_holiday_6m;
    }

    public void setCall_cnt_holiday_6m(Integer call_cnt_holiday_6m) {
        this.call_cnt_holiday_6m = call_cnt_holiday_6m;
    }

    public String getPeer_num() {
        return peer_num;
    }

    public void setPeer_num(String peer_num) {
        this.peer_num = peer_num;
    }

    public Integer getCall_cnt_weekend_3m() {
        return call_cnt_weekend_3m;
    }

    public void setCall_cnt_weekend_3m(Integer call_cnt_weekend_3m) {
        this.call_cnt_weekend_3m = call_cnt_weekend_3m;
    }

    public Integer getCall_cnt_afternoon_6m() {
        return call_cnt_afternoon_6m;
    }

    public void setCall_cnt_afternoon_6m(Integer call_cnt_afternoon_6m) {
        this.call_cnt_afternoon_6m = call_cnt_afternoon_6m;
    }

    public String getP_relation() {
        return p_relation;
    }

    public void setP_relation(String p_relation) {
        this.p_relation = p_relation;
    }

    public String getTrans_start() {
        return trans_start;
    }

    public void setTrans_start(String trans_start) {
        this.trans_start = trans_start;
    }

    public Integer getCall_time_3m() {
        return call_time_3m;
    }

    public void setCall_time_3m(Integer call_time_3m) {
        this.call_time_3m = call_time_3m;
    }

    public Integer getDial_cnt_3m() {
        return dial_cnt_3m;
    }

    public void setDial_cnt_3m(Integer dial_cnt_3m) {
        this.dial_cnt_3m = dial_cnt_3m;
    }

    public Integer getDial_time_3m() {
        return dial_time_3m;
    }

    public void setDial_time_3m(Integer dial_time_3m) {
        this.dial_time_3m = dial_time_3m;
    }

    public Integer getCall_cnt_morning_6m() {
        return call_cnt_morning_6m;
    }

    public void setCall_cnt_morning_6m(Integer call_cnt_morning_6m) {
        this.call_cnt_morning_6m = call_cnt_morning_6m;
    }

    public Integer getCall_cnt_weekend_6m() {
        return call_cnt_weekend_6m;
    }

    public void setCall_cnt_weekend_6m(Integer call_cnt_weekend_6m) {
        this.call_cnt_weekend_6m = call_cnt_weekend_6m;
    }

    public Integer getCall_cnt_holiday_3m() {
        return call_cnt_holiday_3m;
    }

    public void setCall_cnt_holiday_3m(Integer call_cnt_holiday_3m) {
        this.call_cnt_holiday_3m = call_cnt_holiday_3m;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Integer getCall_cnt_evening_3m() {
        return call_cnt_evening_3m;
    }

    public void setCall_cnt_evening_3m(Integer call_cnt_evening_3m) {
        this.call_cnt_evening_3m = call_cnt_evening_3m;
    }

    public Integer getCall_if_whole_day_6m() {
        return call_if_whole_day_6m;
    }

    public void setCall_if_whole_day_6m(Integer call_if_whole_day_6m) {
        this.call_if_whole_day_6m = call_if_whole_day_6m;
    }

    public Integer getDialed_time_6m() {
        return dialed_time_6m;
    }

    public void setDialed_time_6m(Integer dialed_time_6m) {
        this.dialed_time_6m = dialed_time_6m;
    }

    public Integer getCall_cnt_weekday_6m() {
        return call_cnt_weekday_6m;
    }

    public void setCall_cnt_weekday_6m(Integer call_cnt_weekday_6m) {
        this.call_cnt_weekday_6m = call_cnt_weekday_6m;
    }

    public Integer getDialed_cnt_3m() {
        return dialed_cnt_3m;
    }

    public void setDialed_cnt_3m(Integer dialed_cnt_3m) {
        this.dialed_cnt_3m = dialed_cnt_3m;
    }

    public Integer getCall_cnt_night_6m() {
        return call_cnt_night_6m;
    }

    public void setCall_cnt_night_6m(Integer call_cnt_night_6m) {
        this.call_cnt_night_6m = call_cnt_night_6m;
    }

    public Integer getCall_cnt_noon_3m() {
        return call_cnt_noon_3m;
    }

    public void setCall_cnt_noon_3m(Integer call_cnt_noon_3m) {
        this.call_cnt_noon_3m = call_cnt_noon_3m;
    }

    public Integer getCall_cnt_1m() {
        return call_cnt_1m;
    }

    public void setCall_cnt_1m(Integer call_cnt_1m) {
        this.call_cnt_1m = call_cnt_1m;
    }

    public Integer getDialed_cnt_6m() {
        return dialed_cnt_6m;
    }

    public void setDialed_cnt_6m(Integer dialed_cnt_6m) {
        this.dialed_cnt_6m = dialed_cnt_6m;
    }

    public Integer getCall_cnt_afternoon_3m() {
        return call_cnt_afternoon_3m;
    }

    public void setCall_cnt_afternoon_3m(Integer call_cnt_afternoon_3m) {
        this.call_cnt_afternoon_3m = call_cnt_afternoon_3m;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public Integer getCall_cnt_3m() {
        return call_cnt_3m;
    }

    public void setCall_cnt_3m(Integer call_cnt_3m) {
        this.call_cnt_3m = call_cnt_3m;
    }

    public Integer getCall_cnt_noon_6m() {
        return call_cnt_noon_6m;
    }

    public void setCall_cnt_noon_6m(Integer call_cnt_noon_6m) {
        this.call_cnt_noon_6m = call_cnt_noon_6m;
    }

    public Integer getDial_time_6m() {
        return dial_time_6m;
    }

    public void setDial_time_6m(Integer dial_time_6m) {
        this.dial_time_6m = dial_time_6m;
    }

    public Integer getDial_cnt_6m() {
        return dial_cnt_6m;
    }

    public void setDial_cnt_6m(Integer dial_cnt_6m) {
        this.dial_cnt_6m = dial_cnt_6m;
    }

    public Integer getCall_cnt_1w() {
        return call_cnt_1w;
    }

    public void setCall_cnt_1w(Integer call_cnt_1w) {
        this.call_cnt_1w = call_cnt_1w;
    }

    public Integer getCall_time_6m() {
        return call_time_6m;
    }

    public void setCall_time_6m(Integer call_time_6m) {
        this.call_time_6m = call_time_6m;
    }

    public String getTrans_end() {
        return trans_end;
    }

    public void setTrans_end(String trans_end) {
        this.trans_end = trans_end;
    }
}
