# 当前开发环境组件版本记录

本文档记录教学事务管理系统开发前检查到的本机环境版本，用于后续选择兼容的后端、前端和数据库依赖。

检查时间：2026-05-12

## 1. 操作系统

| 项目 | 结果 |
| --- | --- |
| 操作系统 | Microsoft Windows 11 |
| 系统版本 | 10.0.26200 |
| Build Number | 26200 |
| 系统架构 | 64 位 |
| PowerShell 版本 | 5.1.26100.8115 |

## 2. Java 后端环境

| 组件 | 版本 / 状态 | 路径 |
| --- | --- | --- |
| Java Runtime | OpenJDK 21.0.3 LTS, Temurin-21.0.3+9 | `C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot\bin\java.exe` |
| Java Compiler | javac 21.0.3 | `C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot\bin\javac.exe` |
| Maven | Apache Maven 3.9.4 | `C:\apache-maven-3.9.4\bin\mvn.cmd` |
| Gradle | 未安装 | - |
| Spring CLI | 未安装 | - |

### 兼容性说明

- 当前 JDK 为 `21.0.3 LTS`，适合使用 Spring Boot 3.x。
- Maven 已安装，可作为后端项目构建工具。
- Gradle 未安装，不影响本项目，后端建议使用 Maven。
- Spring CLI 未安装，不影响项目开发，可以直接使用 Maven 创建和管理 Spring Boot 项目。

## 3. MySQL 数据库环境

| 组件 | 版本 / 状态 | 路径 |
| --- | --- | --- |
| MySQL Client | 8.0.34 for Win64 | `C:\mysql-8.0.34-winx64\bin\mysql.exe` |
| MySQL Server 服务名 | MySQL | - |
| MySQL Server 服务状态 | Running | - |
| MySQL Server 启动类型 | Manual | - |
| MySQL Server 版本 | 8.0.34 | - |
| MySQL Server 端口 | 3306 | - |
| MySQL Server 字符集 | utf8mb4 | - |
| MySQL Server 排序规则 | utf8mb4_0900_ai_ci | - |

### 当前连接检查结果

使用以下连接信息测试：

```text
host: localhost
port: 3306
user: root
password: 1234
```

当前结果：

```text
mysql_server_version: 8.0.34
port: 3306
character_set_server: utf8mb4
collation_server: utf8mb4_0900_ai_ci
Database (school): school
```

当前连接测试成功，本机存在项目数据库 `school`。

### 兼容性说明

- MySQL 客户端版本为 `8.0.34`，后续 Java 项目建议使用 MySQL Connector/J 8.x。
- 当前 MySQL 服务已启动，可用于数据库结构修改、后端连接测试和接口联调。
- 已知项目数据库为 `school`，后续代码中数据库连接应指向该库。

## 4. 前端环境

| 组件 | 版本 / 状态 | 路径 |
| --- | --- | --- |
| Node.js | v22.22.0 | `d:\cursor\resources\app\resources\helpers\node.exe` |
| npm | 10.7.0 | `D:\NodeJS\npm.cmd` |
| pnpm | 未安装 | - |
| yarn | 未安装 | - |
| Vue CLI | 未安装 | - |

### npm 全局包

```text
D:\NodeJS
+-- corepack@0.28.0
`-- npm@10.7.0
```

### 兼容性说明

- 当前 Node.js 为 `22.22.0`，可以用于 Vue 3 和 Vite 前端项目。
- npm 已安装，前端项目建议直接使用 npm 管理依赖。
- Vue CLI 未安装，但不影响使用 Vue 3；后续可以使用 Vite 创建 Vue 项目。
- pnpm 和 yarn 未安装，后续不建议在本项目中混用多个包管理器。

## 5. 版本控制工具

| 组件 | 版本 / 状态 | 路径 |
| --- | --- | --- |
| Git | 2.43.0.windows.1 | `D:\git\Git\cmd\git.exe` |

### 兼容性说明

- Git 已安装，可以用于项目版本管理。
- 当前任务未检查远程仓库配置，后续如需提交代码或创建分支，需要再检查 Git 仓库状态。

## 6. 后续项目技术建议

根据当前环境，建议后续项目采用以下组合：

| 方向 | 建议 |
| --- | --- |
| 后端语言 | Java 21 |
| 后端框架 | Spring Boot 3.x |
| 构建工具 | Maven 3.9.4 |
| 数据库 | MySQL 8.x |
| 数据库驱动 | MySQL Connector/J 8.x |
| ORM / 数据访问 | MyBatis-Plus 3.5.x |
| 前端框架 | Vue 3 |
| 前端构建工具 | Vite |
| 前端包管理器 | npm |

## 7. 需要注意的问题

1. MySQL 服务当前已启动，但启动类型为手动。

   当前已可连接 `localhost:3306`，并确认存在 `school` 数据库。后续如果重启电脑后连接失败，需要先检查并启动本机 `MySQL` 服务。

2. Node.js 实际命令路径来自 Cursor 内置目录。

   当前检测到的 `node` 路径为 `d:\cursor\resources\app\resources\helpers\node.exe`，而 `npm` 路径为 `D:\NodeJS\npm.cmd`。后续如果出现前端依赖安装异常，需要优先检查 Node 和 npm 是否来自同一套安装环境。

3. Vue CLI 未安装。

   本项目不建议依赖 Vue CLI，建议使用 Vue 3 + Vite。

4. Gradle 未安装。

   后端统一使用 Maven 即可，避免同时维护 Maven 和 Gradle 两套构建方式。
