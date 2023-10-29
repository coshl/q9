/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class UserBlackList {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 手机号吗
     */
    private String phone;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 用户注册时间
     */
    private Date userCreateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态（暂未使用）  默认：0
     */
    private Integer status;
    //备注
    private String remark;
    //逾期天数
    private Integer overdueDay;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(Integer overdueDay) {
        this.overdueDay = overdueDay;
    }

    /**
     * 获取 主键id user_black_list.id
     *
     * @return 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键id user_black_list.id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 手机号吗 user_black_list.phone
     *
     * @return 手机号吗
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号吗 user_black_list.phone
     *
     * @param phone 手机号吗
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 用户姓名 user_black_list.user_name
     *
     * @return 用户姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置 用户姓名 user_black_list.user_name
     *
     * @param userName 用户姓名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取 身份证号码 user_black_list.id_card
     *
     * @return 身份证号码
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置 身份证号码 user_black_list.id_card
     *
     * @param idCard 身份证号码
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    /**
     * 获取 用户注册手机号 user_black_list.user_create_time
     *
     * @return 用户注册手机号
     */
    public Date getUserCreateTime() {
        return userCreateTime;
    }

    /**
     * 设置 用户注册手机号 user_black_list.user_create_time
     *
     * @param userCreateTime 用户注册手机号
     */
    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    /**
     * 获取 创建时间 user_black_list.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 user_black_list.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 user_black_list.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 user_black_list.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取 状态（暂未使用） user_black_list.status
     *
     * @return 状态（暂未使用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置 状态（暂未使用） user_black_list.status
     *
     * @param status 状态（暂未使用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}