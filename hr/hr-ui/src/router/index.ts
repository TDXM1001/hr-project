import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../components/HelloWorld.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 前置路由守卫挂载
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token');
  if (to.path !== '/login' && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
