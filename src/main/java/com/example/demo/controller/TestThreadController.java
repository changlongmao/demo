package com.example.demo.controller;

import com.example.demo.common.BaseResDTO;
import com.example.demo.common.RestResponse;
import com.example.demo.entity.*;
import com.example.demo.enums.ProductStatusEnum;
import com.example.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.client.apidocs.annotations.ApiModule;
import org.apache.shenyu.client.springmvc.annotation.ShenyuSpringMvcClient;
import org.assertj.core.util.Lists;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Scope
@ShenyuSpringMvcClient("/")
@RestController
@RequestMapping("/testThread")
public class TestThreadController {

    public static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();


    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private OkHttpTemplate okHttpTemplate;

    private static final List<Object> list = Collections.synchronizedList(new ArrayList<>());

    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    private static volatile Integer index = 0;

    private static final List<String> token = Lists.newArrayList("a", "b");


    @GetMapping("/testOkhttp")
    public RestResponse testOkhttp(HttpServletRequest request) throws Exception {
        HashMap<String, String> headers = new HashMap<String, String>() {{
            put("Connection", "close");
        }};
        okHttpTemplate.doGet("http://127.0.0.1:8999/testThread/testLock1", null, null);
        okHttpTemplate.doGet("http://127.0.0.1:8999/testThread/testThreadLocal", null, null);
        okHttpTemplate.doGet("http://127.0.0.1:8999/testThread/getListSize", null, null);
//        for (int i = 0; i < 5; i++) {
//        }
//        okHttpTemplate.testDoGet("https://baidu.com");

        log.info("请求次数{}", index++);
        log.info("Connection请求头{}", request.getHeader("Connection"));
        return RestResponse.success();
    }

    @GetMapping("/testLock1")
    public RestResponse testLock1() throws Exception {

        String redisKey = String.format("turing:taskNo:%s", DateUtil.getDateYmd(new Date()));
        RAtomicLong atomicLong = redissonClient.getAtomicLong(redisKey);
        long taskNo = atomicLong.incrementAndGet();
        atomicLong.expire(1, TimeUnit.DAYS);
        String taskNoStr = String.format("%04d", taskNo);
        System.out.println(taskNoStr);
        return RestResponse.success();
    }

    public void testLock() throws InterruptedException {
        log.info(Thread.currentThread().getName());
        log.info("请求进来了");
        while (CollectionUtils.isEmpty(token)) {
            log.info("我没拿到锁，我在自旋");
            Thread.sleep(500);
        }
        String integer = null;
        try {
            integer = token.get(0);
            log.info("我拿到了锁：{}", integer);
            token.remove(integer);

            Thread.sleep(10000);
        } finally {
            log.info("我释放了锁：{}", integer);
            token.add(integer);
        }
    }

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
        // 刷阅读量列表
        List<String> urlList = Arrays.asList("https://juejin.cn/post/7092340939506581540",
                "https://juejin.cn/post/7057906316420841479",
                "https://juejin.cn/post/7057922942952276005",
                "https://juejin.cn/post/7214635327406964795",
                "https://juejin.cn/post/7074557788264857631",
                "https://juejin.cn/post/7033796549821857822",
                // 别人的文章，用作混淆
                "https://juejin.cn/post/7081652062169071630");
        Random random = new Random();
        // scheduleWithFixedDelay 方法将会在上一个任务结束后，注意：**再等待 2 秒，**才开始执行，那么他和上一个任务的开始执行时间的间隔是 7 秒。
        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            try {
                for (String url : urlList) {
                    // 生成10以内的随机数，混淆视听
                    int randomInt = random.nextInt(10);
                    okHttpTemplate.doGet(url, null, null);
                    log.info("请求掘金页面{}，线程名为{}，共请求{}次", url, Thread.currentThread().getName(), atomicInteger.getAndIncrement());
                    // 每隔10-20次休息20-30秒
                    if ((atomicInteger.get() % (randomInt + 10)) == 0) {
                        TimeUnit.SECONDS.sleep(randomInt + 20);
                    } else {
                        // 常规休息3-13秒
                        TimeUnit.SECONDS.sleep(randomInt + 3);
                    }
                }
                // 打乱顺序，防止每次相同顺序
                Collections.shuffle(urlList);
            } catch (InterruptedException e) {
                log.error("异常信息为：", e);
            }
            // 每次任务结束，5秒后再次执行
        }, 0, 5, TimeUnit.SECONDS);
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
            threadPoolExecutor.submit(() -> {
                for (int j = 0; j < 10; j++) {
                    atomicInteger.getAndIncrement();
                }
                System.out.println("atomicInteger：" + atomicInteger.get());
                log.info("threadName: {}", Thread.currentThread().getName());
            });
        }
        threadPoolExecutor.shutdown();
        // 关闭线程池
        threadPoolExecutor.awaitTermination(3600, TimeUnit.SECONDS);

        return RestResponse.success();
    }

    @GetMapping("/getListSize")
    public RestResponse getListSize() throws Exception {
        int andIncrement = atomicInteger.getAndIncrement();
        System.out.println("atomicInteger" + andIncrement);
        return RestResponse.success().put("listSize", list.size());
    }

    @GetMapping("/testThreadLocalGet")
    public RestResponse testThreadLocalGet() throws Exception {

        TimeUnit.SECONDS.sleep(2);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
    }

    public static void main(String[] args) throws Exception {
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
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        StringBuffer sb = new StringBuffer();
//        StringBuilder sb = new StringBuilder();
//        String sb = "";
        for (int i = 0; i < 5000000; i++) {
            sb.append("啊啊啊啊啊");
        }
//        List<Future> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Future<?> submit = executor.submit((Callable<Object>) () -> {
//                for (int j = 0; j < 1000000; j++) {
////                    log.info(Thread.currentThread().getName() + "      " + j);
////                    try {
////                        Thread.sleep(1000);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                    log.info(Thread.currentThread().getName() + "      " + j + 1);
//                    sb.append("啊啊啊啊啊");
//                }
//                return Thread.currentThread().getName();
//            });
////            submit.get();
//            list.add(submit);
//        }
//        for (Future future : list) {
//            Object o = future.get();
//            System.out.println(o);
//        }
        executor.shutdown();

        log.info("调用awaitTermination之前：" + executor.isTerminated());
        executor.awaitTermination(5, TimeUnit.MINUTES);
        log.info("调用awaitTermination之后：" + executor.isTerminated());

        long end = System.currentTimeMillis();
        System.out.println("总耗时" + (end - start) + "ms");
        System.out.println(sb.length());
//        LinkedList<String> smsMsg = new LinkedList<>();
//
//        smsMsg.add("bbbb");
//        smsMsg.addFirst("aaa");
//        smsMsg.addLast("ccc");
//        smsMsg.add("ddd");
//        System.out.println(smsMsg.toString());
//
//        Thread thread1 = new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println(222);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread1.start();
//        Thread thread = new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println(111);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            try {
//                thread1.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            for (int i = 0; i < 10; i++) {
//                System.out.println(333);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
        String name = ProductStatusEnum.EDIT.name();
        System.out.println(name);
        BaseResDTO<User> baseResDTO = new BaseResDTO<>();
        baseResDTO.setData(new User());
        System.out.println(baseResDTO);

        System.out.println(String.format("aaa%g", 111.11));

        DateUtil.getDateStr(new Date(), DateUtil.DateFormat.SHORT_DATE_PATTERN_LINE);
        System.out.println(DateUtil.getMonthStart(new Date()));
        System.out.println(DateUtil.getMonthEnd(new Date()));
        System.out.println(DateUtil.getDateYmd(new Date()));
        System.out.println(DateUtil.getDateStr(new Date(), DateUtil.DateFormat.LONG_DATE_PATTERN_LINE));
        System.out.println(DateUtil.getYearAfterOrBefore(new Date(), 10));
        System.out.println(DateUtil.parseDate("2021-8-27 10"));
        String s = String.format("%04d", 12);
        String dateStr = DateUtil.getDateStr(new Date(), DateUtil.DateFormat.CUSTOM_DATE_PATTERN_NONE);

        System.out.println(dateStr);

        System.out.println(new BigDecimal("2.01").scale());
        List<String> strings = Arrays.asList("罗长龙", "祺林", "晨阳");
        Collections.sort(strings);
        System.out.println(strings.toString());
        Float aaa = 1222222222.222222F;
        float aaaa = 211111112111111211111111111111111111111F;
        double aaaaaa = 211111111111111111111111888888888888111111111211111111111111D;
        int aaaaa = 2111111111;
        float bbb = 2.11F;
        System.out.println(Float.MAX_VALUE);
        System.out.println(aaaaaa);
        System.out.println(bbb);
        new BigInteger("11");
        new BigDecimal("11");
        BigInteger n = new BigInteger("999999").pow(99);
        float f = n.floatValue();
        System.out.println(f);

    }
//
//    static {
//        System.out.println("静态代码块初始化");
//    }
//
//    {
//        System.out.println("实例代码块初始化");
//    }
}


