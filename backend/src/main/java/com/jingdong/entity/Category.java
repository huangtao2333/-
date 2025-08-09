package com.jingdong.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * 对应数据库中的category表
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
@Entity
@Table(name = "category")
@TableName("category")
public class Category {

    /**
     * 分类ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Column(nullable = false, length = 100)
    private String categoryName;

    /**
     * 父分类ID（0表示顶级分类）
     */
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long parentId;

    /**
     * 分类层级（1-一级分类，2-二级分类，3-三级分类）
     */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Integer level;

    /**
     * 分类图标
     */
    @Column(length = 200)
    private String icon;

    /**
     * 分类图片
     */
    @Column(length = 200)
    private String image;

    /**
     * 分类描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 排序号（数字越小越靠前）
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;

    /**
     * 分类状态（0-禁用，1-启用）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status;

    /**
     * 是否显示在导航栏（0-不显示，1-显示）
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer showInNav;

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
