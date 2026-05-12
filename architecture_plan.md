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

前端采用 `Vue` 实现，负责页面展示、用户交互、表单提交、接口调用和基础路由控制。

前端主要职责：

- 登录页面与用户身份保存。
- 根据角色展示不同菜单。
- 管理员、教师、学生对应页面。
- 调用后端 REST API 获取和提交数据。
- 展示课程、成绩、选课、统计分析等信息。

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
| 前端 | Vue |
| 后端语言 | Java |
| 后端框架 | Spring Boot |
| Web 接口 | Spring MVC / REST API |
| ORM / 数据访问 | MyBatis-Plus |
| 数据库 | MySQL |
| 数据传输格式 | JSON |
| 构建工具 | Maven |

暂不引入的组件：

- 暂不引入 Spring Security。
- 暂不引入 Redis。
- 暂不引入微服务组件。
- 暂不引入消息队列。

角色权限先采用后端自定义登录校验和角色判断实现，保持系统复杂度可控。

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
13. 与 Vue 前端联调。

## 13. 最终推荐方案

本项目最终推荐采用：

```text
Vue 前端 + Spring Boot 后端 REST API + MVC 架构 + MyBatis-Plus + MySQL
```

后端保持单体应用结构，不引入复杂中间件。系统通过 Controller、Service、Mapper 三层完成业务开发，通过 MySQL 存储过程和触发器满足数据库课程设计要求，通过角色字段和后端业务判断实现系统管理员、教师、学生三类权限控制。

该方案开发成本适中，结构清晰，符合 B/S 架构要求，也便于后续课程答辩时说明系统设计思路。
