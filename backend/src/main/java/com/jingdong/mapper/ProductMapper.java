package com.jingdong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jingdong.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品Mapper接口
 * 继承MyBatis Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询商品列表
     * 
     * @param page 分页对象
     * @param categoryId 分类ID（可选）
     * @param keyword 关键词（可选）
     * @param status 商品状态（可选）
     * @return 商品分页列表
     */
    @Select("<script>" +
            "SELECT * FROM product WHERE deleted = 0 " +
            "<if test='categoryId != null'> AND category_id = #{categoryId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (product_name LIKE CONCAT('%', #{keyword}, '%') OR subtitle LIKE CONCAT('%', #{keyword}, '%')) </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "ORDER BY sort_order ASC, create_time DESC" +
            "</script>")
    IPage<Product> findProductPage(Page<Product> page, 
                                  @Param("categoryId") Long categoryId, 
                                  @Param("keyword") String keyword, 
                                  @Param("status") Integer status);

    /**
     * 查询推荐商品
     * 
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    @Select("SELECT * FROM product WHERE is_recommend = 1 AND status = 1 AND deleted = 0 ORDER BY sort_order ASC, create_time DESC LIMIT #{limit}")
    List<Product> findRecommendProducts(@Param("limit") Integer limit);

    /**
     * 查询热销商品
     * 
     * @param limit 限制数量
     * @return 热销商品列表
     */
    @Select("SELECT * FROM product WHERE is_hot = 1 AND status = 1 AND deleted = 0 ORDER BY sales_count DESC, create_time DESC LIMIT #{limit}")
    List<Product> findHotProducts(@Param("limit") Integer limit);

    /**
     * 查询新品商品
     * 
     * @param limit 限制数量
     * @return 新品商品列表
     */
    @Select("SELECT * FROM product WHERE is_new = 1 AND status = 1 AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<Product> findNewProducts(@Param("limit") Integer limit);

    /**
     * 根据分类ID查询商品列表
     * 
     * @param categoryId 分类ID
     * @param limit 限制数量
     * @return 商品列表
     */
    @Select("SELECT * FROM product WHERE category_id = #{categoryId} AND status = 1 AND deleted = 0 ORDER BY sort_order ASC, create_time DESC LIMIT #{limit}")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);

    /**
     * 搜索商品（按关键词）
     * 
     * @param page 分页对象
     * @param keyword 关键词
     * @return 商品分页列表
     */
    @Select("SELECT * FROM product WHERE (product_name LIKE CONCAT('%', #{keyword}, '%') OR subtitle LIKE CONCAT('%', #{keyword}, '%') OR brand LIKE CONCAT('%', #{keyword}, '%')) AND status = 1 AND deleted = 0 ORDER BY sales_count DESC, create_time DESC")
    IPage<Product> searchProducts(Page<Product> page, @Param("keyword") String keyword);

    /**
     * 增加商品浏览量
     * 
     * @param productId 商品ID
     * @return 影响行数
     */
    @Update("UPDATE product SET view_count = view_count + 1, update_time = NOW() WHERE product_id = #{productId}")
    int increaseViewCount(@Param("productId") Long productId);

    /**
     * 增加商品销量
     * 
     * @param productId 商品ID
     * @param quantity 销售数量
     * @return 影响行数
     */
    @Update("UPDATE product SET sales_count = sales_count + #{quantity}, update_time = NOW() WHERE product_id = #{productId}")
    int increaseSalesCount(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 减少商品库存
     * 
     * @param productId 商品ID
     * @param quantity 减少数量
     * @return 影响行数
     */
    @Update("UPDATE product SET stock = stock - #{quantity}, update_time = NOW() WHERE product_id = #{productId} AND stock >= #{quantity}")
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 增加商品库存
     * 
     * @param productId 商品ID
     * @param quantity 增加数量
     * @return 影响行数
     */
    @Update("UPDATE product SET stock = stock + #{quantity}, update_time = NOW() WHERE product_id = #{productId}")
    int increaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 检查商品库存是否充足
     * 
     * @param productId 商品ID
     * @param quantity 需要数量
     * @return 库存是否充足
     */
    @Select("SELECT COUNT(1) FROM product WHERE product_id = #{productId} AND stock >= #{quantity} AND status = 1 AND deleted = 0")
    int checkStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
