package com.summer.dao.entity;

public class UserOcrDataWithBLOBs extends UserOcrData {
    /**
     * 实名认证报告
     */
    private String  ocrDetail;

    /**
     * 欺诈报告
     */
    private String ocrFaceIndicators;


    public String getOcrDetail() {
        return ocrDetail;
    }

    public void setOcrDetail(String ocrDetail) {
        this.ocrDetail = ocrDetail;
    }

    public String getOcrFaceIndicators() {
        return ocrFaceIndicators;
    }

    public void setOcrFaceIndicators(String ocrFaceIndicators) {
        this.ocrFaceIndicators = ocrFaceIndicators;
    }
}
