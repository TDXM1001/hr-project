# 人事管理系统 MVP Phase 3 实现计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 完成后端 MyBatis-Plus 自动填充设施与剩余核心实体搭建，配置 JWT 工具，并完成前端 Vue 生态（Pinia/Router/ElementPlus）的核心集成。

**Architecture:** `hr-common` 提供 MyBatis-Plus 通用配置（分页、时间/审计自动填充）及 JWT 加密解密工具；`hr-core` 提供菜单与字典实体模型支持 RBAC 和配置管理；`hr-ui` 接轨企业级前端开发脚手架并挂载路由与状态管理层。

**Tech Stack:** Java 17, Spring Boot, MyBatis-Plus, jjwt, Vue 3, Pinia, Vue Router, Element Plus.

---

### Task 1: MyBatis-Plus 全局基础配置与自动填充

**Files:**
- Create: `hr-common/src/main/java/com/liyisoft/hr/common/config/MybatisPlusConfig.java`
- Create: `hr-common/src/main/java/com/liyisoft/hr/common/handler/MyMetaObjectHandler.java`

**Step 1: 编写 MyBatisPlusConfig 配置文件 (分页插件)**

```java
package com.liyisoft.hr.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加 MySQL 分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

**Step 2: 编写 MyMetaObjectHandler 自动填充处理器**

此处注入审计四要素 `createTime`, `createBy`, `updateTime`, `updateBy`。

```java
package com.liyisoft.hr.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createBy", String.class, "system"); // 后续接入 Security 上下文替换为实际用户
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", String.class, "system");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "updateBy", String.class, "system"); // 后续接入 Security 上下文替换为实际用户
    }
}
```

**Step 3: 验证编译**

Run: `mvn clean compile -pl hr-common`
Expected: 编译返回 BUILD SUCCESS.

**Step 4: 提交**

```bash
git add hr-common/src/main/java/com/liyisoft/hr/common/config/MybatisPlusConfig.java hr-common/src/main/java/com/liyisoft/hr/common/handler/MyMetaObjectHandler.java
git commit -m "feat(common): 增加 mybatis-plus 分页与自动填充机制配置"
```

### Task 2: 完善 hr-core 菜单与系统字典实体模型

**Files:**
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysMenu.java`
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysDictType.java`

**Step 1: 编写 SysMenu 菜单表实体**

```java
package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
@TableName("sys_menu")
public class SysMenu {
    @TableId
    private Long id;
    private String menuName;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String queryParam;
    private Integer isFrame;
    private Integer isCache;
    private Integer menuType;
    private String visible;
    private Integer status;
    private String perms;
    private String icon;
    @TableLogic
    private Integer delFlag;
}
```

**Step 2: 编写 SysDictType 字典分类实体**

```java
package com.liyisoft.hr.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
@TableName("sys_dict_type")
public class SysDictType {
    @TableId
    private Long id;
    private String dictName;
    private String dictType;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
```

**Step 3: 验证编译**

Run: `mvn clean compile -pl hr-core`
Expected: 编译返回 BUILD SUCCESS.

**Step 4: 提交**

```bash
git add hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysMenu.java hr-core/src/main/java/com/liyisoft/hr/core/domain/entity/SysDictType.java
git commit -m "feat(core): 完成菜单和字典基础类型的实体模型"
```

### Task 3: hr-common 集成 JWT 依赖与工具类

**Files:**
- Modify: `hr-common/pom.xml`
- Create: `hr-common/src/main/java/com/liyisoft/hr/common/utils/JwtUtils.java`

**Step 1: hr-common 引入 jjwt 依赖**

将以下依赖插入到 `hr-common/pom.xml` 的 `<dependencies>` 块中：

```xml
        <!-- JWT 支持 -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.5</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.5</version>
            <scope>runtime</scope>
        </dependency>
```

**Step 2: 编写 JwtUtils 核心工具类**

```java
package com.liyisoft.hr.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    
    // 至少32个字符的密钥
    private final String SECRET = "LiyiSoftHumanResourceManagementSystemSecretKey20268888";
    private final long EXPIRATION = 7200000L; // 2 hours
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
```

**Step 3: 验证编译**

Run: `mvn clean compile -pl hr-common`
Expected: 编译返回 BUILD SUCCESS.

**Step 4: 提交**

```bash
git add hr-common/pom.xml hr-common/src/main/java/com/liyisoft/hr/common/utils/JwtUtils.java
git commit -m "feat(common): 添加 jjwt 依赖及其常用工具类支持"
```

### Task 4: hr-ui 前端初始化核心生态组件

**Files:**
- Modify: `hr-ui/package.json`
- Create: `hr-ui/src/router/index.ts`
- Create: `hr-ui/src/store/index.ts`
- Modify: `hr-ui/src/main.ts`

**Step 1: 安装必要的前端基础依赖**

Run: `cd hr-ui && npm install element-plus vue-router pinia axios @element-plus/icons-vue`
Expected: 成功安装 packages 工具或依赖。

**Step 2: 创建基础 router 和 store**

创建 `hr-ui/src/router/index.ts`:
```typescript
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../components/HelloWorld.vue') // 临时组件占位
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
```

创建 `hr-ui/src/store/index.ts`:
```typescript
import { createPinia } from 'pinia';

const pinia = createPinia();

export default pinia;
```

**Step 3: 在 main.ts 中注册生态插件**

修改 `hr-ui/src/main.ts` 为以下内容：
```typescript
import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import pinia from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(router)
app.use(pinia)
app.use(ElementPlus)

app.mount('#app')
```

**Step 4: 验证前端项目构建**

Run: `cd hr-ui && npm run build`
Expected: 没有类型或依赖错误，成功构建出 dist 文件夹。

**Step 5: 提交**

```bash
git add hr-ui/package.json hr-ui/package-lock.json hr-ui/src/router hr-ui/src/store hr-ui/src/main.ts
git commit -m "feat(ui): 安装和集成 vue-router, pinia 及 element-plus 生态组件"
```
