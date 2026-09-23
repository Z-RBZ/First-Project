package com.example.finance.category.dto;

import com.example.finance.category.entity.CategoryType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 含义：
 * @NotBlank：不能是 null、空字符串或全空格；
 * @Size(max = 50)：名称最多 50 个字符；
 * @NotNull：必须选择收入或支出类型；
 * @Min(0)：排序不能小于 0。
 */
public class CreateCategoryRequest {
    @NotNull(message = "分类名称不能为空")
    @Size(max = 50,message = "分类名称最多 50 个字符")
    private String name;
    @Min(value = 0,message = "排序值不能小于 0")
    private int sortOrder;
    @NotNull(message = "分类类型不能为空")
    private CategoryType type;

    public CreateCategoryRequest() {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}
