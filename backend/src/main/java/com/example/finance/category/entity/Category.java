package com.example.finance.category.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
// 表示 id 是数据库主键，对应 SQL 中的 PRIMARY KEY
// 新增分类时，id 由 MySQL 的 AUTO_INCREMENT 自动生成
// 枚举值以文字形式保存到数据库，而不是保存成 0、1 等数字
// 数据库中会保存 INCOME 或 EXPENSE
// 分类类型不能为空，最大长度为 20
// Java 字段名与数据库列名相同时，JPA 会自动对应，无需写 name 属性
// Java 使用 sortOrder，数据库使用 sort_order，因此需要明确指定列名

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(nullable = false,length = 20)
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false,name = "sort_order")
    private int sortOrder = 0;

//    添加构造方法
    protected Category(){

    }

    public Category(String name,CategoryType type){
        this.name = name;
        this.type = type;
    }

    //添加 getter 方法
    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public int getSortOrder() {
        return sortOrder;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
