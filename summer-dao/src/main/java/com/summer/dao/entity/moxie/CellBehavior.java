package com.summer.dao.entity.moxie;

import java.io.Serializable;

/**
 * 用户行为分析（按月汇总,汇总近6个月的）
 */
public class CellBehavior implements Serializable {
    private static final long serialVersionUID = 4053233537327667220L;
    private Long sms_cnt;
    private String call_cnt;
    private Long call_time;
    private String cell_loc;
    private String cell_mth;
    private String cell_operator;
    private String cell_operator_zh;
    private String cell_phone_num;
    private Long dial_cnt;
    private Long dial_time;
    private Long dialed_cnt;
    private Long dialed_time;

    private Long net_flow;

    private Long rechange_amount;
    private Long rechange_cnt;

    private Long total_amount;

    public String getCall_cnt() {
        return call_cnt;
    }

    public void setCall_cnt(String call_cnt) {
        this.call_cnt = call_cnt;
    }

    public Long getCall_time() {
        return call_time;
    }

    public void setCall_time(Long call_time) {
        this.call_time = call_time;
    }

    public String getCell_loc() {
        return cell_loc;
    }

    public void setCell_loc(String cell_loc) {
        this.cell_loc = cell_loc;
    }

    public String getCell_mth() {
        return cell_mth;
    }

    public void setCell_mth(String cell_mth) {
        this.cell_mth = cell_mth;
    }

    public String getCell_operator() {
        return cell_operator;
    }

    public void setCell_operator(String cell_operator) {
        this.cell_operator = cell_operator;
    }

    public String getCell_operator_zh() {
        return cell_operator_zh;
    }

    public void setCell_operator_zh(String cell_operator_zh) {
        this.cell_operator_zh = cell_operator_zh;
    }

    public String getCell_phone_num() {
        return cell_phone_num;
    }

    public void setCell_phone_num(String cell_phone_num) {
        this.cell_phone_num = cell_phone_num;
    }

    public Long getDial_cnt() {
        return dial_cnt;
    }

    public void setDial_cnt(Long dial_cnt) {
        this.dial_cnt = dial_cnt;
    }

    public Long getDial_time() {
        return dial_time;
    }

    public void setDial_time(Long dial_time) {
        this.dial_time = dial_time;
    }

    public Long getDialed_cnt() {
        return dialed_cnt;
    }

    public void setDialed_cnt(Long dialed_cnt) {
        this.dialed_cnt = dialed_cnt;
    }

    public Long getDialed_time() {
        return dialed_time;
    }

    public void setDialed_time(Long dialed_time) {
        this.dialed_time = dialed_time;
    }

    public Long getNet_flow() {
        return net_flow;
    }

    public void setNet_flow(Long net_flow) {
        this.net_flow = net_flow;
    }

    public Long getRechange_amount() {
        return rechange_amount;
    }

    public void setRechange_amount(Long rechange_amount) {
        this.rechange_amount = rechange_amount;
    }

    public Long getRechange_cnt() {
        return rechange_cnt;
    }

    public void setRechange_cnt(Long rechange_cnt) {
        this.rechange_cnt = rechange_cnt;
    }

    public Long getSms_cnt() {
        return sms_cnt;
    }

    public void setSms_cnt(Long sms_cnt) {
        this.sms_cnt = sms_cnt;
    }

    public Long getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Long total_amount) {
        this.total_amount = total_amount;
    }
}
