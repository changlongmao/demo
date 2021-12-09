package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Chang
 * @Description 线程池工具类需要@EnableAsync开启异步
 * @Date 2020/12/22 10:27
 * @Return
 **/
@Configuration
@EnableAsync
public class ExecutorConfig {
    private static int CORE_POOL_SIZE = 10;
    private static int MAX_POOL_SIZE = 200;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        // 线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        // 线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        // 线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(1000);
        // 线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        // 设置默认线程名称
        poolTaskExecutor.setThreadNamePrefix("async-service-");
        // 设置拒绝策略
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 龙猫王帝第五代test1
        return poolTaskExecutor;
    }
}
