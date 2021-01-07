package com.example.demo.controller;

import com.example.demo.entity.RestResponse;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    private ThreadLocal<Object> threadLocal = new ThreadLocal<>();

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


}
