<template>
  <el-aside width="240px" class="h-screen bg-slate-900 flex flex-col shadow-2xl transition-all duration-300">
    <!-- 系统 Logo 区域 -->
    <div class="h-16 flex items-center px-6 bg-slate-800/50 backdrop-blur-sm">
      <div class="w-8 h-8 bg-primary rounded-lg flex items-center justify-center mr-3 scale-90">
        <el-icon color="#fff"><OfficeBuilding /></el-icon>
      </div>
      <span class="text-white text-lg font-bold tracking-wider">HRMS Base</span>
    </div>
    
    <!-- 业务菜单列表 -->
    <el-menu
      :default-active="activeMenu"
      background-color="transparent"
      text-color="#94a3b8"
      active-text-color="#ffffff"
      unique-opened
      router
      class="flex-1 !border-none pt-4"
    >
      <template v-for="menu in menus" :key="menu.id">
        <!-- 基础菜单项 (无子级) -->
        <el-menu-item 
          v-if="!menu.children || menu.children.length === 0" 
          :index="menu.path"
          class="menu-item-custom !mx-3 !rounded-xl !h-12 !mb-2"
        >
          <el-icon class="!mr-3"><component :is="menu.icon" /></el-icon>
          <template #title>
            <span class="font-medium">{{ menu.name }}</span>
          </template>
        </el-menu-item>
        
        <!-- 复合菜单组 (有子级) -->
        <el-sub-menu 
          v-else 
          :index="menu.path" 
          class="submenu-custom !mx-3 !mb-2"
        >
          <template #title>
            <el-icon class="!mr-3"><component :is="menu.icon" /></el-icon>
            <span class="font-medium">{{ menu.name }}</span>
          </template>
          <el-menu-item 
            v-for="child in menu.children" 
            :key="child.id" 
            :index="child.path"
            class="child-menu-item !rounded-lg !h-10 !mb-1"
          >
            <template #title>{{ child.name }}</template>
          </el-menu-item>
        </el-sub-menu>
      </template>
    </el-menu>
    
    <!-- 侧边栏底部统计或状态 -->
    <div class="p-4 bg-slate-800/20 border-t border-slate-700/50 text-slate-500 text-xs text-center italic">
      Core System v1.0.4
    </div>
  </el-aside>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '../store/user';
import { OfficeBuilding } from '@element-plus/icons-vue';

const route = useRoute();
const userStore = useUserStore();

/**
 * 动态加载后端菜单数据
 */
const menus = computed(() => userStore.menus);
const activeMenu = computed(() => {
  // 处理父子路径激活态映射
  return route.path;
});

onMounted(() => {
  // 初始化时若状态库为空则拉取菜单
  if (userStore.menus.length === 0) {
    userStore.fetchMenus();
  }
});
</script>

<style scoped>
/* 深度自定义 Element Plus 菜单样式以符合设计稿 */
:deep(.el-menu-item.is-active) {
  background-color: theme('colors.primary') !important;
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.4);
}

:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.05);
  color: #fff !important;
}

:deep(.el-sub-menu__title:hover) {
  background-color: transparent !important;
  color: #fff !important;
}

:deep(.el-sub-menu.is-active .el-sub-menu__title) {
  color: #fff !important;
}

/* 隐藏极简风格滚动条 */
.el-aside::-webkit-scrollbar {
  display: none;
}
</style>
