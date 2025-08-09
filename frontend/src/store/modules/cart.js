import api from '../../api'

/**
 * 购物车模块
 * 管理购物车状态和操作
 */
const cart = {
  namespaced: true,

  // 状态
  state: {
    // 购物车商品列表
    cartItems: [],
    // 购物车商品总数
    totalCount: 0,
    // 购物车总金额
    totalAmount: 0,
    // 选中的商品列表
    selectedItems: []
  },

  // 同步修改状态
  mutations: {
    /**
     * 设置购物车列表
     */
    SET_CART_ITEMS(state, items) {
      state.cartItems = items
      // 计算总数和总金额
      state.totalCount = items.reduce((total, item) => total + item.quantity, 0)
      state.totalAmount = items.reduce((total, item) => {
        return total + (item.selected ? item.price * item.quantity : 0)
      }, 0)
      // 更新选中商品列表
      state.selectedItems = items.filter(item => item.selected)
    },

    /**
     * 添加商品到购物车
     */
    ADD_CART_ITEM(state, item) {
      const existingItem = state.cartItems.find(cartItem => cartItem.productId === item.productId)
      if (existingItem) {
        // 如果商品已存在，增加数量
        existingItem.quantity += item.quantity
      } else {
        // 如果商品不存在，添加新商品
        state.cartItems.push({ ...item, selected: true })
      }
      // 重新计算总数和总金额
      state.totalCount = state.cartItems.reduce((total, item) => total + item.quantity, 0)
      state.totalAmount = state.cartItems.reduce((total, item) => {
        return total + (item.selected ? item.price * item.quantity : 0)
      }, 0)
      state.selectedItems = state.cartItems.filter(item => item.selected)
    },

    /**
     * 更新购物车商品数量
     */
    UPDATE_CART_ITEM(state, { cartId, quantity }) {
      const item = state.cartItems.find(item => item.cartId === cartId)
      if (item) {
        item.quantity = quantity
        // 重新计算总数和总金额
        state.totalCount = state.cartItems.reduce((total, item) => total + item.quantity, 0)
        state.totalAmount = state.cartItems.reduce((total, item) => {
          return total + (item.selected ? item.price * item.quantity : 0)
        }, 0)
        state.selectedItems = state.cartItems.filter(item => item.selected)
      }
    },

    /**
     * 删除购物车商品
     */
    REMOVE_CART_ITEM(state, cartId) {
      state.cartItems = state.cartItems.filter(item => item.cartId !== cartId)
      // 重新计算总数和总金额
      state.totalCount = state.cartItems.reduce((total, item) => total + item.quantity, 0)
      state.totalAmount = state.cartItems.reduce((total, item) => {
        return total + (item.selected ? item.price * item.quantity : 0)
      }, 0)
      state.selectedItems = state.cartItems.filter(item => item.selected)
    },

    /**
     * 选中/取消选中购物车商品
     */
    SELECT_CART_ITEM(state, { cartId, selected }) {
      const item = state.cartItems.find(item => item.cartId === cartId)
      if (item) {
        item.selected = selected
        // 重新计算总金额和选中商品列表
        state.totalAmount = state.cartItems.reduce((total, item) => {
          return total + (item.selected ? item.price * item.quantity : 0)
        }, 0)
        state.selectedItems = state.cartItems.filter(item => item.selected)
      }
    },

    /**
     * 全选/取消全选
     */
    SELECT_ALL_ITEMS(state, selected) {
      state.cartItems.forEach(item => {
        item.selected = selected
      })
      // 重新计算总金额和选中商品列表
      state.totalAmount = state.cartItems.reduce((total, item) => {
        return total + (item.selected ? item.price * item.quantity : 0)
      }, 0)
      state.selectedItems = state.cartItems.filter(item => item.selected)
    },

    /**
     * 清空购物车
     */
    CLEAR_CART(state) {
      state.cartItems = []
      state.totalCount = 0
      state.totalAmount = 0
      state.selectedItems = []
    }
  },

  // 异步操作
  actions: {
    /**
     * 获取购物车列表
     */
    async getCartList({ commit, dispatch }) {
      try {
        const response = await api.cart.getCartList()
        const cartItems = response.data.data || []
        commit('SET_CART_ITEMS', cartItems)
        return Promise.resolve(cartItems)
      } catch (error) {
        dispatch('showError', '获取购物车失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 添加商品到购物车
     */
    async addToCart({ commit, dispatch }, { productId, quantity = 1 }) {
      try {
        await api.cart.addToCart({ productId, quantity })
        
        // 重新获取购物车列表
        await dispatch('getCartList')
        
        dispatch('showSuccess', '已添加到购物车', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '添加到购物车失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 更新购物车商品数量
     */
    async updateCartItem({ commit, dispatch }, { cartId, quantity }) {
      try {
        await api.cart.updateCartItem({ cartId, quantity })
        commit('UPDATE_CART_ITEM', { cartId, quantity })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '更新失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 删除购物车商品
     */
    async removeCartItem({ commit, dispatch }, cartId) {
      try {
        await api.cart.removeCartItem(cartId)
        commit('REMOVE_CART_ITEM', cartId)
        dispatch('showSuccess', '商品已删除', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '删除失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 选中/取消选中购物车商品
     */
    async selectCartItem({ commit, dispatch }, { cartId, selected }) {
      try {
        await api.cart.selectCartItem({ cartId, selected })
        commit('SELECT_CART_ITEM', { cartId, selected })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '操作失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 全选/取消全选
     */
    async selectAllItems({ commit, dispatch, state }, selected) {
      try {
        // 批量更新选中状态
        const promises = state.cartItems.map(item => 
          api.cart.selectCartItem({ cartId: item.cartId, selected })
        )
        await Promise.all(promises)
        
        commit('SELECT_ALL_ITEMS', selected)
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '操作失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 清空购物车
     */
    async clearCart({ commit, dispatch }) {
      try {
        await api.cart.clearCart()
        commit('CLEAR_CART')
        dispatch('showSuccess', '购物车已清空', { root: true })
        return Promise.resolve()
      } catch (error) {
        const message = error.response?.data?.message || '清空失败'
        dispatch('showError', message, { root: true })
        return Promise.reject(error)
      }
    }
  },

  // 计算属性
  getters: {
    /**
     * 购物车商品列表
     */
    cartItems: state => state.cartItems,

    /**
     * 购物车商品总数
     */
    totalCount: state => state.totalCount,

    /**
     * 购物车总金额
     */
    totalAmount: state => state.totalAmount,

    /**
     * 选中的商品列表
     */
    selectedItems: state => state.selectedItems,

    /**
     * 选中商品的总数
     */
    selectedCount: state => state.selectedItems.reduce((total, item) => total + item.quantity, 0),

    /**
     * 是否全选
     */
    isAllSelected: state => state.cartItems.length > 0 && state.cartItems.every(item => item.selected),

    /**
     * 购物车是否为空
     */
    isEmpty: state => state.cartItems.length === 0
  }
}

export default cart
