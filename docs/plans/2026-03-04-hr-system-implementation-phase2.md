# 人事管理系统 MVP Phase 2 实现计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 完成人事管理系统核心后台的启动器搭建、完善核心域实体模型、建立全局异常处理并初始化员工基础档案模块。

**Architecture:** 继续基于 Spring Boot 3.2 模块化架构演进。本次将配置核心启动主程序应用（`hr-admin`），进一步完善 `hr-core` 的角色和部门实体，在 `hr-common` 增加全局异常处理拦截器，并搭建 `hr-employee` 员工模块供后续流转业务调用。

**Tech Stack:** Java 17, Spring Boot 3.2, MyBatis-Plus, Spring Web.

---

### Task 1: 初始化 hr-admin 启动主工程与基础配置

**Files:**
- Create: `hr-admin/pom.xml`
- Create: `hr-admin/src/main/resources/application.yml`
- Create: `hr-admin/src/main/java/com/liyisoft/hr/admin/HrAdminApplication.java`

**Step 1: 编写 admin 模块的 pom 依赖**

创建 `hr-admin/pom.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>com.liyisoft.hr</groupId>
        <artifactId>hr-project</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>hr-admin</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.liyisoft.hr</groupId>
            <artifactId>hr-core</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**Step 2: 编写全局核心配置文件 application.yml**

创建 `hr-admin/src/main/resources/application.yml`:
```yaml
server:
  port: 8080

spring:
  application:
    name: hr-admin
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/hr_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: password
```

**Step 3: 编写 Spring Boot 主启动类**

创建 `hr-admin/src/main/java/com/liyisoft/hr/admin/HrAdminApplication.java`:
```java
package com.liyisoft.hr.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// 暂时排除自动配置数据源，等实际部署DB再开启
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HrAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrAdminApplication.class, args);
    }
}
```

**Step 4: 编译验证 admin 模块**

Run: `mvn clean compile -pl hr-admin`
Expected: BUILD SUCCESS（输出成功）

**Step 5: 提交记录**

```bash
git add hr-admin/pom.xml hr-admin/src/main/resources/application.yml hr-admin/src/main/java/com/liyisoft/hr/admin/HrAdminApplication.java
git commit -m "feat(admin): 初始化启动器及配置模块"
```

### Task 2: 丰富 hr-core 实体 (角色与部门)

**Files:**
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysRole.java`
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysDept.java`

**Step 1: 编写 SysRole 角色实体类**

创建 `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysRole.java`:
```java
package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole {
    @TableId
    private Long id;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private Integer status;
    @TableLogic
    private Integer delFlag;
}
```

**Step 2: 编写 SysDept 部门实体类**

创建 `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysDept.java`:
```java
package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
@TableName("sys_dept")
public class SysDept {
    @TableId
    private Long id;
    private Long parentId;
    private String ancestors;
    private String deptName;
    private Integer orderNum;
    private String leader;
    private Integer status;
    @TableLogic
    private Integer delFlag;
}
```

**Step 3: 编译验证 core 模块**

Run: `mvn clean compile -pl hr-core`
Expected: BUILD SUCCESS（输出成功）

**Step 4: 提交记录**

```bash
git add hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysRole.java hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysDept.java
git commit -m "feat(core): 添加角色和部门实体模型"
```

### Task 3: 配置 hr-common 全局异常处理

**Files:**
- Create: `hr-common/src/main/java/com/liyisoft/hr/common/exception/GlobalExceptionHandler.java`

**Step 1: 编写全局异常处理器**

创建 `hr-common/src/main/java/com/liyisoft/hr/common/exception/GlobalExceptionHandler.java`:
```java
package com.liyisoft.hr.common.exception;

import com.liyisoft.hr.common.core.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.error("服务器内部错误，请联系管理员");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("业务运行时异常: {}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }
}
```

**Step 2: 编译验证 common 模块**

Run: `mvn clean compile -pl hr-common`
Expected: BUILD SUCCESS（输出成功）

**Step 3: 提交记录**

```bash
git add hr-common/src/main/java/com/liyisoft/hr/common/exception/GlobalExceptionHandler.java
git commit -m "feat(common): 配置框架级统一异常拦截处理器"
```

### Task 4: 创建 hr-employee 模块及实体模型

**Files:**
- Modify: `pom.xml`
- Create: `hr-employee/pom.xml`
- Create: `hr-employee/src/main/java/com/liyisoft/hr/employee/domain/entity/EmpEmployee.java`

**Step 1: 将 hr-employee 加入到根 pom.xml 的 modules 列表中**

对于根 `pom.xml` 增加 module 声明（请手工修改以匹配确切行号，假设插在 `<module>hr-admin</module>` 下方）:
```xml
        <module>hr-employee</module>
```
同样在 `dependencyManagement` 区域注册 `hr-employee` 版本。

**Step 2: 编写 hr-employee 模块 POM**

创建 `hr-employee/pom.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>com.liyisoft.hr</groupId>
        <artifactId>hr-project</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>hr-employee</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.liyisoft.hr</groupId>
            <artifactId>hr-common</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        </dependency>
    </dependencies>
</project>
```

**Step 3: 编写 EmpEmployee 员工实体类**

创建 `hr-employee/src/main/java/com/liyisoft/hr/employee/domain/entity/EmpEmployee.java`:
```java
package com.liyisoft.hr.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.util.Date;

@Data
@TableName("emp_employee")
public class EmpEmployee {
    @TableId
    private Long id;
    private String workNo;
    private String name;
    private Integer gender;
    private String phone;
    private String idCard;
    private Long deptId;
    private Long positionId;
    private String workStatus;
    private Date joinDate;
    @TableLogic
    private Integer delFlag;
}
```

**Step 4: 编译验证**

Run: `mvn clean validate` && `mvn clean compile -pl hr-employee`
Expected: 顶层 validate 通过，hr-employee 子工程编译 BUILD SUCCESS（输出成功）

**Step 5: 提交记录**

```bash
git add pom.xml hr-employee/pom.xml hr-employee/src/main/java/com/liyisoft/hr/employee/domain/entity/EmpEmployee.java
git commit -m "feat(employee): 新增员工档案体系管理基础模块"
```
