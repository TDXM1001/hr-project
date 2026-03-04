import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';

export const constRoutes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../components/HelloWorld.vue')
      },
      {
        path: '/employee/org',
        name: 'OrgStructure',
        component: () => import('../views/Organization.vue') // 替换占位组件为真正实现
      },
      {
        path: '/employee/list',
        name: 'EmployeeList',
        component: () => import('../components/HelloWorld.vue')
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes: constRoutes
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
