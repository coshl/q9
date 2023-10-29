package com.summer.pojo.dto;

import javax.validation.constraints.NotBlank;

public class DeleteIncreaseMoneyDto {
    @NotBlank(message = "删除提额id不能为空")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
