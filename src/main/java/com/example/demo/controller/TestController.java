package com.example.demo.controller;

import com.example.demo.util.JedisUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: TestController
 * @Description:
 * @Author: Chang
 * @Date: 2020/12/28 14:44
 **/

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private JedisUtil jedisUtil;


    public static void main(String[] args) {

    }
}
