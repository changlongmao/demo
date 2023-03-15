package com.example.demo.testMain.spring;

import com.example.demo.controller.UserController;
import com.example.demo.service.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ChangLF 2023-03-10
 */
public class StartSpring {

    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-spring.xml");
        System.out.println("applicationContext启动");
        MessageService messageService = applicationContext.getBean(MessageService.class);
        System.out.println(messageService.getMessage());
        applicationContext.refresh();
    }
}
