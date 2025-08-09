package com.jingdong.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 商品查询DTO
 * 用于接收商品查询请求参数
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class ProductQueryDTO {

    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 10;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 商品状态（0-下架，1-上架）
     */
    private Integer status;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 是否推荐（0-不推荐，1-推荐）
     */
    private Integer isRecommend;

    /**
     * 是否热销（0-不是，1-是）
     */
    private Integer isHot;

    /**
     * 是否新品（0-不是，1-是）
     */
    private Integer isNew;

    /**
     * 排序字段（price-价格，sales-销量，time-时间）
     */
    private String sortField;

    /**
     * 排序方向（asc-升序，desc-降序）
     */
    private String sortOrder = "desc";
}
