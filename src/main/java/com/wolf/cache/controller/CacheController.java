package com.wolf.cache.controller;

import com.alibaba.fastjson.JSONObject;
import com.wolf.cache.model.ProductInfo;
import com.wolf.cache.rebuild.RebuildCacheQueue;
import com.wolf.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/getProductInfo")
    public ProductInfo getProductInfo(String productId) {
        ProductInfo productInfo = null;

        productInfo = cacheService.getProductInfoFromRedisCache(productId);
        log.info("### 从redis查询商品信息 商品信息={} ###", productInfo);

        if (productInfo == null) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            log.info("### 从本地缓存ehcache获取商品信息，商品信息={} ###", productInfo);
        }

        if (productInfo == null) {
            // 就需要从数据库拉取数据，重建缓存
            String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
            productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
            // 将数据推送到内存队列中
            RebuildCacheQueue<ProductInfo> rebuildCacheQueue = RebuildCacheQueue.getInstance();
            rebuildCacheQueue.add(productInfo);
        }

        return productInfo;
    }

}