package com.summer.dao.entity.moxie;

import java.io.Serializable;

/**
 * 用户行为分析：关于电话充值
 */
public class CellBean implements Serializable {
    private static final long serialVersionUID = 3929378538924123744L;
    private Object behavior;
    private String phone_num;

    public Object getBehavior() {
        return behavior;
    }

    public void setBehavior(Object behavior) {
        this.behavior = behavior;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
