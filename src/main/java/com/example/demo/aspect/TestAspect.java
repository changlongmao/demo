package com.example.demo.aspect;

import com.example.demo.util.HttpContextUtils;
import com.example.demo.util.IpUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TestAspect
 * @Description:
 * @Author: Chang
 * @Date: 2020/12/25 14:32
 **/
@Slf4j
@Order(2)
@Aspect
@Component
public class TestAspect {

    @Pointcut("@annotation(com.example.demo.annotation.SysLog)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("TestAspect前置通知");
        //AOP代理类的信息  
        joinPoint.getThis();
        //代理的目标对象  
        joinPoint.getTarget();
        //用的最多 通知的签名  
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法  
        log.info("AOP代理的方法：" + signature.getName());
        //AOP代理类的名字  
        log.info("AOP代理类的名字：" + signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息  
        Class declaringType = signature.getDeclaringType();
        log.info("AOP代理类的类（class）信息  ：" + declaringType);
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        log.info("AOP代理类的名字：" + method);
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息  
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 获取ip
        log.info("AOP代理Ip：" + IpUtils.getIpAddr(request));
        //如果要获取Session信息的话，可以这样写：  
        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        String params = new Gson().toJson(obj);
        log.info("Gson获取的参数：" + params);
        log.info(request.getRequestURL().toString());
        //获取请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, Object> parameterMap = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter, request.getParameter(parameter));
        }
        log.info("请求的参数信息为：" + parameterMap);
    }

    @AfterReturning(value = "pointCut()",returning = "keys")
    public void doAfterReturn(JoinPoint joinPoint,Object keys) {
        log.info("TestAspect后置返回通知" + keys);
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("TestAspect后置通知");
    }

    @AfterThrowing(value = "pointCut()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        log.error("后置异常通知：", throwable);
//        log.error("TestAspect后置异常通知：" + throwable.getMessage());
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        log.info("TestAspect环绕通知的目标方法名：" + point.getSignature().getName());
        long beginTime = System.currentTimeMillis();

        Object result = null;
        try {
            result = point.proceed();
        } finally {
            log.info("测试环绕通知一定执行");
        }

        long time = System.currentTimeMillis() - beginTime;
        log.info("TestAspect方法执行时间：" + time + "ms");
        log.info("环绕通知结束");
        return result;
    }
}
