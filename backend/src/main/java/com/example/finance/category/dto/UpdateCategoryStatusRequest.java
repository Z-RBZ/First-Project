package com.example.finance.category.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateCategoryStatusRequest {

    @NotNull(message = "分类状态不能为空")
    private Boolean active;

    public UpdateCategoryStatusRequest() {
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }
}
