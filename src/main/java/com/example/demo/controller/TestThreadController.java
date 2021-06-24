package com.example.demo.controller;

import cn.hutool.core.lang.Singleton;
import com.example.demo.entity.*;
import com.example.demo.util.DateUtil;
import com.example.demo.util.HttpClientUtil;
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
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


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
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private static final List<Object> list = Collections.synchronizedList(new ArrayList<>());

    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    private static volatile Integer index = 0;

    @GetMapping("/testThreadLocal")
    public RestResponse testThreadLocal() throws Exception {

//        new Queue<>();
        new LinkedHashMap<>();

        index = 1;
        log.info(index + "");
        threadLocal.set(123);
        threadLocal.set("aaa");
        Thread.sleep(5000);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
    }


    @GetMapping("/testScheduledExecutor")
    public RestResponse testScheduledExecutor(String id) throws Exception {
        // 创建并执行在给定延迟后启用的一次性操作。
//        scheduledThreadPoolExecutor.schedule(() ->{
//            list.add(1);
//            log.info(Thread.currentThread().getName());
//        }, 3, TimeUnit.SECONDS);
        List<String> urlList = Arrays.asList("https://juejin.cn/post/6958079172904222727", "https://juejin.cn/post/6958097237398257671");
        // scheduleWithFixedDelay 方法将会在上一个任务结束后，注意：**再等待 2 秒，**才开始执行，那么他和上一个任务的开始执行时间的间隔是 7 秒。
        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            try {
                for (String url : urlList) {
                    HttpClientUtil.doGet(url, null);
                    log.info("请求掘金页面{}，线程名为{}，共请求{}次", url, Thread.currentThread().getName(), atomicInteger.getAndIncrement());
                    if ((atomicInteger.get() % 10) == 0) {
                        TimeUnit.SECONDS.sleep(30);
                    } else {
                        TimeUnit.SECONDS.sleep(5);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
//        TimeUnit.MILLISECONDS.sleep(7000);
//        scheduledFuture.cancel(false);
        map.put(id, scheduledFuture);
        log.info("开启任务：{}", id);
        // scheduleAtFixedRate 方法将会在上一个任务结束完毕立刻执行，他和上一个任务的开始执行时间的间隔是 5 秒（因为必须等待上一个任务执行完毕）。
//        scheduledThreadPoolExecutor.scheduleAtFixedRate(() ->{
//            list.add(1);
//            log.info(Thread.currentThread().getName());
//        }, 1,3, TimeUnit.SECONDS);

        return RestResponse.success();
    }

    @GetMapping("/testInterruptTask")
    public RestResponse testInterruptTask(String id) throws Exception {
        if (map.get(id) != null && map.get(id) instanceof ScheduledFuture) {
            ScheduledFuture<?> future = (ScheduledFuture<?>) map.get(id);
            // 结束当前线程任务，false为当前正在运行的线程结束后再结束定时任务，true为直接中断当前的线程并结束任务，手动判断中断状态，并编写中断线程的代码
            future.cancel(false);
            log.info("中断任务：{}", id);
        }

        return RestResponse.success().put("listSize", list.size());
    }

    @GetMapping("/testThreadPoolTaskExecutor")
    public RestResponse testThreadPoolTaskExecutor() throws Exception {
        ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
        for (int i = 0; i < 200; i++) {
            threadPoolExecutor.submit(() ->{
                for (int j = 0; j < 10; j++) {
                    atomicInteger.getAndIncrement();
                }
                System.out.println("atomicInteger："+ atomicInteger.get());
                log.info("threadName: {}", Thread.currentThread().getName());
            });
        }
//        threadPoolExecutor.shutdown();
        // 关闭线程池
//        threadPoolExecutor.awaitTermination(3600, TimeUnit.SECONDS);

        return RestResponse.success();
    }

    @GetMapping("/getListSize")
    public RestResponse getListSize() throws Exception {
        int andIncrement = atomicInteger.getAndIncrement();
        System.out.println("atomicInteger"+andIncrement);
        return RestResponse.success().put("listSize", list.size());
    }

    @GetMapping("/testThreadLocalGet")
    public RestResponse testThreadLocalGet() throws Exception {

        TimeUnit.SECONDS.sleep(2);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
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
//        executor.shutdown();
//        Adapter adpater = new Adapter();
//        adpater.charge_byBianKong();
//        adpater.charge_byYuanKong();
//        SaleComputer saleComputer = new Lenovo();
//        saleComputer.show();
//        System.out.println(saleComputer);
//        Properties properties = System.getProperties();
//        System.out.println(properties.toString());
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println(atomicInteger.getAndIncrement());

        System.out.println(101 % 10);
    }
}
