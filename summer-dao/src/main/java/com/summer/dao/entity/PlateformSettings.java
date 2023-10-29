package com.summer.dao.entity;


import lombok.Data;

import java.util.Date;

/**
 * 第三方配置实体类
 */
@Data
public class PlateformSettings {

    private Integer id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数key值
     */
    private String key;

    /**
     * 参数value值
     */
    private String value;

    /**
     * 是否有效 0失效 1有效  默认：1
     */
    private Byte status;

    /**
     * 描述
     */
    private String description;

    /**
     * 功能组 功能名称用驼峰命名 例：公信宝 GongXinBao
     */
    private String type;

    /**
     * 创建时间  默认：CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * 修改时间  默认：CURRENT_TIMESTAMP
     */
    private Date updateTime;

}