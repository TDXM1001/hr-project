<template>
  <el-aside width="220px" class="h-screen bg-slate-900 flex flex-col">
    <div class="h-16 flex items-center px-4 text-white text-lg font-bold tracking-wider">
      <div class="w-6 h-6 bg-primary rounded mr-2 flex-shrink-0"></div>
      HRMS Base
    </div>
    <el-menu
      :default-active="activeMenu"
      background-color="transparent"
      text-color="#94a3b8"
      active-text-color="#ffffff"
      class="border-none flex-1 overflow-y-auto"
      router
    >
      <template v-for="menu in menus" :key="menu.id">
        <!-- 无下级菜单 -->
        <el-menu-item v-if="!menu.children || menu.children.length === 0" :index="menu.path" class="menu-item-custom">
          <el-icon><component :is="menu.icon" /></el-icon>
          <span>{{ menu.name }}</span>
        </el-menu-item>
        
        <!-- 有下级菜单 -->
        <el-sub-menu v-else :index="menu.path" class="menu-item-custom">
          <template #title>
            <el-icon><component :is="menu.icon" /></el-icon>
            <span>{{ menu.name }}</span>
          </template>
          <el-menu-item v-for="child in menu.children" :key="child.id" :index="child.path" class="menu-item-custom">
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
:deep(.el-menu) {
  border-right: none;
}
:deep(.menu-item-custom) {
  margin: 4px 12px;
  border-radius: 8px;
}
:deep(.el-menu-item.is-active) {
  background-color: var(--color-primary) !important;
}
:deep(.el-menu-item:hover), :deep(.el-sub-menu__title:hover) {
  background-color: rgba(255,255,255, 0.05) !important;
}
</style>
