package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池配置
 *
 * @author wangl
 * @date 2017/12/13
 * @since v1.0.0
 */
@Configuration
//@EnableAsync
public class ThreadPoolTaskExecutorConfig {

    /**
     * 默认核心线程数
     */
    private static final int DEFAULT_CORE_POOL_SIZE = 10;

    /**
     * 默认最大线程数
     */
    private static final int DEFAULT_MAX_POOL_SIZE = 100;

    /**
     * 默认队列容量
     */
    private static final int DEFAULT_QUEUE_CAPACITY = 1000;

    /**
     * 线程池维护线程所允许的空闲时间(线程数>CorePoolSize并且空闲超过这个时间会销毁线程)
     */
    private static final int KEEP_ALIVE_TIME = 100;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 当线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务。
     * 当线程池中的数量等于corePoolSize，但是缓冲队列workQueue未满，那么任务被放入缓冲队列。
     * 当线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量小于maximumPoolSize，建新的线程来处理被添加的任务。
     * 当线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，那么通过 handler所指定的策略来处理此任务。也就是：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程 maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。
     * AbortPolicy:直接抛出异常，默认情况下采用这种策略，CallerRunsPolicy:只用调用者所在线程来运行任务。
     * 当线程池中的线程数量大于corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。
     * 经测试，在不停的增加线程池任务时，首先增加核心线程数，超过10不到默认队列1000的会使用默认核心线程处理，超过默认队列数则创建新的线程直到最大线程数，当仍然有新的任务则通过拒绝策略，
     * 当前策略为使用调用线程池的线程处理任务，即丢回给原线程处理
     */
    @Bean
    protected ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("my-thread-pool-");
        executor.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
        executor.setQueueCapacity(DEFAULT_QUEUE_CAPACITY);
        executor.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean
    protected ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE,
                r -> new Thread(r, "scheduled-thread-pool-exec-" + threadNumber.getAndIncrement()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setKeepAliveTime(KEEP_ALIVE_TIME, TimeUnit.SECONDS);
        return executor;
    }
}
