package com.example.demo.util;

import com.example.demo.entity.SysLog;
import com.example.demo.entity.SysLogEntity;
import com.example.demo.entity.SysUserEntity;
import com.example.demo.service.SysLogService;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.example.demo.entity.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();

        Object result = point.proceed();

        long time = System.currentTimeMillis() - beginTime;


        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {

            sysLog.setOperation(syslog.value());
        }


        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");


        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args);
            sysLog.setParams(params);


            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

            sysLog.setIp(IpUtils.getIpAddr(request));


            String userName = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUserName();
            sysLog.setUserName(userName);

            sysLog.setTime(time);
            sysLog.setCreateTime(new Date());

            sysLogService.save(sysLog);
        } catch (Exception ignored) {

        }
    }
}
