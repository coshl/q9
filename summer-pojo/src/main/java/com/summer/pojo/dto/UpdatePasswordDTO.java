package com.summer.pojo.dto;

import com.summer.group.SavePassword;
import com.summer.group.SmsVerify;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 修改密码时的DTO
 */
public class UpdatePasswordDTO implements Serializable {

    private static final long serialVersionUID = 3249756405409935935L;
    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空", groups = {SmsVerify.class, SavePassword.class})
    private String phoneNumber;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空", groups = {SmsVerify.class})
    private String code;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {SavePassword.class})
    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
