# HRMS UI Phase 1 Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** 根据 UI 设计规范实现登录页和组织架构页面的视觉效果。

**Architecture:** 
- 登录页：Flex 响应式左右分栏布局。
- 组织架构页：基于 Element Plus 的 Layout 容器实现“侧边导航 + 顶栏 + 内容区（树+表）”。
- 样式方案：使用 Tailwind CSS 映射设计令牌，减少 ad-hoc 样式。

**Tech Stack:** 
- Vue 3 + Vite + TypeScript
- Element Plus (组件库)
- Tailwind CSS (CSS 框架)
- Pinia (状态管理)

---

### Task 1: 初始化 Tailwind CSS 环境

**Files:**
- Modify: `hr/hr-ui/package.json`
- Create: `hr/hr-ui/tailwind.config.js`
- Create: `hr/hr-ui/postcss.config.js`
- Modify: `hr/hr-ui/src/style.css`

**Step 1: 声明 Tailwind 依赖**
在 `devDependencies` 中添加 `tailwindcss`, `postcss`, `autoprefixer`。

**Step 2: 创建配置文件**
初始化 `tailwind.config.js`，并添加品牌主色：
```javascript
module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#4F46E5",
        "slate-900": "#0f172a",
        "slate-800": "#1e293b",
      },
      borderRadius: {
        'xl': '12px'
      }
    },
  },
  plugins: [],
}
```

**Step 3: 引入 Tailwind 指令**
在 `src/style.css` 中添加 `@tailwind base; @tailwind components; @tailwind utilities;` 并清除旧的 Demo 样式。

**Step 4: 运行安装并验证**
使用 `npm install` 安装新增依赖。

**Step 5: Commit**
```bash
git add .
git commit -m "feat: 集成 Tailwind CSS 并配置设计令牌"
```

---

### Task 2: 生成与配置登录背景资产

**Files:**
- Create: `hr/hr-ui/src/assets/login-bg.png`

**Step 1: 生成登录背景图**
使用 `generate_image` 生成一个 "modern 3D geometric abstract background with soft blue and purple lighting, corporate tech style"。

**Step 2: 验证文件存在性**
确认 `src/assets/login-bg.png` 已正确引用。

**Step 3: Commit**
```bash
git add .
git commit -m "asset: 添加登录页 3D 背景图片"
```

---

### Task 3: 登录页布局重写 (Login.vue)

**Files:**
- Modify: `hr/hr-ui/src/views/Login.vue`

**Step 1: 替换现有的 Login.vue 模板**
将原有的 `el-card` 改为左右分栏 `flex` 布局。

**Step 2: 应用设计令牌**
- 左侧：`bg-slate-50`, `w-1/2`, 文字 `#4F46E5`。
- 右侧：`w-1/2`, `max-w-md`, 按钮 `bg-primary`, `rounded-xl` (12px)。

**Step 3: 验证**
检查在不同屏幕宽度下的适配情况（1024px 以下隐藏左侧展示区）。

**Step 4: Commit**
```bash
git add .
git commit -m "style: 重写登录页 UI，实现左右分栏布局"
```

---

### Task 4: 侧边栏与顶栏样式优化 (Layout Components)

**Files:**
- Modify: `hr/hr-ui/src/layout/Sidebar.vue`
- Modify: `hr/hr-ui/src/layout/Header.vue`

**Step 1: 优化 Sidebar.vue**
- 背景色改为 `bg-slate-900` (#0f172a)。
- `el-menu` 背景设为透明，使用 `active-text-color="#ffffff"`。
- 激活项样式通过 `hover:bg-primary` 和全局 CSS 覆盖。

**Step 2: 优化 Header.vue**
- 使用 `el-breadcrumb` 展示“首页 / 组织架构”。
- 调整高度为 64px (h-16)，字体更紧凑。

**Step 3: Commit**
```bash
git add .
git commit -m "style: 优化全局布局，适配深色导航栏和面包屑"
```

---

### Task 5: 组织架构页面实现 (Organization.vue)

**Files:**
- Create: `hr/hr-ui/src/views/Organization.vue`
- Modify: `hr/hr-ui/src/router/index.ts`

**Step 1: 实现“左树右表”布局**
- `src/views/Organization.vue` 中封装两张 `el-card`。
- 使用 `flex h-[calc(100vh-120px)]` 确保铺满视口除去顶栏。

**Step 2: 表格数据对齐**
- 设置 `el-table-column` 的 `align="center"` 以支持“绝对居中对齐”。
- 为状态列添加胶囊组件。

**Step 3: 更新路由**
将 `router/index.ts` 中 `/employee/org` 的占位组件替换为真正的 `Organization.vue`。

**Step 4: Commit**
```bash
git add .
git commit -m "feat: 实现组织架构页面，完成左树右表布局"
```
