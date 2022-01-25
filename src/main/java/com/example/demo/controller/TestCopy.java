package com.example.demo.controller;

import com.example.demo.entity.TestCopyOne;
import com.example.demo.entity.User;
import org.springframework.beans.BeanUtils;

/**
 * @author ChangLF 2022-01-11
 */
public class TestCopy {

    public static void main(String[] args) {
        TestCopyOne testCopyOne = new TestCopyOne();
        testCopyOne.setUsername("aa");
        testCopyOne.setOne("bbb");
        TestCopyOne b = new TestCopyOne();
        b.setOne("aaa");
        b.setPassword("aaa");
        test(testCopyOne, b);
        System.out.println(b);
    }

    public static <T extends User> void test(T a, T b) {
        BeanUtils.copyProperties(a, b);
    }
}
