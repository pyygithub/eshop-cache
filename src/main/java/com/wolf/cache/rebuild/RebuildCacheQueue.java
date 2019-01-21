package com.wolf.cache.rebuild;


import java.util.concurrent.ArrayBlockingQueue;

public class RebuildCacheQueue<T> {

    private ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(1000);

    public void add(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public T take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private RebuildCacheQueue(){}

    private static class SingleTon {
        private static RebuildCacheQueue instance = new RebuildCacheQueue();

        public static RebuildCacheQueue getInstance() {
            return SingleTon.instance;
        }
    }

    public static RebuildCacheQueue getInstance() {
        return SingleTon.getInstance();
    }

    public static void init() {
        getInstance();
    }
}