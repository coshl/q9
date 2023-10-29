package com.summer.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PhoneCodeSend {


    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 手机验证码
     */
    private String content;

    /**
     * 手机验证码
     */
    private String code;

    /**
     * 创建时间
     */
    private String createTime;
}
