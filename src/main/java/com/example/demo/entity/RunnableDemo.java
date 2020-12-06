package com.example.demo.entity;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RunnableDemo implements Runnable {

    private UserService userService;
    private int i;
    private long startTime;
    public static Map<String, String> map = new HashMap<>();

    public RunnableDemo(UserService userService, int i,long startTime) {
        this.userService = userService;
        this.i = i;
        this.startTime = startTime;
    }

    @Override
    public void run() {

        for (int j = 0; j < 10; j++) {
            User user = new User();
            user.setUsername("longMao" + i);
            user.setPassword("123");
            user.setRearName("龙猫" + i);
            user.setCreateTime(new Date());
            userService.save(user);
        }


        if (i == 9999){
            System.out.println("已保存数据"+i+" ,"+(System.currentTimeMillis()-startTime+"ms"));
        }
    }
}
