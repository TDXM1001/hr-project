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
