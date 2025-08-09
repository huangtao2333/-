package com.jingdong.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单DTO
 * 用于接收创建订单的请求参数
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class OrderCreateDTO {

    /**
     * 购物车ID列表（从购物车创建订单）
     */
    private List<Long> cartIds;

    /**
     * 直接购买的商品列表（立即购买）
     */
    private List<OrderItemDTO> orderItems;

    /**
     * 订单总金额
     */
    @NotNull(message = "订单总金额不能为空")
    @DecimalMin(value = "0.01", message = "订单总金额必须大于0")
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFee = BigDecimal.ZERO;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 支付方式（1-支付宝，2-微信，3-银行卡）
     */
    @NotNull(message = "支付方式不能为空")
    private Integer paymentMethod;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    /**
     * 收货人手机号
     */
    @NotBlank(message = "收货人手机号不能为空")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 订单商品项DTO
     */
    @Data
    public static class OrderItemDTO {
        /**
         * 商品ID
         */
        @NotNull(message = "商品ID不能为空")
        private Long productId;

        /**
         * 购买数量
         */
        @NotNull(message = "购买数量不能为空")
        private Integer quantity;
    }
}
