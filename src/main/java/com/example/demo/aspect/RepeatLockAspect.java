package com.example.demo.aspect;

import com.example.demo.annotation.RepeatLock;
import com.example.demo.annotation.RedisKeyNameLock;
import com.example.demo.exception.ApiException;
import com.example.demo.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zengcy 2021-11-24
 */
@Slf4j
@Aspect
@Order(0)
@Component
public class RepeatLockAspect {

    @Resource
    private RedissonClient redissonClient;

    // 定义切点
    @Pointcut("@annotation(com.example.demo.annotation.RepeatLock)")
    public void pointCut() {
    }

    /**
     * 此拦截器需结合@RepeatLock和接口RedisKeyNameLock使用
     * @see RepeatLock
     * @see RedisKeyNameLock
     * 当加了注解没有指定任何参数时，默认对当前方法加不等待永久持有的锁，锁的key为接口URI+入参hashCode+userId(为空时拼接ip)
     * 可在入参的dto实现接口RedisKeyNameLock指定redisKeyName, 此时不再拼接入参hashCode
     * 可在注解RepeatLock中指定等待时间，锁占有时长，是否拼接userId，抛出异常的errorCode
     **/
    @Around(value = "pointCut() && @annotation(repeatLock)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatLock repeatLock) throws Throwable {
        // 因要获取HttpServletRequest和ip，故此注解只能加在http请求过程中方法上
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new ApiException("1120104", "此注解只能加在http请求过程中方法上");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String requestURI = request.getRequestURI();
        String ip = IpUtils.getIpAddr(request);

        String userId = "";
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            if (parameter.equals("userId")) {
                userId = request.getParameter(parameter);
                break;
            }
        }
        StringJoiner redisKey = new StringJoiner("_");

        // 遍历方法入参判断是否有实现RedisKeyLock接口的类
        Object[] obj = joinPoint.getArgs();
        Optional<Object> optional = Arrays.stream(obj).filter(Objects::nonNull).filter(o -> o instanceof RedisKeyNameLock).findFirst();
        if (optional.isPresent()) {
            // 若接口入参类实现了RedisKeyNameLock类则获取实现的redisKeyName
            RedisKeyNameLock redisKeyLock = (RedisKeyNameLock) optional.get();
            if (StringUtils.isEmpty(redisKeyLock.getRedisKeyName())) {
                throw new ApiException("1120104", "需指定redisKey");
            }
            redisKey.add(redisKeyLock.getRedisKeyName());
        } else {
            // 不指定默认为接口路径URI拼接入参hashCode
            redisKey.add(requestURI).add(String.valueOf(Arrays.hashCode(obj)));
        }
        // 若需要拼接用户id则拼接
        if (repeatLock.concatUserId()) {
            redisKey.add(StringUtils.isEmpty(userId) ? ip : userId);
        }

        // 尝试获取锁
        RLock lock = redissonClient.getLock(redisKey.toString());
        // 默认锁时长为不等待，永久持有，redisson有看门狗机制，会默认加锁30秒，每10秒检查锁是否仍在使用进行续期30秒，若服务挂掉则30秒后会自动解锁
        boolean tryLock = lock.tryLock(repeatLock.waitTime(), repeatLock.failTimes(), TimeUnit.SECONDS);
        if (!tryLock) {
            log.warn("ip地址: {}，用户id为: {}，访问接口：{}，获取锁失败, 锁的key为：{}", ip, userId, requestURI, redisKey);
            // 可指定抛出异常错误码，不指定默认错误
            throw new ApiException(StringUtils.isEmpty(repeatLock.errorCode()) ? "1120114" : repeatLock.errorCode());
        }

        log.info("ip地址: {}，用户id为: {}，访问接口：{}，获取锁成功，锁的key为：{}", ip, userId, requestURI, redisKey);
        Object proceed;
        try {
            // 执行方法业务逻辑
            proceed = joinPoint.proceed();
        } finally {
            // 若方法内部抛出异常保证锁能正常释放
            lock.unlock();
        }

        return proceed;
    }

}
