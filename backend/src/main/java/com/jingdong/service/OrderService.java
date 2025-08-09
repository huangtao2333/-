package com.jingdong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jingdong.dto.OrderCreateDTO;
import com.jingdong.dto.OrderQueryDTO;
import com.jingdong.vo.OrderVO;

/**
 * 订单服务接口
 * 定义订单相关的业务操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
public interface OrderService {

    /**
     * 创建订单
     * 
     * @param userId 用户ID
     * @param orderCreateDTO 订单创建信息
     * @return 订单信息
     */
    OrderVO createOrder(Long userId, OrderCreateDTO orderCreateDTO);

    /**
     * 分页查询用户订单列表
     * 
     * @param userId 用户ID
     * @param orderQueryDTO 查询条件
     * @return 订单分页列表
     */
    IPage<OrderVO> getOrderList(Long userId, OrderQueryDTO orderQueryDTO);

    /**
     * 获取订单详情
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderVO getOrderDetail(Long userId, Long orderId);

    /**
     * 取消订单
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 操作结果
     */
    String cancelOrder(Long userId, Long orderId);

    /**
     * 确认收货
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 操作结果
     */
    String confirmOrder(Long userId, Long orderId);

    /**
     * 删除订单
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 操作结果
     */
    String deleteOrder(Long userId, Long orderId);

    /**
     * 支付订单
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 操作结果
     */
    String payOrder(Long userId, Long orderId);

    /**
     * 发货
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    String shipOrder(Long orderId);

    /**
     * 生成订单号
     * 
     * @return 订单号
     */
    String generateOrderNo();
}
