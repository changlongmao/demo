package com.example.demo.entity;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


public class RunnableDemo implements Runnable {

    private UserService userService;
    private int i;
    public static Map<String, String> map = new HashMap<>();

    public RunnableDemo(UserService userService, int i) {
        this.userService = userService;
        this.i = i;
    }

    @Override
    public void run() {
        List<User> users = new ArrayList<>();
        for (int j = 0; j < 100; j++) {
            User user = new User();
            user.setUsername("longMao" + i);
            user.setPassword("123");
            user.setRearName("龙猫" + i);
            user.setCreateTime(new Date());
            users.add(user);
        }
        userService.saveBatch(users);
    }
}
