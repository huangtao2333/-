<template>
  <div class="register-page">
    <div class="register-container">
      <!-- 左侧背景图 -->
      <div class="register-banner">
        <div class="banner-content">
          <h1 class="banner-title">加入仿京东商城</h1>
          <p class="banner-subtitle">开启您的品质购物之旅</p>
        </div>
      </div>

      <!-- 右侧注册表单 -->
      <div class="register-form-wrapper">
        <div class="register-form">
          <div class="form-header">
            <h2 class="form-title">用户注册</h2>
            <p class="form-subtitle">请填写以下信息完成注册</p>
          </div>

          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            size="large"
            @submit.prevent="handleRegister"
          >
            <!-- 用户名 -->
            <el-form-item prop="username">
              <el-input
                v-model="registerForm.username"
                placeholder="请输入用户名"
                prefix-icon="User"
                clearable
              />
            </el-form-item>

            <!-- 邮箱 -->
            <el-form-item prop="email">
              <el-input
                v-model="registerForm.email"
                placeholder="请输入邮箱"
                prefix-icon="Message"
                clearable
              />
            </el-form-item>

            <!-- 手机号 -->
            <el-form-item prop="phone">
              <el-input
                v-model="registerForm.phone"
                placeholder="请输入手机号"
                prefix-icon="Phone"
                clearable
              />
            </el-form-item>

            <!-- 密码 -->
            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>

            <!-- 确认密码 -->
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="请确认密码"
                prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>

            <!-- 真实姓名 -->
            <el-form-item prop="realName">
              <el-input
                v-model="registerForm.realName"
                placeholder="请输入真实姓名（可选）"
                prefix-icon="UserFilled"
                clearable
              />
            </el-form-item>

            <!-- 性别 -->
            <el-form-item prop="gender">
              <el-radio-group v-model="registerForm.gender">
                <el-radio :label="0">保密</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 注册按钮 -->
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                @click="handleRegister"
                class="register-button"
              >
                {{ loading ? '注册中...' : '立即注册' }}
              </el-button>
            </el-form-item>

            <!-- 登录链接 -->
            <div class="login-link">
              <span>已有账号？</span>
              <router-link to="/login" class="login-btn">立即登录</router-link>
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
import { useRouter } from 'vue-router'

export default {
  name: 'Register',
  setup() {
    const store = useStore()
    const router = useRouter()

    // 表单引用
    const registerFormRef = ref()

    // 响应式数据
    const registerForm = reactive({
      username: '',
      email: '',
      phone: '',
      password: '',
      confirmPassword: '',
      realName: '',
      gender: 0
    })

    // 自定义验证规则
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    // 表单验证规则
    const registerRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 4, max: 20, message: '用户名长度在 4 到 20 个字符', trigger: 'blur' },
        { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字、下划线', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
      ]
    }

    // 计算属性
    const loading = computed(() => store.getters.isLoading)

    // 方法
    const handleRegister = async () => {
      if (!registerFormRef.value) return

      try {
        // 表单验证
        await registerFormRef.value.validate()

        // 执行注册
        await store.dispatch('auth/register', {
          username: registerForm.username,
          email: registerForm.email,
          phone: registerForm.phone,
          password: registerForm.password,
          confirmPassword: registerForm.confirmPassword,
          realName: registerForm.realName || undefined,
          gender: registerForm.gender
        })

        // 注册成功，跳转到登录页面
        router.push('/login')

      } catch (error) {
        if (error.errors) {
          // 表单验证错误
          console.log('表单验证失败:', error)
        } else {
          // 注册失败
          console.error('注册失败:', error)
        }
      }
    }

    return {
      registerFormRef,
      registerForm,
      registerRules,
      loading,
      handleRegister
    }
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-container {
  width: 100%;
  max-width: 1000px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  min-height: 700px;
}

/* 左侧背景图 */
.register-banner {
  flex: 1;
  background: linear-gradient(45deg, var(--jd-primary), var(--jd-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.register-banner::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('/images/register-bg.jpg') center/cover;
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

/* 右侧注册表单 */
.register-form-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.register-form {
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

/* 注册按钮 */
.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
}

/* 登录链接 */
.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: var(--jd-text-secondary);
}

.login-btn {
  color: var(--jd-primary);
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
  transition: color 0.3s;
}

.login-btn:hover {
  color: var(--jd-primary-hover);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-container {
    flex-direction: column;
    max-width: 400px;
    min-height: auto;
  }
  
  .register-banner {
    min-height: 200px;
  }
  
  .banner-title {
    font-size: 24px;
  }
  
  .banner-subtitle {
    font-size: 14px;
  }
  
  .register-form-wrapper {
    padding: 30px 20px;
  }
  
  .form-title {
    font-size: 24px;
  }
}

/* 表单项样式调整 */
:deep(.el-form-item) {
  margin-bottom: 20px;
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

:deep(.el-radio-group) {
  display: flex;
  gap: 20px;
}
</style>
