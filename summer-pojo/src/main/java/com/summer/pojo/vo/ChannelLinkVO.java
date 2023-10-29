package com.summer.pojo.vo;


import com.summer.util.encrypt.AESDecrypt;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ChannelLinkVO implements Serializable {

    private static final long serialVersionUID = -4892091763214753589L;
    private Integer id;
    private String dropLink;
    private String backstageLink;
    private String account;
    private String password;
    private String channelCode;
    private String shortUrl;
    private String shortLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDropLink() {
        return dropLink;
    }

    public void setDropLink(String dropLink) {
        this.dropLink = dropLink;
    }

    public String getBackstageLink() {
        return backstageLink;
    }

    public void setBackstageLink(String backstageLink) {
        this.backstageLink = backstageLink;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }
}
