# 个人记账应用开发计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use `superpowers:executing-plans` to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**目标：** 由学习者亲自完成一个可在局域网中使用的 React + Spring Boot + MySQL 个人记账应用。

**架构：** React 通过 REST API 访问 Spring Boot；后端按功能包组织 Controller、Service、Repository、Entity 与 DTO；MySQL 仅在运行服务的电脑本机启动。每个功能先以测试和 Postman 验证 API，再接入 React 页面。

**技术栈：** Java 21、Spring Boot、Maven、Spring Data JPA、Flyway、MySQL（phpStudy）、React、TypeScript、Vite、Recharts、JUnit 5、Mockito、MockMvc、Postman。

**依据文档：**

- [需求文档](requirements.md)
- [设计文档](design.md)

## 全局约束

- 第一版为单用户、单账本；没有登录、多人、多账户和云端同步。
- 前端只使用 `fetch`、`useState`、`useEffect`，不引入 Redux、React Query、Axios。
- 金额在后端使用 `BigDecimal`，在 MySQL 使用 `DECIMAL(12,2)`。
- 数据库只允许本机后端连接；手机只访问后端 HTTP 服务。
- 不提交真实数据库密码、`application-local.yml`、前端本地环境文件或真实账本数据。
- 每个阶段先运行自动化测试，再用 Postman 验证 API，最后再连接 React。
- 一个完成阶段对应一次小而清晰的 Git 提交。

## 审查重点

以下输入与失败场景必须在对应任务中被验证：

1. 金额为 `0`、负数或超过两位小数时，创建交易必须返回字段校验错误（任务 4）。
2. 已停用分类、或收入交易选择支出分类时，后端必须拒绝交易（任务 4）。
3. 同月重复设置总预算、或同月同分类重复设置预算时，更新必须覆盖既有值而非创建重复数据（任务 6）。
4. 删除不存在的交易、分类或预算时，必须返回 `404`，而不是 `500`（任务 4、5、6）。
5. 空列表、空筛选结果和后端不可访问时，前端必须显示对应状态且可重试（任务 5、7）。

---

## 目录与文件清单

第一阶段完成后，项目应逐步形成以下结构：

```text
personal-finance-tracker/
├─ backend/
│  ├─ pom.xml
│  ├─ src/main/java/com/example/finance/
│  │  ├─ FinanceApplication.java
│  │  ├─ common/
│  │  ├─ category/
│  │  ├─ transaction/
│  │  ├─ budget/
│  │  └─ statistics/
│  ├─ src/main/resources/
│  │  ├─ application.yml
│  │  ├─ application-example.yml
│  │  └─ db/migration/
│  └─ src/test/java/com/example/finance/
├─ frontend/
│  ├─ package.json
│  └─ src/
│     ├─ api/
│     ├─ components/
│     ├─ pages/
│     ├─ styles/
│     └─ types/
├─ docs/
└─ postman/
```

---

## 任务 1：准备本地环境与项目骨架

**学习目标：** 了解 Spring Boot 项目结构、Maven 依赖、Vite 开发服务器及 MySQL 连接配置。

**文件：**

- 创建：`backend/pom.xml`
- 创建：`backend/src/main/java/com/example/finance/FinanceApplication.java`
- 创建：`backend/src/main/resources/application.yml`
- 创建：`backend/src/main/resources/application-example.yml`
- 创建：`backend/src/main/resources/db/migration/V1__create_initial_tables.sql`
- 创建：`frontend/`（通过 Vite 创建）
- 创建：`.gitignore`（项目目录内，或补充仓库根 `.gitignore`）

**产出：** 后端能启动并连接 phpStudy MySQL；前端能显示 Vite 默认页面；Flyway 建表成功。

- [ ] 在 phpStudy 中启动 MySQL，创建数据库 `personal_finance` 和只对该数据库有权限的普通用户。

  记录本机端口、用户名和密码到自己的安全位置；不要写入 Git。

- [ ] 在 IntelliJ IDEA 中使用 Spring Initializr 创建 Maven 项目，选择 Java 21，并添加：Spring Web、Spring Data JPA、Validation、MySQL Driver、Flyway Migration、Spring Boot Test。

- [ ] 在 `pom.xml` 中添加测试依赖 Mockito（如果 Spring Boot Starter Test 未间接提供需要的版本），并确认打包方式为 jar。

- [ ] 写入 `application-example.yml`，只保留变量名，不写真实凭据：

  ```yaml
  spring:
    datasource:
      url: ${DB_URL}
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
    jpa:
      hibernate:
        ddl-auto: validate
    flyway:
      enabled: true
  ```

- [ ] 创建不提交的 `application-local.yml`，填入本机 phpStudy MySQL 连接；在 `application.yml` 中启用 `local` profile 或在 IDEA 运行配置中指定该 profile。

- [ ] 写出第一版 Flyway 建表 SQL。`categories` 使用 `UNIQUE(name, type)`；`transactions.category_id`、`category_budgets.category_id` 使用外键引用 `categories.id`；`monthly_budgets` 使用 `UNIQUE(budget_month)`；`category_budgets` 使用 `UNIQUE(budget_month, category_id)`；所有金额列使用 `DECIMAL(12,2)`；为 `transactions(transaction_date)`、`transactions(category_id)` 建索引。

- [ ] 用 Vite 创建 React + TypeScript 项目到 `frontend/`，在 VS Code 中启动它。

- [ ] 把以下规则加入 `.gitignore`：

  ```gitignore
  backend/src/main/resources/application-local.yml
  frontend/.env.local
  frontend/node_modules/
  frontend/dist/
  backend/target/
  ```

- [ ] 启动后端，确认 Flyway 输出迁移成功；在 phpStudy 或数据库客户端确认四张表存在。

- [ ] 启动前端，确认浏览器可打开 Vite 页面。

- [ ] 提交：`chore: initialize backend frontend and database schema`

---

## 任务 2：实现统一错误响应与健康检查

**学习目标：** 理解 Controller、DTO、全局异常处理器和 MockMvc。

**文件：**

- 创建：`backend/src/main/java/com/example/finance/common/api/ApiError.java`
- 创建：`backend/src/main/java/com/example/finance/common/exception/ResourceNotFoundException.java`
- 创建：`backend/src/main/java/com/example/finance/common/exception/GlobalExceptionHandler.java`
- 创建：`backend/src/main/java/com/example/finance/common/health/HealthController.java`
- 创建：`backend/src/test/java/com/example/finance/common/health/HealthControllerTest.java`

**接口：**

- 产生：`GET /api/health` 返回 `{"status":"ok"}`。
- 产生：统一错误 JSON：`status`、`code`、`message`、可选 `fieldErrors`。

- [ ] 先写 `HealthControllerTest`：使用 MockMvc 发送 `GET /api/health`，断言 `200`、`application/json` 和 `status=ok`。

- [ ] 运行测试，确认在控制器不存在时失败。

  ```powershell
  cd backend
  .\mvnw.cmd test -Dtest=HealthControllerTest
  ```

- [ ] 实现 `HealthController`，只返回不可变 Map 或一个简单响应 DTO。

- [ ] 编写 `ApiError` 和 `GlobalExceptionHandler`。至少映射：Bean Validation 异常为 `400/VALIDATION_ERROR`，`ResourceNotFoundException` 为 `404/NOT_FOUND`，未处理异常为 `500/INTERNAL_ERROR`。

- [ ] 重新运行测试，确认通过；再用 Postman 请求 `GET http://localhost:8080/api/health`。

- [ ] 提交：`feat: add health endpoint and api error format`

---

## 任务 3：实现分类管理 API 与默认分类

**学习目标：** 学习 JPA 实体、枚举、Repository、DTO 映射与 Service 业务规则。

**文件：**

- 创建：`backend/src/main/java/com/example/finance/category/CategoryType.java`
- 创建：`backend/src/main/java/com/example/finance/category/Category.java`
- 创建：`backend/src/main/java/com/example/finance/category/CategoryRepository.java`
- 创建：`backend/src/main/java/com/example/finance/category/CategoryService.java`
- 创建：`backend/src/main/java/com/example/finance/category/CategoryController.java`
- 创建：`backend/src/main/java/com/example/finance/category/dto/CreateCategoryRequest.java`
- 创建：`backend/src/main/java/com/example/finance/category/dto/UpdateCategoryRequest.java`
- 创建：`backend/src/main/java/com/example/finance/category/dto/CategoryResponse.java`
- 创建：`backend/src/main/resources/db/migration/V2__insert_default_categories.sql`
- 创建：`backend/src/test/java/com/example/finance/category/CategoryServiceTest.java`
- 创建：`backend/src/test/java/com/example/finance/category/CategoryControllerTest.java`

**接口：**

- `GET /api/categories?type=INCOME|EXPENSE&active=true|false`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `PATCH /api/categories/{id}/status`

- [ ] 写 `CategoryServiceTest` 的失败用例：创建收入分类“工资”后，再创建同类型“工资”时抛出业务冲突异常；同名“工资”可作为支出分类创建。

- [ ] 实现 `CategoryType`：仅 `INCOME`、`EXPENSE` 两个值；实体字段包含 `name`、`type`、`active=true`、`sortOrder` 和审计时间。

- [ ] 在 Repository 中声明按类型、状态、排序查询，以及按类型判断名称是否存在的方法。

- [ ] 实现 Service：创建、列表、更新名称和排序、启用/停用；更新时不得改变类型。

- [ ] 在请求 DTO 使用 `@NotBlank`、`@Size(max = 50)` 等注解；控制器只处理 DTO 和 HTTP 状态。

- [ ] 写 `CategoryControllerTest`：空名称返回 `400`；不存在分类更新状态返回 `404`；停用分类返回更新后的 `active=false`。

- [ ] 创建默认分类迁移：收入至少包含工资、奖金、兼职、投资收益、其他收入；支出至少包含餐饮、交通、住房、购物、娱乐、医疗、教育、其他支出。

- [ ] 用 Postman 建立“Categories”文件夹，保存列表、创建、修改、停用四个请求。

- [ ] 运行全部后端测试：

  ```powershell
  cd backend
  .\mvnw.cmd test
  ```

- [ ] 提交：`feat: add category management api`

---

## 任务 4：实现收支记录 API 与筛选

**学习目标：** 学习实体关系、分页查询、`BigDecimal`、日期范围筛选和跨实体业务校验。

**文件：**

- 创建：`backend/src/main/java/com/example/finance/transaction/Transaction.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/TransactionRepository.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/TransactionService.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/TransactionController.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/dto/CreateTransactionRequest.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/dto/UpdateTransactionRequest.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/dto/TransactionResponse.java`
- 创建：`backend/src/main/java/com/example/finance/transaction/dto/TransactionPageResponse.java`
- 创建：`backend/src/test/java/com/example/finance/transaction/TransactionServiceTest.java`
- 创建：`backend/src/test/java/com/example/finance/transaction/TransactionControllerTest.java`

**接口：**

- `GET /api/transactions?from=YYYY-MM-DD&to=YYYY-MM-DD&type=INCOME|EXPENSE&categoryId={categoryId}&keyword={keyword}&page=0&size=20`
- `POST /api/transactions`
- `PUT /api/transactions/{id}`
- `DELETE /api/transactions/{id}`

- [ ] 先写 Service 失败用例：金额 `0`、负数、三位小数、停用分类、交易类型与分类类型不匹配均被拒绝。

- [ ] 实现 `Transaction` 实体：`amount`、`type`、`transactionDate`、`category`、可空 `note`、创建/更新时间；金额列精度为 `DECIMAL(12,2)`。

- [ ] 实现创建与更新 Service：先查分类，再验证分类启用且类型一致，最后保存交易。

- [ ] 实现查询：结果按 `transactionDate DESC, createdAt DESC` 排序；支持设计文档中的四种筛选条件；返回总元素数、当前页数据及筛选结果的收入合计、支出合计、结余。

- [ ] 实现删除：不存在交易抛出 `ResourceNotFoundException`；存在交易永久删除并返回 `204 No Content`。

- [ ] 写 Controller 测试：非法金额返回 `400` 并含 `fieldErrors.amount`；删除不存在 ID 返回 `404`；查询结果排序正确。

- [ ] 用 Postman 保存创建收入、创建支出、组合筛选、修改、删除和非法请求；检查 phpStudy 中真实数据变化。

- [ ] 提交：`feat: add transaction api and filters`

---

## 任务 5：实现 React 基础壳、分类管理和收支首页

**学习目标：** 学习 React Router、TypeScript 类型、`fetch` 封装、表单、列表及四种异步状态。

**文件：**

- 创建：`frontend/src/api/client.ts`
- 创建：`frontend/src/api/categories.ts`
- 创建：`frontend/src/api/transactions.ts`
- 创建：`frontend/src/types/category.ts`
- 创建：`frontend/src/types/transaction.ts`
- 创建：`frontend/src/components/AppLayout.tsx`
- 创建：`frontend/src/components/TransactionForm.tsx`
- 创建：`frontend/src/components/TransactionFilters.tsx`
- 创建：`frontend/src/components/TransactionList.tsx`
- 创建：`frontend/src/pages/HomePage.tsx`
- 创建：`frontend/src/pages/CategoriesPage.tsx`
- 修改：`frontend/src/App.tsx`

**接口：**

- 消费任务 3、4 的分类和交易 API。
- 产生路由：`/`、`/categories`、`/budgets`、`/statistics`（后两者先显示“建设中”提示）。

- [ ] 定义前端 `Category`、`Transaction`、`TransactionQuery` 类型，字段名与后端响应一致。

- [ ] 实现 `client.ts`：基于 `fetch`，自动设置 `Content-Type: application/json`；非 2xx 响应解析后端 `ApiError` 并抛出包含 `message` 和 `fieldErrors` 的错误对象。

- [ ] 实现应用导航：桌面使用左侧导航；窄屏幕用 CSS 切换为底部导航；首页路径必须是 `/`。

- [ ] 先实现 `HomePage` 的加载中、无数据、请求失败（带重试）和成功四种状态，再实现数据渲染。

- [ ] 实现交易列表：按日期分组；每项显示分类、备注、类型和金额；收入与支出具有可区分但不只依赖颜色的标识。

- [ ] 实现筛选栏：日期范围、类型、分类、关键字；将筛选写入 URL 查询参数；筛选变化后重新请求列表。

- [ ] 实现新增和编辑表单：类型变更后只显示相同类型的启用分类；提交成功后关闭表单并重新加载列表；提交失败时显示字段错误。

- [ ] 实现删除确认弹窗：用户确认后调用删除 API，成功后重新加载；取消不发请求。

- [ ] 实现 `CategoriesPage`：收入/支出切换、创建、改名和启用/停用；每个 API 错误都显示后端消息。

- [ ] 在浏览器验证：空数据、后端停止时重试、筛选、创建、编辑、删除、手机窄屏导航。

- [ ] 提交：`feat: add transaction home page and category ui`

---

## 任务 6：实现月总预算与分类预算

**学习目标：** 学习按月建模、唯一约束、预算使用计算和幂等更新接口。

**文件：**

- 创建：`backend/src/main/java/com/example/finance/budget/MonthlyBudget.java`
- 创建：`backend/src/main/java/com/example/finance/budget/CategoryBudget.java`
- 创建：`backend/src/main/java/com/example/finance/budget/MonthlyBudgetRepository.java`
- 创建：`backend/src/main/java/com/example/finance/budget/CategoryBudgetRepository.java`
- 创建：`backend/src/main/java/com/example/finance/budget/BudgetService.java`
- 创建：`backend/src/main/java/com/example/finance/budget/BudgetController.java`
- 创建：`backend/src/main/java/com/example/finance/budget/dto/UpsertBudgetRequest.java`
- 创建：`backend/src/main/java/com/example/finance/budget/dto/BudgetSummaryResponse.java`
- 创建：`backend/src/test/java/com/example/finance/budget/BudgetServiceTest.java`
- 创建：`backend/src/test/java/com/example/finance/budget/BudgetControllerTest.java`
- 创建：`frontend/src/api/budgets.ts`
- 创建：`frontend/src/pages/BudgetsPage.tsx`

**接口：**

- 消费：`GET /api/budgets?month=YYYY-MM`
- 产生：设计文档定义的四个 `PUT`/`DELETE` 预算接口。

- [ ] 写失败测试：分类预算关联收入分类时拒绝；金额不大于 0 时拒绝；无效月份格式返回 `400`。

- [ ] 实现 `YearMonth` 与数据库 `budgetMonth`（当月第一天）之间的转换，集中放在 BudgetService 或专用转换器中，不能散落在 Controller。

- [ ] 实现总预算与分类预算的 upsert：相同月份（和分类）重复保存时更新金额，不能产生重复行。

- [ ] 计算并返回：预算金额、已支出、剩余金额、使用率、是否超支；已支出只统计该月支出交易。

- [ ] 写 Controller 测试：清除不存在预算返回 `404`；更新同月预算后数据库仅有一条记录；超支时 `remainingAmount` 为负数且 `overBudget=true`。

- [ ] 用 Postman 验证总预算、分类预算、覆盖更新、清除和超支场景。

- [ ] 实现 `BudgetsPage`：月份选择、月总预算输入、分类预算输入、已用/剩余/超支状态；保存或清除后重新请求数据。

- [ ] 提交：`feat: add monthly and category budgets`

---

## 任务 7：实现月度统计接口与图表页

**学习目标：** 学习聚合查询、统计 DTO 和将 API 数据映射为图表数据。

**文件：**

- 创建：`backend/src/main/java/com/example/finance/statistics/StatisticsService.java`
- 创建：`backend/src/main/java/com/example/finance/statistics/StatisticsController.java`
- 创建：`backend/src/main/java/com/example/finance/statistics/dto/MonthlyStatisticsResponse.java`
- 创建：`backend/src/main/java/com/example/finance/statistics/dto/CategoryAmountResponse.java`
- 创建：`backend/src/test/java/com/example/finance/statistics/StatisticsServiceTest.java`
- 创建：`backend/src/test/java/com/example/finance/statistics/StatisticsControllerTest.java`
- 创建：`frontend/src/api/statistics.ts`
- 创建：`frontend/src/pages/StatisticsPage.tsx`

**接口：**

- 产生并消费：`GET /api/statistics/monthly?month=YYYY-MM`。
- 返回：总收入、总支出、结余、收入分类占比、支出分类占比、总预算与分类预算使用情况。

- [ ] 写 Service 测试：给定同月收入、支出和不同分类交易，断言总收入、总支出、结余、分类金额和百分比正确；无交易月份所有金额为 0 且分类列表为空。

- [ ] 实现统计查询，确保只统计目标自然月；收入和支出分类结果分别返回；金额保持 `BigDecimal` 到 DTO 映射的一致性。

- [ ] 写 Controller 测试：非法 `month` 返回 `400`；合法月份返回 `200` 和约定字段。

- [ ] 在前端安装并使用 Recharts：收入与支出使用独立分类图表；预算使用使用对比图或进度条。图表必须有文字摘要，不能只靠颜色表达含义。

- [ ] 实现月份切换和四种异步状态；无数据月份显示解释文字，不显示空白区域。

- [ ] 用至少两个月的 Postman 或数据库测试数据验证切换月份后所有汇总、图表和预算数据同步变化。

- [ ] 提交：`feat: add monthly statistics and charts`

---

## 任务 8：完善局域网运行、文档与发布检查

**学习目标：** 学习让别人能够复现运行开源项目，以及用 Git 管理可验证的交付物。

**文件：**

- 创建：`README.md`（项目目录内）
- 创建：`postman/personal-finance-tracker.postman_collection.json`
- 创建：`backend/src/main/resources/application-example.yml`（如任务 1 未完成）
- 修改：项目 `.gitignore`

**产出：** 新使用者能根据 README 启动 phpStudy MySQL、后端和前端，并从同一 Wi-Fi 的手机访问应用。

- [ ] 在 README 写清：项目功能、技术栈、前置安装项、phpStudy MySQL 建库建用户步骤、后端配置方式、前后端启动命令、默认本机访问地址、手机局域网访问地址格式、常见防火墙提示。

- [ ] 导出 Postman Collection；将基础地址写成 Collection 变量 `baseUrl`，默认 `http://localhost:8080`，避免写死个人 IP。

- [ ] 确认 `git status` 未出现本地配置、密码、数据库导出文件、`node_modules` 或 `target`。

- [ ] 在新终端按 README 分别启动后端、前端，运行全部后端测试并导入 Postman Collection。

  ```powershell
  cd backend
  .\mvnw.cmd test
  cd ..\frontend
  npm run build
  ```

- [ ] 在同一 Wi-Fi 的手机浏览器中访问电脑局域网 IP 的服务地址，新增一条测试交易并在电脑浏览器确认它出现。

- [ ] 手动执行需求文档第 7 节的全部验收标准，逐项记录结果。

- [ ] 提交：`docs: add setup guide and api collection`

---

## 开发时的固定节奏

每次只做一个任务中的一个小步骤：

1. 阅读本计划、需求文档和设计文档中对应部分。
2. 先写一个失败测试或明确的 Postman 预期。
3. 只写让该测试通过的最小代码。
4. 运行对应测试；通过后再运行模块全量测试。
5. 用 Postman 验证真实 API。
6. API 稳定后再让 React 调用它。
7. 运行前端构建，并在桌面和手机宽度下检查页面。
8. 完成一个可独立验收的阶段再提交 Git。

当你准备开始时，从任务 1 的第一项开始；你写每一步，我可以解释、检查你的代码，或在遇到报错时和你一起定位原因。
