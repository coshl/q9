/* https://github.com/12641561 */
package com.summer.dao.entity;

import com.summer.util.encrypt.AESDecrypt;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class UserContactsUpdate implements Serializable {
    /**
     * 序列号
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户的姓名/电话
     */
    private String userName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 手机号码
     */
    private String contactPhone;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 获取 序列号 user_contacts.id
     *
     * @return 序列号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 序列号 user_contacts.id
     *
     * @param id 序列号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 用户id user_contacts.user_id
     *
     * @return 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户id user_contacts.user_id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 用户的姓名/电话 user_contacts.user_name
     *
     * @return 用户的姓名/电话
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置 用户的姓名/电话 user_contacts.user_name
     *
     * @param userName 用户的姓名/电话
     */
    public void setUserName(String userName) {

        this.userName = userName == null ? null : userName.trim();

    }

    /**
     * 获取 联系人姓名 user_contacts.contact_name
     *
     * @return 联系人姓名
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置 联系人姓名 user_contacts.contact_name
     *
     * @param contactName 联系人姓名
     */
    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();

    }

    /**
     * 获取 手机号码 user_contacts.contact_phone
     *
     * @return 手机号码
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置 手机号码 user_contacts.contact_phone
     *
     * @param contactPhone 手机号码
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();

    }

    /**
     * 获取 添加时间 user_contacts.create_time
     *
     * @return 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 添加时间 user_contacts.create_time
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}