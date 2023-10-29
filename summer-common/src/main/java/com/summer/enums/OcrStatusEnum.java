package com.summer.enums;

public enum OcrStatusEnum {


    INIT_TOKEN(-1, "初始化，获取token未录人脸阶段"),
    WAIT_AUTH(0, "认证中(sdk回调操作成功)"),
    SUCCESS_AUTH(1, "认证成功(拿到报告个人信息)"),
    CANCEL_AUTH(2, "认证作废,即已经有认证成功的了其余作废处理"),
    FAIL_AUTH(3, "认证失败（活体攻击等非本人冒名实名情况）");


    private Integer value;
    private String desc;

    OcrStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
