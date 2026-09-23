# 个人记账应用：项目周期学习总览

> 本文档从项目全周期的角度记录学习进度。每天学习结束后更新当天的摘要、当前阶段和下一步；具体到当天的学习过程见 `weekly-learning-log.md`。

## 项目状态

- 项目：个人记账应用（Personal Finance Tracker）
- 当前阶段：分类管理 API 的 DTO 建模
- 当前状态：需求、设计、后端基础环境和数据库结构已完成；分类实体和 Repository 已验证可启动，创建分类请求 DTO 已开始，分类 API 尚未实现。

## 已完成的里程碑

1. 确认项目需求与范围，并完成需求、设计、开发计划和分步学习文档。
2. 确认技术路线：React + TypeScript、Spring Boot + Maven、MySQL + Flyway。
3. 使用 IntelliJ IDEA 和 JDK 17 创建 Spring Boot 后端；本地后端能够在 8080 端口启动。
4. 创建 MySQL 数据库及应用账号，并确认后端可以连接数据库。
5. 用 Flyway 的 `V1__create_tables.sql` 创建四张业务表：分类、收支记录、月度总预算、分类预算。
6. 确认 Flyway 已执行迁移，数据库中存在四张业务表和 `flyway_schema_history` 记录表。
7. 开始分类功能的数据模型：已创建 `CategoryType` 枚举和 `Category` 实体类的字段映射草稿。
8. 补全 `Category` 的构造方法、getter 和允许的修改方法；移除了不应暴露的 id、type 修改方法。
9. 创建 `CategoryRepository`，修正同名类自动导入问题后，确认 Spring Boot 成功识别 1 个 JPA Repository。
10. 创建 `category.dto` 包和 `CreateCategoryRequest`，开始为创建分类接口定义输入数据与基础校验。

## 每日进度索引

| 日期 | 宏观进度 | 当天结束状态 |
|---|---|---|
| 2026-09-21 | 完成需求与设计，搭建后端和数据库基础环境 | Spring Boot 可启动，MySQL 连接可用 |
| 2026-09-22 | 用 Flyway 建立完整数据库结构，开始分类实体建模 | 已创建分类枚举和实体字段；尚未完成实体方法与分类 API |
| 2026-09-23 | 完成分类持久层基础，开始分类 API 的 DTO 建模 | `CategoryResponse` 已创建但尚未填写字段；尚未实现 Service、Controller 和 API |

## 当前技术状态

- 数据库：`personal_finance` 已由 Flyway 管理结构变更。
- 表结构：`categories`、`transactions`、`monthly_budgets`、`category_budgets` 已创建。
- 后端：首页路径 `/` 返回 404 属于预期现象，因为尚未创建任何 Controller 或接口。
- 分类代码：`CategoryType`、`Category`、`CategoryRepository` 已完成并通过应用启动验证；`CreateCategoryRequest` 已创建，`CategoryResponse` 尚未完成。

## 下次继续点

1. 补全 `CategoryResponse`：添加分类返回所需的 id、名称、类型、启用状态、排序字段，再添加构造方法和 getter。
2. 复查 `CreateCategoryRequest` 的名称校验，确保名称为空或全空格时也会被拒绝。
3. 继续创建其余 DTO，再实现分类查询与创建的 Service 逻辑。

## 更新规则

- 每次学习结束时，为当天追加一条宏观进度记录，并更新“项目状态”“已完成的里程碑”和“下次继续点”。
- 不修改已确认的历史事实；发现之前的记录不准确时，以“更正”方式补充说明。
- 本文档只写项目层面的摘要，具体学习过程写入 `weekly-learning-log.md`。
