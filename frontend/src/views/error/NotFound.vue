<template>
  <div class="not-found-page">
    <div class="container">
      <div class="error-content">
        <!-- 404图标 -->
        <div class="error-icon">
          <div class="number-404">
            <span class="four">4</span>
            <span class="zero">0</span>
            <span class="four">4</span>
          </div>
        </div>

        <!-- 错误信息 -->
        <div class="error-info">
          <h1 class="error-title">页面不存在</h1>
          <p class="error-desc">抱歉，您访问的页面不存在或已被删除</p>
          <p class="error-suggestion">请检查网址是否正确，或者返回首页继续浏览</p>
        </div>

        <!-- 操作按钮 -->
        <div class="error-actions">
          <el-button type="primary" size="large" @click="goHome">
            <el-icon><house /></el-icon>
            返回首页
          </el-button>
          <el-button size="large" @click="goBack">
            <el-icon><back /></el-icon>
            返回上页
          </el-button>
        </div>

        <!-- 推荐链接 -->
        <div class="recommend-links">
          <h3>您可能感兴趣的页面：</h3>
          <div class="links-grid">
            <router-link to="/" class="link-item">
              <el-icon><house /></el-icon>
              <span>首页</span>
            </router-link>
            <router-link to="/products" class="link-item">
              <el-icon><goods /></el-icon>
              <span>商品列表</span>
            </router-link>
            <router-link to="/cart" class="link-item">
              <el-icon><shopping-cart /></el-icon>
              <span>购物车</span>
            </router-link>
            <router-link to="/profile" class="link-item">
              <el-icon><user /></el-icon>
              <span>个人中心</span>
            </router-link>
          </div>
        </div>
      </div>

      <!-- 装饰元素 */
      <div class="decoration">
        <div class="floating-element element-1"></div>
        <div class="floating-element element-2"></div>
        <div class="floating-element element-3"></div>
        <div class="floating-element element-4"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { House, Back, Goods, ShoppingCart, User } from '@element-plus/icons-vue'

export default {
  name: 'NotFound',
  components: {
    House,
    Back,
    Goods,
    ShoppingCart,
    User
  },
  setup() {
    const router = useRouter()

    const goHome = () => {
      router.push('/')
    }

    const goBack = () => {
      router.go(-1)
    }

    return {
      goHome,
      goBack
    }
  }
}
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.container {
  position: relative;
  z-index: 2;
}

.error-content {
  text-align: center;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 60px 40px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  max-width: 600px;
}

/* 404数字 */
.error-icon {
  margin-bottom: 40px;
}

.number-404 {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  font-size: 120px;
  font-weight: bold;
  line-height: 1;
}

.four {
  color: var(--jd-primary);
  animation: bounce 2s infinite;
}

.zero {
  color: var(--jd-secondary);
  animation: bounce 2s infinite 0.2s;
}

.four:last-child {
  animation-delay: 0.4s;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-20px);
  }
  60% {
    transform: translateY(-10px);
  }
}

/* 错误信息 */
.error-info {
  margin-bottom: 40px;
}

.error-title {
  font-size: 32px;
  font-weight: bold;
  color: var(--jd-text-primary);
  margin-bottom: 16px;
}

.error-desc {
  font-size: 18px;
  color: var(--jd-text-secondary);
  margin-bottom: 8px;
}

.error-suggestion {
  font-size: 14px;
  color: var(--jd-text-lighter);
}

/* 操作按钮 */
.error-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 40px;
}

.error-actions .el-button {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 25px;
}

/* 推荐链接 */
.recommend-links h3 {
  font-size: 18px;
  color: var(--jd-text-primary);
  margin-bottom: 20px;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.link-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(228, 57, 60, 0.1);
  border-radius: 8px;
  color: var(--jd-primary);
  text-decoration: none;
  transition: all 0.3s;
  font-size: 14px;
}

.link-item:hover {
  background: rgba(228, 57, 60, 0.2);
  transform: translateY(-2px);
}

/* 装饰元素 */
.decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.floating-element {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.element-1 {
  width: 80px;
  height: 80px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.element-2 {
  width: 60px;
  height: 60px;
  top: 20%;
  right: 15%;
  animation-delay: 1s;
}

.element-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 15%;
  animation-delay: 2s;
}

.element-4 {
  width: 40px;
  height: 40px;
  bottom: 10%;
  right: 10%;
  animation-delay: 3s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .error-content {
    padding: 40px 20px;
    margin: 0 10px;
  }
  
  .number-404 {
    font-size: 80px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .error-desc {
    font-size: 16px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .error-actions .el-button {
    width: 200px;
  }
  
  .links-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .number-404 {
    font-size: 60px;
  }
  
  .error-title {
    font-size: 20px;
  }
  
  .error-desc {
    font-size: 14px;
  }
}
</style>
