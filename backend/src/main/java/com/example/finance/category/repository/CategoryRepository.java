package com.example.finance.category.repository;


import com.example.finance.category.entity.Category;
import com.example.finance.category.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * JpaRepository 是 Spring Data JPA 提供的一个现成接口。
 * 它把常用数据库操作都准备好了，我们只要继承它，不用自己写 SQL
 * 其中：
 * Category：要管理的数据类型，也就是对应 categories 表。
 * Long：该表主键 id 的 Java 类型。

 * 继承后，CategoryRepository 自动拥有例如:
 * save(category)        // 新增或更新分类
 * findById(id)          // 按 id 查分类
 * findAll()             // 查询全部分类
 * deleteById(id)        // 按 id 删除分类
 * existsById(id)        // 判断分类是否存在
 *
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllByOrderBySortOrderAscIdAsc();

    List<Category> findByTypeOrderBySortOrderAscIdAsc(CategoryType type);

    List<Category> findByActiveOrderBySortOrderAscIdAsc(boolean active);

    List<Category> findByTypeAndActiveOrderBySortOrderAscIdAsc(CategoryType type,boolean active);

    boolean existsByTypeAndName(CategoryType type,String name);

    boolean existsByTypeAndNameAndIdNot(CategoryType type,String name,Long id);
}
