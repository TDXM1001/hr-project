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
