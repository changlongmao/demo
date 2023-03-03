package com.example.demo.testMain;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ChangLF 2023-02-19
 */
@Slf4j
public class TestThread {

    public static void main(String[] args) {
        log.info("main");
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() ->{
                System.out.println("执行");
            });
        }
        Collections.synchronizedMap(new HashMap<>());
        new ConcurrentHashMap<>();

        Thread thread = new Thread(() -> {
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                log.warn("我报错了" +e);
//            }
            log.info("new thread");
        });
//        thread.start();

        try {
            thread.interrupt();
        } catch (Exception e) {
            log.warn("主线程报错了");
        }

    }
}
