package com.summer.pojo.vo;

import java.io.Serializable;

/**
 * 系统参数VO
 */
public class BackConfigParamsVo implements Serializable {

    private static final long serialVersionUID = 7988135000151349135L;
    //id
    private Integer id;
    //系统参数Value
    private Integer sysValue;
    private String sysKey;
    private String strValue;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSysValue() {
        return sysValue;
    }

    public void setSysValue(Integer sysValue) {
        this.sysValue = sysValue;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
}
