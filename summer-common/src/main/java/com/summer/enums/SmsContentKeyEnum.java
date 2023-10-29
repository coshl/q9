package com.summer.enums;

public enum SmsContentKeyEnum {

    BANK_BIND_SUCC("sms.service.bankBindSucc","绑卡成功短信模板"),
    SMS_SIGN("sms.service.title","短信签名"),
    SMS_SUFFIX("sms.service.suffix","短信后缀"),
    SMS_SENDCONTENT("sms.service.sendContent","短信验证码模板");

    private String value;
    private String desc;

    SmsContentKeyEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
