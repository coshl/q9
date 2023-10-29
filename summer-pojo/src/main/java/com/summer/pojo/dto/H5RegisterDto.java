package com.summer.pojo.dto;

import com.summer.util.Constant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * H5落地页注册
 */
public class H5RegisterDto {
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = Constant.PHONE_REGULAR, message = Constant.PHONE_IS_ILLEGAL)
    private String phoneNumber;
    @NotBlank(message = "渠道编码不能为空")
    private String channelCode;
    @NotBlank(message = "验证码不能为空")
    private String smsCode;
    // 客户端类型：0表示未知，1表示安卓，2表示IOS
    private Integer clientType;
    // 用户APP登录密码
    private String password;
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull
    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(@NotNull String channelCode) {
        this.channelCode = channelCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
