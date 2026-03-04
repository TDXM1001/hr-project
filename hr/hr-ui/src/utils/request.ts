import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '../router';

const request = axios.create({
  baseURL: '/api', // 此处配置在 vite 中被代理
  timeout: 5000
});

// 请求拦截
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截
request.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code !== 200) {
      ElMessage.error(res.message || '系统异常');
      return Promise.reject(new Error(res.message || 'Error'));
    } else {
      return res;
    }
  },
  (error) => {
    if (error.response?.status === 401) {
      ElMessage.error('登录凭证已过期，请重新登录');
      localStorage.removeItem('token');
      router.push('/login');
    } else {
      ElMessage.error(error.message);
    }
    return Promise.reject(error);
  }
);

export default request;
