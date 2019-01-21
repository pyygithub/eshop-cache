package com.wolf.cache.zk;


import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZookeeperSession {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    /**
     * Zookeeper Server 初始化
     */
    private ZookeeperSession(){
        // 去连接zookeeper server, 创建会话的时候，是异步进行的
        // 所有要给一个监听器，说告诉我们什么时候是真正的完成跟zookeeper server 连接
        try {
            this.zooKeeper = new ZooKeeper("192.168.43.10:2181,192.168.43.11:2181,192.168.43.12:2181",
                    5000,
                    new ZookeeperWather());
            log.info("### zookeeper server 连接成功， state={} ###", zooKeeper.getState());

            connectedSemaphore.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ZookeeperWather implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                connectedSemaphore.countDown();
            }
        }
    }

    /**
     * 获取分布式锁
     * @param productId
     */
    public void acquireDistributedLock(String productId) {
        String path = "/product-lock-" + productId;

        try {
            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            log.info("### success to acquire lock form product[id={}]", productId );
        } catch (Exception e) {
            // 如果对应的商品锁的node，已经存在了（就是已经被别人加锁了，那么这里就就会报错：NodeExistsException

            // 如果失败，不断循环尝试（这种方式不推荐，可以参考使用Curator等zookeeper客户端框架来实现）
            int count = 0;
            while(true) {
                try {
                    Thread.sleep(20);

                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    count ++;
                    continue;
                }
                log.info("### success to acquire lock for product[id = {}] after count = {}", productId, count);
                break;
            }
        }
    }

    /**
     * 释放分布式锁
     * @param productId
     */
    public void releaseDistributedLock(String productId) {
        String path = "/product-lock-" + productId;
        try {
            zooKeeper.delete(path, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 私有静态内部类实现单例
     */
    private static class SingleTon {
        private static ZookeeperSession instance = new ZookeeperSession();

        public static ZookeeperSession getInstance() {
            return SingleTon.instance;
        }
    }

    public static ZookeeperSession getInstance() {
        return SingleTon.getInstance();
    }

    public static void init() {
        getInstance();
    }

}