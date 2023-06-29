package com.example.demo.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Chang
 * @Description 线程池工具类需要@EnableAsync开启异步
 * @Date 2020/12/22 10:27
 **/
@Configuration
@EnableAsync
public class ExecutorConfig {

    private static int CORE_POOL_SIZE = 10;
    private static int MAX_POOL_SIZE = 200;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    @Bean
    public ExecutorService executorService() {
        return TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 30,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), r -> new Thread(r,
                "demo-thread-" + threadNumber.getAndIncrement()),
                new ThreadPoolExecutor.CallerRunsPolicy()));
    }
}
