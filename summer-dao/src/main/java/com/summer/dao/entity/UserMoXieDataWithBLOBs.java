/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class UserMoXieDataWithBLOBs extends UserMoXieData {
    /**
     * 魔蝎成功采集后返回的原始数据
     */
    private String mxRaw;

    /**
     * 魔蝎成功采集后返回的报告数据
     */
    private String mxReport;
    private String msgShow;

    public UserMoXieDataWithBLOBs() {
    }

    public UserMoXieDataWithBLOBs(Integer userId, String phone, String taskId, Date createTime, Byte status,Integer mxAuthStatus,
                                  Date updateTime) {
        this.userId = userId;
        this.phone = phone;
        this.taskId = taskId;
        this.createTime = createTime;
        this.status = status;
        this.mxAuthStatus = mxAuthStatus;
        this.updateTime = updateTime;
    }

    public String getMsgShow() {
        return msgShow;
    }

    public void setMsgShow(String msgShow) {
        this.msgShow = msgShow;
    }

    /**
     * 获取 魔蝎成功采集后返回的原始数据 user_moxie_data.mx_raw
     *
     * @return 魔蝎成功采集后返回的原始数据
     */
    public String getMxRaw() {
        return mxRaw;
    }

    /**
     * 设置 魔蝎成功采集后返回的原始数据 user_moxie_data.mx_raw
     *
     * @param mxRaw 魔蝎成功采集后返回的原始数据
     */
    public void setMxRaw(String mxRaw) {
        this.mxRaw = mxRaw;
    }

    /**
     * 获取 魔蝎成功采集后返回的报告数据 user_moxie_data.mx_report
     *
     * @return 魔蝎成功采集后返回的报告数据
     */
    public String getMxReport() {
        return mxReport;
    }

    /**
     * 设置 魔蝎成功采集后返回的报告数据 user_moxie_data.mx_report
     *
     * @param mxReport 魔蝎成功采集后返回的报告数据
     */
    public void setMxReport(String mxReport) {
        this.mxReport = mxReport;
    }
}