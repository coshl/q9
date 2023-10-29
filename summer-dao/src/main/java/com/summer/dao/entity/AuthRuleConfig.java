/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class AuthRuleConfig {
    /**
     * 认证规则主键
     */
    private Integer id;

    /**
     * 认证规则的key
     */
    private String authRuleKey;

    /**
     * 认证规则的描述（对应认前端的认证名称）
     */
    private String authRuleDescript;

    /**
     * 认证类型（0表示必要认证，1表示补充认证）  默认：0
     */
    private Byte authRuleType;

    /**
     * 添加的人
     */
    private String addUser;

    /**
     * 添加的时间  默认：1970-01-01 08:00:01
     */
    private Date createTime;

    /**
     * 更新时间  默认：1970-01-01 08:00:01
     */
    private Date updateTime;

    /**
     * 规则的状态（0表示启用，1表示关闭）
     */
    private Byte status;

    /**
     * 获取 认证规则主键 auth_rule_config.id
     *
     * @return 认证规则主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 认证规则主键 auth_rule_config.id
     *
     * @param id 认证规则主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 认证规则的key auth_rule_config.auth_rule_key
     *
     * @return 认证规则的key
     */
    public String getAuthRuleKey() {
        return authRuleKey;
    }

    /**
     * 设置 认证规则的key auth_rule_config.auth_rule_key
     *
     * @param authRuleKey 认证规则的key
     */
    public void setAuthRuleKey(String authRuleKey) {
        this.authRuleKey = authRuleKey == null ? null : authRuleKey.trim();
    }

    /**
     * 获取 认证规则的描述（对应认前端的认证名称） auth_rule_config.auth_rule_descript
     *
     * @return 认证规则的描述（对应认前端的认证名称）
     */
    public String getAuthRuleDescript() {
        return authRuleDescript;
    }

    /**
     * 设置 认证规则的描述（对应认前端的认证名称） auth_rule_config.auth_rule_descript
     *
     * @param authRuleDescript 认证规则的描述（对应认前端的认证名称）
     */
    public void setAuthRuleDescript(String authRuleDescript) {
        this.authRuleDescript = authRuleDescript == null ? null : authRuleDescript.trim();
    }

    /**
     * 获取 认证类型（0表示必要认证，1表示补充认证） auth_rule_config.auth_rule_type
     *
     * @return 认证类型（0表示必要认证，1表示补充认证）
     */
    public Byte getAuthRuleType() {
        return authRuleType;
    }

    /**
     * 设置 认证类型（0表示必要认证，1表示补充认证） auth_rule_config.auth_rule_type
     *
     * @param authRuleType 认证类型（0表示必要认证，1表示补充认证）
     */
    public void setAuthRuleType(Byte authRuleType) {
        this.authRuleType = authRuleType;
    }

    /**
     * 获取 添加的人 auth_rule_config.add_user
     *
     * @return 添加的人
     */
    public String getAddUser() {
        return addUser;
    }

    /**
     * 设置 添加的人 auth_rule_config.add_user
     *
     * @param addUser 添加的人
     */
    public void setAddUser(String addUser) {
        this.addUser = addUser == null ? null : addUser.trim();
    }

    /**
     * 获取 添加的时间 auth_rule_config.create_time
     *
     * @return 添加的时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 添加的时间 auth_rule_config.create_time
     *
     * @param createTime 添加的时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 auth_rule_config.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 auth_rule_config.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 规则的状态（0表示启用，1表示关闭） auth_rule_config.status
     *
     * @return 规则的状态（0表示启用，1表示关闭）
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 规则的状态（0表示启用，1表示关闭） auth_rule_config.status
     *
     * @param status 规则的状态（0表示启用，1表示关闭）
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}