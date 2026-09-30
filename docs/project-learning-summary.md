# 个人记账应用：项目周期学习总览

> 本文档从项目全周期视角记录学习进度。每天学习结束后更新当天摘要、当前阶段和下一步；每天的具体过程见 `weekly-learning-log.md`。

## 项目状态

- 项目：个人记账应用（Personal Finance Tracker）
- 当前阶段：分类管理功能的首个可用后端接口已完成，准备开始分类页面。
- 当前状态：需求、设计、数据库结构、分类实体/DTO/Repository、统一错误响应基础和分类查询接口均已建立；前端尚未初始化。

## 已完成的里程碑

1. 确认个人使用的记账应用需求、MVP 范围、技术选型、数据模型、API 与页面设计，并生成项目文档。
2. 建立 Spring Boot + Maven 后端，使用 JDK 17、phpStudy MySQL 与 Flyway；本地应用能够连接数据库并在 8080 端口启动。
3. 通过 Flyway 创建 `categories`、`transactions`、`monthly_budgets`、`category_budgets` 四张业务表。
4. 完成分类领域的 `CategoryType`、`Category`、`CategoryRepository` 与请求/响应 DTO；Repository 查询方法可由 Spring Data JPA 识别。
5. 建立 `ApiError`、分类未找到/重名异常和全局异常处理的基础结构，并编写 `ApiErrorTest`。
6. 完成分类读取链路：`CategoryService` 负责查询和实体转响应 DTO，`CategoryController` 提供 `GET /get/category`，支持按 `type`、`active` 可选筛选。
7. 在 MySQL 中准备收入、餐饮、交通等分类测试数据，并手动确认浏览器可访问分类查询接口。

## 每日进度索引

| 日期 | 宏观进度 | 当天结束状态 |
|---|---|---|
| 2026-09-21 | 完成需求与设计，搭建后端和数据库基础环境 | Spring Boot、MySQL 连接可用 |
| 2026-09-22 | 建立 Flyway 表结构，开始分类实体建模 | 分类实体字段映射完成 |
| 2026-09-23 | 完成分类持久层基础并开始 DTO 建模 | Repository 可被 JPA 识别 |
| 2026-09-26 | 完成分类 DTO、Repository 查询方法和统一错误响应基础 | 分类查询链路准备就绪，待接入接口 |
| 2026-09-28 | 完成分类查询 Service、Controller 和测试分类数据 | `GET /get/category` 可手动访问；待创建前端分类页面 |

## 当前技术状态

- 数据库：`personal_finance` 由 Flyway 管理结构变更；分类测试数据已准备。
- 后端：分类查询接口为 `GET /get/category`，可选参数为 `type` 与 `active`。
- 后端分层：Entity 负责表映射，Repository 负责数据访问，Service 负责查询/转换，Controller 负责 HTTP 请求和响应。
- 测试：`ApiErrorTest` 已创建；2026-09-28 收尾执行 `mvnw.cmd test` 时，因 MySQL 未运行而连接失败，未将其误记为代码失败。
- 前端：计划使用 React + TypeScript，但尚未创建工程和分类页面。

## 下次继续点

1. 确认 phpStudy MySQL 已启动，再执行 `backend` 下的 `mvnw.cmd test`，完成一次可重复的后端收尾验证。
2. 在项目根目录创建 React + TypeScript 前端工程，先实现分类页面：读取并展示 `GET /get/category` 返回的分类列表。
3. 页面能展示数据后，再补充按收入/支出筛选，以及创建、编辑、启用/停用分类的接口与页面交互。

## 更新规则

- 每次学习结束时，追加当天宏观进度，并更新“项目状态”“已完成的里程碑”和“下次继续点”。
- 不删除已确认的历史事实；发现历史描述不准确时，以带日期的更正补充说明。
- 本文档只写项目级摘要，具体学习过程写入 `weekly-learning-log.md`。

## 2026-09-30 状态更新

> 更正：此前“前端尚未初始化”的记录已不再适用。

- 当前阶段：分类管理功能第一版已完成前后端联调，进入页面体验验证与后续功能迭代阶段。
- 当天完成：创建 React + TypeScript + Vite 前端；完成分类查询、收入/支出筛选与新增分类表单；后端补充 `POST /get/category`、重复分类冲突处理和本地 CORS 配置；页面主题统一为金色 `#DAA520`，并补充设计稿与尺寸标注稿。
- 验证状态：`mvnw.cmd test` 通过 2 个测试；`npm run build` 通过；前后端代码已分两次提交并推送到 GitHub `main` 分支。

### 每日进度补充

| 日期 | 宏观进度 | 当天结束状态 |
|---|---|---|
| 2026-09-30 | 完成分类管理页面第一版与新增分类联调 | 已推送 GitHub；待浏览器手动验证完整交互，并开始下一项记账记录功能 |

### 下次继续点

1. 同时启动 phpStudy MySQL、Spring Boot 后端与 Vite 前端，在浏览器完整验证分类列表、筛选、新增、重复分类报错和空排序值默认 0。
2. 分类页面验证通过后，设计并开始“收入/支出记账记录”的数据模型与第一个列表接口。
