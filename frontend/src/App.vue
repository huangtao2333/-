<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <Header v-if="showHeader" />
    
    <!-- 主要内容区域 -->
    <main class="main-content" :class="{ 'no-header': !showHeader }">
      <router-view />
    </main>
    
    <!-- 底部信息 -->
    <Footer v-if="showFooter" />
  </div>
</template>

<script>
import Header from './components/layout/Header.vue'
import Footer from './components/layout/Footer.vue'

export default {
  name: 'App',
  components: {
    Header,
    Footer
  },
  computed: {
    /**
     * 是否显示头部导航
     * 登录和注册页面不显示头部
     */
    showHeader() {
      const hideHeaderRoutes = ['/login', '/register']
      return !hideHeaderRoutes.includes(this.$route.path)
    },
    
    /**
     * 是否显示底部信息
     * 登录和注册页面不显示底部
     */
    showFooter() {
      const hideFooterRoutes = ['/login', '/register']
      return !hideFooterRoutes.includes(this.$route.path)
    }
  }
}
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  background-color: #f5f5f5;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding-top: 80px; /* 为固定头部留出空间 */
}

.main-content.no-header {
  padding-top: 0;
}

/* 链接样式 */
a {
  text-decoration: none;
  color: inherit;
}

a:hover {
  color: #e4393c;
}

/* 按钮样式 */
.btn-primary {
  background-color: #e4393c;
  border-color: #e4393c;
}

.btn-primary:hover {
  background-color: #c81623;
  border-color: #c81623;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
