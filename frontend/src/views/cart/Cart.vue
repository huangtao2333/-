<template>
  <div class="cart-page">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1 class="page-title">购物车</h1>
        <div class="cart-count">
          共 <span class="count">{{ cartItems.length }}</span> 件商品
        </div>
      </div>

      <!-- 购物车内容 -->
      <div v-if="!isEmpty" class="cart-content">
        <!-- 购物车表头 -->
        <div class="cart-header">
          <div class="select-all">
            <el-checkbox 
              v-model="isAllSelected" 
              @change="handleSelectAll"
              :indeterminate="isIndeterminate"
            >
              全选
            </el-checkbox>
          </div>
          <div class="product-info">商品信息</div>
          <div class="unit-price">单价</div>
          <div class="quantity">数量</div>
          <div class="subtotal">小计</div>
          <div class="actions">操作</div>
        </div>

        <!-- 购物车商品列表 -->
        <div class="cart-items">
          <div 
            v-for="item in cartItems" 
            :key="item.cartId"
            class="cart-item"
            :class="{ 'out-of-stock': !item.hasStock }"
          >
            <!-- 选择框 -->
            <div class="item-select">
              <el-checkbox 
                :model-value="item.selected === 1"
                @change="handleSelectItem(item, $event)"
                :disabled="!item.hasStock"
              />
            </div>

            <!-- 商品信息 -->
            <div class="item-product">
              <div class="product-image">
                <img 
                  :src="item.mainImage || '/images/default-product.png'" 
                  :alt="item.productName"
                  @click="goToProduct(item.productId)"
                >
                <div v-if="!item.hasStock" class="stock-mask">缺货</div>
              </div>
              <div class="product-details">
                <h3 class="product-name" @click="goToProduct(item.productId)">
                  {{ item.productName }}
                </h3>
                <div class="product-stock">
                  库存：{{ item.stock || 0 }}件
                </div>
              </div>
            </div>

            <!-- 单价 -->
            <div class="item-price">
              <span class="price">¥{{ formatPrice(item.price) }}</span>
            </div>

            <!-- 数量 -->
            <div class="item-quantity">
              <el-input-number
                v-model="item.quantity"
                :min="1"
                :max="item.stock"
                :disabled="!item.hasStock"
                @change="handleQuantityChange(item)"
                size="small"
              />
            </div>

            <!-- 小计 -->
            <div class="item-subtotal">
              <span class="subtotal-price">¥{{ formatPrice(item.totalPrice) }}</span>
            </div>

            <!-- 操作 -->
            <div class="item-actions">
              <el-button 
                type="text" 
                @click="handleRemoveItem(item)"
                class="remove-btn"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>

        <!-- 购物车底部 -->
        <div class="cart-footer">
          <div class="footer-left">
            <el-button @click="handleClearCart" type="text" class="clear-btn">
              清空购物车
            </el-button>
          </div>
          <div class="footer-right">
            <div class="total-info">
              <span class="selected-count">
                已选择 {{ selectedCount }} 件商品
              </span>
              <span class="total-amount">
                合计：<span class="amount">¥{{ formatPrice(totalAmount) }}</span>
              </span>
            </div>
            <el-button 
              type="primary" 
              size="large"
              :disabled="selectedCount === 0"
              @click="handleCheckout"
              class="checkout-btn"
            >
              去结算
            </el-button>
          </div>
        </div>
      </div>

      <!-- 空购物车 -->
      <div v-else class="empty-cart">
        <div class="empty-icon">
          <el-icon size="80"><shopping-cart /></el-icon>
        </div>
        <div class="empty-text">购物车还是空的</div>
        <div class="empty-desc">快去挑选喜欢的商品吧~</div>
        <el-button type="primary" @click="goToProducts" class="go-shopping-btn">
          去购物
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'

export default {
  name: 'Cart',
  components: {
    ShoppingCart
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    // 计算属性
    const cartItems = computed(() => store.getters['cart/cartItems'])
    const isEmpty = computed(() => store.getters['cart/isEmpty'])
    const selectedCount = computed(() => store.getters['cart/selectedCount'])
    const totalAmount = computed(() => store.getters['cart/totalAmount'])
    const isAllSelected = computed(() => store.getters['cart/isAllSelected'])
    
    // 半选状态
    const isIndeterminate = computed(() => {
      const selectedItems = cartItems.value.filter(item => item.selected === 1)
      return selectedItems.length > 0 && selectedItems.length < cartItems.value.length
    })

    // 方法
    const formatPrice = (price) => {
      if (!price) return '0.00'
      return parseFloat(price).toFixed(2)
    }

    const goToProduct = (productId) => {
      router.push(`/product/${productId}`)
    }

    const goToProducts = () => {
      router.push('/products')
    }

    const handleSelectAll = (selected) => {
      store.dispatch('cart/selectAllItems', selected)
    }

    const handleSelectItem = (item, selected) => {
      store.dispatch('cart/selectCartItem', {
        cartId: item.cartId,
        selected: selected ? 1 : 0
      })
    }

    const handleQuantityChange = (item) => {
      if (item.quantity < 1) {
        item.quantity = 1
        return
      }
      if (item.quantity > item.stock) {
        item.quantity = item.stock
        ElMessage.warning('商品数量不能超过库存')
        return
      }

      store.dispatch('cart/updateCartItem', {
        cartId: item.cartId,
        quantity: item.quantity
      })
    }

    const handleRemoveItem = async (item) => {
      try {
        await ElMessageBox.confirm(
          '确定要删除这件商品吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        await store.dispatch('cart/removeCartItem', item.cartId)
      } catch (error) {
        // 用户取消删除
      }
    }

    const handleClearCart = async () => {
      try {
        await ElMessageBox.confirm(
          '确定要清空购物车吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        await store.dispatch('cart/clearCart')
      } catch (error) {
        // 用户取消清空
      }
    }

    const handleCheckout = () => {
      if (selectedCount.value === 0) {
        ElMessage.warning('请选择要结算的商品')
        return
      }
      
      // 跳转到订单确认页面
      router.push('/order')
    }

    // 生命周期
    onMounted(async () => {
      try {
        await store.dispatch('cart/getCartList')
      } catch (error) {
        console.error('获取购物车失败:', error)
      }
    })

    return {
      cartItems,
      isEmpty,
      selectedCount,
      totalAmount,
      isAllSelected,
      isIndeterminate,
      formatPrice,
      goToProduct,
      goToProducts,
      handleSelectAll,
      handleSelectItem,
      handleQuantityChange,
      handleRemoveItem,
      handleClearCart,
      handleCheckout
    }
  }
}
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: var(--jd-text-primary);
}

.cart-count {
  font-size: 14px;
  color: var(--jd-text-secondary);
}

.count {
  color: var(--jd-primary);
  font-weight: bold;
}

/* 购物车内容 */
.cart-content {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.cart-header {
  display: grid;
  grid-template-columns: 60px 1fr 120px 150px 120px 80px;
  gap: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-bottom: 1px solid var(--jd-border);
  font-weight: 500;
  color: var(--jd-text-primary);
}

.cart-items {
  max-height: 600px;
  overflow-y: auto;
}

.cart-item {
  display: grid;
  grid-template-columns: 60px 1fr 120px 150px 120px 80px;
  gap: 20px;
  padding: 20px;
  border-bottom: 1px solid var(--jd-border);
  align-items: center;
  transition: background-color 0.3s;
}

.cart-item:hover {
  background: #f8f9fa;
}

.cart-item.out-of-stock {
  opacity: 0.6;
  background: #fafafa;
}

/* 商品信息 */
.item-product {
  display: flex;
  gap: 15px;
  align-items: center;
}

.product-image {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.stock-mask {
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
  font-size: 12px;
}

.product-details {
  flex: 1;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  color: var(--jd-text-primary);
  margin-bottom: 8px;
  cursor: pointer;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.product-name:hover {
  color: var(--jd-primary);
}

.product-stock {
  font-size: 12px;
  color: var(--jd-text-secondary);
}

/* 价格 */
.item-price .price {
  font-size: 18px;
  font-weight: bold;
  color: var(--jd-primary);
}

/* 小计 */
.item-subtotal .subtotal-price {
  font-size: 18px;
  font-weight: bold;
  color: var(--jd-primary);
}

/* 操作按钮 */
.remove-btn {
  color: var(--jd-text-secondary);
}

.remove-btn:hover {
  color: var(--jd-danger);
}

/* 购物车底部 */
.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-top: 1px solid var(--jd-border);
}

.clear-btn {
  color: var(--jd-text-secondary);
}

.clear-btn:hover {
  color: var(--jd-danger);
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.total-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 5px;
}

.selected-count {
  font-size: 14px;
  color: var(--jd-text-secondary);
}

.total-amount {
  font-size: 16px;
  color: var(--jd-text-primary);
}

.amount {
  font-size: 20px;
  font-weight: bold;
  color: var(--jd-primary);
}

.checkout-btn {
  padding: 12px 30px;
  font-size: 16px;
}

/* 空购物车 */
.empty-cart {
  text-align: center;
  padding: 80px 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  margin-bottom: 20px;
  color: var(--jd-text-lighter);
}

.empty-text {
  font-size: 18px;
  color: var(--jd-text-primary);
  margin-bottom: 8px;
}

.empty-desc {
  font-size: 14px;
  color: var(--jd-text-secondary);
  margin-bottom: 30px;
}

.go-shopping-btn {
  padding: 12px 30px;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .cart-header,
  .cart-item {
    grid-template-columns: 40px 1fr 80px 100px 80px 60px;
    gap: 10px;
    padding: 15px 10px;
  }
  
  .product-image {
    width: 60px;
    height: 60px;
  }
  
  .product-name {
    font-size: 14px;
  }
  
  .item-price .price,
  .item-subtotal .subtotal-price {
    font-size: 16px;
  }
  
  .cart-footer {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .footer-right {
    justify-content: space-between;
  }
}
</style>
