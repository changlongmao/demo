package com.example.demo.testMain;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author ChangLF 2023-03-26
 */
public class TestSemaphore {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release(1);
            }
        }).start();

        boolean acquire1 = semaphore.tryAcquire(1, TimeUnit.SECONDS);
        boolean acquire2 = semaphore.tryAcquire(1, TimeUnit.SECONDS);
        System.out.println(acquire1);
        System.out.println(acquire2);




    }
}
