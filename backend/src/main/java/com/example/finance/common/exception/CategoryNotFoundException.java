package com.example.finance.common.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long id){
        super("未找到 id 为 " + id + " 的分类");
    }
}
