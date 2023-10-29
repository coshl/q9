/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class InformationManagement {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 添加人员
     */
    private String addPerson;

    /**
     * 类型
     */
    private int type;

    /**
     * 修改人员
     */
    private String modifyPerson;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 排序
     */
    private int orderBy;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 更新时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 获取 自增ID platform_information_management.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID platform_information_management.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 标题 platform_information_management.title
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置 标题 platform_information_management.title
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取 添加人员 platform_information_management.add_person
     *
     * @return 添加人员
     */
    public String getAddPerson() {
        return addPerson;
    }

    /**
     * 设置 添加人员 platform_information_management.add_person
     *
     * @param addPerson 添加人员
     */
    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson == null ? null : addPerson.trim();
    }

    /**
     * 获取 类型 platform_information_management.type
     *
     * @return 类型
     */
    public int getType() {
        return type;
    }

    /**
     * 设置 类型 platform_information_management.type
     *
     * @param type 类型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取 修改人员 platform_information_management.modify_person
     *
     * @return 修改人员
     */
    public String getModifyPerson() {
        return modifyPerson;
    }

    /**
     * 设置 修改人员 platform_information_management.modify_person
     *
     * @param modifyPerson 修改人员
     */
    public void setModifyPerson(String modifyPerson) {
        this.modifyPerson = modifyPerson == null ? null : modifyPerson.trim();
    }

    /**
     * 获取 浏览量 platform_information_management.views
     *
     * @return 浏览量
     */
    public Integer getViews() {
        return views;
    }

    /**
     * 设置 浏览量 platform_information_management.views
     *
     * @param views 浏览量
     */
    public void setViews(Integer views) {
        this.views = views;
    }

    /**
     * 获取 排序 platform_information_management.order_by
     *
     * @return 排序
     */
    public int getOrderBy() {
        return orderBy;
    }

    /**
     * 设置 排序 platform_information_management.order_by
     *
     * @param orderBy 排序
     */
    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 获取 创建时间 platform_information_management.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 platform_information_management.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 platform_information_management.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 platform_information_management.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 内容 platform_information_management.content
     *
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置 内容 platform_information_management.content
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}