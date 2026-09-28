package com.example.finance.common.exception;

public class DuplicateCategoryException extends RuntimeException{
    public DuplicateCategoryException(String name){
        super("同类型下已存在分类："+name);
    }
}
