<template>
  <div class="login-page">
    <div class="login-container">
      <!-- 左侧背景图 -->
      <div class="login-banner">
        <div class="banner-content">
          <h1 class="banner-title">欢迎来到仿京东商城</h1>
          <p class="banner-subtitle">品质生活，从这里开始</p>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-wrapper">
        <div class="login-form">
          <div class="form-header">
            <h2 class="form-title">用户登录</h2>
            <p class="form-subtitle">请输入您的账号和密码</p>
          </div>

          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            size="large"
            @submit.prevent="handleLogin"
          >
            <!-- 用户名 -->
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名/邮箱/手机号"
                prefix-icon="User"
                clearable
                @keyup.enter="handleLogin"
              />
            </el-form-item>

            <!-- 密码 -->
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                show-password
                clearable
                @keyup.enter="handleLogin"
              />
            </el-form-item>

            <!-- 记住我 -->
            <el-form-item>
              <div class="form-options">
                <el-checkbox v-model="loginForm.rememberMe">
                  记住我
                </el-checkbox>
                <a href="#" class="forgot-password">忘记密码？</a>
              </div>
            </el-form-item>

            <!-- 登录按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                @click="handleLogin"
                class="login-button"
              >
                {{ loading ? '登录中...' : '登录' }}
              </el-button>
            </el-form-item>

            <!-- 注册链接 -->
            <div class="register-link">
              <span>还没有账号？</span>
              <router-link to="/register" class="register-btn">立即注册</router-link>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  setup() {
    const store = useStore()
    const router = useRouter()
    const route = useRoute()

    // 表单引用
    const loginFormRef = ref()

    // 响应式数据
    const loginForm = reactive({
      username: '',
      password: '',
      rememberMe: false
    })

    // 表单验证规则
    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 2, max: 50, message: '用户名长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ]
    }

    // 计算属性
    const loading = computed(() => store.getters.isLoading)

    // 方法
    const handleLogin = async () => {
      if (!loginFormRef.value) return

      try {
        // 表单验证
        await loginFormRef.value.validate()

        // 执行登录
        await store.dispatch('auth/login', {
          username: loginForm.username,
          password: loginForm.password,
          rememberMe: loginForm.rememberMe
        })

        // 登录成功，跳转到目标页面或首页
        const redirect = route.query.redirect || '/'
        router.push(redirect)

      } catch (error) {
        if (error.errors) {
          // 表单验证错误
          console.log('表单验证失败:', error)
        } else {
          // 登录失败
          console.error('登录失败:', error)
        }
      }
    }

    return {
      loginFormRef,
      loginForm,
      loginRules,
      loading,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 1000px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  min-height: 600px;
}

/* 左侧背景图 */
.login-banner {
  flex: 1;
  background: linear-gradient(45deg, var(--jd-primary), var(--jd-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.login-banner::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('/images/login-bg.jpg') center/cover;
  opacity: 0.2;
}

.banner-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: #fff;
  padding: 40px;
}

.banner-title {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.banner-subtitle {
  font-size: 18px;
  opacity: 0.9;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* 右侧登录表单 */
.login-form-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-form {
  width: 100%;
  max-width: 400px;
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-title {
  font-size: 28px;
  font-weight: bold;
  color: var(--jd-text-primary);
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 14px;
  color: var(--jd-text-secondary);
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.forgot-password {
  color: var(--jd-primary);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.forgot-password:hover {
  color: var(--jd-primary-hover);
}

/* 登录按钮 */
.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: var(--jd-text-secondary);
}

.register-btn {
  color: var(--jd-primary);
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
  transition: color 0.3s;
}

.register-btn:hover {
  color: var(--jd-primary-hover);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    max-width: 400px;
    min-height: auto;
  }
  
  .login-banner {
    min-height: 200px;
  }
  
  .banner-title {
    font-size: 24px;
  }
  
  .banner-subtitle {
    font-size: 14px;
  }
  
  .login-form-wrapper {
    padding: 30px 20px;
  }
  
  .form-title {
    font-size: 24px;
  }
}

/* 表单项样式调整 */
:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 12px rgba(228, 57, 60, 0.3);
}

:deep(.el-button--primary) {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(228, 57, 60, 0.3);
  transition: all 0.3s;
}

:deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(228, 57, 60, 0.4);
}
</style>
