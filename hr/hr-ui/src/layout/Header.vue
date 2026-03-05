<template>
  <el-header class="h-16 bg-white shadow-sm flex justify-between items-center px-6 !border-b !border-slate-100">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ currentRouteName }}</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="user-info">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="cursor-pointer text-slate-700 hover:text-primary transition-colors flex items-center font-medium">
          <div class="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center mr-2 text-slate-500">
            <el-icon><User /></el-icon>
          </div>
          Admin <el-icon class="ml-1 text-sm"><arrow-down /></el-icon>
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
import { computed } from 'vue';
import { useUserStore } from '../store/user';
import { useRoute } from 'vue-router';
import { User, ArrowDown } from '@element-plus/icons-vue';

const userStore = useUserStore();
const route = useRoute();

const currentRouteName = computed(() => {
  if (route.name === 'OrgStructure') return '组织架构'
  return route.name || 'Dashboard'
});

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout();
  }
};
</script>
