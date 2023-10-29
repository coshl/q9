package com.summer.dao.entity.moxie;

import java.io.Serializable;

/**
 * 魔蝎基本信息校验项
 */
public class BasicCheckItems implements Serializable {
    private static final long serialVersionUID = 6190111168672322058L;
    //校验项
    private String check_item;
    //校验结果
    private String result;
    //校验原则
    private String comment;

    public String getCheck_item() {
        return check_item;
    }

    public void setCheck_item(String check_item) {
        this.check_item = check_item;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "BasicCheckItems{" +
                "check_item='" + check_item + '\'' +
                ", result='" + result + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
