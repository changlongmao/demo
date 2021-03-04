package com.example.demo.controller;

import cn.hutool.core.lang.Singleton;
import com.example.demo.entity.*;
import com.example.demo.util.DateUtil;
import com.sun.org.apache.bcel.internal.generic.ArithmeticInstruction;
import jodd.exception.UncheckedException;
import jodd.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.ArithmeticNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.management.ServiceNotFoundException;
import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.net.UnknownServiceException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName: TestThreadController
 * @Description:
 * @Author: Chang
 * @Date: 2021/01/07 09:20
 **/
@Slf4j
@RestController
@RequestMapping("/testThread")
public class TestThreadController {

    public static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;
    private static Integer index;

    @GetMapping("/testThreadLocal")
    public RestResponse testThreadLocal() throws Exception {

        index = 1;
        log.info(index + "");
        threadLocal.set(123);
        threadLocal.set("aaa");
        Thread.sleep(5000);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
    }

    @GetMapping("/testThreadLocalGet")
    public RestResponse testThreadLocalGet() throws Exception {

        index = 2;
        log.info(index + "");
        threadLocal.set("bbb");
        threadLocal.set(456);
        Thread.sleep(5000);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
    }

    @GetMapping("/testLogLevel")
    public RestResponse testLogLevel() throws Exception {

        System.out.println("开始");

        new Thread(() -> {
            while (true) {
                System.out.println("i");
            }
        });
        System.out.println("结束");

        return RestResponse.success();
    }

    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
//        User user = new User();
//        user.setName("123");
//        user.setCreateTime(DateUtil.parseDate("2021-01-08"));
//
//        User user1 = new User();
//        user1.setName("456");
//        System.out.println(user1.getName());
//        System.out.println(user.getName());
//
//        System.out.println(StringUtils.isEmpty("123"));

//        log.info("{} -- {} -- {}", 123,456,789);
//
//        int hashCode = "123".hashCode();
//        System.out.println(hashCode);
//        System.out.println(hashCode >>> 2);
//        System.out.println(hashCode ^ (hashCode >>> 2));
//        System.out.println(hashCode >>> 16);
//        System.out.println(hashCode ^ (hashCode >>> 16));
//        System.out.println(11 & hashCode);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < 10; i++) {
//            executor.submit(() -> {
//                for (int j = 0; j < 10000000; j++) {
//                    sb.append("啊");
//                }
//            });
//        }
//
//        executor.shutdown();
//
//        log.info("调用awaitTermination之前：" + executor.isTerminated());
//        executor.awaitTermination(5, TimeUnit.MINUTES);
//        log.info("调用awaitTermination之后：" + executor.isTerminated());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1; i++) {
            sb.append("啊");
        }
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < 100000000; i++) {
//            sb.append("啊");
//        }
//        String sb = "";
//        for (int i = 0; i < 100000; i++) {
//            sb += "啊";
//        }

        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
        System.out.println(sb.length());
//        System.out.println(sb);
//        String s = "";
//        System.out.println(11 & 160343085);
//        System.out.println(15 & 160343085);
//        Map<String, Object> map = new HashMap<>();
//        for (int i = 0; i < 10; i++) {
//            map.put(i + "a", i+1);
//            Object put = map.put(i + "a", i);
//            map.get("1a");
//            if (put != null) {
//                log.info("oldValue: {}, newValue: {}", put, i);
//            }
//        }
//        System.out.println(Singleton.getInstance());
        for (int i = 0; i < 100; i++) {
//            executor.submit(() -> System.out.println(Singleton.getInstance()));
        }
        executor.shutdown();
        Adapter adpater = new Adapter();
        adpater.charge_byBianKong();
        adpater.charge_byYuanKong();
        SaleComputer saleComputer = new Lenovo();
        saleComputer.show();
        System.out.println(saleComputer);
    }
}
