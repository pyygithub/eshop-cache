package com.wolf.cache.service;

import com.wolf.cache.model.ProductInfo;
import com.wolf.cache.model.ShopInfo;

/**
 * 缓存service接口
 */
public interface CacheService {
    /**
     * 将商品信息保存到本地缓存
     * @param productInfo
     * @return
     */
    ProductInfo saveProductInfoToLocalCache(ProductInfo productInfo);

    /**
     * 从本地缓存获取商品信息
     * @param productId
     * @return
     */
    ProductInfo getProductInfoFromLocalCache(String productId);

    /**
     * 将店铺信息保存到本地缓存
     * @param shopInfo
     * @return
     */
    ShopInfo saveShopInfoToLocalCache(ShopInfo shopInfo);

    /**
     * 从本地缓存获取店铺信息
     * @param shopId
     * @return
     */
    ShopInfo getShopInfoFromLocalCache(String shopId);



    /**
     * 将商品信息保存到Redis缓存
     * @param productInfo
     * @return
     */
    void saveProductInfoToRedisCache(ProductInfo productInfo);

    /**
     * 从redis缓存获取商品信息
     * @param productId
     * @return
     */
    ProductInfo getProductInfoFromRedisCache(String productId);

    /**
     * 将店铺信息保存到Redis缓存
     * @param shopInfo
     * @return
     */
    void saveShopInfoToRedisCache(ShopInfo shopInfo);

    /**
     * 从redis缓存获取店铺信息
     * @param shopId
     * @return
     */
    ShopInfo getShopInfoFromRedisCache(String shopId);
}
