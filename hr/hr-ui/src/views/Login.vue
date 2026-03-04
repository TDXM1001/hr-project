<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>人事管理系统</h2>
      <el-form :model="loginForm" label-width="60px">
        <el-form-item label="账号">
          <el-input v-model="loginForm.username" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import request from '../utils/request';
import { ElMessage } from 'element-plus';

const router = useRouter();
const loginForm = reactive({ username: 'admin', password: '123456' });

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
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f3f4f6;
}
.login-card {
  width: 400px;
  text-align: center;
}
.login-btn {
  width: 100%;
}
</style>
