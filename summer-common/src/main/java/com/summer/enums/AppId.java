package com.summer.enums;

/**
 * 商户区分标识
 */
public enum AppId {
    JCB(101, "聚财宝", "在用"),
    LMYQ(102, "立马有钱", "在用"),
    AXD(103, "安心袋", "在用"),
    MSQB(104, "免审钱包", "在用"),
    XHZ(105, "小红猪", "在用"),
    SYT(106, "速易通", "在用"),
    ZZX(107, "蜘蛛侠", "在用"),
    JYH(108, "佳业花", "在用"),
    JDX(109, "金大鑫", "在用"),
    XJZ(110, "小桔子", "在用"),
    DRD(117, "达人袋", "在用");

    AppId(Integer value, String desc, String remark) {
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
