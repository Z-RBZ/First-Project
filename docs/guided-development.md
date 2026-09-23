# 个人记账应用跟做开发手册

这是一份给初学者的操作手册。不要试图一次完成全部内容：每次只做一个“检查点”，看到预期结果后再继续。你负责亲手输入、理解和运行；卡住时把命令输出、报错文字或截图发给我，我会只带你解决当前检查点。

本手册配合以下文档使用：

- [需求文档](requirements.md)：做什么。
- [设计文档](design.md)：为什么这样设计。
- [开发计划](development-plan.md)：完整任务清单和测试要求。

## 0. 开始前：你需要准备什么

| 工具 | 用途 | 完成标志 |
|---|---|---|
| IntelliJ IDEA | 编写、运行 Java 和 Spring Boot | 能创建或打开 Java 项目。 |
| VS Code | 编写 React | 能打开文件夹和集成终端。 |
| JDK 21 | 运行和编译 Java | `java -version` 显示 21。 |
| Node.js LTS | 运行 React、Vite | `node -v` 和 `npm -v` 有版本号。 |
| phpStudy | 启动 MySQL | 面板中 MySQL 状态为“已启动”。 |
| Postman | 单独测试后端 API | 能新建一个 HTTP 请求。 |
| Git | 保存阶段性成果 | `git --version` 有版本号。 |

### 检查点 0：确认命令行环境

1. 打开 PowerShell。
2. 依次输入：

   ```powershell
   java -version
   node -v
   npm -v
   git --version
   ```

3. 每一条都应输出版本号；其中 Java 的主版本必须是 `21`。
4. 若任何一条显示“不是内部或外部命令”，先不要继续。把整段输出发给我。

完成后，在本手册对应位置打勾：

- [ ] 检查点 0 已通过。

## 1. 本项目的学习方式

每个功能都遵守同一个小循环：

```text
理解一个小目标 → 自己写代码 → 运行测试 → Postman 请求 → React 页面调用 → Git 提交
```

例如，“新增收支记录”不是一次写完，而是拆成：

1. 后端定义请求数据。
2. 后端写一个失败测试。
3. 后端实现最小代码，让测试通过。
4. Postman 创建一笔记录，确认 MySQL 中有数据。
5. React 写表单，调用已经验证过的接口。
6. 浏览器确认新增记录马上出现在首页。

### 你每次需要发给我的内容

到每个检查点时，优先发其中一种即可：

- 命令的完整文字输出；
- 报错文字，不要只描述“报错了”；
- 相关文件的完整内容；
- 浏览器或 phpStudy 的截图。

我会解释原因、给出下一小步或提示你如何修改；除非你明确要求示例答案，我不会替你直接写整块代码。

## 2. 第一次开发会话：建立空项目

本次会话的目标不是做记账功能，而是让“后端、前端、MySQL”三个部分能分别运行。

### 检查点 1：确认项目目录

1. 在 PowerShell 中进入项目目录：

   ```powershell
   Set-Location E:\CodexLearnPlan\projects\personal-finance-tracker
   Get-ChildItem
   ```

2. 你应该能看到 `docs` 文件夹。
3. 此时不要手动创建 `backend` 和 `frontend` 文件夹；下一步由各自的创建工具生成。

- [ ] 检查点 1 已通过。

### 检查点 2：用 IntelliJ IDEA 创建后端

1. 打开 IntelliJ IDEA，选择 **New Project**。
2. 选择 **Spring Boot**；如果 IDE 没有该选项，选择 **Spring Initializr**。
3. 填写以下值：

   | 项目项 | 值 |
   |---|---|
   | Name | `backend` |
   | Location | `E:\CodexLearnPlan\projects\personal-finance-tracker\backend` |
   | Language | Java |
   | Type | Maven |
   | Group | `com.example` |
   | Artifact | `finance` |
   | Package name | `com.example.finance` |
   | JDK | 21 |
   | Packaging | Jar |

4. 在依赖选择页面勾选：

   - Spring Web
   - Spring Data JPA
   - Validation
   - MySQL Driver
   - Flyway Migration
   - Spring Boot Starter Test

5. 点击 Create，等待 Maven 下载依赖完成。
6. 找到 `FinanceApplication.java`，点击类左侧的绿色运行按钮。

**预期结果：** 控制台最后出现类似 `Started FinanceApplication` 的文字。此时可能因为数据库尚未配置而启动失败；如果失败，把从 `ERROR` 开始的内容发给我，不要自行删依赖。

- [ ] 检查点 2 已通过。

### 检查点 3：用 phpStudy 创建专用数据库

1. 打开 phpStudy，启动 MySQL。
2. 打开 phpStudy 内置的数据库管理工具，或打开你平时连接 phpStudy MySQL 的工具。
3. 执行以下 SQL。第二条命令里的密码请替换为你自己新建的本地密码；不要把真实密码发到 Git 或写进截图。

   ```sql
   CREATE DATABASE personal_finance
     CHARACTER SET utf8mb4
     COLLATE utf8mb4_unicode_ci;

   CREATE USER 'finance_app'@'localhost'
     IDENTIFIED BY 'ChooseANewLocalPassword';

   GRANT ALL PRIVILEGES ON personal_finance.*
     TO 'finance_app'@'localhost';

   FLUSH PRIVILEGES;
   ```

4. 刷新数据库列表，确认出现 `personal_finance`。
5. 用新账户连接一次该数据库，确认账户可用。

**为什么不使用 root：** 项目代码即使配置泄露，也只能访问这一套数据库，影响范围更小。

- [ ] 检查点 3 已通过。

### 检查点 4：让 Spring Boot 连接 MySQL

1. 在 `backend/src/main/resources/` 新建 `application-local.yml`。
2. 写入下面的配置。把 `你的密码` 改为你刚刚设置的密码；端口 `3306` 若与你的 phpStudy 不同，改成 phpStudy 显示的端口。

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/personal_finance?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
       username: finance_app
       password: 你的密码
     jpa:
       hibernate:
         ddl-auto: validate
     flyway:
       enabled: true
   ```

3. 在同一目录创建 `application.yml`：

   ```yaml
   spring:
     application:
       name: personal-finance-tracker
     config:
       import: optional:classpath:application-local.yml
   ```

4. 创建 `application-example.yml`，复制 `application-local.yml` 的结构，但把密码改为 `${DB_PASSWORD}`，用户名改为 `${DB_USERNAME}`，数据库地址改为 `${DB_URL}`。
5. 打开项目根目录 `.gitignore`；如果不存在就创建。加入：

   ```gitignore
   backend/src/main/resources/application-local.yml
   backend/target/
   ```

6. 此时运行仍会失败，因为 Flyway 还没有迁移文件。这是正常的，继续下一检查点。

- [ ] 检查点 4 已通过。

### 检查点 5：创建第一份数据库迁移

1. 在 `backend/src/main/resources/` 下创建目录 `db/migration/`。
2. 在其中创建文件 `V1__create_initial_tables.sql`。
3. 先自己根据设计文档的“4. 数据模型”尝试写出四张表；不要求一次写对。
4. 写完后对照下面的检查清单逐项确认：

   - [ ] 有 `categories` 表，并包含分类名称、收入/支出类型、启用状态、排序和时间字段。
   - [ ] 有 `transactions` 表，并包含金额、交易日期、分类外键、备注和时间字段。
   - [ ] 有 `monthly_budgets` 表，且一个月份最多一条。
   - [ ] 有 `category_budgets` 表，且同月同分类最多一条。
   - [ ] 所有金额都是 `DECIMAL(12,2)`，不是 `FLOAT` 或 `DOUBLE`。
   - [ ] 交易和分类预算通过外键关联到分类。
   - [ ] `categories` 同一收入/支出类型内名称唯一。

5. 运行后端：

   ```powershell
   Set-Location E:\CodexLearnPlan\projects\personal-finance-tracker\backend
   .\mvnw.cmd spring-boot:run
   ```

6. 打开数据库工具，刷新 `personal_finance`，确认四张表出现。

**遇到 SQL 报错时：** 不要连续改很多行。把 SQL 文件内容和控制台中 `Caused by:` 后的完整错误一起发给我；我会先解释是哪条约束或语法出问题。

- [ ] 检查点 5 已通过。

### 检查点 6：创建 React 前端

1. 打开 VS Code，选择 **File → Open Folder**，打开：

   ```text
   E:\CodexLearnPlan\projects\personal-finance-tracker
   ```

2. 在 VS Code 的终端输入：

   ```powershell
   npm create vite@latest frontend -- --template react-ts
   Set-Location frontend
   npm install
   npm run dev
   ```

3. 打开终端显示的本地地址，通常是 `http://localhost:5173`。
4. 看到 Vite + React 页面即表示前端骨架完成。
5. 在项目根 `.gitignore` 再加入：

   ```gitignore
   frontend/node_modules/
   frontend/dist/
   frontend/.env.local
   ```

- [ ] 检查点 6 已通过。

### 第一次会话结束时应达到的状态

你应能同时做到：

- phpStudy 的 MySQL 正在运行。
- Spring Boot 启动不报错，Flyway 已创建四张表。
- Vite 前端在浏览器可访问。
- `application-local.yml` 没有被 Git 跟踪。

完成后运行：

```powershell
Set-Location E:\CodexLearnPlan
git status --short
```

确认输出中没有 `application-local.yml`、密码或 `node_modules`。然后把输出发给我，我们再进行“健康检查 API”。

---

## 3. 第二次会话：先做一个最小 API

本次只实现 `GET /api/health`，目的是建立“写代码 → 运行 → Postman 验证”的习惯。

### 检查点 7：健康检查接口

1. 在 `com.example.finance` 下创建包 `common.health`。
2. 在其中创建 `HealthController`。
3. 先写一个测试文件：`src/test/java/com/example/finance/common/health/HealthControllerTest.java`。
4. 测试要求：请求 `GET /api/health` 时返回 `200`，JSON 内有 `status` 且值为 `ok`。
5. 先运行测试，确认它失败；失败证明测试确实在检查一个尚未实现的接口。
6. 再实现控制器，只返回 `{"status":"ok"}`。
7. 重新运行测试：

   ```powershell
   .\mvnw.cmd test -Dtest=HealthControllerTest
   ```

8. 在 Postman 中新建请求：

   ```text
   GET http://localhost:8080/api/health
   ```

9. 确认状态是 `200 OK`，响应体为：

   ```json
   {"status":"ok"}
   ```

10. 保存请求到 Postman Collection 的 `Health` 文件夹。

完成后把以下三样内容发给我：测试文件、控制器文件、Postman 响应。我会帮你做第一次代码审阅。

---

## 4. 后续会话路线图

后面的功能遵循相同节奏。每个会话只选一项，不要跳跃。

| 顺序 | 本次只完成什么 | 完成标志 |
|---|---|---|
| 3 | 统一错误响应 | 不合法参数得到固定 JSON 错误格式。 |
| 4 | 分类实体与读取接口 | Postman 能读取默认分类。 |
| 5 | 创建、修改、停用分类 | 停用分类不能用于新增交易。 |
| 6 | 新增收支记录 API | Postman 能创建收入和支出，并写入 MySQL。 |
| 7 | 查询、筛选、编辑、删除交易 | API 排序与筛选正确，删除返回 204。 |
| 8 | React 路由与首页静态布局 | 首页是日期分组的收支列表，不是仪表盘。 |
| 9 | React 调用分类与交易 API | 浏览器能新增、编辑、删除真实记录。 |
| 10 | 预算 API 和预算页 | 能设置月总预算和分类预算。 |
| 11 | 统计 API 和图表页 | 切换月份后汇总和图表同步变化。 |
| 12 | README、Postman Collection、手机局域网访问 | 按 README 可在新环境复现。 |

每个会话开始前，阅读 [开发计划](development-plan.md) 中对应任务；它列出了准确的文件名、测试与接口。每个会话结束时，把这三件事发给我：

1. 你修改的文件；
2. 测试或 Postman 的结果；
3. 一个你最不理解的点。

## 5. 常见情况与处理方式

| 现象 | 先检查什么 | 不要做什么 |
|---|---|---|
| Maven 下载很久 | 网络、Maven 下载进度、JDK 版本 | 不要删除 `pom.xml` 的依赖。 |
| Spring Boot 连不上 MySQL | phpStudy 是否启动、端口、数据库名、用户名 | 不要改用 `root` 或关闭密码。 |
| Flyway 报 SQL 语法错 | 错误中指向的迁移文件行号 | 不要删除 Flyway 历史表。 |
| Postman 404 | 后端是否启动、URL 与 Controller 映射 | 不要先改 React。 |
| 浏览器跨域错误 | 开发阶段的 Vite 代理或 Spring CORS 配置 | 不要直接关闭所有安全配置。 |
| 前端显示空白 | 浏览器控制台、Network 请求状态 | 不要先猜后端数据库坏了。 |

## 6. 现在应该做什么

从“检查点 0”开始，而不是直接创建业务代码。完成检查点 0 后，把 PowerShell 输出发给我，并说“检查点 0 完成”；我会只给你检查点 1 的下一步。
