<template>
  <div class="product-card" @click="goToDetail">
    <!-- 商品图片 -->
    <div class="product-image">
      <img 
        :src="product.mainImage || '/images/default-product.png'" 
        :alt="product.productName"
        @error="handleImageError"
      >
      <!-- 标签 -->
      <div class="product-tags">
        <span v-if="product.isNew" class="tag tag-new">新品</span>
        <span v-if="product.isHot" class="tag tag-hot">热销</span>
        <span v-if="product.isRecommend" class="tag tag-recommend">推荐</span>
      </div>
    </div>

    <!-- 商品信息 -->
    <div class="product-info">
      <!-- 商品名称 -->
      <h3 class="product-name" :title="product.productName">
        {{ product.productName }}
      </h3>
      
      <!-- 商品副标题 -->
      <p v-if="product.subtitle" class="product-subtitle" :title="product.subtitle">
        {{ product.subtitle }}
      </p>

      <!-- 价格信息 -->
      <div class="price-info">
        <span class="current-price">¥{{ formatPrice(product.price) }}</span>
        <span v-if="product.originalPrice && product.originalPrice > product.price" class="original-price">
          ¥{{ formatPrice(product.originalPrice) }}
        </span>
      </div>

      <!-- 销量和评价 -->
      <div class="product-stats">
        <span class="sales-count">已售{{ product.salesCount || 0 }}件</span>
        <span class="view-count">{{ product.viewCount || 0 }}人浏览</span>
      </div>

      <!-- 操作按钮 -->
      <div class="product-actions">
        <el-button 
          type="primary" 
          size="small" 
          :disabled="!product.hasStock"
          @click.stop="handleAddToCart"
        >
          {{ product.hasStock ? '加入购物车' : '暂无库存' }}
        </el-button>
        <el-button 
          size="small" 
          @click.stop="handleBuyNow"
          :disabled="!product.hasStock"
        >
          立即购买
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    }
  },
  emits: ['add-to-cart', 'buy-now'],
  setup(props, { emit }) {
    const router = useRouter()

    // 方法
    const formatPrice = (price) => {
      if (!price) return '0.00'
      return parseFloat(price).toFixed(2)
    }

    const handleImageError = (event) => {
      event.target.src = '/images/default-product.png'
    }

    const goToDetail = () => {
      router.push(`/product/${props.product.productId}`)
    }

    const handleAddToCart = () => {
      if (!props.product.hasStock) return
      emit('add-to-cart', props.product)
    }

    const handleBuyNow = () => {
      if (!props.product.hasStock) return
      emit('buy-now', props.product)
    }

    return {
      formatPrice,
      handleImageError,
      goToDetail,
      handleAddToCart,
      handleBuyNow
    }
  }
}
</script>

<style scoped>
.product-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

/* 商品图片 */
.product-image {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

/* 商品标签 */
.product-tags {
  position: absolute;
  top: 8px;
  left: 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.tag {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 10px;
  color: #fff;
  font-weight: 500;
}

.tag-new {
  background: var(--jd-success);
}

.tag-hot {
  background: var(--jd-danger);
}

.tag-recommend {
  background: var(--jd-primary);
}

/* 商品信息 */
.product-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  color: var(--jd-text-primary);
  margin-bottom: 8px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-subtitle {
  font-size: 12px;
  color: var(--jd-text-secondary);
  margin-bottom: 12px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 价格信息 */
.price-info {
  margin-bottom: 12px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.current-price {
  font-size: 20px;
  font-weight: bold;
  color: var(--jd-primary);
}

.original-price {
  font-size: 14px;
  color: var(--jd-text-secondary);
  text-decoration: line-through;
}

/* 商品统计 */
.product-stats {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: var(--jd-text-secondary);
}

/* 操作按钮 */
.product-actions {
  margin-top: auto;
  display: flex;
  gap: 8px;
}

.product-actions .el-button {
  flex: 1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .product-image {
    height: 160px;
  }
  
  .product-info {
    padding: 12px;
  }
  
  .product-name {
    font-size: 14px;
  }
  
  .current-price {
    font-size: 18px;
  }
  
  .product-actions {
    flex-direction: column;
  }
}

/* 无库存状态 */
.product-card:has(.product-actions .el-button:disabled) {
  opacity: 0.7;
}

.product-card:has(.product-actions .el-button:disabled) .product-image::after {
  content: '暂无库存';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
}
</style>
