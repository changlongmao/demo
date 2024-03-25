package com.example.demo.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ChangLF 2024/01/24
 */
public class SynchronizedLock {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(() -> {
            System.out.println("A我尝试拿到锁");
            testLock();
        });
        executorService.execute(() -> {
            System.out.println("B我尝试拿到锁");
            synchronized (SynchronizedLock.class) {
                System.out.println("B我拿到锁了,先睡3秒");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B我释放锁了");
            }
        });

    }

    public static synchronized void testLock() {
        System.out.println("A我拿到锁了,先睡3秒");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("A我释放锁了");
    }
}
