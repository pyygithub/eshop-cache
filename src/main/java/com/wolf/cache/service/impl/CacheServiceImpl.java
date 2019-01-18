package com.wolf.cache.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wolf.cache.model.ProductInfo;
import com.wolf.cache.model.ShopInfo;
import com.wolf.cache.service.CacheService;
import com.wolf.cache.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("cacheService")
@Slf4j
public class CacheServiceImpl implements CacheService {

    private static final String CACHE_NAME = "local";

    private static final String PRODUCT_INFO_PREFIX = "product_info_";

    private static final String SHOP_INFO_PREFIX = "shop_info_";

    @Autowired
    private RedisService redisService;

    @Override
    @CachePut(value = CACHE_NAME, key = "#productInfo.id")
    public ProductInfo saveProductInfoToLocalCache(ProductInfo productInfo) {
        log.info("### saveProductInfoToLocalCache 更新local缓存， productInfo={} ###",productInfo);
        return productInfo;
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "#shopInfo.id")
    public ShopInfo saveShopInfoToLocalCache(ShopInfo shopInfo) {
        log.info("### saveShopInfoToLocalCache 更新local缓存， shopInfo={} ###",shopInfo);
        return shopInfo;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#shopId")
    public ShopInfo getShopInfoFromLocalCache(String shopId) {
        return null;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#productId")
    public ProductInfo getProductInfoFromLocalCache(String productId) {
        return null;
    }

    // ----------------------------------------------------------

    @Override
    public void saveProductInfoToRedisCache(ProductInfo productInfo) {
        String key = PRODUCT_INFO_PREFIX + productInfo.getId();
        redisService.set(key, JSONObject.toJSONString(productInfo));
        log.info("### saveProductInfoToRedisCache 更新redis缓存， productInfo={} ###",productInfo);
    }

    @Override
    public void saveShopInfoToRedisCache(ShopInfo shopInfo) {
        String key = SHOP_INFO_PREFIX + shopInfo.getId();
        redisService.set(key, JSONObject.toJSONString(shopInfo));
        log.info("### saveShopInfoToRedisCache 更新redis缓存， shopInfo={} ###",shopInfo);
    }

    @Override
    public ProductInfo getProductInfoFromRedisCache(String productId) {
        String key = PRODUCT_INFO_PREFIX + productId;
        String productInfoString = redisService.get(key);
        log.info("### getProductInfoFromRedisCache 从redis缓存查询商品信息， productInfoString={} ###",productInfoString);

        return JSONObject.parseObject(productInfoString, ProductInfo.class);
    }

    @Override
    public ShopInfo getShopInfoFromRedisCache(String shopId) {
        String key = SHOP_INFO_PREFIX + shopId;
        String shopInfoString = redisService.get(key);
        log.info("### getProductInfoFromRedisCache 从redis缓存查询店铺信息， shopInfoString={} ###",shopInfoString);

        return JSONObject.parseObject(shopInfoString, ShopInfo.class);
    }
}