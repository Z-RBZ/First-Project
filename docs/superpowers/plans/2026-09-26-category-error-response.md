# 分类统一错误响应：学习计划

## 本阶段目标

为分类 API 准备统一的 JSON 错误结构。后续 Controller 或 Service 出现参数不合法、分类不存在、名称重复等情况时，客户端能够得到明确的 HTTP 状态码、错误代码和中文提示，而不是 Whitelabel HTML 页面或数据库异常堆栈。

## 为什么现在做

Repository 已经能查询和检查重复名称。下一步写 Service 时会首次出现“找不到分类”和“名称重复”这两类业务错误；先定义异常和统一返回格式，Service 之后只需抛出合适的异常，不必自己处理 HTTP 响应。

本阶段暂不写 `CategoryService`、`CategoryController` 或 Postman 请求。没有 Controller 时，错误处理器还不能被浏览器直接触发；本阶段先完成基础结构和启动验证。

## 开始前的当前状态

创建本计划时，工作区中已经出现正在编写的 `ApiError.java`。继续学习前先打开该文件，对照“任务 2”的字段和职责检查；不要因为计划中的顺序而覆盖已经写好的内容。若它符合要求，就从尚未完成的测试或异常类继续。

## 错误返回格式

设计文档规定错误 JSON 使用以下结构：

```json
{
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "请求参数不合法",
  "fieldErrors": {
    "name": "分类名称不能为空"
  }
}
```

其中 `fieldErrors` 只在字段校验失败时提供；分类不存在、名称重复等业务错误不需要字段明细。

## 本阶段文件及职责

| 文件 | 是否新建 | 职责 |
|---|---|---|
| `common/api/ApiError.java` | 新建 | 统一的错误响应 DTO，向前端或 Postman 返回状态、错误代码、提示和字段错误。 |
| `common/exception/CategoryNotFoundException.java` | 新建 | 表示按 id 查不到分类的业务错误。 |
| `common/exception/DuplicateCategoryException.java` | 新建 | 表示同一种分类类型下名称重复的业务错误。 |
| `common/exception/GlobalExceptionHandler.java` | 新建 | 集中捕获异常，并把它们转换为 `ApiError` 和相应 HTTP 状态码。 |
| `src/test/java/.../common/exception/GlobalExceptionHandlerTest.java` | 新建 | 验证错误代码、状态码和信息不会被后续修改破坏。 |

目录位置：

```text
backend/src/main/java/com/example/finance/common/
├─ api/
│  └─ ApiError.java
└─ exception/
   ├─ CategoryNotFoundException.java
   ├─ DuplicateCategoryException.java
   └─ GlobalExceptionHandler.java
```

`common` 与 `category` 同级，因为未来交易、预算等模块也要复用同一套错误响应机制。

## 本阶段任务

### 任务 1：先写错误处理器的测试

**为什么先测试：** 先把“输入什么异常、应该得到什么状态码和错误代码”写清楚，避免代码写完后才发现 404、409 或错误结构不符合设计。

**要验证的场景：**

| 场景 | 预期 HTTP 状态 | 预期错误代码 |
|---|---:|---|
| 分类不存在 | 404 | `CATEGORY_NOT_FOUND` |
| 同类型分类名称重复 | 409 | `DUPLICATE_CATEGORY_NAME` |
| DTO 字段校验失败 | 400 | `VALIDATION_ERROR` |

**完成标准：** 测试能表达这三条规则；由于异常类和处理器尚未创建，第一次运行测试应因缺少对应代码而失败。

### 任务 2：创建 `ApiError`

**为什么现在做：** 它是所有错误响应的共同数据结构；异常处理器和测试都需要依赖它。

**类的字段：**

```java
int status;
String code;
String message;
Map<String, String> fieldErrors;
```

**设计要求：**

1. 为普通业务错误提供 `status`、`code`、`message` 的构造方法。
2. 为字段校验错误提供包含 `fieldErrors` 的构造方法。
3. 只提供 getter，不提供 setter；它是后端创建后返回给客户端的数据。

**完成标准：** 可以创建普通错误对象和带字段明细的校验错误对象；JSON 序列化能读取所有字段。

### 任务 3：创建两个分类业务异常

**为什么现在做：** Service 只需要表达“发生了什么业务问题”，不需要知道 HTTP 404 或 JSON 如何返回；这让 Service 与 Web 层保持分离。

**要创建的类：**

1. `CategoryNotFoundException extends RuntimeException`
   - 构造方法接收 `Long id`。
   - 异常信息表达“未找到 id 为 X 的分类”。
2. `DuplicateCategoryException extends RuntimeException`
   - 构造方法接收 `String name`。
   - 异常信息表达“同类型下已存在分类：X”。

**完成标准：** 两个类都只描述业务错误，不包含 `ResponseEntity`、HTTP 状态码或 Controller 注解。

### 任务 4：创建 `GlobalExceptionHandler`

**为什么现在做：** 这是唯一负责把 Java 异常翻译为 HTTP JSON 响应的位置。以后 Controller 不必在每个接口中重复写 `try-catch`。

**类职责：**

1. 使用 `@RestControllerAdvice` 让 Spring 在整个项目范围内应用它。
2. 捕获 `CategoryNotFoundException`，返回 404、`CATEGORY_NOT_FOUND`。
3. 捕获 `DuplicateCategoryException`，返回 409、`DUPLICATE_CATEGORY_NAME`。
4. 捕获 `MethodArgumentNotValidException`，返回 400、`VALIDATION_ERROR`，并把 DTO 注解中的字段提示放入 `fieldErrors`。
5. 捕获未知 `Exception`，返回 500、`INTERNAL_SERVER_ERROR` 和通用提示，不返回堆栈、SQL 或数据库账号信息。

**完成标准：** 每个处理方法返回 `ResponseEntity<ApiError>`；状态码、错误代码和 `ApiError` 字段与本计划前面的表格一致。

### 任务 5：运行测试和启动验证

**为什么最后验证：** 前面的测试需要先从失败变为通过；完整启动还能确认 Spring 能识别 `@RestControllerAdvice`，并发现导入、注解或方法签名错误。

**执行方式：**

1. 在 `backend` 目录运行 `mvnw.cmd test`。
2. 在 IDEA 启动 `BackendApplication`。
3. 确认 Maven 显示 `BUILD SUCCESS`，启动日志没有 `ERROR`。

**完成标准：** 测试全部通过、后端成功启动。实际的 Postman 404/409/400 验收，留到 Controller 完成后进行。

## 本阶段完成后应理解

1. DTO 校验失败和业务规则失败，都可以转换为统一 JSON，但它们发生的位置不同。
2. `RuntimeException` 用来表达业务错误；`GlobalExceptionHandler` 负责决定它对应哪个 HTTP 状态码。
3. Controller 不应重复写异常处理逻辑。
4. 不把数据库异常、堆栈或密码返回给客户端，是 API 的基本安全要求。

## 开始时的第一步

先创建测试包和 `GlobalExceptionHandlerTest.java`，写“分类不存在返回 404 和 `CATEGORY_NOT_FOUND`”这一条测试。确认它因相关类不存在而失败后，再开始创建 `ApiError`。
