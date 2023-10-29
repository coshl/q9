package com.summer.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SourceH5 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private String code;

    /**
     * h5域名
     */
    private String domainName;

    /**
     * logo
     */
    private String logo;

    /**
     * 首页背景图片
     */
    private String backgroundUrl;

    /**
     * 微信公众号二维码
     */
    private String wechatQrcodeUrl;

    /**
     * banner1
     */
    private String banner1;

    /**
     * banner2
     */
    private String banner2;

    /**
     * banner3
     */
    private String banner3;

    /**
     * 微信公众号描述
     */
    private String wechatDescribe;

    /**
     * 登录页
     */
    private String login;

    /**
     * 开卡页
     */
    private String card;

    /**
     * 产品名
     */
    private String product;

    /**
     * 公众号名称
     */
    private String wechat;

    /**
     * 公众号登录页面
     */
    private String wechatLogin;

    /**
     * 公众号名称
     */
    private String appId;

    /**
     * 公众号名称
     */
    private String appSecret;

    /**
     * 创建时间
     */
    private Date createTime;

}
