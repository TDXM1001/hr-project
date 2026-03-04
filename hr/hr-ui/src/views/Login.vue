<template>
  <div class="h-screen w-screen flex bg-white overflow-hidden font-sans">
    <!-- 左侧：品牌展示区 (仅在 lg 屏幕及以上显示) -->
    <div class="hidden lg:flex w-1/2 bg-slate-50 relative flex-col justify-between p-16 animate-fade-in">
      <div class="absolute inset-0 z-0 overflow-hidden">
        <!-- 引入生成的 3D 背景图 -->
        <img 
          src="../assets/login-bg.png" 
          class="w-full h-full object-cover opacity-90 scale-105 transition-transform duration-[20s] ease-linear transform hover:scale-110" 
          alt="Login Background"
        />
        <!-- 叠加渐变阴影提升文字可读性 -->
        <div class="absolute inset-0 bg-gradient-to-b from-slate-900/20 via-transparent to-slate-900/40"></div>
      </div>
      
      <!-- 顶部 Logo 标识 -->
      <div class="relative z-10 flex items-center">
        <div class="w-12 h-12 bg-primary rounded-xl flex items-center justify-center shadow-lg shadow-primary/30 mr-4">
          <el-icon :size="24" color="#fff"><Management /></el-icon>
        </div>
        <h1 class="text-3xl font-bold text-white tracking-tight drop-shadow-md">
          人事管理系统 (HRMS)
        </h1>
      </div>
      
      <!-- 底部品牌 Slogan -->
      <div class="relative z-10 border-l-4 border-primary pl-6 py-2">
        <p class="text-2xl text-white font-medium leading-relaxed drop-shadow-md">
          驱动组织效能 · 赋能企业创新
        </p>
        <p class="text-slate-200 mt-2 opacity-80">面向未来的数字化人力资源管理平台</p>
      </div>
    </div>
    
    <!-- 右侧：表单登录区 -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 bg-white">
      <div class="w-full max-w-md space-y-8 animate-slide-up">
        <div class="text-center lg:text-left">
          <h2 class="text-3xl font-extrabold text-slate-900 tracking-tight">系统登录</h2>
          <p class="mt-3 text-slate-500 text-lg">欢迎回来，请使用您的管理员账号继续</p>
        </div>
        
        <el-form 
          :model="loginForm" 
          label-position="top" 
          size="large" 
          class="mt-10 space-y-6"
          @keyup.enter="handleLogin"
        >
          <el-form-item label="管理员账号" class="font-medium">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入管理员账号" 
              class="custom-input shadow-sm"
              prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item label="访问密码" class="font-medium">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="请输入 6 位以上密码" 
              show-password
              class="custom-input shadow-sm"
              prefix-icon="Lock"
            />
          </el-form-item>
          
          <div class="flex items-center justify-between">
            <el-checkbox v-model="rememberMe" class="!text-slate-600">记住登录状态</el-checkbox>
            <el-link type="primary" :underline="false" class="font-medium hover:opacity-80 transition-opacity">
              忘记密码？
            </el-link>
          </div>
          
          <div class="pt-4">
            <el-button 
              type="primary" 
              class="w-full !h-14 !text-lg font-bold !rounded-xl !border-none !bg-primary hover:!bg-indigo-700 shadow-xl shadow-primary/20 transition-all active:scale-[0.98]" 
              @click="handleLogin"
              :loading="loading"
            >
              即 刻 登 录
            </el-button>
          </div>
        </el-form>
        
        <div class="pt-8 text-center">
          <p class="text-slate-400 text-sm">© 2026 HRMS Enterprise Division. All rights reserved.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import request from '../utils/request';
import { ElMessage } from 'element-plus';
import { User, Lock, Management } from '@element-plus/icons-vue';

const router = useRouter();
const loading = ref(false);
const rememberMe = ref(true);

// 表单数据初始化
const loginForm = reactive({ 
  username: 'admin', 
  password: '123456' 
});

/**
 * 处理登录逻辑
 * 包含 Token 存储与路由跳转
 */
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('请完整填写登录信息');
    return;
  }
  
  loading.value = true;
  try {
    const response: any = await request.post('/auth/login', loginForm);
    // 存储认证令牌
    localStorage.setItem('token', response.data.token);
    ElMessage.success({
      message: '欢迎回家人事系统',
      duration: 2000
    });
    router.push('/');
  } catch (error) {
    console.error('登录异常:', error);
    // 拦截器已处理具体错误提示
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* 注入自定义输入框圆角和阴影 */
:deep(.custom-input .el-input__wrapper) {
  border-radius: 12px !important;
  padding-left: 12px;
  background-color: #f8fafc;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05) !important;
}

:deep(.custom-input .el-input__wrapper.is-focus) {
  background-color: #fff;
  box-shadow: 0 0 0 1px #4F46E5 !important;
}

/* 核心动画定义 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.8s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.6s cubic-bezier(0.16, 1, 0.3, 1);
}
</style>
