package com.example.demo.controller;

import com.example.demo.entity.PushOrderEvent;
import com.example.demo.entity.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
