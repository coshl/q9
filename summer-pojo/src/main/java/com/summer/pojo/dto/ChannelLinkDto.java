package com.summer.pojo.dto;


import javax.validation.constraints.NotBlank;

/**
 * 渠道连接DTo
 */
public class ChannelLinkDto {
    @NotBlank(message = "id不能为空")
    private Integer id;
    @NotBlank(message = "投放连接")
    private String dropLink;
    @NotBlank(message = "后台链接不能为空")
    private String backstageLink;
    @NotBlank(message = "账户不能为空")
    private String account;
    @NotBlank(message = "密码不能为空")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
