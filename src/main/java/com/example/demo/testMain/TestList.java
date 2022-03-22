package com.example.demo.testMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChangLF 2022-02-18
 */
public class TestList {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        Object[] strings = list.toArray();
        System.out.println(Arrays.toString(strings));

        System.out.println("aaa\taaa".replaceAll(" ",""));
    }
}
