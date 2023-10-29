package com.summer.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 运营商行为日志记录表
 * </p>
 *
 * @author jobob
 * @since 2020-09-13
 */
@TableName(value = "user_mobile_data_record")
public class UserMobileDataRecord implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 用户手机号码
     */
    private String mobile;

    /**
     * 运营商报告表user_moxie_data.id
     */
    private Long mxId;

    private String logId;

    /**
     * 当前状态:1-获取h5链接；2-跳到了成功url；3-跳失败url；4-收到了回调；5-拉完了报告
     */
    private Integer status;

    /**
     * h5链接
     */
    private String h5Url;

    /**
     * 回调内容
     */
    private String notifyContent;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Long getMxId() {
        return mxId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setMxId(Long mxId) {
        this.mxId = mxId;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }
    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
