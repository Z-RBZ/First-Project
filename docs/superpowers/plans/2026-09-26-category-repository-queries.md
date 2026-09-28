# 2026-09-26 下午学习计划：分类 Repository 查询

## 本次目标

在不写 SQL 的前提下，为分类模块补充查询和重复名称检查能力。完成后，后续的 `CategoryService` 能通过 Repository 从 MySQL 查询分类、判断同类型名称是否已经存在。

预计用时：60～75 分钟。

## 本次范围

只修改一个文件：

`backend/src/main/java/com/example/finance/category/repository/CategoryRepository.java`

`CategoryRepository` 是数据库访问层：它负责描述“要从数据库查什么”，但不负责决定“是否允许创建分类”等业务规则。业务规则会在下一阶段放进 `CategoryService`。

本次不创建 Service、Controller、Postman 请求，也不修改 DTO、Entity 或数据库表。

## 需要先理解的规则

Spring Data JPA 能读取 Repository 的方法名，自动生成查询语句。例如：

- `findByType...`：按分类类型查询。
- `existsByTypeAndName...`：判断是否存在同类型、同名称的分类。
- `OrderBySortOrderAscIdAsc`：先按排序值升序，再按 id 升序，保证列表顺序稳定。
- `IdNot`：排除当前 id，专门用于“修改分类时检查是否与其他分类重名”。

这些方法只是声明，不需要写方法体，也不需要手写 SQL。

## 下午任务

### 任务 1：准备查询方法需要的导入

**为什么现在做：** 后面的查询方法会返回多条 `Category`，也会按 `CategoryType` 筛选；先准备类型，方法声明才看得懂。

**要处理的内容：** 在 `CategoryRepository.java` 中导入 `java.util.List` 和 `CategoryType`。

**完成标准：** 编辑器不再提示 `List` 或 `CategoryType` 找不到。

### 任务 2：声明四个分类查询方法

**为什么现在做：** 分类列表将来可以有四种查询场景：不筛选、只筛类型、只筛启用状态、同时筛类型和状态。

**要声明的方法：**

```java
List<Category> findAllByOrderBySortOrderAscIdAsc();

List<Category> findByTypeOrderBySortOrderAscIdAsc(CategoryType type);

List<Category> findByActiveOrderBySortOrderAscIdAsc(boolean active);

List<Category> findByTypeAndActiveOrderBySortOrderAscIdAsc(
        CategoryType type,
        boolean active
);
```

**完成标准：** 四个方法都在接口中、没有方法体，并且能够说明每个方法的筛选条件和排序方式。
sgsdafgfewe

### 任务 3：声明两个重复名称检查方法

**为什么现在做：** 创建分类和修改分类都要避免同一种类型下出现两个同名分类；收入“餐饮”和支出“餐饮”则允许共存。

**要声明的方法：**

```java
boolean existsByTypeAndName(CategoryType type, String name);

boolean existsByTypeAndNameAndIdNot(CategoryType type, String name, Long id);
```

**两个方法的分工：**

- 第一个方法给“创建分类”使用：只要发现同类型、同名称的分类，就说明重复。
- 第二个方法给“修改分类”使用：查询时排除正在修改的那一条分类，避免它把自己误判为重复。

**完成标准：** 两个方法返回 `boolean`；第二个方法包含 `IdNot` 和当前分类的 `Long id`。

### 任务 4：启动并验证方法名

**为什么最后验证：** Spring Boot 启动时会解析 Repository 的方法名。拼写不符合 Spring Data JPA 规则时，通常会在此时报告错误。

**验证步骤：**

1. 在 IDEA 中启动 `BackendApplication`。
2. 确认控制台出现 `Started BackendApplication`，且没有 `QueryCreationException`。
3. 在 `backend` 目录运行 `mvnw.cmd test`。

**完成标准：** Maven 输出 `BUILD SUCCESS`，且测试统计为 0 个失败、0 个错误。

## 完成后你应掌握什么

1. Repository 接口中的方法可以没有方法体。
2. Spring Data JPA 会根据规范的方法名生成 SQL。
3. `findBy...` 用于取数据，`existsBy...` 用于得到“是否存在”的真假结果。
4. Repository 只访问数据库；“重复时返回什么错误”等规则留给下一阶段的 Service。

## 下午的第一步

打开 `CategoryRepository.java`，先只添加 `List` 和 `CategoryType` 的 import。完成后再开始写第一个查询方法。
