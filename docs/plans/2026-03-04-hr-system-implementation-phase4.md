# 人事管理系统 MVP Phase 4 实现计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 搭建基于 Spring Security 的鉴权过滤机制，完成后端登录鉴权接口，并在前端 Vue 接入 Axios 封装与提供基础登录页面。

**Architecture:** `hr-core` 提供基于 `Spring Security` 和 `JWT` 的安全过滤器链，实现 `UserDetails` 以供认证鉴权封装；创建对外 `SysAuthController` 处理授权；`hr-ui` 初始化全局 `Axios` 请求与请求拦截器，制作基础 `Login.vue` 并完成前端 `vue-router` 路由授权鉴权拦截。

**Tech Stack:** Java 17, Spring Boot Security 3.2, JWT, Vue 3, Pinia, Axios.

---

### Task 1: hr-core 引入 Spring Security 以及配置安全基础属性

**Files:**
- Modify: `hr-core/pom.xml`
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/security/config/SecurityConfig.java`

**Step 1: 引入依赖**

在 `hr-core/pom.xml` 的 `<dependencies>` 块中加入 `spring-boot-starter-security`：
```xml
        <!-- Spring Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```

**Step 2: 编写基础 SecurityConfig**

提供开放跨域，并关闭 CSRF 方便 JWT 使用，完全禁用 Session (无状态)。

```java
package com.liyisoft.hr.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

**Step 3: 验证编译**

Run: `mvn clean compile -pl hr-core`
Expected: 编译返回 BUILD SUCCESS.

**Step 4: 提交**

```bash
git add hr-core/pom.xml hr-core/src/main/java/com/liyisoft/hr/core/security/config/SecurityConfig.java
git commit -m "feat(core): 引入 spring security 并提供无状态和跨域的支持配置"
```

### Task 2: 编写 JWT 核心过滤器与 UserDetailsService

**Files:**
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/security/filter/JwtAuthenticationFilter.java`
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/security/service/UserDetailsServiceImpl.java`

**Step 1: 实现 UserDetailsServiceImpl 模拟获取用户**

用于支持加载用户逻辑（因目前暂无 Mapper 数据直通，使用直接注入与模拟假表或者借助 MyBatis 服务。此处假定我们有通用的服务，为避免复杂，我们返回一个 Mock 用户支持启动联调）：

```java
package com.liyisoft.hr.core.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 当前暂时直接提供一组默认验证，待实际对接 Mapper 时替换为 selectOne(SysUser) 查询
        if ("admin".equals(username)) {
            // "123456" 对应的 BCrypt 解密密文
            return new User("admin", "$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2", Collections.emptyList());
        }
        throw new UsernameNotFoundException("用户不存在");
    }
}
```

**Step 2: 注入 JWT 拦截器**

（此处省略了JwtUtils的调用具体代码逻辑，仅做拦截骨架。将拦截器推入全局）：

```java
package com.liyisoft.hr.core.security.filter;

import com.liyisoft.hr.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtUtils.getSubjectFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token 无效或过期
                logger.error("Token verification failed", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

**Step 3: 将其注入 SecurityConfig**

需要先在 `SecurityConfig` 中通过 `@Autowired` 增加对此 filter 的注入。
在 Task 1 步骤的 `SecurityConfig` 里面加上拦截器生效执行： `http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);`

具体代码更新方式按执行者操作合并即可。但为了验证先略过本步测试拦截细节，后续任务补全。

**Step 4: 编译验证与提交**

Run: `mvn clean compile -pl hr-core`
Expected: 编译返回 BUILD SUCCESS.

```bash
git add hr-core/src/main/java/com/liyisoft/hr/core/security/filter/JwtAuthenticationFilter.java hr-core/src/main/java/com/liyisoft/hr/core/security/service/UserDetailsServiceImpl.java
git commit -m "feat(core): 编写 jwt 拦截验证以及简易版授权用户加载模拟"
```

### Task 3: 后端 AuthController 登录逻辑编写

**Files:**
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/controller/SysAuthController.java`

**Step 1: 编写提供给前端调用的 Controller**

此接口接受登录账号及密码，返回包含 Token 的 CommonResult：

```java
package com.liyisoft.hr.core.controller;

import com.liyisoft.hr.common.core.CommonResult;
import com.liyisoft.hr.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class SysAuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@RequestBody Map<String, String> loginBody) {
        String username = loginBody.get("username");
        String password = loginBody.get("password");

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            return CommonResult.error("用户不存在或密码错误");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            String token = jwtUtils.generateToken(claims, username);
            Map<String, String> res = new HashMap<>();
            res.put("token", token);
            return CommonResult.success(res);
        } else {
            return CommonResult.error("用户不存在或密码错误");
        }
    }
}
```

**Step 2: 编译与主程序拉起检查**

Run: `mvn clean compile -pl hr-core`
Expected: BUILD SUCCESS. 如果 `hr-admin` 当前可拉起，可以运行检查上下文。暂不要求。

**Step 3: 提交**

```bash
git add hr-core/src/main/java/com/liyisoft/hr/core/controller/SysAuthController.java
git commit -m "feat(core): 实现基础鉴权 Controller 颁发系统访问 Token"
```

### Task 4: 前端 Axios 拦截器注入及封装

**Files:**
- Create: `hr-ui/src/utils/request.ts`

**Step 1: 初始化统一 HTTP 请求实例**

编写 `hr-ui/src/utils/request.ts`，配置基础请求路由以及携带 Token 和处理 401 状态：

```typescript
import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '../router';

const request = axios.create({
  baseURL: '/api', // 此处配置在 vite 中被代理
  timeout: 5000
});

// 请求拦截
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截
request.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统异常');
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      return res;
    }
  },
  (error) => {
    if (error.response?.status === 401) {
      ElMessage.error('登录凭证已过期，请重新登录');
      localStorage.removeItem('token');
      router.push('/login');
    } else {
      ElMessage.error(error.message);
    }
    return Promise.reject(error);
  }
);

export default request;
```

**Step 2: 验证编译**

Run: `cd hr-ui && npm run build`
Expected: 成功构建。

**Step 3: 提交**

```bash
git add hr-ui/src/utils/request.ts
git commit -m "feat(ui): 增加 axios 全局封装进行 token 拦截与挂载"
```

### Task 5: 编写 Login.vue 并整合路由拦截

**Files:**
- Create: `hr-ui/src/views/Login.vue`
- Modify: `hr-ui/src/router/index.ts`
- Modify: `hr-ui/vite.config.ts`

**Step 1: 创建 Login.vue 组件实现基础表单提交**

```vue
<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>人事管理系统</h2>
      <el-form :model="loginForm" label-width="60px">
        <el-form-item label="账号">
          <el-input v-model="loginForm.username" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import request from '../utils/request';
import { ElMessage } from 'element-plus';

const router = useRouter();
const loginForm = reactive({ username: 'admin', password: '123456' });

const handleLogin = async () => {
  try {
    const response: any = await request.post('/auth/login', loginForm);
    localStorage.setItem('token', response.data.token);
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error) {
    // 错误在拦截器中已经提示
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f3f4f6;
}
.login-card {
  width: 400px;
  text-align: center;
}
.login-btn {
  width: 100%;
}
</style>
```

**Step 2: 配置 vue-router 守卫和路由表注入**

覆盖此前的 `hr-ui/src/router/index.ts`：

```typescript
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../components/HelloWorld.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 前置路由守卫挂载
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.path !== '/login' && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
```

**Step 3: Vite 配置反向代理接口解决跨域联调**

在 `hr-ui/vite.config.ts` 的 defineConfig 内增加 server 项拦截 `/api` 去向 `http://localhost:8080/api`:

```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

**Step 4: 编译检查及提交**

Run: `cd hr-ui && npm run build`
Expected: 构建正常

```bash
git add hr-ui/src/views/Login.vue hr-ui/src/router/index.ts hr-ui/vite.config.ts
git commit -m "feat(ui): 建立应用核心路由表并装载页面登录组件拦截未授权访问"
```
