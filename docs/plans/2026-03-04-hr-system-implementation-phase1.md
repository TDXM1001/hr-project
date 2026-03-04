# 人事管理系统 MVP Phase 1 实现计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**目标:** 搭建人事管理系统的基础骨架（Phase 1），包含多模块工程初始化、公共组件 `hr-common` 和核心模块 `hr-core` 的基础结构，以及 `hr-ui` 的基础骨架。

**架构:** 基于 Spring Boot 3.2 模块化单体架构，配合 Maven 多模块管理。由于当前项目尚为完全空白，我们优先初始化全局配置和核心基建工程。

**技术栈:** Java 17, Spring Boot 3.2, MyBatis-Plus, Maven, Vue 3, Vite, Element-Plus.

---

### 任务 1: 初始化 Maven 多模块顶级工程与 Git Ignore

**文件:**
- Create: `pom.xml`
- Create: `.gitignore`

**步骤 1: 编写多模块配置和 gitignore**

创建根 `pom.xml` 代码如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.liyisoft.hr</groupId>
    <artifactId>hr-project</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>hr-common</module>
        <module>hr-core</module>
        <module>hr-admin</module>
    </modules>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.3</spring-boot.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Spring Boot Dependencies -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- MyBatis-Plus -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>
            <!-- Internal Modules -->
            <dependency>
                <groupId>com.liyisoft.hr</groupId>
                <artifactId>hr-common</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.liyisoft.hr</groupId>
                <artifactId>hr-core</artifactId>
                <version>${project.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

创建 `.gitignore`：
```text
target/
!.mvn/wrapper/maven-wrapper.jar
log/
*.log
**/*.log

.idea/
*.iws
*.iml
*.ipr

.vscode/

node_modules/
dist/
dist-ssr/
*.local

.env.local
.env.*.local
```

**步骤 2: 验证项目结构**

Run: `mvn clean validate`
Expected: BUILD SUCCESS 获取无错输出。

**步骤 3: 提交代码**

```bash
git add pom.xml .gitignore
git commit -m "chore: initialize maven multi-module project structure"
```

### 任务 2: 搭建 hr-common 公共模块

**文件:**
- Create: `hr-common/pom.xml`
- Create: `hr-common/src/main/java/com/liyisoft/hr/common/core/domain/Result.java`

**步骤 1: 编写 common 模块依赖**

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

    <artifactId>hr-common</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
</project>
```

**步骤 2: 编写全局统一响应体对象 (Result)**

```java
package com.liyisoft.hr.common.core.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
```

**步骤 3: 验证编译**

Run: `mvn clean compile -pl hr-common`
Expected: BUILD SUCCESS

**步骤 4: 提交代码**

```bash
git add hr-common/pom.xml hr-common/src/main/java/com/liyisoft/hr/common/core/domain/Result.java
git commit -m "feat(common): create global response Result object"
```

### 任务 3: 搭建 hr-core 核心模块与体系实体

**文件:**
- Create: `hr-core/pom.xml`
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysUser.java`

**步骤 1: 引入依赖并创建模块配置**

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

    <artifactId>hr-core</artifactId>

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

**步骤 2: 编写用户实体 SysUser**

```java
package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId
    private Long id;
    
    private String username;
    
    private String password;
    
    private String nickname;
    
    private String phone;
    
    private Integer status;
    
    @TableLogic
    private Integer delFlag;
}
```

**步骤 3: 验证编译**

Run: `mvn clean compile -pl hr-core`
Expected: BUILD SUCCESS

**步骤 4: 提交代码**

```bash
git add hr-core/pom.xml hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysUser.java
git commit -m "feat(core): setup core module and SysUser entity"
```

### 任务 4: 初始化 hr-ui 前端工程

**步骤 1: 使用 npm 初始化 vite+vue3 项目**

Run: `npm create vite@latest hr-ui -- --template vue-ts`
Expected: 控制台提示创建成功。

**步骤 2: 验证前端项目构建**

Run: `cd hr-ui; npm install; npm run build; cd ..`
Expected: 成功构建出 dist 文件夹。

**步骤 3: 提交代码**

```bash
git add hr-ui
git commit -m "chore(ui): initialize vue 3 frontend project"
```
