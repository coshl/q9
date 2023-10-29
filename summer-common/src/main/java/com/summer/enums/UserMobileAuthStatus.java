package com.summer.enums;

/**
 * 商户区分标识
 */
public enum UserMobileAuthStatus {
    INIT(-1, "待认证", "在用"),
    ING(0, "认证中", "在用"),
    SUC(1, "成功", "在用"),
    FAIL(2, "失败", "在用");

    UserMobileAuthStatus(Integer value, String desc, String remark) {
        this.value = value;
        this.desc = desc;
        this.remark = remark;
    }


    private Integer value;
    private String desc;
    private String remark;


    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
