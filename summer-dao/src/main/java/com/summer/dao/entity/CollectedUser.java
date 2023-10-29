/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class CollectedUser {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 手机号码
     */
    private String userPhone;

    /**
     * 逾期天数
     */
    private Integer overduedays;

    /**
     * 创建时间
     */
    private Date createTime;

    private Integer status;

    /**
     * 获取 自增ID collected_user.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID collected_user.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 真实姓名 collected_user.realname
     *
     * @return 真实姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置 真实姓名 collected_user.realname
     *
     * @param realname 真实姓名
     */
    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    /**
     * 获取 身份证号码 collected_user.id_number
     *
     * @return 身份证号码
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * 设置 身份证号码 collected_user.id_number
     *
     * @param idNumber 身份证号码
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    /**
     * 获取 手机号码 collected_user.user_phone
     *
     * @return 手机号码
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * 设置 手机号码 collected_user.user_phone
     *
     * @param userPhone 手机号码
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    /**
     * 获取 逾期天数 collected_user.overdueDays
     *
     * @return 逾期天数
     */
    public Integer getOverduedays() {
        return overduedays;
    }

    /**
     * 设置 逾期天数 collected_user.overdueDays
     *
     * @param overduedays 逾期天数
     */
    public void setOverduedays(Integer overduedays) {
        this.overduedays = overduedays;
    }

    /**
     * 获取 创建时间 collected_user.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 collected_user.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}