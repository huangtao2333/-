package com.jingdong.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 更新购物车DTO
 * 用于接收更新购物车商品的请求参数
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class CartUpdateDTO {

    /**
     * 购物车ID
     */
    @NotNull(message = "购物车ID不能为空")
    private Long cartId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
}
