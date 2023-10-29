/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class UserAfuData {
    /**
     */
    public Integer id;

    /**
     * 平台标识
     */
    public Long pid;

    /**
     * 用户手机号
     */
    public Long uid;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    public Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    public Date updateTime;

    /**
     * 获取 user_afu_data.id
     *
     * @return user_afu_data.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 user_afu_data.id
     *
     * @param id user_afu_data.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 平台标识 user_afu_data.pid
     *
     * @return 平台标识
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 设置 平台标识 user_afu_data.pid
     *
     * @param pid 平台标识
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 获取 用户手机号 user_afu_data.uid
     *
     * @return 用户手机号
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置 用户手机号 user_afu_data.uid
     *
     * @param uid 用户手机号
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * 获取 创建时间 user_afu_data.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 user_afu_data.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 user_afu_data.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 user_afu_data.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}