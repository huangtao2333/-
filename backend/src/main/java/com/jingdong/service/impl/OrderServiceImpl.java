package com.jingdong.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jingdong.dto.OrderCreateDTO;
import com.jingdong.dto.OrderQueryDTO;
import com.jingdong.entity.*;
import com.jingdong.exception.BusinessException;
import com.jingdong.mapper.*;
import com.jingdong.service.OrderService;
import com.jingdong.vo.OrderItemVO;
import com.jingdong.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 * 实现订单相关的业务逻辑
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 创建订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(Long userId, OrderCreateDTO orderCreateDTO) {
        log.info("创建订单，用户ID：{}", userId);

        // 获取订单商品信息
        List<Cart> cartItems = new ArrayList<>();
        List<OrderCreateDTO.OrderItemDTO> orderItemDTOs = new ArrayList<>();

        if (orderCreateDTO.getCartIds() != null && !orderCreateDTO.getCartIds().isEmpty()) {
            // 从购物车创建订单
            cartItems = cartMapper.findSelectedCartByUserId(userId);
            if (cartItems.isEmpty()) {
                throw new BusinessException("请选择要购买的商品");
            }
        } else if (orderCreateDTO.getOrderItems() != null && !orderCreateDTO.getOrderItems().isEmpty()) {
            // 立即购买
            orderItemDTOs = orderCreateDTO.getOrderItems();
        } else {
            throw new BusinessException("订单商品信息不能为空");
        }

        // 验证商品库存和计算总金额
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        if (!cartItems.isEmpty()) {
            // 从购物车商品创建订单项
            for (Cart cartItem : cartItems) {
                Product product = productMapper.selectById(cartItem.getProductId());
                if (product == null || product.getDeleted() == 1 || product.getStatus() == 0) {
                    throw new BusinessException("商品不存在或已下架：" + cartItem.getProductName());
                }
                if (product.getStock() < cartItem.getQuantity()) {
                    throw new BusinessException("商品库存不足：" + cartItem.getProductName());
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(product.getProductName());
                orderItem.setProductImage(product.getMainImage());
                orderItem.setProductPrice(product.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                orderItems.add(orderItem);

                calculatedTotal = calculatedTotal.add(orderItem.getTotalPrice());
            }
        } else {
            // 从立即购买商品创建订单项
            for (OrderCreateDTO.OrderItemDTO itemDTO : orderItemDTOs) {
                Product product = productMapper.selectById(itemDTO.getProductId());
                if (product == null || product.getDeleted() == 1 || product.getStatus() == 0) {
                    throw new BusinessException("商品不存在或已下架");
                }
                if (product.getStock() < itemDTO.getQuantity()) {
                    throw new BusinessException("商品库存不足：" + product.getProductName());
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(itemDTO.getProductId());
                orderItem.setProductName(product.getProductName());
                orderItem.setProductImage(product.getMainImage());
                orderItem.setProductPrice(product.getPrice());
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
                orderItems.add(orderItem);

                calculatedTotal = calculatedTotal.add(orderItem.getTotalPrice());
            }
        }

        // 验证订单金额
        BigDecimal expectedTotal = calculatedTotal.add(orderCreateDTO.getShippingFee()).subtract(orderCreateDTO.getDiscountAmount());
        if (expectedTotal.compareTo(orderCreateDTO.getTotalAmount()) != 0) {
            throw new BusinessException("订单金额计算错误");
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(orderCreateDTO.getTotalAmount());
        order.setPayAmount(orderCreateDTO.getTotalAmount());
        order.setShippingFee(orderCreateDTO.getShippingFee());
        order.setDiscountAmount(orderCreateDTO.getDiscountAmount());
        order.setStatus(0); // 待付款
        order.setPaymentMethod(orderCreateDTO.getPaymentMethod());
        order.setPaymentStatus(0); // 未支付
        order.setReceiverName(orderCreateDTO.getReceiverName());
        order.setReceiverPhone(orderCreateDTO.getReceiverPhone());
        order.setReceiverAddress(orderCreateDTO.getReceiverAddress());
        order.setRemark(orderCreateDTO.getRemark());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        int result = orderMapper.insert(order);
        if (result <= 0) {
            throw new BusinessException("订单创建失败");
        }

        // 创建订单详情
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getOrderId());
            orderItem.setCreateTime(LocalDateTime.now());
            orderItem.setUpdateTime(LocalDateTime.now());
        }
        
        // 批量插入订单详情
        orderItemMapper.batchInsert(orderItems);

        // 减少商品库存
        for (OrderItem orderItem : orderItems) {
            int stockResult = productMapper.decreaseStock(orderItem.getProductId(), orderItem.getQuantity());
            if (stockResult <= 0) {
                throw new BusinessException("库存扣减失败：" + orderItem.getProductName());
            }
        }

        // 如果是从购物车创建订单，删除购物车中的商品
        if (!cartItems.isEmpty()) {
            List<Long> cartIds = cartItems.stream().map(Cart::getCartId).collect(Collectors.toList());
            cartMapper.deleteByCartIds(cartIds);
        }

        log.info("订单创建成功，订单ID：{}，订单号：{}", order.getOrderId(), order.getOrderNo());

        // 返回订单信息
        return getOrderDetail(userId, order.getOrderId());
    }

    /**
     * 分页查询用户订单列表
     */
    @Override
    public IPage<OrderVO> getOrderList(Long userId, OrderQueryDTO orderQueryDTO) {
        log.info("查询用户订单列表，用户ID：{}", userId);

        Page<Order> page = new Page<>(orderQueryDTO.getPageNum(), orderQueryDTO.getPageSize());
        IPage<Order> orderPage = orderMapper.findOrderPageByUserId(page, userId, orderQueryDTO.getStatus());

        // 转换为VO
        IPage<OrderVO> orderVOPage = orderPage.convert(this::convertToOrderVO);

        // 批量查询订单详情
        if (!orderVOPage.getRecords().isEmpty()) {
            List<Long> orderIds = orderVOPage.getRecords().stream()
                    .map(OrderVO::getOrderId)
                    .collect(Collectors.toList());
            
            List<OrderItem> orderItems = orderItemMapper.findByOrderIds(orderIds);
            Map<Long, List<OrderItem>> orderItemMap = orderItems.stream()
                    .collect(Collectors.groupingBy(OrderItem::getOrderId));

            // 设置订单详情
            for (OrderVO orderVO : orderVOPage.getRecords()) {
                List<OrderItem> items = orderItemMap.get(orderVO.getOrderId());
                if (items != null) {
                    List<OrderItemVO> itemVOs = items.stream()
                            .map(this::convertToOrderItemVO)
                            .collect(Collectors.toList());
                    orderVO.setOrderItems(itemVOs);
                }
            }
        }

        return orderVOPage;
    }

    /**
     * 获取订单详情
     */
    @Override
    public OrderVO getOrderDetail(Long userId, Long orderId) {
        log.info("获取订单详情，用户ID：{}，订单ID：{}", userId, orderId);

        Order order = orderMapper.findByUserIdAndOrderId(userId, orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        OrderVO orderVO = convertToOrderVO(order);

        // 查询订单详情
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
        List<OrderItemVO> itemVOs = orderItems.stream()
                .map(this::convertToOrderItemVO)
                .collect(Collectors.toList());
        orderVO.setOrderItems(itemVOs);

        return orderVO;
    }

    /**
     * 取消订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelOrder(Long userId, Long orderId) {
        log.info("取消订单，用户ID：{}，订单ID：{}", userId, orderId);

        Order order = orderMapper.findByUserIdAndOrderId(userId, orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() != 0) {
            throw new BusinessException("只能取消待付款订单");
        }

        // 更新订单状态
        int result = orderMapper.updateOrderStatus(orderId, 5); // 已取消
        if (result <= 0) {
            throw new BusinessException("取消订单失败");
        }

        // 恢复商品库存
        List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
        for (OrderItem orderItem : orderItems) {
            productMapper.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
        }

        log.info("订单取消成功，订单ID：{}", orderId);
        return "订单取消成功";
    }

    /**
     * 确认收货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirmOrder(Long userId, Long orderId) {
        log.info("确认收货，用户ID：{}，订单ID：{}", userId, orderId);

        Order order = orderMapper.findByUserIdAndOrderId(userId, orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() != 2) {
            throw new BusinessException("只能确认待收货订单");
        }

        // 更新订单状态和确认收货时间
        String confirmTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int result = orderMapper.updateConfirmTime(orderId, confirmTime);
        if (result > 0) {
            orderMapper.updateOrderStatus(orderId, 3); // 待评价
        } else {
            throw new BusinessException("确认收货失败");
        }

        log.info("确认收货成功，订单ID：{}", orderId);
        return "确认收货成功";
    }

    /**
     * 删除订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteOrder(Long userId, Long orderId) {
        log.info("删除订单，用户ID：{}，订单ID：{}", userId, orderId);

        Order order = orderMapper.findByUserIdAndOrderId(userId, orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() < 4 && order.getStatus() != 5) {
            throw new BusinessException("只能删除已完成或已取消的订单");
        }

        // 逻辑删除订单
        order.setDeleted(1);
        order.setUpdateTime(LocalDateTime.now());
        int result = orderMapper.updateById(order);
        
        if (result > 0) {
            log.info("订单删除成功，订单ID：{}", orderId);
            return "订单删除成功";
        } else {
            throw new BusinessException("订单删除失败");
        }
    }

    /**
     * 支付订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String payOrder(Long userId, Long orderId) {
        log.info("支付订单，用户ID：{}，订单ID：{}", userId, orderId);

        Order order = orderMapper.findByUserIdAndOrderId(userId, orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() != 0) {
            throw new BusinessException("只能支付待付款订单");
        }

        // 更新支付状态和支付时间
        String paymentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int result = orderMapper.updatePaymentStatus(orderId, 1, paymentTime);
        if (result > 0) {
            orderMapper.updateOrderStatus(orderId, 1); // 待发货
        } else {
            throw new BusinessException("支付失败");
        }

        log.info("订单支付成功，订单ID：{}", orderId);
        return "支付成功";
    }

    /**
     * 发货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String shipOrder(Long orderId) {
        log.info("订单发货，订单ID：{}", orderId);

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        if (order.getStatus() != 1) {
            throw new BusinessException("只能发货待发货订单");
        }

        // 更新发货时间和订单状态
        String shipTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int result = orderMapper.updateShipTime(orderId, shipTime);
        if (result > 0) {
            orderMapper.updateOrderStatus(orderId, 2); // 待收货
        } else {
            throw new BusinessException("发货失败");
        }

        log.info("订单发货成功，订单ID：{}", orderId);
        return "发货成功";
    }

    /**
     * 生成订单号
     */
    @Override
    public String generateOrderNo() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int) (Math.random() * 1000));
        String orderNo = "JD" + timestamp + String.format("%03d", Integer.parseInt(random));
        
        // 检查订单号是否已存在
        if (orderMapper.checkOrderNoExists(orderNo) > 0) {
            return generateOrderNo(); // 递归生成新的订单号
        }
        
        return orderNo;
    }

    /**
     * 转换为OrderVO
     */
    private OrderVO convertToOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        
        // 设置状态描述
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentMethod(order.getPaymentMethod());
        orderVO.setPaymentStatus(order.getPaymentStatus());
        
        return orderVO;
    }

    /**
     * 转换为OrderItemVO
     */
    private OrderItemVO convertToOrderItemVO(OrderItem orderItem) {
        OrderItemVO itemVO = new OrderItemVO();
        BeanUtils.copyProperties(orderItem, itemVO);
        return itemVO;
    }
}
