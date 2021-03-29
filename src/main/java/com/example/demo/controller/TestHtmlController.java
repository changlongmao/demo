package com.example.demo.controller;

import com.example.demo.entity.PushOrderEvent;
import com.example.demo.entity.RestResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.StringJoiner;
import java.util.UUID;

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
        byte[] bytes = Base64.decodeBase64("eyJqdGkiOiI4MjIwODQ0NDM3NzM3MTAzMzYiLCJ1c2VySWQiOjgyMjA4NDQ0Mzc3MzcxMDMzNiwidXNlck5hbWUiOiLluLjpvpnpo54iLCJwaG9uZSI6IjEzMDY3ODQ2MjcwIiwicGxhdGZvcm0iOiJQQ19DTElFTlQiLCJpcCI6IjM5LjE3MC40Mi44MiIsImRldmljZUlkIjoicWwiLCJ2ZXJzaW9uIjoiMC4wLjEiLCJyb290T3JnSWQiOjEsIm9yZ0lkIjo4MTQ4MzY2ODkzMzkwMDI5MTYsInN0YWZmSWQiOjgyMjA4NDQ0MDE4MzM4NjExNSwic3RhZmZOYW1lIjoi5bi46b6Z6aOeIiwic3RhZmZDb2RlIjoiSlkwNjg4NSIsInBlcnNvbklkIjowLCJwb3N0SWQiOjgyMjA4NDQ0MDUzMTUxMzM0NCwicG9zdE5hbWUiOiJKYXZh5byA5Y-R5bel56iL5biIIiwicm9sZU1hcCI6eyIwIjo3MDQwMTc4OTkwOTgwNTg3NTIsIjYiOjcxNTMwMjAyMDkwMjQwMDAwMCwiNyI6NzE0Nzg0NTE0NjQzMTc3NDcyLCI4Ijo4MTcwMzg4NjM1NjEyNDA1NzZ9LCJzdXBlckFkbWluIjowLCJzdGFmZkVpZCI6NzQ5OSwiY29tcGFueVR5cGUiOjEsImlhdCI6MTYxNjExODcyNywiZXhwIjoxNjE2MjA1MTI3fQ.Ztbp4shbY__Nmhh_BzGuBS0lVsgTbdgROdn3uvEkr5A");
        String s = new String(bytes);
        System.out.println(s);

        StringJoiner stringJoiner = new StringJoiner(",", "aaa", "bbb");
        stringJoiner.add("111");
        stringJoiner.add("222");


        System.out.println(stringJoiner);
    }
}
