/* https://github.com/12641561 */
package com.summer.pojo.vo;

import java.util.Date;

public class UserMoXieDataVo {

    public Integer pid;
    /**
     * 自增ID
     */
    public Integer id;

    /**
     * 借款用户ID  默认：0
     */
    public Integer userId;

    /**
     * 手机号码
     */
    public String phone;

    /**
     * 魔蝎平台返回的唯一标识
     */
    public String taskId;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    public Date createTime;

    /**
     * -4 用户输入出错（密码等输错且未继续输入）-3 魔蝎数据服务异常 -2 平台方服务问题（如中国移动维护等）-1 默认状态（用于没有进行操作退出）0 认证失败，异常错误 1 任务进行成功
     * 2 任务进行中  默认：0
     */
    public Byte status;

    public Integer mxAuthStatus;
    /**
     * 修改时间  默认：CURRENT_TIMESTAMP
     */
    public Date updateTime;

    /**
     * 魔蝎成功采集后返回的原始数据
     */
    private byte[] mxRaw;

    /**
     * 魔蝎成功采集后返回的报告数据
     */
    private byte[] mxReport;
    private byte[] msgShow;

    public UserMoXieDataVo() {
    }

    public UserMoXieDataVo(Integer id, Integer userId, String phone, String taskId, Date createTime, Byte status,
                           Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.phone = phone;
        this.taskId = taskId;
        this.createTime = createTime;
        this.status = status;
        this.updateTime = updateTime;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 获取 自增ID user_moxie_data.id
     *
     * @return 自增ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 自增ID user_moxie_data.id
     *
     * @param id 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 借款用户ID user_moxie_data.user_id
     *
     * @return 借款用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置 借款用户ID user_moxie_data.user_id
     *
     * @param userId 借款用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取 手机号码 user_moxie_data.phone
     *
     * @return 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置 手机号码 user_moxie_data.phone
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取 魔蝎平台返回的唯一标识 user_moxie_data.task_id
     *
     * @return 魔蝎平台返回的唯一标识
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 设置 魔蝎平台返回的唯一标识 user_moxie_data.task_id
     *
     * @param taskId 魔蝎平台返回的唯一标识
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * 获取 创建时间 user_moxie_data.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 user_moxie_data.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 -4 用户输入出错（密码等输错且未继续输入）-3 魔蝎数据服务异常 -2 平台方服务问题（如中国移动维护等）-1 默认状态（用于没有进行操作退出）0 认证失败，异常错误 1 任务进行成功
     * 2 任务进行中 user_moxie_data.status
     *
     * @return -4 用户输入出错（密码等输错且未继续输入）-3 魔蝎数据服务异常 -2 平台方服务问题（如中国移动维护等）-1 默认状态（用于没有进行操作退出）0 认证失败，异常错误 1 任务进行成功
     * 2 任务进行中
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置 -4 用户输入出错（密码等输错且未继续输入）-3 魔蝎数据服务异常 -2 平台方服务问题（如中国移动维护等）-1 默认状态（用于没有进行操作退出）0 认证失败，异常错误 1 任务进行成功
     * 2 任务进行中 user_moxie_data.status
     *
     * @param status -4 用户输入出错（密码等输错且未继续输入）-3 魔蝎数据服务异常 -2 平台方服务问题（如中国移动维护等）-1 默认状态（用于没有进行操作退出）0 认证失败，异常错误 1 任务进行成功
     *               2 任务进行中
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取 修改时间 user_moxie_data.update_time
     *
     * @return 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置 修改时间 user_moxie_data.update_time
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMxAuthStatus() {
        return mxAuthStatus;
    }

    public void setMxAuthStatus(Integer mxAuthStatus) {
        this.mxAuthStatus = mxAuthStatus;
    }

    public byte[] getMsgShow() {
        return msgShow;
    }

    public void setMsgShow(byte[] msgShow) {
        this.msgShow = msgShow;
    }

    /**
     * 获取 魔蝎成功采集后返回的原始数据 user_moxie_data.mx_raw
     *
     * @return 魔蝎成功采集后返回的原始数据
     */
    public byte[] getMxRaw() {
        return mxRaw;
    }

    /**
     * 设置 魔蝎成功采集后返回的原始数据 user_moxie_data.mx_raw
     *
     * @param mxRaw 魔蝎成功采集后返回的原始数据
     */
    public void setMxRaw(byte[] mxRaw) {
        this.mxRaw = mxRaw;
    }

    /**
     * 获取 魔蝎成功采集后返回的报告数据 user_moxie_data.mx_report
     *
     * @return 魔蝎成功采集后返回的报告数据
     */
    public byte[] getMxReport() {
        return mxReport;
    }

    /**
     * 设置 魔蝎成功采集后返回的报告数据 user_moxie_data.mx_report
     *
     * @param mxReport 魔蝎成功采集后返回的报告数据
     */
    public void setMxReport(byte[] mxReport) {
        this.mxReport = mxReport;
    }
}