package com.summer.pojo.vo;

import com.summer.pojo.dto.BaseUserInfo;
import com.summer.util.Constant;

/**
 * 账户管理--页面展示字段
 */
public class PlatformUserVo extends BaseUserInfo {

    private Integer id;

    private String userName; //用户名

   // private String phoneNumber;//手机号码

    private String roleName;//角色名称

    private Integer status;//状态

    private String ipAddress;//最后登录ip地址

    private String lastLoginTime;//最后登录时间

    private String userSuperName;//添加人

    private Integer roleId;//角色id
    //催收分组(id)
    private Integer groupLevel;
    //催收分组名称
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

   /* public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }*/

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUserSuperName() {
        return userSuperName;
    }

    public void setUserSuperName(String userSuperName) {
        this.userSuperName = userSuperName;
    }

    public Integer getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(Integer groupLevel) {
        this.groupLevel = groupLevel;
        if (null != groupLevel) {
            if (Constant.XJX_OVERDUE_LEVEL_S1.equals(groupLevel.toString())) {
                this.groupName = Constant.groupNameMap.get(Constant.XJX_OVERDUE_LEVEL_S1);
            } else if (Constant.XJX_OVERDUE_LEVEL_S2.equals(groupLevel.toString())) {
                this.groupName = Constant.groupNameMap.get(Constant.XJX_OVERDUE_LEVEL_S2);
            } else {
                this.groupName = "";
            }
        } else {
            this.groupName = "";
        }
    }
}
