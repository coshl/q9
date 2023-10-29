/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class MessagePush {
    /**
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 对象
     */
    private String object;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    /**
     * 添加人员
     */
    private String addPerson;

    /**
     * 推送类型
     */
    private Integer type;

    /**
     * 发送数量
     */
    private Integer sendNums;

    /**
     * pv
     */
    private Integer pv;

    /**
     * uv
     */
    private Integer uv;

    /**
     * 创建日期  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 修改日期  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 获取 platform_message_push.id
     *
     * @return platform_message_push.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 platform_message_push.id
     *
     * @param id platform_message_push.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 标题 platform_message_push.title
     *
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置 标题 platform_message_push.title
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取 添加人员 platform_message_push.add_person
     *
     * @return 添加人员
     */
    public String getAddPerson() {
        return addPerson;
    }

    /**
     * 设置 添加人员 platform_message_push.add_person
     *
     * @param addPerson 添加人员
     */
    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson == null ? null : addPerson.trim();
    }

    /**
     * 获取 推送类型 platform_message_push.type
     *
     * @return 推送类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 推送类型 platform_message_push.type
     *
     * @param type 推送类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取 发送数量 platform_message_push.send_nums
     *
     * @return 发送数量
     */
    public Integer getSendNums() {
        return sendNums;
    }

    /**
     * 设置 发送数量 platform_message_push.send_nums
     *
     * @param sendNums 发送数量
     */
    public void setSendNums(Integer sendNums) {
        this.sendNums = sendNums;
    }

    /**
     * 获取 pv platform_message_push.pv
     *
     * @return pv
     */
    public Integer getPv() {
        return pv;
    }

    /**
     * 设置 pv platform_message_push.pv
     *
     * @param pv pv
     */
    public void setPv(Integer pv) {
        this.pv = pv;
    }

    /**
     * 获取 uv platform_message_push.uv
     *
     * @return uv
     */
    public Integer getUv() {
        return uv;
    }

    /**
     * 设置 uv platform_message_push.uv
     *
     * @param uv uv
     */
    public void setUv(Integer uv) {
        this.uv = uv;
    }

    /**
     * 获取 创建日期 platform_message_push.create_time
     *
     * @return 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建日期 platform_message_push.create_time
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改日期 platform_message_push.update_time
     *
     * @return 修改日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改日期 platform_message_push.update_time
     *
     * @param updateTime 修改日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}