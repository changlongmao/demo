package com.example.demo.annotation;

import com.example.demo.aspect.RepeatLockAspect;
import com.example.demo.entity.RedisKeyNameLock;

import java.lang.annotation.*;

/**
 * 使用此注解会被RepeatLockAspect拦截，默认对当前方法加不等待永久持有的锁，直到此方法执行结束释放锁
 * 锁的范围为同一用户同一接口同一入参不能重复请求
 * @see RedisKeyNameLock
 * @see RepeatLockAspect
 * @author zengcy 2021-11-23
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatLock {

    /**
     * 持有锁时间，指定时间后自动释放锁， 默认 -1秒，永久持有，直到当前锁被释放
     */
    int failTimes() default -1;

    /**
     * 等待时间，默认-1秒，不等待，立即失败
     */
    int waitTime() default -1;

    /**
     * redisKey是否需要拼接userId，若找不到userId则拼接用户ip，默认true拼接，（pc和小程序获取userId，运营后台获取staffId）
     */
    boolean concatUserId() default true;

    /**
     * 若获取锁失败抛出异常的错误码，需在api-error.properties中维护，抛出ApiException
     */
    String errorCode() default "";
}
