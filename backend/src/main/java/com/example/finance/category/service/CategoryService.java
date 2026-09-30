package com.example.finance.category.service;

import com.example.finance.category.dto.CategoryResponse;
import com.example.finance.category.dto.CreateCategoryRequest;
import com.example.finance.category.entity.Category;
import com.example.finance.category.entity.CategoryType;
import com.example.finance.category.repository.CategoryRepository;
import com.example.finance.common.exception.DuplicateCategoryException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getCategories(
            CategoryType categoryType,
            Boolean active
    ){
        List<Category> categories;
        if (categoryType == null && active == null) {
            categories = categoryRepository.findAllByOrderBySortOrderAscIdAsc();
        } else if (categoryType == null) {
            categories = categoryRepository.findByActiveOrderBySortOrderAscIdAsc(active);
        } else if (active == null) {
            categories = categoryRepository.findByTypeOrderBySortOrderAscIdAsc(categoryType);
        }else {
            categories = categoryRepository.findByTypeAndActiveOrderBySortOrderAscIdAsc(categoryType,active);
        }
        return categories.stream().map(this::toResponse).toList();
    }

    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getSortOrder(),
                category.isActive()
        );
    }

    public CategoryResponse createCategory(CreateCategoryRequest request){
        boolean categoryExist = categoryRepository.existsByTypeAndName(
                request.getType(),
                request.getName()
        );

        if (categoryExist){
            throw new DuplicateCategoryException(request.getName());
        }

        Category category = new Category(
                request.getName(),
                request.getType()
        );

        category.setSortOrder(request.getSortOrder());

        Category saveCategory = categoryRepository.save(category);

        return toResponse(saveCategory);
    }

}
