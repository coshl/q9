/* https://github.com/12641561 */
package com.summer.dao.entity;

public class ProductRecommend {
    /** 
     * id
     */ 
    private Integer id;

    /** 
     * app名称
     */ 
    private String appName;

    /** 
     * 产品logo地址
     */ 
    private String logoUrl;

    /** 
     * app描述
     */ 
    private String appDescription;

    /** 
     * 下载地址（落地页注册地址）
     */ 
    private String downUrl;

    /** 
     * 备注
     */ 
    private String remark;

    /** 
     * 获取 id product_recommend.id
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /** 
     * 设置 id product_recommend.id
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /** 
     * 获取 app名称 product_recommend.app_name
     * @return app名称
     */
    public String getAppName() {
        return appName;
    }

    /** 
     * 设置 app名称 product_recommend.app_name
     * @param appName app名称
     */
    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    /** 
     * 获取 产品logo地址 product_recommend.logo_url
     * @return 产品logo地址
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /** 
     * 设置 产品logo地址 product_recommend.logo_url
     * @param logoUrl 产品logo地址
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    /** 
     * 获取 app描述 product_recommend.app_descriptoin
     * @return app描述
     */
    public String getAppDescription() {
        return appDescription;
    }

    /** 
     * 设置 app描述 product_recommend.app_descriptoin
     * @param appDescription app描述
     */
    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription == null ? null : appDescription.trim();
    }

    /** 
     * 获取 下载地址（落地页注册地址） product_recommend.down_url
     * @return 下载地址（落地页注册地址）
     */
    public String getDownUrl() {
        return downUrl;
    }

    /** 
     * 设置 下载地址（落地页注册地址） product_recommend.down_url
     * @param downUrl 下载地址（落地页注册地址）
     */
    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl == null ? null : downUrl.trim();
    }

    /** 
     * 获取 备注 product_recommend.remark
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /** 
     * 设置 备注 product_recommend.remark
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}