# 分类页面第一版开发计划

> **执行说明：** 本计划用于手把手完成分类页面第一版。每个任务完成后再进入下一项；由学习者编写代码，Codex 负责解释、检查和排错。

**目标：** 创建 React + TypeScript 分类管理页面，实现分类列表、收入/支出筛选和新增分类的完整前后端链路。

**实现思路：** React 页面通过 `categoryApi` 请求 Spring Boot。后端保留现有分类查询接口，增加新增分类接口与本地 CORS 配置；页面按视觉稿展示列表和表单，新建成功后重新查询列表。

**技术栈：** React、TypeScript、Vite、npm、Spring Boot、Maven、MySQL、VS Code、IntelliJ IDEA。

**设计依据：** `docs/frontend-category-page-design.md`

## 全局约束

- 前端工程固定在 `E:\CodexLearnPlan\projects\personal-finance-tracker\frontend`。
- 前端使用 React + TypeScript + Vite，包管理器使用 npm。
- 后端开发地址为 `http://localhost:8080`，前端开发地址为 `http://localhost:5173`。
- 现有查询地址保持为 `GET /get/category`；本次新增 `POST /get/category`。
- 不实现编辑、删除、启用/停用分类，也不修改已执行的 Flyway V1 迁移文件。
- 每个阶段结束只做一次有意义的验证；出现报错时先根据报错定位，不靠猜测修改。

## 今天的成果与完成标准

浏览器打开前端页面后，能够看到数据库中的分类；点击“收入”或“支出”能正确筛选；提交一个合法新分类后，页面列表和 MySQL 都能看到新数据；同类型重名时显示中文错误信息。

---

### 任务 0：恢复并验证本地运行环境

**为什么现在做：** 前端页面依赖后端和 MySQL 返回真实数据。上次收尾测试失败的原因是 MySQL 未运行，先处理环境能避免后续把连接问题误认为代码问题。

**涉及位置：**

- phpStudy：启动 MySQL 服务；
- `backend/`：运行 Maven 测试和 Spring Boot；
- 浏览器：确认现有 `GET /get/category` 可访问。

**操作：**

1. 打开 phpStudy，确认 MySQL 状态为“运行中”。
2. 在 PowerShell 进入 `backend`，执行：

   ```powershell
   $env:JAVA_HOME='D:\JavaJDK'
   $env:Path='D:\JavaJDK\bin;' + $env:Path
   .\mvnw.cmd test
   ```

3. 在 IntelliJ IDEA 启动后端后，浏览器访问 `http://localhost:8080/get/category`。

**完成标准：** Maven 输出 `BUILD SUCCESS`，浏览器显示分类 JSON 数组。

---

### 任务 1：初始化 React + TypeScript 前端工程

**为什么现在做：** `frontend/` 目前只有目录，没有 `package.json`、React 入口或运行脚本；Vite 模板会一次性创建这些标准基础设施。

**涉及文件及职责：**

- `frontend/package.json`：记录 React、Vite 依赖和 `npm run dev` 等命令；
- `frontend/index.html`：Vite 的网页入口；
- `frontend/src/main.tsx`：把 React 应用挂载到网页；
- `frontend/src/App.tsx`：当前应用根组件；
- `frontend/src/`：前端源代码目录。

**操作：** 在 `frontend` 目录执行 Vite 的 React + TypeScript 模板创建命令。若 Vite 提示目标目录不是空目录，先停下来检查提示；现有目录中没有用户代码，不能不看提示就选择删除。

**完成标准：** `npm install` 完成，`npm run dev` 能启动 Vite，并显示本地访问地址。

---

### 任务 2：建立分类前端的数据类型与请求层

**为什么现在做：** 页面组件不应该各自拼接 URL 或重复写请求逻辑。先把“分类是什么”和“怎样请求后端”固定下来，后续页面只调用统一函数。

**涉及文件及职责：**

- `frontend/src/types/category.ts`：定义 `CategoryType`、`Category`、`CreateCategoryRequest`；
- `frontend/src/api/categoryApi.ts`：封装获取分类和新增分类的 `fetch` 请求。

**接口约定：**

- `getCategories(type?: CategoryType): Promise<Category[]>` 请求 `GET /get/category`；
- `createCategory(request: CreateCategoryRequest): Promise<Category>` 请求 `POST /get/category`；
- 后端业务错误返回时，读取 `message` 并转成前端可显示的错误文本。

**完成标准：** TypeScript 没有类型错误；页面组件可以只调用两个函数，不直接出现后端 URL。

---

### 任务 3：补齐后端的跨域与新增分类接口

**为什么现在做：** 浏览器默认会阻止 `5173` 端口的前端请求 `8080` 端口的后端；同时前端表单需要一个真正写入 MySQL 的接口，不能只做假数据。

**涉及文件及职责：**

- `backend/src/main/java/com/example/finance/common/config/WebConfig.java`：只允许 `http://localhost:5173` 调用本地分类接口；
- `backend/src/main/java/com/example/finance/category/service/CategoryService.java`：新增创建分类、检查同类型重名、保存实体、转换响应的逻辑；
- `backend/src/main/java/com/example/finance/category/controller/CategoryController.java`：新增 `POST /get/category`，接收 `CreateCategoryRequest`，返回 HTTP 201；
- `backend/src/main/java/com/example/finance/common/exception/GlobalExceptionHandler.java`：将 `DuplicateCategoryException` 转为清晰的 HTTP 409 响应；
- `backend/src/test/...`：为创建分类的核心规则添加或补充测试。

**完成标准：** Postman 或浏览器开发者工具能看到：合法请求返回 201；同类型重名返回 409 和中文提示。

---

### 任务 4：实现分类页面的三个组件

**为什么现在做：** 这一步把真实接口数据变成用户看得见、能操作的页面，是今天最直观的成果。

**涉及文件及职责：**

- `frontend/src/pages/CategoryPage.tsx`：保存当前筛选类型、列表数据、加载/成功/错误状态，并协调三个组件；
- `frontend/src/components/CategoryFilter.tsx`：显示“全部、收入、支出”并向父组件报告选择；
- `frontend/src/components/CategoryList.tsx`：显示分类名称、中文类型标签和排序值；
- `frontend/src/components/CategoryForm.tsx`：管理名称、类型、排序值输入，提交新增请求；
- `frontend/src/App.tsx`：在应用根位置显示 `CategoryPage`；
- `frontend/src/index.css`（或同等样式文件）：按视觉稿实现布局、卡片、间距和响应式单列规则。

**关键行为：**

1. 页面首次加载分类列表；
2. 点击筛选后请求相应类型的数据；
3. 新增成功后清空表单并重新加载列表；
4. 加载中、空列表、后端重名、网络失败都要有用户能看懂的提示。

**完成标准：** 页面布局与 `docs/assets/category-page-desktop-annotated.png` 的结构一致，能真实读取和新增 MySQL 分类。

---

### 任务 5：端到端验证与收尾

**为什么现在做：** 单独启动前端或后端不等于整条链路可用；最后要证明浏览器、Spring Boot 和 MySQL 能一起工作。

**检查清单：**

1. phpStudy MySQL 保持运行；
2. 后端的 `mvnw.cmd test` 通过；
3. 后端运行在 8080，前端运行在 5173；
4. 浏览器能加载分类列表；
5. “收入 / 支出”筛选正确；
6. 新增一个未使用的分类名后，列表立即更新；
7. 再次新增同类型同名称时，页面显示中文重名提示；
8. `npm run build` 成功，确认 TypeScript 与前端构建均无错误。

**完成标准：** 八项全部通过后再提交代码和更新当天学习总结。

## 注意事项

- 视觉稿中的颜色、图标和字体是实现参考；第一版优先保证布局和功能正确，图标可以先使用简单文本或 Emoji。
- 如果 Vite 初始化生成了默认 `src` 文件，再按本计划逐步替换，不需要保留示例计数器页面。
- 今天先不接入任何 UI 组件库，避免学习点过多；使用 React 自带能力和普通 CSS 完成页面。
