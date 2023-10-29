/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class PlatformBanner {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 渠道类型，0表示PC端，1移动端，2表示其他  默认：0
     */
    private Integer equementType;

    /**
     * 频道类型，0表示首页，1表示关于我们，2表示其他  默认：0
     */
    private Integer channelId;

    /**
     * 预留字段  默认：0
     */
    private Integer userLevel;

    /**
     * 发布方式，0表示立即发布，1表示定时发布  默认：0
     */
    private Integer presentWay;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 正常（0）禁用（1）
     */
    private Integer status;

    /**
     * 图片路径
     */
    private String url;
    private String addPerson;

    /**
     * H5页面地址
     */
    private String reurl;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    private Integer type;

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson;
    }

    /**
     * 获取 自增ID platform_banner.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID platform_banner.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 渠道类型，0表示PC端，1移动端，2表示其他 platform_banner.equement_type
     *
     * @return 渠道类型，0表示PC端，1移动端，2表示其他
     */
    public Integer getEquementType() {
        return equementType;
    }

    /**
     * 设置 渠道类型，0表示PC端，1移动端，2表示其他 platform_banner.equement_type
     *
     * @param equementType 渠道类型，0表示PC端，1移动端，2表示其他
     */
    public void setEquementType(Integer equementType) {
        this.equementType = equementType;
    }

    /**
     * 获取 频道类型，0表示首页，1表示关于我们，2表示其他 platform_banner.channel_id
     *
     * @return 频道类型，0表示首页，1表示关于我们，2表示其他
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * 设置 频道类型，0表示首页，1表示关于我们，2表示其他 platform_banner.channel_id
     *
     * @param channelId 频道类型，0表示首页，1表示关于我们，2表示其他
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * 获取 预留字段 platform_banner.user_level
     *
     * @return 预留字段
     */
    public Integer getUserLevel() {
        return userLevel;
    }

    /**
     * 设置 预留字段 platform_banner.user_level
     *
     * @param userLevel 预留字段
     */
    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * 获取 发布方式，0表示立即发布，1表示定时发布 platform_banner.present_way
     *
     * @return 发布方式，0表示立即发布，1表示定时发布
     */
    public Integer getPresentWay() {
        return presentWay;
    }

    /**
     * 设置 发布方式，0表示立即发布，1表示定时发布 platform_banner.present_way
     *
     * @param presentWay 发布方式，0表示立即发布，1表示定时发布
     */
    public void setPresentWay(Integer presentWay) {
        this.presentWay = presentWay;
    }

    /**
     * 获取 活动开始时间 platform_banner.start_time
     *
     * @return 活动开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置 活动开始时间 platform_banner.start_time
     *
     * @param startTime 活动开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取 活动结束时间 platform_banner.end_time
     *
     * @return 活动结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置 活动结束时间 platform_banner.end_time
     *
     * @param endTime 活动结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取 排序 platform_banner.sort
     *
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 排序 platform_banner.sort
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取 正常（0）禁用（1） platform_banner.status
     *
     * @return 正常（0）禁用（1）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 正常（0）禁用（1） platform_banner.status
     *
     * @param status 正常（0）禁用（1）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取 图片路径 platform_banner.url
     *
     * @return 图片路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置 图片路径 platform_banner.url
     *
     * @param url 图片路径
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取 H5页面地址 platform_banner.reurl
     *
     * @return H5页面地址
     */
    public String getReurl() {
        return reurl;
    }

    /**
     * 设置 H5页面地址 platform_banner.reurl
     *
     * @param reurl H5页面地址
     */
    public void setReurl(String reurl) {
        this.reurl = reurl == null ? null : reurl.trim();
    }

    /**
     * 获取 标题 platform_banner.title
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置 标题 platform_banner.title
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取 创建时间 platform_banner.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 platform_banner.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 platform_banner.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 platform_banner.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}