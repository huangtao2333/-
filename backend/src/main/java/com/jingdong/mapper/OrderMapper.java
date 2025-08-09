package com.jingdong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jingdong.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 订单Mapper接口
 * 继承MyBatis Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询用户订单列表
     * 
     * @param page 分页对象
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 订单分页列表
     */
    @Select("<script>" +
            "SELECT * FROM orders WHERE user_id = #{userId} AND deleted = 0 " +
            "<if test='status != null'> AND status = #{status} </if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    IPage<Order> findOrderPageByUserId(Page<Order> page, 
                                      @Param("userId") Long userId, 
                                      @Param("status") Integer status);

    /**
     * 根据订单号查询订单
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    @Select("SELECT * FROM orders WHERE order_no = #{orderNo} AND deleted = 0")
    Order findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户ID和订单ID查询订单
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 订单信息
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} AND order_id = #{orderId} AND deleted = 0")
    Order findByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param status 订单状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status}, update_time = NOW() WHERE order_id = #{orderId}")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    /**
     * 更新订单支付状态
     * 
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @param paymentTime 支付时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET payment_status = #{paymentStatus}, payment_time = #{paymentTime}, update_time = NOW() WHERE order_id = #{orderId}")
    int updatePaymentStatus(@Param("orderId") Long orderId, 
                           @Param("paymentStatus") Integer paymentStatus, 
                           @Param("paymentTime") String paymentTime);

    /**
     * 更新订单发货时间
     * 
     * @param orderId 订单ID
     * @param shipTime 发货时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET ship_time = #{shipTime}, update_time = NOW() WHERE order_id = #{orderId}")
    int updateShipTime(@Param("orderId") Long orderId, @Param("shipTime") String shipTime);

    /**
     * 更新订单确认收货时间
     * 
     * @param orderId 订单ID
     * @param confirmTime 确认收货时间
     * @return 影响行数
     */
    @Update("UPDATE orders SET confirm_time = #{confirmTime}, update_time = NOW() WHERE order_id = #{orderId}")
    int updateConfirmTime(@Param("orderId") Long orderId, @Param("confirmTime") String confirmTime);

    /**
     * 检查订单号是否存在
     * 
     * @param orderNo 订单号
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM orders WHERE order_no = #{orderNo}")
    int checkOrderNoExists(@Param("orderNo") String orderNo);

    /**
     * 统计用户订单数量（按状态）
     * 
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单数量
     */
    @Select("SELECT COUNT(1) FROM orders WHERE user_id = #{userId} AND status = #{status} AND deleted = 0")
    int countOrdersByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 统计用户总订单数量
     * 
     * @param userId 用户ID
     * @return 订单数量
     */
    @Select("SELECT COUNT(1) FROM orders WHERE user_id = #{userId} AND deleted = 0")
    int countOrdersByUserId(@Param("userId") Long userId);
}
