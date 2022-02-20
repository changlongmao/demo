package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.entity.User;
import com.example.demo.util.DateUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author ChangLF 2022-01-28
 */
public class TestStack {

    public static void main(String[] args) {
        User user = new User();

        user.setRearName("bbb");

        User getUser = getUser(user);
        System.out.println("getUser:" + getUser);
        System.out.println("user:" + user);
    }

    public static User getUser(User user) {
        user.setId("aaa");
        user = new User("ccc");
        return user;
    }
}
