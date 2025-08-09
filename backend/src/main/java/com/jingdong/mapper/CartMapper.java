package com.jingdong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingdong.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 购物车Mapper接口
 * 继承MyBatis Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 根据用户ID查询购物车列表（包含商品信息）
     * 
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("SELECT c.*, p.product_name, p.price, p.main_image, p.stock " +
            "FROM cart c " +
            "LEFT JOIN product p ON c.product_id = p.product_id " +
            "WHERE c.user_id = #{userId} AND c.deleted = 0 AND p.deleted = 0 AND p.status = 1 " +
            "ORDER BY c.create_time DESC")
    List<Cart> findCartWithProductByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和商品ID查询购物车项
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 购物车项
     */
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND product_id = #{productId} AND deleted = 0")
    Cart findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * 更新购物车商品数量
     * 
     * @param cartId 购物车ID
     * @param quantity 商品数量
     * @return 影响行数
     */
    @Update("UPDATE cart SET quantity = #{quantity}, update_time = NOW() WHERE cart_id = #{cartId}")
    int updateQuantity(@Param("cartId") Long cartId, @Param("quantity") Integer quantity);

    /**
     * 更新购物车商品选中状态
     * 
     * @param cartId 购物车ID
     * @param selected 选中状态
     * @return 影响行数
     */
    @Update("UPDATE cart SET selected = #{selected}, update_time = NOW() WHERE cart_id = #{cartId}")
    int updateSelected(@Param("cartId") Long cartId, @Param("selected") Integer selected);

    /**
     * 批量更新购物车商品选中状态
     * 
     * @param userId 用户ID
     * @param selected 选中状态
     * @return 影响行数
     */
    @Update("UPDATE cart SET selected = #{selected}, update_time = NOW() WHERE user_id = #{userId} AND deleted = 0")
    int updateSelectedByUserId(@Param("userId") Long userId, @Param("selected") Integer selected);

    /**
     * 根据用户ID删除购物车（清空购物车）
     * 
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE cart SET deleted = 1, update_time = NOW() WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据购物车ID列表删除购物车项
     * 
     * @param cartIds 购物车ID列表
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE cart SET deleted = 1, update_time = NOW() WHERE cart_id IN " +
            "<foreach collection='cartIds' item='cartId' open='(' separator=',' close=')'>" +
            "#{cartId}" +
            "</foreach>" +
            "</script>")
    int deleteByCartIds(@Param("cartIds") List<Long> cartIds);

    /**
     * 统计用户购物车商品总数
     * 
     * @param userId 用户ID
     * @return 商品总数
     */
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = #{userId} AND deleted = 0")
    Integer countCartItemsByUserId(@Param("userId") Long userId);

    /**
     * 获取用户选中的购物车项
     * 
     * @param userId 用户ID
     * @return 选中的购物车项列表
     */
    @Select("SELECT c.*, p.product_name, p.price, p.main_image, p.stock " +
            "FROM cart c " +
            "LEFT JOIN product p ON c.product_id = p.product_id " +
            "WHERE c.user_id = #{userId} AND c.selected = 1 AND c.deleted = 0 AND p.deleted = 0 AND p.status = 1 " +
            "ORDER BY c.create_time DESC")
    List<Cart> findSelectedCartByUserId(@Param("userId") Long userId);
}
