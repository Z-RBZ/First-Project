# 个人记账应用：每周学习日志

> 本文档按周记录每天的具体学习内容。周一开始、周日结束；每次当天学习收尾时补充或完善当天条目。

## 第 1 周（2026-09-21 至 2026-09-27）

### 2026-09-21（周一）

#### 当天完成

- 确认个人记账应用的目标、MVP、技术选型、数据库模型和 API/页面设计。
- 在 `docs/` 下生成需求、设计、开发计划和跟写式开发文档。
- 使用 IntelliJ IDEA 与 JDK 17 创建 Spring Boot 后端，创建本地 MySQL 数据库和应用账号。

#### 学到的内容

- JDK、Maven、Spring Boot 的基本关系。
- `application.yml`、`application-local.yml` 与示例配置文件的作用。

#### 当天检查结果

- Tomcat 在 8080 端口启动，JPA 已初始化并能连接数据库。

#### 未完成与下次继续点

- 用 Flyway 创建数据库迁移脚本与业务表。

### 2026-09-22（周二）

#### 当天完成

- 创建 `V1__create_tables.sql`，建立分类、收支记录、月预算、分类预算四张业务表。
- 创建 `CategoryType` 枚举和 `Category` 实体的字段映射。

#### 学到的内容

- Flyway 迁移命名与“已执行迁移不随意修改”的规则。
- `@Entity`、`@Table`、`@Id`、`@GeneratedValue`、`@Enumerated` 和 `@Column` 的作用。

#### 当天检查结果

- Flyway 已执行，数据库中存在四张业务表和 `flyway_schema_history`。

#### 未完成与下次继续点

- 补全分类实体方法，创建 Repository。

### 2026-09-23（周三）

#### 当天完成

- 补全 `Category` 的构造方法、getter 和受业务规则限制的修改方法。
- 创建 `CategoryRepository`，修复同名类错误导入后应用启动成功。
- 创建分类 DTO：`CreateCategoryRequest`、`CategoryResponse` 等基础文件。

#### 学到的内容

- `JpaRepository<Category, Long>` 提供的基础数据库操作。
- DTO 用于隔离 API 输入/输出与 Entity；实例方法通常不需要 `static`。

#### 当天检查结果

- 日志显示已识别 1 个 JPA Repository，应用可启动。

#### 未完成与下次继续点

- 完成 DTO，并实现分类查询的 Repository、Service 和 Controller。

### 2026-09-26（周五）

#### 当天完成

- 补全分类响应、更新和状态更新 DTO。
- 为 `CategoryRepository` 增加排序、按类型/状态查询、重名检查方法。
- 创建统一错误响应 `ApiError`、分类相关异常和全局异常处理基础；编写 `ApiErrorTest`。

#### 学到的内容

- 构造方法的作用，以及响应对象为何通过构造方法一次性赋值。
- Maven 负责依赖管理、编译和测试；测试失败时可从“期望值/实际值”定位问题。

#### 当天检查结果

- `ApiErrorTest` 修正后通过；Spring 能识别 Repository 查询方法。

#### 未完成与下次继续点

- 实现分类查询 Service 和 Controller，让浏览器能看到真实分类数据。

## 第 2 周（2026-09-28 至 2026-10-04）

### 2026-09-28（周一）

#### 当天完成

- 创建 `CategoryService`，负责选择合适的 Repository 查询并把 `Category` 转换为 `CategoryResponse`。
- 创建 `CategoryController`，提供 `GET /get/category`；可传 `type=EXPENSE` 等可选筛选参数。
- 解决访问地址多写一个 `/` 导致的 404，确认正确地址为 `http://localhost:8080/get/category`。
- 在 phpStudy MySQL 中确认已有分类数据，并补充了一套收入、支出分类测试数据。

#### 学到的内容

- Service 层承担业务协调和 DTO 转换，Controller 层只负责接收 HTTP 请求、调用 Service、返回 JSON。
- `@RestController`、`@RequestMapping`、`@GetMapping`、`@RequestParam` 的职责，以及 URL 路径必须与映射完全一致。
- 端口被占用通常表示已有 Java 进程正在运行；根路径 `/` 的 404 与接口路径错误是两个不同问题。

#### 当天检查结果

- Spring Boot 已在 8080 端口启动；用户已手动访问 `GET /get/category` 并确认可用。
- 收尾执行 `mvnw.cmd test` 未完成：MySQL 当时无法连接，错误为 `Communications link failure`。该结果说明需要先启动 phpStudy MySQL，不代表编译错误或接口逻辑错误。

#### 未完成与下次继续点

- 先启动 phpStudy MySQL 并重新运行 `mvnw.cmd test`。
- 随后初始化 React + TypeScript 前端，开始分类页面：先请求并显示现有分类列表。

## 第 2 周（2026-09-28 至 2026-10-04）

### 2026-09-30（周三）

#### 当天完成

- 创建 React + TypeScript + Vite 前端工程，并完成分类管理页面第一版：分类列表、全部/收入/支出筛选和新增分类表单。
- 后端新增 `POST /get/category`，实现同类型分类重名校验、HTTP 201 响应与重复分类的 HTTP 409 错误响应。
- 添加 CORS 配置，使本地前端 `http://localhost:5173` 可以访问分类接口。
- 为分类页面加入金色主题 `#DAA520`、收入/支出 SVG 图标，以及普通版和尺寸标注版设计稿。
- 修复排序值输入框无法删除默认 `0` 的问题：编辑时保留字符串，提交时再转成数字；空值提交时按 `0` 处理。
- 将后端和前端分别以两次中文提交推送到 GitHub `main` 分支。

#### 学到的内容

- React 受控输入框中，`type="number"` 的 DOM 值仍是字符串；若每次输入都立即调用 `Number('')`，空字符串会变回 `0`。
- 前端通过 `fetch` 调用后端时，浏览器跨域访问需要后端显式配置允许的来源、方法和请求头。
- 一个仓库可以同时管理 `backend/` 与 `frontend/`；用两个独立提交可以让后端接口变更和前端页面变更的历史更清晰。
- Git 提交信息使用 `feat(范围): 中文摘要`、`fix(范围): 中文摘要` 等规范格式，能清楚表达改动性质和范围。

#### 当日检查结果

- 后端执行 `mvnw.cmd test`：2 个测试通过。
- 前端执行 `npm run build`：TypeScript 编译和 Vite 构建通过。
- GitHub 远程仓库 `Z-RBZ/First-Project` 已推送至 `main`；提交为 `7662962` 与 `16466b2`。

#### 未完成与下次继续点

- 在浏览器中完整手动验证分类列表、筛选、新增、重复分类提示，以及空排序值按 `0` 保存。
- 验证后进入收入/支出记账记录功能的需求细化与数据/API 设计。
