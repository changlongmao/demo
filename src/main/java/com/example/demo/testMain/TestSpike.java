package com.example.demo.testMain;

import org.apache.commons.lang3.time.StopWatch;
import org.redisson.api.RedissonClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ChangLF 2023-03-20
 */
public class TestSpike {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(200, 200, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        int taskNum = 1000000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CountDownLatch downLatch = new CountDownLatch(taskNum);
        AtomicInteger integer = new AtomicInteger(100);
        for (int i = 0; i < taskNum; i++) {
            executor.execute(() -> {
                downLatch.countDown();
                int start = integer.get();
                try {
                    TimeUnit.NANOSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (start > 0) {
                    int get = integer.getAndDecrement();
                    if (get > 0) {
                        System.out.println("获取成功：" + get);
                    }
                }
            });
        }

        downLatch.await();
        System.out.println(integer.get());
        executor.shutdown();

        stopWatch.stop();
        System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms");

    }
}
