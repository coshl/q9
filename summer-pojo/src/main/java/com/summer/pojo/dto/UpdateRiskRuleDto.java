package com.summer.pojo.dto;

import javax.validation.constraints.NotNull;

public class UpdateRiskRuleDto {

    @NotNull(message = "风控规则的状态不能为空")
    private Integer status;

    @NotNull
    public Integer getStatus() {
        return status;
    }

    public void setStatus(@NotNull Integer status) {
        this.status = status;
    }
}
