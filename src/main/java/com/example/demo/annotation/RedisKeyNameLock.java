package com.example.demo.annotation;

import com.example.demo.aspect.RepeatLockAspect;

/**
 * @author ChangLF 2022-01-22
 * @see RepeatLockAspect
 */
public interface RedisKeyNameLock {

    /**
     * 指定redisKeyName,不指定默认为接口uri+hashCode+userId
     */
    String getRedisKeyName();
}
