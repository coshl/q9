package com.summer.dao.entity.moxie;

import java.io.Serializable;

/**
 * 魔蝎行为检测
 */
public class BehaviorCheck implements Serializable {
    private static final long serialVersionUID = 8337789136862048533L;
    //结果
    private String result;
    //证据
    private String evidence;
    //标记
    private String score;
    //分析点
    private String check_point;
    //分析名称
    private String check_point_cn;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCheck_point() {
        return check_point;
    }

    public void setCheck_point(String check_point) {
        this.check_point = check_point;
    }

    public String getCheck_point_cn() {
        return check_point_cn;
    }

    public void setCheck_point_cn(String check_point_cn) {
        this.check_point_cn = check_point_cn;
    }
}
