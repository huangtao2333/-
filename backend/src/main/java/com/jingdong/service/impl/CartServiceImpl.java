package com.jingdong.service.impl;

import com.jingdong.dto.CartAddDTO;
import com.jingdong.dto.CartSelectDTO;
import com.jingdong.dto.CartUpdateDTO;
import com.jingdong.entity.Cart;
import com.jingdong.entity.Product;
import com.jingdong.exception.BusinessException;
import com.jingdong.mapper.CartMapper;
import com.jingdong.mapper.ProductMapper;
import com.jingdong.service.CartService;
import com.jingdong.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 * 实现购物车相关的业务逻辑
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 获取用户购物车列表
     */
    @Override
    public List<CartVO> getCartList(Long userId) {
        log.info("获取用户购物车列表，用户ID：{}", userId);

        List<Cart> cartList = cartMapper.findCartWithProductByUserId(userId);
        
        return cartList.stream().map(this::convertToCartVO).collect(Collectors.toList());
    }

    /**
     * 添加商品到购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addToCart(Long userId, CartAddDTO cartAddDTO) {
        log.info("添加商品到购物车，用户ID：{}，商品ID：{}", userId, cartAddDTO.getProductId());

        // 检查商品是否存在且有库存
        Product product = productMapper.selectById(cartAddDTO.getProductId());
        if (product == null || product.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }
        if (product.getStatus() == 0) {
            throw new BusinessException("商品已下架");
        }
        if (product.getStock() < cartAddDTO.getQuantity()) {
            throw new BusinessException("商品库存不足");
        }

        // 检查购物车中是否已存在该商品
        Cart existingCart = cartMapper.findByUserIdAndProductId(userId, cartAddDTO.getProductId());
        
        if (existingCart != null) {
            // 如果已存在，更新数量
            int newQuantity = existingCart.getQuantity() + cartAddDTO.getQuantity();
            if (newQuantity > product.getStock()) {
                throw new BusinessException("商品库存不足");
            }
            
            existingCart.setQuantity(newQuantity);
            existingCart.setUpdateTime(LocalDateTime.now());
            cartMapper.updateById(existingCart);
        } else {
            // 如果不存在，新增购物车项
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(cartAddDTO.getProductId());
            cart.setQuantity(cartAddDTO.getQuantity());
            cart.setSelected(1); // 默认选中
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            
            cartMapper.insert(cart);
        }

        log.info("商品添加到购物车成功，用户ID：{}，商品ID：{}", userId, cartAddDTO.getProductId());
        return "添加成功";
    }

    /**
     * 更新购物车商品数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateCartItem(Long userId, CartUpdateDTO cartUpdateDTO) {
        log.info("更新购物车商品数量，用户ID：{}，购物车ID：{}", userId, cartUpdateDTO.getCartId());

        // 检查购物车项是否存在且属于当前用户
        Cart cart = cartMapper.selectById(cartUpdateDTO.getCartId());
        if (cart == null || cart.getDeleted() == 1) {
            throw new BusinessException("购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作此购物车项");
        }

        // 检查商品库存
        Product product = productMapper.selectById(cart.getProductId());
        if (product == null || product.getDeleted() == 1 || product.getStatus() == 0) {
            throw new BusinessException("商品不存在或已下架");
        }
        if (product.getStock() < cartUpdateDTO.getQuantity()) {
            throw new BusinessException("商品库存不足");
        }

        // 更新数量
        int result = cartMapper.updateQuantity(cartUpdateDTO.getCartId(), cartUpdateDTO.getQuantity());
        if (result > 0) {
            log.info("购物车商品数量更新成功，购物车ID：{}", cartUpdateDTO.getCartId());
            return "更新成功";
        } else {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 删除购物车商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeCartItem(Long userId, Long cartId) {
        log.info("删除购物车商品，用户ID：{}，购物车ID：{}", userId, cartId);

        // 检查购物车项是否存在且属于当前用户
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || cart.getDeleted() == 1) {
            throw new BusinessException("购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作此购物车项");
        }

        // 逻辑删除
        cart.setDeleted(1);
        cart.setUpdateTime(LocalDateTime.now());
        int result = cartMapper.updateById(cart);
        
        if (result > 0) {
            log.info("购物车商品删除成功，购物车ID：{}", cartId);
            return "删除成功";
        } else {
            throw new BusinessException("删除失败");
        }
    }

    /**
     * 批量删除购物车商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeCartItems(Long userId, List<Long> cartIds) {
        log.info("批量删除购物车商品，用户ID：{}，购物车ID列表：{}", userId, cartIds);

        if (cartIds == null || cartIds.isEmpty()) {
            throw new BusinessException("购物车ID列表不能为空");
        }

        // 检查所有购物车项是否属于当前用户
        for (Long cartId : cartIds) {
            Cart cart = cartMapper.selectById(cartId);
            if (cart == null || cart.getDeleted() == 1) {
                throw new BusinessException("购物车项不存在：" + cartId);
            }
            if (!cart.getUserId().equals(userId)) {
                throw new BusinessException("无权限操作购物车项：" + cartId);
            }
        }

        // 批量删除
        int result = cartMapper.deleteByCartIds(cartIds);
        if (result > 0) {
            log.info("批量删除购物车商品成功，删除数量：{}", result);
            return "删除成功";
        } else {
            throw new BusinessException("删除失败");
        }
    }

    /**
     * 清空购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String clearCart(Long userId) {
        log.info("清空购物车，用户ID：{}", userId);

        int result = cartMapper.deleteByUserId(userId);
        log.info("购物车清空成功，用户ID：{}，删除数量：{}", userId, result);
        return "清空成功";
    }

    /**
     * 更新购物车商品选中状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String selectCartItem(Long userId, CartSelectDTO cartSelectDTO) {
        log.info("更新购物车商品选中状态，用户ID：{}，购物车ID：{}", userId, cartSelectDTO.getCartId());

        // 检查购物车项是否存在且属于当前用户
        Cart cart = cartMapper.selectById(cartSelectDTO.getCartId());
        if (cart == null || cart.getDeleted() == 1) {
            throw new BusinessException("购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权限操作此购物车项");
        }

        // 更新选中状态
        int result = cartMapper.updateSelected(cartSelectDTO.getCartId(), cartSelectDTO.getSelected());
        if (result > 0) {
            log.info("购物车商品选中状态更新成功，购物车ID：{}", cartSelectDTO.getCartId());
            return "更新成功";
        } else {
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 全选/取消全选购物车商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String selectAllCartItems(Long userId, Integer selected) {
        log.info("全选/取消全选购物车商品，用户ID：{}，选中状态：{}", userId, selected);

        int result = cartMapper.updateSelectedByUserId(userId, selected);
        log.info("购物车商品全选状态更新成功，用户ID：{}，更新数量：{}", userId, result);
        return "更新成功";
    }

    /**
     * 获取用户购物车商品总数
     */
    @Override
    public Integer getCartItemCount(Long userId) {
        return cartMapper.countCartItemsByUserId(userId);
    }

    /**
     * 获取用户选中的购物车项
     */
    @Override
    public List<CartVO> getSelectedCartItems(Long userId) {
        log.info("获取用户选中的购物车项，用户ID：{}", userId);

        List<Cart> cartList = cartMapper.findSelectedCartByUserId(userId);
        
        return cartList.stream().map(this::convertToCartVO).collect(Collectors.toList());
    }

    /**
     * 转换为CartVO
     */
    private CartVO convertToCartVO(Cart cart) {
        CartVO cartVO = new CartVO();
        BeanUtils.copyProperties(cart, cartVO);
        
        // 计算小计金额
        cartVO.calculateTotalPrice();
        
        // 设置库存状态
        cartVO.setStock(cart.getStock());
        
        return cartVO;
    }
}
