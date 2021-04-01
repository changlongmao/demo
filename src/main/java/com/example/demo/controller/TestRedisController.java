package com.example.demo.controller;

import com.example.demo.entity.Lenovo;
import com.example.demo.entity.RestResponse;
import com.example.demo.entity.SaleComputer;
import com.example.demo.entity.User;
import com.example.demo.util.AESUtil;
import com.example.demo.util.DateUtil;
import com.example.demo.util.HttpClientUtil;
import com.example.demo.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;


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

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

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

    @GetMapping("testRedisTemplate")
    public RestResponse testRedisTemplate() throws Exception {
        // 测试得知redis的自增数字能保证线程安全
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    Long abc = redisTemplate.opsForValue().increment("abc", 1);
                    System.out.println(abc);
                }
            });
        }

        return RestResponse.success();
    }

    public static void main(String[] args) {
        System.gc();

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


        String pw16 = "Lwsjj!365";

        String s = "/aaa/bbb/ccc/";
        String[] strings = s.split("/");
        System.out.println(strings.length);
        System.out.println(Arrays.toString(strings));

        new Thread(() -> {
            while (true) {
                System.out.println("i");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("结束");
    }

}
