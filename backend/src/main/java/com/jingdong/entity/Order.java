package com.jingdong.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 对应数据库中的order表
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
@Entity
@Table(name = "orders")  // order是MySQL关键字，使用orders
@TableName("orders")
public class Order {

    /**
     * 订单ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单号（唯一）
     */
    @NotBlank(message = "订单号不能为空")
    @Column(unique = true, nullable = false, length = 50)
    private String orderNo;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Column(nullable = false)
    private Long userId;

    /**
     * 订单总金额
     */
    @NotNull(message = "订单总金额不能为空")
    @DecimalMin(value = "0.01", message = "订单总金额必须大于0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    @NotNull(message = "实付金额不能为空")
    @DecimalMin(value = "0.01", message = "实付金额必须大于0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payAmount;

    /**
     * 运费
     */
    @Column(precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal shippingFee;

    /**
     * 优惠金额
     */
    @Column(precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal discountAmount;

    /**
     * 订单状态
     * 0-待付款，1-待发货，2-待收货，3-待评价，4-已完成，5-已取消，6-已退款
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer status;

    /**
     * 支付方式（1-支付宝，2-微信，3-银行卡）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer paymentMethod;

    /**
     * 支付状态（0-未支付，1-已支付，2-支付失败）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Column(nullable = false, length = 50)
    private String receiverName;

    /**
     * 收货人手机号
     */
    @NotBlank(message = "收货人手机号不能为空")
    @Column(nullable = false, length = 20)
    private String receiverPhone;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    @Column(nullable = false, length = 500)
    private String receiverAddress;

    /**
     * 订单备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 发货时间
     */
    private LocalDateTime shipTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime confirmTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志（0-未删除，1-已删除）
     */
    @TableLogic
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer deleted;
}
