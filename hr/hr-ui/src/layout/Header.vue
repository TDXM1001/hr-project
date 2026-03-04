<template>
  <el-header class="h-16 bg-white border-b border-slate-100 flex items-center justify-between px-8 z-50">
    <!-- 面包屑引导 -->
    <div class="flex items-center space-x-4">
      <el-breadcrumb separator="/" class="text-slate-400">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item v-if="currentPathTitle">{{ currentPathTitle }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    
    <!-- 用户交互区 -->
    <div class="flex items-center space-x-6">
      <div class="hidden md:flex items-center space-x-4 pr-4 border-r border-slate-100">
        <el-icon class="text-slate-400 cursor-pointer hover:text-primary transition-colors" :size="20"><Search /></el-icon>
        <el-icon class="text-slate-400 cursor-pointer hover:text-primary transition-colors" :size="20"><Bell /></el-icon>
      </div>
      
      <el-dropdown trigger="click" @command="handleCommand" class="focus:outline-none">
        <div class="flex items-center space-x-3 cursor-pointer group">
          <el-avatar 
            :size="36" 
            class="!bg-indigo-50 !text-primary font-bold shadow-sm group-hover:shadow-md transition-all"
          >
            A
          </el-avatar>
          <div class="flex flex-col text-left">
            <span class="text-sm font-bold text-slate-700 leading-tight">Admin Master</span>
            <span class="text-xs text-slate-400 scale-90 -ml-1">超级管理员</span>
          </div>
          <el-icon class="text-slate-300 group-hover:text-primary transition-colors"><ArrowDown /></el-icon>
        </div>
        
        <template #dropdown>
          <el-dropdown-menu class="custom-dropdown">
            <el-dropdown-item command="profile" icon="User">个人中心</el-dropdown-item>
            <el-dropdown-item command="settings" icon="Setting">系统设置</el-dropdown-item>
            <el-dropdown-item divided command="logout" icon="SwitchButton" class="!text-red-500">
              安全退出
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '../store/user';
import { Search, Bell, ArrowDown } from '@element-plus/icons-vue';

const route = useRoute();
const userStore = useUserStore();

/**
 * 根据当前路由获取面包屑显示名称
 */
const currentPathTitle = computed(() => {
  // 处理路由名称映射
  if (route.path === '/employee/org') return '组织架构';
  if (route.path === '/dashboard') return '工作控制台';
  return '';
});

/**
 * 处理用户下拉指令
 * @param command 操作类型
 */
const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout(); // 清除 Token 并跳转登录
  } else {
    // 处理个人中心等二级页面逻辑
    console.log(`执行操作: ${command}`);
  }
};
</script>

<style scoped>
/* 覆盖 Element 下拉菜单样式以符合极简风格 */
:deep(.el-header) {
  padding: 0 2rem;
}

.custom-dropdown :deep(.el-dropdown-menu__item) {
  padding: 8px 20px;
  font-size: 14px;
}
</style>
