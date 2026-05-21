# 教学事务管理系统（IMS）

基于 B/S 架构的学分制教学事务管理系统。浏览器通过前端页面访问后端 REST API，后端连接 MySQL `school` 数据库完成教务业务。

**技术栈：** Vue 3 + Element Plus + Pinia + Vue Router + Spring Boot 3.3.5 + MyBatis-Plus 3.5.7 + MySQL 8.x

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

### 3. 启动前端

前端已位于 `frontend` 目录，基于 **Vue 3 + Vite + Element Plus** 搭建，包含角色路由、Pinia 登录态和全接口请求封装骨架。

#### 3.1 安装依赖

```bash
cd frontend
npm install
```

#### 3.2 启动开发环境

```bash
npm run dev
```

默认访问地址（Vite）：

```text
http://localhost:5173
```

开发环境代理说明：

- 前端请求基础路径为 `VITE_API_BASE_URL=/api`。
- `vite.config.ts` 已将 `/api` 代理到 `http://localhost:8080`（可通过 `VITE_BACKEND_PROXY_TARGET` 调整）。
- 因此前端调用 `/api/*` 时会自动转发到后端服务。

#### 3.3 生产构建与预览

```bash
npm run build
npm run preview
```

常见问题：

- 若启动时报 Node 版本不兼容，请优先使用 Node.js 22.x（见 [environment_versions.md](environment_versions.md)）。
- 若接口请求失败，先确认后端 `http://localhost:8080` 已正常启动。

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
├── frontend/                         # 前端 Vue 3 + Element Plus 项目
│   ├── src/
│   │   ├── api/                     # 按契约封装的全部接口
│   │   ├── router/                  # 路由与权限守卫
│   │   ├── stores/                  # Pinia 全局状态
│   │   ├── layout/                  # 主布局
│   │   ├── views/                   # 管理员/教师/学生页面骨架
│   │   ├── utils/                   # request/storage 等基础设施
│   │   └── types/                   # 契约类型定义
│   ├── .env.development             # 开发环境变量
│   ├── .env.production              # 生产环境变量
│   └── vite.config.ts               # Vite 与 API 代理配置
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

### `frontend` 前端

前端目录已完成基础骨架，当前职责包括：

- 登录与角色识别（系统管理员、教师、学生）
- 按角色展示不同菜单与页面
- 调用后端 `/api` 接口完成选课、成绩录入、查询与统计展示
- 与 `api_contract_basic.md` 和 `api_contract_basic.openapi.yaml` 中的接口契约保持一致

当前前端特性：

- 已完成管理员、教师、学生三类菜单与页面路由骨架。
- 已完成 Axios 请求层和统一 `Result<T>` 响应解析。
- 已封装全部基础接口（含 `GET /api/student/credit-summary` 预留接口）。
- 页面以“列表 + 表单 + 统计容器”为主，便于后续逐步补全业务细节。

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
