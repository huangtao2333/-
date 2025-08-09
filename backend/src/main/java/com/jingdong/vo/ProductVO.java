package com.jingdong.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品VO
 * 用于返回给前端的商品信息
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class ProductVO {

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品型号
     */
    private String model;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 商品主图
     */
    private String mainImage;

    /**
     * 商品详情图片列表
     */
    private List<String> detailImageList;

    /**
     * 商品详情描述
     */
    private String description;

    /**
     * 商品规格参数
     */
    private String specifications;

    /**
     * 商品重量（克）
     */
    private Integer weight;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 商品状态描述
     */
    private String statusDesc;

    /**
     * 是否推荐
     */
    private Integer isRecommend;

    /**
     * 是否热销
     */
    private Integer isHot;

    /**
     * 是否新品
     */
    private Integer isNew;

    /**
     * 销量
     */
    private Integer salesCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 折扣率（如果有原价）
     */
    private BigDecimal discountRate;

    /**
     * 设置商品状态描述
     */
    public void setStatus(Integer status) {
        this.status = status;
        if (status != null) {
            switch (status) {
                case 1:
                    this.statusDesc = "上架";
                    break;
                case 0:
                    this.statusDesc = "下架";
                    break;
                default:
                    this.statusDesc = "未知";
                    break;
            }
        }
    }

    /**
     * 设置是否有库存
     */
    public void setStock(Integer stock) {
        this.stock = stock;
        this.hasStock = stock != null && stock > 0;
    }

    /**
     * 计算折扣率
     */
    public void calculateDiscountRate() {
        if (originalPrice != null && price != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = originalPrice.subtract(price);
            this.discountRate = discount.divide(originalPrice, 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 设置详情图片列表
     */
    public void setDetailImages(String detailImages) {
        if (detailImages != null && !detailImages.trim().isEmpty()) {
            this.detailImageList = List.of(detailImages.split(","));
        }
    }
}
