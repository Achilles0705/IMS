# 教学事务管理系统（IMS）

基于 B/S 架构的学分制教学事务管理系统。浏览器通过前端页面访问后端 REST API，后端连接 MySQL `school` 数据库完成教务业务。

**技术栈：** Vue 3（前端，规划中）+ Spring Boot 3.3.5 + MyBatis-Plus 3.5.7 + MySQL 8.x

---

## 环境要求

| 组件 | 建议版本 |
| --- | --- |
| JDK | 21 |
| Maven | 3.9.x |
| MySQL | 8.0.x（库名 `school`） |
| Node.js | 22.x（前端开发时使用） |

本机环境版本详见 [environment_versions.md](environment_versions.md)。

---

## 项目启动方式

### 1. 启动数据库

1. 确保 MySQL 服务已启动（默认端口 `3306`）。
2. 确认存在数据库 `school`，且包含课程设计所需的基础表（结构说明见 [school_schema.md](school_schema.md)）。
3. 若本机账号/密码与项目配置不一致，请修改 `backend/src/main/resources/application.yml` 中的 `spring.datasource` 配置。

默认连接信息（可在配置文件中修改）：

```text
url:      jdbc:mysql://localhost:3306/school
username: root
password: 1234
```

### 2. 启动后端

在项目根目录下进入 `backend` 目录，使用 Maven 启动 Spring Boot：

```bash
cd backend
mvn spring-boot:run
```

或在 IDE 中直接运行主类 `com.ims.ImsApplication`。

启动成功后，后端默认监听：

```text
http://localhost:8080
```

接口统一前缀为 `/api`。

**健康检查（可选）：**

| 地址 | 说明 |
| --- | --- |
| `GET http://localhost:8080/api/health` | 检查后端是否运行 |
| `GET http://localhost:8080/api/health/db` | 检查数据库连接是否正常 |

### 3. 启动前端（规划中）

前端将放在 `frontend` 目录，计划使用 **Vue 3 + Vite**。当前前端尚未创建，待项目初始化后可按以下方式启动（示例）：

```bash
cd frontend
npm install
npm run dev
```

开发环境下前端会通过 HTTP 请求访问 `http://localhost:8080` 的后端 API。具体端口与代理配置以后端接口契约为准（见 [api_contract_basic.md](api_contract_basic.md)）。

### 4. 访问系统

完整 B/S 联调流程为：

```text
启动 MySQL → 启动 backend → 启动 frontend → 浏览器访问前端地址
```

目前可单独使用 Postman、浏览器或 `curl` 调用后端 REST 接口进行联调与测试。

---

## 目录与文件说明

```text
IMS/
├── backend/                          # 后端 Spring Boot 项目
│   ├── pom.xml                       # Maven 依赖与构建配置
│   └── src/main/
│       ├── java/com/ims/
│       │   ├── ImsApplication.java   # 应用入口
│       │   ├── config/               # 框架配置（如 MyBatis-Plus）
│       │   ├── controller/           # REST 控制器，对外提供 API
│       │   ├── service/              # 业务接口定义
│       │   ├── service/impl/         # 业务实现
│       │   ├── mapper/               # 数据访问层（MyBatis-Plus Mapper）
│       │   ├── entity/               # 与数据库表对应的实体类
│       │   ├── dto/                  # 请求体、入参对象
│       │   ├── vo/                   # 返回视图对象（预留）
│       │   ├── common/               # 统一响应、异常处理等公共组件
│       │   └── util/                # 工具类（预留）
│       └── resources/
│           └── application.yml       # 端口、数据源、MyBatis-Plus 等配置
├── frontend/                         # 前端 Vue 项目（规划中，目录待创建）
│   └── （页面、路由、组件、接口封装等）
├── architecture_plan.md            # 系统架构、角色权限、数据库扩展与开发顺序
├── school_schema.md                  # school 数据库现有表/视图结构说明
├── environment_versions.md           # 本机开发环境版本记录
├── api_contract_basic.md             # 前后端基础 REST 接口契约（Markdown）
├── api_contract_basic.openapi.yaml   # 同上接口的 OpenAPI 描述
└── README.md                         # 本说明文件
```

### 根目录文档

| 文件 | 作用 |
| --- | --- |
| `architecture_plan.md` | 总体技术方案：B/S 分层、三类角色功能、数据库“只增不删”原则、触发器/存储过程设计思路、开发顺序等 |
| `school_schema.md` | 记录 `school` 库中现有表、视图及字段，便于对照实体类与 SQL 扩展 |
| `environment_versions.md` | 记录 JDK、Maven、MySQL、Node 等本机版本，便于选型与排错 |
| `api_contract_basic.md` | 后端 API 路径、请求/响应格式、各角色基础接口说明 |
| `api_contract_basic.openapi.yaml` | 接口的机器可读描述，可用于导入 Swagger/Postman 等工具 |

### `backend` 后端分层

| 包/目录 | 作用 |
| --- | --- |
| `controller` | 接收 HTTP 请求，调用 Service，返回 `Result<T>` 统一 JSON |
| `service` / `service.impl` | 业务逻辑：选课、成绩、权限判断、统计、调用存储过程等 |
| `mapper` | 通过 MyBatis-Plus 访问 `department`、`student`、`teacher`、`course`、`class`、`course_selection` 等表 |
| `entity` | 数据库表映射实体 |
| `dto` | 创建/更新请求的数据传输对象 |
| `vo` | 面向前端的组合查询结果（预留） |
| `common` | `Result` 统一响应、`BusinessException`、`GlobalExceptionHandler` |
| `config` | MyBatis-Plus 等 Spring 配置 |

当前已实现的部分管理端与公共接口示例（前缀均为 `/api`）：

- `/health` — 健康检查
- `/departments`、`/courses` — 院系、课程查询
- `/admin/students`、`/admin/teachers`、`/admin/courses`、`/admin/classes` — 管理员基础数据维护

完整接口列表以 [api_contract_basic.md](api_contract_basic.md) 为准。

### `frontend` 前端（规划中）

前端目录用于存放 Vue 3 单页应用，预期职责包括：

- 登录与角色识别（系统管理员、教师、学生）
- 按角色展示不同菜单与页面
- 调用后端 `/api` 接口完成选课、成绩录入、查询与统计展示
- 与 `api_contract_basic.md` 中的接口契约保持一致

初始化前端时建议在项目根目录执行（示例）：

```bash
npm create vite@latest frontend -- --template vue
```

具体命令与依赖版本可在创建时根据 [environment_versions.md](environment_versions.md) 调整。

---

## 课程实验要求

本学期分组完成 **「教学事务管理系统」**，具体要求如下：

1. 系统必须是在 **B/S 结构** 下实现。
2. 数据库在原理 1 的 **School 数据库** 基础下自行修改，**只能添加，不能删除**。
3. 系统具有为不同的角色（**系统管理员、教师、学生**）提供不同操作权限的功能。
4. 系统为系统管理员提供具有 **学分制教务管理** 特色的各类功能。
5. 学生根据每个学期所开设的课程进行 **自主选课** 并具有查询有关信息的功能。
6. 教师根据学生所选课程进行 **成绩登录** 并且具有日常教学管理的功能。
7. 系统为不同的角色提供各类 **统计分析**。
8. 数据库中至少包含 **一个触发器** 和 **一个存储过程**，并在系统中使用和调用。
9. **其他辅助功能**。

上述要求与 [architecture_plan.md](architecture_plan.md) 中的功能规划、数据库扩展方案及触发器/存储过程设计相互对应，开发过程中可对照该文档推进实现与答辩说明。

---

## 相关链接

- 架构与功能设计：[architecture_plan.md](architecture_plan.md)
- 数据库结构：[school_schema.md](school_schema.md)
- 接口契约：[api_contract_basic.md](api_contract_basic.md)
