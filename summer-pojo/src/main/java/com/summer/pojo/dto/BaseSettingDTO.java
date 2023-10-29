package com.summer.pojo.dto;

import com.summer.group.SaveAppBaseSetting;

import javax.validation.constraints.NotBlank;

/**
 * @author ls
 * @Title:
 * @date 2019/2/20 17:44
 */
public class BaseSettingDTO {
    @NotBlank(message = "app名称不能为空", groups = SaveAppBaseSetting.class)
    private String name;

    @NotBlank(message = "描述不能为空", groups = SaveAppBaseSetting.class)
    private String descript;
    /**
     * logo图片地址
     */
    private String logo;

    @NotBlank(message = "客服电话不能为空", groups = SaveAppBaseSetting.class)
    private String servicePhone;

    private Integer id;

    /**
     * 客服微信
     */
    private String weixin;

    /**
     * 客服qq
     */
    private String qq;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }
}
