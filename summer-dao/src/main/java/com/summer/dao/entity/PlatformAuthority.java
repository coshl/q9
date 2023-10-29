package com.summer.dao.entity;

import java.util.Date;
import java.util.List;

/**
 * 权限实体类
 */
public class PlatformAuthority {
    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer parentId;

    private Date createDate;

    private Date modifyDate;

    private List<PlatformAuthority> list;//子模块

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public List<PlatformAuthority> getList() {
        return list;
    }

    public void setList(List<PlatformAuthority> list) {
        this.list = list;
    }
}