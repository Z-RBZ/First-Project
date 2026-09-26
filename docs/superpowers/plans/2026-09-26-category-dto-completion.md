# 分类 DTO 层收尾：今日学习计划

## 今天的目标

完成分类管理 API 所需的 4 个 DTO，使后续的 Service 和 Controller 能分别处理“创建、修改、启用/停用、返回分类数据”。今天不写 Service、Controller 或 API 路由，避免把“数据形状”和“业务规则”混在一起学习。

## 为什么今天先做 DTO

浏览器或 Postman 发送的 JSON 不能直接交给数据库实体 `Category`，否则客户端可能传入不该修改的 `id`、`type` 等字段。DTO 是接口边界：请求 DTO 只接收允许输入的数据，响应 DTO 只返回允许展示的数据。

```text
Postman / 前端 JSON
        ↓
请求 DTO（今天完成）
        ↓
Service（后续实现业务规则）
        ↓
Category Entity ↔ MySQL categories 表
        ↓
响应 DTO（今天完成）
        ↓
Postman / 前端 JSON
```

## 今天会处理的文件及职责

| 文件 | 今天的状态 | 它负责什么 |
|---|---|---|
| `CategoryResponse.java` | 补全 | 将已有分类的 id、名称、类型、状态、排序安全返回给客户端。 |
| `CreateCategoryRequest.java` | 复查并修正 | 接收创建分类的数据，校验名称、类型和排序。 |
| `UpdateCategoryRequest.java` | 新建 | 接收修改名称和排序的数据；故意不允许改 type。 |
| `UpdateCategoryStatusRequest.java` | 新建 | 接收启用或停用分类的数据。 |

## 今天的任务顺序

### 任务 1：补全 `CategoryResponse`

**为什么先做：** 先明确“分类成功返回时长什么样”，后续 Service 才能将 Entity 转换成稳定的 API 返回数据。

**文件作用：** `CategoryResponse` 是输出 DTO，只用于后端返回数据，不用 setter，因为客户端不能通过返回对象修改数据库。

**要完成的内容：**

1. 导入 `CategoryType`。
2. 添加字段：`Long id`、`String name`、`CategoryType type`、`boolean active`、`int sortOrder`。
3. 添加一个接收这五个字段的构造方法。
4. 添加这五个字段的 getter，不添加 setter。

**完成标准：** 这个类能完整表达一条分类的返回数据，且没有对外的修改方法。

### 任务 2：修正 `CreateCategoryRequest` 的名称校验

**为什么现在修：** 当前 `name` 使用了 `@NotNull`，它只能拒绝 `null`，但会允许空字符串和全空格；分类名称应当真实存在。

**文件作用：** `CreateCategoryRequest` 是创建分类时的输入 DTO，后续 Controller 用 `@Valid` 检查它。

**要完成的内容：**

1. 将 `name` 上的 `@NotNull` 改为 `@NotBlank`。
2. 保留 `@Size(max = 50)`。
3. 保留 type 的 `@NotNull` 和 sortOrder 的 `@Min(0)`。

**完成标准：** 名称为 `null`、空字符串或全空格时都不合法；类型和排序的已有校验不受影响。

### 任务 3：创建 `UpdateCategoryRequest`

**为什么此时创建：** 修改分类与创建分类不同。设计规定创建后不可修改收入/支出类型，因此更新请求中不能包含 `type`。

**文件作用：** 它是 `PUT /api/categories/{id}` 的输入 DTO，只允许修改名称和排序。

**要完成的内容：**

1. 新建 `UpdateCategoryRequest.java`。
2. 添加 `String name` 与 `int sortOrder`。
3. 为 name 添加 `@NotBlank`、`@Size(max = 50)`；为 sortOrder 添加 `@Min(0)`。
4. 添加无参构造方法、getter、setter。

**完成标准：** 该类不含 id、type、active；以后使用它就无法从 API 请求中修改这些字段。

### 任务 4：创建 `UpdateCategoryStatusRequest`

**为什么单独一个类：** 启用/停用只修改 active，一次只处理一种明确操作。独立 DTO 让接口和业务意图更清楚。

**文件作用：** 它是 `PATCH /api/categories/{id}/status` 的输入 DTO。

**要完成的内容：**

1. 新建 `UpdateCategoryStatusRequest.java`。
2. 使用 `Boolean active`，而不是 `boolean active`。
3. 给 active 添加 `@NotNull`。
4. 添加无参构造方法、getter、setter。

**为什么使用 `Boolean`：** Java 的 `boolean` 没传值时会自动变成 `false`，无法分辨“用户明确要求停用”和“用户根本没有传 active”。`Boolean` 可为 null，`@NotNull` 能准确拒绝漏传字段。

**完成标准：** 请求必须明确传 `true` 或 `false`，漏传 active 会在进入 Service 前被识别为不合法。

### 任务 5：启动验证并检查 DTO 边界

**为什么最后验证：** 先保证所有 DTO 能编译，再让 Spring Boot 加载完整项目；这能及早发现拼写、导入或注解错误。

**要完成的内容：**

1. 在 IDEA 中运行 `BackendApplication`，或使用 Maven Wrapper 运行测试。
2. 确认后端正常启动，没有编译错误。
3. 最后核对：请求 DTO 不含 id；修改 DTO 不含 type；响应 DTO 不含 setter。

**完成标准：** DTO 层完成，下一次可以开始 `CategoryService`，专门学习查询、创建、重复名称检查等业务规则。

## 今天的第一步

从任务 1 开始：补全 `CategoryResponse` 的五个字段。完成后再添加构造方法和 getter，不要同时开始其他 DTO。
