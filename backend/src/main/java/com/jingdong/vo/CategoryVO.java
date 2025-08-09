package com.jingdong.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类VO
 * 用于返回给前端的分类信息
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Data
public class CategoryVO {

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类层级
     */
    private Integer level;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 分类图片
     */
    private String image;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 分类状态
     */
    private Integer status;

    /**
     * 是否显示在导航栏
     */
    private Integer showInNav;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子分类列表
     */
    private List<CategoryVO> children;

    /**
     * 商品数量
     */
    private Integer productCount;
}
