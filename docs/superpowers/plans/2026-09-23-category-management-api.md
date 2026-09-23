# 分类管理 API：后续学习计划

**阶段目标：** 完成分类的查询、创建、修改名称/排序、启用/停用 API，并能用 Postman 连接真实 MySQL 验证它们。

**设计依据：** `docs/design.md` 中的分类 API、业务约束和统一错误处理规则。

**本阶段暂不做：** 收支记录、预算、统计、React 页面、分类物理删除。分类有历史交易时只能停用，因此删除功能留到交易功能完成后再评估。

## 本阶段会新增或修改的文件

| 文件或类 | 何时创建 | 作用 |
|---|---|---|
| `Category.java` | 第一项修改 | 数据库实体；保持 `id` 由数据库生成、`type` 创建后不可修改。 |
| `CategoryRepository.java` | 第 3 阶段修改 | 数据库访问层；声明按类型/状态查询、检查名称重复的方法。 |
| `CreateCategoryRequest.java` | 第 2 阶段创建 | 接收“创建分类”请求的数据，并校验名称、类型和排序值。 |
| `UpdateCategoryRequest.java` | 第 2 阶段创建 | 接收“修改分类名称和排序”请求；不包含 type。 |
| `UpdateCategoryStatusRequest.java` | 第 2 阶段创建 | 接收“启用或停用分类”的请求。 |
| `CategoryResponse.java` | 第 2 阶段创建 | 向 API 调用方返回分类信息；不直接暴露 Entity。 |
| `CategoryService.java` | 第 5 阶段创建 | 放业务规则：名称不能重复、找不到分类、类型不可改。 |
| `CategoryNotFoundException.java` | 第 4 阶段创建 | 表示请求了不存在的分类。 |
| `DuplicateCategoryException.java` | 第 4 阶段创建 | 表示同一种类型下创建了重复名称。 |
| `GlobalExceptionHandler.java` | 第 4 阶段创建 | 把业务异常和参数校验异常转换为统一 JSON 错误响应。 |
| `CategoryController.java` | 第 6 阶段创建 | 接收 HTTP 请求，调用 Service 并返回 DTO。 |
| Postman Collection | 第 7 阶段创建 | 保存可重复使用的手工 API 验收请求。 |

## 最终接口清单

| 方法 | 路径 | 作用 | 成功状态 |
|---|---|---|---|
| `GET` | `/api/categories` | 按可选的 type、active 查询分类 | 200 |
| `POST` | `/api/categories` | 创建分类 | 201 |
| `PUT` | `/api/categories/{id}` | 修改名称和排序 | 200 |
| `PATCH` | `/api/categories/{id}/status` | 启用或停用分类 | 200 |

## 业务规则

1. `type` 只能是 `INCOME` 或 `EXPENSE`。
2. 同一种 type 内，分类名称不能重复；收入“餐饮”和支出“餐饮”可以同时存在。
3. 创建后不允许修改 `type`。
4. 新分类默认启用。
5. 找不到 id 时返回 404。
6. 重复名称时返回 409。
7. 名称为空、过长或排序小于 0 时返回 400。

---

## 阶段 1：修正实体的职责边界（现在开始）

**目的：** 让 `Category` 只允许符合设计的状态变化，避免后续 Service 无意中修改数据库主键或分类类型。

**修改文件：**

`backend/src/main/java/com/example/finance/category/entity/Category.java`

### 步骤

1. 删除 `setId(Long id)` 方法。
   - `id` 是 MySQL `AUTO_INCREMENT` 生成的主键，业务代码不应修改它。
2. 删除 `setType(CategoryType type)` 方法。
   - 设计已确认：分类创建后类型不可改。
3. 保留 `setName`、`setActive`、`setSortOrder`。
   - 它们分别对应本阶段允许的“修改名称”“启用/停用”“修改排序”。
4. 运行 `BackendApplication`。

**完成标准：** 后端正常启动，且 `Category` 只暴露符合业务规则的修改方法。

---

## 阶段 2：定义 API 输入和输出 DTO

**目的：** 把“网页或 Postman 传来的数据”和“数据库实体”隔开。Controller 不应直接接收或返回 `Category` 实体。

**创建包：**

`com.example.finance.category.dto`

### DTO 字段设计

| DTO | 字段 | 作用 |
|---|---|---|
| `CreateCategoryRequest` | `name`、`type`、`sortOrder` | 创建分类时的请求体。 |
| `UpdateCategoryRequest` | `name`、`sortOrder` | 修改分类时的请求体；故意不提供 type。 |
| `UpdateCategoryStatusRequest` | `active` | 启用或停用分类时的请求体。 |
| `CategoryResponse` | `id`、`name`、`type`、`active`、`sortOrder` | 返回给前端或 Postman 的分类数据。 |

### 校验规则

- `name`：使用 `@NotBlank`，最大长度 50。
- `type`：创建时使用 `@NotNull`。
- `sortOrder`：使用 `@Min(0)`。
- `active`：使用 `@NotNull`，必须明确传 true 或 false。

**完成标准：** DTO 能表达四个接口所需的数据，且不允许客户端修改分类 type。

---

## 阶段 3：补充 Repository 查询方法

**目的：** 让 Service 可以按筛选条件读取分类，并检查名称是否重复；Repository 只表达数据库访问，不放业务判断。

**修改文件：**

`backend/src/main/java/com/example/finance/category/repository/CategoryRepository.java`

### 需要的方法

```java
List<Category> findAllByOrderBySortOrderAscIdAsc();

List<Category> findByTypeOrderBySortOrderAscIdAsc(CategoryType type);

List<Category> findByActiveOrderBySortOrderAscIdAsc(boolean active);

List<Category> findByTypeAndActiveOrderBySortOrderAscIdAsc(
        CategoryType type,
        boolean active
);

boolean existsByTypeAndName(CategoryType type, String name);

boolean existsByTypeAndNameAndIdNot(CategoryType type, String name, Long id);
```

**完成标准：** 项目编译且启动成功；这些方法由 Spring Data JPA 根据方法名生成查询，不需要手写 SQL。

---

## 阶段 4：建立分类 API 的错误响应

**目的：** 让客户端得到清楚、一致的 400、404、409 JSON 错误，而不是 Spring 默认 HTML 错误页或数据库异常堆栈。

**创建文件：**

- `common/exception/CategoryNotFoundException.java`
- `common/exception/DuplicateCategoryException.java`
- `common/exception/GlobalExceptionHandler.java`
- `common/api/ApiError.java`

### 错误规则

| 情况 | HTTP 状态 | 错误代码 |
|---|---|---|
| 请求字段不合法 | 400 | `VALIDATION_ERROR` |
| 分类 id 不存在 | 404 | `CATEGORY_NOT_FOUND` |
| 同类型名称重复 | 409 | `DUPLICATE_CATEGORY_NAME` |

**完成标准：** Service 抛出业务异常后，Controller 返回 JSON 错误对象；不暴露堆栈或数据库密码等敏感信息。

---

## 阶段 5：实现 `CategoryService`

**目的：** 集中处理分类业务规则。Controller 只转发 HTTP，Repository 只访问数据库，规则全部留在 Service。

**创建文件：**

`backend/src/main/java/com/example/finance/category/service/CategoryService.java`

### 需要提供的业务方法

```java
List<CategoryResponse> getCategories(CategoryType type, Boolean active);

CategoryResponse createCategory(CreateCategoryRequest request);

CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

CategoryResponse updateCategoryStatus(Long id, UpdateCategoryStatusRequest request);
```

### 各方法必须完成的事

| 方法 | 必须做的业务操作 |
|---|---|
| `getCategories` | 根据 type 和 active 是否为空，选择对应 Repository 查询；按排序值、id 升序返回 DTO。 |
| `createCategory` | 检查同类型名称是否已存在；不存在则创建 Entity、设置排序并保存。 |
| `updateCategory` | 先按 id 查找；检查同类型下新名称是否与其他记录重复；只更新 name、sortOrder。 |
| `updateCategoryStatus` | 先按 id 查找；只更新 active。 |

**完成标准：** 分类所有业务规则都在 Service 中实现；Entity 的 `type` 从未被修改。

---

## 阶段 6：创建 `CategoryController`

**目的：** 把浏览器/Postman 的 HTTP 请求转换为对 `CategoryService` 的调用，并返回 JSON 数据。

**创建文件：**

`backend/src/main/java/com/example/finance/category/controller/CategoryController.java`

### 路由与 Service 的对应关系

| Controller 方法 | HTTP 映射 | 调用的 Service 方法 |
|---|---|---|
| `getCategories` | `GET /api/categories` | `getCategories` |
| `createCategory` | `POST /api/categories` | `createCategory` |
| `updateCategory` | `PUT /api/categories/{id}` | `updateCategory` |
| `updateCategoryStatus` | `PATCH /api/categories/{id}/status` | `updateCategoryStatus` |

所有请求 DTO 参数都要添加 `@Valid`，确保字段校验在进入 Service 前执行。

**完成标准：** 启动后端后，四个路径都能由 Postman 访问；返回 JSON 而不是 Whitelabel 错误页。

---

## 阶段 7：用 Postman 和 MySQL 验收

**目的：** 在接 React 前，确认 API、业务规则和真实数据库一起正常工作。

### 最小验收顺序

1. `POST /api/categories` 创建 `EXPENSE` 类型的“餐饮”。
2. 再创建同类型“餐饮”，确认返回 409。
3. `GET /api/categories?type=EXPENSE&active=true`，确认能查到“餐饮”。
4. `PUT /api/categories/{id}` 修改名称与排序，确认成功。
5. `PATCH /api/categories/{id}/status` 设为 false。
6. 再用 `GET` 加 `active=true` 查询，确认该分类不再出现。
7. 在 MySQL 中查询 `categories`，确认数据与接口返回一致。

**完成标准：** 四个分类 API 的正常分支、重复名称、找不到分类、字段不合法均有手工验证结果。

## 学习顺序

先完成阶段 1，再逐步进入阶段 2。不要跳过 DTO 或 Service，直接写 Controller；分层的目的就是让每个类只做一种事情，后面调试和加功能才不会混乱。
