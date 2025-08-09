import api from '../../api'
import Cookies from 'js-cookie'

/**
 * 用户认证模块
 * 管理用户登录状态和用户信息
 */
const auth = {
  namespaced: true,

  // 状态
  state: {
    // 用户信息
    user: null,
    // JWT令牌
    token: Cookies.get('token') || null,
    // 登录状态
    isLoggedIn: false
  },

  // 同步修改状态
  mutations: {
    /**
     * 设置用户信息
     */
    SET_USER(state, user) {
      state.user = user
      state.isLoggedIn = !!user
    },

    /**
     * 设置令牌
     */
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        // 保存到Cookie，7天过期
        Cookies.set('token', token, { expires: 7 })
      } else {
        // 清除Cookie
        Cookies.remove('token')
      }
    },

    /**
     * 清除用户信息
     */
    CLEAR_USER(state) {
      state.user = null
      state.token = null
      state.isLoggedIn = false
      Cookies.remove('token')
    }
  },

  // 异步操作
  actions: {
    /**
     * 用户登录
     */
    async login({ commit, dispatch }, loginData) {
      try {
        dispatch('showLoading', null, { root: true })
        
        const response = await api.auth.login(loginData)
        const { data: token } = response.data
        
        // 保存令牌
        commit('SET_TOKEN', token)
        
        // 获取用户信息
        await dispatch('getCurrentUser')
        
        dispatch('showSuccess', '登录成功', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '登录失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      } finally {
        dispatch('hideLoading', null, { root: true })
      }
    },

    /**
     * 用户注册
     */
    async register({ dispatch }, registerData) {
      try {
        dispatch('showLoading', null, { root: true })
        
        await api.auth.register(registerData)
        
        dispatch('showSuccess', '注册成功，请登录', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '注册失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      } finally {
        dispatch('hideLoading', null, { root: true })
      }
    },

    /**
     * 获取当前用户信息
     */
    async getCurrentUser({ commit, dispatch, state }) {
      if (!state.token) {
        return Promise.reject(new Error('未登录'))
      }

      try {
        const response = await api.auth.getCurrentUser()
        const user = response.data.data
        
        commit('SET_USER', user)
        return Promise.resolve(user)
      } catch (error) {
        // 令牌无效，清除登录状态
        commit('CLEAR_USER')
        return Promise.reject(error)
      }
    },

    /**
     * 用户退出登录
     */
    logout({ commit, dispatch }) {
      commit('CLEAR_USER')
      dispatch('cart/clearCart', null, { root: true })
      dispatch('showInfo', '已退出登录', { root: true })
    },

    /**
     * 更新用户信息
     */
    async updateUser({ commit, dispatch }, updateData) {
      try {
        dispatch('showLoading', null, { root: true })
        
        await api.auth.updateUser(updateData)
        
        // 重新获取用户信息
        await dispatch('getCurrentUser')
        
        dispatch('showSuccess', '信息更新成功', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '更新失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      } finally {
        dispatch('hideLoading', null, { root: true })
      }
    },

    /**
     * 修改密码
     */
    async changePassword({ dispatch }, passwordData) {
      try {
        dispatch('showLoading', null, { root: true })
        
        await api.auth.changePassword(passwordData)
        
        dispatch('showSuccess', '密码修改成功', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '密码修改失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      } finally {
        dispatch('hideLoading', null, { root: true })
      }
    }
  },

  // 计算属性
  getters: {
    /**
     * 是否已登录
     */
    isLoggedIn: state => state.isLoggedIn,

    /**
     * 当前用户信息
     */
    currentUser: state => state.user,

    /**
     * 用户令牌
     */
    token: state => state.token,

    /**
     * 用户名
     */
    username: state => state.user?.username || '',

    /**
     * 用户头像
     */
    avatar: state => state.user?.avatar || '/images/default-avatar.png'
  }
}

export default auth
