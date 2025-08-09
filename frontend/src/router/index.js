import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

// 路由组件懒加载
const Home = () => import('../views/Home.vue')
const Login = () => import('../views/auth/Login.vue')
const Register = () => import('../views/auth/Register.vue')
const ProductList = () => import('../views/product/ProductList.vue')
const ProductDetail = () => import('../views/product/ProductDetail.vue')
const Cart = () => import('../views/cart/Cart.vue')
const Order = () => import('../views/order/Order.vue')
const OrderList = () => import('../views/order/OrderList.vue')
const Profile = () => import('../views/user/Profile.vue')
const NotFound = () => import('../views/error/NotFound.vue')

/**
 * 路由配置
 */
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '仿京东商城 - 首页',
      requiresAuth: false
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      title: '用户登录',
      requiresAuth: false,
      hideForAuth: true // 已登录用户不能访问
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: {
      title: '用户注册',
      requiresAuth: false,
      hideForAuth: true // 已登录用户不能访问
    }
  },
  {
    path: '/products',
    name: 'ProductList',
    component: ProductList,
    meta: {
      title: '商品列表',
      requiresAuth: false
    }
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: ProductDetail,
    meta: {
      title: '商品详情',
      requiresAuth: false
    }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: Cart,
    meta: {
      title: '购物车',
      requiresAuth: true
    }
  },
  {
    path: '/order',
    name: 'Order',
    component: Order,
    meta: {
      title: '确认订单',
      requiresAuth: true
    }
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: OrderList,
    meta: {
      title: '我的订单',
      requiresAuth: true
    }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: {
      title: '个人中心',
      requiresAuth: true
    }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: NotFound,
    meta: {
      title: '页面不存在',
      requiresAuth: false
    }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

/**
 * 创建路由实例
 */
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  // 路由切换时滚动到顶部
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

/**
 * 全局前置守卫
 * 处理路由权限验证和页面标题设置
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }

  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    if (store.getters.isLoggedIn) {
      next()
    } else {
      // 未登录，跳转到登录页面
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  } else if (to.meta.hideForAuth && store.getters.isLoggedIn) {
    // 已登录用户不能访问登录/注册页面
    next('/')
  } else {
    next()
  }
})

/**
 * 全局后置钩子
 * 路由切换完成后的处理
 */
router.afterEach((to, from) => {
  // 可以在这里添加页面访问统计等逻辑
  console.log(`路由切换: ${from.path} -> ${to.path}`)
})

export default router
