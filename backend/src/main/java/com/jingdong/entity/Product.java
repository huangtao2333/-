package com.jingdong.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 对应数据库中的product表
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
@Entity
@Table(name = "product")
@TableName("product")
public class Product {

    /**
     * 商品ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long productId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Column(nullable = false, length = 200)
    private String productName;

    /**
     * 商品副标题
     */
    @Column(length = 300)
    private String subtitle;

    /**
     * 商品分类ID
     */
    @NotNull(message = "商品分类不能为空")
    @Column(nullable = false)
    private Long categoryId;

    /**
     * 商品品牌
     */
    @Column(length = 100)
    private String brand;

    /**
     * 商品型号
     */
    @Column(length = 100)
    private String model;

    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 商品原价（用于显示折扣）
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /**
     * 商品库存
     */
    @NotNull(message = "商品库存不能为空")
    @Min(value = 0, message = "商品库存不能小于0")
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer stock;

    /**
     * 商品主图
     */
    @Column(length = 500)
    private String mainImage;

    /**
     * 商品详情图片（多张图片用逗号分隔）
     */
    @Column(length = 2000)
    private String detailImages;

    /**
     * 商品详情描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 商品规格参数（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    private String specifications;

    /**
     * 商品重量（克）
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer weight;

    /**
     * 商品状态（0-下架，1-上架）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status;

    /**
     * 是否推荐（0-不推荐，1-推荐）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer isRecommend;

    /**
     * 是否热销（0-不是，1-是）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer isHot;

    /**
     * 是否新品（0-不是，1-是）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer isNew;

    /**
     * 销量
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer salesCount;

    /**
     * 浏览量
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer viewCount;

    /**
     * 排序号（数字越小越靠前）
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;

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
