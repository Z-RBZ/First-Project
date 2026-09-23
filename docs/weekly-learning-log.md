# 个人记账应用：每周学习日志

> 本文档按周记录每天的具体学习内容。周从周一开始、周日结束；每次当天学习收尾时，补充或完善当天的条目。

## 第 1 周（2026-09-21 至 2026-09-27）

### 2026-09-21（周一）

#### 当天完成

- 讨论并确认个人记账应用的目标、MVP 功能、明确不做的范围、技术选型、数据模型、API 和页面结构。
- 在项目 `docs/` 下生成需求文档、设计文档、开发计划和跟写式开发引导。
- 确认使用 React + TypeScript 作为前端，Spring Boot + Maven 作为后端，phpStudy 的 MySQL 作为本地数据库。
- 在 IntelliJ IDEA 中使用 JDK 17 创建 Spring Boot 后端项目。
- 创建本地 MySQL 数据库和应用账号，配置 Spring Boot 的本地数据库连接。
- 成功运行 Spring Boot；Tomcat 在 `http://localhost:8080` 启动。

#### 学到的内容

- JDK 版本、Maven、Spring Boot 项目创建的基本关系。
- 本地数据库账号应区别于 MySQL `root` 账号。
- `application.yml`、`application-local.yml` 和示例配置文件的用途。

#### 当天检查结果

- 后端启动日志显示 Tomcat 已在 8080 端口启动。
- JPA 已初始化，说明后端能够连接数据库。

#### 未完成与下次继续点

- 创建由 Flyway 管理的数据库迁移脚本，并建立记账应用所需表结构。

### 2026-09-22（周二）

#### 当天完成

- 理解 `http://localhost:8080/` 的 Whitelabel 404：后端已启动，但目前没有为根路径 `/` 编写接口，因此不是启动失败。
- 在 `src/main/resources/db/migration/` 创建 `V1__create_tables.sql`。
- 编写并注释四张表的创建 SQL：`categories`、`transactions`、`monthly_budgets`、`category_budgets`。
- 重启后端，让 Flyway 自动执行 V1 迁移；确认 MySQL 中已有四张业务表和 `flyway_schema_history` 表。
- 学习 Java 包与文件夹的关系，创建 `com.example.finance.category.entity` 包。
- 创建 `CategoryType` 枚举，定义 `INCOME` 和 `EXPENSE`。
- 开始创建 `Category` 实体，理解 `@Entity`、`@Table`、`@Id`、`@GeneratedValue`、`@Enumerated(EnumType.STRING)` 与 `@Column` 的基本作用。

#### 学到的内容

- Flyway 的迁移文件命名规则：`V1__create_tables.sql`；迁移成功后不能继续随意改动该版本文件。
- `DECIMAL(12, 2)` 适合保存金额，避免使用 `FLOAT`。
- 数据库外键、唯一约束、索引和默认值的作用。
- Java 的 package 是代码分组目录；`entity` 用于放数据库表对应的实体类。
- Java 枚举可限制分类类型；使用 `EnumType.STRING` 可在数据库中保存清晰的 `INCOME`、`EXPENSE` 文本。
- JPA 通常会根据 Java 字段名推断列名；字段名与数据库列名不同时，使用 `@Column(name = "...")` 指定映射。

#### 当天检查结果

- Flyway 迁移已成功执行，数据库共出现五张表。
- `CategoryType.java` 和 `Category.java` 已创建；`Category` 目前处于字段映射的学习阶段。

#### 未完成与下次继续点

- 补全并复查 `Category` 实体类：构造方法、getter 和符合业务规则的修改方法。
- 运行后端，验证 JPA 映射与数据库表结构一致。
- 创建 `CategoryRepository`，再逐步实现分类查询和新增 API。

### 2026-09-23（周三）

#### 当天完成

- 补全 `Category` 实体的构造方法、getter、名称/状态/排序修改方法，并使 id 使用 `Long`、新分类默认启用。
- 创建 `CategoryRepository`，理解 `JpaRepository<Category, Long>` 为分类数据提供基础读写能力。
- 运行后端时出现 `Not a managed type: interface jdk.jfr.Category`；确认是 IDE 自动导入了错误的同名类，将 import 改为项目自己的 `Category` 实体后启动成功。
- 启动日志显示已发现 1 个 JPA Repository，数据库、Flyway、JPA 实体和 Repository 能一起正常启动。
- 建立更完整的分类管理 API 学习计划，明确 Entity、DTO、Repository、Service、Controller 的职责和后续七个阶段。
- 创建 `com.example.finance.category.dto` 包及 `CreateCategoryRequest`，包含名称、类型、排序字段、基础校验注解、getter 和 setter。
- 创建 `CategoryResponse.java` 文件，但尚未填写字段。

#### 学到的内容

- Java 方法的基本结构、getter 的作用，以及实例方法通常不需要 `static`。
- JPA 管理的实体类通过 `@Entity` 与数据库表建立映射。
- Spring Boot 报错优先查看最底部的 `Caused by`；包名 `jdk.jfr.Category` 直接说明导入了错误的同名类。
- `JpaRepository` 会自动提供 save、find、delete 等基础数据库操作。
- DTO 用于隔离 API 输入/输出与数据库 Entity；请求 DTO 不应允许客户端传 id 或修改分类 type。

#### 当天检查结果

- 修正 `CategoryRepository` 的 import 后，日志显示 `Found 1 JPA repository interface`，应用启动成功。
- `CategoryResponse` 文件存在但仍为空，尚未参与任何 API。

#### 未完成与下次继续点

- 先补全 `CategoryResponse` 的五个字段：id、name、type、active、sortOrder。
- 为 `CategoryResponse` 添加构造方法与 getter。
- 复查 `CreateCategoryRequest` 的 name 校验，确保空字符串和全空格都会被拒绝。
