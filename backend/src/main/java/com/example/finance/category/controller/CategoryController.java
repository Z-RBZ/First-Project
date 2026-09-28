package com.example.finance.category.controller;


import com.example.finance.category.dto.CategoryResponse;
import com.example.finance.category.entity.CategoryType;
import com.example.finance.category.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getCategories(
            @RequestParam(required = false)CategoryType type,
            @RequestParam(required = false)Boolean active
            ){
        return categoryService.getCategories(type,active);
    }
}
