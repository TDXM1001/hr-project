<template>
  <div class="h-screen w-screen flex bg-white overflow-hidden">
    <!-- 左侧：品牌展示区 -->
    <div class="hidden lg:flex w-1/2 bg-slate-50 relative flex-col justify-between p-12">
      <div class="absolute inset-0 z-0">
        <img src="../assets/login-bg.png" class="w-full h-full object-cover opacity-80" alt="Login background" />
      </div>
      <div class="relative z-10">
        <h1 class="text-3xl font-bold text-slate-800 flex items-center">
          <div class="w-10 h-10 bg-primary rounded-lg mr-3"></div>
          人事管理系统 (HRMS)
        </h1>
      </div>
      <div class="relative z-10">
        <p class="text-xl text-slate-600 font-medium">驱动组织效能 · 赋能企业创新</p>
      </div>
    </div>
    
    <!-- 右侧：表单区 -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8">
      <div class="w-full max-w-md">
        <h2 class="text-2xl font-bold text-slate-900 mb-2">系统登录</h2>
        <p class="text-slate-500 mb-8">欢迎回来，请输入您的管理员凭据</p>
        
        <el-form :model="loginForm" label-position="top" size="large">
          <el-form-item label="账号">
            <el-input v-model="loginForm.username" placeholder="请输入您的账号" class="rounded-xl" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <div class="flex items-center justify-between mb-6">
            <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
          <el-button type="primary" class="w-full h-12 bg-primary border-none rounded-xl text-lg" @click="handleLogin">
            登 录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import request from '../utils/request';
import { ElMessage } from 'element-plus';

const router = useRouter();
const loginForm = reactive({ username: 'admin', password: '123456' });
const rememberMe = ref(false);

const handleLogin = async () => {
  try {
    const response: any = await request.post('/auth/login', loginForm);
    localStorage.setItem('token', response.data.token);
    ElMessage.success('登录成功');
    router.push('/');
  } catch (error) {
    // 错误在拦截器中已经提示
  }
};
</script>

<style scoped>
/* 使用 Tailwind CSS 替代原有样式，去除不需要的冗余 CSS */
:deep(.el-input__wrapper) {
  border-radius: 12px;
}
</style>
