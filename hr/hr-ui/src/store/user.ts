import { defineStore } from 'pinia';
import request from '../utils/request';
import router from '../router';

/**
 * 全局用户状态 Store
 * 负责管理用户登录凭证、动态菜单等数据
 */
export const useUserStore = defineStore('user', {
  state: () => ({
    menus: [] as any[],
    token: localStorage.getItem('token') || ''
  }),
  actions: {
    /**
     * 获取用户动态菜单树
     */
    async fetchMenus() {
      try {
        const res: any = await request.get('/system/menu/user-menus');
        // 将获取的菜单树存入状态中
        this.menus = res.data;
        return this.menus;
      } catch (error) {
        return [];
      }
    },

    /**
     * 用户退出登录，清空本地及内存缓存
     */
    logout() {
      this.token = '';
      this.menus = [];
      localStorage.removeItem('token');
      router.push('/login');
    }
  }
});
