package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: TestHtmlController
 * @Description:
 * @Author: Chang
 * @Date: 2021/01/05 09:28
 **/

@Controller
@RequestMapping("/testHtml")
public class TestHtmlController {

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
