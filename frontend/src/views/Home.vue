<template>
  <div class="home">
    <!-- 轮播图 -->
    <section class="banner-section">
      <div class="container">
        <el-carousel height="400px" indicator-position="outside">
          <el-carousel-item v-for="(banner, index) in banners" :key="index">
            <div class="banner-item" :style="{ backgroundImage: `url(${banner.image})` }">
              <div class="banner-content">
                <h2 class="banner-title">{{ banner.title }}</h2>
                <p class="banner-subtitle">{{ banner.subtitle }}</p>
                <el-button type="primary" size="large" @click="goToBanner(banner)">
                  立即查看
                </el-button>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </section>

    <!-- 商品分类 -->
    <section class="category-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">商品分类</h2>
        </div>
        <div class="category-grid">
          <div 
            v-for="category in categories" 
            :key="category.categoryId"
            class="category-card"
            @click="goToCategory(category.categoryId)"
          >
            <div class="category-icon">
              <img :src="category.image || '/images/default-category.png'" :alt="category.categoryName">
            </div>
            <h3 class="category-name">{{ category.categoryName }}</h3>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐商品 -->
    <section class="recommend-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">为您推荐</h2>
          <router-link to="/products?recommend=1" class="more-link">查看更多</router-link>
        </div>
        <div class="product-grid">
          <ProductCard 
            v-for="product in recommendProducts" 
            :key="product.productId"
            :product="product"
            @add-to-cart="handleAddToCart"
          />
        </div>
      </div>
    </section>

    <!-- 热销商品 -->
    <section class="hot-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">热销商品</h2>
          <router-link to="/products?hot=1" class="more-link">查看更多</router-link>
        </div>
        <div class="product-grid">
          <ProductCard 
            v-for="product in hotProducts" 
            :key="product.productId"
            :product="product"
            @add-to-cart="handleAddToCart"
          />
        </div>
      </div>
    </section>

    <!-- 新品上市 -->
    <section class="new-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">新品上市</h2>
          <router-link to="/products?new=1" class="more-link">查看更多</router-link>
        </div>
        <div class="product-grid">
          <ProductCard 
            v-for="product in newProducts" 
            :key="product.productId"
            :product="product"
            @add-to-cart="handleAddToCart"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import ProductCard from '../components/product/ProductCard.vue'

export default {
  name: 'Home',
  components: {
    ProductCard
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    // 响应式数据
    const banners = ref([
      {
        id: 1,
        title: 'iPhone 15 Pro 新品上市',
        subtitle: '钛金属设计，A17 Pro芯片，专业级摄影',
        image: '/images/banner1.jpg',
        link: '/product/1'
      },
      {
        id: 2,
        title: '华为Mate 60 Pro',
        subtitle: '卫星通话，麒麟9000S，突破想象',
        image: '/images/banner2.jpg',
        link: '/product/2'
      },
      {
        id: 3,
        title: 'MacBook Pro 14',
        subtitle: 'M3芯片，专业性能，创意无界',
        image: '/images/banner3.jpg',
        link: '/product/3'
      }
    ])

    // 计算属性
    const categories = computed(() => store.getters['product/categories'].slice(0, 8))
    const recommendProducts = computed(() => store.getters['product/recommendProducts'])
    const hotProducts = computed(() => store.getters['product/hotProducts'])
    const newProducts = computed(() => store.getters['product/newProducts'])
    const isLoggedIn = computed(() => store.getters['auth/isLoggedIn'])

    // 方法
    const goToBanner = (banner) => {
      if (banner.link) {
        router.push(banner.link)
      }
    }

    const goToCategory = (categoryId) => {
      router.push({
        path: '/products',
        query: { categoryId }
      })
    }

    const handleAddToCart = async (product) => {
      if (!isLoggedIn.value) {
        store.dispatch('showWarning', '请先登录')
        router.push('/login')
        return
      }

      try {
        await store.dispatch('cart/addToCart', {
          productId: product.productId,
          quantity: 1
        })
      } catch (error) {
        console.error('添加到购物车失败:', error)
      }
    }

    // 生命周期
    onMounted(async () => {
      try {
        // 并行获取数据
        await Promise.all([
          store.dispatch('product/getCategoryList'),
          store.dispatch('product/getRecommendProducts', 8),
          store.dispatch('product/getHotProducts', 8),
          store.dispatch('product/getNewProducts', 8)
        ])
      } catch (error) {
        console.error('获取首页数据失败:', error)
      }
    })

    return {
      banners,
      categories,
      recommendProducts,
      hotProducts,
      newProducts,
      goToBanner,
      goToCategory,
      handleAddToCart
    }
  }
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}

/* 轮播图区域 */
.banner-section {
  margin-bottom: 40px;
}

.banner-item {
  height: 400px;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  position: relative;
}

.banner-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}

.banner-content {
  position: relative;
  z-index: 1;
  color: #fff;
  padding-left: 60px;
}

.banner-title {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 16px;
}

.banner-subtitle {
  font-size: 18px;
  margin-bottom: 24px;
  opacity: 0.9;
}

/* 通用区域样式 */
.category-section,
.recommend-section,
.hot-section,
.new-section {
  margin-bottom: 60px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--jd-primary);
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  color: var(--jd-text-primary);
}

.more-link {
  color: var(--jd-primary);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.more-link:hover {
  color: var(--jd-primary-hover);
}

/* 分类网格 */
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 20px;
}

.category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.category-icon {
  width: 60px;
  height: 60px;
  margin-bottom: 12px;
  border-radius: 50%;
  overflow: hidden;
}

.category-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.category-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--jd-text-primary);
  text-align: center;
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner-content {
    padding-left: 20px;
  }
  
  .banner-title {
    font-size: 24px;
  }
  
  .banner-subtitle {
    font-size: 14px;
  }
  
  .category-grid {
    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
    gap: 15px;
  }
  
  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 15px;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>
