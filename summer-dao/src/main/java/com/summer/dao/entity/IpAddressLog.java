/* https://github.com/12641561 */
package com.summer.dao.entity;

import java.util.Date;

public class IpAddressLog {
    /**
     * id
     */
    private Long id;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 访问连接
     */
    private String url;

    /**
     * 渠道编码名称
     */
    private String channelCode;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 获取 id ip_address_log.id
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id ip_address_log.id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 访问ip ip_address_log.ip
     *
     * @return 访问ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置 访问ip ip_address_log.ip
     *
     * @param ip 访问ip
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 获取 访问连接 ip_address_log.url
     *
     * @return 访问连接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置 访问连接 ip_address_log.url
     *
     * @param url 访问连接
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取 渠道编码名称 ip_address_log.channel_code
     *
     * @return 渠道编码名称
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * 设置 渠道编码名称 ip_address_log.channel_code
     *
     * @param channelCode 渠道编码名称
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    /**
     * 获取 创建时间 ip_address_log.create_time
     *
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 ip_address_log.create_time
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}