package com.example.demo.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author ChangLF 2024/01/17
 */
@Slf4j
@Component
public class LogAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        log.info("LogAdvice拦截方法准备执行方法: " + method.getName() + ", 参数列表：" + Arrays.toString(args));
    }

}
