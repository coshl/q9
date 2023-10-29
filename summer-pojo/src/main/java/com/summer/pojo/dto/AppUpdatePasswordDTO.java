package com.summer.pojo.dto;

import com.summer.group.SavePassword;
import com.summer.group.SmsVerify;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * APP用户修改密码时的DTO
 */
public class AppUpdatePasswordDTO implements Serializable {

    private static final long serialVersionUID = 3249756405409935935L;
    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空", groups = {SmsVerify.class, SavePassword.class})
    private String phoneNumber;
    /**
     * 验证码，忘记密码时填写
     */
//    @NotBlank(message = "验证码不能为空",groups = {SmsVerify.class})
    private String smsCode;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {SavePassword.class})
    private String password;
    /**
     * 修改密码类型，0：忘记密码，1：APP用户登录进去后直接修改密码
     */
    private Integer passwordType;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(Integer passwordType) {
        this.passwordType = passwordType;
    }
}
