package com.example.demo.controller;

import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TestVolatile
 * @Description:
 * @Author: Chang
 * @Date: 2021/03/04 15:58
 **/
@Slf4j
public class TestVolatile {

    public volatile int inc = 0;
    public void increase() {
        int x = inc + 1;
        inc = x;
//        inc++;
    }

    public static void main(String[] args) throws InterruptedException {
        final TestVolatile test = new TestVolatile();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    test.increase();
                }
            });
        }
        executor.shutdown();

        log.info("调用awaitTermination之前：" + executor.isTerminated());
        executor.awaitTermination(10, TimeUnit.MINUTES);
        log.info("调用awaitTermination之后：" + executor.isTerminated());
        System.out.println(test.inc);
    }

}
