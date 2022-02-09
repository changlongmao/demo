package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.entity.User;
import com.example.demo.util.DateUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * @author ChangLF 2022-01-28
 */
public class TestSortMain {

    public static void main(String[] args) {
        User user = new User();
        user.setCreateTime(new Date());

        System.out.println(DateUtil.getDateStr(new Date(1644373800000L), DateUtil.DateFormat.LONG_DATE_PATTERN_LINE));
    }
}
