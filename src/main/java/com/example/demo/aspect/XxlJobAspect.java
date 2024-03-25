package com.example.demo.aspect;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * xxl-job拦截器
 *
 * @author 2023/5/23 14:32 ChangLF
 **/
@Slf4j
@Order(1)
@Aspect
@Component
public class XxlJobAspect {

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(xxlJob)")
    public Object doAround(ProceedingJoinPoint point, XxlJob xxlJob) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        String jobName = xxlJob.value();
        StopWatch sw = new StopWatch();
        sw.start();
        log.info("定时任务[{}]开始，开始时间：{}，输入参数：{}", jobName, LocalDateTime.now(), XxlJobHelper.getJobParam());
        Object proceed;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            log.warn("定时任务[{}]执行失败", jobName, e);
            failure(e, traceId);
            return null;
        }
        sw.stop();
        log.info("定时任务[{}]结束！执行时间：{} ms", jobName, sw.getTotalTimeMillis());
        success(traceId);
        return proceed;
    }

    private void failure(Throwable e, String traceId) {
        XxlJobHelper.handleFail("traceId=" + traceId + ",<br>exception=" + ExceptionUtils.getStackTrace(e));
        MDC.remove("traceId");
    }

    private void success(String traceId) {
        XxlJobHelper.handleSuccess("traceId=" + traceId);
        MDC.remove("traceId");
    }

}
