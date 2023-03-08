package com.example.demo.testMain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ChangLF 2022-03-23
 */
public class TestHashMap {

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();

        map.put(2, "2020-02-01");
        map.put(3, "2021-01-01");


        map.values().stream().sorted(String::compareTo).forEach(System.out::println);
        map.keySet().stream().sorted(Integer::compareTo).forEach(System.out::println);

        char x = 1;
        char y = 2;
        System.out.println(x - y);
        System.out.println("2020-02-01".compareTo("2021-01-01"));

    }
}
