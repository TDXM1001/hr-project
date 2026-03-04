# 人事管理系统 MVP Phase 5 实现计划

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 构建前端整体布局基础骨架 (Layout: 侧边栏菜单, 头部导航条, 主内容区)，实现后端基于用户的动态菜单查询接口以驱动前端左侧菜单动态渲染加载。

**Architecture:** `hr-core` 建立 `SysMenu` 基础相关实体并由 `SysMenuController` 暴露获取当前用户可见路由菜单树接口（当前阶段先返回模拟结构的侧边栏菜单配置）；`hr-ui` 构建基于 Element Plus 的 `Layout` 核心组件体系（包含 `Sidebar`, `Header` 等），通过 Pinia 以及 Vue Router 获取后端菜单并在前端渲染展示。

**Tech Stack:** Java 17, Spring Boot, Vue 3, Element Plus, Pinia, Vue Router.

---

### Task 1: hr-core 建立 SysMenu 相关数据实体与后端模拟获取树形结构

**Files:**
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/entity/SysMenu.java`
- Create: `hr-core/src/main/java/com/liyisoft/hr/core/controller/SysMenuController.java`

**Step 1: 建立 SysMenu 实体（目前不需要严格 JPA/Mybatis 映射，做普通类支持 JSON 返回）**

```java
package com.liyisoft.hr.core.entity;

import java.util.List;

public class SysMenu {
    private Long id;
    private String name;
    private String path;
    private String icon;
    private List<SysMenu> children;

    public SysMenu(Long id, String name, String path, String icon) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.icon = icon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public List<SysMenu> getChildren() { return children; }
    public void setChildren(List<SysMenu> children) { this.children = children; }
}
```

**Step 2: 创建 SysMenuController 返回模拟当前用户菜单**

```java
package com.liyisoft.hr.core.controller;

import com.liyisoft.hr.common.core.CommonResult;
import com.liyisoft.hr.core.entity.SysMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController {

    @GetMapping("/user-menus")
    public CommonResult<List<SysMenu>> getUserMenus() {
        // 模拟提供一套基础菜单，包含首页和员工管理
        SysMenu dashboard = new SysMenu(1L, "首页面板", "/dashboard", "House");
        SysMenu employeeGroup = new SysMenu(2L, "员工组织", "/employee", "User");
        
        SysMenu myProfile = new SysMenu(3L, "组织架构", "/employee/org", "Connection");
        SysMenu employeeList = new SysMenu(4L, "档案管理", "/employee/list", "List");
        
        employeeGroup.setChildren(Arrays.asList(myProfile, employeeList));
        
        return CommonResult.success(Arrays.asList(dashboard, employeeGroup));
    }
}
```

**Step 3: 运行检查并编译**

Run: `mvn clean compile -pl hr-core`
Expected: 编译成功。

**Step 4: 提交**

```bash
git add hr-core/src/main/java/com/liyisoft/hr/core/entity/SysMenu.java hr-core/src/main/java/com/liyisoft/hr/core/controller/SysMenuController.java
git commit -m "feat(core): 新增获取动态系统菜单业务以支持前端页面侧边栏渲染"
```

### Task 2: hr-ui 安装 icon 图标库并设置前端路由骨架

**Files:**
- Modify: `hr-ui/package.json`
- Modify: `hr-ui/src/main.ts`
- Modify: `hr-ui/src/router/index.ts`

**Step 1: 安装 Element Plus icon 图标支持**

Run: `cd hr-ui && npm install @element-plus/icons-vue`
Expected: 安装成功。

**Step 2: 注册 Icon 及 Element Plus（若此前未全局注册）并检查 `main.ts`**

修改 `hr-ui/src/main.ts` 确保挂载：

```typescript
import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.mount('#app')
```

**Step 3: 改造现有 router，将基础路由配置为嵌套路由**

修改 `hr-ui/src/router/index.ts`，我们将会在接下来的任务创建 Layout 和 Dashboard 组件，这里先配好：

```typescript
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

export const constRoutes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../components/HelloWorld.vue')
      },
      {
        path: '/employee/org',
        name: 'OrgStructure',
        component: () => import('../components/HelloWorld.vue') // 暂用HelloWorld占位
      },
      {
        path: '/employee/list',
        name: 'EmployeeList',
        component: () => import('../components/HelloWorld.vue') // 暂用HelloWorld占位
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes: constRoutes
});

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

**Step 4: 提交**

```bash
git add hr-ui/package.json hr-ui/package-lock.json hr-ui/src/main.ts hr-ui/src/router/index.ts
git commit -m "feat(ui): 引入 icon 支持并调整路由使用 layout 抽屉式嵌套布局路由"
```

### Task 3: hr-ui 使用 Pinia 存储后端菜单状态

**Files:**
- Create: `hr-ui/src/store/user.ts`

**Step 1: 建立获取菜单的全局状态，实现登出与重现缓存**

建立文件 `hr-ui/src/store/user.ts`：

```typescript
import { defineStore } from 'pinia';
import request from '../utils/request';
import router from '../router';

export const useUserStore = defineStore('user', {
  state: () => ({
    menus: [] as any[],
    token: localStorage.getItem('token') || ''
  }),
  actions: {
    async fetchMenus() {
      try {
        const res: any = await request.get('/system/menu/user-menus');
        this.menus = res.data;
        return this.menus;
      } catch (error) {
        return [];
      }
    },
    logout() {
      this.token = '';
      this.menus = [];
      localStorage.removeItem('token');
      router.push('/login');
    }
  }
});
```

**Step 2: 编译状态管理文件防错验证**

Run: `cd hr-ui && npm run build`
Expected: 成功构建。

**Step 3: 提交**

```bash
git add hr-ui/src/store/user.ts
git commit -m "feat(ui): 开发用于维护菜单缓存与认证登出交互的全局用户状态 store"
```

### Task 4: hr-ui 前端 Layout 系列组件 (Sidebar 及 Header 等界面搭建)

**Files:**
- Create: `hr-ui/src/layout/index.vue`
- Create: `hr-ui/src/layout/Sidebar.vue`
- Create: `hr-ui/src/layout/Header.vue`

**Step 1: 制作 Sidebar 侧边栏及对 Store 的渲染加载**

编写 `hr-ui/src/layout/Sidebar.vue`:
```vue
<template>
  <el-aside width="220px" class="sidebar">
    <div class="logo">人事管理系统</div>
    <el-menu
      :default-active="activeMenu"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      router
    >
      <template v-for="menu in menus" :key="menu.id">
        <!-- 无下级菜单 -->
        <el-menu-item v-if="!menu.children || menu.children.length === 0" :index="menu.path">
          <el-icon><component :is="menu.icon" /></el-icon>
          <span>{{ menu.name }}</span>
        </el-menu-item>
        
        <!-- 有下级菜单 -->
        <el-sub-menu v-else :index="menu.path">
          <template #title>
            <el-icon><component :is="menu.icon" /></el-icon>
            <span>{{ menu.name }}</span>
          </template>
          <el-menu-item v-for="child in menu.children" :key="child.id" :index="child.path">
            <el-icon><component :is="child.icon" /></el-icon>
            <span>{{ child.name }}</span>
          </el-menu-item>
        </el-sub-menu>
      </template>
    </el-menu>
  </el-aside>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '../store/user';

const route = useRoute();
const userStore = useUserStore();

const menus = computed(() => userStore.menus);
const activeMenu = computed(() => route.path);

onMounted(() => {
  if (userStore.menus.length === 0) {
    userStore.fetchMenus();
  }
});
</script>

<style scoped>
.sidebar {
  height: 100vh;
  background-color: #304156;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}
</style>
```

**Step 2: 制作 Header 顶部导航组件**

编写 `hr-ui/src/layout/Header.vue`:
```vue
<template>
  <el-header class="header">
    <div class="breadcrumb">欢迎来到人事与组织发展平台</div>
    <div class="user-info">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="el-dropdown-link">
          Admin <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup lang="ts">
import { useUserStore } from '../store/user';

const userStore = useUserStore();

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout();
  }
};
</script>

<style scoped>
.header {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}
.el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>
```

**Step 3: 制作 Layout 整体结构大外壳并使用全局 router-view**

编写 `hr-ui/src/layout/index.vue`:
```vue
<template>
  <el-container class="app-wrapper">
    <Sidebar />
    <el-container class="main-container">
      <Header />
      <el-main class="app-main">
        <!-- 路由匹配到的子组件 -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import Sidebar from './Sidebar.vue';
import Header from './Header.vue';
</script>

<style scoped>
.app-wrapper {
  height: 100vh;
  width: 100%;
}
.main-container {
  display: flex;
  flex-direction: column;
}
.app-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
```

**Step 4: 配置消除全局内外边距保证满屏**

修改 `hr-ui/src/App.vue`:
```vue
<template>
  <router-view />
</template>

<script setup lang="ts">
</script>

<style>
body {
  margin: 0;
  padding: 0;
}
</style>
```

**Step 5: 验证编译并提交**

Run: `cd hr-ui && npm run build`
Expected: 编译正常。

```bash
git add hr-ui/src/layout/index.vue hr-ui/src/layout/Sidebar.vue hr-ui/src/layout/Header.vue hr-ui/src/App.vue
git commit -m "feat(ui): 实现在 Layout 中的导航条与动态路由渲染侧边栏，联调主界面架子搭建完毕"
```
