# 教学事务管理系统技术与架构方案

## 1. 项目目标

本项目为一个基于 B/S 架构的教学事务管理系统。系统面向系统管理员、教师、学生三类用户，提供课程管理、开课管理、学生选课、成绩录入、信息查询、统计分析等功能。

系统建设基于已有 MySQL `school` 数据库，在原有数据库基础上进行扩展。数据库修改原则为：只添加新表、新字段、新索引、新视图、新触发器、新存储过程等对象，不删除已有表和字段。

## 2. 总体架构

系统采用前后端分离架构：

```text
浏览器
  |
  | HTTP / JSON
  |
Vue 前端项目
  |
  | REST API
  |
Spring Boot 后端服务
  |
  | MyBatis-Plus
  |
MySQL school 数据库
```

### 2.1 前端

前端采用 `Vue 3 + Vite + Element Plus` 实现，负责页面展示、用户交互、表单提交、接口调用、路由控制与基础权限拦截。

前端主要职责：

- 提供登录、退出、个人信息展示页面，维护当前会话状态。
- 根据角色（管理员、教师、学生）动态展示菜单与可访问路由。
- 提供管理员、教师、学生三类业务页面（表格、表单、统计图表容器）。
- 统一封装接口请求，调用后端 REST API 获取和提交数据。
- 统一处理加载状态、成功提示、错误提示和异常页面（403、404）。
- 基于 Element Plus 实现一致的界面风格与表单校验体验。

### 2.2 后端

后端采用 `Java + Spring Boot` 实现，作为独立 API 服务运行。

后端主要职责：

- 提供 REST API。
- 实现系统业务逻辑。
- 进行角色权限判断。
- 通过 MyBatis-Plus 访问 MySQL。
- 调用数据库中的存储过程。
- 使用数据库触发器完成部分自动化数据维护。

### 2.3 数据库

数据库采用已有 MySQL `school` 数据库。

当前可作为核心业务基础的表：

| 表名 | 用途 |
| --- | --- |
| `department` | 院系信息 |
| `student` | 学生信息 |
| `teacher` | 教师信息 |
| `course` | 课程信息 |
| `class` | 学期开课信息 |
| `course_selection` | 学生选课与成绩信息 |

当前也存在传统教学示例表：

| 表名 | 用途 |
| --- | --- |
| `s` | 示例学生表 |
| `c` | 示例课程表 |
| `sc` | 示例选课成绩表 |

后续系统开发优先围绕 `department`、`student`、`teacher`、`course`、`class`、`course_selection` 这组表扩展。

## 3. 技术选型

| 层次 | 技术 |
| --- | --- |
| 前端框架 | Vue 3 |
| 前端 UI 组件库 | Element Plus |
| 前端路由 | Vue Router |
| 前端状态管理 | Pinia |
| 前端 HTTP 客户端 | Axios |
| 前端构建工具 | Vite |
| 后端语言 | Java |
| 后端框架 | Spring Boot |
| Web 接口 | Spring MVC / REST API |
| ORM / 数据访问 | MyBatis-Plus |
| 数据库 | MySQL |
| 数据传输格式 | JSON |
| 后端构建工具 | Maven |
| 前端包管理 | npm |

暂不引入的组件：

- 暂不引入 Spring Security。
- 暂不引入 Redis。
- 暂不引入微服务组件。
- 暂不引入消息队列。

角色权限先采用后端自定义登录校验和角色判断实现，保持系统复杂度可控。

### 3.1 前端工程目录建议（Vue 3 + Element Plus）

```text
frontend
  ├── public
  ├── src
  │   ├── api
  │   │   ├── auth.ts
  │   │   ├── admin.ts
  │   │   ├── student.ts
  │   │   ├── teacher.ts
  │   │   └── statistics.ts
  │   ├── assets
  │   ├── components
  │   │   ├── common
  │   │   └── business
  │   ├── layout
  │   ├── router
  │   │   ├── index.ts
  │   │   └── guards.ts
  │   ├── stores
  │   │   ├── auth.ts
  │   │   ├── app.ts
  │   │   └── dict.ts
  │   ├── utils
  │   │   ├── request.ts
  │   │   ├── storage.ts
  │   │   └── constants.ts
  │   ├── views
  │   │   ├── login
  │   │   ├── admin
  │   │   ├── student
  │   │   ├── teacher
  │   │   └── error
  │   ├── App.vue
  │   └── main.ts
  ├── .env.development
  ├── .env.production
  ├── package.json
  └── vite.config.ts
```

目录划分原则：

- `views` 只放页面级组件（路由入口）。
- `components` 放可复用业务组件和通用组件。
- `api` 按角色与业务域拆分接口函数，不在页面里直接写请求地址。
- `stores` 统一维护全局状态，避免组件层层传参。
- `utils/request.ts` 统一处理请求头、响应解析、错误提示。

### 3.2 前端分层设计

前端建议采用“页面层 + 组件层 + 状态层 + 接口层 + 基础设施层”结构：

1. 页面层（`views`）：组织页面布局、绑定交互事件，不直接写底层请求细节。
2. 组件层（`components`）：沉淀可复用业务组件，例如课程选择表格、成绩录入弹窗。
3. 状态层（`stores`）：保存用户身份、菜单状态、全局字典数据等跨页面共享状态。
4. 接口层（`api`）：按接口契约封装 API 函数，对齐 `api_contract_basic.md`。
5. 基础设施层（`utils`、`router`）：封装 Axios 实例、路由守卫、常量与存储工具。

这种分层方式与后端的 Controller / Service / Mapper 思路一致，便于后端开发者理解与维护。

### 3.3 路由与权限控制（前端侧）

路由建议按“公共路由 + 角色路由”组织：

- 公共路由：`/login`、`/403`、`/404`。
- 管理员路由前缀：`/admin/*`。
- 教师路由前缀：`/teacher/*`。
- 学生路由前缀：`/student/*`。

每个业务路由在 `meta` 中标记允许角色，例如：

```text
meta: { roles: ['ADMIN'] }
```

守卫流程建议：

1. 进入路由前读取 `authStore` 中的登录状态和角色。
2. 未登录用户统一跳转登录页。
3. 已登录但角色不匹配时跳转 403 页面。
4. 前端仅做体验层拦截，真正权限校验仍以后端接口返回结果为准。

### 3.4 状态管理方案（Pinia）

建议最少建立以下 Store：

| Store | 主要状态 | 说明 |
| --- | --- | --- |
| `authStore` | `token`、`role`、`userId`、`profile` | 登录态和用户信息 |
| `appStore` | `sidebarCollapsed`、`activeMenu`、`breadcrumbs` | 布局和导航状态 |
| `dictStore` | `departments`、`semesters` 等基础字典 | 减少重复请求 |

会话持久化建议：

- `token`、`role` 可持久化到 `localStorage` 或 `sessionStorage`。
- 页面刷新后通过 `/api/auth/profile` 重新拉取用户信息。
- 敏感权限判断必须以后端为准，前端持久化仅用于体验优化。

### 3.5 接口调用与错误处理规范

统一在 `utils/request.ts` 创建 Axios 实例，约定：

- `baseURL` 使用 `/api`，通过 Vite 代理转发到后端 `http://localhost:8080`。
- 请求拦截器统一附加登录凭证和角色标识（按后端契约字段传递）。
- 响应拦截器按后端 `Result<T>` 结构统一判断 `code` 和 `message`。
- 对常见错误（401、403、500、网络超时）给出统一提示并按需跳转。

接口文件组织建议：

- `api/auth.ts`：登录、退出、个人信息。
- `api/admin.ts`：管理员数据维护相关接口。
- `api/student.ts`：选课、退课、成绩、统计接口。
- `api/teacher.ts`：授课班级、成绩录入、教学统计接口。
- `api/statistics.ts`：跨角色统计查询接口（如后续需要）。

### 3.6 页面模块规划（对应三类角色）

| 角色 | 页面模块建议 |
| --- | --- |
| 系统管理员 | 学生管理、教师管理、课程管理、开课管理、选课结果查询、统计分析 |
| 教师 | 授课课程列表、课程学生名单、成绩录入/修改、教学统计 |
| 学生 | 开课查询、我的选课、退课、成绩查询、个人学分统计 |

通用页面建议：

- 登录页、首页仪表盘、个人中心、403 页面、404 页面。
- 列表页优先使用 Element Plus 的 `el-table + el-pagination`。
- 表单页优先使用 `el-form` 及内置校验规则。

### 3.7 前端开发顺序建议（适合后端开发者）

1. 初始化 `Vue 3 + Vite + TypeScript` 项目，安装 `element-plus`、`vue-router`、`pinia`、`axios`。
2. 搭建基础布局（顶部/侧边栏）与路由骨架，先完成登录、退出、403、404。
3. 封装请求层（Axios）与统一错误处理，对接 `auth` 相关接口。
4. 完成管理员模块基础增删改查页面，优先打通“列表 + 表单 + 分页”通用模式。
5. 完成学生模块（开课查询、选课、退课、成绩与统计）。
6. 完成教师模块（授课查询、学生名单、成绩录入与统计）。
7. 统一优化交互细节（加载态、空状态、二次确认、表单校验）并开展联调。

## 4. 后端 MVC 架构设计

后端采用经典 MVC 分层架构。

```text
Controller 层
  |
Service 层
  |
Mapper 层
  |
MySQL 数据库
```

### 4.1 Controller 层

Controller 层负责接收前端请求、校验基础参数、调用 Service，并返回统一 JSON 结果。

示例职责：

- `AuthController`：登录、退出、获取当前用户信息。
- `AdminController`：管理员相关操作入口。
- `StudentController`：学生相关操作入口。
- `TeacherController`：教师相关操作入口。
- `CourseController`：课程与开课管理。
- `SelectionController`：学生选课、退课。
- `GradeController`：成绩查询与成绩录入。
- `StatisticsController`：统计分析。

### 4.2 Service 层

Service 层负责核心业务逻辑，是系统最重要的业务层。

示例职责：

- 判断用户角色是否有权限执行某个操作。
- 判断学生是否可以选课。
- 判断课程是否已经重复选择。
- 判断教师是否可以录入某门课的成绩。
- 计算学生已修学分、平均分、通过课程数量。
- 组织统计分析数据。
- 调用 Mapper 查询数据库。
- 调用存储过程完成特定统计。

### 4.3 Mapper 层

Mapper 层负责数据库访问，使用 MyBatis-Plus 简化 CRUD 操作。

常规单表操作可使用 MyBatis-Plus 提供的基础方法；复杂联表查询、统计分析、存储过程调用可编写自定义 SQL。

示例 Mapper：

- `StudentMapper`
- `TeacherMapper`
- `DepartmentMapper`
- `CourseMapper`
- `ClassMapper`
- `CourseSelectionMapper`
- `StatisticsMapper`

### 4.4 Entity / DTO / VO

后端建议区分以下对象：

| 类型 | 用途 |
| --- | --- |
| Entity | 与数据库表结构对应 |
| DTO | 接收前端请求参数 |
| VO | 返回给前端的展示数据 |

示例：

- `Student`：对应 `student` 表。
- `CourseSelection`：对应 `course_selection` 表。
- `LoginDTO`：接收登录账号、密码、角色。
- `CourseVO`：返回课程名称、学分、任课教师、上课时间等展示信息。
- `GradeVO`：返回学生成绩、课程名称、学分等展示信息。

## 5. 后端包结构建议

```text
com.ims
  ├── ImsApplication.java
  ├── common
  │   ├── Result.java
  │   ├── PageResult.java
  │   └── BusinessException.java
  ├── config
  │   ├── WebConfig.java
  │   └── MybatisPlusConfig.java
  ├── controller
  │   ├── AuthController.java
  │   ├── AdminController.java
  │   ├── StudentController.java
  │   ├── TeacherController.java
  │   ├── CourseController.java
  │   ├── SelectionController.java
  │   ├── GradeController.java
  │   └── StatisticsController.java
  ├── service
  │   ├── AuthService.java
  │   ├── StudentService.java
  │   ├── TeacherService.java
  │   ├── CourseService.java
  │   ├── SelectionService.java
  │   ├── GradeService.java
  │   └── StatisticsService.java
  ├── service.impl
  ├── mapper
  ├── entity
  ├── dto
  ├── vo
  └── util
```

该结构保持 MVC 清晰，同时适合课程项目展示和后续编码。

## 6. 用户角色与权限设计

系统包含三类角色：

| 角色 | 说明 |
| --- | --- |
| 系统管理员 | 维护基础数据、管理课程、开课、用户、统计分析 |
| 教师 | 查看授课课程、查看学生名单、录入成绩、教学统计 |
| 学生 | 查询课程、自主选课、退课、查询成绩和个人统计 |

### 6.1 权限实现方式

由于暂不引入 Spring Security，权限控制采用轻量方式实现：

1. 用户登录成功后，后端返回用户身份信息和登录凭证。
2. 前端保存当前用户信息。
3. 前端请求 API 时携带用户标识和角色信息。
4. 后端在 Controller 或 Service 中进行角色校验。
5. 无权限访问时返回统一错误结果。

后续如果项目复杂度提高，可以再升级为基于拦截器、Token 或 Spring Security 的权限控制。

### 6.2 账号设计

已有 `student` 和 `teacher` 表中没有密码字段。为避免破坏原表结构，建议后续新增统一账号表：

```text
user_account
```

建议字段：

| 字段 | 说明 |
| --- | --- |
| `user_id` | 用户账号 ID |
| `username` | 登录名 |
| `password` | 登录密码 |
| `role` | 角色：ADMIN、TEACHER、STUDENT |
| `related_id` | 关联的学生编号或教师编号 |
| `status` | 账号状态 |
| `created_at` | 创建时间 |
| `updated_at` | 更新时间 |

这样既能满足登录需求，又符合“只能添加，不能删除”的数据库要求。

## 7. 功能模块设计

### 7.1 登录与用户模块

功能：

- 用户登录。
- 用户退出。
- 查询当前用户信息。
- 根据角色返回菜单。

核心接口示例：

```text
POST /api/auth/login
GET  /api/auth/profile
POST /api/auth/logout
```

### 7.2 管理员模块

功能：

- 学生信息管理。
- 教师信息管理。
- 院系信息管理。
- 课程信息管理。
- 学期开课管理。
- 选课结果查询。
- 成绩查询。
- 统计分析。

核心接口示例：

```text
GET    /api/admin/students
POST   /api/admin/students
PUT    /api/admin/students/{studentId}

GET    /api/admin/teachers
POST   /api/admin/teachers
PUT    /api/admin/teachers/{staffId}

GET    /api/admin/courses
POST   /api/admin/courses
PUT    /api/admin/courses/{courseId}

GET    /api/admin/classes
POST   /api/admin/classes
PUT    /api/admin/classes/{semester}/{courseId}/{staffId}
```

### 7.3 学生模块

功能：

- 查询当前学期开课列表。
- 自主选课。
- 退课。
- 查询本人已选课程。
- 查询本人成绩。
- 查询本人学分统计。

核心接口示例：

```text
GET    /api/student/classes
GET    /api/student/selections
POST   /api/student/selections
DELETE /api/student/selections
GET    /api/student/grades
GET    /api/student/statistics
```

### 7.4 教师模块

功能：

- 查询本人授课课程。
- 查询课程学生名单。
- 录入或修改成绩。
- 查询成绩分布。
- 查询教学统计。

核心接口示例：

```text
GET /api/teacher/classes
GET /api/teacher/classes/{semester}/{courseId}/students
PUT /api/teacher/grades
GET /api/teacher/statistics
```

### 7.5 统计分析模块

不同角色提供不同统计信息：

管理员统计：

- 学院学生人数统计。
- 课程选课人数统计。
- 教师授课数量统计。
- 各课程平均分、最高分、最低分。
- 不及格人数统计。

教师统计：

- 本人课程选课人数。
- 本人课程成绩分布。
- 本人课程平均分。
- 未录入成绩人数。

学生统计：

- 已选课程数量。
- 已获得学分。
- 平均成绩。
- 通过课程数量。
- 不及格课程数量。

## 8. 数据库扩展设计

项目必须基于已有 `school` 数据库扩展，不能删除已有结构。

### 8.1 建议新增表

#### `user_account`

用于系统登录和角色识别。

#### `operation_log`

用于记录重要操作，例如登录、选课、退课、成绩修改。

#### `course_selection_log`

用于记录学生选课、退课日志，可配合触发器使用。

#### `grade_log`

用于记录成绩录入和修改历史，可配合触发器使用。

### 8.2 建议新增字段

可根据业务需要为 `class` 表新增字段：

| 字段 | 说明 |
| --- | --- |
| `capacity` | 课程容量 |
| `selected_count` | 已选人数 |
| `status` | 开课状态 |

这些字段有助于实现自主选课时的容量限制。

## 9. 触发器与存储过程设计

项目要求数据库中至少包含一个触发器和一个存储过程，并在系统中使用和调用。

### 9.1 触发器建议

触发器名称示例：

```text
trg_after_course_selection_insert
```

用途：

- 学生选课成功后，自动写入选课日志。
- 或自动更新开课表中的已选人数。

触发时机：

```text
AFTER INSERT ON course_selection
```

### 9.2 存储过程建议

存储过程名称示例：

```text
sp_get_student_credit_summary
```

用途：

- 根据学生编号统计该学生已通过课程数、已获得学分、平均成绩。

后端调用位置：

- `StatisticsMapper`
- `StatisticsService`
- 学生端统计接口 `/api/student/statistics`

这样可以在系统中明确体现“存储过程被后端调用”。

## 10. API 返回格式

后端建议统一返回 JSON 格式。

成功示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

失败示例：

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

建议封装统一返回类：

```text
Result<T>
```

## 11. 异常处理设计

后端建议使用统一异常处理机制。

常见异常类型：

- 参数错误。
- 用户未登录。
- 用户无权限。
- 数据不存在。
- 重复选课。
- 课程容量已满。
- 成绩不合法。

通过统一异常处理返回标准 JSON，避免每个接口重复处理错误响应。

## 12. 后端开发顺序建议

建议按以下顺序开发：

1. 搭建 Spring Boot 项目。
2. 配置 MySQL 连接。
3. 引入 MyBatis-Plus。
4. 创建 Entity、Mapper。
5. 实现统一返回结果和异常处理。
6. 实现登录与角色识别。
7. 实现管理员基础数据管理。
8. 实现学生选课与查询。
9. 实现教师成绩录入。
10. 实现统计分析。
11. 新增并调用存储过程。
12. 新增并验证触发器。
13. 与 Vue 3 + Element Plus 前端联调。

## 13. 最终推荐方案

本项目最终推荐采用：

```text
Vue 3 + Element Plus 前端 + Spring Boot 后端 REST API + MVC 架构 + MyBatis-Plus + MySQL
```

后端保持单体应用结构，不引入复杂中间件。系统通过 Controller、Service、Mapper 三层完成业务开发，通过 MySQL 存储过程和触发器满足数据库课程设计要求，通过角色字段和后端业务判断实现系统管理员、教师、学生三类权限控制。

该方案开发成本适中，结构清晰，符合 B/S 架构要求，也便于后续课程答辩时说明系统设计思路。
