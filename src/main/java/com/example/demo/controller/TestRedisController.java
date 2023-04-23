package com.example.demo.controller;

import com.example.demo.annotation.RepeatLock;
import com.example.demo.common.RestResponse;
import com.example.demo.entity.*;
import com.example.demo.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName: TestController
 * @Description:
 * @Author: Chang
 * @Date: 2020/12/28 14:44
 **/

@Slf4j
@RestController
@RequestMapping("/testRedis")
public class TestRedisController {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private JedisUtil jedisUtil;


    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ApplicationContext applicationContext;


    @GetMapping("getApplicationContext")
    public RestResponse getApplicationContext(@RequestParam String name) {

        Object bean = applicationContext.getBean(name);
        return RestResponse.success().put(name, bean);
    }

    @RepeatLock
    @GetMapping("testRedissonClient")
    public RestResponse testRedissonClient(@Valid @RequestBody SysLogEntity sysLogEntity,
                                   @RequestParam("userId") Long userId,
                                   @RequestHeader("appCode") String appCode) throws Exception {
//        System.out.println(sysLogEntity.getRedisKeyName());
//        SysLogEntity o = new SysLogEntity();
//        o.setId("aaa");
//        o.setUserName("bbb");
//        int i = 1 / 0;
//        Thread.sleep(10000);
        RLock rLock = redissonClient.getLock("user_auth");
        boolean tryLock = rLock.tryLock(0L, 30L, TimeUnit.SECONDS);
        if (!tryLock) {
            log.info("获取锁失败");
            throw new Exception();
        }
        try {
            log.info("获取锁成功");
            // 执行业务逻辑
            // ...
        } finally {
            // 保证锁的释放
            rLock.unlock();
        }
        return RestResponse.success(sysLogEntity.toString());

//
//        Thread.sleep(5000);

    }

    @GetMapping("testJedisObject")
    public RestResponse testJedisObject() throws Exception {

        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        User user = new User("111");
        map.put("user", user);
        users.add(map);

        jedisUtil.setObject("a", users, 3600);
        log.info("剩余时间" + jedisUtil.ttl("a"));

        jedisUtil.setObjectMap("testMap", new HashMap<String, Object>() {{
            put("aaa", user);
        }}, 3600);
        List<Map<String, Object>> a = (List<Map<String, Object>>) jedisUtil.getObject("a");
        return RestResponse.success().put("a", a.toString());
    }

    @GetMapping("testJedisList")
    public RestResponse testJedisList() throws Exception {

        List<String> strings = new ArrayList<>();
        strings.add("111");
        jedisUtil.setList("strings", strings, 3600);
        System.out.println(jedisUtil.getList("strings"));
        jedisUtil.listAdd("strings", "222", "333");
        System.out.println(jedisUtil.getList("strings"));
        System.out.println(jedisUtil.getKeysByPrefix(""));
        return RestResponse.success();
    }

    @GetMapping("testExpireTime")
    public RestResponse testExpireTime() throws Exception {

//        jedisUtil.set("str", "aaa", 0);
        jedisUtil.set("str", "aaa", 1);
//        jedisUtil.set("str", "aaa", -1);
        Thread.sleep(1000);
        System.out.println(jedisUtil.ttl("str"));
        System.out.println(jedisUtil.get("str"));

        System.out.println(jedisUtil.getKeysByPrefix(""));
        return RestResponse.success();
    }

    public static void main(String[] args) {
//        System.gc();

//        User user = new User(){{
//            testSynchronized();
//        }};
//        user.testSynchronized();
//        SaleComputer saleComputer = new Lenovo();
//        saleComputer.show();
//        SaleComputer saleComputer1 = money -> "111";
//
//        System.out.println(saleComputer);
//        Lenovo lenovo = new Lenovo();
//        lenovo.test();
////        System.out.println(AESUtil.encrypt("FYLAENVYZKDYANVO", null));
//        System.out.println("解密："+AESUtil.decrypt("FYLAENVYZKDYANVO", "AF785C6124F3BE498367881B952E5ABE"));


//        String pw16 = "Lwsjj!365";
//
//        String s = "/aaa/bbb/ccc/";
//        String[] strings = s.split("/");
//        System.out.println(strings.length);
//        System.out.println(Arrays.toString(strings));

//        new Thread(() -> {
//            while (true) {
//                System.out.println("i");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        System.out.println("结束");
//        Socket socket = new Socket();
//        HttpServlet httpServlet = new HttpServlet() {
//        };

        Object[] arr = new Object[]{1,2};
        int[] arr1 = new int[]{1,2,2};
        int[] arr2 = new int[]{1,2,3};

        System.out.println(Arrays.deepHashCode(arr));
        System.out.println(Arrays.hashCode(arr));
        System.out.println(Arrays.hashCode(arr1));
    }

}
