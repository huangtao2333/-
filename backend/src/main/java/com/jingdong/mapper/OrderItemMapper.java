package com.jingdong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingdong.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单详情Mapper接口
 * 继承MyBatis Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单ID查询订单详情列表
     * 
     * @param orderId 订单ID
     * @return 订单详情列表
     */
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId} AND deleted = 0 ORDER BY create_time ASC")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单ID列表查询订单详情
     * 
     * @param orderIds 订单ID列表
     * @return 订单详情列表
     */
    @Select("<script>" +
            "SELECT * FROM order_item WHERE order_id IN " +
            "<foreach collection='orderIds' item='orderId' open='(' separator=',' close=')'>" +
            "#{orderId}" +
            "</foreach>" +
            " AND deleted = 0 ORDER BY order_id, create_time ASC" +
            "</script>")
    List<OrderItem> findByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 根据商品ID统计销量
     * 
     * @param productId 商品ID
     * @return 销量
     */
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM order_item oi " +
            "LEFT JOIN orders o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = #{productId} AND oi.deleted = 0 AND o.deleted = 0 AND o.status >= 1")
    int countSalesByProductId(@Param("productId") Long productId);

    /**
     * 批量插入订单详情
     * 
     * @param orderItems 订单详情列表
     * @return 影响行数
     */
    int batchInsert(@Param("orderItems") List<OrderItem> orderItems);
}
