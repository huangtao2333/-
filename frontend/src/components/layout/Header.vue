<template>
  <header class="header">
    <!-- 顶部导航栏 -->
    <div class="header-top">
      <div class="container">
        <div class="header-top-content">
          <div class="header-left">
            <span class="welcome-text">欢迎来到仿京东商城！</span>
          </div>
          <div class="header-right">
            <!-- 未登录状态 -->
            <div v-if="!isLoggedIn" class="auth-links">
              <router-link to="/login" class="auth-link">请先登录</router-link>
              <span class="divider">|</span>
              <router-link to="/register" class="auth-link">免费注册</router-link>
            </div>
            <!-- 已登录状态 -->
            <div v-else class="user-info">
              <el-dropdown @command="handleUserCommand">
                <span class="user-name">
                  {{ username }}
                  <el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                    <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                    <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <span class="divider">|</span>
            <router-link to="/orders" class="nav-link">我的订单</router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 主导航栏 -->
    <div class="header-main">
      <div class="container">
        <div class="header-main-content">
          <!-- Logo -->
          <div class="logo">
            <router-link to="/">
              <img src="/images/logo.png" alt="仿京东商城" class="logo-img">
            </router-link>
          </div>

          <!-- 搜索框 -->
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="请输入商品名称"
              class="search-input"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button 
                  type="primary" 
                  @click="handleSearch"
                  :icon="Search"
                >
                  搜索
                </el-button>
              </template>
            </el-input>
            <!-- 热门搜索词 -->
            <div class="hot-keywords">
              <span 
                v-for="keyword in hotKeywords" 
                :key="keyword"
                class="hot-keyword"
                @click="searchByKeyword(keyword)"
              >
                {{ keyword }}
              </span>
            </div>
          </div>

          <!-- 购物车 -->
          <div class="cart-box">
            <router-link to="/cart" class="cart-link">
              <el-badge :value="cartCount" :hidden="cartCount === 0">
                <el-icon class="cart-icon" size="24"><shopping-cart /></el-icon>
              </el-badge>
              <span class="cart-text">购物车</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 分类导航栏 -->
    <div class="header-nav">
      <div class="container">
        <div class="nav-content">
          <!-- 全部商品分类 -->
          <div class="category-menu">
            <el-dropdown placement="bottom-start">
              <div class="category-trigger">
                <el-icon><menu /></el-icon>
                <span>全部商品分类</span>
              </div>
              <template #dropdown>
                <div class="category-dropdown">
                  <div 
                    v-for="category in navCategories" 
                    :key="category.categoryId"
                    class="category-item"
                    @click="goToCategory(category.categoryId)"
                  >
                    <el-icon v-if="category.icon" class="category-icon">
                      <component :is="category.icon" />
                    </el-icon>
                    <span>{{ category.categoryName }}</span>
                  </div>
                </div>
              </template>
            </el-dropdown>
          </div>

          <!-- 导航链接 -->
          <nav class="nav-links">
            <router-link to="/" class="nav-link">首页</router-link>
            <router-link to="/products" class="nav-link">商品列表</router-link>
            <a href="#" class="nav-link">品牌特卖</a>
            <a href="#" class="nav-link">新品上市</a>
            <a href="#" class="nav-link">热门推荐</a>
          </nav>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { computed, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, ShoppingCart, ArrowDown, Menu } from '@element-plus/icons-vue'

export default {
  name: 'Header',
  components: {
    Search,
    ShoppingCart,
    ArrowDown,
    Menu
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    
    // 响应式数据
    const searchKeyword = ref('')
    const hotKeywords = ref(['iPhone 15', '华为Mate60', 'MacBook', '小米手机', '戴尔笔记本'])

    // 计算属性
    const isLoggedIn = computed(() => store.getters['auth/isLoggedIn'])
    const username = computed(() => store.getters['auth/username'])
    const cartCount = computed(() => store.getters['cart/totalCount'])
    const navCategories = computed(() => store.getters['product/navCategories'])

    // 方法
    const handleSearch = () => {
      if (!searchKeyword.value.trim()) {
        ElMessage.warning('请输入搜索关键词')
        return
      }
      
      router.push({
        path: '/products',
        query: { keyword: searchKeyword.value.trim() }
      })
    }

    const searchByKeyword = (keyword) => {
      searchKeyword.value = keyword
      handleSearch()
    }

    const handleUserCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'orders':
          router.push('/orders')
          break
        case 'logout':
          store.dispatch('auth/logout')
          router.push('/')
          break
      }
    }

    const goToCategory = (categoryId) => {
      router.push({
        path: '/products',
        query: { categoryId }
      })
    }

    // 生命周期
    onMounted(async () => {
      // 获取导航分类
      try {
        await store.dispatch('product/getNavCategories')
      } catch (error) {
        console.error('获取导航分类失败:', error)
      }

      // 如果已登录，获取购物车信息
      if (isLoggedIn.value) {
        try {
          await store.dispatch('cart/getCartList')
        } catch (error) {
          console.error('获取购物车失败:', error)
        }
      }
    })

    return {
      searchKeyword,
      hotKeywords,
      isLoggedIn,
      username,
      cartCount,
      navCategories,
      handleSearch,
      searchByKeyword,
      handleUserCommand,
      goToCategory
    }
  }
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 顶部导航栏 */
.header-top {
  background: #f5f5f5;
  height: 30px;
  line-height: 30px;
  font-size: 12px;
}

.header-top-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text {
  color: #666;
}

.header-right {
  display: flex;
  align-items: center;
}

.auth-links, .user-info {
  display: flex;
  align-items: center;
}

.auth-link, .nav-link {
  color: #666;
  text-decoration: none;
  transition: color 0.3s;
}

.auth-link:hover, .nav-link:hover {
  color: var(--jd-primary);
}

.user-name {
  color: var(--jd-primary);
  cursor: pointer;
  display: flex;
  align-items: center;
}

.divider {
  margin: 0 8px;
  color: #ccc;
}

/* 主导航栏 */
.header-main {
  height: 80px;
  background: #fff;
}

.header-main-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.logo {
  margin-right: 40px;
}

.logo-img {
  height: 40px;
}

.search-box {
  flex: 1;
  max-width: 600px;
  position: relative;
}

.search-input {
  width: 100%;
}

.hot-keywords {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-top: none;
  padding: 8px 12px;
  display: none;
}

.search-box:hover .hot-keywords {
  display: block;
}

.hot-keyword {
  display: inline-block;
  margin-right: 12px;
  color: #666;
  cursor: pointer;
  font-size: 12px;
}

.hot-keyword:hover {
  color: var(--jd-primary);
}

.cart-box {
  margin-left: 40px;
}

.cart-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #666;
  text-decoration: none;
  transition: color 0.3s;
}

.cart-link:hover {
  color: var(--jd-primary);
}

.cart-icon {
  margin-bottom: 4px;
}

.cart-text {
  font-size: 12px;
}

/* 分类导航栏 */
.header-nav {
  height: 50px;
  background: var(--jd-primary);
}

.nav-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.category-menu {
  margin-right: 40px;
}

.category-trigger {
  display: flex;
  align-items: center;
  color: #fff;
  cursor: pointer;
  padding: 0 16px;
  height: 50px;
  background: rgba(0, 0, 0, 0.1);
}

.category-trigger:hover {
  background: rgba(0, 0, 0, 0.2);
}

.category-dropdown {
  width: 200px;
  max-height: 400px;
  overflow-y: auto;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.category-item:hover {
  background: #f5f5f5;
}

.category-icon {
  margin-right: 8px;
}

.nav-links {
  display: flex;
  align-items: center;
}

.nav-links .nav-link {
  color: #fff;
  margin-right: 30px;
  text-decoration: none;
  transition: color 0.3s;
}

.nav-links .nav-link:hover,
.nav-links .nav-link.router-link-active {
  color: #ffeb3b;
}
</style>
