/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class Protocol {
    /**
     */
    private Integer id;

    /**
     * 姓名
     */
    private String modifyName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 标题(协议名称)
     */
    private String protocolTitle;

    /**
     * 协议类型（）
     */
    private Integer type;

    /**
     * 状态（0正常1禁用）
     */
    private Integer status;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 乙方
     */
    private String partyB;

    /**
     * 丙方
     */
    private String partyC;

    /**
     * 丁方
     */
    private String partyD;

    /**
     * 添加人员
     */
    private String addPerson;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 创建日期  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 修改日期  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

    /**
     * 内容
     */
    private String content;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取 platform_protocol.id
     *
     * @return platform_protocol.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 platform_protocol.id
     *
     * @param id platform_protocol.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取 姓名 platform_protocol.name
     *
     * @return 姓名
     */
    public String getModifyName() {
        return modifyName;
    }

    /**
     * 设置 姓名 platform_protocol.name
     *
     * @param modifyName 姓名
     */
    public void setModifyName(String modifyName) {
        this.modifyName = modifyName == null ? null : modifyName.trim();
    }

    /**
     * 获取 手机号 platform_protocol.phone
     *
     * @return 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号 platform_protocol.phone
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 身份证号 platform_protocol.id_card
     *
     * @return 身份证号
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置 身份证号 platform_protocol.id_card
     *
     * @param idCard 身份证号
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    /**
     * 获取 标题 platform_protocol.title
     *
     * @return 标题
     */
    public String getProtocolTitle() {
        return protocolTitle;
    }

    /**
     * 设置 标题 platform_protocol.title
     *
     * @param protocolTitle 标题
     */
    public void setProtocolTitle(String protocolTitle) {
        this.protocolTitle = protocolTitle == null ? null : protocolTitle.trim();
    }

    /**
     * 获取 协议类型（） platform_protocol.type
     *
     * @return 协议类型（）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置 协议类型（） platform_protocol.type
     *
     * @param type 协议类型（）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取 乙方 platform_protocol.party_b
     *
     * @return 乙方
     */
    public String getPartyB() {
        return partyB;
    }

    /**
     * 设置 乙方 platform_protocol.party_b
     *
     * @param partyB 乙方
     */
    public void setPartyB(String partyB) {
        this.partyB = partyB == null ? null : partyB.trim();
    }

    /**
     * 获取 丙方 platform_protocol.party_c
     *
     * @return 丙方
     */
    public String getPartyC() {
        return partyC;
    }

    /**
     * 设置 丙方 platform_protocol.party_c
     *
     * @param partyC 丙方
     */
    public void setPartyC(String partyC) {
        this.partyC = partyC == null ? null : partyC.trim();
    }

    /**
     * 获取 丁方 platform_protocol.party_d
     *
     * @return 丁方
     */
    public String getPartyD() {
        return partyD;
    }

    /**
     * 设置 丁方 platform_protocol.party_d
     *
     * @param partyD 丁方
     */
    public void setPartyD(String partyD) {
        this.partyD = partyD == null ? null : partyD.trim();
    }

    /**
     * 获取 添加人员 platform_protocol.add_person
     *
     * @return 添加人员
     */
    public String getAddPerson() {
        return addPerson;
    }

    /**
     * 设置 添加人员 platform_protocol.add_person
     *
     * @param addPerson 添加人员
     */
    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson == null ? null : addPerson.trim();
    }

    /**
     * 获取 图片地址 platform_protocol.image_url
     *
     * @return 图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置 图片地址 platform_protocol.image_url
     *
     * @param imageUrl 图片地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    /**
     * 获取 创建日期 platform_protocol.create_time
     *
     * @return 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建日期 platform_protocol.create_time
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 修改日期 platform_protocol.update_time
     *
     * @return 修改日期
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改日期 platform_protocol.update_time
     *
     * @param updateTime 修改日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 内容 platform_protocol.content
     *
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置 内容 platform_protocol.content
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}