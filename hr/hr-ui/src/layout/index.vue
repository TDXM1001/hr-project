<template>
  <el-container class="h-screen w-screen overflow-hidden bg-slate-50">
    <Sidebar />
    <el-container class="flex flex-col h-full bg-slate-50">
      <Header />
      <el-main class="flex-1 p-6 relative overflow-hidden box-border">
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
