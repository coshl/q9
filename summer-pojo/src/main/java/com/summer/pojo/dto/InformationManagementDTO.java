package com.summer.pojo.dto;

import com.summer.group.AddInformationManagement;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ls
 * @Title:
 * @date 2019/2/20 15:31
 */
public class InformationManagementDTO {
    @NotNull(message = "id不能为空", groups = {AddInformationManagement.class})
    private int id;

    @NotNull(message = "归属类目不能为空", groups = {AddInformationManagement.class})
    private Integer type;

    @NotBlank(message = "标题不能为空", groups = {AddInformationManagement.class})
    private String title;

    @NotNull(message = "请选择排序", groups = {AddInformationManagement.class})
    private int orderBy;

    @NotBlank(message = "内容不能为空", groups = {AddInformationManagement.class})
    private String content;


    private String addPerson;

    private String startTime;

    private String endTime;

    private String pageSize;

    private String pageNum;

    public String getAddPerson() {
        return addPerson;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
