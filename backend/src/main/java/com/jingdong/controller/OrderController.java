package com.jingdong.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jingdong.common.Result;
import com.jingdong.dto.OrderCreateDTO;
import com.jingdong.dto.OrderQueryDTO;
import com.jingdong.service.OrderService;
import com.jingdong.utils.JwtUtils;
import com.jingdong.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 订单控制器
 * 处理订单相关的HTTP请求
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 创建订单
     * 
     * @param orderCreateDTO 订单创建信息
     * @param request HTTP请求对象
     * @return 订单信息
     */
    @PostMapping("/create")
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("创建订单请求，用户ID：{}", userId);
        
        OrderVO orderVO = orderService.createOrder(userId, orderCreateDTO);
        return Result.success("订单创建成功", orderVO);
    }

    /**
     * 分页查询订单列表
     * 
     * @param orderQueryDTO 查询条件
     * @param request HTTP请求对象
     * @return 订单分页列表
     */
    @GetMapping("/list")
    public Result<IPage<OrderVO>> getOrderList(@Valid OrderQueryDTO orderQueryDTO, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("查询订单列表请求，用户ID：{}", userId);
        
        IPage<OrderVO> orderPage = orderService.getOrderList(userId, orderQueryDTO);
        return Result.success(orderPage);
    }

    /**
     * 获取订单详情
     * 
     * @param orderId 订单ID
     * @param request HTTP请求对象
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderVO> getOrderDetail(@PathVariable @NotNull Long orderId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("获取订单详情请求，用户ID：{}，订单ID：{}", userId, orderId);
        
        OrderVO orderVO = orderService.getOrderDetail(userId, orderId);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/{orderId}/cancel")
    public Result<String> cancelOrder(@PathVariable @NotNull Long orderId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("取消订单请求，用户ID：{}，订单ID：{}", userId, orderId);
        
        String result = orderService.cancelOrder(userId, orderId);
        return Result.success(result);
    }

    /**
     * 确认收货
     * 
     * @param orderId 订单ID
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/{orderId}/confirm")
    public Result<String> confirmOrder(@PathVariable @NotNull Long orderId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("确认收货请求，用户ID：{}，订单ID：{}", userId, orderId);
        
        String result = orderService.confirmOrder(userId, orderId);
        return Result.success(result);
    }

    /**
     * 删除订单
     * 
     * @param orderId 订单ID
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @DeleteMapping("/{orderId}")
    public Result<String> deleteOrder(@PathVariable @NotNull Long orderId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("删除订单请求，用户ID：{}，订单ID：{}", userId, orderId);
        
        String result = orderService.deleteOrder(userId, orderId);
        return Result.success(result);
    }

    /**
     * 支付订单
     * 
     * @param orderId 订单ID
     * @param request HTTP请求对象
     * @return 操作结果
     */
    @PutMapping("/{orderId}/pay")
    public Result<String> payOrder(@PathVariable @NotNull Long orderId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        log.info("支付订单请求，用户ID：{}，订单ID：{}", userId, orderId);
        
        String result = orderService.payOrder(userId, orderId);
        return Result.success(result);
    }

    /**
     * 发货（管理员功能）
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PutMapping("/{orderId}/ship")
    public Result<String> shipOrder(@PathVariable @NotNull Long orderId) {
        log.info("订单发货请求，订单ID：{}", orderId);
        
        String result = orderService.shipOrder(orderId);
        return Result.success(result);
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
