import api from '../../api'

/**
 * 商品模块
 * 管理商品相关状态和操作
 */
const product = {
  namespaced: true,

  // 状态
  state: {
    // 商品分类列表
    categories: [],
    // 分类树结构
    categoryTree: [],
    // 导航分类
    navCategories: [],
    // 商品列表
    productList: [],
    // 商品列表总数
    productTotal: 0,
    // 当前商品详情
    currentProduct: null,
    // 推荐商品
    recommendProducts: [],
    // 热销商品
    hotProducts: [],
    // 新品商品
    newProducts: [],
    // 搜索关键词
    searchKeyword: '',
    // 当前查询参数
    currentQuery: {}
  },

  // 同步修改状态
  mutations: {
    /**
     * 设置商品分类列表
     */
    SET_CATEGORIES(state, categories) {
      state.categories = categories
    },

    /**
     * 设置分类树结构
     */
    SET_CATEGORY_TREE(state, tree) {
      state.categoryTree = tree
    },

    /**
     * 设置导航分类
     */
    SET_NAV_CATEGORIES(state, categories) {
      state.navCategories = categories
    },

    /**
     * 设置商品列表
     */
    SET_PRODUCT_LIST(state, { list, total }) {
      state.productList = list
      state.productTotal = total
    },

    /**
     * 设置当前商品详情
     */
    SET_CURRENT_PRODUCT(state, product) {
      state.currentProduct = product
    },

    /**
     * 设置推荐商品
     */
    SET_RECOMMEND_PRODUCTS(state, products) {
      state.recommendProducts = products
    },

    /**
     * 设置热销商品
     */
    SET_HOT_PRODUCTS(state, products) {
      state.hotProducts = products
    },

    /**
     * 设置新品商品
     */
    SET_NEW_PRODUCTS(state, products) {
      state.newProducts = products
    },

    /**
     * 设置搜索关键词
     */
    SET_SEARCH_KEYWORD(state, keyword) {
      state.searchKeyword = keyword
    },

    /**
     * 设置当前查询参数
     */
    SET_CURRENT_QUERY(state, query) {
      state.currentQuery = query
    },

    /**
     * 清除当前商品详情
     */
    CLEAR_CURRENT_PRODUCT(state) {
      state.currentProduct = null
    }
  },

  // 异步操作
  actions: {
    /**
     * 获取商品分类列表
     */
    async getCategoryList({ commit, dispatch }) {
      try {
        const response = await api.category.getCategoryList()
        const categories = response.data.data || []
        commit('SET_CATEGORIES', categories)
        return Promise.resolve(categories)
      } catch (error) {
        dispatch('showError', '获取分类列表失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取分类树结构
     */
    async getCategoryTree({ commit, dispatch }) {
      try {
        const response = await api.category.getCategoryTree()
        const tree = response.data.data || []
        commit('SET_CATEGORY_TREE', tree)
        return Promise.resolve(tree)
      } catch (error) {
        dispatch('showError', '获取分类树失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取导航分类
     */
    async getNavCategories({ commit, dispatch }) {
      try {
        const response = await api.category.getNavCategories()
        const categories = response.data.data || []
        commit('SET_NAV_CATEGORIES', categories)
        return Promise.resolve(categories)
      } catch (error) {
        dispatch('showError', '获取导航分类失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取商品列表
     */
    async getProductList({ commit, dispatch }, params = {}) {
      try {
        commit('SET_CURRENT_QUERY', params)
        const response = await api.product.getProductList(params)
        const { list, total } = response.data.data
        commit('SET_PRODUCT_LIST', { list: list || [], total: total || 0 })
        return Promise.resolve({ list, total })
      } catch (error) {
        dispatch('showError', '获取商品列表失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取商品详情
     */
    async getProductDetail({ commit, dispatch }, productId) {
      try {
        const response = await api.product.getProductDetail(productId)
        const product = response.data.data
        commit('SET_CURRENT_PRODUCT', product)
        return Promise.resolve(product)
      } catch (error) {
        dispatch('showError', '获取商品详情失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 搜索商品
     */
    async searchProducts({ commit, dispatch }, params = {}) {
      try {
        commit('SET_SEARCH_KEYWORD', params.keyword || '')
        commit('SET_CURRENT_QUERY', params)
        const response = await api.product.searchProducts(params)
        const { list, total } = response.data.data
        commit('SET_PRODUCT_LIST', { list: list || [], total: total || 0 })
        return Promise.resolve({ list, total })
      } catch (error) {
        dispatch('showError', '搜索商品失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取推荐商品
     */
    async getRecommendProducts({ commit, dispatch }, limit = 10) {
      try {
        const response = await api.product.getRecommendProducts(limit)
        const products = response.data.data || []
        commit('SET_RECOMMEND_PRODUCTS', products)
        return Promise.resolve(products)
      } catch (error) {
        dispatch('showError', '获取推荐商品失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取热销商品
     */
    async getHotProducts({ commit, dispatch }, limit = 10) {
      try {
        const response = await api.product.getHotProducts(limit)
        const products = response.data.data || []
        commit('SET_HOT_PRODUCTS', products)
        return Promise.resolve(products)
      } catch (error) {
        dispatch('showError', '获取热销商品失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 获取新品商品
     */
    async getNewProducts({ commit, dispatch }, limit = 10) {
      try {
        const response = await api.product.getNewProducts(limit)
        const products = response.data.data || []
        commit('SET_NEW_PRODUCTS', products)
        return Promise.resolve(products)
      } catch (error) {
        dispatch('showError', '获取新品商品失败', { root: true })
        return Promise.reject(error)
      }
    },

    /**
     * 清除当前商品详情
     */
    clearCurrentProduct({ commit }) {
      commit('CLEAR_CURRENT_PRODUCT')
    }
  },

  // 计算属性
  getters: {
    /**
     * 商品分类列表
     */
    categories: state => state.categories,

    /**
     * 分类树结构
     */
    categoryTree: state => state.categoryTree,

    /**
     * 导航分类
     */
    navCategories: state => state.navCategories,

    /**
     * 商品列表
     */
    productList: state => state.productList,

    /**
     * 商品列表总数
     */
    productTotal: state => state.productTotal,

    /**
     * 当前商品详情
     */
    currentProduct: state => state.currentProduct,

    /**
     * 推荐商品
     */
    recommendProducts: state => state.recommendProducts,

    /**
     * 热销商品
     */
    hotProducts: state => state.hotProducts,

    /**
     * 新品商品
     */
    newProducts: state => state.newProducts,

    /**
     * 搜索关键词
     */
    searchKeyword: state => state.searchKeyword,

    /**
     * 当前查询参数
     */
    currentQuery: state => state.currentQuery,

    /**
     * 根据ID获取分类信息
     */
    getCategoryById: state => id => {
      return state.categories.find(category => category.categoryId === id)
    }
  }
}

export default product
