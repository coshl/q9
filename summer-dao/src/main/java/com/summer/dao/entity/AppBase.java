/* https://github.com/12641561 */
package com.summer.dao.entity;

public class AppBase {
    /**
     */
    private Integer id;

    /**
     * app名称
     */
    private String name;

    /**
     * app描述
     */
    private String appDesc;

    /**
     * 客服电话
     */
    private String servicePhone;

    /**
     * 客服微信
     */
    private String weixin;

    /**
     * 图片
     */
    private String imgUrl;
    //客服qq
    private String qq;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * 获取 platform_app_base.id
     *
     * @return platform_app_base.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 platform_app_base.id
     *
     * @param id platform_app_base.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 app名称 platform_app_base.name
     *
     * @return app名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 app名称 platform_app_base.name
     *
     * @param name app名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取 app描述 platform_app_base.app_desc
     *
     * @return app描述
     */
    public String getAppDesc() {
        return appDesc;
    }

    /**
     * 设置 app描述 platform_app_base.app_desc
     *
     * @param appDesc app描述
     */
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc == null ? null : appDesc.trim();
    }

    /**
     * 获取 客服电话 platform_app_base.service_phone
     *
     * @return 客服电话
     */
    public String getServicePhone() {
        return servicePhone;
    }

    /**
     * 设置 客服电话 platform_app_base.service_phone
     *
     * @param servicePhone 客服电话
     */
    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone == null ? null : servicePhone.trim();
    }

    /**
     * 获取 图片 platform_app_base.img_url
     *
     * @return 图片
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置 图片 platform_app_base.img_url
     *
     * @param imgUrl 图片
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}