package com.summer.pojo.vo;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @ClassName ： RiskTrialCountVo
 * @Description ：
 * @Author：
 * @Date ：2019/10/15 11:18
 * @Version ：V1.0
 **/
public class RiskTrialCountVo {

    /**
     * 数量
     */
    private Integer hitCount;

    /**
     * 规则的描述
     */
    private String ruleDescript;

    /**
     * 创建时间  默认：1970-01-01 08:00:01
     */
    private String createTime;


    public Integer getHitCount() {
        return hitCount;
    }

    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    public String getRuleDescript() {
        return ruleDescript;
    }

    public void setRuleDescript(String ruleDescript) {
        this.ruleDescript = ruleDescript;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
