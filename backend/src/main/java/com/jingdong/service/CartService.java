package com.jingdong.service;

import com.jingdong.dto.CartAddDTO;
import com.jingdong.dto.CartSelectDTO;
import com.jingdong.dto.CartUpdateDTO;
import com.jingdong.vo.CartVO;

import java.util.List;

/**
 * 购物车服务接口
 * 定义购物车相关的业务操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
public interface CartService {

    /**
     * 获取用户购物车列表
     * 
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<CartVO> getCartList(Long userId);

    /**
     * 添加商品到购物车
     * 
     * @param userId 用户ID
     * @param cartAddDTO 添加购物车信息
     * @return 操作结果
     */
    String addToCart(Long userId, CartAddDTO cartAddDTO);

    /**
     * 更新购物车商品数量
     * 
     * @param userId 用户ID
     * @param cartUpdateDTO 更新购物车信息
     * @return 操作结果
     */
    String updateCartItem(Long userId, CartUpdateDTO cartUpdateDTO);

    /**
     * 删除购物车商品
     * 
     * @param userId 用户ID
     * @param cartId 购物车ID
     * @return 操作结果
     */
    String removeCartItem(Long userId, Long cartId);

    /**
     * 批量删除购物车商品
     * 
     * @param userId 用户ID
     * @param cartIds 购物车ID列表
     * @return 操作结果
     */
    String removeCartItems(Long userId, List<Long> cartIds);

    /**
     * 清空购物车
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    String clearCart(Long userId);

    /**
     * 更新购物车商品选中状态
     * 
     * @param userId 用户ID
     * @param cartSelectDTO 选中状态信息
     * @return 操作结果
     */
    String selectCartItem(Long userId, CartSelectDTO cartSelectDTO);

    /**
     * 全选/取消全选购物车商品
     * 
     * @param userId 用户ID
     * @param selected 选中状态
     * @return 操作结果
     */
    String selectAllCartItems(Long userId, Integer selected);

    /**
     * 获取用户购物车商品总数
     * 
     * @param userId 用户ID
     * @return 商品总数
     */
    Integer getCartItemCount(Long userId);

    /**
     * 获取用户选中的购物车项
     * 
     * @param userId 用户ID
     * @return 选中的购物车项列表
     */
    List<CartVO> getSelectedCartItems(Long userId);
}
