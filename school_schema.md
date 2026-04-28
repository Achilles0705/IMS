# School 数据库表结构说明

本文档记录 MySQL 数据库 `school` 下现有表、视图及其字段结构。

说明：

- 本文档只记录表结构，不记录表中的具体数据。
- 字段信息包含字段名、数据类型、是否允许为空、键类型、默认值和额外属性。

## 数据库对象概览

| 对象名 | 类型 |
| --- | --- |
| `c` | 表 |
| `class` | 表 |
| `course` | 表 |
| `course_selection` | 表 |
| `department` | 表 |
| `s` | 表 |
| `sc` | 表 |
| `student` | 表 |
| `teacher` | 表 |
| `v_fail_computer` | 视图 |

## 表结构

### `c`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `CNO` | `varchar(10)` | 否 | PRI | NULL |  |
| 2 | `CNAME` | `varchar(30)` | 否 |  | NULL |  |
| 3 | `CDEPT` | `varchar(20)` | 是 |  | NULL |  |
| 4 | `TNAME` | `varchar(20)` | 是 |  | NULL |  |

### `class`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `semester` | `varchar(6)` | 否 | PRI | NULL |  |
| 2 | `course_id` | `varchar(8)` | 否 | PRI | NULL |  |
| 3 | `staff_id` | `varchar(4)` | 否 | PRI | NULL |  |
| 4 | `class_time` | `varchar(20)` | 是 |  | NULL |  |

### `course`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `course_id` | `varchar(8)` | 否 | PRI | NULL |  |
| 2 | `course_name` | `varchar(50)` | 否 | MUL | NULL |  |
| 3 | `credit` | `int` | 是 |  | 4 |  |
| 4 | `credit_hours` | `int` | 是 |  | 40 |  |
| 5 | `dept_id` | `varchar(2)` | 是 | MUL | NULL |  |

### `course_selection`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `student_id` | `varchar(4)` | 否 | PRI | NULL |  |
| 2 | `semester` | `varchar(6)` | 否 | PRI | NULL |  |
| 3 | `course_id` | `varchar(8)` | 否 | PRI | NULL |  |
| 4 | `staff_id` | `varchar(4)` | 否 | PRI | NULL |  |
| 5 | `score` | `int` | 是 |  | NULL |  |

### `department`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `dept_id` | `varchar(2)` | 否 | PRI | NULL |  |
| 2 | `dept_name` | `varchar(20)` | 否 |  | NULL |  |
| 3 | `address` | `varchar(50)` | 是 |  | NULL |  |
| 4 | `phone_code` | `varchar(20)` | 是 |  | NULL |  |

### `s`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `SNO` | `varchar(10)` | 否 | PRI | NULL |  |
| 2 | `SNAME` | `varchar(20)` | 否 |  | NULL |  |
| 3 | `AGE` | `int` | 是 |  | NULL |  |
| 4 | `SEX` | `char(2)` | 是 |  | NULL |  |
| 5 | `SDEPT` | `varchar(20)` | 是 |  | NULL |  |

### `sc`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `SNO` | `varchar(10)` | 否 | PRI | NULL |  |
| 2 | `CNO` | `varchar(10)` | 否 | PRI | NULL |  |
| 3 | `GRADE` | `decimal(5,2)` | 是 |  | NULL |  |

### `student`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `student_id` | `varchar(4)` | 否 | PRI | NULL |  |
| 2 | `name` | `varchar(20)` | 否 |  | NULL |  |
| 3 | `sex` | `enum('男','女')` | 是 |  | NULL |  |
| 4 | `date_of_birth` | `date` | 是 |  | NULL |  |
| 5 | `native_place` | `varchar(50)` | 是 |  | NULL |  |
| 6 | `mobile_phone` | `varchar(11)` | 是 |  | NULL |  |
| 7 | `dept_id` | `varchar(2)` | 是 | MUL | NULL |  |
| 8 | `Status` | `varchar(10)` | 是 |  | NULL |  |

### `teacher`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `staff_id` | `varchar(4)` | 否 | PRI | NULL |  |
| 2 | `name` | `varchar(20)` | 否 |  | NULL |  |
| 3 | `sex` | `enum('男','女')` | 是 |  | NULL |  |
| 4 | `date_of_birth` | `date` | 是 |  | NULL |  |
| 5 | `professional_title` | `varchar(10)` | 是 |  | NULL |  |
| 6 | `salary` | `decimal(8,2)` | 是 |  | NULL |  |
| 7 | `dept_id` | `varchar(2)` | 是 | MUL | NULL |  |

## 视图结构

### `v_fail_computer`

| 序号 | 字段名 | 数据类型 | 允许为空 | 键 | 默认值 | 额外属性 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `student_id` | `varchar(4)` | 否 |  | NULL |  |
| 2 | `name` | `varchar(20)` | 否 |  | NULL |  |
| 3 | `sex` | `enum('男','女')` | 是 |  | NULL |  |
| 4 | `mobile_phone` | `varchar(11)` | 是 |  | NULL |  |
| 5 | `course_name` | `varchar(50)` | 否 |  | NULL |  |
| 6 | `score` | `int` | 是 |  | NULL |  |

视图定义摘要：

`v_fail_computer` 查询计算机学院中成绩低于 60 分的学生信息、课程名称和成绩。
