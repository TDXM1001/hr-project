import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../components/HelloWorld.vue') // 临时组件占位
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
