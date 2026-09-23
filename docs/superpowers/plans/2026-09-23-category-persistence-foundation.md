# 分类数据持久层基础：今晚学习计划

**今晚目标：** 完成“分类”的 Java 数据模型和数据访问层，为后面编写分类业务逻辑与 API 打好基础。

**这阶段做什么：** `Category` 类继续对应已有的 MySQL `categories` 表；`CategoryType` 限制分类只能是收入或支出；`CategoryRepository` 让 Spring Data JPA 自动提供基础的数据库读写能力。

**使用技术：** Java 17、Spring Boot、Spring Data JPA、MySQL、Flyway、Maven、IntelliJ IDEA。

**设计依据：** `docs/design.md`

## 今晚不做的事

- 不修改已经成功执行的 Flyway V1 建表脚本。
- 不创建 Service、Controller、API 接口或 React 页面。
- 不手写 SQL 查询。
- 由你亲自写 Java 代码；我每次只解释并带你完成一个小步骤。

## 本阶段每个文件的作用

| 文件或类 | 现在是否创建 | 作用 |
|---|---|---|
| `CategoryType.java` | 已创建 | 定义分类的两种合法类型：收入 `INCOME` 与支出 `EXPENSE`。 |
| `Category.java` | 已创建，待补全 | 对应数据库的 `categories` 表；一个 `Category` 对象表示表中的一条分类数据。 |
| `CategoryRepository.java` | 今晚创建 | 负责让 Java 代码读取、保存、查询和删除分类数据；基础操作由 Spring Data JPA 自动提供。 |
| `CategoryService.java` | 下次创建 | 放分类业务规则，例如分类名不能重复、分类类型创建后不可修改。 |
| `CategoryController.java` | 后续创建 | 接收浏览器或 Postman 的 HTTP 请求，并调用 Service 返回结果。 |

## 今晚完成后的检查标准

- `Category` 能正确映射 `categories` 表。
- Spring Boot 能正常启动，控制台没有 JPA 表或字段映射错误。
- `CategoryRepository` 已创建，且 Spring Boot 能自动识别它。

## 今晚步骤总览

| 阶段 | 要完成什么 | 如何确认成功 |
|---|---|---|
| 1 | 补全 `Category` 实体类 | 后端能正常启动，没有结构校验错误 |
| 2 | 创建 `CategoryRepository` | 后端仍能正常启动，Repository 被自动扫描 |
| 3 | 回顾字段与数据表对应关系 | 能说明每个 Java 字段对应哪个数据库列 |

---

## 阶段 1：补全 `Category` 实体类

**涉及文件：**

- 修改：`backend/src/main/java/com/example/finance/category/entity/Category.java`
- 参考：`backend/src/main/java/com/example/finance/category/entity/CategoryType.java`

**这个阶段的目的：** 让 Java 能准确理解“分类数据由哪些字段组成”，并知道这些字段与 MySQL `categories` 表如何对应。完成后，Hibernate 才能把数据库查到的一行分类数据转换成一个 `Category` 对象。

### 第 1 步：修正字段类型与默认值（已完成）

```java
private Long id;
private boolean active = true;
```

- `Long` 对应 MySQL 的 `BIGINT`。
- `active = true` 保证新建分类默认启用，避免 Java 默认的 `false` 与设计不一致。

### 第 2 步：添加构造方法（当前进行）

在字段后面添加：

```java
protected Category() {
}

public Category(String name, CategoryType type) {
    this.name = name;
    this.type = type;
}
```

- 无参构造方法供 Hibernate 从数据库读取数据时使用。
- 有参构造方法供后续 Java 代码创建分类时使用。

### 第 3 步：添加 getter 方法

添加以下方法，让其他类能读取分类信息：

```java
public Long getId() {
    return id;
}

public String getName() {
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
```

### 第 4 步：添加允许修改的字段方法

分类名称、启用状态和排序可以修改；分类类型创建后不能修改。因此添加：

```java
public void setName(String name) {
    this.name = name;
}

public void setActive(boolean active) {
    this.active = active;
}

public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
}
```

不要创建 `setType`，因为设计规定分类类型不可修改。

### 第 5 步：启动验证

在 IDEA 中运行 `BackendApplication`。

成功标准：应用正常启动，控制台没有 `Schema-validation`、`AnnotationException`、表名或字段名映射错误。

---

## 阶段 2：创建 `CategoryRepository`

**涉及文件：**

- 新建：`backend/src/main/java/com/example/finance/category/repository/CategoryRepository.java`
- 参考：`backend/src/main/java/com/example/finance/category/entity/Category.java`

**这个阶段的目的：** 在不手写 SQL 的前提下，为后续 Service 准备访问 `categories` 表的工具。它不是业务规则，也不是接口；它只负责数据的基础读写。

### 第 1 步：创建包和接口

创建包：

```text
com.example.finance.category.repository
```

在里面创建接口 CategoryRepository：

```java
package com.example.finance.category.repository;

import com.example.finance.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
```

`Category` 表示这个仓库管理的是分类数据；`Long` 表示分类主键 `id` 的类型。

### 第 2 步：启动验证

再次运行 `BackendApplication`。

成功标准：应用正常启动。由于 `CategoryRepository` 位于 `com.example.finance` 包下面，Spring Boot 会自动发现它。

---

## 阶段 3：今晚收尾检查

检查 Java 字段和数据库列的对应关系：

| Java 字段 | MySQL 字段 |
|---|---|
| `id`（`Long`） | `id`（`BIGINT`） |
| `name` | `name` |
| `type`（`CategoryType`） | `type`（`VARCHAR(20)`） |
| `active` | `active` |
| `sortOrder` | `sort_order` |

完成以上三个阶段后，下一次学习从 `CategoryService` 开始：在那里实现分类名称不能重复、分类类型不可修改等业务规则。
