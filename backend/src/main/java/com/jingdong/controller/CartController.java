package com.jingdong.controller;

import com.jingdong.common.Result;
import com.jingdong.dto.CartAddDTO;
import com.jingdong.dto.CartSelectDTO;
import com.jingdong.dto.CartUpdateDTO;
import com.jingdong.service.CartService;
import com.jingdong.utils.JwtUtils;
import com.jingdong.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 购物车控制器
 * 处理购物车相关的HTTP请求
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Slf4j
@RestController
@RequestMapping("/cart")
@Validated
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取购物车列表
     * 
     * @param request HTTP请求对象
     * @return 购物车列表
     */
    @GetMapping("/list")
    public Result<List<CartVO>> getCartList(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取购物车列表请求，用户ID：{}", userId);
        
        List<CartVO> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    /**
     * 添加商品到购物车
     * 
     * @param cartAddDTO 添加购物车信息
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<String> addToCart(@Valid @RequestBody CartAddDTO cartAddDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("添加商品到购物车请求，用户ID：{}，商品ID：{}", userId, cartAddDTO.getProductId());
        
        String result = cartService.addToCart(userId, cartAddDTO);
        return Result.success(result);
    }

    /**
     * 更新购物车商品数量
     * 
     * @param cartUpdateDTO 更新购物车信息
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<String> updateCartItem(@Valid @RequestBody CartUpdateDTO cartUpdateDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("更新购物车商品数量请求，用户ID：{}，购物车ID：{}", userId, cartUpdateDTO.getCartId());
        
        String result = cartService.updateCartItem(userId, cartUpdateDTO);
        return Result.success(result);
    }

    /**
     * 删除购物车商品
     * 
     * @param cartId 购物车ID
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @DeleteMapping("/{cartId}")
    public Result<String> removeCartItem(@PathVariable @NotNull Long cartId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("删除购物车商品请求，用户ID：{}，购物车ID：{}", userId, cartId);
        
        String result = cartService.removeCartItem(userId, cartId);
        return Result.success(result);
    }

    /**
     * 批量删除购物车商品
     * 
     * @param cartIds 购物车ID列表
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public Result<String> removeCartItems(@RequestBody List<Long> cartIds, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("批量删除购物车商品请求，用户ID：{}，购物车ID列表：{}", userId, cartIds);
        
        String result = cartService.removeCartItems(userId, cartIds);
        return Result.success(result);
    }

    /**
     * 清空购物车
     * 
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @DeleteMapping("/clear")
    public Result<String> clearCart(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("清空购物车请求，用户ID：{}", userId);
        
        String result = cartService.clearCart(userId);
        return Result.success(result);
    }

    /**
     * 更新购物车商品选中状态
     * 
     * @param cartSelectDTO 选中状态信息
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/select")
    public Result<String> selectCartItem(@Valid @RequestBody CartSelectDTO cartSelectDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("更新购物车商品选中状态请求，用户ID：{}，购物车ID：{}", userId, cartSelectDTO.getCartId());
        
        String result = cartService.selectCartItem(userId, cartSelectDTO);
        return Result.success(result);
    }

    /**
     * 全选/取消全选购物车商品
     * 
     * @param selected 选中状态
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/select/all")
    public Result<String> selectAllCartItems(@RequestParam @NotNull Integer selected, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("全选/取消全选购物车商品请求，用户ID：{}，选中状态：{}", userId, selected);
        
        String result = cartService.selectAllCartItems(userId, selected);
        return Result.success(result);
    }

    /**
     * 获取购物车商品总数
     * 
     * @param request HTTP请求对象
     * @return 商品总数
     */
    @GetMapping("/count")
    public Result<Integer> getCartItemCount(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        
        Integer count = cartService.getCartItemCount(userId);
        return Result.success(count);
    }

    /**
     * 获取选中的购物车项
     * 
     * @param request HTTP请求对象
     * @return 选中的购物车项列表
     */
    @GetMapping("/selected")
    public Result<List<CartVO>> getSelectedCartItems(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取选中的购物车项请求，用户ID：{}", userId);
        
        List<CartVO> cartList = cartService.getSelectedCartItems(userId);
        return Result.success(cartList);
    }

    /**
     * 从请求中获取当前用户ID
     * 
     * @param request HTTP请求对象
     * @return 用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        throw new RuntimeException("未登录或登录已过期");
    }
}
