package com.wolf.cache.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wolf.cache.model.ProductInfo;
import com.wolf.cache.service.CacheService;
import com.wolf.cache.spring.SpringContext;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka消息处理线程
 */
@Slf4j
public class KafkaMessageProcessor implements Runnable {

    private static final String PRODUCT_INFO_SERVICE = "product_info_service";

    private static final String SHOP_INFO_SERVICE = "shop_info_service";

    private CacheService cacheService;

    private KafkaStream<byte[], byte[]> kafkaStream;

    public KafkaMessageProcessor(KafkaStream<byte[], byte[]> kafkaStream) {
        this.kafkaStream = kafkaStream;
        this.cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while(it.hasNext()) {
            String message = new String(it.next().message());
            // 将message转换为JSON对象
            JSONObject messageJSONObject = JSONObject.parseObject(message);
            log.info("### kafkaMessageProcessor 消费消息，message={} ###", messageJSONObject);

            // 从消息中提取消息对应的服务标识
            String serviceId = messageJSONObject.getString("serviceId");

            // 如果商品信息服务：更改商品消息
            if (serviceId != null && PRODUCT_INFO_SERVICE.equals(serviceId)) {
                processProductInfoChangeMessage(messageJSONObject);
            }
            // 如果店铺信息服务：更改店铺消息
            else if (serviceId != null && SHOP_INFO_SERVICE.equals(serviceId)) {
                processShopInfoChangeMessage(messageJSONObject);
            }
        }
    }

    /**
     * 商品信息变更消息
     * @param messageJSONObject
     */
    private void processProductInfoChangeMessage(JSONObject messageJSONObject) {
        // 提取商品ID
        String productId = messageJSONObject.getString("productId");
        // 调用商品信息服务接口
        log.info("### 调用商品信息服务：根据商品id查询商品信息 ###");
        // 商品信息服务，就会查询数据库，去获取productId=1的商品信息，然后返回回来
        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

        // 更新本地JVM缓存
        cacheService.saveProductInfoToLocalCache(productInfo);
        log.info("### 更新本地缓存完毕 ###");

        // 更新redis缓存
        cacheService.saveProductInfoToRedisCache(productInfo);
        log.info("### 更新Redis缓存完毕 ###");
    }

    /**
     * 商品信息变更消息
     * @param messageJSONObject
     */
    private void processShopInfoChangeMessage(JSONObject messageJSONObject) {
        // 提取店铺ID

        // 调用商品信息服务接口
        log.info("### 调用商品信息服务：根据商品id查询商品信息 ###");

        // 商品信息服务，就会查询数据库，去获取productId=1的商品信息，然后返回回来

        // 更新本地JVM缓存
        log.info("### 更新本地缓存完毕 ###");

        // 更新redis缓存
        log.info("### 更新Redis缓存完毕 ###");
    }
}