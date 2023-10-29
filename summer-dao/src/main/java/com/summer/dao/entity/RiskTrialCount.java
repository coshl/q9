/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class RiskTrialCount {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 规则的id
     */
    private Integer ruleId;

    /**
     * 数量
     */
    private Integer hitCount;

    /**
     * (0表示命中规则的统计，1表示通过该规则的统计)
     */
    private Byte countType;

    /**
     * 规则的描述
     */
    private String ruleDescript;

    /**
     * 创建时间  默认：1970-01-01 08:00:01
     */
    private Date createTime;

    /**
     * 更新时间  默认：1970-01-01 08:00:01
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 获取 主键 risk_trial_count.id
     *
     * @return 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 risk_trial_count.id
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 规则的id risk_trial_count.rule_id
     *
     * @return 规则的id
     */
    public Integer getRuleId() {
        return ruleId;
    }

    /**
     * 设置 规则的id risk_trial_count.rule_id
     *
     * @param ruleId 规则的id
     */
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取 数量 risk_trial_count.hit_count
     *
     * @return 数量
     */
    public Integer getHitCount() {
        return hitCount;
    }

    /**
     * 设置 数量 risk_trial_count.hit_count
     *
     * @param hitCount 数量
     */
    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }

    /**
     * 获取 (0表示命中规则的统计，1表示通过该规则的统计) risk_trial_count.count_type
     *
     * @return (0表示命中规则的统计 ， 1表示通过该规则的统计)
     */
    public Byte getCountType() {
        return countType;
    }

    /**
     * 设置 (0表示命中规则的统计，1表示通过该规则的统计) risk_trial_count.count_type
     *
     * @param countType (0表示命中规则的统计，1表示通过该规则的统计)
     */
    public void setCountType(Byte countType) {
        this.countType = countType;
    }

    /**
     * 获取 规则的描述 risk_trial_count.rule_descript
     *
     * @return 规则的描述
     */
    public String getRuleDescript() {
        return ruleDescript;
    }

    /**
     * 设置 规则的描述 risk_trial_count.rule_descript
     *
     * @param ruleDescript 规则的描述
     */
    public void setRuleDescript(String ruleDescript) {
        this.ruleDescript = ruleDescript == null ? null : ruleDescript.trim();
    }

    /**
     * 获取 创建时间 risk_trial_count.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 risk_trial_count.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 risk_trial_count.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 risk_trial_count.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 状态 risk_trial_count.status
     *
     * @return 状态
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 状态 risk_trial_count.status
     *
     * @param status 状态
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}