/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class PhoneDeviceInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 唯一订单号，保证和提交风控请求的订单号一致
     */
    private String orderid;

    /**
     * 柏特提供唯一商户号
     */
    private String merchcode;

    /**
     * 系统时间，yyyy-mm-dd hh:mm:ss
     */
    private Date trantime;

    /**
     * (当先系统时间戳，也就是sign中使用的时间戳
     */
    private Date timestamp;

    /**
     * 获取设备信息的节点，1：注册 2：登录 3：贷款申请
     */
    private Integer stepid;

    /**
     * 签名串：MD5(username/merchCode + "-" + timestamp + "-" + userKey + "-" + orderId)
     */
    private String sign;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 手机设备信息
     */

    private String phoneDeviceDataStr;

    public String getPhoneDeviceDataStr() {
        return phoneDeviceDataStr;
    }

    public void setPhoneDeviceDataStr(String phoneDeviceDataStr) {
        this.phoneDeviceDataStr = phoneDeviceDataStr;
    }

    /**
     * 获取 自增ID phone_device_info.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID phone_device_info.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 用户ID phone_device_info.user_id
     *
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 用户ID phone_device_info.user_id
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 手机号 phone_device_info.phone
     *
     * @return 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号 phone_device_info.phone
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 唯一订单号，保证和提交风控请求的订单号一致 phone_device_info.orderId
     *
     * @return 唯一订单号，保证和提交风控请求的订单号一致
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置 唯一订单号，保证和提交风控请求的订单号一致 phone_device_info.orderId
     *
     * @param orderid 唯一订单号，保证和提交风控请求的订单号一致
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    /**
     * 获取 柏特提供唯一商户号 phone_device_info.merchCode
     *
     * @return 柏特提供唯一商户号
     */
    public String getMerchcode() {
        return merchcode;
    }

    /**
     * 设置 柏特提供唯一商户号 phone_device_info.merchCode
     *
     * @param merchcode 柏特提供唯一商户号
     */
    public void setMerchcode(String merchcode) {
        this.merchcode = merchcode == null ? null : merchcode.trim();
    }

    /**
     * 获取 系统时间，yyyy-mm-dd hh:mm:ss phone_device_info.tranTime
     *
     * @return 系统时间，yyyy-mm-dd hh:mm:ss
     */
    public Date getTrantime() {
        return trantime;
    }

    /**
     * 设置 系统时间，yyyy-mm-dd hh:mm:ss phone_device_info.tranTime
     *
     * @param trantime 系统时间，yyyy-mm-dd hh:mm:ss
     */
    public void setTrantime(Date trantime) {
        this.trantime = trantime;
    }

    /**
     * 获取 (当先系统时间戳，也就是sign中使用的时间戳 phone_device_info.timestamp
     *
     * @return (当先系统时间戳 ， 也就是sign中使用的时间戳
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * 设置 (当先系统时间戳，也就是sign中使用的时间戳 phone_device_info.timestamp
     *
     * @param timestamp (当先系统时间戳，也就是sign中使用的时间戳
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取 获取设备信息的节点，1：注册 2：登录 3：贷款申请 phone_device_info.stepId
     *
     * @return 获取设备信息的节点，1：注册 2：登录 3：贷款申请
     */
    public Integer getStepid() {
        return stepid;
    }

    /**
     * 设置 获取设备信息的节点，1：注册 2：登录 3：贷款申请 phone_device_info.stepId
     *
     * @param stepid 获取设备信息的节点，1：注册 2：登录 3：贷款申请
     */
    public void setStepid(Integer stepid) {
        this.stepid = stepid;
    }

    /**
     * 获取 签名串：MD5(username/merchCode + "-" + timestamp + "-" + userKey + "-" + orderId) phone_device_info.sign
     *
     * @return 签名串：MD5(username/merchCode + "-" + timestamp + "-" + userKey + "-" + orderId)
     */
    public String getSign() {
        return sign;
    }

    /**
     * 设置 签名串：MD5(username/merchCode + "-" + timestamp + "-" + userKey + "-" + orderId) phone_device_info.sign
     *
     * @param sign 签名串：MD5(username/merchCode + "-" + timestamp + "-" + userKey + "-" + orderId)
     */
    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    /**
     * 获取 创建时间 phone_device_info.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 phone_device_info.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 更新时间 phone_device_info.update_time
     *
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 更新时间 phone_device_info.update_time
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}