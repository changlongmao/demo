package com.example.demo.testMain;

import com.example.demo.exception.ApiException;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author ChangLF 2022-11-06
 */
public class TestPorp {

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            System.out.println(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(2);
        }).start();
        System.out.println(3);
        test();
    }

    public static void test() throws Exception {
        throw new RuntimeException("111");
    }
}

