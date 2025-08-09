package com.jingdong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jingdong.dto.ProductQueryDTO;
import com.jingdong.vo.ProductVO;

import java.util.List;

/**
 * 商品服务接口
 * 定义商品相关的业务操作
 * 
 * @author huangtao
 * @date 2025-08-09
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     * 
     * @param productQueryDTO 查询条件
     * @return 商品分页列表
     */
    IPage<ProductVO> getProductList(ProductQueryDTO productQueryDTO);

    /**
     * 获取商品详情
     * 
     * @param productId 商品ID
     * @return 商品详情
     */
    ProductVO getProductDetail(Long productId);

    /**
     * 搜索商品
     * 
     * @param productQueryDTO 搜索条件
     * @return 商品分页列表
     */
    IPage<ProductVO> searchProducts(ProductQueryDTO productQueryDTO);

    /**
     * 获取推荐商品
     * 
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<ProductVO> getRecommendProducts(Integer limit);

    /**
     * 获取热销商品
     * 
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<ProductVO> getHotProducts(Integer limit);

    /**
     * 获取新品商品
     * 
     * @param limit 限制数量
     * @return 新品商品列表
     */
    List<ProductVO> getNewProducts(Integer limit);

    /**
     * 根据分类ID获取商品列表
     * 
     * @param categoryId 分类ID
     * @param limit 限制数量
     * @return 商品列表
     */
    List<ProductVO> getProductsByCategory(Long categoryId, Integer limit);

    /**
     * 增加商品浏览量
     * 
     * @param productId 商品ID
     */
    void increaseViewCount(Long productId);
}
