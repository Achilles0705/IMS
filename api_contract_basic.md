# 教学事务管理系统基础接口契约

本文档定义教学事务管理系统后端的基础接口契约，用于前后端联调和后续业务开发。

当前后端项目位于 `backend` 目录，技术栈为 `Java 21 + Spring Boot 3.3.5 + MyBatis-Plus 3.5.7 + MySQL 8.0.34`。

本阶段只界定满足课程设计要求所需的最基础接口，后续可在此基础上扩展分页、筛选、导出、日志审计、权限拦截等能力。

## 1. 通用约定

### 1.1 请求基础路径

```text
http://localhost:8080
```

后端接口统一使用 `/api` 作为前缀。

### 1.2 数据格式

请求和响应均使用 JSON。

```http
Content-Type: application/json
```

### 1.3 统一响应结构

当前后端已定义统一响应类 `Result<T>`。

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

字段说明：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `code` | number | 业务状态码，成功为 `200` |
| `message` | string | 响应消息 |
| `data` | object / array / null | 响应数据 |

### 1.4 通用错误响应

```json
{
  "code": 400,
  "message": "错误原因",
  "data": null
}
```

常用错误码约定：

| code | 说明 |
| --- | --- |
| `200` | 请求成功 |
| `400` | 请求参数错误或业务规则不满足 |
| `401` | 未登录 |
| `403` | 无权限 |
| `404` | 数据不存在 |
| `500` | 系统异常 |

### 1.5 角色约定

系统基础角色：

| 角色值 | 说明 |
| --- | --- |
| `ADMIN` | 系统管理员 |
| `TEACHER` | 教师 |
| `STUDENT` | 学生 |

在暂不引入 Spring Security 的前提下，基础阶段可由前端在请求中携带当前登录用户信息，后端在 Service 层进行角色判断。

建议后续统一通过请求头携带：

| 请求头 | 说明 |
| --- | --- |
| `X-User-Id` | 当前登录账号 ID |
| `X-Role` | 当前登录角色 |
| `X-Related-Id` | 关联学生编号或教师编号 |

## 2. 基础数据字段

### 2.1 Department

对应数据库表：`department`。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `deptId` | string | 院系编号 |
| `deptName` | string | 院系名称 |
| `address` | string | 地址 |
| `phoneCode` | string | 联系电话 |

### 2.2 Student

对应数据库表：`student`。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `studentId` | string | 学生编号 |
| `name` | string | 学生姓名 |
| `sex` | string | 性别，`男` 或 `女` |
| `dateOfBirth` | string | 出生日期，格式 `yyyy-MM-dd` |
| `nativePlace` | string | 籍贯 |
| `mobilePhone` | string | 手机号 |
| `deptId` | string | 院系编号 |
| `status` | string | 学生状态 |

### 2.3 Teacher

对应数据库表：`teacher`。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `staffId` | string | 教师编号 |
| `name` | string | 教师姓名 |
| `sex` | string | 性别，`男` 或 `女` |
| `dateOfBirth` | string | 出生日期，格式 `yyyy-MM-dd` |
| `professionalTitle` | string | 职称 |
| `salary` | number | 工资 |
| `deptId` | string | 院系编号 |

### 2.4 Course

对应数据库表：`course`。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `courseId` | string | 课程编号 |
| `courseName` | string | 课程名称 |
| `credit` | number | 学分 |
| `creditHours` | number | 学时 |
| `deptId` | string | 开课院系编号 |

### 2.5 ClassInfo

对应数据库表：`class`。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `semester` | string | 学期，例如 `201202` |
| `courseId` | string | 课程编号 |
| `staffId` | string | 任课教师编号 |
| `classTime` | string | 上课时间 |

### 2.6 CourseSelection

对应数据库表：`course_selection`。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `studentId` | string | 学生编号 |
| `semester` | string | 学期 |
| `courseId` | string | 课程编号 |
| `staffId` | string | 任课教师编号 |
| `score` | number / null | 成绩，未录入时为 `null` |

## 3. 已实现接口

### 3.1 健康检查

```http
GET /api/health
```

请求参数：无。

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `status` | string | 服务状态 |
| `message` | string | 状态说明 |

响应示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "status": "UP",
    "message": "教学事务管理系统后端运行正常"
  }
}
```

### 3.2 数据库连接检查

```http
GET /api/health/db
```

请求参数：无。

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `status` | string | 数据库连接状态 |
| `message` | string | 状态说明 |
| `departmentCount` | number | `department` 表记录数 |

响应示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "status": "UP",
    "message": "数据库连接正常",
    "departmentCount": 3
  }
}
```

## 4. 登录与当前用户接口

基础阶段只定义最简单的登录契约，用于区分系统管理员、教师和学生。

### 4.1 登录

```http
POST /api/auth/login
```

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `username` | string | 是 | 登录名 |
| `password` | string | 是 | 登录密码 |
| `role` | string | 是 | 登录角色：`ADMIN`、`TEACHER`、`STUDENT` |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `userId` | string | 账号 ID |
| `username` | string | 登录名 |
| `role` | string | 用户角色 |
| `relatedId` | string / null | 学生编号或教师编号，管理员可为 `null` |
| `displayName` | string | 页面显示名称 |

请求示例：

```json
{
  "username": "1102",
  "password": "123456",
  "role": "STUDENT"
}
```

响应示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "U1102",
    "username": "1102",
    "role": "STUDENT",
    "relatedId": "1102",
    "displayName": "刘晓明"
  }
}
```

### 4.2 查询当前用户信息

```http
GET /api/auth/profile
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-User-Id` | 是 | 当前登录账号 ID |
| `X-Role` | 是 | 当前登录角色 |
| `X-Related-Id` | 否 | 学生编号或教师编号 |

响应 `data` 与登录接口一致。

## 5. 管理员基础接口

管理员基础接口用于维护学生、教师、课程和开课信息。当前只定义列表、新增、修改，不定义删除。

### 5.1 查询学生列表

```http
GET /api/admin/students
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `deptId` | string | 否 | 按院系筛选 |
| `keyword` | string | 否 | 按学生编号或姓名模糊查询 |

响应 `data`：

```text
Student[]
```

### 5.2 新增学生

```http
POST /api/admin/students
```

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `studentId` | string | 是 | 学生编号 |
| `name` | string | 是 | 学生姓名 |
| `sex` | string | 否 | 性别 |
| `dateOfBirth` | string | 否 | 出生日期，格式 `yyyy-MM-dd` |
| `nativePlace` | string | 否 | 籍贯 |
| `mobilePhone` | string | 否 | 手机号 |
| `deptId` | string | 否 | 院系编号 |
| `status` | string | 否 | 学生状态 |

响应 `data`：

```text
Student
```

### 5.3 修改学生

```http
PUT /api/admin/students/{studentId}
```

路径参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `studentId` | string | 是 | 学生编号 |

请求字段：同新增学生接口，`studentId` 以路径参数为准。

响应 `data`：

```text
Student
```

### 5.4 查询教师列表

```http
GET /api/admin/teachers
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `deptId` | string | 否 | 按院系筛选 |
| `keyword` | string | 否 | 按教师编号或姓名模糊查询 |

响应 `data`：

```text
Teacher[]
```

### 5.5 新增教师

```http
POST /api/admin/teachers
```

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `staffId` | string | 是 | 教师编号 |
| `name` | string | 是 | 教师姓名 |
| `sex` | string | 否 | 性别 |
| `dateOfBirth` | string | 否 | 出生日期，格式 `yyyy-MM-dd` |
| `professionalTitle` | string | 否 | 职称 |
| `salary` | number | 否 | 工资 |
| `deptId` | string | 否 | 院系编号 |

响应 `data`：

```text
Teacher
```

### 5.6 修改教师

```http
PUT /api/admin/teachers/{staffId}
```

路径参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `staffId` | string | 是 | 教师编号 |

请求字段：同新增教师接口，`staffId` 以路径参数为准。

响应 `data`：

```text
Teacher
```

### 5.7 查询课程列表

```http
GET /api/admin/courses
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `deptId` | string | 否 | 按院系筛选 |
| `keyword` | string | 否 | 按课程编号或课程名称模糊查询 |

响应 `data`：

```text
Course[]
```

### 5.8 新增课程

```http
POST /api/admin/courses
```

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `courseId` | string | 是 | 课程编号 |
| `courseName` | string | 是 | 课程名称 |
| `credit` | number | 否 | 学分 |
| `creditHours` | number | 否 | 学时 |
| `deptId` | string | 否 | 开课院系编号 |

响应 `data`：

```text
Course
```

### 5.9 修改课程

```http
PUT /api/admin/courses/{courseId}
```

路径参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `courseId` | string | 是 | 课程编号 |

请求字段：同新增课程接口，`courseId` 以路径参数为准。

响应 `data`：

```text
Course
```

### 5.10 查询开课列表

```http
GET /api/admin/classes
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 否 | 学期 |
| `courseId` | string | 否 | 课程编号 |
| `staffId` | string | 否 | 教师编号 |

响应 `data`：

```text
ClassInfo[]
```

### 5.11 新增开课

```http
POST /api/admin/classes
```

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 是 | 学期 |
| `courseId` | string | 是 | 课程编号 |
| `staffId` | string | 是 | 任课教师编号 |
| `classTime` | string | 否 | 上课时间 |

响应 `data`：

```text
ClassInfo
```

### 5.12 修改开课

```http
PUT /api/admin/classes/{semester}/{courseId}/{staffId}
```

路径参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 是 | 学期 |
| `courseId` | string | 是 | 课程编号 |
| `staffId` | string | 是 | 任课教师编号 |

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `classTime` | string | 否 | 上课时间 |

响应 `data`：

```text
ClassInfo
```

## 6. 学生基础接口

学生接口用于自主选课和查询个人信息。

### 6.1 查询可选课程

```http
GET /api/student/classes
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 是 | 学期 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `semester` | string | 学期 |
| `courseId` | string | 课程编号 |
| `courseName` | string | 课程名称 |
| `credit` | number | 学分 |
| `creditHours` | number | 学时 |
| `staffId` | string | 教师编号 |
| `teacherName` | string | 教师姓名 |
| `classTime` | string | 上课时间 |
| `selected` | boolean | 当前学生是否已选 |

### 6.2 查询本人已选课程

```http
GET /api/student/selections
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 否 | 学期 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `studentId` | string | 学生编号 |
| `semester` | string | 学期 |
| `courseId` | string | 课程编号 |
| `courseName` | string | 课程名称 |
| `credit` | number | 学分 |
| `staffId` | string | 教师编号 |
| `teacherName` | string | 教师姓名 |
| `classTime` | string | 上课时间 |
| `score` | number / null | 成绩 |

### 6.3 学生选课

```http
POST /api/student/selections
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 是 | 学期 |
| `courseId` | string | 是 | 课程编号 |
| `staffId` | string | 是 | 任课教师编号 |

响应 `data`：

```text
CourseSelection
```

基础业务规则：

- 当前学生不能重复选择同一条开课记录。
- 选课记录写入 `course_selection` 表。
- `score` 初始为 `null`。

### 6.4 学生退课

```http
DELETE /api/student/selections
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 是 | 学期 |
| `courseId` | string | 是 | 课程编号 |
| `staffId` | string | 是 | 任课教师编号 |

响应 `data`：

```json
{
  "deleted": true
}
```

基础业务规则：

- 只能退当前学生本人的课程。
- 已录入成绩的课程不允许退课。

### 6.5 查询本人成绩

```http
GET /api/student/grades
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 否 | 学期 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `semester` | string | 学期 |
| `courseId` | string | 课程编号 |
| `courseName` | string | 课程名称 |
| `credit` | number | 学分 |
| `staffId` | string | 教师编号 |
| `teacherName` | string | 教师姓名 |
| `score` | number / null | 成绩 |
| `passed` | boolean / null | 是否通过，未录入成绩时为 `null` |

### 6.6 查询本人基础统计

```http
GET /api/student/statistics
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `selectedCourseCount` | number | 已选课程数 |
| `gradedCourseCount` | number | 已出成绩课程数 |
| `passedCourseCount` | number | 已通过课程数 |
| `failedCourseCount` | number | 不及格课程数 |
| `earnedCredits` | number | 已获得学分 |
| `averageScore` | number / null | 平均成绩 |

## 7. 教师基础接口

教师接口用于查看授课课程、学生名单和录入成绩。

### 7.1 查询本人授课课程

```http
GET /api/teacher/classes
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `TEACHER` |
| `X-Related-Id` | 是 | 当前教师编号 |

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 否 | 学期 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `semester` | string | 学期 |
| `courseId` | string | 课程编号 |
| `courseName` | string | 课程名称 |
| `credit` | number | 学分 |
| `staffId` | string | 教师编号 |
| `classTime` | string | 上课时间 |
| `studentCount` | number | 选课学生数 |

### 7.2 查询课程学生名单

```http
GET /api/teacher/classes/{semester}/{courseId}/students
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `TEACHER` |
| `X-Related-Id` | 是 | 当前教师编号 |

路径参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 是 | 学期 |
| `courseId` | string | 是 | 课程编号 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `studentId` | string | 学生编号 |
| `studentName` | string | 学生姓名 |
| `sex` | string | 性别 |
| `deptId` | string | 院系编号 |
| `mobilePhone` | string | 手机号 |
| `score` | number / null | 成绩 |

基础业务规则：

- 教师只能查看本人授课课程的学生名单。
- `staffId` 从请求头 `X-Related-Id` 获取，不由前端路径传入。

### 7.3 录入或修改成绩

```http
PUT /api/teacher/grades
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `TEACHER` |
| `X-Related-Id` | 是 | 当前教师编号 |

请求字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `studentId` | string | 是 | 学生编号 |
| `semester` | string | 是 | 学期 |
| `courseId` | string | 是 | 课程编号 |
| `score` | number | 是 | 成绩，范围 `0-100` |

响应 `data`：

```text
CourseSelection
```

基础业务规则：

- 教师只能录入本人授课课程的成绩。
- 成绩必须在 `0-100` 范围内。
- 学生必须已经选择该课程。

### 7.4 查询本人教学统计

```http
GET /api/teacher/statistics
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `TEACHER` |
| `X-Related-Id` | 是 | 当前教师编号 |

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 否 | 学期 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `classCount` | number | 授课班级数 |
| `studentCount` | number | 选课学生总数 |
| `gradedCount` | number | 已录入成绩数量 |
| `ungradedCount` | number | 未录入成绩数量 |
| `averageScore` | number / null | 平均成绩 |
| `failedCount` | number | 不及格人数 |

## 8. 管理员统计基础接口

### 8.1 查询管理员首页统计

```http
GET /api/admin/statistics/overview
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `ADMIN` |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `departmentCount` | number | 院系数量 |
| `studentCount` | number | 学生数量 |
| `teacherCount` | number | 教师数量 |
| `courseCount` | number | 课程数量 |
| `classCount` | number | 开课数量 |
| `selectionCount` | number | 选课记录数量 |

### 8.2 查询课程成绩统计

```http
GET /api/admin/statistics/course-grades
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `ADMIN` |

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `semester` | string | 否 | 学期 |
| `courseId` | string | 否 | 课程编号 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `semester` | string | 学期 |
| `courseId` | string | 课程编号 |
| `courseName` | string | 课程名称 |
| `studentCount` | number | 选课人数 |
| `gradedCount` | number | 已出成绩人数 |
| `averageScore` | number / null | 平均分 |
| `maxScore` | number / null | 最高分 |
| `minScore` | number / null | 最低分 |
| `failedCount` | number | 不及格人数 |

## 9. 院系与公共查询接口

这些接口供管理员、教师、学生页面复用。

### 9.1 查询院系列表

```http
GET /api/departments
```

请求参数：无。

响应 `data`：

```text
Department[]
```

### 9.2 查询课程基础列表

```http
GET /api/courses
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `deptId` | string | 否 | 院系编号 |
| `keyword` | string | 否 | 课程编号或课程名称 |

响应 `data`：

```text
Course[]
```

## 10. 存储过程与触发器预留接口

项目要求至少使用一个存储过程和一个触发器。本阶段仅保留契约位置，具体数据库对象后续再设计实现。

### 10.1 学生学分统计

```http
GET /api/student/credit-summary
```

请求头：

| 请求头 | 必填 | 说明 |
| --- | --- | --- |
| `X-Role` | 是 | 固定为 `STUDENT` |
| `X-Related-Id` | 是 | 当前学生编号 |

响应 `data`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `studentId` | string | 学生编号 |
| `passedCourseCount` | number | 通过课程数 |
| `earnedCredits` | number | 已获得学分 |
| `averageScore` | number / null | 平均成绩 |

说明：

- 该接口后续可调用 MySQL 存储过程，例如 `sp_get_student_credit_summary`。
- 如果该接口与 `/api/student/statistics` 内容重叠，后续实现时可以合并，只保留一个入口。

## 11. 基础接口清单

| 模块 | 方法 | 路径 | 说明 | 当前状态 |
| --- | --- | --- | --- | --- |
| 健康检查 | GET | `/api/health` | 检查后端服务 | 已实现 |
| 健康检查 | GET | `/api/health/db` | 检查数据库连接 | 已实现 |
| 登录 | POST | `/api/auth/login` | 用户登录 | 待实现 |
| 登录 | GET | `/api/auth/profile` | 查询当前用户 | 待实现 |
| 公共查询 | GET | `/api/departments` | 查询院系 | 待实现 |
| 公共查询 | GET | `/api/courses` | 查询课程 | 待实现 |
| 管理员 | GET | `/api/admin/students` | 查询学生 | 待实现 |
| 管理员 | POST | `/api/admin/students` | 新增学生 | 待实现 |
| 管理员 | PUT | `/api/admin/students/{studentId}` | 修改学生 | 待实现 |
| 管理员 | GET | `/api/admin/teachers` | 查询教师 | 待实现 |
| 管理员 | POST | `/api/admin/teachers` | 新增教师 | 待实现 |
| 管理员 | PUT | `/api/admin/teachers/{staffId}` | 修改教师 | 待实现 |
| 管理员 | GET | `/api/admin/courses` | 查询课程 | 待实现 |
| 管理员 | POST | `/api/admin/courses` | 新增课程 | 待实现 |
| 管理员 | PUT | `/api/admin/courses/{courseId}` | 修改课程 | 待实现 |
| 管理员 | GET | `/api/admin/classes` | 查询开课 | 待实现 |
| 管理员 | POST | `/api/admin/classes` | 新增开课 | 待实现 |
| 管理员 | PUT | `/api/admin/classes/{semester}/{courseId}/{staffId}` | 修改开课 | 待实现 |
| 学生 | GET | `/api/student/classes` | 查询可选课程 | 待实现 |
| 学生 | GET | `/api/student/selections` | 查询已选课程 | 待实现 |
| 学生 | POST | `/api/student/selections` | 学生选课 | 待实现 |
| 学生 | DELETE | `/api/student/selections` | 学生退课 | 待实现 |
| 学生 | GET | `/api/student/grades` | 查询成绩 | 待实现 |
| 学生 | GET | `/api/student/statistics` | 查询个人统计 | 待实现 |
| 教师 | GET | `/api/teacher/classes` | 查询授课课程 | 待实现 |
| 教师 | GET | `/api/teacher/classes/{semester}/{courseId}/students` | 查询课程学生 | 待实现 |
| 教师 | PUT | `/api/teacher/grades` | 录入或修改成绩 | 待实现 |
| 教师 | GET | `/api/teacher/statistics` | 查询教学统计 | 待实现 |
| 管理员统计 | GET | `/api/admin/statistics/overview` | 首页统计 | 待实现 |
| 管理员统计 | GET | `/api/admin/statistics/course-grades` | 课程成绩统计 | 待实现 |

## 12. 后续扩展方向

后续可在本契约基础上继续扩展：

- 分页参数：`pageNum`、`pageSize`。
- 更完整的登录凭证，例如 token。
- 账号表 `user_account`。
- 选课容量字段和选课状态字段。
- 选课日志、成绩日志。
- 存储过程调用接口。
- 触发器配套的日志查询接口。
- 更细粒度的角色权限校验。
