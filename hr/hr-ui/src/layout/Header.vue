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
