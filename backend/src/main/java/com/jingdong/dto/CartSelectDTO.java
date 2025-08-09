package com.jingdong.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 购物车选中DTO
 * 用于接收购物车商品选中状态的请求参数
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class CartSelectDTO {

    /**
     * 购物车ID
     */
    @NotNull(message = "购物车ID不能为空")
    private Long cartId;

    /**
     * 是否选中（0-未选中，1-选中）
     */
    @NotNull(message = "选中状态不能为空")
    private Integer selected;
}
