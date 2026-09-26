package com.example.finance.category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategoryRequest {
    @NotBlank
    @Size(max = 50)
    private String name;

    @Min(0)
    private int sortOrder;

    public UpdateCategoryRequest() {
    }

    public UpdateCategoryRequest(String name, int sortOrder) {
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public String getName() {
        return name;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
