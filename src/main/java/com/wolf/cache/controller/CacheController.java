package com.wolf.cache.controller;

import com.wolf.cache.model.ProductInfo;
import com.wolf.cache.service.CacheService;
import com.wolf.cache.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @PostMapping("/testPutLocalCache")
    public Result testPutCache(ProductInfo productInfo) {
        cacheService.saveProductInfoToLocalCache(productInfo);
        return Result.success();
    }

    @GetMapping("/testGetLocalCache")
    public Result testGetCache(String id) {
        ProductInfo productInfo = cacheService.getProductInfoFromLocalCache(id);
        return Result.success(productInfo);
    }

}