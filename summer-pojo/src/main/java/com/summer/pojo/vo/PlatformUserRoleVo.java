package com.summer.pojo.vo;


import java.util.Date;

/**
 * 角色管理--页面展示字段
 */
public class PlatformUserRoleVo {

    private Integer id;

    private String roleName;//角色名称

    // private String authorityName;//权限名称

    private Integer authorityCount;//权限个数

    private Integer status;//角色状态

    private Date createTime;//创建时间

    private String userSuperName;//添加人

    private String description;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getAuthorityCount() {
        return authorityCount;
    }

    public void setAuthorityCount(Integer authorityCount) {
        this.authorityCount = authorityCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserSuperName() {
        return userSuperName;
    }

    public void setUserSuperName(String userSuperName) {
        this.userSuperName = userSuperName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
