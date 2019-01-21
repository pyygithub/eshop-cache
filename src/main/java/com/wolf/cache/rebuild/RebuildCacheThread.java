package com.wolf.cache.rebuild;

import com.wolf.cache.model.ProductInfo;
import com.wolf.cache.service.CacheService;
import com.wolf.cache.spring.SpringContext;
import com.wolf.cache.zk.ZookeeperSession;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 缓存重建线程
 */
@Slf4j
public class RebuildCacheThread implements Runnable{
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        RebuildCacheQueue<ProductInfo> rebuildCacheQueue = RebuildCacheQueue.getInstance();
        ZookeeperSession zookeeperSession = ZookeeperSession.getInstance();
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");

        while (true) {
            ProductInfo productInfo = rebuildCacheQueue.take();
            // 获取分布式锁
            zookeeperSession.acquireDistributedLock(productInfo.getId());

            // 从redis拉取数据
            ProductInfo existedProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());
            if (existedProductInfo != null){
                // 比较当前数据的版本和已有数据的时间版本是新还是旧
                Date currentTime = dateTimeFormatter.parseDateTime(productInfo.getModifiedTime()).toDate();
                Date existedTime = dateTimeFormatter.parseDateTime(existedProductInfo.getModifiedTime()).toDate();
                // 如果当前数据比较旧
                if (currentTime.before(existedTime)) {
                    log.info("### current date = {} is before existed date = {} ###", currentTime, existedTime);
                    continue;
                }
                log.info("### product current date = {} is after existed date = {} ###", currentTime, existedTime);
            } else {
                log.info("### existed product info is null ###");
            }
            // 当前数据比较新，就更新redis缓存
            cacheService.saveProductInfoToRedisCache(productInfo);
        }
    }
}