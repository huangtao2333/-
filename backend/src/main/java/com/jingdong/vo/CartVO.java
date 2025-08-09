package com.jingdong.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车VO
 * 用于返回给前端的购物车信息
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class CartVO {

    /**
     * 购物车ID
     */
    private Long cartId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String mainImage;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 是否选中
     */
    private Integer selected;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 小计金额（价格 * 数量）
     */
    private BigDecimal totalPrice;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 计算小计金额
     */
    public void calculateTotalPrice() {
        if (price != null && quantity != null) {
            this.totalPrice = price.multiply(new BigDecimal(quantity));
        }
    }

    /**
     * 设置库存状态
     */
    public void setStock(Integer stock) {
        this.stock = stock;
        this.hasStock = stock != null && stock > 0;
    }
}
