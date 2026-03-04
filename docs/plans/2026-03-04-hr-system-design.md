# 人事管理系统整体架构与设计方案

## 1. 概述与需求范围

本系统定位为企业级人事管理系统（HRMS），旨在解决核心 HR 工作场景，提升人力资源管理效率。

### 1.1 核心功能模块
1. **员工信息管理**（基础底座）：档案管理、数据维护、权限控制。
2. **组织架构管理**：架构配置（部门/岗位）、架构调整、可视化展示。
3. **招聘管理**：需求申请与审核、简历库、面试记录、入职流程。
4. **考勤与假期管理**：考勤规则设计、打卡数据接入、假期额度管理、考勤统计。
5. **薪资与社保管理**：薪资结构设定、批核算、社保增减、薪资条发放。
6. **培训与绩效管理**：培训计划与记录、绩效考核模型、员工成长规划。
7. **离职与异动管理**：调岗/晋升/调薪审批、离职结算清算、离职员工库返聘。
8. **报表与数据分析**：固定/定制化报表，可视化大屏展示。

---

## 2. 系统技术架构

### 2.1 技术栈选型
*   **后端方案**：全栈 Java 方案，基于 `Spring Boot 3.2.x` + `MyBatis-Plus 3.5.x`。
*   **认证与权限**：`Spring Security 6.x` + `JWT` + `RBAC` (基于角色的访问控制)。
*   **工作流引擎**：`Flowable 7.x` 引擎应对审批流管理。
*   **数据库引擎**：`MySQL 8.0+` 构建业务关系。
*   **缓存与中间件**：`Redis 7.x`，`MinIO`（文件对象存储）。
*   **其他工具**：`MapStruct`, `Hutool`, `EasyExcel`, `Knife4j`（在线接口文档）。
*   **前端选型**：`Vue 3` + `Vite` + `TypeScript` + `Pinia` + `Element Plus` + `ECharts`。

### 2.2 模块化单体组织拆分
后端采用 Maven 多模块工程管理设计，实现低代码耦合，但单进程统一部署：
```text
hr-project/
├── hr-common       (公共工具类与基础配置拦截支持)
├── hr-core         (基础认证/授权、系统菜单与角色配置)
├── hr-employee     (核心实体档案：员工与架构)
├── hr-recruit      (招聘线业务流)
├── hr-attendance   (考勤线排版与规则)
├── hr-salary       (独立财务薪资核算线)
├── hr-performance  (培训绩效指标建立模块)
├── hr-transfer     (异动与调整流转线)
├── hr-report       (数据归集大屏数据生成处理)
├── hr-workflow     (集成 Flowable 提供全局审批统一入口控制)
├── hr-admin        (主程序启动器聚合上述资源运行)
└── hr-ui           (Vue 3 前端分离工程)
```

### 2.3 前端结构与风格
采用类飞书/钉钉现代化管理端布局：
*   **主题色彩**：`#4F46E5` 为主调（科技蓝紫）。
*   **响应流形**：卡片式分离、统计挂载，数据直观展示。
*   **架构规范**：独立封装全局 Axios、组合式函数 (`composables`) 管理权限挂载，遵循声明式渲染开发。

---

## 3. 面向业务的数据模型

数据表统一通过 `del_flag` 作伪删除。审计四要素 `create_by/update_by/time` 通用附加。所有表生成器映射。

### 3.1 权限控制域 (Core)
*   系统基础：`sys_user`, `sys_role`, `sys_menu`, `sys_dept`。
*   配置参数：`sys_config`, `sys_dict_type`, `sys_dict_data`。

### 3.2 档案与组织结构域 (Employee)
*   人员体系：`emp_employee`(全集中档案), `emp_contract`, `emp_certificate`, `emp_change_record`。
*   组织部门架构：`org_department`, `org_position`。

### 3.3 核心流转业务数据表
*   **招聘**：`rec_demand`, `rec_resume`, `rec_interview` 面试流程。
*   **考勤与请假**：`att_schedule`, `att_record`, `att_leave_type`, `att_leave_apply`。
*   **财务与社保**：`sal_employee` 职级薪资标准挂载，`sal_record` 计算结果快照。
*   **绩效与转归**：`perf_template`, `perf_evaluation`, `trans_apply`, `resign_apply`。
*   **工作流数据支持**：由 `ACT_*` 底层托管表与业务层的关联追踪实现。

---

## 4. 关键流程集成

### 4.1 "JWT + RBAC" 一站式权限解析
1.  客户端发端登录（账号 + 鉴权码验证成功）。
2.  下发 `AccessToken`(短时) 和 `RefreshToken`(双 Token 无感续用)。
3.  菜单拦截通过缓存校验，同时借助 `@DataScope` 注解增强对 `sys_dept` 的跨部门数据拦截机制。

### 4.2 Flowable 的切面介入方式
统一封装由服务层 `WorkflowService` 注入：
抽象暴露 `startProcess`, `approve`, `reject`。从请假到离职结算各个表单提交流程在表数据初始化状态的同时联动生成一次 `ProcessInstanceId` 后异步推送到 HR 层复审。

### 4.3 高效开发与数据交互协同
因为属于紧密聚合单体而非微服务，规避使用 RPC（Feign）。各域数据相互需要时暴露提供无副作用查询 `XXXFacade`，如员工转正后，薪资表数据经 `EmployeeFacade` 通知 `hr-salary` 构建月度模板项。

---

## 5. 质量保障

*   **运行安全控制**：慢数据截流防穿透，敏感数据 `@Desensitize` Json 过滤。使用 H2 / 本地 MySQL 环境切换支持 Dev -> Test 阶段。
*   **部署规划支持**：通过打包统一后交付 Dockerfile 与 Docker Compose 文件（集齐 MySQL, Redis 后端应用包, 前端 Nginx 的容器镜像拉起）。
*   **业务开发规范指导**：遵循 Alibaba Code 标准，RESTful 借口规范，Controller -> Facade -> Service 统一调取模式。提供 Knife4j 集成帮助随时联调与测试输出口对齐验证。
