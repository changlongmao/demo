package com.example.demo.testMain;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ChangLF 2023-02-19
 */
public class TestReentrantLock {


    public static void main(String[] args) {

        int a = 1;
        ReentrantLock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() ->{
                lock.lock();

                System.out.println("aaa");

                lock.unlock();
            });
        }

        if (true)
            System.out.println("bb");
        executorService.shutdown();

    }
}
