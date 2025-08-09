package com.jingdong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingdong.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类Mapper接口
 * 继承MyBatis Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据父分类ID查询子分类列表
     * 
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    @Select("SELECT * FROM category WHERE parent_id = #{parentId} AND status = 1 AND deleted = 0 ORDER BY sort_order ASC, create_time ASC")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    /**
     * 根据分类层级查询分类列表
     * 
     * @param level 分类层级
     * @return 分类列表
     */
    @Select("SELECT * FROM category WHERE level = #{level} AND status = 1 AND deleted = 0 ORDER BY sort_order ASC, create_time ASC")
    List<Category> findByLevel(@Param("level") Integer level);

    /**
     * 查询导航栏显示的分类
     * 
     * @return 导航栏分类列表
     */
    @Select("SELECT * FROM category WHERE show_in_nav = 1 AND status = 1 AND deleted = 0 ORDER BY sort_order ASC, create_time ASC")
    List<Category> findNavCategories();

    /**
     * 查询所有启用的顶级分类
     * 
     * @return 顶级分类列表
     */
    @Select("SELECT * FROM category WHERE parent_id = 0 AND status = 1 AND deleted = 0 ORDER BY sort_order ASC, create_time ASC")
    List<Category> findTopCategories();

    /**
     * 检查分类名称是否存在（同级别下）
     * 
     * @param categoryName 分类名称
     * @param parentId 父分类ID
     * @param excludeId 排除的分类ID（用于更新时排除自己）
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM category WHERE category_name = #{categoryName} AND parent_id = #{parentId} AND category_id != #{excludeId} AND deleted = 0")
    int checkCategoryNameExists(@Param("categoryName") String categoryName, 
                               @Param("parentId") Long parentId, 
                               @Param("excludeId") Long excludeId);

    /**
     * 统计分类下的商品数量
     * 
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @Select("SELECT COUNT(1) FROM product WHERE category_id = #{categoryId} AND deleted = 0")
    int countProductsByCategory(@Param("categoryId") Long categoryId);
}
