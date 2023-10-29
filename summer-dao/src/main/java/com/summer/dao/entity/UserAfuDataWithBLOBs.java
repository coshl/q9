/* https://github.com/12641561 */
package com.summer.dao.entity;

public class UserAfuDataWithBLOBs extends UserAfuData {


    /**
     * 阿福风险报告原始数据
     */
    private String afReportData;

    /**
     * 阿福评分原始数据
     */
    private String afScoreData;

    public UserAfuDataWithBLOBs() {
    }

    public UserAfuDataWithBLOBs(Long pid, Long uid, String afReportData, String afScoreData) {
        this.pid = pid;
        this.uid = uid;
        this.afReportData = afReportData;
        this.afScoreData = afScoreData;
    }

    /**
     * 获取 阿福风险报告原始数据 user_afu_data.af_report_data
     *
     * @return 阿福风险报告原始数据
     */
    public String getAfReportData() {
        return afReportData;
    }

    /**
     * 设置 阿福风险报告原始数据 user_afu_data.af_report_data
     *
     * @param afReportData 阿福风险报告原始数据
     */
    public void setAfReportData(String afReportData) {
        this.afReportData = afReportData;
    }

    /**
     * 获取 阿福评分原始数据 user_afu_data.af_score_data
     *
     * @return 阿福评分原始数据
     */
    public String getAfScoreData() {
        return afScoreData;
    }

    /**
     * 设置 阿福评分原始数据 user_afu_data.af_score_data
     *
     * @param afScoreData 阿福评分原始数据
     */
    public void setAfScoreData(String afScoreData) {
        this.afScoreData = afScoreData;
    }


}