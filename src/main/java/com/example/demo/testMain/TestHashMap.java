package com.example.demo.testMain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChangLF 2022-03-23
 */
public class TestHashMap {

    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();

        Integer i = 1;
        map.put(i, 2);
        System.out.println(map.get(i));
        i = 2;
        System.out.println(map.get(i));

    }
}
