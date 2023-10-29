package com.summer.pojo.dto;

import com.summer.group.AddBanner;
import com.summer.group.UpdateBanner;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ls
 * @Title:
 * @date 2019/2/20 17:23
 */
public class BannerDTO {
    @NotNull(message = "id不能为空", groups = {UpdateBanner.class})
    private Integer id;


    private Integer type;

    @NotBlank(message = "标题不能为空", groups = {AddBanner.class})
    private String title;


    private Integer orderBy;

    @NotBlank(message = "链接地址不能为空", groups = {AddBanner.class})
    private String reurl;

    @NotNull(message = "状态不能为空", groups = {AddBanner.class})
    private Integer status;

    private Integer pageNum;

    private Integer pageSize;


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }


    public String getReurl() {
        return reurl;
    }

    public void setReurl(String reurl) {
        this.reurl = reurl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
