package com.example.demo.controller;

import com.example.demo.entity.RestResponse;
import com.example.demo.entity.User;
import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


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

    private static Integer index;

    @GetMapping("testThreadLocal")
    public RestResponse testThreadLocal() throws Exception {

        index = 1;
        log.info(index + "");
        threadLocal.set(123);
        threadLocal.set("aaa");
        Thread.sleep(5000);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
    }

    @GetMapping("testThreadLocalGet")
    public RestResponse testThreadLocalGet() throws Exception {

        index = 2;
        log.info(index + "");
        threadLocal.set("bbb");
        threadLocal.set(456);
        Thread.sleep(5000);
        log.info("threadLocal" + threadLocal.get());
        return RestResponse.success().put("threadLocal", threadLocal.get());
    }


    public static void main(String[] args) {
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

        String sb = "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 65535; i++) {
            sb += "i";
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start) + "ms");
        System.out.println(sb.length());
        System.out.println(sb);
        String s = "";
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
    }
}
