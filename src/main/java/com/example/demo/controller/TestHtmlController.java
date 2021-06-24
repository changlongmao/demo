package com.example.demo.controller;

import com.example.demo.entity.PushOrderEvent;
import com.example.demo.entity.RestResponse;
import com.example.demo.entity.Singleton;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.util.StringUtils;
import com.example.demo.util.TimeUtil;
import org.apache.commons.codec.binary.Base64;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @ClassName: TestHtmlController
 * @Description:
 * @Author: Chang
 * @Date: 2021/01/05 09:28
 **/

@Controller
@RequestMapping("/testHtml")
public class TestHtmlController {
    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/push")
    public RestResponse pushOrder() {
        String orderNo = UUID.randomUUID().toString();

        // 发布事件
        publisher.publishEvent(new PushOrderEvent(this, orderNo, 666666L));

        return RestResponse.success(orderNo);
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "clf");
        model.addAttribute("age", 23);
        model.addAttribute("info", "我是一个爱学习的好青年");
        return "index";
    }

    @RequestMapping("/testModel")
    public ModelAndView testModel(){
        ModelAndView  modelAndView= new ModelAndView("index");
        return modelAndView;
    }

    public static void main(String[] args) {
        byte[] bytes = Base64.decodeBase64("eyJqdGkiOiI4MjIwODQ0NDM3NzM3MTAzMzYiLCJ1c2VySWQiOjgyMjA4NDQ0Mzc3MzcxMDMzNiwidXNlck5hbWUiOiLluLjpvpnpo54iLCJwaG9uZSI6IjEzMDY3ODQ2MjcwIiwicGxhdGZvcm0iOiJQQ19DTElFTlQiLCJpcCI6IjM5LjE3MC40Mi44MiIsImRldmljZUlkIjoicWwiLCJ2ZXJzaW9uIjoiMC4wLjEiLCJyb290T3JnSWQiOjEsIm9yZ0lkIjo4MTQ4MzY2ODkzMzkwMDI5MTYsInN0YWZmSWQiOjgyMjA4NDQ0MDE4MzM4NjExNSwic3RhZmZOYW1lIjoi5bi46b6Z6aOeIiwic3RhZmZDb2RlIjoiSlkwNjg4NSIsInBlcnNvbklkIjowLCJwb3N0SWQiOjgyMjA4NDQ0MDUzMTUxMzM0NCwicG9zdE5hbWUiOiJKYXZh5byA5Y-R5bel56iL5biIIiwicm9sZU1hcCI6eyIwIjo4MzM2NDExODE4MDcyMjY4ODAsIjYiOjgzMzY0MTE4MjM3NzY1MjIyNH0sInN1cGVyQWRtaW4iOjAsInN0YWZmRWlkIjo3NDk5LCJjb21wYW55VHlwZSI6MSwiaWF0IjoxNjIxODM0NzQ3LCJlbnRVc2VySWQiOjAsImVudEN1c3RJZCI6MCwiZW50Q29tcGFueUlkIjowLCJleHAiOjE2MjIwMDc1NDd9");
        String s = new String(bytes);
        System.out.println(s);

        ArrayList<String> strings = Lists.newArrayList("aa", "bb", "cc", "saa");
        System.out.println(strings.toString());
        System.out.println(0L == 0);z
    }
}
