package com.example.demo.controller;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.example.demo.common.RestResponse;
import com.example.demo.entity.TaskSettlementPriceReqDto;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.ExecutorService;

/**
 * @author ChangLF 2023-06-27
 */
@Slf4j
@RequestMapping("/testTTLExecutor")
@RestController
public class TestTTLExecutorController {

    public static TransmittableThreadLocal<Object> context = new TransmittableThreadLocal<>();
    public static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    @Resource
    private ExecutorService executorService;

    @Resource
    private UserService userService;

    @GetMapping("/testTread")
    public RestResponse testTread(@RequestParam String value) {

        context.set(value);
        threadLocal.set(value);
        log.info("设置值为{}", value);

        executorService.submit(() -> {
            Object o = context.get();
            log.info("获取值为{}", o);
        });

        userService.executeAsync();
        return RestResponse.success();
    }


}
