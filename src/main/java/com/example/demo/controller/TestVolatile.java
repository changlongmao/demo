package com.example.demo.controller;

import com.example.demo.entity.RestResponse;
import com.example.demo.entity.TaskSettlementPriceReqDto;
import com.example.demo.entity.User;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: TestVolatile
 * @Description:
 * @Author: Chang
 * @Date: 2021/03/04 15:58
 **/
@Slf4j
@RestController
@RequestMapping("/testVolatile")
public class TestVolatile {

    public AtomicInteger inc = new AtomicInteger();
    public void increase() {
        inc.getAndIncrement();
//        int x = inc + 1;
//        inc = x;
//        inc++;
    }

    public void test1() {
        String test1 = "test1第二次提交";
    }

    public void test11() {
        String test1 = "test1第三次提交";
    }

    public void test111() {
        String test1 = "test1第四次提交";
    }

    @PostMapping("/testValid")
    public RestResponse testValid(@Valid @RequestBody TaskSettlementPriceReqDto taskSettlementPriceReqDto) {
        String a = "test1";

        return RestResponse.success();
    }

    public static void main(String[] args) throws InterruptedException {
        final TestVolatile test = new TestVolatile();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    test.increase();
                }
            });
        }
        executor.shutdown();

        log.info("调用awaitTermination之前：" + executor.isTerminated());
        executor.awaitTermination(10, TimeUnit.MINUTES);
        log.info("调用awaitTermination之后：" + executor.isTerminated());
        System.out.println(test.inc);

        System.out.println("systemError: \n org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'entering' in 'class java.lang.Boolean'\n\tat org.apache.ibatis.reflection.Reflector.getGetInvoker(Reflector.java:419)\n\tat org.apache.ibatis.reflection.MetaClass.getGetInvoker(MetaClass.java:164)\n\tat org.apache.ibatis.reflection.wrapper.BeanWrapper.getBeanProperty(BeanWrapper.java:162)\n\tat org.apache.ibatis.reflection.wrapper.BeanWrapper.get(BeanWrapper.java:49)\n\tat org.apache.ibatis.reflection.MetaObject.getValue(MetaObject.java:122)\n\tat org.apache.ibatis.scripting.xmltags.DynamicContext$ContextMap.get(DynamicContext.java:94)\n\tat org.apache.ibatis.scripting.xmltags.DynamicContext$ContextAccessor.getProperty(DynamicContext.java:108)\n\tat org.apache.ibatis.ognl.OgnlRuntime.getProperty(OgnlRuntime.java:2685)\n\tat org.apache.ibatis.ognl.ASTProperty.getValueBody(ASTProperty.java:114)\n\tat org.apache.ibatis.ognl.SimpleNode.evaluateGetValueBody(SimpleNode.java:212)\n\tat org.apache.ibatis.ognl.SimpleNode.getValue(SimpleNode.java:258)\n\tat org.apache.ibatis.ognl.ASTEq.getValueBody(ASTEq.java:50)\n\tat org.apache.ibatis.ognl.SimpleNode.evaluateGetValueBody(SimpleNode.java:212)\n\tat org.apache.ibatis.ognl.SimpleNode.getValue(SimpleNode.java:258)\n\tat org.apache.ibatis.ognl.Ognl.getValue(Ognl.java:470)\n\tat org.apache.ibatis.ognl.Ognl.getValue(Ognl.java:434)\n\tat org.apache.ibatis.scripting.xmltags.OgnlCache.getValue(OgnlCache.java:44)\n\tat org.apache.ibatis.scripting.xmltags.ExpressionEvaluator.evaluateBoolean(ExpressionEvaluator.java:32)\n\tat org.apache.ibatis.scripting.xmltags.IfSqlNode.apply(IfSqlNode.java:34)\n\tat org.apache.ibatis.scripting.xmltags.MixedSqlNode.apply(MixedSqlNode.java:33)\n\tat org.apache.ibatis.scripting.xmltags.DynamicSqlSource.getBoundSql(DynamicSqlSource.java:41)\n\tat org.apache.ibatis.mapping.MappedStatement.getBoundSql(MappedStatement.java:292)\n\tat com.github.pagehelper.PageInterceptor.intercept(PageInterceptor.java:83)\n\tat org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)\n\tat com.sun.proxy.$Proxy308.query(Unknown Source)\n\tat sun.reflect.GeneratedMethodAccessor452.invoke(Unknown Source)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.apache.ibatis.plugin.Invocation.proceed(Invocation.java:49)\n\tat com.joyowo.auth.aspect.AuthDataIntercepter.intercept(AuthDataIntercepter.java:40)\n\tat org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)\n\tat com.sun.proxy.$Proxy308.query(Unknown Source)\n\tat org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:148)\n\tat org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:141)\n\tat sun.reflect.GeneratedMethodAccessor464.invoke(Unknown Source)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:433)\n\t... 90 common frames omitted\nWrapped by: org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'entering' in 'class java.lang.Boolean'\n\tat org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:77)\n\tat org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:446)\n\tat com.sun.proxy.$Proxy146.selectList(Unknown Source)\n\tat org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:230)\n\tat org.apache.ibatis.binding.MapperMethod.executeForMany(MapperMethod.java:139)\n\tat org.apache.ibatis.binding.MapperMethod.execute(MapperMethod.java:76)\n\tat org.apache.ibatis.binding.MapperProxy.invoke(MapperProxy.java:59)\n\tat com.sun.proxy.$Proxy227.selectChannelSource(Unknown Source)\n\tat sun.reflect.GeneratedMethodAccessor936.invoke(Unknown Source)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:343)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:197)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n\tat org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:139)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)\n\tat org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:212)\n\tat com.sun.proxy.$Proxy228.selectChannelSource(Unknown Source)\n\tat com.joyowo.recruit.service.impl.ChannelSourceServiceImpl.getChannelSource(ChannelSourceServiceImpl.java:62)\n\tat com.joyowo.recruit.service.impl.ChannelSourceServiceImpl$$FastClassBySpringCGLIB$$e341676e.invoke(<generated>)\n\tat org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)\n\tat org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:746)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)\n\tat org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294)\n\tat org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)\n\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:185)\n\tat org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)\n\tat com.joyowo.recruit.service.impl.ChannelSourceServiceImpl$$EnhancerBySpringCGLIB$$ed5a748b.getChannelSource(<generated>)\n\tat com.joyowo.recruit.controller.ChannelSourceController.getChannelSource(ChannelSourceController.java:35)\n\tat sun.reflect.GeneratedMethodAccessor935.invoke(Unknown Source)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:209)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:136)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:102)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:877)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:783)\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:991)\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:925)\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:974)\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:877)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:660)\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:851)\n\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:741)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.filterAndRecordMetrics(WebMvcMetricsFilter.java:158)\n\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.filterAndRecordMetrics(WebMvcMetricsFilter.java:126)\n\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:111)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter.doFilterInternal(HttpTraceFilter.java:84)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:109)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:543)\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139)\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:615)\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:818)\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1627)\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n\tat java.lang.Thread.run(Thread.java:748)\n\n");
    }

}
