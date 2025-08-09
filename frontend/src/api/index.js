import axios from 'axios'
import store from '../store'
import router from '../router'
import Cookies from 'js-cookie'

/**
 * 创建axios实例
 */
const request = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * 请求拦截器
 * 在发送请求之前做些什么
 */
request.interceptors.request.use(
  config => {
    // 显示加载状态
    store.dispatch('showLoading')
    
    // 添加认证令牌
    const token = Cookies.get('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    return config
  },
  error => {
    // 隐藏加载状态
    store.dispatch('hideLoading')
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 * 对响应数据做点什么
 */
request.interceptors.response.use(
  response => {
    // 隐藏加载状态
    store.dispatch('hideLoading')
    
    // 检查响应状态
    const { code, message } = response.data
    if (code === 200) {
      return response
    } else {
      // 业务错误
      store.dispatch('showError', message || '请求失败')
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  error => {
    // 隐藏加载状态
    store.dispatch('hideLoading')
    
    // 处理HTTP错误
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // 未授权，清除登录状态并跳转到登录页
          store.dispatch('auth/logout')
          router.push('/login')
          break
        case 403:
          store.dispatch('showError', '没有权限访问')
          break
        case 404:
          store.dispatch('showError', '请求的资源不存在')
          break
        case 500:
          store.dispatch('showError', '服务器内部错误')
          break
        default:
          store.dispatch('showError', data?.message || '网络错误')
      }
    } else if (error.request) {
      // 网络错误
      store.dispatch('showError', '网络连接失败，请检查网络')
    } else {
      // 其他错误
      store.dispatch('showError', error.message || '请求失败')
    }
    
    return Promise.reject(error)
  }
)

/**
 * API接口定义
 */
const api = {
  // 用户认证相关接口
  auth: {
    // 用户登录
    login: (data) => request.post('/user/login', data),
    // 用户注册
    register: (data) => request.post('/user/register', data),
    // 获取当前用户信息
    getCurrentUser: () => request.get('/user/current'),
    // 更新用户信息
    updateUser: (data) => request.put('/user/update', data),
    // 修改密码
    changePassword: (data) => request.put('/user/password', null, { params: data }),
    // 检查用户名是否存在
    checkUsername: (username) => request.get('/user/check/username', { params: { username } }),
    // 检查邮箱是否存在
    checkEmail: (email) => request.get('/user/check/email', { params: { email } }),
    // 检查手机号是否存在
    checkPhone: (phone) => request.get('/user/check/phone', { params: { phone } })
  },

  // 商品相关接口
  product: {
    // 获取商品列表
    getProductList: (params) => request.get('/product/list', { params }),
    // 获取商品详情
    getProductDetail: (id) => request.get(`/product/${id}`),
    // 搜索商品
    searchProducts: (params) => request.get('/product/search', { params }),
    // 获取推荐商品
    getRecommendProducts: (limit = 10) => request.get('/product/recommend', { params: { limit } }),
    // 获取热销商品
    getHotProducts: (limit = 10) => request.get('/product/hot', { params: { limit } }),
    // 获取新品商品
    getNewProducts: (limit = 10) => request.get('/product/new', { params: { limit } })
  },

  // 商品分类相关接口
  category: {
    // 获取分类列表
    getCategoryList: () => request.get('/category/list'),
    // 获取分类树
    getCategoryTree: () => request.get('/category/tree'),
    // 获取导航分类
    getNavCategories: () => request.get('/category/nav')
  },

  // 购物车相关接口
  cart: {
    // 获取购物车列表
    getCartList: () => request.get('/cart/list'),
    // 添加商品到购物车
    addToCart: (data) => request.post('/cart/add', data),
    // 更新购物车商品数量
    updateCartItem: (data) => request.put('/cart/update', data),
    // 删除购物车商品
    removeCartItem: (cartId) => request.delete(`/cart/${cartId}`),
    // 清空购物车
    clearCart: () => request.delete('/cart/clear'),
    // 选中/取消选中购物车商品
    selectCartItem: (data) => request.put('/cart/select', data)
  },

  // 订单相关接口
  order: {
    // 创建订单
    createOrder: (data) => request.post('/order/create', data),
    // 获取订单列表
    getOrderList: (params) => request.get('/order/list', { params }),
    // 获取订单详情
    getOrderDetail: (orderId) => request.get(`/order/${orderId}`),
    // 取消订单
    cancelOrder: (orderId) => request.put(`/order/${orderId}/cancel`),
    // 确认收货
    confirmOrder: (orderId) => request.put(`/order/${orderId}/confirm`),
    // 删除订单
    deleteOrder: (orderId) => request.delete(`/order/${orderId}`)
  }
}

export default api
