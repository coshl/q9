/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class RiskRuleConfig {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 规则名称
     */
    private String ruleKey;

    /**
     * 条件值(JSON字符串)
     */
    private String ruleValue;

    /**
     * 风控决策（0拒绝，1通过）  默认：0
     */
    private Byte riskDecision;

    /**
     * 规则描述
     */
    private String ruleDescript;

    /**
     * （0表示单数值型，1表示数值范围型，2表示对比字符串型）
     */
    private Byte ruleType;

    /**
     * 创建时间  默认：1970-01-01 08:00:01
     */
    private Date createTime;

    /**
     * 修改时间  默认：1970-01-01 08:00:01
     */
    private Date updateTime;

    /**
     * 0启用 ，1关闭  默认：0
     */
    private Byte status;

    /**
     * 获取 主键 risk_rule_config.id
     *
     * @return 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 risk_rule_config.id
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 规则名称 risk_rule_config.rule_key
     *
     * @return 规则名称
     */
    public String getRuleKey() {
        return ruleKey;
    }

    /**
     * 设置 规则名称 risk_rule_config.rule_key
     *
     * @param ruleKey 规则名称
     */
    public void setRuleKey(String ruleKey) {
        this.ruleKey = ruleKey == null ? null : ruleKey.trim();
    }

    /**
     * 获取 条件值(JSON字符串) risk_rule_config.rule_value
     *
     * @return 条件值(JSON字符串)
     */
    public String getRuleValue() {
        return ruleValue;
    }

    /**
     * 设置 条件值(JSON字符串) risk_rule_config.rule_value
     *
     * @param ruleValue 条件值(JSON字符串)
     */
    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue == null ? null : ruleValue.trim();
    }

    /**
     * 获取 风控决策（0拒绝，1通过） risk_rule_config.risk_decision
     *
     * @return 风控决策（0拒绝，1通过）
     */
    public Byte getRiskDecision() {
        return riskDecision;
    }

    /**
     * 设置 风控决策（0拒绝，1通过） risk_rule_config.risk_decision
     *
     * @param riskDecision 风控决策（0拒绝，1通过）
     */
    public void setRiskDecision(Byte riskDecision) {
        this.riskDecision = riskDecision;
    }

    /**
     * 获取 规则描述 risk_rule_config.rule_descript
     *
     * @return 规则描述
     */
    public String getRuleDescript() {
        return ruleDescript;
    }

    /**
     * 设置 规则描述 risk_rule_config.rule_descript
     *
     * @param ruleDescript 规则描述
     */
    public void setRuleDescript(String ruleDescript) {
        this.ruleDescript = ruleDescript == null ? null : ruleDescript.trim();
    }

    /**
     * 获取 （0表示单数值型，1表示数值范围型，2表示对比字符串型） risk_rule_config.rule_type
     *
     * @return （0表示单数值型，1表示数值范围型，2表示对比字符串型）
     */
    public Byte getRuleType() {
        return ruleType;
    }

    /**
     * 设置 （0表示单数值型，1表示数值范围型，2表示对比字符串型） risk_rule_config.rule_type
     *
     * @param ruleType （0表示单数值型，1表示数值范围型，2表示对比字符串型）
     */
    public void setRuleType(Byte ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * 获取 创建时间 risk_rule_config.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 risk_rule_config.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改时间 risk_rule_config.update_time
     *
     * @return 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 risk_rule_config.update_time
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 0启用 ，1关闭 risk_rule_config.status
     *
     * @return 0启用 ，1关闭
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 0启用 ，1关闭 risk_rule_config.status
     *
     * @param status 0启用 ，1关闭
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}