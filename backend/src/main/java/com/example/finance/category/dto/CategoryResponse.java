package com.example.finance.category.dto;

import com.example.finance.category.entity.CategoryType;

public class CategoryResponse {
    private Long id;

    private String name;

    private CategoryType type;

    private  int sortOrder;

    private  boolean active;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isActive() {
        return active;
    }

    public CategoryResponse(Long id, String name, CategoryType type, int sortOrder, boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sortOrder = sortOrder;
        this.active = active;
    }
}
