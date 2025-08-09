package com.jingdong.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情实体类
 * 对应数据库中的order_item表
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
@Entity
@Table(name = "order_item")
@TableName("order_item")
public class OrderItem {

    /**
     * 订单详情ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long orderItemId;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @Column(nullable = false)
    private Long orderId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    @Column(nullable = false)
    private Long productId;

    /**
     * 商品名称（冗余字段，防止商品信息变更）
     */
    @Column(nullable = false, length = 200)
    private String productName;

    /**
     * 商品图片（冗余字段）
     */
    @Column(length = 500)
    private String productImage;

    /**
     * 商品单价（冗余字段，记录下单时的价格）
     */
    @NotNull(message = "商品单价不能为空")
    @DecimalMin(value = "0.01", message = "商品单价必须大于0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer quantity;

    /**
     * 小计金额（单价 * 数量）
     */
    @NotNull(message = "小计金额不能为空")
    @DecimalMin(value = "0.01", message = "小计金额必须大于0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

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
