import { createStore } from 'vuex'
import auth from './modules/auth'
import cart from './modules/cart'
import product from './modules/product'

/**
 * Vuex状态管理
 * 管理应用的全局状态
 */
export default createStore({
  // 全局状态
  state: {
    // 应用加载状态
    loading: false,
    // 全局消息提示
    message: {
      show: false,
      type: 'info',
      text: ''
    }
  },

  // 同步修改状态的方法
  mutations: {
    /**
     * 设置加载状态
     */
    SET_LOADING(state, loading) {
      state.loading = loading
    },

    /**
     * 显示消息提示
     */
    SHOW_MESSAGE(state, { type, text }) {
      state.message = {
        show: true,
        type,
        text
      }
    },

    /**
     * 隐藏消息提示
     */
    HIDE_MESSAGE(state) {
      state.message.show = false
    }
  },

  // 异步操作和复杂逻辑
  actions: {
    /**
     * 显示加载状态
     */
    showLoading({ commit }) {
      commit('SET_LOADING', true)
    },

    /**
     * 隐藏加载状态
     */
    hideLoading({ commit }) {
      commit('SET_LOADING', false)
    },

    /**
     * 显示成功消息
     */
    showSuccess({ commit }, text) {
      commit('SHOW_MESSAGE', { type: 'success', text })
      setTimeout(() => {
        commit('HIDE_MESSAGE')
      }, 3000)
    },

    /**
     * 显示错误消息
     */
    showError({ commit }, text) {
      commit('SHOW_MESSAGE', { type: 'error', text })
      setTimeout(() => {
        commit('HIDE_MESSAGE')
      }, 5000)
    },

    /**
     * 显示警告消息
     */
    showWarning({ commit }, text) {
      commit('SHOW_MESSAGE', { type: 'warning', text })
      setTimeout(() => {
        commit('HIDE_MESSAGE')
      }, 4000)
    },

    /**
     * 显示信息消息
     */
    showInfo({ commit }, text) {
      commit('SHOW_MESSAGE', { type: 'info', text })
      setTimeout(() => {
        commit('HIDE_MESSAGE')
      }, 3000)
    }
  },

  // 计算属性
  getters: {
    /**
     * 是否正在加载
     */
    isLoading: state => state.loading,

    /**
     * 消息提示信息
     */
    message: state => state.message
  },

  // 模块化状态管理
  modules: {
    auth,    // 用户认证模块
    cart,    // 购物车模块
    product  // 商品模块
  }
})
