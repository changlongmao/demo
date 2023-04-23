package com.example.demo.testMain;

import com.example.demo.entity.User;
import com.example.demo.util.JsonUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChangLF 2022-08-07
 */
public class TestClass {

    public static final String a = "1";

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("aa", "bb");
        map.put("aa", "cc");
        System.out.println(JsonUtils.toJson(map));

        System.out.println(foo(4));
        System.out.println(new User().getClass().getClassLoader());
        System.out.println(new BigDecimal("1.40").compareTo(new BigDecimal("1.4")));
    }
    public static int foo(Integer x) {
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            System.out.println("exception"+x);
            return x;
        } finally {

            System.out.println("finally"+x);
        }
    }
}
